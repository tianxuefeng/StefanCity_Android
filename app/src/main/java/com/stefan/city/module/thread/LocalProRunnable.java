package com.stefan.city.module.thread;

import java.util.List;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.ProvinceEntity;
import com.stefan.city.module.service.CityDataService;

import android.content.Context;
import android.os.Handler;

/**
 * LocalCityRunnable
 * 定位省份
 * @author 日期：2014-7-12下午04:42:56
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class LocalProRunnable extends REBaseRunnable {

	private CityDataService cityDataService;
	
	public LocalProRunnable(Context context, Handler handler) {
		super(handler);
		cityDataService = new CityDataService(context);
	}

	@Override
	public void run() {
		List<ProvinceEntity> list = cityDataService.findProList();	// 得到省份信息
		sendMessage(1, list);
	}

}
