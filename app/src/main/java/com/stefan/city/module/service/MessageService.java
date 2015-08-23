package com.stefan.city.module.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.MessageEntity;
import com.stefan.city.module.service.base.BaseService;

/**
 * MessageService
 * 留言信息管理
 * @author 日期：2014-6-14下午10:27:42
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class MessageService extends BaseService {
	/** 留言信息管理Server URL **/
	public static final String MESSAGE_URL = ContantURL.URL_SERVER + "Srvs/wallsrv.asmx/";
	/** 提交留言，用于首次提交**/
	public static final String Method_Insert = "InsertMessage";
	/** 回复留言信息 **/
	public static final String Method_ReplayMsg = "ReplayMessage";
	/** 删除留言信息**/
	public static final String Method_DelMsg = "deleteMessage";
	/** 获得发布内容中留言信息列表**/
	public static final String Method_MsgList = "getWallList";
	
	/**
	 * 提交新的留言
	 * @param entity
	 * @return
	 */
	public int insert(MessageEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_MSG, entity.getMsg()));
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_OWNER_ID, entity.getOwnerId()+""));
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_ITEM_ID, entity.getItemId()+""));
		
		String result = postData(MESSAGE_URL+Method_Insert, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 回复已有的留言
	 * @param entity
	 * @return
	 */
	public int replayMsg(MessageEntity entity) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_MSG, entity.getMsg()));
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_OWNER_ID, entity.getOwnerId()+""));
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_ITEM_ID, entity.getItemId()+""));
		
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_PARENT_ID, entity.getParentId()+""));
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_REPLIER_ID, entity.getReplierID()+""));
		
		String result = postData(MESSAGE_URL+Method_ReplayMsg, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除指定的留言信息
	 * @param entity
	 * @return
	 */
	public int deleteMsg(int msgId) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_ID, msgId+""));
		
		String result = postData(MESSAGE_URL+Method_DelMsg, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 根据内容ID来获得全部留言信息
	 * @param categoryId
	 * @return
	 */
	public List<MessageEntity> getListByItem(String itemId) {
		List<MessageEntity> list = null;
		
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(MessageEntity.FIELD_ITEM_ID, itemId));
		
		String dataStr = postData(MESSAGE_URL+Method_MsgList, postData, "utf-8");
		if(dataStr != null && !dataStr.equals("")) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(dataStr);
				if(jsonArray != null) {
					list = new ArrayList<MessageEntity>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						MessageEntity entity = new MessageEntity(jsonObject);
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
