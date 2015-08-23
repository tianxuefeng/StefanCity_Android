package com.stefan.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;

import com.rockeagle.framework.core.REActivity;
import com.rockeagle.framework.util.AsyncImageLoader;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.tools.ParseStringUtil;
import com.stefan.city.ui.adapter.ImagePagerAdapter;

/**
 * PhotoActivity
 * 图片详细
 * @author 日期：2014-8-17上午11:28:24
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class PhotoActivity extends BaseActivity {
	
	private List<String> imgPaths;
	
	private ViewPager viewPager;
	private ImagePagerAdapter pagerAdapter;
	
	private boolean isIntent;
	private AsyncImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		viewPager = (ViewPager) findViewById(R.id.photo_viewpage);
		isIntent = true;
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isIntent = true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				String images = bundle.getString("images");
				if(images == null || images.equals("")) {
					Object object = bundle.getSerializable("imgMaps");
					if(object == null) {
						PhotoActivity.this.finish();
					}
					Map<Long, String> imgMaps = (HashMap<Long, String>) object;
					int curIndex = 0;
					if(imgMaps != null && imgMaps.size() > 0) {
						if(imgPaths != null && imgPaths.size() > 0) {
							imgPaths.clear();
						}
						imgPaths = new ArrayList<String>();
						long index = bundle.getLong("index", 0);
						Set<Long> longs = imgMaps.keySet();
						int i = 0;
						for (Long long1 : longs) {
							if(long1 == index) {
								curIndex = i;	// 获得当前选中的图片
							}
							imgPaths.add(imgMaps.get(long1));
							i++;
						}
						if(pagerAdapter == null) {
							pagerAdapter = new ImagePagerAdapter(PhotoActivity.this, imgPaths);
							viewPager.setAdapter(pagerAdapter);
						} else {
							pagerAdapter.notifyDataSetChanged();
						}
						pagerAdapter.setLocal(true);
						viewPager.setCurrentItem(curIndex);
					} else {
						PhotoActivity.this.finish();
					}
					
				} else {
					int index = bundle.getInt("index", 0);
					if(imgPaths != null && imgPaths.size() > 0) {
						imgPaths.clear();
					}
					imgPaths = ParseStringUtil.strToArray(images, "|");
					if(pagerAdapter == null) {
						pagerAdapter = new ImagePagerAdapter(PhotoActivity.this, imgPaths);
						viewPager.setAdapter(pagerAdapter);
					} else {
						pagerAdapter.notifyDataSetChanged();
					}
					// 当前选中的图片
					viewPager.setCurrentItem(index);
				}
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			// 释放内存
			viewPager.destroyDrawingCache();
			viewPager.removeAllViews();
			PhotoActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
