package com.app.common;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.app.sdk.TyqiuHttpClient;
import com.zehua.tyqiu.R;

public abstract class BaseActivity extends SherlockFragmentActivity {
	
	private static Context mContext;
	
	//记录当前的状态
	public int i_curState;

	//当前Activity处于开始状态
	public static final int STATE_START = 1;
	//当前Activity处于暂停状态
	public static final int STATE_PAUSE = 2;
	//当前Activity处于连接数据状态
	public static final int STATE_CONNECTING = 3;
	// 当前Activity处于显示对话框状态
	public static final int STATE_SHOW_DIALOG = 4;
	//当前Activity处于停止状态
	public static final int STATE_STOP = 5;

	//当前请求的线程数 
	public int i_curReqTimes;

	private BaseApplication mBaseApp;
	
	public static TyqiuHttpClient TyqiuSdk;
	
	public static boolean envLoaded = false;
	
	public static AccountMr accountMr;
	public static SettingMr settingMr;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mBaseApp = (BaseApplication) getApplication();
		mBaseApp.addActivitToStack(this);
		
		mContext = this;
		TyqiuSdk  = new TyqiuHttpClient(this);

		accountMr = new AccountMr(this);
		settingMr = new SettingMr(this);
		
		if(!envLoaded){
			loadEnvSetting();
		}
	}
	
	private void loadEnvSetting(){

		/*
		Map<String,String> mParams = new HashMap<String,String>();

		TyqiuSdk.get("passport.order.list",mParams, new AsyncHttpResponseHandler(){
			
			public void onFinish() {
				super.onFinish();
				Log.i("networkhttp","AsyncHttpResponseHandler -- onFinish --> init ");
			
			}

			@Override
			public void onSuccess(HttpEntity content, int reqType) {
				super.onSuccess(content, reqType);
				
				Log.i("networkhttp","AsyncHttpResponseHandler -- onSuccess --> init ");
				
				try {
					Log.i("networkhttp",EntityUtils.toString(content));
					
					JSONObject res = JsonPaserFactory.paserObj(content);
					
					if(JsonPaserFactory.isNull(res)){
						throw new Exception("null");
					}
					
					if(JsonPaserFactory.isErr(res)){
						throw new Exception(res.optString("data"));
					}
					
					OrdersList OrdersListRes =  new OrdersList();
					OrdersListRes.paser(res);
					contents = OrdersListRes.getContents();
					
					if (contents != null && contents.size() > 0) {
						Log.i("networkhttp","mAdapter -- setData --> init ");
						
		                mAdapter.setData(contents);
		                mAdapter.notifyDataSetChanged();							
					}

					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MainActivity.showToast(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MainActivity.showToast(e.getMessage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MainActivity.showToast(e.getMessage());
					
				}	
			}

			@Override
			public void onFailure(Throwable error, String content, int reqType) {
				super.onFailure(error, content, reqType);
				Log.i("networkhttp","AsyncHttpResponseHandler -- onFailure --> init ");
				if (error != null) {
					if (error instanceof ConnectException || error instanceof IOException) {
						//i_errRes = R.string.MSG_CONNECTION_ERR;
						MainActivity.showToast(R.string.MSG_CONNECTION_ERR);
					} else if (error instanceof TimeoutException) {
						//i_errRes = R.string.MSG_TIME_OUT;
						MainActivity.showToast(R.string.MSG_TIME_OUT);
					}
					error.printStackTrace();
				}
	
			}
			
		});		
		*/
		envLoaded = true;
	}

	private String str_err;
	private int i_errRes;	
	
	public void onReqResponse(String data,Object o, int methodId) {}

	public void onErrResponse(String data,Throwable error, String content, int type) {
		i_errRes = -1;
		str_err = null;
		if (error != null) {
			if (error instanceof ConnectException
					|| error instanceof IOException) {
				i_errRes = R.string.MSG_CONNECTION_ERR;
			} else if (error instanceof TimeoutException) {
				i_errRes = R.string.MSG_TIME_OUT;
			}
			error.printStackTrace();
		}
		if (i_errRes > 0)
			showDialog(101);
	}

	public void onErrResponse(Throwable error, String content,boolean isBackGroundThread) {}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		i_curState = STATE_START;
	}

	@Override
	protected void onStop() {
		i_curState = STATE_STOP;
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mBaseApp.addActivitToStack(this);
		super.onDestroy();
	}

	public static void showToast(String s) {
		Toast toast = Toast.makeText(mContext, s, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showToast(int s) {
		Toast toast = Toast.makeText(mContext, s, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}	
	
	public void quit() {
		onQuit();
		mBaseApp.quit();
	}

	public void onQuit() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

}
