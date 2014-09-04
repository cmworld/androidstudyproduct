package com.zehua.api.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.common.BaseEntity;


public class OrdersList extends BaseEntity  {
	
	public static List<OrderEntity> mList = new ArrayList<OrderEntity>();
	
	@Override
	public void paser(JSONObject json) throws JSONException, Exception{

		JSONArray arr=json.optJSONArray("data");
		
		if(arr!=null){
			OrderEntity entity =null;
			int size =arr.length();
			for(int i =0;i<size;i++)
			{
				entity=new OrderEntity();
				entity.paser(arr.getJSONObject(i));
				mList.add(entity);
			}
		}
	}
	
	public List<OrderEntity> getContents(){
		return mList;
	}
}