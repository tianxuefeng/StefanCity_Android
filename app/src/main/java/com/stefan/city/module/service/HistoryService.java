package com.stefan.city.module.service;

import java.util.List;

import android.content.Context;

import com.stefan.city.module.db.CityDataBase;
import com.stefan.city.module.db.StoreDataBase;
import com.stefan.city.module.entity.StoreEntity;

/**
 * HistoryService
 * 浏览记录管理
 * @author 日期：2015-3-9下午08:16:11
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class HistoryService {

	private CityDataBase storeDataBase;
	
	public HistoryService(Context context) {
		storeDataBase = new CityDataBase(context);
	}
	
	public List<StoreEntity> findAll() {
		return storeDataBase.findHistoryAll();
	}
	
	public StoreEntity findById(String id) {
		return storeDataBase.searchHistory(id);
	}
	
	public int add(StoreEntity entity) {
		if(findById(entity.getItemId()) != null && !entity.getItemId().equals("")) {
			return 0;
		} else {
			return (int)storeDataBase.insertHistory(entity);
		}
	}
	
	public int delete(StoreEntity entity) {
		return deleteById(entity.getItemId());
	}
	
	public int deleteById(String id) {
		return storeDataBase.deleteHistory(id);
	}
	
	/**
	 * 删除全部历史浏览记录
	 * @return
	 */
	public int deletes() {
		return storeDataBase.deleteHistory(null, null);
	}
}
