package com.stefan.city.module.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rockeagle.framework.tools.RESecretUtility;
import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.service.base.BaseService;

/**
 * MemberService
 * 	用户信息管理，包括用户注册、修改密码、登陆判断以及修改用户信息
 * @author 日期：2014-6-12上午10:07:46
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class MemberService extends BaseService {
	
	/** 用户信息管理Server URL **/
	public static final String MEMBER_URL = ContantURL.URL_SERVER + "Srvs/membersrv.asmx/";
	/** 用户登陆 **/
	public static final String Method_Login = "Login";
	/** 用户注册**/
	public static final String Method_Register = "InsertMember";
	/** 用户修改密码**/
	public static final String Method_UpdatePawd = "ChangePasswordWithOldPassword";
	/** 修改用户信息**/
	public static final String Method_Update = "UpdateMember";
	/** 获得详细用户信息**/
	public static final String Method_Detail = "MemberDetail";
	/** 判断当前城市是否有管理员**/
	public static final String Method_HasAdmin = "hasAdmin";
	/** 屏蔽指定用户**/
	public static final String Method_BockAnMember = "bockAnMember";
	/** 修改用户类型**/
	public static final String Method_ChangeMemberType = "changeMemberType";
	/** 获得指定城市的用户信息 **/
	public static final String Method_MemberListByCity = "getMemberListByCity";
	
	/**
	 * 获得指定城市中的所有用户信息
	 * @param city
	 * @return
	 */
	public List<UserEntity> findListByCity(String city) {
		List<UserEntity> list = null;
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_CITY, city));
		
		String result = postData(MEMBER_URL+Method_MemberListByCity, postData, "utf-8");
		if(result != null && !result.equals("")) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				list = new ArrayList<UserEntity>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					UserEntity entity = new UserEntity(json);
					list.add(entity);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 获得用户详细信息
	 * @param userId
	 * @return
	 */
	public UserEntity findById(String userId) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_ID, userId));
		String result = postData(MEMBER_URL+Method_Detail, postData, "utf-8");
		if(result != null && !result.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				UserEntity user = new UserEntity(jsonObject);
				return user;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 用户登陆，登陆成功则返回当前用户信息，失败返回null
	 * @param email
	 * @param pawd
	 * @return
	 */
	public UserEntity toLogin(String email, String pawd) {
		if(pawd == null) {
			return null;
		}
		int id = loginCheck(UserEntity.FIELD_EMAIL, email, pawd);
		if(id == -1) {
			return null;
		}
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_ID, id+""));
		String result = postData(MEMBER_URL+Method_Detail, postData, "utf-8");
		if(result != null && !result.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				UserEntity user = new UserEntity(jsonObject);
				return user;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 用户登陆，登陆成功则返回当前用户信息，失败返回null
	 * @param email
	 * @param pawd MD5加密过的数据
	 * @return
	 */
	public UserEntity toLoginMD5(String email, String pawd) {
		if(pawd == null) {
			return null;
		}
		int id = loginCheckMD5(UserEntity.FIELD_EMAIL, email, pawd);
		if(id == -1) {
			return null;
		}
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_ID, id+""));
		String result = postData(MEMBER_URL+Method_Detail, postData, "utf-8");
		if(result != null && !result.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				UserEntity user = new UserEntity(jsonObject);
				return user;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 用户登陆判断
	 * @param fields
	 * @param value
	 * @param pawd
	 * @return	登陆失败返回-1，正确则返回用户ID
	 */
	public int loginCheck(String fields, String value, String pawd) {
		if(pawd == null) {
			return -1;
		}
		String pawdMd5 = RESecretUtility.getMD5Str(pawd);
		return loginCheckMD5(fields, value, pawdMd5);
	}
	
	/**
	 * 用户登陆判断
	 * @param fields
	 * @param value
	 * @param pawdMd5
	 * @return	登陆失败返回-1，正确则返回用户ID
	 */
	public int loginCheckMD5(String fields, String value, String pawdMd5) {
		if(pawdMd5 == null) {
			return -1;
		}
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(fields, value));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_PAWD, pawdMd5));
		// 通过post方式提交数据
		String result = postData(MEMBER_URL+Method_Login, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			String idStr = result.substring(result.lastIndexOf("|")+1, result.length());
			try{
				int id = Integer.parseInt(idStr);
				return id;
			} catch (Exception e) {
			}
		}
		return -1;
	}
	
	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
	public int register(UserEntity user) {
		if(user.getPawd() == null) {
			return -1;
		}
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_NAME, user.getName()));
		String pawdMd5 = RESecretUtility.getMD5Str(user.getPawd());
		postData.add(new BasicNameValuePair(UserEntity.FIELD_PAWD, pawdMd5));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_EMAIL, user.getEmail()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_PHONE, user.getPhone()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_CITY, user.getCity()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_MEMTYPE, user.getMemType()+""));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_QQSkype, user.getQqSkype()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_DEVICE_TOKEN, user.getDeviceToken()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_IC, user.getIc()));
		// 通过post方式提交数据
		String result = postData(MEMBER_URL+Method_Register, postData, "utf-8");
		result = result.replaceAll("\"", "");
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 */
	public int update(UserEntity user, boolean isMd5) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		if(isMd5 && user.getPawd() != null && user.getPawd().length() > 0) {
			String pawdMd5 = RESecretUtility.getMD5Str(user.getPawd());
			postData.add(new BasicNameValuePair(UserEntity.FIELD_PAWD, pawdMd5));
		} else {
			postData.add(new BasicNameValuePair(UserEntity.FIELD_PAWD, user.getPawd()));
		}
		postData.add(new BasicNameValuePair(UserEntity.FIELD_ID, user.getId()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_NAME, user.getName()));
//		postData.add(new BasicNameValuePair(UserEntity.FIELD_EMAIL, user.getEmail()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_PHONE, user.getPhone()));
//		postData.add(new BasicNameValuePair(UserEntity.FIELD_CITY, user.getCity()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_MEMTYPE, user.getMemType()+""));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_QQSkype, user.getQqSkype()));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_DEVICE_TOKEN, user.getDeviceToken()));
		// 通过post方式提交数据
		String result = postData(MEMBER_URL+Method_Update, postData, "utf-8");
		result = result.replaceAll("\"", "").toLowerCase();
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	public int updatePassword(String userId, String oldPawd, String newPawd) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		if(userId == null) {
			return 0;
		}
		postData.add(new BasicNameValuePair(UserEntity.FIELD_ID, userId));
		String pawdMd5 = RESecretUtility.getMD5Str(oldPawd);
		postData.add(new BasicNameValuePair("OldPassword", pawdMd5));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_PAWD, RESecretUtility.getMD5Str(newPawd)));
		// 通过post方式提交数据
		String result = postData(MEMBER_URL+Method_UpdatePawd, postData, "utf-8");
		result = result.replaceAll("\"", "").toLowerCase();
		if(result.startsWith("true")) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 判断当前城市是否有管理员
	 * @param city
	 * @return
	 */
	public int isHasAdmin(String city) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_CITY, city));
		String result = postData(MEMBER_URL+Method_HasAdmin, postData, "utf-8");
		if(result == null || result.equals("")) {
			return -1;
		} else if(result.equalsIgnoreCase("false")) {
			return 0;
		}
		return 1;
	}
	
	/**
	 * 屏蔽指定用户，屏蔽后的用户不能发布信息
	 * @param city
	 * @return
	 */
	public int bockAnMember(String userId) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_ID, userId));
		String result = postData(MEMBER_URL+Method_BockAnMember, postData, "utf-8");
		if(result == null || result.equals("")) {
			return -1;
		} else if(result.toLowerCase().startsWith("false")) {
			return 0;
		}
		return 1;
	}
	
	/**
	 * 修改用户类型，用于屏蔽和解除屏蔽、升级管理员等操作
	 * @param city
	 * @return
	 */
	public int updateMemberType(String userId, String memType) {
		List<NameValuePair> postData = new ArrayList<NameValuePair>();
		postData.add(new BasicNameValuePair(UserEntity.FIELD_ID, userId));
		postData.add(new BasicNameValuePair(UserEntity.FIELD_MEMTYPE, memType));
		String result = postData(MEMBER_URL+Method_ChangeMemberType, postData, "utf-8");
		if(result == null || result.equals("")) {
			return -1;
		} else if(result.toLowerCase().startsWith("false")) {
			return 0;
		}
		return 1;
	}
	
}
