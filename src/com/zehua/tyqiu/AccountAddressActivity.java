package com.zehua.tyqiu;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.app.common.BaseActivity;
import com.app.common.JsonPaserFactory;
import com.app.http.AsyncHttpResponseHandler;
import com.zehua.api.data.AddressEntity;
import com.zehua.api.data.AddressList;
import com.zehua.api.data.OrderEntity;
import com.zehua.api.data.OrdersList;
import com.zehua.tyqiu.adapter.AddressListAdapter;
import com.zehua.tyqiu.adapter.OrderListAdapter;

public class AccountAddressActivity extends BaseActivity {

	private ListView address_list;
	private AddressListAdapter mAdapter;
	private List<AddressEntity> contents;	
	
    public static void start(Context context) {
        Intent intent = new Intent(context, AccountAddressActivity.class);
        context.startActivity(intent);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_address);
		
		
		ActionBar ab = getSupportActionBar();
        ab.setDisplayUseLogoEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setTitle(R.string.title_address);
        
        address_list= (ListView)findViewById(R.id.account_address_list);
        
        if(mAdapter == null){
        	contents = new ArrayList<AddressEntity>();     	
        	mAdapter = new AddressListAdapter(this,contents);
        }
        
        address_list.setAdapter(mAdapter);
        address_list.setOnItemClickListener(new OnItemClickListener() {  

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Log.i("item",position+"");
			}
        });
        
        initAddressData();
	}
	
	
	public void initAddressData(){
		Map<String,String> mParams = new HashMap<String,String>();

		TyqiuSdk.get("passport.consignee.list",mParams, new AsyncHttpResponseHandler(){
			
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
					
					AddressList AddressListRes =  new AddressList();
					AddressListRes.paser(res);
					contents = AddressListRes.getContents();
					
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
	}	
	
}
