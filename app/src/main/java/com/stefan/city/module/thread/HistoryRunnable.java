package com.stefan.city.module.thread;

import java.util.List;

import com.stefan.city.module.entity.StoreEntity;
import com.stefan.city.module.service.HistoryService;

import android.content.Context;
import android.os.Handler;


/**
 * HistoryRunnable
 * @author 日期：2015-3-9下午08:23:00
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class HistoryRunnable extends REBaseRunnable {

	private HistoryService historyService;
	
	public HistoryRunnable(Handler handler, Context context) {
		super(handler);
		historyService = new HistoryService(context);
	}

	@Override
	public void run() {
		List<StoreEntity> list = historyService.findAll();
		if(list != null && list.size() > 0) {
			sendMessage(1, list);
		} else {
			sendMessage(0, null);
		}
	}
}
