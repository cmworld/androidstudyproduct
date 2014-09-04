package com.app.common;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseEntity {

	public final static String KEY_RESULT="result";
	public final static String KEY_DATA="data";

	
	public  abstract void paser(JSONObject json) throws Exception ;

    public String err;
	public static  void paserKeys(JSONArray arr,LinkedHashMap<String, String> map)	throws Exception
	{	
		JSONArray keys=arr.getJSONArray(0);
		JSONArray values=arr.getJSONArray(1);
		int size =keys.length();	
		for(int i=0;i<size;i++)
		{
			map.put(keys.getString(i), values.getString(i));
		}
	}
}
