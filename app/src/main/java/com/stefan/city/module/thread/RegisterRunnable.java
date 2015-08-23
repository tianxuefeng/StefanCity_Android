package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * RegisterRunnable
 * @author 日期：2014-7-14上午12:09:14
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegisterRunnable extends REBaseRunnable {
	
	private MemberService memberService;
	private UserEntity entity;
	private boolean isLogin;	// 注册成功是否自动登陆

	public RegisterRunnable(Handler handler, UserEntity entity, boolean isLogin) {
		super(handler);
		memberService = new MemberService();
		this.entity = entity;
		this.isLogin = isLogin;
	}

	@Override
	public void run() {
		int what = 0;
		if (REHttpUtility.isCheckConOK()) {
			int rows = memberService.register(entity);
			if(rows > 0) {
				if(isLogin) {
					UserEntity userEntity = memberService.toLogin(entity.getEmail(), entity.getPawd());
					if(userEntity != null) {
						what = 1;
						Contant.curUser = userEntity;
					} else {
						what = -2;	// 注册成功但是自动登陆失败，需要手动登陆
					}
				}
			}
		} else {
			what = -1;	// 网络不通
		}
		sendMessage(what, null);
	}

}
