package com.stefan.city.module.thread;

import java.util.ArrayList;
import java.util.List;

import com.stefan.city.local.LocationJsonUtil;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.service.RegionService;

import android.content.Context;
import android.os.Handler;

/**
 * LocationRunnable
 * 根据传入的经纬度来获取指定的位置信息
 * @author 日期：2014-10-25下午10:13:13
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class LocationRunnable extends REBaseRunnable {
	
	private LocationEntity locationEntity;
	private double latitude, longitude;
	private String language;
	
	private Context context;
	
	private RegionService regionService;
	
//	private static final String URL = "http://maps.google.com/maps/api/geocode/json?latlng=%s,%s&language=%s&sensor=true";

	public LocationRunnable(Context context, Handler handler, double latitude, double longitude, String language) {
		super(handler);
		this.context = context;
		this.latitude = latitude /*1.333179*/;
		this.longitude = longitude /*103.850283*/;
		this.language = language;
		regionService = new RegionService();
	}

	@Override
	public void run() {
		String result = null;
		result = regionService.getLocation(latitude, longitude, language);//(1.350571d, 103.850283, language);
		if(result != null && !result.equals("")) {
			// 采用对谷歌反向地理编码的数据进行解析
			toGoogle(result);
		}
		if(locationEntity == null) {
			sendMessage(-2, null);	
		} else {
			sendMessage(5, locationEntity);
		}
	}
	
	/**
	 * 采用谷歌定位解析
	 */
	private void toGoogle(String result) {
		locationEntity = LocationJsonUtil.regexJson(result);
		if(locationEntity != null) {
			// 解析成功后，再去api中获得当前城市列表
			List<RegionManEntity> list = regionService.getList(locationEntity.getAdministrative(), language);
			// 新增当前定位的城市
//			RegionManEntity entityCity = new RegionManEntity();
//			entityCity.setParentName(locationEntity.getCountry());
//			entityCity.setName(locationEntity.getAdministrative());
//			entityCity.setLanguageCode(language);
//			// 新增到数据库中
//			regionService.insert(entityCity);
////				curRegionEntity 新增当前定位的地区
			RegionManEntity entity = new RegionManEntity();
			entity.setName(locationEntity.getLocality());
			entity.setLanguageCode(language);
			entity.setParentName(locationEntity.getAdministrative());
//			// 如果当前地区未收录，那么新增到数据库中
//			regionService.insert(entity);
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
			locationEntity.setLongitude(longitude+"");
			locationEntity.setLatitude(latitude+"");
			Contant.regionManEntities = list;
		}
	}

}
