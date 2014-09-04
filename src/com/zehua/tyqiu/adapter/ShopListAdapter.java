package com.zehua.tyqiu.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.zehua.api.data.ShopEntity;
import com.zehua.tyqiu.R;

public class ShopListAdapter extends BaseAdapter {

	public List<ShopEntity> contents;
	private LayoutInflater inflater;

	public ShopListAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}
	
	public void setData(List<ShopEntity> entity) {
		this.contents = entity;
	}

	public void addData(List<ShopEntity> entity) {
		this.contents.addAll(entity);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents.size();
	}

	@Override
	public ShopEntity getItem(int position) {
		// TODO Auto-generated method stub
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	class ShopInfo {
		TextView shop_title;
		ImageView shop_image;
		TextView shop_category;
		ImageView shop_star;
	}
	
	private View getViewContent(ShopInfo ShopInfo) {
		
		View content = inflater.inflate(R.layout.pulldown_shop_item, null, false);
		ShopInfo.shop_title = (TextView) content.findViewById(R.id.shop_title);
		ShopInfo.shop_image = (ImageView) content.findViewById(R.id.shop_image);
		ShopInfo.shop_category = (TextView) content.findViewById(R.id.shop_category);
		ShopInfo.shop_star = (ImageView)content.findViewById(R.id.shop_star);
		content.setTag(ShopInfo);
		return content;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ShopEntity entity = getItem(position);
		ShopInfo ShopInfo = null;
		if (convertView == null) {
			ShopInfo = new ShopInfo();
			
			convertView = getViewContent(ShopInfo);
		} else {
			ShopInfo = (ShopInfo) convertView.getTag();
		}
		
		ShopInfo.shop_title.setText(entity.shop_name);
		ShopInfo.shop_category.setText(entity.cate_txt);
		//ShopInfo.shop_image.setBackgroundResource(R.drawable.bg_list_foces);
		//UrlImageViewHelper.setUrlDrawable(ShopInfo.previewImg, entity.img_url,R.drawable.bg_list_foces);
		UrlImageViewHelper.setUrlDrawable(ShopInfo.shop_image, entity.logo);
		ShopInfo.shop_star.setBackgroundResource(R.drawable.ico_star);
		return convertView;
	}

}
