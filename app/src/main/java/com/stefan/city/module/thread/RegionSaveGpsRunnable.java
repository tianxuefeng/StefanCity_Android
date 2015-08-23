package com.stefan.city.module.thread;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.service.RegionService;

/**
 * RegionSaveGpsRunnable
 * @author 日期：2015-2-15下午06:37:37
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegionSaveGpsRunnable extends REBaseRunnable {
	
	private RegionService regionService;
	
	private String language;
	
	private LocationEntity locationEntity;

	public RegionSaveGpsRunnable(Handler handler, String language, LocationEntity locationEntity) {
		super(handler);
		regionService = new RegionService();
		this.language = language;
		this.locationEntity = locationEntity;
	}

	@Override
	public void run() {
		int what = 0;
		if (locationEntity != null && REHttpUtility.isCheckConOK()) {
			// 如果当前定位的数据不存在，那么新增到数据库中
			boolean bool = regionService.isManagedCity(locationEntity.getAdministrative(), language);
			if(!bool) {
				// 新增当前定位的城市
				RegionManEntity entityCity = new RegionManEntity();
				entityCity.setParentName(locationEntity.getCountry());
				entityCity.setName(locationEntity.getAdministrative());
				entityCity.setLanguageCode(language);
				// 新增到数据库中
				what = regionService.insert(entityCity);
//				curRegionEntity 新增当前定位的地区
				RegionManEntity entity = new RegionManEntity();
				entity.setName(locationEntity.getLocality());
				entity.setLanguageCode(language);
				entity.setParentName(locationEntity.getAdministrative());
				// 如果当前地区未收录，那么新增到数据库中
				what = regionService.insert(entity);
				
				// 收录当前的街道信息
				if(locationEntity.getRegion() != null) {
					RegionManEntity streetEntity = new RegionManEntity();
					streetEntity.setName(locationEntity.getRegion());
					streetEntity.setLanguageCode(language);
					streetEntity.setParentName(locationEntity.getLocality());
					// 如果当前地区未收录，那么新增到数据库中
					what = regionService.insert(streetEntity);
					Contant.curStreetRegion = streetEntity;
				}
				
				// 解析成功后，再去api中获得当前城市列表
				List<RegionManEntity> list = regionService.getList(locationEntity.getAdministrative(), language);
				if(list == null || list.size() <= 0) {
					list = new ArrayList<RegionManEntity>();
					list.add(entity);
				}
				for (RegionManEntity regionManEntity : list) {
					// 得到当前选中的地区
					if(regionManEntity.getName().equalsIgnoreCase(locationEntity.getLocality())) {
						Contant.curRegionEntity = regionManEntity;
					}
				}
				Contant.regionManEntities = list;
				sendMessage(what, entity.getName());
			} else {
				what = 0;
				sendMessage(what, null);
			}
		} else {
			what = -1;
			sendMessage(what, null);
		}
	}

}
