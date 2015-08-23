package com.stefan.city.module.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.FavoriteEntity;
import com.stefan.city.module.service.base.BaseService;

/**
 * FavoriteService
 * 用户收藏信息管理
 * @author 日期：2014-6-13下午09:18:13
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class FavoriteService extends BaseService {

	/** 收藏信息管理Server URL **/
	public static final String FAVORITE_URL = ContantURL.URL_SERVER + "Srvs/favoritesrv.asmx/";
	/** 收藏内容**/
	public static final String Method_Insert = "InsertFavorite";
	/** 清空收藏内容**/
	public static final String Method_Clear = "clearFavorites";
	/** 根据ID删除指定收藏信息**/
	public static final String Method_Delete = "deleteFavorite";
	/** 获得所有收藏信息**/
	public static final String Method_FavoriteList = "getFavoriteList";
	
	/**
	 * 添加收藏信息
	 * @param entity
	 * @return	1表示添加成功，为0表示添加失败
	 */
	public int insert(String userId, String itemId) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(FavoriteEntity.FIELD_ITEM_ID, itemId));
		postData.add(new BasicNameValuePair(FavoriteEntity.FIELD_USER_ID, userId));
		
		String result = postData(FAVORITE_URL+Method_Insert, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 清空指定的收藏信息
	 * @param entity
	 * @return
	 */
	public int deleteById(String id) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair("ID", id));
		
		String result = postData(FAVORITE_URL+Method_Delete, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除指定用户的收藏信息
	 * @param entity
	 * @return
	 */
	public int deleteByUserId(String userId) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(FavoriteEntity.FIELD_USER_ID, userId));
		
		String result = postData(FAVORITE_URL+Method_Clear, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 获得指定用户收藏的信息列表
	 * @param categoryId
	 * @return
	 */
	public List<FavoriteEntity> getListByUser(String userId) {
		List<FavoriteEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(FavoriteEntity.FIELD_USER_ID, userId));
		
		String dataStr = postData(FAVORITE_URL+Method_FavoriteList, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<FavoriteEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						FavoriteEntity entity = new FavoriteEntity(jsonObject);
						list.add(entity);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
}
