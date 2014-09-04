package com.zehua.api.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.app.common.BaseEntity;

public class AddressEntity extends BaseEntity {
	public int addr_id;
	public String address;
	public String consignee;

	@Override
	public void paser(JSONObject json) throws Exception {
		
		addr_id = json.optInt("addr_id", 0);
		address = json.optString("address", null);
		consignee = json.optString("consignee", null);
	}

}
