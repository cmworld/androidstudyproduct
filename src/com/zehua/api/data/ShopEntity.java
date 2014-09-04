package com.zehua.api.data;

import org.json.JSONObject;

import com.app.common.BaseEntity;


public class ShopEntity extends BaseEntity {

	public int id;
	public String shop_name;
	public String city;
	public String city_id;
	/***/
	public String logo;
	public String address;
	public String site_name;
	public String phone;
	public String mobile;
	public String deliver_desc;
	public String description;
	public String cate_id;
	public String cate_txt;
	public String invoice;

	public String promotion_info;
	public String invoice_min_amount;
	public String has_agent_fee;
	public String agent_fee;
	
	public String lng;
	public String lat;
	public String serving_time;
	public String uid;
	public String username;
	public String order_mode;
	public String add_time;
	public String is_bookable;
	public String busy_level;
	public String ip;
	public String status;
	public String rating;
	public String buy_num;
	public String speed;
	public String likes;
	public String price;
	public String opt;
	public String book_time;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		id = json.optInt("id", 0);
		shop_name = json.optString("shop_name", null);
		city = json.optString("city", null);
		city_id = json.optString("city_id", null);
		logo = json.optString("logo", null);
		address = json.optString("address", null);
		site_name = json.optString("site_name", null);

		phone = json.optString("phone");
		mobile = json.optString("mobile", null);
		deliver_desc = json.optString("deliver_desc", null);
		description = json.optString("description", null);
		promotion_info = json.optString("promotion_info", null);
		
		
		cate_id = json.optString("cate_id", null);
		cate_txt = json.optString("cate_txt", null);

		invoice = json.optString("invoice", null);
		invoice_min_amount = json.optString("invoice_min_amount", null);
		has_agent_fee = json.optString("has_agent_fee", null);
		agent_fee = json.optString("agent_fee", null);
		lng = json.optString("lng", null);
		lat = json.optString("lat", null);
		serving_time = json.optString("serving_time", null);
		uid = json.optString("uid", null);
		username = json.optString("username", null);
		order_mode = json.optString("order_mode", null);
		add_time = json.optString("add_time", null);
		is_bookable = json.optString("is_bookable", null);
		busy_level = json.optString("busy_level", null);
		ip = json.optString("ip", null);
		status = json.optString("status", null);
		rating = json.optString("rating", null);
		buy_num = json.optString("buy_num", null);
		speed = json.optString("speed", null);
		likes = json.optString("likes", null);
		price = json.optString("price", null);
		opt = json.optString("opt", null);
		book_time = json.optString("book_time", null);

		//"scope": "[{\"color\":\"#35D5B2\",\"type\":\"polygon\",\"region\":{\"point\":[\"121.511278,31.235041\",\"121.536255,31.225867\",\"121.535933,31.220747\",\"121.513467,31.221096\"],\"path\":\"\"},\"system_weight\":0,\"weight_kind\":1,\"price\":\"20\"}]",

	}

}
