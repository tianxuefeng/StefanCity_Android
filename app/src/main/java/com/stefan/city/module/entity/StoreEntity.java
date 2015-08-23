package com.stefan.city.module.entity;

/**
 * StoreEntity 浏览记录信息实体类
 * 
 * @author 日期：2012-12-25上午12:14:18
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021 All Rights Reserved.
 **/
public class StoreEntity implements java.io.Serializable {

	private String itemId; // 信息ID
	private String title; // 标题
	private String time;
	private int type; // 资源的类型 1 html； 2 图片信息； 3 视频信息； 4 其他

	public StoreEntity() {
	}

	public StoreEntity(String title, String itemId, String time) {
		super();
		this.title = title;
		this.itemId = itemId;
		this.time = time;
	}

	public StoreEntity(String title, String itemId, String time, int type) {
		super();
		this.title = title;
		this.itemId = itemId;
		this.time = time;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
