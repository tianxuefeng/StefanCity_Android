package com.stefan.city.module.thread;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.service.FavoriteService;

/**
 * FavoriteManageRunnable
 * @author 日期：2015-3-2上午11:24:25
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class FavoriteManageRunnable extends REBaseRunnable {
	
	public static final int STATUS_DEL = 1;
	
	public static final int STATUS_UPDATE = 2;
	
	public static final int STATUS_ADD = 3;
	
	private int operateStatus;
	
	private FavoriteService favoriteService;
	
	private String id;
	
	private String userId;
	
	private String itemId;

	public FavoriteManageRunnable(Handler handler, String userId, String itemId,
			String id, int operateStatus) {
		super(handler);
		this.id = id;
		favoriteService = new FavoriteService();
		this.userId = userId;
		this.operateStatus = operateStatus;
		this.itemId = itemId;
	}

	@Override
	public void run() {
		int what = -1;
		if (userId != null && REHttpUtility.isCheckConOK()) {
			switch(operateStatus) {
			case STATUS_ADD:
				if(itemId != null) {
					what = favoriteService.insert(userId, itemId);
				} else {
					what = 0;
				}
				break;
				
			case STATUS_DEL:
				if(id != null) {
					what = favoriteService.deleteById(id);
				} else {
					what = 0;
				}
				break;
			}
		}
		sendMessage(what, null);
	}
}
