package com.zehua.tyqiu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.app.common.BaseActivity;

public class SearchShopActivity extends BaseActivity implements OnClickListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_shop);

		//set bar which show
		ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
      
        //Inflate the custom search bar
        View serachBar = LayoutInflater.from(this).inflate(R.layout.search_bar, null);
        ImageButton searchButton = (ImageButton) serachBar.findViewById(R.id.search_btn);
		searchButton.setOnClickListener(this);

        //Attach to the action bar
        getSupportActionBar().setCustomView(serachBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);     
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
