package com.stefan.city.module.entity;

import java.io.Serializable;

/**
 * RegionEntity
 * @author 日期：2014-7-22上午11:49:26
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegionEntity implements Serializable {

	public String name;
	public String cityName;
	public String id;		// 区域编号
	public String parentId;	// 所属城市编号
	
	public RegionEntity() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
