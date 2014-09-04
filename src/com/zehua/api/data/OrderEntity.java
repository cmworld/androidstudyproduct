package com.zehua.api.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.app.common.BaseEntity;

public class OrderEntity extends BaseEntity {
	public int order_id;
	public String uid;
	public String shop_name;
	public String shop_id;
	public String confirm;
	public String is_delivery;
	public String payment;
	public String itemnum;
	public String note;
	public String comment_habbit;
	public String createtime;
	
	public String status;
	public String status_tip;
	public String pay_status;
	public String ship_status;
	public String user_status;
	public String comment_status;

	public String ship_name;
	public String ship_addr;
	public String ship_mobile;
	public String ship_tel;
	public String ship_time;
	
	public String total_amount;
	public String final_amount;
	public String pmt_amount;
	public String payed;
	
	public List<OrderItemEntity> items;
	
	@Override
	public void paser(JSONObject json) throws Exception {
	
		order_id = json.optInt("order_id", 0);
		uid = json.optString("uid", null);
		shop_name = json.optString("shop_name", null);
		shop_id = json.optString("shop_id", null);
		//logo = "http://192.168.1.200/data/upload/shop/" + json.optString("logo", null);
		confirm = json.optString("confirm", null);
		is_delivery = json.optString("is_delivery", null);
		payment = json.optString("payment");
		itemnum = json.optString("itemnum", null);
		note = json.optString("note", null);
		comment_habbit = json.optString("comment_habbit", null);
		createtime = json.optString("createtime", null);
		
		status = json.optString("status", null);
		pay_status = json.optString("pay_status", null);
		status_tip = json.optString("status_tip", null);
		ship_status = json.optString("ship_status", null);
		user_status = json.optString("user_status", null);
		comment_status = json.optString("comment_status", null);

		ship_name = json.optString("ship_name", null);
		ship_addr = json.optString("ship_addr", null);
		ship_mobile = json.optString("ship_mobile", null);
		ship_tel = json.optString("ship_tel", null);
		ship_time = json.optString("ship_time", null);
		total_amount = json.optString("total_amount", null);
		final_amount = json.optString("final_amount", null);
		pmt_amount = json.optString("pmt_amount", null);
		payed = json.optString("payed", null);
		
		items = new ArrayList<OrderItemEntity>();
		JSONArray itemArr=json.optJSONArray("items");
		if(itemArr!=null){
			OrderItemEntity entity =null;
			int size =itemArr.length();
			for(int i =0;i<size;i++){
				entity = new OrderItemEntity();
				entity.paser(itemArr.getJSONObject(i));
				items.add(entity);
			}
		}
	}

}
