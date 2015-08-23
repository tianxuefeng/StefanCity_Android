/**
 * 
 */
package com.stefan.city.module.thread;

//import com.google.android.gms.internal.en;
import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.service.MemberService;

import android.os.Handler;

/**
 * UserDetailRunnable
 * @author 日期：2015年7月5日下午2:39:40
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserDetailRunnable extends REBaseRunnable {
	
	private String userId;
	private MemberService memberService;

	/**
	 * @param handler
	 */
	public UserDetailRunnable(Handler handler, String userId) {
		super(handler);
		memberService = new MemberService();
		this.userId = userId;
	}

	@Override
	public void run() {
		int what = 0;
		UserEntity entity = null;
		if (REHttpUtility.isCheckConOK()) {
			entity = memberService.findById(userId);
			if(entity != null) {
				what = 1;
			}
		} else {
			what = -1;
		}
		sendMessage(what, entity);
	}

}
