package com.zehua.api.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.app.common.BaseEntity;


public class ItemGroupList extends BaseEntity  {
	
	public List<ItemEntity> mitemList = new ArrayList<ItemEntity>();
	public List<CateEntity> mcateList = new ArrayList<CateEntity>();
	public List<BaseEntity> groupList = new ArrayList<BaseEntity>();
	
	@Override
	public void paser(JSONObject json) throws JSONException, Exception{

		JSONObject data=json.getJSONObject("data");
		
		if(data!=null){
			
			int size;
			//cate
			JSONArray cates=data.getJSONArray("cates");
			
			CateEntity entity = null;
			size =cates.length();
			for(int i =0;i<size;i++)
			{
				entity=new CateEntity();
				entity.paser(cates.getJSONObject(i));
				mcateList.add(entity);
			}
			
			//item
			JSONArray items=data.getJSONArray("items");
			
			ItemEntity itementity = null;
			size =items.length();
			for(int i =0;i<size;i++)
			{
				itementity=new ItemEntity();
				itementity.paser(items.getJSONObject(i));
				mitemList.add(itementity);
			}
			
			//itemGroup
			for(int j =0 ; j < mcateList.size(); j++ ){
				
				groupList.add(mcateList.get(j));
				for(int i =0 ;i < mitemList.size(); i++ ){
					if( mitemList.get(i).cate_id.equals(mcateList.get(j).id)){
						groupList.add(mitemList.get(i));
					}
				}
			}
		}
	}
	
	public List<ItemEntity> getContents(){
		return mitemList;
	}
	
	public List<CateEntity> getCates(){
		return mcateList;
	}
	
	public List<BaseEntity> getGroupList(){
		return groupList;
	}
}
