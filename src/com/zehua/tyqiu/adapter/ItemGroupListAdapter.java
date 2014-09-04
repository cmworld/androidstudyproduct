package com.zehua.tyqiu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.R.integer;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.common.BaseEntity;
import com.zehua.api.data.CateEntity;
import com.zehua.api.data.ItemEntity;
import com.zehua.tyqiu.R;

public class ItemGroupListAdapter extends BaseAdapter {

	public List<BaseEntity> contents;
	private LayoutInflater minflater;
	private View pw;
	private TextView cart_num;
	private TextView cart_status;
	private Button cart_submit;
	private Drawable defBtnStatus;
	private CatInfo catInfo;
	private ItemInfo itemInfo;
	private static int CartNumInc;
	private static int CartPriceInc; 
	private HashMap<Integer, View> ListArr = new HashMap<Integer, View>();
	
	// postion,buynum
	private HashMap<Integer, Integer> UserItemArr = new HashMap<Integer, Integer>();

	public ItemGroupListAdapter(View parentView,LayoutInflater inflater) {
		minflater = inflater;
		pw = parentView;
		
		cart_num= (TextView) pw.findViewById(R.id.shopping_cart_num);
		cart_status= (TextView) pw.findViewById(R.id.shopping_cart_status);
		cart_submit= (Button) pw.findViewById(R.id.shopping_cart_submit);
		
		defBtnStatus = cart_submit.getBackground();
		
		CartNumInc = 0;
		CartPriceInc = 0;
	}
	
	public List<HashMap<String, Object>> getCartItems(){
		List<HashMap<String, Object>> CartItemArr = new ArrayList<HashMap<String,Object>>();
		
		for(Map.Entry<Integer , Integer> entry : UserItemArr.entrySet()){
			int position = entry.getKey();
			int buynum = entry.getValue();
			
			ItemEntity o = (ItemEntity) getItem(position);
			
			HashMap<String, Object> newItem = new HashMap<String, Object>();
			newItem.put("id",o.id);
			newItem.put("title",o.title);
			newItem.put("price",o.price);
			newItem.put("num",buynum);
			CartItemArr.add(newItem);
		}
		
		return CartItemArr;
	}
	
	public int getCartNumInc(){
		return CartNumInc;
	}
	
	public int getCartPriceInc(){
		return CartPriceInc;
	}
	
	public void setData(List<BaseEntity> entity) {
		this.contents = entity;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents.size();
	}

	@Override
	public BaseEntity getItem(int position) {
		// TODO Auto-generated method stub
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

    @Override  
    public boolean isEnabled(int position) {  
        // TODO Auto-generated method stub
    	 BaseEntity o = getItem(position);
    	 if(o instanceof ItemEntity){ 
             return false;  
         }
         return super.isEnabled(position);  
    }	
	
    
    private class ItemInfo {
    	TextView item_title;
    	TextView item_price;
    	TextView buynum;
    	Button add_cart;
    }
    
    private class CatInfo {
    	TextView label;
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		BaseEntity o = getItem(position);
        if(o instanceof CateEntity){
        	
        	view = getCatView(position,convertView,o);
        }else{
        	view = getItemView(position,convertView,o);
        }
		
		return view;
	}
	
	public View getCatView(int pos,View convertView,BaseEntity o){
		
		if(!ListArr.containsKey(pos)){//if(convertView == null){	
	    	convertView= minflater.inflate(R.layout.shop_scroll_item_tag, null);
	    	
	    	catInfo = new CatInfo();
	    	catInfo.label=(TextView) convertView.findViewById(R.id.item_title);
	        convertView.setTag(catInfo);
	        ListArr.put(pos,convertView);
		}else{
			convertView = (View) ListArr.get(pos);
			catInfo = (CatInfo) convertView.getTag();
		}
		
		CateEntity cate = (CateEntity)o;
		catInfo.label.setText(cate.name);

		return convertView;
	}
	
	public View getItemView(int pos,View convertView,BaseEntity o){
		
		Log.i("group","item init ,position="+pos);
		if(!ListArr.containsKey(pos)){
			convertView = minflater.inflate(R.layout.shop_scroll_item, null);
			itemInfo = new ItemInfo();
			itemInfo.item_title = (TextView)convertView.findViewById(R.id.item_title);
			itemInfo.item_price = (TextView)convertView.findViewById(R.id.item_price);
			itemInfo.buynum = (TextView)convertView.findViewById(R.id.buynum);
			itemInfo.add_cart = (Button)convertView.findViewById(R.id.ico_add_to_cart);
			convertView.setTag(itemInfo);
			ListArr.put(pos,convertView);
		}else{
			convertView = (View) ListArr.get(pos);
			itemInfo = (ItemInfo) convertView.getTag();
		}			
		
		ItemEntity item = (ItemEntity) o;
		
		itemInfo.item_title.setText(item.title);
		itemInfo.item_price.setText(item.price);
		itemInfo.buynum.setText("0");
		itemInfo.add_cart.setOnClickListener(new ItemClickListener(pos));
		
		
		if(UserItemArr.containsKey(pos)){
			int buynum = UserItemArr.get(pos);
			itemInfo.buynum.setVisibility(View.VISIBLE);
			itemInfo.buynum.setText(buynum + "");		
		}
		
		
		return convertView;
	}
	
	
	public class ItemClickListener implements  OnClickListener{
		private int position;
		
		public ItemClickListener(int pos){
			position = pos;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			View view = ListArr.get(position);
			Object itemView = view.getTag();
			if (itemView  instanceof ItemInfo){
				ItemInfo iteminfo = (ItemInfo) itemView;
				if(v.getId() == iteminfo.add_cart.getId()){
					Log.i("grouplist","-----ItemClickListener--------->"+iteminfo.item_title.getText());
					
					int buynum = Integer.parseInt( (String) iteminfo.buynum.getText() );
					buynum++;
					
					iteminfo.buynum.setVisibility(View.VISIBLE);
					iteminfo.buynum.setText(buynum + "");
					
					CartNumInc++;
					cart_num.setText(CartNumInc +"");
					
					ItemEntity itemEntity = (ItemEntity) getItem(position);
					
					int price = Integer.parseInt(itemEntity.price);
					CartPriceInc +=price;
					cart_status.setText("总价 "+ CartPriceInc +"元");
					
					if(CartPriceInc >= 20){
						cart_submit.setText("选好了");
						cart_submit.setBackgroundColor(0xff41b4e6);
						cart_submit.setEnabled(true);
					}else{
						cart_submit.setText("差 "+(20 - CartPriceInc)+" 元起送 ");
						cart_submit.setEnabled(false);
						cart_submit.setBackgroundDrawable(defBtnStatus);
					}
					
					//add userItems
					if(UserItemArr.containsKey(position)){
						int num = UserItemArr.get(position);
						UserItemArr.put(position, ++num);
					}else{
						UserItemArr.put(position, 1);
					}
				}
			}
		}
	}
}
