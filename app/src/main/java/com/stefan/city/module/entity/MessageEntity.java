package com.stefan.city.module.entity;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

/**
 * MessageEntity
 * 留言信息
 * @author 日期：2014-6-14下午06:28:58
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class MessageEntity implements Serializable {
	
	/** id标识 **/
	public static final String FIELD_ID = "ID";
	/** msg标识 **/
	public static final String FIELD_MSG = "Msg";
	/** ItemID标识 **/
	public static final String FIELD_ITEM_ID = "ItemID";
	/** OwnerID标识 **/
	public static final String FIELD_OWNER_ID = "OwnerID";
	/** ReplierID标识 **/
	public static final String FIELD_REPLIER_ID = "ReplierID";
	/** ParentID标识 **/
	public static final String FIELD_PARENT_ID = "ParentID";
	/** PostTime标识 **/
	public static final String FIELD_POSTTIME = "PostTime";
	/** ReplyTime标识 **/
	public static final String FIELD_REPLYTIME = "ReplyTime";

	private String id;
	private String msg;
	private String ownerId;	// 第一个留言的人
	private String replierID;	// 回复人ID
	private String parentId;	// 留言引用的父级ID
	
	private String itemId;		// 内容ID

	private Date postTime;
	private Date replyTime;	// 回复时间
	
	public MessageEntity() {
	}

	public MessageEntity(String id, String msg, String ownerId, String replierID,
			String parentId, String itemId, Date postTime, Date replyTime) {
		super();
		this.id = id;
		this.msg = msg;
		this.ownerId = ownerId;
		this.replierID = replierID;
		this.parentId = parentId;
		this.itemId = itemId;
		this.postTime = postTime;
		this.replyTime = replyTime;
	}
	
	public MessageEntity(JSONObject jsonObject) {
		try {
			id = jsonObject.getString(FIELD_ID);
			msg = jsonObject.getString(FIELD_MSG);
			ownerId = jsonObject.getString(FIELD_OWNER_ID);
			replierID = jsonObject.getString(FIELD_REPLIER_ID);
			parentId = jsonObject.getString(FIELD_PARENT_ID);
			itemId = jsonObject.getString(FIELD_ITEM_ID);
			String createStr = jsonObject.getString(FIELD_POSTTIME);
			if(createStr != null && !createStr.equals("")) {
				createStr = createStr.replaceAll("/", "").replaceAll("[Date()]", "");
				Long createLong = Long.valueOf(createStr);
				postTime = new Date(createLong);
			}
			
			String updateStr = jsonObject.getString(FIELD_REPLYTIME);
			if(updateStr != null && !updateStr.equals("")) {
				updateStr = updateStr.replaceAll("/", "").replaceAll("[Date()]", "");
				Long updateLong = Long.valueOf(updateStr);
				replyTime = new Date(updateLong);
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getReplierID() {
		return replierID;
	}

	public void setReplierID(String replierID) {
		this.replierID = replierID;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	
}
