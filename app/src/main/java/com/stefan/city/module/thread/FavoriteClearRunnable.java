package com.stefan.city.module.thread;

import java.util.List;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.service.FavoriteService;

/**
 * FavoriteClearRunnable
 * @author 日期：2015-2-9下午09:59:04
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class FavoriteClearRunnable extends REBaseRunnable {
	
	private FavoriteService favoriteService;
	
	private String userId;

	public FavoriteClearRunnable(Handler handler, String userId) {
		super(handler);
		favoriteService = new FavoriteService();
		this.userId = userId;
	}

	@Override
	public void run() {
		int what = 0;
		if (REHttpUtility.isCheckConOK()) {
			what = favoriteService.deleteByUserId(userId);
		} else {
			what = -1;	// 网络不通
		}
		sendMessage(what, null);
	}
}
