package com.stefan.city.module.thread;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.service.CategoryService;

/**
 * CategoryDeleteRunnable
 * @author 日期：2015-2-15下午02:23:40
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CategoryDeleteRunnable extends REBaseRunnable {
	
	private String categoryId;
	private CategoryService categoryService;

	public CategoryDeleteRunnable(Handler handler, String categoryId) {
		super(handler);
		this.categoryId = categoryId;
		categoryService = new CategoryService();
	}

	@Override
	public void run() {
		int what = 0;
		if(REHttpUtility.isCheckConOK()) {
			what = categoryService.delete(categoryId);
			what = (what == 1) ? 5 : what;
		} else {
			what = -1;	// 网络不可用
		}
		sendMessage(what, null);
	}

}
