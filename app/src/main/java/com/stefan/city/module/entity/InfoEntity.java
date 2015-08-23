package com.stefan.city.module.entity;

import java.io.Serializable;

/**
 * InfoEntity
 * 	信息实体类
 * @author 日期：2014-4-8下午10:51:46
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class InfoEntity implements Serializable {
	
	private int id;					// 信息ID
	private String title;			// 标题
	private String description;		// 描述
	private double price;			// 价格
	private String images;			// 图片
	private String phone;			// 联系方式
	private String qqSkype;			// QQ等其他网络联系方式
	
	private String countryCode;		// 国家标识
	private String city;			// 城市
	private String region;			// 区域
	private int categoryID;			// 类别
	private int	rank;				// 置顶
	private int sequence;			// 信息的顺序
	private int userId;				// 用户标识
	private String createDate;		// 创建时间
	private String updateDate;		// 修改时间
	
	public InfoEntity() {
	}
	
	public InfoEntity(String title, String description, double price,
			String images, String phone, String qqSkype, String countryCode,
			String city, String region, int categoryID, int rank, int sequence,
			int userId, String createDate, String updateDate) {
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
		this.categoryID = categoryID;
		this.rank = rank;
		this.sequence = sequence;
		this.userId = userId;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public InfoEntity(int id, String title, String description, double price,
			String images, String phone, String qqSkype, String countryCode,
			String city, String region, int categoryID, int rank, int sequence,
			int userId, String createDate, String updateDate) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.images = images;
		this.phone = phone;
		this.qqSkype = qqSkype;
		this.countryCode = countryCode;
		this.city = city;
		this.region = region;
		this.categoryID = categoryID;
		this.rank = rank;
		this.sequence = sequence;
		this.userId = userId;
		this.createDate = createDate;
		this.updateDate = updateDate;
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

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
}
