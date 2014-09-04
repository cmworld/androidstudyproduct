package com.zehua.tyqiu;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.app.common.BaseActivity;
import com.app.common.BaseEntity;
import com.app.common.JsonPaserFactory;
import com.app.http.AsyncHttpResponseHandler;
import com.zehua.api.data.ItemGroupList;
import com.zehua.tyqiu.adapter.FocusViewAdapter;
import com.zehua.tyqiu.adapter.ItemGroupListAdapter;

public class ShopfaceActivity extends BaseActivity implements OnPageChangeListener{

	private ViewPager mPager;
	private FragAdapter adapter; 
	private List<Fragment> mFragments;
	private ImageView cursor;
	private TextView tab_menu, tab_intro;
	
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	
	private static int shop_id;
	private static String shop_title;
	
    public static void start(Context context, int id, String title) {
        Intent intent = new Intent(context, ShopfaceActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopface);
		
        shop_id = getIntent().getIntExtra("id", 0);
        shop_title = getIntent().getStringExtra("title");
		
		mFragments = new ArrayList<Fragment>(2);
		
		//set bar which show
		ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        ab.setTitle(shop_title);		
		
		InitImageView();
		InitTextView();
		InitViewPager();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			Intent upIntent = new Intent(this, MainActivity.class);

			upIntent.addFlags(
						 Intent.FLAG_ACTIVITY_CLEAR_TOP |
						 Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(upIntent);
			finish();
			overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in );
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	
	private void InitTextView() {
		tab_menu = (TextView) findViewById(R.id.shop_menu);
		tab_intro = (TextView) findViewById(R.id.shop_intro);

		tab_menu.setOnClickListener(new TabOnClickListener(0));
		tab_intro.setOnClickListener(new TabOnClickListener(1));

	}

	private void InitViewPager() {
		
		mPager = (ViewPager) findViewById(R.id.shop_pager);
		
        List<Fragment> fragments = new ArrayList<Fragment>();  
        fragments.add(new ItemFragment());  
        fragments.add(new IntroFragment());  
          
        adapter = new FragAdapter(getSupportFragmentManager(), fragments);  
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(this);
	}


	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.tab_cursor).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 2 - bmpW);
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	public class TabOnClickListener implements View.OnClickListener {
		private int index = 0;

		public TabOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
	
	
	@Override
	public void onPageSelected(int position) {
		Log.i("pager","---onPageSelected--> position:"+position);
		Animation animation = null;
		switch (position) {
		case 0:
			if (currIndex == 1) {
				animation = new TranslateAnimation(offset * 2 + bmpW, 0, 0, 0);
			}
			
			break;
		case 1:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, offset * 2 + bmpW, 0, 0);
			}
			break;
		}
		

		currIndex = position;
		animation.setFillAfter(true);
		animation.setDuration(300);
		cursor.startAnimation(animation);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.i("pager","---onPageScrolled--> init");
		//switchFragmentContent(0);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		Log.i("pager","---onPageScrollStateChanged--> arg0"+arg0);
	}

	public class FragAdapter extends FragmentPagerAdapter{  
	      
	    private List<Fragment> fragments;  
	      
	  
	    public FragAdapter(FragmentManager fm) {  
	        super(fm);  
	    }  
	      
	    public FragAdapter(FragmentManager fm, List<Fragment> fragments) {  
	        super(fm);  
	        this.fragments = fragments;  
	    }  
	  
	    @Override  
	    public Fragment getItem(int position) {  
	        return fragments.get(position);  
	    }  
	  
	    @Override  
	    public int getCount() {  
	        return fragments.size();  
	    }  
	} 
	
    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
	public static class ItemFragment extends Fragment implements OnClickListener{
    	
    	private LayoutInflater minflater;
    	private ItemGroupListAdapter mAdapter;
    	private ListView  item_list;
    	private List<BaseEntity> contents;
    	
    	@Override
        public void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
        	
        	Log.i("Fragment","---onCreate--> init");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	Log.i("Fragment","---onCreateView--> init");
        	
            View rootView = inflater.inflate(R.layout.shop_pager_item, container, false);
            
            minflater = inflater;

            item_list= (ListView)rootView.findViewById(R.id.shop_item_list);
            
            if(mAdapter == null){
            	mAdapter = new ItemGroupListAdapter(rootView,minflater);
            	contents = new ArrayList<BaseEntity>();
            	mAdapter.setData(contents);
            }
            
			item_list.setAdapter(mAdapter);
			item_list.setOnItemClickListener(new OnItemClickListener() {  

    			@Override
    			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    				// TODO Auto-generated method stub
    				Log.i("item",position+"");
    			}
            });
            
            //go to cart
			Button cart_submit = (Button) rootView.findViewById(R.id.shopping_cart_submit);
			cart_submit.setOnClickListener(this);
			
			Map<String,String> mParams = new HashMap<String,String>();
			mParams.put("shop_id",shop_id+"");
			
			TyqiuSdk.get("app.item.list",mParams, new AsyncHttpResponseHandler(){
				
				public void onFinish() {
					super.onFinish();
					Log.i("networkhttp","AsyncHttpResponseHandler -- onFinish --> init ");
				
				}

				@Override
				public void onSuccess(HttpEntity content, int reqType) {
					super.onSuccess(content, reqType);
					
					Log.i("networkhttp","AsyncHttpResponseHandler -- onSuccess --> init ");
					
					
					try {
						Log.i("networkhttp",EntityUtils.toString(content));
						
						JSONObject res = JsonPaserFactory.paserObj(content);
						
						ItemGroupList ItemGroupListRes =  new ItemGroupList();
						ItemGroupListRes.paser(res);
						contents = ItemGroupListRes.getGroupList();
						
						if (contents != null && contents.size() > 0) {
							Log.i("networkhttp","mAdapter -- setData --> init ");
							
			                mAdapter.setData(contents);
			                mAdapter.notifyDataSetChanged();							
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MainActivity.showToast(e.getMessage());
						
					}
				}

				@Override
				public void onFailure(Throwable error, String content, int reqType) {
					super.onFailure(error, content, reqType);
					Log.i("networkhttp","AsyncHttpResponseHandler -- onFailure --> init ");
					if (error != null) {
						if (error instanceof ConnectException || error instanceof IOException) {
							//i_errRes = R.string.MSG_CONNECTION_ERR;
							MainActivity.showToast(R.string.MSG_CONNECTION_ERR);
						} else if (error instanceof TimeoutException) {
							//i_errRes = R.string.MSG_TIME_OUT;
							MainActivity.showToast(R.string.MSG_TIME_OUT);
						}
						error.printStackTrace();
					}
		
				}
				
			});            
            
            return rootView;
        }
        

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
				case R.id.shopping_cart_submit:
					
					List<HashMap<String, Object>> items = mAdapter.getCartItems();
					int CartNumInc = mAdapter.getCartNumInc();
					int CartPriceInc =  mAdapter.getCartPriceInc();
					ShoppingCartActivity.start(getActivity().getApplicationContext(),shop_id,shop_title,CartNumInc,CartPriceInc,items);
			}
		}
        
        
        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
        
        @Override
        public void onDestroy() {
            super.onDestroy();
            if(mAdapter != null && mAdapter.contents != null){
            	mAdapter.contents.clear();
            	mAdapter = null;
            }
        }
  
	}
	
	public static class IntroFragment extends Fragment{
			
		public ViewPager focusViewPager;
		public FocusViewAdapter focusPagerAdater;
		private ImageView[] tips;
		private int[] imgIdArray;
		private ImageView[] mImageViews;
		private Context mcontext;
		
        @Override
        public void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
        	
        	Log.i("IntroFragment","---onCreate--> init");
        	mcontext = getActivity().getApplicationContext();
        }

    	public void onActivityCreated(Bundle savedInstanceState) {
    		super.onActivityCreated(savedInstanceState);
    	}
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	Log.i("IntroFragment","---onCreateView--> init");
        	
            View rootView = inflater.inflate(R.layout.shop_pager_intro, container, false);  

    		ViewGroup group = (ViewGroup)rootView.findViewById(R.id.viewGroup);
    		focusViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
    		
    		imgIdArray = new int[]{
    				R.drawable.item01, R.drawable.item02, R.drawable.item03, R.drawable.item04,
    				R.drawable.item05,R.drawable.item06, R.drawable.item07, R.drawable.item08
    		};
    		
    		tips = new ImageView[imgIdArray.length];
    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,15);
    		params.setMargins(10,0,10,0);
    		
    		for(int i=0; i<tips.length; i++){
    			ImageView imageView = new ImageView(mcontext);
    	    	imageView.setLayoutParams(params);

    	    	tips[i] = imageView;
    	    	if(i == 0){
    	    		tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
    	    	}else{
    	    		tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
    	    	}
    	    	
    	    	 group.addView(imageView);
    		}
    		
    		mImageViews = new ImageView[imgIdArray.length];
    		for(int i=0; i<mImageViews.length; i++){
    			ImageView imageView = new ImageView(mcontext);
    			mImageViews[i] = imageView;
    			imageView.setBackgroundResource(imgIdArray[i]);
    		}
    		
    		focusPagerAdater = new FocusViewAdapter(mImageViews);
    		focusViewPager.setAdapter(focusPagerAdater);
    		focusViewPager.setOnPageChangeListener(new OnPageChangeListener(){

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					setImageBackground(arg0 % mImageViews.length);
				}
    			
    		});
    		focusViewPager.setCurrentItem((mImageViews.length) * 100);        	
        	
            return rootView;
        }


    	private void setImageBackground(int selectItems){
    		for(int i=0; i<tips.length; i++){
    			if(i == selectItems){
    				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
    			}else{
    				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
    			}
    		}
    	}
	}
}