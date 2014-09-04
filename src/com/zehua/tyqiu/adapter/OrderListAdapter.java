package com.zehua.tyqiu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.zehua.api.data.OrderEntity;
import com.zehua.tyqiu.R;

public class OrderListAdapter extends BaseAdapter {

	private List<OrderEntity> contents;
	private Context mcontext;
	
	public OrderListAdapter(Context context,List<OrderEntity> res) {
		mcontext = context;
		contents = res;
	}
	
	public void setData(List<OrderEntity> entity) {
		this.contents = entity;
	}

	public void addData(List<OrderEntity> entity) {
		this.contents.addAll(entity);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents.size();
	}

	@Override
	public OrderEntity getItem(int position) {
		// TODO Auto-generated method stub
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	class OrderInfo {
		TextView shop_name;
		ImageView shop_image;
		TextView order_time;
		TextView order_price;
		TextView order_status;
	}
	
	private View getViewContent(OrderInfo OrderInfo) {
		View content = LayoutInflater.from(mcontext).inflate(R.layout.account_order_item, null, false);
		OrderInfo.shop_name = (TextView) content.findViewById(R.id.shop_name);
		OrderInfo.shop_image = (ImageView) content.findViewById(R.id.shop_image);
		OrderInfo.order_time = (TextView) content.findViewById(R.id.order_time);
		OrderInfo.order_price = (TextView)content.findViewById(R.id.order_price);
		OrderInfo.order_status = (TextView)content.findViewById(R.id.order_status);
		content.setTag(OrderInfo);
		return content;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		OrderEntity entity = getItem(position);
		OrderInfo OrderInfo = null;
		if (convertView == null) {
			OrderInfo = new OrderInfo();
			convertView = getViewContent(OrderInfo);
		} else {
			OrderInfo = (OrderInfo) convertView.getTag();
		}
		
		OrderInfo.shop_name.setText(entity.shop_name);
		OrderInfo.shop_image.setBackgroundResource(R.drawable.logo_sample_3);
		OrderInfo.order_time.setText(entity.createtime);
		OrderInfo.order_price.setText(entity.final_amount);
		OrderInfo.order_status.setText(entity.status_tip);
		
		//ShopInfo.shop_image.setBackgroundResource(R.drawable.bg_list_foces);
		//UrlImageViewHelper.setUrlDrawable(ShopInfo.previewImg, entity.img_url,R.drawable.bg_list_foces);
		//UrlImageViewHelper.setUrlDrawable(ShopInfo.shop_image, entity.logo);
		
		return convertView;
	}

}
