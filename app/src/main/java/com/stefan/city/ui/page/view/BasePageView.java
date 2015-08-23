package com.stefan.city.ui.page.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.AbsListView.OnScrollListener;

/**
 * BasePageView
 * @author 日期：2012-12-11下午10:50:19
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public abstract class BasePageView extends LinearLayout {

	/** 是否启用滑动数据加载 **/
	protected boolean isTouch = true;
	/** 数据是否正在加载中 **/
	protected boolean isLoad = false;
	/** 数据是否加载完毕 **/
	protected boolean isLast = false;
	
	protected boolean isRefreshFoot = false;
	
	public BasePageView(Context context) {
		super(context);
	}
	
	public BasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void addView(View view) {
		LayoutParams relativeParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		super.addView(view, relativeParams);
	}

	/**
	 * 滑动数据加载
	 */
	protected OnScrollListener scrollLoadListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(isTouch && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
				if (view.getLastVisiblePosition() == view.getCount() - 1 && !isLoad && !isLast) {    
					isLoad = true; // 用布尔作为开关，防止在加载数据时，出现多次启动线程加载数据
					pageLoadData();
				}    
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if(firstVisibleItem + visibleItemCount == totalItemCount){
				isRefreshFoot = true;
			}else{
				isRefreshFoot = false;
			}
		}
	};
	
	/**
	 * 初始化数据加载
	 */
	protected abstract void loadData();
	/**
	 * 初始化属性
	 */
	protected abstract void initVal();
	
	/**
	 * 通过URL加载翻页数据网络XML数据
	 * @param infoUrl
	 */
	protected abstract void pageLoadData();
	
	/**
	 * 销毁
	 */
	public abstract void onDestroy();
	
	/**
	 * 重新加载
	 */
	public abstract void onResume();
	/**
	 * 重新加载，指定是否重新加载数据
	 */
	public abstract void onResume(boolean isLoad);
}
