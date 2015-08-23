package com.stefan.city.module.thread;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.service.CategoryService;
import com.stefan.city.module.service.ItemInfoService;

/**
 * ItemsDeleteRunnable
 * @author 日期：2015-3-3上午09:55:54
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ItemsDeleteRunnable extends REBaseRunnable {
	
	private String infoId;
	private ItemInfoService itemInfoService;

	public ItemsDeleteRunnable(Handler handler, String infoId) {
		super(handler);
		this.infoId = infoId;
		itemInfoService = new ItemInfoService();
	}

	@Override
	public void run() {
		int what = 0;
		if(REHttpUtility.isCheckConOK()) {
			what = itemInfoService.deleteById(infoId);
		} else {
			what = -1;	// 网络不可用
		}
		sendMessage(what, null);
	}
}
