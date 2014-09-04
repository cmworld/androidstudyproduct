package com.app.utils;

import java.util.Map;
import java.util.Set;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DBPreferenceUtil {
 
    private SharedPreferences sp;
    private Editor editor;
    private String name = "tyqiu_global";
    private int mode = Context.MODE_PRIVATE;
 
    public DBPreferenceUtil(Context context) {
        this.sp = context.getSharedPreferences(name, mode);
        this.editor = sp.edit();
    }

    public DBPreferenceUtil(Context context, String name, int mode) {
        this.sp = context.getSharedPreferences(name, mode);
        this.editor = sp.edit();
    }
 
    public void add(Map<String, String> map) {
        Set<String> set = map.keySet();
        for (String key : set) {
            editor.putString(key, map.get(key));
        }
        editor.commit();
    }
 
    public void deleteAll() throws Exception {
        editor.clear();
        editor.commit();
    }
 
    public void delete(String key){
        editor.remove(key);
        editor.commit();
    }
 
    public String get(String key){
        if (sp != null) {
            return sp.getString(key, "");
        }
        
        return "";
    }
    
    public Map<String, ?> getAll(){
    	
        if (sp != null) {
            return sp.getAll();
        }
        
        return null;
    }
 
    public Editor getEditor() {
        return editor;
    }
}