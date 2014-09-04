package com.zehua.tyqiu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.app.common.BaseActivity;
import com.app.common.JsonPaserFactory;
import com.app.http.AsyncHttpResponseHandler;
import com.app.utils.Base64;
import com.app.utils.CommonUtils;
import com.zehua.tyqiu.AccountRegisterActivity.RegsiterHttpHandle;

public class AccountLoginActivity extends BaseActivity implements OnClickListener {
	
	private EditText login_username;
	private EditText login_password;	
	
	private TextView register_btn;
	private TextView findpass_btn;
	private TextView submit_btn;
	private InputMethodManager manager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_account_login);
		
		ActionBar actionBar = getActionBar();  
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.title_login);
		
		login_username= (EditText)findViewById(R.id.login_username);
		login_password= (EditText)findViewById(R.id.login_password);
		
		register_btn= (TextView)findViewById(R.id.login_register_btn);
		register_btn.setOnClickListener(this);

		findpass_btn= (TextView)findViewById(R.id.login_findpass_btn);
		findpass_btn.setOnClickListener(this);
		
		submit_btn= (TextView)findViewById(R.id.login_submit_btn);
		submit_btn.setOnClickListener(this);
		
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			Intent upIntent = new Intent(this, MainActivity.class);

			upIntent.addFlags(
						 Intent.FLAG_ACTIVITY_CLEAR_TOP |
						 Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(upIntent);
			finish();
			overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in );
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override  
	 public boolean onTouchEvent(MotionEvent event) {  
	  // TODO Auto-generated method stub  
	  if(event.getAction() == MotionEvent.ACTION_DOWN){  
	     if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){  
	       manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
	     }  
	  }  
	  return super.onTouchEvent(event);  
	 }	
	
	@Override
	public void onClick(View v) {
		Intent mintent;
		switch (v.getId()) {
			case R.id.login_register_btn:
				mintent = new Intent(AccountLoginActivity.this,AccountRegisterActivity.class);
				startActivity(mintent);
				break;
				
			case R.id.login_findpass_btn:
				mintent = new Intent(AccountLoginActivity.this,AccountRegisterActivity.class);
				startActivity(mintent);
				break;

			case R.id.login_submit_btn:

				String username = login_username.getText().toString();
				String password = login_password.getText().toString();

				if(username == null || !CommonUtils.isMobileNO(username)){
					showToast("用户名格式不正确");
					return;
				}
				
				if(password == null || password.length() == 0){
					showToast("请输入密码");
					return;
				}
				
				StringBuffer sb=new StringBuffer();
				sb.append(username);
				sb.append("\t");
				sb.append(password);
				
				String authorization = null;
				try {
					byte[] midbytes = sb.toString().getBytes("UTF8");
					byte[] authorization_byte = Base64.encode(midbytes,0);
					authorization = new String(authorization_byte,"UTF-8");	
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Map<String,String> mParams = new HashMap<String,String>();
				mParams.put("authorization",authorization);
				
				TyqiuSdk.get("passport.account.login",mParams, new LoginHttpHandle());
				
				break;		
			default:
				break;
		}
	}
	
	public class LoginHttpHandle extends AsyncHttpResponseHandler{ 
		
		public void onFinish() {
			super.onFinish();

		}

		@Override
		public void onSuccess(HttpEntity content, int reqType) {
			super.onSuccess(content, reqType);
			
			try {
				Log.i("networkhttp",EntityUtils.toString(content));
				
				JSONObject res = JsonPaserFactory.paserObj(content);
				
				if(res == null){
					throw new Exception("用户名或密码错误");
				}
				
				JSONObject userInfo = res.optJSONObject("data");
				
				List<Map<?, ?>> rstList = new ArrayList<Map<?, ?>>();
				JsonPaserFactory.JsonObject2HashMap(userInfo, rstList);
				
				Log.i("userinfo","userinfo = "+userInfo);
				
				accountMr.setData(rstList);
				
				AccountLoginActivity.this.setResult(MainActivity.REGISTER_REQCODE);
				AccountLoginActivity.this.finish();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showToast(e.getMessage());
			}
		}

		@Override
		public void onFailure(Throwable error, String content, int reqType) {
			super.onFailure(error, content, reqType);
			if (error != null) {
				if (error instanceof ConnectException || error instanceof IOException) {
					//i_errRes = R.string.MSG_CONNECTION_ERR;
					showToast(R.string.MSG_CONNECTION_ERR);
				} else if (error instanceof TimeoutException) {
					//i_errRes = R.string.MSG_TIME_OUT;
					showToast(R.string.MSG_TIME_OUT);
				}
				error.printStackTrace();
			}

		}
	}	
}
