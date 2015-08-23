package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.MessageEntity;
import com.stefan.city.module.service.MessageService;

import android.os.Handler;

/**
 * MessageSendRunnable
 * @author 日期：2014-7-21上午11:13:56
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class MessageSendRunnable extends REBaseRunnable {
	
	private MessageService messageService;
	private MessageEntity entity;

	public MessageSendRunnable(Handler handler, String itemId, String msg, String msgUserId) {
		super(handler);
		messageService = new MessageService();
		entity = new MessageEntity();
		entity.setItemId(itemId);
		entity.setMsg(msg);
		entity.setOwnerId(msgUserId);
	}

	@Override
	public void run() {
		int what = 0;
		if (REHttpUtility.isCheckConOK()) {
			what = messageService.insert(entity);
		} else {
			what = -1; // 网络不可用
		}
		sendMessage(what, entity);
	}

}
