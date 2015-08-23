package com.stefan.city.module.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.service.base.BaseService;

/**
 * ItemInfoService
 * 内容信息管理，包含：新增信息、活得信息列表、编辑、删除
 * @author 日期：2014-6-12下午03:14:23
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ItemInfoService extends BaseService {
	
	/** 内容信息管理Server URL **/
	public static final String ITEMINFO_URL = ContantURL.URL_SERVER + "Srvs/itemsrv.asmx/";
	
	
	/** 根据ID获得详细信息**/
	public static final String Method_ItemDetail = "getItemDetail";
	/** 发布信息**/
	public static final String Method_InsertItem = "InsertItem";
	/** 编辑发布的信息**/
	public static final String Method_UpdateItem = "UpdateItem";
	/** 删除发布的信息**/
	public static final String Method_DelItem = "delItem";
	/** 获得所有信息列表**/
	public static final String Method_ItemList = "getItemList";
	/** 以分页的方式获得所有信息列表**/
	public static final String Method_ItemListWithPage = "getItemListByCategoryIDWithPage";
	
	/** 通过关键字搜索信息 **/
	public static final String Method_ItemSearchWithPage = "searchItemsByKeywordWithPage";
	
	/** 获得指定用户发布的信息 **/
	public static final String Method_MyPublishedItems = "getMyPublishedItems";
	
	/**
	 * 发布新的信息
	 * @param entity
	 * @return
	 */
	public int insert(InfoItemEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Title, entity.getTitle()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Desc, entity.getDescription()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Price, entity.getPrice()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Images, entity.getImages()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Phone, entity.getPhone()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_QQSkype, entity.getQqSkype()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_CountryCode, entity.getCountryCode()));
		
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_City, entity.getCity()));
		if(entity.getRegion() != null) {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Region, entity.getRegion()));
		} else {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Region, ""));
		}
		if(entity.getSubRegion() != null) {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Sub_Region, entity.getSubRegion()));
		} else {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Sub_Region, ""));
		}
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_PostalCode, entity.getPostalCode()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Address, entity.getAddress()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_CategoryID, entity.getCategoryID()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Type, entity.getType()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Rank, entity.getRank()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Sequence, entity.getSequence()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_UserID, entity.getUserId()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Status, entity.getStatus()+""));
		
		String result = postData(ITEMINFO_URL+Method_InsertItem, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		} else if (result.indexOf("blocked") != -1) {
			return -2; // 该用户被屏蔽，禁止发布信息
		}
		return 0;
	}
	
	/**
	 * 修改发布的信息
	 * @param entity
	 * @return
	 */
	public int update(InfoItemEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_ID, entity.getId()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Title, entity.getTitle()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Desc, entity.getDescription()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Price, entity.getPrice()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Images, entity.getImages()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Phone, entity.getPhone()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_QQSkype, entity.getQqSkype()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_CountryCode, entity.getCountryCode()));
		
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_City, entity.getCity()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Region, entity.getRegion()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_PostalCode, entity.getPostalCode()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Address, entity.getAddress()));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_CategoryID, entity.getCategoryID()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Type, entity.getType()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Rank, entity.getRank()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Sequence, entity.getSequence()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_UserID, entity.getUserId()+""));
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Status, entity.getStatus()+""));
		
		String result = postData(ITEMINFO_URL+Method_UpdateItem, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除指定的发布信息
	 * @param entity
	 * @return
	 */
	public int deleteById(String id) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_ID, id));
		String result = postData(ITEMINFO_URL+Method_DelItem, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 通过ID获得详细信息
	 * @param id
	 * @return
	 */
	public InfoItemEntity findById(String id) {
		InfoItemEntity entity = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_ID, id));
		
		String dataStr = postData(ITEMINFO_URL+Method_ItemDetail, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			try {
				JSONObject json = new JSONObject(dataStr);
				entity = new InfoItemEntity(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return entity;
	}

	/**
	 * 根据分类来获得信息列表
	 * @param categoryId
	 * @return
	 */
	public List<InfoItemEntity> getList(int categoryId, Integer type) {
		List<InfoItemEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_CategoryID, categoryId+""));
		if(type != null)
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Type, type+""));
		
		String dataStr = postData(ITEMINFO_URL+Method_ItemList, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<InfoItemEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						InfoItemEntity entity = new InfoItemEntity(jsonObject);
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
	 * 根据分类来获得信息列表
	 * @param categoryId
	 * @return
	 */
	public List<InfoItemEntity> getSearch(String keyword, String city, int pageNo, int pageSize) {
		List<InfoItemEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_City, city));
		if(keyword != null)
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Search, keyword));
		postData.add(new BasicNameValuePair("page", pageNo+""));
		postData.add(new BasicNameValuePair("pageSize", pageSize+""));
		
		String dataStr = postData(ITEMINFO_URL+Method_ItemSearchWithPage, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<InfoItemEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						InfoItemEntity entity = new InfoItemEntity(jsonObject);
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
	 * 以分页的方式获取信息列表
	 * @param city
	 * @param region
	 * @param categoryID
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<InfoItemEntity> getPage(String city, String region, String regionStreet, String parentId, 
			String categoryID, String type, int pageNo, int pageSize) {
		List<InfoItemEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		if(city != null)
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_City, city));
		if(region != null) {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Region, region));
		} else {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Region, ""));
		}
		if(regionStreet != null) {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Sub_Region, regionStreet));
		} else {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Sub_Region, ""));
		}
		// 进行大类别判断
		if(parentId != null && parentId.equals("0")) {
			// 如果为大类别，那么获取当前下属类别所有信息
			if(categoryID != null)
				postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_ParentID, categoryID));
			
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_CategoryID, "0"));
		} else {
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_ParentID, parentId));
			if(categoryID != null)
				postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_CategoryID, categoryID));
		}
		if(type != null)
			postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_Type, type));
		postData.add(new BasicNameValuePair("page", pageNo+""));
		postData.add(new BasicNameValuePair("pageSize", pageSize+""));
		
		String dataStr = postData(ITEMINFO_URL+Method_ItemListWithPage, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<InfoItemEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						InfoItemEntity entity = new InfoItemEntity(jsonObject);
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
	 * 获得指定用户发布的信息
	 * @param userId
	 * @return
	 */
	public List<InfoItemEntity> getByUserId(String userId) {
		List<InfoItemEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(InfoItemEntity.FIELD_UserID, userId));
		
		String dataStr = postData(ITEMINFO_URL+Method_MyPublishedItems, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<InfoItemEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						InfoItemEntity entity = new InfoItemEntity(jsonObject);
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
