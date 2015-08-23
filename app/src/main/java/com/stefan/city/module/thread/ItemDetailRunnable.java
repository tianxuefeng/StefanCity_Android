package com.stefan.city.module.thread;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.service.ItemInfoService;


/**
 * ItemDetailRunnable
 * @author 日期：2015-3-5下午04:48:10
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ItemDetailRunnable extends REBaseRunnable {

	private ItemInfoService infoService;
	
	private String id;
	
	public ItemDetailRunnable(Handler handler, String id) {
		super(handler);
		this.id = id;
		infoService = new ItemInfoService();
	}
	
	@Override
	public void run() {
		int what = 0;
		InfoItemEntity entity = null;
		if(id != null && !id.equals("") && REHttpUtility.isCheckConOK()) {
			entity = infoService.findById(id);
		} else {
			what = -1;
		}
		sendMessage(what, entity);
	}

}
