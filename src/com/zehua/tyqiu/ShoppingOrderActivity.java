package com.zehua.tyqiu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.app.common.BaseActivity;

public class ShoppingOrderActivity extends BaseActivity implements OnClickListener {
	
	private int shop_id;
	private String shop_name;
	private int item_price_total;
	private int item_num;
	private HashMap<String, Integer> user_items;
	
	private TextView cart_num;
	private TextView cart_status;
	private Button cart_submit;		
	
    public static void start(Context context,int shop_id,String shop_name,int CartNumInc,int CartPriceInc,HashMap<String, Integer> items) {
        Intent intent = new Intent(context, ShoppingOrderActivity.class);

        intent.putExtra("shop_id", shop_id);
        intent.putExtra("shop_name", shop_name);
        intent.putExtra("item_num", CartNumInc);
        intent.putExtra("item_price_total", CartPriceInc);
        intent.putExtra("items",(Serializable)items);
        
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startActivity(intent);
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_order);
		
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
		user_items= (HashMap<String, Integer>) getIntent().getSerializableExtra("items");        
        
		//set data
		cart_num= (TextView) findViewById(R.id.shopping_cart_num);
		cart_status= (TextView) findViewById(R.id.shopping_cart_status);
		cart_submit= (Button) findViewById(R.id.shopping_cart_submit);
		cart_num.setText(item_num+"");
		cart_status.setText("总价 "+item_price_total+" 元");
		cart_submit.setText("提交订单");
		cart_submit.setOnClickListener(this);	
		
        //go to cart
		Button cart_submit = (Button) findViewById(R.id.shopping_cart_submit);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
		
}
