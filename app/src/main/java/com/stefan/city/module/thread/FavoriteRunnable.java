package com.stefan.city.module.thread;

import java.util.List;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.FavoriteEntity;
import com.stefan.city.module.service.FavoriteService;

import android.os.Handler;

/**
 * FavoriteRunnable
 * @author 日期：2014-7-14下午09:07:39
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class FavoriteRunnable extends REBaseRunnable {
	
	private FavoriteService favoriteService;
	
	private String userId;

	public FavoriteRunnable(Handler handler, String userId) {
		super(handler);
		favoriteService = new FavoriteService();
		this.userId = userId;
	}

	@Override
	public void run() {
		int what = 0;
		if (REHttpUtility.isCheckConOK()) {
			List<FavoriteEntity> list = favoriteService.getListByUser(userId);
			if(list != null && list.size() > 0) {
				what = 1;
				sendMessage(what, list);
			} else {
				what = 0;
			}
		} else {
			what = -1;	// 网络不通
		}
		sendMessage(what, null);
	}

}
