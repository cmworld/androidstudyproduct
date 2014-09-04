package com.zehua.api.data;

import org.json.JSONObject;

import com.app.common.BaseEntity;


public class OrderItemEntity extends BaseEntity {

	public int id;
	public String order_id;
	public String item_id;
	public String dly_status;
	public String type_id;
	public String bn;
	public String name;
	public String cost;
	public String price;
	public String amount;
	public String score;
	public String nums;
	public String minfo;
	public String sendnum;
	public String addon;
	public String is_type;
	public String point;
	public String rating;
	
	public String comment;
	public String is_comment;

	@Override
	public void paser(JSONObject json) throws Exception {
		id = json.optInt("id", 0);
		order_id = json.optString("order_id", null);
		item_id = json.optString("item_id", null);
		dly_status = json.optString("dly_status", null);
		type_id = json.optString("type_id", null);
		bn = json.optString("bn", null);
		name = json.optString("name", null);

		cost = json.optString("cost");
		price = json.optString("price", null);
		amount = json.optString("amount", null);
		score = json.optString("score", null);
		nums = json.optString("nums", null);
		minfo = json.optString("minfo", null);

		sendnum = json.optString("sendnum", null);
		addon = json.optString("addon", null);
		is_type = json.optString("is_type", null);
		point = json.optString("point", null);
		rating = json.optString("rating", null);
		comment = json.optString("comment", null);
		is_comment = json.optString("is_comment", null);
	}
}
