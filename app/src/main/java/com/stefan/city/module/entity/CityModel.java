package com.stefan.city.module.entity;

/**
 * 
 * CityModel 城市属性实体类
 * 
 * @author 日期：2014-7-21下午10:51:16
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 * 
 */
public class CityModel {
	
	private String cityName; // 城市名字
	private String nameSort; // 城市首字母

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getNameSort() {
		return nameSort;
	}

	public void setNameSort(String nameSort) {
		this.nameSort = nameSort;
	}

}
