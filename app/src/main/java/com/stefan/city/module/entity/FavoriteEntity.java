package com.stefan.city.module.entity;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * FavoriteEntity
 * @author 日期：2014-6-13下午09:11:26
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class FavoriteEntity extends InfoItemEntity {

	/** 收藏信息ID **/
	public static final String FIELD_ID = "FavoriteID";
	/** 收藏信息的内容信息ID **/
	public static final String FIELD_ITEM_ID = "ItemID";
	/** 收藏信息的用户ID **/
	public static final String FIELD_USER_ID = "UserID";
	
	private String favoriteId;
	
	public FavoriteEntity() {
		super();
	}
	
	public FavoriteEntity(JSONObject jsonObject) {
		super(jsonObject);
		try {
			favoriteId = jsonObject.getString(FIELD_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public FavoriteEntity(String title, String description, double price,
			String images, String phone, String qqSkype, String countryCode,
			String city, String region, String subRegion, String categoryID, int rank,
			int sequence, String userId, Date createDate, Date updateDate) {
		super(title, description, price, images, phone, qqSkype, countryCode, city,
				region, subRegion, categoryID, rank, sequence, userId, createDate, updateDate);
	}

	public String getFavoriteId() {
		return favoriteId;
	}

	public void setFavoriteId(String favoriteId) {
		this.favoriteId = favoriteId;
	}
	
}
