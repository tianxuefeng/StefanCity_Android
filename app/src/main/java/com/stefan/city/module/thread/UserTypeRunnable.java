package com.stefan.city.module.thread;

import android.os.Handler;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.service.MemberService;

/**
 * UserTypeRunnable
 * @author 日期：2015-4-17下午04:05:31
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserTypeRunnable extends REBaseRunnable {
	
	private MemberService memberService;
	private String userId;
	private int memType;

	public UserTypeRunnable(Handler handler, String userId, int memType) {
		super(handler);
		this.userId = userId;
		this.memType = memType;
		memberService = new MemberService();
	}

	@Override
	public void run() {
		int what = 0;
		if(REHttpUtility.isCheckConOK()) {
			what = memberService.updateMemberType(userId, memType+"");
		} else {
			what = -1;
		}
		sendMessage(what, null);
	}
}
