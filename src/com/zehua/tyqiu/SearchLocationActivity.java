package com.zehua.tyqiu;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.app.common.BaseActivity;
import com.app.utils.AMapUtil;
import com.app.utils.Constants;
import com.app.utils.ToastUtil;

public class SearchLocationActivity extends BaseActivity implements LocationSource,AMapLocationListener, OnClickListener, TextWatcher,OnPoiSearchListener{

	private AutoCompleteTextView searchText;
	private String keyword;
	
	private ProgressDialog progDialog = null;
	
	private AMap aMap;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private double lat;
	private double lng;
	private LatLng myLocation;
	
	private PoiSearch.Query query;// Poi��ѯ������
	private PoiSearch poiSearch;// POI����
	private PoiResult poiResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_map);
		
		progDialog = new ProgressDialog(this);
		
		//set bar which show
		ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
      
        //Inflate the custom search bar
        View serachBar = LayoutInflater.from(this).inflate(R.layout.search_bar, null);
        ImageButton searchButton = (ImageButton) serachBar.findViewById(R.id.search_btn);
		searchButton.setOnClickListener(this);
		searchText = (AutoCompleteTextView) serachBar.findViewById(R.id.search_keyword);
		searchText.addTextChangedListener(this);// ����ı����������¼�

        //Attach to the action bar
        getSupportActionBar().setCustomView(serachBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);        
        
        //amap init
        initMap();
	}
	
	public void initMap(){
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.wmap)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				
				UiSettings uiSettings=aMap.getUiSettings();
				uiSettings.setCompassEnabled(false);
				uiSettings.setMyLocationButtonEnabled(true);
				uiSettings.setScaleControlsEnabled(false);
				uiSettings.setScrollGesturesEnabled(true);//�Ƿ�������
				uiSettings.setZoomGesturesEnabled(true);//�Ƿ�������������
				
		
				aMap.setMapType(Constants.MAP_TYPE_NORMAL);
				aMap.setTrafficEnabled(false);
				aMap.setLocationSource(this);
				aMap.setMyLocationEnabled(true);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        //getSupportMenuInflater().inflate(R.menu.topbar_search_menu, menu);

        // Associate searchable configuration with the SearchView
        //MenuItem search=menu.findItem(R.id.menu_search);
        //search.collapseActionView();
		
        // ��ȡSearchView���� 
        //searchview= new SearchView(getSupportActionBar().getThemedContext());
       // searchview.setQueryHint(getResources().getString(R.string.search_hint));
        
        //searchview.setOnQueryTextListener(this);
       // searchview.setOnSuggestionListener(this);      
        
        //������������
        //SearchQuerySuggestionsAdapter suggestion = getSuggestions();
        //searchview.setSuggestionsAdapter(suggestion);        
        
        // ��ӵ�menu��
        //menu.add("Search").setIcon(R.drawable.ic_search_inverse)
        //	.setActionView(searchview)
        //	.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
        return super.onCreateOptionsMenu(menu);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		initMap();
	}	
	
	@Override
	protected void onPause() {
		super.onPause();
		deactivate();
	}
	
	//���λ
	@Override
	public void activate(OnLocationChangedListener listener) {
		Log.i("local","---activate--> init");
		
		showDialog("���ڶ�λ");
		
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(SearchLocationActivity.this);		
			//1.0.2�汾��������������true��ʾ��϶�λ�а���gps��λ��false��ʾ�����綨λ��Ĭ����true
			mAMapLocationManager.setGpsEnable(true);
			// Location API��λ����GPS�������϶�λ��ʽ��ʱ�������5000����
			mAMapLocationManager.requestLocationUpdates( LocationProviderProxy.AMapNetwork, 5000, 10, this);
		}
	}

	//ֹͣ��λ
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.i("local","---onLocationChanged--> init");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.i("local","---onStatusChanged--> init");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.i("local","---onProviderEnabled--> init");
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.i("local","---onProviderDisabled--> init");
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onLocationChanged(location);
		}
		
		if(location != null){
			lat =location.getLatitude();
			lng =location.getLongitude();
			
			myLocation = new LatLng(lat,lng);
			CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(myLocation).zoom(16).build());
			aMap.animateCamera(update);
		}
		
		dismissDialog();
	}

	// ��ʾ�������Ի���
	public void showDialog(String message) {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage(message);
		progDialog.show();
	}

	//���ؽ������Ի���
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		Log.i("search","---beforeTextChanged--> s:" +s);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		Log.i("search","---onTextChanged--> s:" +s);
		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(SearchLocationActivity.this,
				new InputtipsListener() {

					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0) {// ��ȷ����
							List<String> listString = new ArrayList<String>();
							for (int i = 0; i < tipList.size(); i++) {
								listString.add(tipList.get(i).getName());
							}
							ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
									getApplicationContext(),
									R.layout.search_tips_item, listString);
							searchText.setAdapter(aAdapter);
							aAdapter.notifyDataSetChanged();
						}
					}
				});
		try {
			inputTips.requestInputtips(newText, "021");// ��һ��������ʾ��ʾ�ؼ��֣��ڶ�������Ĭ�ϴ���ȫ����Ҳ����Ϊ��������

		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		Log.i("search","---afterTextChanged--> s:" +s);
	}

	public void searchButton() {
		keyword = AMapUtil.checkEditText(searchText);
		if ("".equals(keyword)) {
			ToastUtil.show(SearchLocationActivity.this, "�����������ؼ���");
			return;
		} else {
			doSearchQuery();
		}
	}
	
	protected void doSearchQuery() {
		showDialog(R.string.location);// ��ʾ���ȿ�
		//currentPage = 0;
		query = new PoiSearch.Query(keyword, "", "021");
		query.setPageSize(10);// ����ÿҳ��෵�ض�����poiitem
		query.setPageNum(0);// ���ò��һҳ

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("search","---onClick--> init");
		
		switch (v.getId()) {
			case R.id.search_btn:
				searchButton();
				break;
			default:
				break;
		}		
	}

	
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		Log.i("search","---onPoiSearched--> init");
		// ���ضԻ���
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(query)) {// �Ƿ���ͬһ��
					poiResult = result;
					// ȡ����������poiitems�ж���ҳ
					List<PoiItem> poiItems = poiResult.getPois();
					// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ

					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// ����֮ǰ��ͼ��
						PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else {
						ToastUtil.show(SearchLocationActivity.this, R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(SearchLocationActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(SearchLocationActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(SearchLocationActivity.this, R.string.error_key);
		} else {
			ToastUtil.show(SearchLocationActivity.this, getString(R.string.error_other) + rCode);
		}
	}
	
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub
		Log.i("search","---onPoiItemDetailSearched--> init");
	}
}
