/**
 * 
 */
package com.stefan.city.module.thread;

import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * UserChangePawdRunnable
 * 用户修改密码
 * @author 日期：2015年7月13日下午4:52:05
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserChangePawdRunnable extends REBaseRunnable {

	private MemberService memberService;
	
	private String[] strings;
	
	private String userId;
	
	/**
	 * @param handler
	 */
	public UserChangePawdRunnable(Handler handler, String userId, String[] strings) {
		super(handler);
		this.userId = userId;
		this.strings = strings;
		memberService = new MemberService();
	}

	@Override
	public void run() {
		int what = 0;
		if (REHttpUtility.isCheckConOK()) {
			what = memberService.updatePassword(userId, strings[0], strings[1]);
		} else {
			what = -1;
		}
		sendMessage(what, null);
	}

}
