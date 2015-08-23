package com.stefan.city.module.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.service.base.BaseService;

/**
 * RegionService
 * @author 日期：2014-8-30上午10:49:40
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegionService extends BaseService {

	/** 分类信息管理Server URL **/
	public static final String REGION_URL = ContantURL.URL_SERVER + "srvs/regionsrv.asmx/";
	/** 新增 **/
	public static final String Method_Insert = "InsertRegion";
	/** 修改 **/
	public static final String Method_Update = "UpdateRegion";
	/** 删除 **/
	public static final String Method_Delete = "DeleteRegion";
	/** 获得信息**/
	public static final String Method_RegionList = "getRegionList";
	/** 通过经纬度和语言参数，获得指定信息**/
	public static final String Method_Location = "getLocationInfoByLatLng";
	/** 获得该城市是否已有管理员，如果有的话，那就不进行城市自动添加 **/
	public static final String Method_IsManagedCity = "IsManagedCity";
	/** 得到父级和子集地区列表 **/
	public static final String Method_TwoLevelRegionList = "getTwoLevelRegionList";
	
	/**
	 * 指定父级地区来获得地区信息
	 * @param parentName
	 * @return
	 */
	public List<RegionManEntity> getList(String parentName, String language) {
		List<RegionManEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_PARENT_NAME, parentName));
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_LANGUAGE, language));
		String dataStr = postData(REGION_URL+Method_RegionList, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<RegionManEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						RegionManEntity entity = new RegionManEntity(jsonObject);
						list.add(entity);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	/**
	 * 根据参入的地区参数，得到该地区下面两级地区list信息
	 * @param parentName
	 * @return
	 */
	public List<RegionManEntity> getTwoLevelRegionList(String parentName, String languageCode) {
		
		List<RegionManEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_PARENT_NAME, parentName));
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_LANGUAGE, languageCode));
		String dataStr = postData(REGION_URL+Method_TwoLevelRegionList, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<RegionManEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject json = jsonArray.getJSONObject(i);
						RegionManEntity entity = new RegionManEntity();
						entity.setName(json.getString("Region"));
						entity.setParentName(json.getString("ParentRegion"));
						list.add(entity);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return list;
		
	}
	
	/**
	 * 判断当前城市是否已有管理员，有的话返回true
	 * @param city
	 * @param language
	 * @return
	 */
	public boolean isManagedCity(String city, String language) {
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_CITY, city));
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_LANGUAGE_C, language));
		String result = postData(REGION_URL+Method_IsManagedCity, postData, "utf-8");
		
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return true;	// 已存在
		} else if(result.startsWith("false")) {
			return false; 
		}
		return false;
	}
	
	/**
	 * 新增地区信息
	 * @param entity
	 * @return
	 */
	public int insert(RegionManEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_ID, entity.getId()));
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_NAME, entity.getName()));
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_PARENT_NAME, entity.getParentName()));
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_LANGUAGE, entity.getLanguageCode()));
		String result = postData(REGION_URL+Method_Insert, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		} else if(result.startsWith("false")) {
			return -1; // 已存在
		}
		return 0;
	}
	
	/**
	 * 新增地区信息
	 * @param entity
	 * @return
	 */
	public int update(RegionManEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_ID, entity.getId()));
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_NAME, entity.getName()));
		
		String result = postData(REGION_URL+Method_Update, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 新增地区信息
	 * @param entity
	 * @return
	 */
	public int delete(String regionId) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(RegionManEntity.FIELD_ID, regionId));
		
		String result = postData(REGION_URL+Method_Delete, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 通过传入经纬度和指定语言国际化，返回对应的定位信息
	 * @param latitude	
	 * @param longitude
	 * @param language
	 * @return
	 */
	public String getLocation(double latitude, double longitude, String language) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		
		postData.add(new BasicNameValuePair("Latitude", latitude+""));
		postData.add(new BasicNameValuePair("Longtitude", longitude+""));
		postData.add(new BasicNameValuePair("Language", language));
		
		String result = postData(REGION_URL+Method_Location, postData, "utf-8");
		
		return result;
	}
	
}
