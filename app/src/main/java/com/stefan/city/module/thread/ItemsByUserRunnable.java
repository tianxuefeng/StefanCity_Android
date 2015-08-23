package com.stefan.city.module.thread;

import java.util.List;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.service.ItemInfoService;

import android.os.Handler;

/**
 * ItemsByUserRunnable
 * 根据用户ID获得用户发布的信息
 * @author 日期：2015-3-9下午06:22:44
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ItemsByUserRunnable extends REBaseRunnable {
	
	private String userId;
	private ItemInfoService infoService;

	public ItemsByUserRunnable(Handler handler, String userId) {
		super(handler);
		this.userId = userId;
		infoService = new ItemInfoService();
	}

	@Override
	public void run() {
		int what = 0;
		if (REHttpUtility.isCheckConOK()) {
			List<InfoItemEntity> list = infoService.getByUserId(userId);
			if(list != null && list.size() > 0) {
				sendMessage(1, list);
			}
		} else {
			what = -1;
		}
		sendMessage(what, null);
	}

}
