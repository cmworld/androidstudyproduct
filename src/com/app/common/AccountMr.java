package com.app.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.app.utils.DBPreferenceUtil;

public class AccountMr {
	private DBPreferenceUtil DB;
	private String name = "user_info";
	private int mode = Context.MODE_PRIVATE;
	private static HashMap<String, String> data = new  HashMap<String, String>();
	
	public AccountMr(Context context){
		DB = new DBPreferenceUtil(context,name,mode);
		data = (HashMap<String, String>) DB.getAll();
	}
	
	public void setData(List<Map<?, ?>> rstList){
		for(int i=0; i< rstList.size(); i++){
			set((HashMap<String, String>) rstList.get(i));
		}
	}
	
	public boolean logout(){
		
		try {
			
			data.clear();
			DB.deleteAll();
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
	}
	
	public boolean isLogin(){
		return true;
	}
	
	public HashMap<String, String> getAll(){
		return data;
	}
	
	public String get(String key){
		return data.get(key);
	}
	
	public void set(String key,String value){
		HashMap<String, String> map= new HashMap<String, String>();
		map.put(key, value);
		data.put(key, value);
		
		DB.add(map);
	}
	
	public void set(HashMap<String, String> map){
		Set<String> set = map.keySet();
        for (String key : set) {
        	data.put(key, map.get(key));
        }
        
		DB.add(map);
	}
}
