package com.stefan.city.module.thread;

import java.util.List;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * UserListRunnable
 * @author 日期：2015-4-13上午11:31:39
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserListRunnable extends REBaseRunnable {
	
	private String city;
	
	private MemberService memberService;

	public UserListRunnable(Handler handler, String city) {
		super(handler);
		this.city = city;
		memberService = new MemberService();
	}

	@Override
	public void run() {
		int what = 0;
		List<UserEntity> list = null;
		if(REHttpUtility.isCheckConOK()) {
			list = memberService.findListByCity(city);
			if(list != null && list.size() > 0) {
				what = 1;
			}
		} else {
			what = -1;
		}
		sendMessage(what, list);
	}

}
