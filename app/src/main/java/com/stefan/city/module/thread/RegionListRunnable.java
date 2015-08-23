package com.stefan.city.module.thread;

import java.util.List;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.service.RegionService;

import android.os.Handler;

/**
 * RegionListRunnable
 * @author 日期：2014-10-29下午08:07:00
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegionListRunnable extends REBaseRunnable {
	
	private RegionService regionService;
	
	private String language;
	
	private String cityName;

	public RegionListRunnable(Handler handler, String cityName, String language) {
		super(handler);
		this.cityName = cityName;
		this.language = language;
		regionService = new RegionService();
	}

	@Override
	public void run() {
		int what = 0;
		List<RegionManEntity> list = null;
		if (REHttpUtility.isCheckConOK()) {
			list = regionService.getList(cityName, language);
			if(list != null && list.size() > 0) {
				what = 1;
			}
		} else {
			what = -1;
		}
		sendMessage(what, list);
	}

}
