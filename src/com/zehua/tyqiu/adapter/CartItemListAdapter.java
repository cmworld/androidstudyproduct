package com.zehua.tyqiu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.internal.view.menu.MenuView.ItemView;
import com.zehua.api.data.ItemEntity;
import com.zehua.tyqiu.R;

public class CartItemListAdapter extends BaseAdapter {

	private List<HashMap<String, Object>> contents;
	private LayoutInflater minflater;
	
	private Drawable unEnable;
	private Drawable Enable;
	private Drawable defBtnStatus;
	
	private ItemInfo itemInfo;
	private HashMap<Integer, View> ListArr = new HashMap<Integer, View>();
	
	private TextView cart_num;
	private TextView cart_status;
	private Button cart_submit;	
	private static int CartNumInc;
	private static int CartPriceInc;
	
	// postion,buynum
	private HashMap<String, Integer> UserItemArr = new HashMap<String, Integer>();	
	
	public CartItemListAdapter(Context context,LayoutInflater inflater,List<HashMap<String, Object>> cartItems,View cart_num_view,View cart_status_view,View cart_submit_view) {
		contents = cartItems;
		minflater = inflater;

		unEnable = context.getResources().getDrawable(R.drawable.cart_btn_border);
		Enable = context.getResources().getDrawable(R.drawable.cart_btn_border_on);
		
		cart_num= (TextView) cart_num_view;
		cart_status= (TextView) cart_status_view;
		cart_submit= (Button) cart_submit_view;
		
		defBtnStatus = cart_submit.getBackground();
	}
	
	public void setCartNumInc(int num){
		CartNumInc = num;
	}

	public void setCartPriceInc(int price){
		CartPriceInc = price;
	}
	
	
	public int getCartNumInc(){
		return CartNumInc;
	}
	
	public int getCartPriceInc(){
		return CartPriceInc;
	}
	
	public HashMap<String, Integer> getCartItems(){
		return UserItemArr;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
    
    private class ItemInfo {
    	TextView item_title;
    	TextView item_price;
    	Button num_dec;
    	EditText buynum;
    	Button num_inc;
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(!ListArr.containsKey(position)){
	    	convertView= minflater.inflate(R.layout.shopping_cart_item_list, null);
	    	
	    	itemInfo = new ItemInfo();
	    	itemInfo.item_title=(TextView) convertView.findViewById(R.id.item_title);
	    	itemInfo.item_price=(TextView) convertView.findViewById(R.id.item_price);
	    	itemInfo.buynum=(EditText) convertView.findViewById(R.id.buynum);
	    	itemInfo.num_inc=(Button) convertView.findViewById(R.id.btn_num_inc);
	    	itemInfo.num_dec=(Button) convertView.findViewById(R.id.btn_num_dec);
	        convertView.setTag(itemInfo);	        
	        
	        ListArr.put(position,convertView);
		}else{
			itemInfo = (ItemInfo) convertView.getTag();
		}
		
		HashMap<String, Object> o = getItem(position);
		
		itemInfo.item_title.setText((String)o.get("title"));
		itemInfo.item_price.setText("¥" + o.get("price")+" X ");
		itemInfo.buynum.setText(o.get("num")+"");
		
		//if num, the dec btn should be enable
		itemInfo.num_dec.setBackgroundDrawable(Enable);				
		itemInfo.num_dec.setEnabled(true);		
		
		itemInfo.num_inc.setOnClickListener(new IncDecClickListener(position));
		itemInfo.num_dec.setOnClickListener(new IncDecClickListener(position));
		
		//set userItems
		String id = String.valueOf(o.get("id"));
		if(!UserItemArr.containsKey(id)){
			UserItemArr.put(id, (Integer) o.get("num"));
		}
		
		return convertView;
	}
	
	public class IncDecClickListener implements  OnClickListener{
		private int position;
		
		public IncDecClickListener(int pos){
			position = pos;
		}
		
		@Override
		public void onClick(View v) {
			int num = 0;
			HashMap<String, Object> o = getItem(position);
			
			View view = ListArr.get(position);
			ItemInfo itemView =  (ItemInfo)view.getTag();
			
			if(v.getId() == itemInfo.num_inc.getId()){
				num = (Integer) o.get("num");
				num++;
					
				CartNumInc++;
				CartPriceInc += Integer.parseInt((String)o.get("price"));

			}else if (v.getId() == itemInfo.num_dec.getId()) {
				num = (Integer) o.get("num");
				num--;
				
				CartNumInc--;
				CartPriceInc -= Integer.parseInt((String)o.get("price"));
			}
			
			itemView.buynum.setText(num + "");
			o.put("num", num);
			contents.set(position, o);
			
			if(num <= 0){
				itemView.num_dec.setBackgroundDrawable(unEnable);
				itemView.num_dec.setEnabled(false);
			}else{
				itemView.num_dec.setBackgroundDrawable(Enable);				
				itemView.num_dec.setEnabled(true);
			}
			
			//change shopping status
			cart_num.setText(CartNumInc +"");
			cart_status.setText("总价 "+ CartPriceInc +"元");
			
			if(CartPriceInc >= 20){
				cart_submit.setText("下一步");
				cart_submit.setBackgroundColor(0xff41b4e6);
				cart_submit.setEnabled(true);
			}else{
				cart_submit.setText("差 "+(20 - CartPriceInc)+" 元起送 ");
				cart_submit.setEnabled(false);
				cart_submit.setBackgroundDrawable(defBtnStatus);
			}
			
			//change userItems
			String id =String.valueOf(o.get("id"));
			if(UserItemArr.containsKey(id)){
				UserItemArr.put(id, num);
			}
		}
	}

}
