package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * UserManageRunnable
 * 用于判断当前城市是否有管理员存在
 * @author 日期：2015-4-13上午10:48:16
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserHasAdminRunnable extends REBaseRunnable {
	
	private String city;
	private MemberService memberService;

	public UserHasAdminRunnable(Handler handler, String city) {
		super(handler);
		this.city = city;
		memberService = new MemberService();
	}

	@Override
	public void run() {
		int what = 1;
		if(city != null && !city.equals("") && REHttpUtility.isCheckConOK()) {
			// 如果没有管理员则返回1
			what = memberService.isHasAdmin(city);
		} else {
			what = -1;
		}
		sendMessage(what, null);
	}

}
