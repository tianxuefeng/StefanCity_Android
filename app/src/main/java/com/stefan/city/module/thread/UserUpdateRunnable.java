/**
 * 
 */
package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * UserUpdateRunnable
 * @author 日期：2015年7月5日下午3:23:05
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserUpdateRunnable extends REBaseRunnable {
	
	private MemberService memberService;
	
	private UserEntity entity;

	/**
	 * @param handler
	 */
	public UserUpdateRunnable(Handler handler, UserEntity entity) {
		super(handler);
		memberService = new MemberService();
		this.entity = entity;
	}

	@Override
	public void run() {
		int what = 0;
		if (REHttpUtility.isCheckConOK()) {
			what = memberService.update(entity, false);
		} else {
			what = -1;
		}
		sendMessage(what, null);
	}

}
