package com.stefan.city.module.thread;

import java.util.ArrayList;
import java.util.List;

//import com.google.android.gms.internal.en;
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
public class RegionTwoListRunnable extends REBaseRunnable {
	
	private RegionService regionService;
	
	private String language;
	
	private String cityName;

	public RegionTwoListRunnable(Handler handler, String cityName, String language) {
		super(handler);
		this.cityName = cityName;
		this.language = language;
		regionService = new RegionService();
	}

	@Override
	public void run() {
		int what = 0;
//		List<String> strings = null;
		List<String> strings = null;
		List<RegionManEntity> entities = null;
		if (REHttpUtility.isCheckConOK()) {
			List<RegionManEntity> list  = regionService.getTwoLevelRegionList(cityName, language);
			if(list != null && list.size() > 0) {
				strings = new ArrayList<String>();
				entities = new ArrayList<RegionManEntity>();
				for (RegionManEntity regionManEntity : list) {
					String name = regionManEntity.getName();
					if(regionManEntity.getParentName().equals(cityName)) {
						entities.add(regionManEntity);
						strings.add(name);
						for (RegionManEntity entity : list) {
							if(entity.getParentName().equals(name)) {
								entity.setName("     "+entity.getName());
								entities.add(entity);
								strings.add(entity.getName());
							}
						}
					}
				}
				what = 1;
			}
		} else {
			what = -1;
		}
		int size = strings == null ? 0 : strings.size();
		String[] nameTemp = new String[size];
		RegionManEntity[] entitiesT = new RegionManEntity[size];
		if(strings != null) {
			sendMessage(what, new Object[] {strings.toArray(nameTemp), entities.toArray(entitiesT)});
		} else {
			sendMessage(what, new Object[] {nameTemp, entitiesT});
		}
	}

}
