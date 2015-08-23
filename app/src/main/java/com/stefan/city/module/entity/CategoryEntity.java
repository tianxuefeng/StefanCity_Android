package com.stefan.city.module.entity;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

/**
 * CategoryEntity
 * 分类，例如：求职、房屋出租等
 * @author 日期：2014-6-12下午07:52:08
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CategoryEntity implements Serializable {
	
	/** 参数LanguageCode **/
	public static final String LanguageCode = "LanguageCode";
	
	/** ID标识 **/
	public static final String FIELD_ID = "ID";
	/** title标识 **/
	public static final String FIELD_TITLE = "Title";
	/** Description标识 **/
	public static final String FIELD_DESC = "Description";
	/** ParentID标识 **/
	public static final String FIELD_PARENT_ID = "ParentID";
	/** Images标识 **/
	public static final String FIELD_IMAGES = "Images";
	/** type标识 **/
	public static final String FIELD_TYPE = "type";
	/** Sequence标识 **/
	public static final String FIELD_SEQUENCE = "Sequence";
	/** CountryCode标识 **/
	public static final String FIELD_COUNTRY_CODE = "CountryCode";
	/** CreateDate标识 **/
	public static final String FIELD_CREATEDATE = "CreateDate";
	/** UpdateDate标识 **/
	public static final String FIELD_UPDATEDATE = "UpdateDate";
	/** CreateUser标识 **/
	public static final String FIELD_CREATE_USER = "CreateUser";
	/** UpdateUser标识 **/
	public static final String FIELD_UPDATE_USER = "UpdateUser";
	
	private String id;				// ID
	private String title;		// 分类名称
	private String desc;		// 描述
	private String parentId;	// 上级分类
	private String images;		// 图片
	private String sequence;		// 排序
	private String countryCode;	// 国家标识
	private String createUser;	// 创建人
	private String updateUser;	// 最后编辑人
	
	private String type;			// 分类的类型，是否需要显示相关信息，例如价格等
	
	private Date createDate;
	private Date updateDate;
	
	private String imgPath;
	
	public CategoryEntity() {
	}

	public CategoryEntity(String title) {
		super();
		this.title = title;
	}

	public CategoryEntity(String id, String title, String desc, String parentId,
			String images, String type, String sequence, String countryCode, String createUser,
			String updateUser, Date createDate, Date updateDate) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.parentId = parentId;
		this.images = images;
		this.sequence = sequence;
		this.countryCode = countryCode;
		this.createUser = createUser;
		this.updateUser = updateUser;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.type = type;
	}
	
	public CategoryEntity(CategoryEntity entity) {
		this.id = entity.id;
		this.title = entity.title;
		this.desc = entity.desc;
		this.parentId = entity.parentId;
		this.images = entity.images;
		this.sequence = entity.sequence;
		this.countryCode = entity.countryCode;
		this.type = entity.type;
		this.createUser = entity.createUser;
		this.updateUser = entity.updateUser;
		this.createDate = entity.createDate;
		this.updateDate = entity.updateDate;
	}
	
	public CategoryEntity(JSONObject jsonObject) {
		try {
			id = jsonObject.getString(FIELD_ID);
			parentId = jsonObject.getString(FIELD_PARENT_ID);
			title = jsonObject.getString(FIELD_TITLE);
			desc = jsonObject.getString(FIELD_DESC);
			if(desc != null && desc.equalsIgnoreCase("null")) {
				desc = null;
			}
			images = jsonObject.getString(FIELD_IMAGES);
			countryCode = jsonObject.getString(FIELD_COUNTRY_CODE);
			sequence = jsonObject.getString(FIELD_SEQUENCE);
			createUser = jsonObject.getString(FIELD_CREATE_USER);
			updateUser = jsonObject.getString(FIELD_UPDATE_USER);
			
			type = jsonObject.getString(FIELD_TYPE);
			
			String createStr = jsonObject.getString(FIELD_CREATEDATE);
			if(createStr != null && !createStr.equals("")) {
				createStr = createStr.replaceAll("/", "").replaceAll("[Date()]", "");
				Long createLong = Long.valueOf(createStr);
				createDate = new Date(createLong);
			}
			
			String updateStr = jsonObject.getString(FIELD_UPDATEDATE);
			if(updateStr != null && !updateStr.equals("")) {
				updateStr = updateStr.replaceAll("/", "").replaceAll("[Date()]", "");
				Long updateLong = Long.valueOf(updateStr);
				updateDate = new Date(updateLong);
			}
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
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
	
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	@Override
	public String toString() {
		return title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
