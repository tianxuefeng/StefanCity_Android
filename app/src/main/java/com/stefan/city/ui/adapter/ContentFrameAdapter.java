package com.stefan.city.ui.adapter;

import java.util.List;

import com.stefan.city.ui.page.ContentFrame;
import com.stefan.city.ui.page.Navigation;
import com.viewpagerindicator.IconPagerAdapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContentFrameAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

	private List<ContentFrame> fragments;

	public ContentFrameAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setList(List<ContentFrame> list) {
		fragments = list;
	}

	@Override
	public Fragment getItem(int pos) {
		if (fragments == null) {
			return null;
		}
		return fragments.get(pos);
	}

	@Override
	public int getCount() {
		if (fragments != null) {
			return fragments.size();
		} else {
			return 0;
		}
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		if (fragments != null) {
			return fragments.get(position).getNavigation().getTitle();
		}
		return super.getPageTitle(position);
	}

	@Override
	public int getIconResId(int pos) {
		Navigation navigation = (Navigation) fragments.get(pos).getNavigation();
		return navigation.getIconId();
	}

}
