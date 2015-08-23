package com.stefan.city.module.thread;

import java.util.Date;
import java.util.Map;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.service.ItemInfoService;
import com.stefan.city.module.service.UploadService;

/**
 * ItemsEditRunnable
 * @author 日期：2014-5-21下午09:39:46
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ItemsEditRunnable extends REBaseRunnable {
	
	private Map<Long, String> imgPaths;
	private InfoItemEntity entity;
	
	private ItemInfoService infoService;
	private UploadService uploadService;
	
	public ItemsEditRunnable(Handler handler, Map<Long, String> imgPaths, InfoItemEntity entity) {
		super(handler);
		this.imgPaths = imgPaths;
		this.entity = entity;
		infoService = new ItemInfoService();
		uploadService = new UploadService();
	}
	
	/* 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int what = 1;
		if (REHttpUtility.isCheckConOK()) {
			boolean bool = true;
			String images = "";
			if(bool) {
				for (Long key : imgPaths.keySet()) {
					String filePath = imgPaths.get(key);
					String fileName = filePath.substring(filePath.lastIndexOf("."), filePath.length());
					fileName = "test_" + new Date().getTime() + fileName;
					images += fileName + "|";
					int row = uploadService.upload(filePath, fileName);
					bool = bool & row == 1;
				}
			}
			// post上传新增的信息
			entity.setImages(images);	// 设置图片信息
			what = infoService.insert(entity);
		}
		sendMessage(what, null);
	}
	
}
