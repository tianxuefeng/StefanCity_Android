package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * UserBockRunnable
 * @author 日期：2015-4-13上午11:56:01
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserBockRunnable extends REBaseRunnable {
	
	private MemberService memberService;
	private String userId;

	public UserBockRunnable(Handler handler, String userId) {
		super(handler);
		this.userId = userId;
		memberService = new MemberService();
	}

	@Override
	public void run() {
		int what = 0;
		if(REHttpUtility.isCheckConOK()) {
			what = memberService.bockAnMember(userId);
		} else {
			what = -1;
		}
		sendMessage(what, null);
	}

}
