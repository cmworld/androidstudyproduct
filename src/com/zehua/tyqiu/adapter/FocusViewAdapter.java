package com.zehua.tyqiu.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class FocusViewAdapter extends PagerAdapter {
	
	private ImageView[] mImageViews;
	
	public FocusViewAdapter(ImageView[] arr){
		mImageViews = arr;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);
		
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
		return mImageViews[position % mImageViews.length];
	}
	
}
