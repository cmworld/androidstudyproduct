package com.app.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonPaserFactory {

	public static JSONObject paserObj(HttpEntity en) {
		
		String s = null;
		JSONObject jsonObject = null;
		
		try {
			s = EntityUtils.toString(en);

			if (s == null || s.trim().toString().equals(""))
				return null;			
			
			jsonObject = new JSONObject(s);
		
			if(JsonPaserFactory.isNull(jsonObject)){
				throw new Exception("null");
			}
			
			if(JsonPaserFactory.isErr(jsonObject)){
				throw new Exception(jsonObject.optString("data"));
			}		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return jsonObject;
	}

	public static boolean isErr(JSONObject jsonObject) {
		// 错误ID及错误说明（当其存在，result值无效）
		int status = jsonObject.optInt("status");
		if (status != 0) {
			return true;
		}
		return false;
	}

	public static boolean isNull(JSONObject jsonObject) {
		String ss = jsonObject.optString(BaseEntity.KEY_DATA, null);
		if (ss == null)
			return true;
		if (ss.equals("{}") || ss.equals("[]"))
			return true;
		return false;

	}
	
	
    public static void JsonObject2HashMap(JSONObject jo, List<Map<?, ?>> rstList) {  
        for (Iterator<String> keys = jo.keys(); keys.hasNext();) {  
            try {  
                String key1 = keys.next();  

                if (jo.get(key1) instanceof JSONObject) {  
  
                    JsonObject2HashMap((JSONObject) jo.get(key1), rstList);  
                    continue;  
                }  
                if (jo.get(key1) instanceof JSONArray) {  
                    JsonArray2HashMap((JSONArray) jo.get(key1), rstList);  
                    continue;  
                }  

                json2HashMap(key1, jo.get(key1), rstList);  
  
            } catch (JSONException e) {  
                e.printStackTrace();  
            }  
  
        }  
  
    }  
    public static void JsonArray2HashMap(JSONArray joArr, List<Map<?, ?>> rstList) {  
        for (int i = 0; i < joArr.length(); i++) {  
            try {  
                if (joArr.get(i) instanceof JSONObject) {  
  
                    JsonObject2HashMap((JSONObject) joArr.get(i), rstList);  
                    continue;  
                }  
                if (joArr.get(i) instanceof JSONArray) {  
  
                    JsonArray2HashMap((JSONArray) joArr.get(i), rstList);  
                    continue;  
                }  

            } catch (JSONException e) {  
                e.printStackTrace();  
            }
        }
    }  
  
    public static void json2HashMap(String key, Object value, List<Map<?, ?>> rstList) {  
        HashMap<String, Object> map = new HashMap<String, Object>();  
        map.put(key, value);  
        rstList.add(map);  
    }  	
}
