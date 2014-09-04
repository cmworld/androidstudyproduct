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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.app.common.BaseActivity;
import com.app.common.JsonPaserFactory;
import com.app.http.AsyncHttpResponseHandler;
import com.apple.adapter.ViewPagerAdapter;
import com.apple.common.BaseHttpRes;
import com.apple.utils.CommonUtil;
import com.apple.utils.MySharedPreferencesMgr;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hupu.sns.R;
import com.hupu.sns.data.ActionAll;
import com.hupu.sns.data.AllGroupEntity;
import com.hupu.sns.data.MyGroupEntity;
import com.hupu.sns.data.NewsAll;
import com.hupu.sns.data.NewsFoucsAll;
import com.hupu.sns.data.SpaceAll;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.zehua.api.data.ShopEntity;
import com.zehua.api.data.ShopsList;
import com.zehua.tyqiu.adapter.ShopListAdapter;
//import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnItemClickListener,OnClickListener {
	
	private static final int SHOP_FACE = 1000;
	
	private DrawerLayout mDrawerLayout;
	private View mDrawer;
	private ImageView avatar;
	private Button signin_btn;
	private Button logout_btn;
	private Fragment fragment;

	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
	public static final int REGISTER_REQCODE = 5001;
	public static final int SIGN_REQCODE = 5002;
	
	private TextView userIntface;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//set bar which show
		ActionBar ab = getSupportActionBar();
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        
        //Navigation Drawer
        initDrawerLayout(savedInstanceState);
        
        // update the main content by replacing fragments
        fragment = new PlanetFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        //click event
        //clickEven = new MainClickEven();    
	}
	
    @Override
    public void onResume() {
        super.onResume();
        
        String username  = accountMr.get("username");
        Log.i("onresume","username = "+username);
        
        if(username != null){
        	userIntface.setText(username);
        	userIntface.setVisibility(View.VISIBLE);
        	userIntface.setOnClickListener(this);
        	
        	signin_btn.setVisibility(View.GONE);
        	
        	if(logout_btn != null){
        		logout_btn.setVisibility(View.VISIBLE);
        	}
        	
        }
    }
	
    private void initDrawerLayout(Bundle savedInstanceState) {
    	
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = findViewById(R.id.left_drawer);
        
    	View mAvatarFrame = findViewById(R.id.avatar_frame);
    	avatar = (ImageView) mAvatarFrame.findViewById(R.id.avatar);
    	signin_btn = (Button) mAvatarFrame.findViewById(R.id.signin_btn);
        
        avatar.setOnClickListener(this);
        signin_btn.setOnClickListener(this);
        
        logout_btn = (Button) findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(this);
        
        userIntface = (TextView) findViewById(R.id.userIntface);
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);    
        mDrawerList = (ListView) findViewById(R.id.list);
        
        String[] mPlanetTitles  = getResources().getStringArray(R.array.planets);
        int[] iconIds = { R.drawable.ico_nav_news, R.drawable.ico_nav_order, R.drawable.ico_nav_book, R.drawable.ico_nav_book, R.drawable.ico_nav_book};
        
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item,mPlanetTitles));
        mDrawerList.setAdapter(new NavigationAdapter(mPlanetTitles, iconIds));
        mDrawerList.setOnItemClickListener(this);
        mDrawerToggle = new ActionBarDrawerToggle ( this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close ) {
        	
			public void onDrawerClosed(View view) {
            	invalidateOptionsMenu();
              }
            
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.topbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
	        case android.R.id.home:	
	        	if (mDrawerLayout.isDrawerOpen(mDrawer)) { 
	        		mDrawerLayout.closeDrawer(mDrawer); 
	        	} else { 
	        		mDrawerLayout.openDrawer(mDrawer); 
	        	}
	
	            return true;
	        case R.id.menu_search:
	        	Intent intent = new Intent(MainActivity.this, SearchShopActivity.class);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	            return true;
	        case R.id.menu_booksection:
	            return true;

	            
        }
        
        return super.onOptionsItemSelected(item);
	}

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* The click listener for ListView in the navigation drawer */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	
    	if (position == 1) {
			AccountOrderActivity.start(this);
			return;
		}else if(position == 2){
			AccountAddressActivity.start(this);
			return;
		}else if(position == 3){
			AccountChgPassActivity.start(this);
			return;
		}else if(position == 4){
			AboutUsActivity.start(this);
			return;
		}
        
    	selectItem(position);
    }

    private void selectItem(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawer);
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		Log.i("click", "---MainClickEven--onClick-->"+v.getId());
		
		switch (v.getId()) {

		case R.id.signin_btn:
			startActivityForResult(new Intent(MainActivity.this,AccountLoginActivity.class),SIGN_REQCODE);
			break;
		
		case R.id.logout_btn:
			if(accountMr.logout()){
				userIntface.setText("");
				userIntface.setVisibility(View.GONE);
				signin_btn.setVisibility(View.VISIBLE);
				logout_btn.setVisibility(View.GONE);
			}
			
			break;
		case R.id.userIntface:	
			break;			
			
		case R.id.avatar:	
			break;
		default:
			break;
		}
	}		

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {

		   case SIGN_REQCODE:
				Bundle b=data.getExtras();
				String username=b.getString("username");
				
				Log.i("activity_result","get username = "+ username);
				
				userIntface.setText(username);
				userIntface.setVisibility(View.VISIBLE);
				signin_btn.setVisibility(View.GONE);
				logout_btn.setVisibility(View.VISIBLE);
			   break;
		default:
		    break;
	    }
	}
    	
    /**
     * Fragment that appears in the "content_frame", shows a planet
     * @param <PullToRefreshListView>
     */
    
	public static class PlanetFragment extends Fragment implements OnClickListener{
        public static final String ARG_PLANET_NUMBER = "planet_number";
        
        private LayoutInflater inflater;
    	private ListView mListView;
    	private ShopListAdapter mAdapter;
    	private PullToRefreshListView mPullDownView;
    	private boolean isLoading;
    	protected MainActivity mAct;
    	
    	public ShopsList shopsList;
        private View loadding;
        
        int page;
        
    	ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String,Object>>();

		private View mPullDownFootView;
        
    	@Override
    	public void onAttach(Activity activity) {
    		super.onAttach(activity);
    		mAct = (MainActivity) activity;
    	}
    	
        @Override
        public void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            	
            this.inflater = inflater;
            
            //bottom bar
            RelativeLayout localbar = (RelativeLayout) rootView.findViewById(R.id.bottom_localbar);
            localbar.getBackground().setAlpha(200);
            TextView changeLocal_btn = (TextView) localbar.findViewById(R.id.changeLocal);
            changeLocal_btn.setOnClickListener(this);
           
            
            loadding = rootView.findViewById(R.id.loading);

            mPullDownView = (PullToRefreshListView) rootView.findViewById(R.id.pull_down_view);
            mPullDownFootView = inflater.inflate(R.layout.pulldown_footer, mPullDownView.getRefreshableView(),false);
            mPullDownView.getRefreshableView().addFooterView(mPullDownFootView, null, false);

    		mPullDownView.setOnRefreshListener(new  OnRefreshListener<ListView>(){
    			
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
    				Log.i("PullDownListener","PullDownListener --onRefresh --> init ");
    				
    				isLoading = true;
    				
    				String label = DateUtils.formatDateTime(mAct, System.currentTimeMillis(),  
                            DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  
      
                    // Update the LastUpdatedLabel  
                    refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                    
    	    		loadShopData(10,true);
				}
    			
    		});
    		
    		mPullDownView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                @Override
                public void onLastItemVisible() {
                	
                    if(!isLoading){
                        TextView text = (TextView) mPullDownFootView.findViewById(R.id.list_footview_text);
                        text.setText(R.string.no_more);
                        mPullDownFootView.findViewById(R.id.list_footview_progress).setVisibility(View.GONE);
                        mPullDownFootView.setOnClickListener(null);
                    }
                }
            });	
    		
    		mPullDownView.setOnItemClickListener(new OnItemClickListener() {
    			
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    				// TODO Auto-generated method stub
    				//Toast.makeText(this, "clicked: " + position, Toast.LENGTH_SHORT).show();
    				Log.i("click", "---PullDownClieckListener--click--> ");
    				
    				
    		        Object obj = parent.getItemAtPosition(position);
    		        if(obj != null && obj instanceof ShopEntity){
    		        	ShopEntity c = (ShopEntity)obj;
    		        	ShopfaceActivity.start(parent.getContext(), c.id,c.shop_name);
    		        }
    			}
    			
			});

            return rootView;
        }

        boolean isShowing;
        @Override
        public void onResume() {
            super.onResume();
            
            Log.i("planfargment", String.format(" fragment resume, showing = %b",isShowing));
            
            if(!isShowing){
                loadding.setVisibility(View.VISIBLE);
                loadShopData(10, true);
                isShowing = true;
            }
        }        
        
		private void loadShopData(int placeId ,boolean getnew){

            TextView text = (TextView) mPullDownFootView.findViewById(R.id.list_footview_text);
            text.setText(R.string.loading);            
            mPullDownFootView.findViewById(R.id.list_footview_progress).setVisibility(View.VISIBLE);
            mPullDownFootView.setOnClickListener(null);
            
            page = getnew ? 1 : page + 1;
			
            isLoading = false;
            //mPullDownView.setRefreshing();
           // loadding.setVisibility(View.GONE);
			
			Map<String,String> mParams = new HashMap<String,String>();
			mParams.put("placeId",placeId+"");
			mParams.put("page",page+"");
			
			TyqiuSdk.get("app.shop.list",mParams, new AsyncHttpResponseHandler(){
				
				public void onFinish() {
					super.onFinish();
					Log.i("networkhttp","AsyncHttpResponseHandler -- onFinish --> init ");

					mPullDownView.onRefreshComplete();
					isLoading = false;					
				}

				@Override
				public void onSuccess(HttpEntity content, int reqType) {
					super.onSuccess(content, reqType);
					
					Log.i("networkhttp","AsyncHttpResponseHandler -- onSuccess --> init ");
					
					
					try {
						Log.i("networkhttp",EntityUtils.toString(content));
						
						JSONObject res = JsonPaserFactory.paserObj(content);
						
						if(JsonPaserFactory.isNull(res)){
							throw new Exception("null");
						}
						
						if(JsonPaserFactory.isErr(res)){
							throw new Exception(res.optString("data"));
						}
						
						ShopsList shopsListRes =  new ShopsList();
						shopsListRes.paser(res);
						List<ShopEntity> contents = shopsListRes.getContents();
						
						if (contents != null && contents.size() > 0) {
							Log.i("networkhttp","mAdapter -- setData --> init ");

			                if(mAdapter == null){
			                	mAdapter = new ShopListAdapter(inflater);
			                }
			                
			                if(page <= 1){
			                	mAdapter.setData(contents);
			            		mPullDownView.setAdapter(mAdapter);
			                }else{
			                	mAdapter.addData(contents);
			                }
			                
			                loadding.setVisibility(View.GONE);
			                mAdapter.notifyDataSetChanged();
			                isLoading = false;							
							
							
							mAdapter.notifyDataSetChanged();
						}

						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MainActivity.showToast(e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MainActivity.showToast(e.getMessage());
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
					
					
	                TextView text = (TextView) mPullDownFootView.findViewById(R.id.list_footview_text);
	                text.setText(R.string.reloading);
	                mPullDownFootView.findViewById(R.id.list_footview_progress).setVisibility(View.GONE);
	                mPullDownFootView.setOnClickListener(new OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
                            if(page >1) {
                                page -= 1;
                                loadShopData(10, false);
                            }else{
                            	loadShopData(10,true);
                            }
	                    }
	                });

	               // if(mPullDownView.getRefreshableView().getAdapter()==null)
	                //    timeOut.setVisibility(View.VISIBLE);				
				}
				
			});		
				
    	}
		
		
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            isShowing = false;
        }
        
        @Override
        public void onDestroy() {
            super.onDestroy();
            if(mAdapter != null && mAdapter.contents != null){
            	mAdapter.contents.clear();
            	mAdapter = null;
            }
        }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
    		Log.i("click", "---MainClickEven--onClick-->"+v.getId());
    		
    		switch (v.getId()) {
	    		case R.id.changeLocal:
		        	
		        	Intent intent = new Intent(getActivity(), SearchLocationActivity.class);
		        	startActivity(intent);
		        	getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		        	break;
    		}
		}
    	
    }
	
	class NavigationItem {
	    String title;
	    Drawable icon;
	    //int num;
	}

	public class NavigationAdapter extends BaseAdapter {
		
	    NavigationItem[] navs;

	    public NavigationAdapter(String[] titles, int[] iconIds) {//,int[] tips
	        navs = new NavigationItem[titles.length];
	        for (int i = 0; i < titles.length && i < iconIds.length; i++) {
	            navs[i] = new NavigationItem();
	            navs[i].title = titles[i];
	            navs[i].icon = getResources().getDrawable(iconIds[i]);
	            //navs[i].num = tips[0];
	        }
	    }

	    @Override
	    public int getCount() {
	        return navs.length;
	    }

	    @Override
	    public NavigationItem getItem(int position) {
	        return navs[position];
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        NavigationItem item = getItem(position);
	        if (convertView == null) {
	            convertView = getLayoutInflater().inflate( R.layout.navigation_list_item, parent, false);
	        }
	        ImageView iconView = (ImageView) convertView.findViewById(R.id.icon);
	        TextView titleView = (TextView) convertView.findViewById(R.id.text);
	        iconView.setImageDrawable(item.icon);
	        titleView.setText(item.title);
	        return convertView;
	    }

	}


	@Override
	public void onErrResponse(String data, Throwable error, String content,
			int type) {
		// TODO Auto-generated method stub
		super.onErrResponse(data, error, content, type);
	}

	@Override
	public void onReqResponse(String data, Object o, int methodId) {
		super.onReqResponse(data, o, methodId);
		try {
			switch (methodId) {
			case BaseHttpRes.REQ_METHOD_GET_NEW_FOCUS:

				break;
			case BaseHttpRes.REQ_METHOD_GET_NEWS_ARTICLES:

				break;
			case BaseHttpRes.REQ_METHOD_GET_TRENDS_SECLECT:

				break;
			case BaseHttpRes.REQ_METHOD_GET_TRENDS_SECLECT_NEXT:

				break;
			case BaseHttpRes.REQ_METHOD_GET_GROUP_ATTENTIONLIST:

				break;
			case BaseHttpRes.REQ_METHOD_GET_GROUP_BOARDLIST:

				break;
			case BaseHttpRes.REQ_METHOD_POST_USER_GETSPACE:

				break;
			}
		} catch (Exception e) {

		}
	}	
	
}
