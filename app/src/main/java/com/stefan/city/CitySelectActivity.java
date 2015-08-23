package com.stefan.city;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.rockeagle.framework.core.REActivityManager;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.local.LocationUtils;
import com.stefan.city.module.db.CityDBManage;
import com.stefan.city.module.db.RECityData;
import com.stefan.city.module.entity.CityModel;
import com.stefan.city.module.entity.RegionEntity;
import com.stefan.city.ui.RELetterListView;
import com.stefan.city.ui.RELetterListView.OnTouchingLetterChangedListener;
import com.stefan.city.ui.adapter.CityInfoAdapter;

/**
 * CitySelectActivity
 * @author 日期：2014-7-21下午11:00:00
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 *               @deprecated	国外版本不用到这个
 **/
public class CitySelectActivity extends BaseActivity {
	
	private CityInfoAdapter cityInfoAdapter;
	
	private ListView cityListView;
	private TextView overlay;
	
	private RELetterListView letterListView;
	
	private OverlayThread overlayThread;
	private Handler handler;
	
	private ArrayList<CityModel> mCityNames;
	private Button btnSearch, btnSelectCur;
	private EditText editSearch;
	
	private RECityData reCityData;
	
	private TextView txtCurCity, txtCurArea, txtCurLocal;
	private LocationUtils locationUntils;
	
	private RegionEntity regionEntity;
	
	private Class activityClass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		initLayout();
		handler = new Handler();
		reCityData = new RECityData();
		reCityData.openOrCreateDatabase(CityDBManage.DB_PATH + "/"
				+ CityDBManage.DB_NAME);
		mCityNames = reCityData.getCityNames();
		letterListView
		.setOnTouchingLetterChangedListener(letterChangedListener);
		overlayThread = new OverlayThread();
		updateAdapter();
	}
	
	private void initLayout() {
		setContentView(R.layout.activity_city_list);
		btnSearch = (Button) findViewById(R.id.btn_city_search);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = editSearch.getText().toString().trim();
				mCityNames.clear();
				mCityNames.addAll(reCityData.getSelectCityNames(content));
				updateAdapter();
			}
		});
		editSearch = (EditText) findViewById(R.id.txt_city_search);
		overlay = (TextView) findViewById(R.id.city_overlay);
		
		cityListView = (ListView) findViewById(R.id.city_listview);
		cityListView.setOnItemClickListener(itemClickListener);
		letterListView = (RELetterListView) findViewById(R.id.cityLetterListView);
		
		txtCurCity = (TextView) findViewById(R.id.edit_cityLocalCity);
		txtCurArea = (TextView) findViewById(R.id.edit_cityLocalArea);
		
		btnSelectCur = (Button) findViewById(R.id.btn_city_sel_cur);
		btnSelectCur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(regionEntity == null) {
					Toast.makeText(CitySelectActivity.this, R.string.msg_city_select_city_error, Toast.LENGTH_SHORT).show();
				} else {
					gotoMain();
				}
			}
		});
		
		txtCurLocal = (TextView) findViewById(R.id.txt_city_curLocal);
		txtCurLocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int i = (Integer) txtCurLocal.getTag();
				if(i == -1) {
					txtCurLocal.setTag(1);
					txtCurLocal.setText(R.string.lab_localing);
					toLocal();
				}
			}
		});
		// 正在定位中
		txtCurLocal.setTag(1);
		toLocal();
	}
	
	private boolean isIntent = false;
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isIntent = true;
	}
	
	private boolean isMain;
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				Object object = bundle.get("activityClass");
				if(object != null) {
					activityClass = (Class) object;
				} else {
					activityClass = MainActivity.class;
				}
				isMain = bundle.getBoolean("isMain", false);
			}
		}
	}
	
	private void gotoMain() {
		if(activityClass == null) {
			activityClass = MainActivity.class;
		}
		if(activityClass == MainActivity.class) {
//			Contant.curCity = regionEntity;
		}
		Intent intent = new Intent(CitySelectActivity.this, activityClass);
		intent.putExtra("regionEntity", regionEntity);
		startActivity(intent);
		CitySelectActivity.this.finish();
	}
	
	private void toLocal() {
//		if(locationUntils == null) {
//			locationUntils = new LocationUntils(CitySelectActivity.this, localHandler);
//			locationUntils.onCreate();
//			locationUntils.start();
//		} else {
//			if(locationUntils.isStarted()) {
//				locationUntils.stop();
//			}
//			locationUntils.start();
//		}
	}
	
	private Handler localHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 5) {
//				locationUntils.getmLocationClient().stop();
//				txtCurLocal.setTag(0);	// 定位结束
//				BDLocation location = (BDLocation) msg.obj;
////				location = null;
//				if(location == null || location.getProvince() == null || location.getProvince().equals("null")) {
//					txtCurArea.setText("");
//					txtCurCity.setText("");
//					txtCurLocal.setTag(-1);
//					txtCurLocal.setText(R.string.msg_local_error);
//					regionEntity = null;
//				} else {
//					txtCurLocal.setTag(2);	// 定位成功
//					txtCurLocal.setText(R.string.msg_local_success);
//					regionEntity = new RegionEntity();
//					regionEntity.setCityName(location.getCity());
//					regionEntity.setParentId(location.getCityCode());
//					regionEntity.setName(location.getDistrict());
//					
//					txtCurArea.setText(location.getDistrict());
//					txtCurCity.setText(location.getCity());
//				}
			}
		};
	};
	
	private OnTouchingLetterChangedListener letterChangedListener = new OnTouchingLetterChangedListener() {
		
		@Override
		public void onTouchingLetterChanged(String s) {
			Integer index = cityInfoAdapter.getAlphaIndexer().get(s);
			if (index != null) {
				int position = index;
				cityListView.setSelection(position);
				overlay.setText(cityInfoAdapter.getSections()[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			CityModel cityModel = (CityModel) cityListView.getAdapter()
			.getItem(pos);
			// 选中地区
			regionEntity = new RegionEntity();
			regionEntity.setCityName(cityModel.getCityName());
			gotoMain();
		}
	};

	/**
	 * 为ListView设置适配器
	 * 
	 * @param list
	 */
	private void updateAdapter() {
		if (mCityNames != null) {
			if(cityInfoAdapter == null) {
				cityInfoAdapter = new CityInfoAdapter(this, mCityNames);
				cityListView.setAdapter(cityInfoAdapter);
			} else {
				cityInfoAdapter.notifyDataSetChanged();
			}
		}
	}
	
	// 设置overlay不可见
	private class OverlayThread implements Runnable {

		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(isMain) {
				REActivityManager.getActivityManager().exit();
			} else {
				CitySelectActivity.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
