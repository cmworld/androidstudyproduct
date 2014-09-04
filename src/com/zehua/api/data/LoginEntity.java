package com.zehua.api.data;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.common.BaseEntity;

public class LoginEntity extends BaseEntity {

	public int status;
	public String msg;
	public String uid;
	public String username;
	public String token;
	public String code;
	public String faiMsg;
	@Override
	public void paser(JSONObject json) throws Exception {
		status=json.optInt("status");
		msg=json.optString("msg");
		JSONObject dataJSON=json.optJSONObject("data");
		if(dataJSON!=null){
			uid=dataJSON.optString("uid");
			username=dataJSON.optString("username");
			token=dataJSON.optString("token");
			code=dataJSON.optString("code");
			faiMsg=dataJSON.optString("faiMsg");
		}
	}

}
