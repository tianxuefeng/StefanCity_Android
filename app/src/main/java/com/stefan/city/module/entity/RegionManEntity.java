package com.stefan.city.module.entity;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

/**
 * RegionManEntity
 * 用于管理的地区实体
 * @author 日期：2014-8-30上午10:59:59
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegionManEntity implements Serializable {
	
	public static final String FIELD_ID = "ID";
	public static final String FIELD_NAME = "RegionName";
	public static final String FIELD_PARENT_NAME = "ParentRegionName";
	public static final String FIELD_LANGUAGE = "LanguageCode";
	
	public static final String FIELD_CITY = "City";
	public static final String FIELD_LANGUAGE_C = "Language";
	
	private String id;
	private String name;
	private String parentName;
	private String languageCode;
	
	public RegionManEntity() {
	}
	
	public RegionManEntity(JSONObject jsonObject) {
		try {
			id = jsonObject.getString(FIELD_ID);
			name = jsonObject.getString(FIELD_NAME);
			parentName = jsonObject.getString(FIELD_PARENT_NAME);
			languageCode = jsonObject.getString(FIELD_LANGUAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
