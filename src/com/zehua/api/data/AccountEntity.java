package com.zehua.api.data;

import org.json.JSONObject;

import com.app.common.BaseEntity;


public class AccountEntity extends BaseEntity {

	public int id;
	public String cate_id;
	public String shop_id;
	public String title;
	public String intro;
	
	/***/
	public String img;
	public String cost_price;
	public String price;
	public String weight;
	public String hits;
	public String likes;
	public String comments;
	public String add_time;
	public String comments_cache;

	public String ordid;
	public String status;
	public String rank;
	public String buy_count;
	
	public String buy_m_count;
	public String rating;
	public String store;
	public String freeze;
	public String hot;
	public String _new;
	public String recommend;
	public String buy_num;
	
	@Override
	public void paser(JSONObject json) throws Exception {
		id = json.optInt("id", 0);
		title = json.optString("title", null);
		intro = json.optString("intro", null);
		cate_id = json.optString("cate_id", null);
		shop_id = json.optString("shop_id", null);
		img = "http://192.168.1.200/data/upload/item/" + json.optString("img", null);
		cost_price = json.optString("cost_price", null);
		price = json.optString("price", null);

		weight = json.optString("weight");
		hits = json.optString("hits", null);
		likes = json.optString("likes", null);
		comments = json.optString("comments", null);
		add_time = json.optString("add_time", null);
		comments_cache = json.optString("comments_cache", null);

		ordid = json.optString("ordid", null);
		status = json.optString("status", null);
		rank = json.optString("rank", null);
		buy_count = json.optString("buy_count", null);
		buy_m_count = json.optString("buy_m_count", null);
		rating = json.optString("rating", null);
		store = json.optString("store", null);
		freeze = json.optString("freeze", null);
		hot = json.optString("hot", null);
		_new = json.optString("new", null);
		recommend = json.optString("recommend", null);
		buy_num = json.optString("buy_num", null);
		
	}

}
