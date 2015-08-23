package com.stefan.city.module.service;

import java.util.List;

import android.content.Context;

import com.stefan.city.module.db.CityDataBase;
import com.stefan.city.module.entity.CityEntity;
import com.stefan.city.module.entity.ProvinceEntity;

/**
 * CityDataService
 * 城市信息管理
 * @author 日期：2014-7-12下午02:45:37
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CityDataService {
	
	private CityDataBase cityDataBase;
	
	public CityDataService(Context context) {
		cityDataBase = new CityDataBase(context);
	}

	public List<CityEntity> findList() {
		return cityDataBase.findCityAll();
	}
	
	public List<CityEntity> findById(String id) {
		return cityDataBase.searchCity(CityDataBase.TABLE_CITY_ID, id);
	}
	
	public List<CityEntity> findByParent(String parentId) {
		return cityDataBase.searchCity(CityDataBase.TABLE_CITY_PARENT_ID, parentId);
	}
	
	public List<CityEntity> findByName(String name) {
		return cityDataBase.searchCity(CityDataBase.TABLE_CITY_NAME, name);
	}
	
	public long adds(List<CityEntity> list) {
		long rows = 0;
		for (CityEntity entity : list) {
			rows += add(entity);
		}
		return rows;
	}
	
	public long add(CityEntity entity) {
		return cityDataBase.insertCity(entity);
	}
	
	public int delete(String id) {
		return cityDataBase.deleteCity(id);
	}
	
	public List<ProvinceEntity> findProList() {
		return cityDataBase.findProAll();
	}
	
	public List<ProvinceEntity> findProById(String id) {
		return cityDataBase.searchPro(CityDataBase.TABLE_PROVINCE_ID, id);
	}
	
	public List<ProvinceEntity> findProByName(String name) {
		return cityDataBase.searchPro(CityDataBase.TABLE_PROVINCE_NAME, name);
	}
	
	public long addPros(List<ProvinceEntity> list) {
		long row = 0;
		for (ProvinceEntity entity : list) {
			row += cityDataBase.insertPro(entity);
		}
		return row;
	}
	
	public long addPro(ProvinceEntity entity) {
		return cityDataBase.insertPro(entity);
	}
	
	public int deletePro(String id) {
		return cityDataBase.deletePro(id);
	}
	
	public void close() {
		cityDataBase.close();
	}
}
