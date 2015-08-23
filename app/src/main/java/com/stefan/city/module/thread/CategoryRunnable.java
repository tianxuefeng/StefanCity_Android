package com.stefan.city.module.thread;

import java.util.List;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.service.CategoryService;


import android.os.Handler;

/**
 * CategoryRunnable
 * 获取分类信息
 * @author 日期：2014-4-12上午10:54:39
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class CategoryRunnable extends REBaseRunnable {
	
	private CategoryService categoryService;
	private String parentId;
	private String language;

	public CategoryRunnable(Handler handler, String parentId, String language) {
		super(handler);
		categoryService = new CategoryService();
		this.parentId = parentId;
		this.language = language;
	}

	@Override
	public void run() {
		int what = 0;
		List<CategoryEntity> list = null;
		if(REHttpUtility.isCheckConOK()) {
			list = categoryService.getList(parentId, language);
			if(list != null && list.size() > 0) {
				what = 1;
			}
		} else {
			what = -1;	// 网络不可用
		}
		sendMessage(what, list);
	}

}
