package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.stefan.city.base.BaseActivity;
import com.stefan.city.local.LocationUtils;
import com.stefan.city.module.entity.CityEntity;
import com.stefan.city.module.entity.ProvinceEntity;
import com.stefan.city.module.service.CityDataService;
import com.stefan.city.module.thread.LocalProRunnable;
import com.stefan.city.ui.adapter.LocalProAdapter;

/**
 * LocalActivity
 * 定位、选择所在位置
 * @author 日期：2014-7-8上午11:37:21
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 *               @deprecated
 **/
public class LocalActivity extends BaseActivity {
	
	private ListView listView;
	private LocalProAdapter proAdapter;
	private LocalProRunnable localProRunnable;
	
	private List<ProvinceEntity> list;
	private ProvinceEntity entity, curEntity;	// 定位所在省份
	
	private CityEntity cityEntity;	// 定位所在的城市
	
	private LocationUtils locationUntils;
	
	private boolean isMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);
		initLayout();
	}
	
	private void initLayout() {
		listView = (ListView) findViewById(R.id.local_listview);
		listView.setOnItemClickListener(itemClickListener);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			if(index == 0) {
				if(entity == null) {
					return ;
				} else if (entity.getLocalStatus() == 1) {
					CityDataService dataService = new CityDataService(LocalActivity.this);
					List<ProvinceEntity> list = dataService.findProByName(entity.getName());
					if(list != null && list.size() > 0) {
						entity.setProvinceId(list.get(0).getProvinceId());
						cityEntity.setParentId(entity.getProvinceId());
					}
					List<CityEntity> cityEntities = dataService.findByName(cityEntity.getCityName());
					if(cityEntities != null && cityEntities.size() > 0) {
						cityEntity.setCityId(cityEntities.get(0).getCityId());
					}
					gotoMain(entity);
				} else if (entity.getLocalStatus() == 0) {
					Toast.makeText(LocalActivity.this, R.string.msg_local_click, Toast.LENGTH_SHORT).show();
				} else if (entity.getLocalStatus() == -1) {
					entity.setLocalStatus(0);
					entity.setName(getString(R.string.lab_localing));
//					toLocal();
				}
			} else {
				curEntity = list.get(index);
				gotoMain(curEntity);
			}
		}
	};
	
	public void gotoMain(ProvinceEntity entity) {
		if(isMain) {
			Intent intent = new Intent(LocalActivity.this, MainActivity.class);
			intent.putExtra("provinceEntity", entity);
			if(cityEntity != null && cityEntity.getCityId() != null && !cityEntity.getCityId().equals("")) {
				if(cityEntity.getParentId().equals(entity.getProvinceId())) {
					intent.putExtra("cityEntity", cityEntity);
				}
			}
			startActivity(intent);
		} else {
//			Contant.curCity = entity;
		}
		finish();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(list == null || list.size() < 1) {
			if(localProRunnable != null) {
				localProRunnable.isStop();
				localProRunnable = null;
			}
			localProRunnable = new LocalProRunnable(LocalActivity.this, handler);
			new Thread(localProRunnable).start();
		}
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			isMain = bundle.getBoolean("isMain", false);
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1 && msg.obj != null) {
				list = (ArrayList<ProvinceEntity>) msg.obj;
				entity = new ProvinceEntity("-1", getString(R.string.lab_localing));
				entity.setLocalStatus(0);
				list.add(0, entity);
				if(proAdapter == null) {
					proAdapter = new LocalProAdapter(LocalActivity.this, list);
					listView.setAdapter(proAdapter);
				} else {
					proAdapter.notifyDataSetChanged();
				}
//				toLocal();
			} else if (msg.what == 5) {
//				locationUntils.getmLocationClient().stop();
//				BDLocation location = (BDLocation) msg.obj;
//				if(location == null || location.getProvince() == null || location.getProvince().equals("null")) {
//					entity.setName(getString(R.string.msg_local_error));
//					entity.setLocalStatus(-1);			
//					proAdapter.notifyDataSetChanged();
//				} else {
//					entity.setName(location.getProvince());
//					entity.setLocalName(getString(R.string.lab_local_cur) + entity.getName());
//					entity.setLocalStatus(1);
//					proAdapter.notifyDataSetChanged();
//					
//					cityEntity = new CityEntity("0", location.getCity(), "0");
//				}
			}
		};
	};
	
//	private void toLocal() {
//		if(locationUntils == null) {
//			locationUntils = new LocationUntils(LocalActivity.this, handler);
//			locationUntils.onCreate();
//			locationUntils.start();
//		} else {
//			if(locationUntils.isStarted()) {
//				locationUntils.stop();
//			}
//			locationUntils.start();
//		}
//	}
	
}
