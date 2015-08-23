package com.stefan.city.module.thread;

import android.content.Context;
import android.os.Handler;

import com.stefan.city.module.entity.StoreEntity;
import com.stefan.city.module.service.HistoryService;

/**
 * HistoryManageRunnable
 * @author 日期：2015-3-2下午03:00:58
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class HistoryManageRunnable extends REBaseRunnable {

	private HistoryService historyService;
	private StoreEntity storeEntity;
	
	public HistoryManageRunnable(Handler handler, Context context, StoreEntity storeEntity) {
		super(handler);
		this.storeEntity = storeEntity;
		historyService = new HistoryService(context);
	}

	@Override
	public void run() {
//		int what = 0;
		if(storeEntity != null) {
			historyService.add(storeEntity);
		}
//		sendMessage(what, null);
	}
}
