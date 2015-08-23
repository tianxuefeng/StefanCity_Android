package com.stefan.city.ui.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * REDialog<br>
 * 	描述：对话框、弹出框基类，用于规范所有的自定义对话框组件
 * @author 日期：2013-3-1下午07:11:26
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public abstract class REDialog {
	
	protected Dialog dialog;
	protected Context context;
	
	public REDialog(Context context) {
		this.context = context;
	}

	/** dialog初始化 **/
	protected abstract void initDialog();
	
	/** 弹出对话框 **/
	public void show() {
		if(dialog == null){
			initDialog();
		}else{
			dialog.show();
		}
	};
	/** 关闭对话框 **/
	public void dismiss() {
		if(dialog.isShowing()){
			dialog.dismiss();
		}
	};
	
	/** 释放所占用的内存 **/
	protected void onDestroy() {
	}
}
