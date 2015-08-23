package com.stefan.city.local;

import android.location.LocationListener;
import android.location.LocationManager;

/**
 * GPSUtils GPS定位
 * 
 * @author 日期：2014-10-21下午09:35:09
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 **/
public class GPSUtils {

	private LocationManager manager;
	
	public GPSUtils(LocationManager manager) {
		this.manager = manager;
	}

	public void run(LocationListener locationListener) {
		// 从GPS_PROVIDER获取最近的定位信息
		// 判断GPS是否可用
		if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			;
		}
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
	}

}
