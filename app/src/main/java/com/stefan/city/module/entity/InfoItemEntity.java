package com.stefan.city.module.entity;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

/**
 * InfoItemEntity
 * 信息列表实体类
 * @author 日期：2014-5-13下午08:59:34
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class InfoItemEntity implements Serializable {
	
	/** ID标识【int】 **/
	public static final String FIELD_ID = "ID"; 
	/** 标题【String】 **/
	public static final String FIELD_Title = "Title";
	/** 说明 【String】**/
	public static final String FIELD_Desc = "Description";
	/** 价格【double】 **/
	public static final String FIELD_Price = "Price";
	/** 图片集【String】 **/
	public static final String FIELD_Images = "Images";
	/** 联系方式【String】 **/
	public static final String FIELD_Phone = "Phone";
	/** QQ联系方式【String】 **/
	public static final String FIELD_QQSkype = "QQSkype";
	/** 国家标识【int】 **/
	public static final String FIELD_CountryCode = "CountryCode";
	
	/** 关键字标识【搜索用】 **/
	public static final String FIELD_Search = "keyword";
	
	/** 城市【String】 **/
	public static final String FIELD_City = "City";
	/** 区域【String】 **/
	public static final String FIELD_Region = "Region";
	/** 街道【String】 **/
	public static final String FIELD_Sub_Region = "SubRegion";
	/** 邮编【String】 **/
	public static final String FIELD_PostalCode = "PostalCode";
	/** 地址【int】 **/
	public static final String FIELD_Address = "Address";
	/** 父级分类【int】 **/
	public static final String FIELD_ParentID = "ParentID";
	/** 分类【int】 **/
	public static final String FIELD_CategoryID = "CategoryID";
	
	/** 类型【int】 **/
	public static final String FIELD_Type = "Type";
	/** 置顶级别【int】 **/
	public static final String FIELD_Rank = "Rank";
	/** 顺序【int】 **/
	public static final String FIELD_Sequence = "Sequence";
	/** 发布用户ID【int】 **/
	public static final String FIELD_UserID = "UserID";
	/** 状态【int】 **/
	public static final String FIELD_Status = "Status";
	
	/** 创建日期【String】 **/
	public static final String FIELD_CreateDate = "CreateDate";
	/** 修改日期【String】 **/
	public static final String FIELD_UpdateDate = "UpdateDate";
	
	private int id;					// 信息ID
	private String title;			// 标题
	private String description;		// 描述
	private double price;			// 价格
	private String images;			// 图片
	private String phone;			// 联系方式
	private String qqSkype;			// QQ等其他网络联系方式
	
	private String postalCode;		// 邮政编码
	
	private String address;
	
	private String countryCode;		// 国家标识
	private String city;			// 城市
	private String region;			// 区域
	private String subRegion;
	private String categoryID;			// 分类
	private int type;				// 类型，买或者卖
	private int	rank;				// 置顶
	private int sequence;			// 信息的顺序
	private String userId;			// 用户标识
	private int status;
	private Date createDate;		// 创建时间
	private Date updateDate;		// 修改时间
	
	public InfoItemEntity(String title, String description, double price,
			String images, String phone, String qqSkype, String countryCode,
			String city, String region, String subRegion, String categoryID, int rank, int sequence,
			String userId, Date createDate, Date updateDate) {
		super();
		this.title = title;
		this.description = description;
		this.price = price;
		this.images = images;
		this.phone = phone;
		this.qqSkype = qqSkype;
		this.countryCode = countryCode;
		this.city = city;
		this.region = region;
		this.subRegion = subRegion;
		this.categoryID = categoryID;
		this.rank = rank;
		this.sequence = sequence;
		this.userId = userId;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
	
	public InfoItemEntity(JSONObject jsonObject) {
		try {
			id = jsonObject.getInt(FIELD_ID);
			title = jsonObject.getString(FIELD_Title);
			description = jsonObject.getString(FIELD_Desc);
			price = jsonObject.getDouble(FIELD_Price);
			images = jsonObject.getString(FIELD_Images);
			phone = jsonObject.getString(FIELD_Phone);
			qqSkype = jsonObject.getString(FIELD_QQSkype);
			countryCode = jsonObject.getString(FIELD_CountryCode);
			city = jsonObject.getString(FIELD_City);
			region = jsonObject.getString(FIELD_Region);
//			subRegion = jsonObject.getString(FIELD_Sub_Region);
			postalCode = jsonObject.getString(FIELD_PostalCode);
			address = jsonObject.getString(FIELD_Address);
			categoryID = jsonObject.getString(FIELD_CategoryID);
			type = jsonObject.getInt(FIELD_Type);
			rank = jsonObject.getInt(FIELD_Rank);
			sequence = jsonObject.getInt(FIELD_Sequence);
			userId = jsonObject.getString(FIELD_UserID);
			String createStr = jsonObject.getString(FIELD_CreateDate);
			if(createStr != null && !createStr.equals("")) {
				createStr = createStr.replaceAll("/", "").replaceAll("[Date()]", "");
				Long createLong = Long.valueOf(createStr);
				createDate = new Date(createLong);
			}
			
			String updateStr = jsonObject.getString(FIELD_UpdateDate);
			if(updateStr != null && !updateStr.equals("")) {
				updateStr = updateStr.replaceAll("/", "").replaceAll("[Date()]", "");
				Long updateLong = Long.valueOf(updateStr);
				updateDate = new Date(updateLong);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public InfoItemEntity() {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQqSkype() {
		return qqSkype;
	}

	public void setQqSkype(String qqSkype) {
		this.qqSkype = qqSkype;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubRegion() {
		return subRegion;
	}

	public void setSubRegion(String subRegion) {
		this.subRegion = subRegion;
	}
	
}
