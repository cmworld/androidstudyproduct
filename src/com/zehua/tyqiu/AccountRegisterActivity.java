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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.common.BaseActivity;
import com.app.common.CustomDialog;
import com.app.common.JsonPaserFactory;
import com.app.http.AsyncHttpResponseHandler;
import com.app.utils.Base64;
import com.app.utils.CommonUtils;

public class AccountRegisterActivity extends BaseActivity implements OnClickListener {
	
	private InputMethodManager manager;
	
	private EditText register_mobile;
	private EditText register_password;
	private EditText register_vaildcode;

	private Button submit_btn;
	private Button vaildcode_btn;
	private String mobile;
	private static String vaildCode;
	
	private Drawable Enable;
	private Drawable unEnable;
	private TimeCount timer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register_vaildmobile);
		
		ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setTitle(R.string.title_register);
		
		register_mobile = (EditText) findViewById(R.id.register_mobile);
		register_password = (EditText) findViewById(R.id.register_password);
		register_vaildcode = (EditText) findViewById(R.id.register_vaildcode);
		submit_btn = (Button) findViewById(R.id.submit_btn);
		submit_btn.setOnClickListener(this);
		
		vaildcode_btn = (Button) findViewById(R.id.vaildcode_btn);
		vaildcode_btn.setOnClickListener(this);
		
		Enable = vaildcode_btn.getBackground();
		unEnable = getResources().getDrawable(R.drawable.shape_unenable);		
		
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if (item.getItemId() == android.R.id.home) {
			Intent upIntent = new Intent(this, MainActivity.class);

			upIntent.addFlags(
						 Intent.FLAG_ACTIVITY_CLEAR_TOP |
						 Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(upIntent);
			finish();
			overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.vaildcode_btn:
			mobile = register_mobile.getText().toString();
			if(!CommonUtils.isMobileNO(mobile)){
				showToast("手机号码格式不正确");
				return;
			}
			         
			showDialog("将短信验证码发送至",mobile);
			break;
		case R.id.submit_btn:
			
			String input_password = register_password.getText().toString();
			String input_vaildcode = register_vaildcode.getText().toString();
			
			Log.i("register", "input_vaildcode = "+input_vaildcode+ " ,vaildcode="+vaildCode);
			if(!input_vaildcode.equals(vaildCode)){
				showToast("验证码不正确");
				return;
			}
			
			if(input_password.length() < 6){
				showToast("密码至少大于或等于6个字符");
				return;
			}
			
			StringBuffer sb=new StringBuffer();
			sb.append(mobile);
			sb.append("\t");
			sb.append(input_password);
			sb.append("\t");
			sb.append(input_vaildcode);
			
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
			
			TyqiuSdk.get("passport.account.register",mParams, new RegsiterHttpHandle());
			
			break;
		default:
			break;
		}
	}
	
	public void showDialog(String title,String message) {
	    Dialog dialog = null;

        CustomDialog.Builder customBuilder = new
        CustomDialog.Builder(AccountRegisterActivity.this);
        customBuilder.setTitle(title).setMessage(message)
        	.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	Log.i("dialog","cancel clicked");
                	dialog.dismiss();
                }
            })
            .setPositiveButton("确认",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	Log.i("dialog","confirm clicked");
                	
        			Map<String,String> mParams = new HashMap<String,String>();
        			mParams.put("mobile",mobile);
        			TyqiuSdk.get("system.sms.getcode",mParams, new VaildCodeHttpHandle());
                    dialog.dismiss();
                    
                    timer = new TimeCount(60000, 1000);
                    timer.start();
                }
            });
       
        dialog = customBuilder.create();
        dialog.show();
	}
	
	public class VaildCodeHttpHandle extends AsyncHttpResponseHandler{ 
		
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
					throw new Exception("获取验证码失败，请重试");
				}
				
				vaildCode = res.optString("data");
				Log.i("vaildcode","vaildCode = "+vaildCode);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showToast(e.getMessage());
				
				if(timer != null){
					timer.cancel();
					vaildcode_btn.setText(R.string.register_sms);
					vaildcode_btn.setBackgroundDrawable(Enable);
					vaildcode_btn.setClickable(true);					
				}
				
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
	
	public class RegsiterHttpHandle extends AsyncHttpResponseHandler{ 
		
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
					throw new Exception("注册失败");
				}
				
				JSONObject userInfo = res.optJSONObject("data");
				
				List<Map<?, ?>> rstList = new ArrayList<Map<?, ?>>();
				JsonPaserFactory.JsonObject2HashMap(userInfo, rstList);
				
				Log.i("userinfo","userinfo = "+userInfo);
				
				accountMr.setData(rstList);
				
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				bundle.putString("username", accountMr.get("username"));
				intent.putExtras(bundle);		
				
				AccountRegisterActivity.this.setResult(MainActivity.REGISTER_REQCODE,intent);
				AccountRegisterActivity.this.finish();

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
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		
		@Override
		public void onFinish() {//计时完毕时触发
			vaildcode_btn.setText(R.string.register_sms);
			vaildcode_btn.setBackgroundDrawable(Enable);
			vaildcode_btn.setClickable(true);
		}
		
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			vaildcode_btn.setClickable(false);
			vaildcode_btn.setBackgroundDrawable(unEnable);
			vaildcode_btn.setText("获取验证码 " + millisUntilFinished /1000+"秒");
		}
	}	
}
