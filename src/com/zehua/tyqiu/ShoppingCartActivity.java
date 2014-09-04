package com.zehua.tyqiu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.app.common.BaseActivity;
import com.app.common.BaseEntity;
import com.zehua.tyqiu.adapter.CartItemListAdapter;
import com.zehua.tyqiu.adapter.ItemGroupListAdapter;

public class ShoppingCartActivity extends BaseActivity implements OnClickListener {
	
	private LayoutInflater minflater;
	private ListView item_list;
	private CartItemListAdapter mAdapter;
	
	private TextView cart_num;
	private TextView cart_status;
	private Button cart_submit;	
	
	private int shop_id;
	private String shop_name;
	private int item_price_total;
	private int item_num;
	private List<HashMap<String, Object>> cartItems;

    public static void start(Context context,int shop_id,String shop_name,int CartNumInc,int CartPriceInc,List<HashMap<String, Object>> items) {
        Intent intent = new Intent(context, ShoppingCartActivity.class);
        
        intent.putExtra("shop_id", shop_id);
        intent.putExtra("shop_name", shop_name);
        intent.putExtra("item_num", CartNumInc);
        intent.putExtra("item_price_total", CartPriceInc);
        intent.putExtra("items",  (Serializable)items);
        
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startActivity(intent);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);
		
		//set bar which show
		ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(R.string.shpping_cart_title);
		
		//get data
		shop_id = getIntent().getIntExtra("shop_id",0);
		shop_name = getIntent().getStringExtra("shop_name");
		item_price_total= getIntent().getIntExtra("item_price_total",0);
		item_num= getIntent().getIntExtra("item_num", 0);
		cartItems=(List<HashMap<String, Object>>) getIntent().getSerializableExtra("items");
		
		for(int i=0; i< cartItems.size();i++){
			HashMap<String, Object> o = cartItems.get(i);
			Log.i("cartitems","title="+o.get("title")+",num="+o.get("num")+"");
		}
		
		//set data
		cart_num= (TextView) findViewById(R.id.shopping_cart_num);
		cart_status= (TextView) findViewById(R.id.shopping_cart_status);
		cart_submit= (Button) findViewById(R.id.shopping_cart_submit);
		cart_num.setText(item_num+"");
		cart_status.setText("总价 "+item_price_total+" 元");
		cart_submit.setText("下一步");
		cart_submit.setOnClickListener(this);
		
        minflater = getLayoutInflater();
        item_list= (ListView) findViewById(R.id.item_list);
        
        if(mAdapter == null){
        	mAdapter = new CartItemListAdapter(getApplicationContext(),minflater,cartItems,cart_num,cart_status,cart_submit);
        	mAdapter.setCartNumInc(item_num);
        	mAdapter.setCartPriceInc(item_price_total);
        }
        
		item_list.setAdapter(mAdapter);	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.shopping_cart_submit:
				HashMap<String, Integer> items = mAdapter.getCartItems();
				int CartNumInc = mAdapter.getCartNumInc();
				int CartPriceInc =  mAdapter.getCartPriceInc();
				ShoppingOrderActivity.start(getApplicationContext(),shop_id,shop_name,CartNumInc,CartPriceInc,items);
		}
	}
}
