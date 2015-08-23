package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * UserLoginRunnable
 * @author 日期：2014-7-7下午11:22:31
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserLoginRunnable extends REBaseRunnable {

	private MemberService memberService;
	
	private String email, pawd;
	
	public UserLoginRunnable(Handler handler, String email, String pawd) {
		super(handler);
		this.email = email;
		this.pawd = pawd;
		memberService = new MemberService();
	}

	@Override
	public void run() {
		int what = 1;
		if (REHttpUtility.isCheckConOK()) {
			UserEntity entity = memberService.toLogin(email, pawd);
			if(entity == null) {
				what = 0;
			}
			sendMessage(what, entity);
		} else {
			what = -1;
		}
		sendMessage(what, null);
	}

}
