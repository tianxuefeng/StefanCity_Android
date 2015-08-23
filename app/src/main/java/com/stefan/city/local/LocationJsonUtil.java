package com.stefan.city.local;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.google.android.gms.internal.en;
import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.entity.RegionEntity;

/**
 * LocationJsonUtil
 * @author 日期：2014-10-27下午11:17:06
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class LocationJsonUtil {
	
	/**
	 * 解析得到的json位置数据，然后解析成具体位置信息
	 * @param jsonStr
	 * @return
	 */
	public static LocationEntity regexJson(String jsonStr) {
		LocationEntity entity = null;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONArray array = jsonObject.getJSONArray("results");
			if(array == null || array.length() <= 0) {
				return null;
			}
			entity = new LocationEntity();
			JSONObject json1 = array.getJSONObject(0);
			JSONArray detailArray = json1.getJSONArray("address_components");
			String detailInfo = json1.getString("formatted_address");
//			if(detailInfo.indexOf(" ") != -1) {
//				detailInfo = detailInfo.substring(0, detailInfo.indexOf(" "));
//			}
			entity.setDetailInfo(detailInfo);
//			int index = -1;
			// 是否有临近地区
			boolean isPostal = false;
			if(detailArray != null) {
				for (int i = detailArray.length()-1; i > 0 ; i--) {
					// 国家
					JSONObject jsonTemp = detailArray.getJSONObject(i);
					String types = jsonTemp.getString("types");
					if(types.indexOf("\"country\"") != -1) {
						String countryName = jsonTemp.getString("long_name");
//						if(countryName.indexOf("中国") != -1) {
//							isChina = true;
//						}
						// 国家信息
						entity.setCountry(countryName);
//						if(isPostal) {
//							index = detailArray.length()-1 - i - 1;
//						} else {
//							index = detailArray.length()-1 - i;
//						}
						continue ;
					} else if (types.indexOf("\"postal_code\"") != -1) {
						isPostal = true;
						continue ;
					} else if (types.indexOf("\"locality\"") != -1) {
						// 城市
						entity.setAdministrative(jsonTemp.getString("long_name"));
					} else if (types.indexOf("\"neighborhood\"") != -1) {	
						isPostal = true;
					} else if (types.indexOf("\"sublocality\"") != -1 && types.indexOf("\"neighborhood\"") == -1) {
						// 地区
						entity.setLocality(jsonTemp.getString("long_name"));
					} else if (types.indexOf("\"route\"") != -1) {
						if(isPostal) {
							entity.setLocality(jsonTemp.getString("long_name"));
						} else {
							// 街道
							entity.setRegion(jsonTemp.getString("long_name"));
						}
					}
//					if(index != -1) {
//						switch (index) {
//						case 0:
//							if(!isChina) {
//								entity.setAdministrative(jsonTemp.getString("long_name"));
//							}
//							break;
//							
//						case 1: {
//							if(!isChina) {
//								entity.setLocality(jsonTemp.getString("long_name"));
//							} else {
//								entity.setAdministrative(jsonTemp.getString("long_name"));
//							}
//						}
//							break;
//							
//						case 2: {
//							if (types.indexOf("sublocality") != -1 || types.indexOf("administrative_area") != -1) {
//								if(isChina) {
//									entity.setLocality(jsonTemp.getString("long_name"));
//								} else {
//									entity.setRegion(jsonTemp.getString("long_name"));
//								}
//							} else if(types.indexOf("route") != -1) {
//								entity.setRoute(jsonTemp.getString("long_name"));
//							}
//						}
//							break;
//							
//						case 3: {
//							if(types.indexOf("route") != -1) {
//								entity.setRoute(jsonTemp.getString("long_name"));
//							}
//						}
//							break;
//							
//						default:
//							break;
//						}
//						index ++;
//					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}
	
}
