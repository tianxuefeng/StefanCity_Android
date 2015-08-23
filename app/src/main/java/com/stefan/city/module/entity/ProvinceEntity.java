package com.stefan.city.module.entity;

import java.io.Serializable;

/**
 * ProvinceEntity
 * 省份信息
 * @author 日期：2014-7-11下午04:12:44
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ProvinceEntity implements Serializable {
	
	private String provinceId;
	private String name;
	
	private String localName;
	
	private int localStatus;
	
	public ProvinceEntity() {
	}

	public ProvinceEntity(String provinceId, String name) {
		super();
		this.provinceId = provinceId;
		this.name = name;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public int getLocalStatus() {
		return localStatus;
	}

	public void setLocalStatus(int localStatus) {
		this.localStatus = localStatus;
	}
	
}
