package com.stefan.city.module.entity;

import java.io.Serializable;

/**
 * SettingEntity
 * @author 日期：2013-3-12下午10:11:40
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class SettingEntity implements Serializable {
	
	/** 浏览历史记录设置 **/
	public final static String Setting_Store_History = "settHistory";
	
	/** 震动效果设置 **/
	public final static String Setting_Vibration = "settVibration";
	
	private int id;				// 唯一标识
	private String itemName;	// 设置项的名称
	private int type;			// 类型
	private int status;			// 状态，如果type为可以选择的类型，状态即为0或1
	
	public SettingEntity() {
	}
	
	public SettingEntity(int id, String itemName, int type, int status) {
		super();
		this.id = id;
		this.itemName = itemName;
		this.type = type;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
