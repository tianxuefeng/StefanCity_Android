package com.stefan.city.module.listener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * RELocationListner 实现了监听定位的接口
 * 
 * @author 日期：2014-7-8上午11:54:44
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 **/
public class RELocationListner implements LocationListener {

	private LocationManager locationManager;

	private Location currentLocation;

	private LocationListener gpsListener = null;
	private LocationListener networkListner = null;
	
	private Handler handler;
	
	public RELocationListner() {
	}

	public void registerLocationListener(Context context, Handler handler) {
		this.handler = handler;
		
		locationManager = (LocationManager) context
        .getSystemService(Context.LOCATION_SERVICE);
		
//		networkListner = new RELocationListner();
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 3000, 1, this);
//		gpsListener = new RELocationListner();
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				5000, 0, gpsListener);
	}

	@Override
	public void onLocationChanged(Location location) {
		// Called when a new location is found by the location provider.
		Log.v("GPSTEST",
				"Got New Location of provider:" + location.getProvider());
		if (currentLocation != null) {
			if (isBetterLocation(location, currentLocation)) {
				Log.v("GPSTEST", "It's a better location");
				currentLocation = location;
				showLocation(location);
			} else {
				Log.v("GPSTEST", "Not very good!");
			}
		} else {
			Log.v("GPSTEST", "It's first location");
			currentLocation = location;
			showLocation(location);
		}
		// 移除基于LocationManager.NETWORK_PROVIDER的监听器
		if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
			locationManager.removeUpdates(this);
		}
	}

	// 后3个方法此处不做处理
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onProviderDisabled(String provider) {
	}

	private void showLocation(Location location) {
		// 纬度
		Log.v("GPSTEST", "Latitude:" + location.getLatitude());
		// 经度
		Log.v("GPSTEST", "Longitude:" + location.getLongitude());
		// 精确度
		Log.v("GPSTEST", "Accuracy:" + location.getAccuracy());
		Message msg = Message.obtain(handler);
		msg.obj = location;
		handler.sendMessage(msg);
		// Location还有其它属性，请自行探索
		// 关掉GPS定位
//		locationManager.removeUpdates(gpsListener);
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	private static final int CHECK_INTERVAL = 1000 * 30;

	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location,
		// use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must
			// be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
