package com.stefan.city.module.thread;

import java.util.Date;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.service.CategoryService;
import com.stefan.city.module.service.UploadService;

import android.os.Handler;

/**
 * CategoryAddRunnable
 * @author 日期：2014-9-12下午02:05:34
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CategoryAddRunnable extends REBaseRunnable {
	
	private CategoryEntity entity;
	
	private UploadService uploadService;
	private CategoryService categoryService;

	public CategoryAddRunnable(Handler handler, CategoryEntity entity) {
		super(handler);
		this.entity = entity;
		uploadService = new UploadService();
		categoryService = new CategoryService();
	}

	@Override
	public void run() {
		int what = 0;
		String images = null;//test_1410785999128.jpg
		boolean bool = false;
		if(REHttpUtility.isCheckConOK()) {
			String filePath = entity.getImages();
			int row = 1;
			// 允许图标为空
			if(filePath != null) {
				String fileName = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				fileName = "test_" + new Date().getTime() + fileName;
				images = fileName;
				row = uploadService.upload(filePath, fileName);
			}
			bool = (row == 1);
			if(bool) {
				entity.setImages(images);
				what = categoryService.insert(entity);
			} else {
				what = -2; // 图片上传失败
			}
		} else {
			what = -1;	// 网络不可用
		}
		sendMessage(what, null);
	}

}
