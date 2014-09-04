package com.zehua.tyqiu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.app.common.BaseActivity;

public class AccountFavActivity extends BaseActivity {
	
	
    public static void start(Context context) {
        Intent intent = new Intent(context, AccountFavActivity.class);
        context.startActivity(intent);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_fav);
		
		
		ActionBar ab = getSupportActionBar();
        ab.setDisplayUseLogoEnabled(false);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        ab.setTitle(R.string.account_fav);

		InitGridView();
	}
	
	
	public  void InitGridView(){
		
        GridView gridView=(GridView)findViewById(R.id.gridview);  
        gridView.setAdapter(new ImageAdapter(this));  
        //单击GridView元素的响应  
        gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
                //弹出单击的GridView元素的位置  
                //Toast.makeText(HelloGridView.this,mThumbIds[position], Toast.LENGTH_SHORT).show();  	
			}  
        });
	}
	
    //展示图片  
    private Integer[] mThumbIds = {  
            R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
            R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
            R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
            R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
            R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
           // R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
           // R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
           // R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
           // R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
           // R.drawable.logo_sample_1, R.drawable.logo_sample_2, R.drawable.logo_sample_3,
    };
	
    private class ImageAdapter extends BaseAdapter{  
        private Context mContext;
        
        public ImageAdapter(Context context){
        	mContext = context;
        }
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mThumbIds.length;  
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mThumbIds[position];  
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		    //定义一个ImageView,显示在GridView里  
		    ImageView imageView;
		    if(convertView==null){
		        imageView=new ImageView(mContext);  
		        //imageView.setLayoutParams(new GridView.LayoutParams(80, 80));  
		        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);  
		        //imageView.setPadding(8, 8, 8, 8);  
		    }else{
		        imageView = (ImageView) convertView;  
		    }
		    imageView.setImageResource(mThumbIds[position]);  
		    return imageView;
		}    
    }
}