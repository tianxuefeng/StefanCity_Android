package com.stefan.city.module.thread;

import android.os.Handler;
import android.os.Message;
/**
 * @author 描述：REBaseRunnable.java继承于Runnable，实现线程控制<br>
 * 			通过传入的Handler与外界进行数据交互<br>
 * 			通过isRunning()获取线程的状态，通过isStop()来停止线程
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @author 日期：2011年10月30日
 * @author (C) Copyright Gmy Corporation 2011 - 2021
 *               All Rights Reserved.
 */
public abstract class REBaseRunnable implements Runnable {
	
	protected boolean isCache;
	
	protected boolean isStop;
	protected Handler handler;
	
	protected String url;
	
	public REBaseRunnable(Handler handler) {
		this.handler = handler;
	}

	protected void sendMessage(int what, Object obj) {
		if(isStop){
			return ;
		}
		if(obj != null){
			Message msg = handler.obtainMessage(what, obj);
			handler.sendMessage(msg);
		}else{
			handler.sendEmptyMessage(what);
		}
	}
	
	/**
	 * 获取线程的状态
	 * @return boolean
	 */
	public boolean isRunning(){
		synchronized(this){
			return isRunning();
		}
	}
	
	/**
	 * 停止线程
	 */
	public void isStop(){
		synchronized(this){
			isStop = true;
		}
	}

}
