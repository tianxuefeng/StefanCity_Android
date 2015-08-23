package com.stefan.city.module.entity;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

/**
 * User
 * @author 日期：2014-6-12下午02:14:29
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserEntity implements Serializable {
	
	/** id标识 **/
	public static final String FIELD_ID = "ID";
	/** name标识 **/
	public static final String FIELD_NAME = "Name";
	/** Email标识 **/
	public static final String FIELD_EMAIL = "Email";
	/** Password标识 **/
	public static final String FIELD_PAWD = "Password";
	/** IC标识 **/
	public static final String FIELD_IC = "IC";
	/** Phone标识 **/
	public static final String FIELD_PHONE = "Phone";
	/** QQSkype标识 **/
	public static final String FIELD_QQSkype = "QQSkype";
	/** MemType标识 **/
	public static final String FIELD_MEMTYPE = "MemType";
	/** DeviceToken标识 **/
	public static final String FIELD_DEVICE_TOKEN = "DeviceToken";
	/** City标识 **/
	public static final String FIELD_CITY = "City";
	
	/** 用户注册时间 **/
	public static final String FIELD_REGISTER_TIME = "RegisterTime";
	
	private String id;
	private String name;
	private String pawd;
	private String email;
	private String ic;
	private String phone;
	private String qqSkype;
	private int memType;
	
	private String city;
	
	private String deviceToken;	// 设备信息
	
	private Date registerTime;
	
	public UserEntity() {
	}

	public UserEntity(String name, String pawd, String email, String ic,
			String phone, String qqSkype, int memType, String city, String deviceToken) {
		super();
		this.name = name;
		this.pawd = pawd;
		this.email = email;
		this.ic = ic;
		this.phone = phone;
		this.qqSkype = qqSkype;
		this.memType = memType;
		this.city = city;
		this.deviceToken = deviceToken;
	}
	
	public UserEntity(UserEntity entity) {
		this.id = entity.id;
		this.name = entity.name;
		this.pawd = entity.pawd;
		this.email = entity.email;
		this.ic = entity.ic;
		this.phone = entity.phone;
		this.qqSkype = entity.qqSkype;
		this.memType = entity.memType;
		this.city = entity.city;
		this.deviceToken = entity.deviceToken;
		this.registerTime = entity.registerTime;
	}
	
	public UserEntity(JSONObject jsonObject) {
		try {
			id = jsonObject.getString(FIELD_ID);
			name = jsonObject.getString(FIELD_NAME);
			email = jsonObject.getString(FIELD_EMAIL);
			pawd = jsonObject.getString(FIELD_PAWD);
			phone = jsonObject.getString(FIELD_PHONE);
			qqSkype = jsonObject.getString(FIELD_QQSkype);
			memType = jsonObject.getInt(FIELD_MEMTYPE);
			ic = jsonObject.getString(FIELD_IC);
			deviceToken = jsonObject.getString(FIELD_DEVICE_TOKEN);
			city = jsonObject.getString(FIELD_CITY);
			
			String registerTimeStr = jsonObject.getString(FIELD_REGISTER_TIME);
			
			if(registerTimeStr != null && !registerTimeStr.equals("")) {
				registerTimeStr = registerTimeStr.replaceAll("/", "").replaceAll("[Date()]", "");
				Long createLong = Long.valueOf(registerTimeStr);
				registerTime = new Date(createLong);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPawd() {
		return pawd;
	}

	public void setPawd(String pawd) {
		this.pawd = pawd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
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

	public int getMemType() {
		return memType;
	}

	public void setMemType(int memType) {
		this.memType = memType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	
}
