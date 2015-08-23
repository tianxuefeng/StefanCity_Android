package com.stefan.city.local;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.stefan.city.R;
import com.stefan.city.module.entity.LocationEntity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 定位回调
 * LocationApplication
 * @author 日期：2014-7-12下午04:47:26
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 *
 */
public class LocationUtils {
	
	private LocationClient mLocationClient;
//	private GeofenceClient mGeofenceClient;
	private MyLocationListener mMyLocationListener;
	
	private Context context;
	private Handler handler;
	
//	private RegionService regionService;
	
	public LocationUtils(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
//		regionService = new RegionService();
	}
	
	public void onCreate() {
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
//		mGeofenceClient = new GeofenceClient(context);
		
		initLocation();
	}

	
	/**
	 * 定位回调接口
	 * MyLocationListener
	 * @author 日期：2014-7-12下午04:48:06
	 * @author 作者：岩鹰
	 * @author 邮箱：jyanying@163.com
	 * @version 0.1
	 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
	 *               All Rights Reserved.
	 *
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if(location != null) {
//				LocationEntity locationEntity = new LocationEntity();
//				String country = context.getString(R.string.country_china);
//				// 在这里进行处理，将百度定位的location信息转换成程序定义的地区结构，并且通过handler返回过去
//				locationEntity.setCountry(country);
//				locationEntity.setAdministrative(location.getCity());
//				locationEntity.setLocality(location.getDistrict());
//				locationEntity.setRegion(location.getStreet());
//				
//				locationEntity.setLatitude(location.getLatitude()+"");
//				locationEntity.setLongitude(location.getLongitude()+"");
//				locationEntity.setDetailInfo(country + location.getAddrStr());
				
				Message msg = handler.obtainMessage(1, location);
				handler.sendMessage(msg);
			} else {
				handler.sendEmptyMessage(-1);
			}
			
		}
	}

	public LocationClient getmLocationClient() {
		return mLocationClient;
	}

	public void start() {
		if(!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
	}

	public boolean isStarted() {
		return mLocationClient.isStarted();
	}
	
	public void stop() {
		if(mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}
	
	private void initLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("gcj02");
		int span = 2000;
//		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
	}
	
}
