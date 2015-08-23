package com.stefan.city.module.thread;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.service.ItemInfoService;
import com.stefan.city.module.service.UploadService;

/**
 * ItemSearchRunnable
 * @author 日期：2015-2-14上午11:44:53
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ItemSearchRunnable extends REBaseRunnable {

	private int pageNo;		// 当前页码
	private int pageSize;	// 每页获取的数据条数
	private String city;
	private String keyword;
	private ItemInfoService infoService;
	
	public ItemSearchRunnable(Handler handler, String city, String keyword, int pageNo, int pageSize) {
		super(handler);
		this.city = city;
		this.keyword = keyword;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		infoService = new ItemInfoService();
	}

	@Override
	public void run() {
		int what = 0;
		List<InfoItemEntity> list = null;
		if (REHttpUtility.isCheckConOK()) {
			list = infoService.getSearch(keyword, city, pageNo, pageSize);
			if(list != null && list.size() > 0) {
				what = 1;
			}
		} else {
			what = -1; // 网络不可用
		}
		sendMessage(what, list);
	}
}
