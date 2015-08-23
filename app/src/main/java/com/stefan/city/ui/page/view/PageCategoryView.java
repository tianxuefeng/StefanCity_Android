package com.stefan.city.ui.page.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.InfoListActivity;
import com.stefan.city.MainActivity;
import com.stefan.city.R;
import com.stefan.city.RegionListActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.thread.CategoryRunnable;
import com.stefan.city.ui.adapter.CategoryAdapter;
import com.stefan.city.ui.floatview.FloatLocatCityView;

/**
 * PageCategoryView
 * @author 日期：2014-6-30下午01:39:32
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class PageCategoryView extends BasePageView {
	
	private CategoryAdapter categoryAdapter;
	private GridView gridView;
	
	private Button btnAddress;
	
	private TextView labTitle, labReset;
	
	private List<CategoryEntity> list;
	
	private CategoryRunnable categoryRunnable;
	
	private boolean isUpdate;	// 是否强制更新数据
	
	// 当前选择的地区
	private RegionManEntity regionManEntity;
	
//	private RegionEntity regionEntity;	// 当前所在城市
	
	private FloatLocatCityView floatLocatCityView;
	
	public PageCategoryView(Context context) {
		super(context);
		initLayout();
	}
	
	private void initLayout() {
		View view = inflate(getContext(), R.layout.page_category, null);
		
		gridView = (GridView) view.findViewById(R.id.sortGridView);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CategoryEntity entity = list.get(arg2);
				if(entity != null) {
					Intent intent = new Intent(getContext(), InfoListActivity.class);
					intent.putExtra("categoryEntity", entity);
					intent.putExtra("regionManEntity", regionManEntity);
					getContext().startActivity(intent);
				}
			}
		});
		
		labReset = (TextView) view.findViewById(R.id.lab_main_category_notinfo);
		labReset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				labReset.setVisibility(View.GONE);
				loadData();
			}
		});
		labTitle = (TextView) view.findViewById(R.id.labMainAddress);
		
		btnAddress = (Button) view.findViewById(R.id.btnMainAddress);
		btnAddress.setOnClickListener(clickListener);
		this.addView(view);
		
		floatLocatCityView = new FloatLocatCityView(getContext(), new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				floatLocatCityView.dismiss();
				regionManEntity = Contant.regionManEntities.get(arg2);
			}
		});
		floatLocatCityView.setView(btnAddress);
	}
	
	@Override
	protected void loadData() {
		if(list == null || list.size() < 1 || isUpdate) {
			isUpdate = false;
			if(categoryRunnable != null) {
				categoryRunnable.isStop();
				categoryRunnable = null;
			}
			String language = SharePreferenceHelper.getSharepreferenceString(getContext(), Contant.SETTINGSP, Contant.PREF_LANGUAGE, "zh");
			categoryRunnable = new CategoryRunnable(handler, "0", language);
			((MainActivity) getContext()).showLoading();
			new Thread(categoryRunnable).start();
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), RegionListActivity.class);
			intent.putExtra("activityClass", MainActivity.class);
			getContext().startActivity(intent);
//			if(floatLocatCityView.getList() == null) {
//				floatLocatCityView.setList(Contant.regionManEntities);
//			}
//			if(!floatLocatCityView.isShowing()) {
//				floatLocatCityView.showView();
//			} else {
//				floatLocatCityView.dismiss();
//			}
		}
	};
	
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			((MainActivity) getContext()).dismissLoading();
			if(msg.obj != null) {
				list = (ArrayList<CategoryEntity>) msg.obj;
				if(list != null && list.size() > 0) {
					labReset.setVisibility(View.GONE);
					gridView.setVisibility(View.VISIBLE);
					if(categoryAdapter == null) {
						categoryAdapter = new CategoryAdapter(getContext(), list);
						gridView.setAdapter(categoryAdapter);
					} else {
						categoryAdapter.notifyDataSetChanged();
					}
				} else {
					labReset.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.GONE);
				}
			} else {
				labReset.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.GONE);
			}
		};
	};

	@Override
	protected void initVal() {
	}

	@Override
	protected void pageLoadData() {}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void onResume() {
		loadData();
		if(Contant.curLocationEntity != null) {
			labTitle.setText(Contant.curLocationEntity.getAdministrative() + " "  + getContext().getString(R.string.title_appname));
		}
	}
	
	@Override
	public void onResume(boolean isLoad) {
		if(isLoad) {
			onResume();
		} else {
			// 只加载地区名称
			if(Contant.curLocationEntity != null) {
				labTitle.setText(Contant.curLocationEntity.getAdministrative() + " " + getContext().getString(R.string.title_appname));
			}
		}
	}

	public RegionManEntity getRegionEntity() {
		return regionManEntity;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
//		if(regionEntity == null) {
//			regionEntity = Contant.curCity;
//		}
	}

	public void setRegionManEntity(RegionManEntity regionManEntity) {
		this.regionManEntity = regionManEntity;
		floatLocatCityView.setList(Contant.regionManEntities);
	}

}
