package com.stefan.city.module.thread;

import java.util.List;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.service.ItemInfoService;

/**
 * ItemsRunnable
 * 
 * @author 日期：2014-4-12上午11:49:34
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021 All Rights Reserved.
 **/
public class ItemsGetRunnable extends REBaseRunnable {

	private int pageNo;		// 当前页码
	private int pageSize;	// 每页获取的数据条数
	private String city;
	private String region;
	private String regionStreet;
	private String parentId;
	private String categoryId;
	private String type;
	private ItemInfoService infoService;
	
	public ItemsGetRunnable(Handler handler, String city, String region, 
			String regionStreet, String parentId, String categoryId, String type, int pageNo, int pageSize) {
		super(handler);
		this.city = city;
		this.region = region;
		this.regionStreet = regionStreet;
		this.parentId = parentId;
		this.categoryId = categoryId;
		this.type = type;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		infoService = new ItemInfoService();
	}

	@Override
	public void run() {
		int what = 0;
		List<InfoItemEntity> list = null;
		if (REHttpUtility.isCheckConOK()) {
			list = infoService.getPage(city, region, regionStreet, parentId, categoryId, type, pageNo, pageSize);
			if(list != null && list.size() > 0) {
				what = 1;
			}
		} else {
			what = -1; // 网络不可用
		}
		sendMessage(what, list);
	}
}
