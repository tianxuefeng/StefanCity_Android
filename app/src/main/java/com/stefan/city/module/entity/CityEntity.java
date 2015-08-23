package com.stefan.city.module.entity;

import java.io.Serializable;

/**
 * CityEntity
 * 城市信息
 * @author 日期：2014-7-11下午04:10:40
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CityEntity implements Serializable {

	private String cityId;
	private String cityName;
	private String parentId;
	
	public CityEntity() {
	}
	
	public CityEntity(String cityId, String cityName, String parentId) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.parentId = parentId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
}
