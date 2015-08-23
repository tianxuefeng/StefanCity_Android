package com.stefan.city;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.rockeagle.framework.core.REActivityManager;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.local.GSPLocationUtils;
import com.stefan.city.local.LocationUtils;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.thread.LocationRunnable;
import com.stefan.city.module.thread.RegionSaveGpsRunnable;

/**
 * GPSLocationActivity
 * 
 * @author 日期：2014-10-23下午08:36:13
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 **/
public class GPSLocationActivity extends BaseActivity {

//	private LocationManager manager;

	private Button btnGpsLocation, btnWelcome, btnSelectCity, btnSelectRegion, btnSelectStreet;
	
	// 国家、一级地区、二级地区
	private EditText editCountry, editAdministrative, editCity, editStreet;

	private TextView labMsg;

	private LocationRunnable locationRunnable;
	
	private LocationEntity locationEntity;
	
	private boolean isIntent;
	
	private InputMethodManager inputMethodManager;
	
	// 地区管理
	private RegionSaveGpsRunnable regionSaveGpsRunnable;
//	private RegionAddRunnable regionAddRunnable;
	
	private LocationUtils locationUtils;
	
	private TextView labTestGps;
	
	// 是否采用的谷歌定位
	private boolean isGoogleType;
	
	@SuppressWarnings("rawtypes")
	private Class activityClass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		setContentView(R.layout.activity_gps_location);
		initLayout();
		initData();
	}

	private void initLayout() { 
		btnGpsLocation = (Button) findViewById(R.id.btnLocationToGps);
		btnWelcome = (Button) findViewById(R.id.btnLocationStart);
		
		btnSelectRegion = (Button) findViewById(R.id.btn_location_select_locality);
		btnSelectCity = (Button) findViewById(R.id.btn_location_select_city);
		btnSelectStreet = (Button) findViewById(R.id.btn_location_select_street);

		editAdministrative = (EditText) findViewById(R.id.edit_location_city);
		editCountry = (EditText) findViewById(R.id.edit_location_country);
		if(Contant.curUser != null && Contant.curUser.getMemType() >= 7) {
			editCountry.setEnabled(true);
			editCountry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus) {
						// 用户输入完毕，进行国家信息获取
						String countryStr = editCountry.getText().toString();
						if(countryStr == null || countryStr.equals("")) {
							editCountry.setFocusable(true);
							Toast.makeText(GPSLocationActivity.this, R.string.msg_not_region_name, Toast.LENGTH_SHORT).show();
							return ;
						}
						if(!countryStr.equals(Contant.curLocationEntity.getCountry())) {
							Contant.curLocationEntity.setCountry(countryStr);
							Contant.curLocationEntity.setAdministrative("");
							Contant.curLocationEntity.setLocality("");
							Contant.curLocationEntity.setRegion("");
							Contant.curLocationEntity.setDetailInfo("");
							locationEntity = Contant.curLocationEntity;
							
							Contant.curStreetRegion = null;
							Contant.curRegionEntity = null;
							
							editAdministrative.setText("");
							editCity.setText("");
							editStreet.setText("");
						}
					}
				}
			});
		}
		editCity = (EditText) findViewById(R.id.edit_location_locality);
		
		editStreet = (EditText) findViewById(R.id.edit_location_street);

		labMsg = (TextView) findViewById(R.id.labLocationMsg);

		labTestGps = (TextView) findViewById(R.id.lab_gps_test);
		labTestGps.setVisibility(View.GONE);
		
		btnGpsLocation.setOnClickListener(clickListener);
		btnWelcome.setOnClickListener(clickListener);
		btnSelectRegion.setOnClickListener(clickListener);
		btnSelectStreet.setOnClickListener(clickListener);
		btnSelectCity.setOnClickListener(clickListener);
		
		locationEntity = Contant.curLocationEntity;
	}

	private void initData() {
		inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(locationEntity != null) {
			String msg = getString(R.string.lab_location_old);
			labMsg.setText(msg + locationEntity.getDetailInfo());
			
			editCountry.setText(locationEntity.getCountry());
			editAdministrative.setText(locationEntity.getAdministrative());
			editCity.setText(locationEntity.getLocality());
			if(locationEntity.getRegion() != null) {
				editStreet.setText(locationEntity.getRegion());
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 开始定位
	 */
	private void toLocation() {
		TelephonyManager tm = (TelephonyManager)GPSLocationActivity.this.getSystemService(Activity.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
		String locale = GPSLocationActivity.this.getResources().getConfiguration().locale.getCountry();
//		
//		Toast.makeText(GPSLocationActivity.this, locale, Toast.LENGTH_SHORT).show();
        if(countryCodeValue != "" && (countryCodeValue.equalsIgnoreCase("cn"))) {
			isGoogleType = false;
//			toBaiduLocation();
		} else if(countryCodeValue == "" && locale.equalsIgnoreCase("CN")) {
			isGoogleType = false;
//			toBaiduLocation();
		} else {
			isGoogleType = true;
		}
        toBaiduLocation();
	}
	
	private void toBaiduLocation() {
		// 直接采用百度定位
		labMsg.setText(R.string.lab_localing);
		btnGpsLocation.setEnabled(false);
		toBaidu();
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnLocationToGps:
				toLocation();
				break;

			case R.id.btnLocationStart: {
				gotoMain();
				break;
			}
			
			case R.id.btn_location_select_city: {
				selectCity();
				break;
			}

			case R.id.btn_location_select_locality: {
				selectRegion();
				break;
			}
			
			case R.id.btn_location_select_street: {
				selectStreet();
				break;
			}
			
			default:
				break;
			}
		}
	};
	
	/**
	 * 选择城市
	 */
	private void selectCity() {
		if(locationEntity != null && locationEntity.getCountry() != null 
				&& locationEntity.getAdministrative() != null) {
			// 跳转
			Contant.curLocationEntity = locationEntity;
			Intent intent = new Intent(GPSLocationActivity.this, CityListActivity.class);
			intent.putExtra("activityClass", GPSLocationActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 选择地区
	 */
	private void selectRegion() {
		if(locationEntity != null && locationEntity.getCountry() != null 
				&& locationEntity.getAdministrative() != null) {
			// 跳转
			Intent intent = new Intent(GPSLocationActivity.this, RegionListActivity.class);
			intent.putExtra("activityClass", GPSLocationActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 选择街道
	 */
	private void selectStreet() {
		if(locationEntity != null && locationEntity.getCountry() != null 
				&& locationEntity.getAdministrative() != null && locationEntity.getLocality() != null) {
			// 跳转
			Intent intent = new Intent(GPSLocationActivity.this, RegionStreetActivity.class);
			intent.putExtra("activityClass", GPSLocationActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 进入下一步
	 */
	private void gotoMain() {
		if(locationEntity != null && locationEntity.getCountry() != null 
				&& locationEntity.getAdministrative() != null) {
			String city = editAdministrative.getText().toString().trim();
			String region = editCity.getText().toString().trim();
			String language = SharePreferenceHelper.getSharepreferenceString(GPSLocationActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
			if(city == null || city.equals("") || region == null || region.equals("")) {
				// 地区不能为空
				Toast.makeText(GPSLocationActivity.this, R.string.msg_not_region_name, Toast.LENGTH_SHORT).show();
				return ;
			}
			locationEntity.setAdministrative(city);
			locationEntity.setLocality(region);
			Contant.curLocationEntity = locationEntity;
			if(isGoogleType) {
				// 国内不进行自动地区增加
				showDialog(DIALOG_LOAD);
				if(regionSaveGpsRunnable != null) {
					regionSaveGpsRunnable.isStop();
					regionSaveGpsRunnable = null;
				}
				regionSaveGpsRunnable = new RegionSaveGpsRunnable(handlerRegion, language, locationEntity);
				new Thread(regionSaveGpsRunnable).start();
			} else {
				saveSharePre();
				// 跳转
				Intent intent = null;
				if(activityClass == null) {
					intent = new Intent(GPSLocationActivity.this, MainActivity.class);
					intent.putExtra("locationEntity", locationEntity);
					intent.putExtra("isInit", 1);
				} else {
					intent = new Intent(GPSLocationActivity.this, activityClass);
				}
				startActivity(intent);
				GPSLocationActivity.this.finish();
			}
		} else {
			Toast.makeText(GPSLocationActivity.this, R.string.msg_local_error, 
					Toast.LENGTH_SHORT).show();
		}
	}


	private void updateInfo(Location location) {
		btnGpsLocation.setEnabled(true);
		if (location == null) {
			labMsg.setText(R.string.msg_local_error);
			return;
		}
		labMsg.setText(R.string.lab_localing_regex);
		if (locationRunnable != null) {
			locationRunnable.isStop();
			locationRunnable = null;
		}
		GSPLocationUtils.updateWithNewLocation(GPSLocationActivity.this, location);
		String language = SharePreferenceHelper.getSharepreferenceString(
				GPSLocationActivity.this, Contant.SETTINGSP,
				Contant.PREF_LANGUAGE, "zh-CN");
		locationRunnable = new LocationRunnable(GPSLocationActivity.this,
				handler, location.getLatitude(), location.getLongitude(),
				language);
		new Thread(locationRunnable).start();
	}

	private Handler handlerRegion = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			switch (msg.what) {
			case 0:
				break;
				
			case 1:
				if(msg.obj != null) {
					locationEntity.setLocality(msg.obj.toString());
				}
				break;

			default:
				break;
			}
			saveSharePre();
			// 跳转
			Intent intent = null;
			if(activityClass == null) {
				intent = new Intent(GPSLocationActivity.this, MainActivity.class);
				intent.putExtra("locationEntity", locationEntity);
				intent.putExtra("isInit", 1);
			} else {
				intent = new Intent(GPSLocationActivity.this, activityClass);
			}
			startActivity(intent);
			GPSLocationActivity.this.finish();
		};
	};
	
	/**
	 * 用户处理定位回调信息
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			btnGpsLocation.setEnabled(true);
			if (msg.what == 1) {
				BDLocation location = (BDLocation) msg.obj;
				if(locationUtils != null) {
					locationUtils.getmLocationClient().stop();
				}
				if (locationRunnable != null) {
					locationRunnable.isStop();
					locationRunnable = null;
				}
				labTestGps.setText("Latitude:"+location.getLatitude()+
						",Longitude:"+location.getLongitude());
				String language = SharePreferenceHelper.getSharepreferenceString(
						GPSLocationActivity.this, Contant.SETTINGSP,
						Contant.PREF_LANGUAGE, "zh-CN");
				locationRunnable = new LocationRunnable(GPSLocationActivity.this,
						handler, location.getLatitude(), location.getLongitude(),
						language);
				new Thread(locationRunnable).start();
			} else if(msg.what == 5) {
				// 反向地理编码完成, 定位结束
				LocationEntity entity = (LocationEntity) msg.obj;
				if(entity != null) {
					labMsg.setText(entity.getDetailInfo());
					locationEntity = entity;
					Contant.curLocationEntity = locationEntity;
					editCountry.setText(entity.getCountry());
					editAdministrative.setText(entity.getAdministrative());
					editCity.setText(entity.getLocality());
					if(entity.getRegion() != null) {
						editStreet.setText(entity.getRegion());
					}
					saveSharePre();
				}
			} else {
				labMsg.setText(R.string.msg_local_error);
			}
			
		};
	};
	
	/**
	 * 将定位信息保存下来
	 */
	private void saveSharePre() {
		// 将当前的定位信息保存下来
		SharePreferenceHelper.setSharepreferenceString(GPSLocationActivity.this, 
				Contant.SETTINGSP, Contant.PREF_COUNTRY, locationEntity.getCountry());
		
		SharePreferenceHelper.setSharepreferenceString(GPSLocationActivity.this, 
				Contant.SETTINGSP, Contant.PREF_CITY, locationEntity.getAdministrative());
		
		SharePreferenceHelper.setSharepreferenceString(GPSLocationActivity.this, 
				Contant.SETTINGSP, Contant.PREF_LOCALITY, locationEntity.getLocality());
		
//		SharePreferenceHelper.setSharepreferenceString(GPSLocationActivity.this, 
//				Contant.SETTINGSP, Contant.PREF_STREET, locationEntity.getRegion());
		
		SharePreferenceHelper.setSharepreferenceString(GPSLocationActivity.this, 
				Contant.SETTINGSP, Contant.PREF_DETAIL, locationEntity.getDetailInfo());
		
		SharePreferenceHelper.setSharepreferenceString(GPSLocationActivity.this, 
				Contant.SETTINGSP, Contant.PREF_LATITUDE, locationEntity.getLatitude()+"");
		
		SharePreferenceHelper.setSharepreferenceString(GPSLocationActivity.this, 
				Contant.SETTINGSP, Contant.PREF_LONGITUDE, locationEntity.getLongitude()+"");
	}
	
	private void toBaidu() {
		if(locationUtils == null) {
			locationUtils = new LocationUtils(GPSLocationActivity.this, handler);
			locationUtils.onCreate();
			locationUtils.start();
		} else {
			if(locationUtils.isStarted()) {
				locationUtils.stop();
			}
			locationUtils.start();
		}
		editAdministrative.setText("");
		editCity.setText("");
		editStreet.setText("");
	}
	
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isIntent = true;
	};
	
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
					if(btnWelcome != null) {
						btnWelcome.setText(R.string.btnOK);
					}
				}
			}
		}
		if(Contant.curLocationEntity == null) {
			toLocation();
		} else {
			if(locationEntity == null) {
				locationEntity = Contant.curLocationEntity;
			}
			String cityStr = editAdministrative.getText().toString();
			if(cityStr != null && !cityStr.equals("")) {
				if(!cityStr.equals(Contant.curLocationEntity.getAdministrative())) {
					locationEntity.setAdministrative(Contant.curLocationEntity.getAdministrative());
					editAdministrative.setText(Contant.curLocationEntity.getAdministrative());
					// 如果重新选择的城市，那么清空已选择的地区信息
					editCity.setText("");
					return;
				}
			} else {
				locationEntity.setAdministrative(Contant.curLocationEntity.getAdministrative());
				editAdministrative.setText(Contant.curLocationEntity.getAdministrative());
			}
		}
		if(locationEntity != null && Contant.curRegionEntity != null) {
			String cityStr = editCity.getText().toString();
			locationEntity.setLocality(Contant.curRegionEntity.getName());
			editCity.setText(Contant.curRegionEntity.getName());
			if(cityStr != null && !cityStr.equals(Contant.curRegionEntity.getName())) {
				// 城市发生改变
				editStreet.setText("");
			}
		}
		if(locationEntity != null && Contant.curStreetRegion != null) {
			locationEntity.setRegion(Contant.curStreetRegion.getName());
			editStreet.setText(Contant.curStreetRegion.getName());
		}
		if(editCountry != null && Contant.curUser != null && Contant.curUser.getMemType() >= 7) {
			editCountry.setEnabled(true);
		} else if(editCountry != null && (Contant.curUser == null || Contant.curUser.getMemType() < 7)) {
			editCountry.setEnabled(false);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(locationUtils != null) {
			locationUtils.stop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(locationUtils != null) {
			locationUtils.stop();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			GPSLocationActivity.this.finish();
			if(activityClass == null) {
				REActivityManager.getActivityManager().exit();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
