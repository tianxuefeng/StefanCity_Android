package com.stefan.city.ui.adapter;

import java.util.List;

import com.stefan.city.ui.page.view.BasePageView;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class PageItemAdapter extends PagerAdapter {
	
	private List<BasePageView> mViewArray;

	public PageItemAdapter(List<BasePageView> mViewArray) {
		super();
		this.mViewArray = mViewArray;
	}

	@Override
	public int getCount() {
		return mViewArray.size();
	}

	@Override
	public Object instantiateItem(View pager, int position) {
		View v = mViewArray.get(position);
		if (v.getParent() == null) {
			((ViewPager) pager).addView(v, 0);
		}
		return v;
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView(mViewArray.get(position));
	}

	@Override
	public boolean isViewFromObject(View view, Object xview) {
		String viewName = view.getClass().getSimpleName();
		String xviewName = xview.getClass().getSimpleName();
		return viewName.equals(xviewName);
	}

	@Override
	public void finishUpdate(View view) {
	}

	@Override
	public void restoreState(Parcelable p, ClassLoader c) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {

	}
}