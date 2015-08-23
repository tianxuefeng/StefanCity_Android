package com.stefan.city.base;

import com.rockeagle.framework.core.REActivity;
import com.rockeagle.framework.core.REActivityManager;
import com.rockeagle.framework.core.RockEagleApp;
import com.rockeagle.framework.core.app.REApplication;
import com.rockeagle.framework.core.contant.REContant;
import com.rockeagle.framework.core.init.REAppInit;
import com.rockeagle.framework.module.xml.dao.RELoadXmlDAO;
import com.rockeagle.framework.util.REMappingUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.Toast;
/**
 * @author 描述：REActivity，继承于Activity。顶层Activity基类<br>
 * 			通过
 * @author 作者：辰梦
 * @author 邮箱：chenmeng_gmy@163.com
 * @author 日期：2015年3月17日
 * @author (C) Copyright 辰梦 Corporation 2011 - 2021
 *               All Rights Reserved.
 *
 */
public class BaseActivity extends REActivity {
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
