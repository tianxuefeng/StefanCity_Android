package com.stefan.city.module.entity;

import java.io.Serializable;

/**
 * LocationEntity
 * @author 日期：2014-10-27下午10:48:55
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class LocationEntity implements Serializable {
	
	private String country;			// 国家
	
	private String administrative;	// 一级行政地区(城市)
	
	private String locality;		// 区域（地区）
	private String region;			// 街道
	private String route;			// 街道号
	
	private String longitude, latitude;
	
	private String detailInfo;		// 详细位置信息
	
	public LocationEntity() {
		
	}

	public LocationEntity(String country, String administrative,
			String locality, String region, String longitude, String latitude,
			String detailInfo) {
		super();
		this.country = country;
		this.administrative = administrative;
		this.locality = locality;
		this.region = region;
		this.longitude = longitude;
		this.latitude = latitude;
		this.detailInfo = detailInfo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAdministrative() {
		return administrative;
	}

	public void setAdministrative(String administrative) {
		this.administrative = administrative;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getDetailInfo() {
		return detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
}
