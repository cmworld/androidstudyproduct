package com.zehua.api.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.common.BaseEntity;


public class AddressList extends BaseEntity  {
	
	public static List<AddressEntity> mList = new ArrayList<AddressEntity>();
	
	@Override
	public void paser(JSONObject json) throws JSONException, Exception{

		JSONArray arr=json.optJSONArray("data");
		
		if(arr!=null){
			AddressEntity entity =null;
			int size =arr.length();
			for(int i =0;i<size;i++)
			{
				entity=new AddressEntity();
				entity.paser(arr.getJSONObject(i));
				mList.add(entity);
			}
		}
	}
	
	public List<AddressEntity> getContents(){
		return mList;
	}
}