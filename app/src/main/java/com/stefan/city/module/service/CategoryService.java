package com.stefan.city.module.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.service.base.BaseService;

/**
 * CategoryService
 * 分类信息管理
 * @author 日期：2014-6-12下午10:26:36
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CategoryService extends BaseService {

	/** 分类信息管理Server URL **/
	public static final String CATEGORY_URL = ContantURL.URL_SERVER + "Srvs/categorysrv.asmx/";
	/** 新增分类 **/
	public static final String Method_Insert = "InsertCategory";
	/** 编辑分类 **/
	public static final String Method_Update = "UpdateCategory";
	/** 删除分类 **/
	public static final String Method_Delete = "DeleteCategory";
	/** 获得分类信息**/
	public static final String Method_CategoryList = "getCategoryList";
	
	/**
	 * 指定父级分类来获得分类信息
	 * @param parentId
	 * @return
	 */
	public List<CategoryEntity> getList(String parentId, String language) {
		List<CategoryEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_PARENT_ID, parentId));
		postData.add(new BasicNameValuePair(CategoryEntity.LanguageCode, language));
		String dataStr = postData(CATEGORY_URL+Method_CategoryList, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<CategoryEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						CategoryEntity entity = new CategoryEntity(jsonObject);
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
	 * 新增分类
	 * @param entity
	 * @return
	 */
	public int insert(CategoryEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_PARENT_ID, entity.getParentId()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_TITLE, entity.getTitle()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_DESC, entity.getDesc()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_IMAGES, entity.getImages()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_SEQUENCE, entity.getSequence()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_CREATE_USER, entity.getCreateUser()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_COUNTRY_CODE, entity.getCountryCode()+""));
		
		String result = postData(CATEGORY_URL+Method_Insert, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 编辑分类
	 * @param entity
	 * @return
	 */
	public int update(CategoryEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_ID, entity.getId()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_PARENT_ID, entity.getParentId()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_TITLE, entity.getTitle()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_DESC, entity.getDesc()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_IMAGES, entity.getImages()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_SEQUENCE, entity.getSequence()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_UPDATE_USER, entity.getUpdateUser()+""));
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_COUNTRY_CODE, entity.getCountryCode()+""));
		
		String result = postData(CATEGORY_URL+Method_Update, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除分类
	 * @param entity
	 * @return
	 */
	public int delete(String id) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(CategoryEntity.FIELD_ID, id));
		
		String result = postData(CATEGORY_URL+Method_Delete, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
}
