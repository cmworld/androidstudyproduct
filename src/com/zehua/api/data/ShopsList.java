package com.zehua.api.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.common.BaseEntity;


public class ShopsList extends BaseEntity  {
	
	public static List<ShopEntity> mList = new ArrayList<ShopEntity>();
	
	@Override
	public void paser(JSONObject json) throws JSONException, Exception{

		JSONArray arr=json.optJSONArray("data");
		
		if(arr!=null){
			ShopEntity entity =null;
			int size =arr.length();
			for(int i =0;i<size;i++)
			{
				entity=new ShopEntity();
				entity.paser(arr.getJSONObject(i));
				mList.add(entity);
			}
		}
	}
	
	public List<ShopEntity> getContents(){
		return mList;
	}
}
