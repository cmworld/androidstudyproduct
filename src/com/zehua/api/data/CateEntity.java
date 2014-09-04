package com.zehua.api.data;

import org.json.JSONObject;

import com.app.common.BaseEntity;
import com.zehua.tyqiu.R.string;

public class CateEntity extends BaseEntity{

    public String id;
    public String shop_id;
    public String name;
    public String status;
    public String item_num;
    public String ordid;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		// TODO Auto-generated method stub
		
		id = json.optString("id",null);
		shop_id = json.optString("shop_id",null);
		name = json.optString("name",null);
		status = json.optString("status",null);
		item_num = json.optString("item_num",null);
		ordid = json.optString("ordid",null);
	}
	
}
