package com.zehua.tyqiu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zehua.api.data.AddressEntity;
import com.zehua.tyqiu.R;

public class AddressListAdapter extends BaseAdapter {

	private List<AddressEntity> contents;
	private Context mcontext;
	
	public AddressListAdapter(Context context,List<AddressEntity> res) {
		mcontext = context;
		contents = res;
	}
	
	public void setData(List<AddressEntity> entity) {
		this.contents = entity;
	}

	public void addData(List<AddressEntity> entity) {
		this.contents.addAll(entity);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents.size();
	}

	@Override
	public AddressEntity getItem(int position) {
		// TODO Auto-generated method stub
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	class AddressInfo {
		TextView address;
	}
	
	private View getViewContent(AddressInfo AddressInfo) {
		View content = LayoutInflater.from(mcontext).inflate(R.layout.account_address_item, null, false);
		AddressInfo.address = (TextView) content.findViewById(R.id.address);
		content.setTag(AddressInfo);
		return content;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AddressEntity entity = getItem(position);
		AddressInfo AddressInfo = null;
		if (convertView == null) {
			AddressInfo = new AddressInfo();
			convertView = getViewContent(AddressInfo);
		} else {
			AddressInfo = (AddressInfo) convertView.getTag();
		}
		
		AddressInfo.address.setText(entity.address);
		return convertView;
	}

}
