package com.stefan.city.module.thread;

import java.util.List;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.entity.MessageEntity;
import com.stefan.city.module.service.ItemInfoService;
import com.stefan.city.module.service.MessageService;

import android.os.Handler;

/**
 * MessageRunnable
 * @author 日期：2014-7-19上午11:06:53
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class MessageRunnable extends REBaseRunnable {
	
	private ItemInfoService itemInfoService;
	
	private MessageService messageService;
	
	private String itemId;
	private InfoItemEntity itemEntity;

	public MessageRunnable(Handler handler, String itemId) {
		super(handler);
		this.itemId = itemId;
		init();
	}
	
	public MessageRunnable(Handler handler, InfoItemEntity itemEntity) {
		super(handler);
		this.itemEntity = itemEntity;
		init();
	}
	
	private void init() {
		itemInfoService = new ItemInfoService();
		messageService = new MessageService();
	}

	@Override
	public void run() {
		int what = 0;
		List<MessageEntity> list = null;
		if (REHttpUtility.isCheckConOK()) {
			if(itemEntity != null) {
				itemId = itemEntity.getId()+"";
			} else if(itemId != null && itemEntity == null) {
//				itemEntity = itemInfoService.
			}
			list = messageService.getListByItem(itemId);
			if(list != null && list.size() > 0) {
				what = 1;
			}
		} else {
			what = -1; // 网络不可用
		}
		sendMessage(what, new Object[] {itemEntity, list});
	}

}
