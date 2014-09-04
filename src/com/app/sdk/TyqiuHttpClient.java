package com.app.sdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.app.http.AsyncHttpClient;
import com.app.http.AsyncHttpResponseHandler;
import com.app.http.RequestParams;
import com.app.utils.DeviceInfo;

public class TyqiuHttpClient {
	
	private static Context AppContext;
	
	public AsyncHttpClient mHttpClient;
	public RequestParams mParams;

	private static String mToken;
	public static String mDeviceId;
	public static String timestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

	public static final String version ="1.0";
	public static final String platform ="android";
	
	public static final String APIKEY ="3542e676b4c80983f6131cdfe577ac9b";
	public static final String ApiURL ="http://115.28.90.137:81/rest";

	public TyqiuHttpClient(Context context){
		AppContext = context;
		mDeviceId = DeviceInfo.getUUID();
		mHttpClient = new AsyncHttpClient();
		mHttpClient.setTimeout(5*1000);
	}
	
	public boolean get(String api,Map<String, String> params, AsyncHttpResponseHandler responseHandler){
		params.put("method", api);
		sendRequest(ApiURL,"get",params,responseHandler);
		return true;
	}
	
	public boolean post(String api,Map<String, String> params, AsyncHttpResponseHandler responseHandler){
		params.put("method", api);
		sendRequest(ApiURL,"post",params,responseHandler);
		return true;
	}
	
	public boolean sendRequest(String url,String method, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
		
		Map<String, String> requestParams = getSystemRequestMap();
		requestParams.putAll(params);
		
		String sign = getRequestSign(requestParams);
		requestParams.put("sign",sign);
		
		if (!DeviceInfo.isNetWorkEnable(AppContext)) {
			return false;
		}
		
		if(method.equals("get")){
			mHttpClient.post(url, new RequestParams(requestParams), responseHandler,0);
		}else{
			mHttpClient.get(url, new RequestParams(requestParams), responseHandler,1);
		}
		Log.i("networking",url);
		Log.i("networking",requestParams.toString());
		return true;
	}
	
	public String getRequestSign(Map<String, String> map) {
		if(map==null) return "";
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
				map.entrySet());

		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1,
					Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < infoIds.size(); i++) {
			Map.Entry<String, String> map1 = (Map.Entry<String, String>) infoIds
					.get(i);
			builder.append(map1.getKey() + map1.getValue());
		}
		
		builder.append(APIKEY);
		return MD5Util.md5(builder.toString());
	}

	public Map<String, String> getSystemRequestMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.clear();
		map.put("uuid", mDeviceId);
		map.put("v", version);
		map.put("_timestamp", timestamp);
		map.put("platform", "platform");
		return map;
	}
	
	public void onReqResponse(String data, Object o, int methodId) {
		/*if (o != null && ((BaseEntity) o).err != null) {
			onErrResponse(data, null, ((BaseEntity) o).err, methodId);
		}
		
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
		removeDialog(DialogRes.DIALOG_ID_NET_CONNECT);
		*/
	}
	
	public void onErrResponse(String data, Throwable error, String content, int type) {
		
		/*
		super.onErrResponse(data, error, content, type);
		UMENG_MAP.clear();
		if (error != null) {
			error.printStackTrace();
			if (error instanceof org.apache.http.conn.ConnectTimeoutException) {
				UMENG_MAP.put("http", "timeout");
			} else if (error instanceof ConnectException) {
				UMENG_MAP.put("http", "failed");
			}
		} else {
			UMENG_MAP.put("failed", "failed");
		}
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
		*/
	}
}
