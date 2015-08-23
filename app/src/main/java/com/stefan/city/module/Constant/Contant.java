package com.stefan.city.module.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.entity.UserEntity;

/**
 * Contant
 * @author 日期：2014-4-12上午10:55:54
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class Contant {
	
	public static boolean IsNetStatus = false;
	
	// 当前登陆的用户
	public static UserEntity curUser;
	/** 当前用户的定位信息 **/
	public static LocationEntity curLocationEntity;
	/** 当前用户所在城市的地区列表 **/
	public static List<RegionManEntity> regionManEntities;
	/** 当前选中的地区 **/
	public static RegionManEntity curRegionEntity;
	/** 当前选中的街道 **/
	public static RegionManEntity curStreetRegion;
	/** 当前用户所在城市的地区的街道列表 **/
	public static List<RegionManEntity> regionStreetEntities;
	
	/** 配置文件路径 **/
	public static final String SETTINGSP = "com.stefan.city.city_preferences";
	
	/** 配置信息【是否弹出管理员窗口】 **/
	public static final String PREF_SHOW_DIALOG = "init_showDialog";
	/** 配置信息【保存的程序版本号】 **/
	public static final String PREF_VERSION = "init_version";
	
	/** 配置信息【语言】 **/
	public static final String PREF_LANGUAGE = "city_language";
	
	/** 配置信息-定位信息【国家】 **/
	public static final String PREF_COUNTRY = "gps_country";
	/** 配置信息-定位信息【城市】 **/
	public static final String PREF_CITY = "gps_city";
	/** 配置信息-定位信息【地区】 **/
	public static final String PREF_LOCALITY = "gps_locality";
	/** 配置信息-定位信息【街道】 **/
	public static final String PREF_STREET = "gps_street";
	/** 配置信息-定位信息【详细信息】 **/
	public static final String PREF_DETAIL = "gps_detail";
	/** 配置信息-定位信息【经度】 **/
	public static final String PREF_LONGITUDE = "gps_longitude";
	/** 配置信息-定位信息【纬度】 **/
	public static final String PREF_LATITUDE = "gps_latitude";
	
	/** 设置照片的最大像素为200W像素，照片过大容易出现内存溢出  **/
	public static final int PhotoMaxSize = 2000000;
	
	/** 语言参数，用于获取信息 **/
	public static String LANGUAGE_PARAMETER = "zh-CN";
	
	/** 当前屏幕大小 **/
	public static final int[] ScreenSize = new int[2];
	
	/**
	 * 主界面
	 */
	public static final int VIEW_MAIN = 0;
	
	/**
	 * 积分
	 */
	public static final int VIEW_SCORE = 1;
	
	/**
	 * 个人中心
	 */
	public static final int VIEW_USER = 2;
	
	/** 缓存存放目录 **/
	public static String CACHE_PATH = "cache";
	
	/**
	 * 抓取网页信息时，设置头标识
	 */
	public static final Map<String,String> heads = new HashMap<String, String>();

	static{
		heads.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US)");
	}
	
	/**
	 * 获得当前版本号
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}
	
	/**
	 * 格式化double数据
	 * @param value
	 * @return
	 */
	public static String formatFloatNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("############.00");
            return df.format(value);
        }else{
            return "0.00";
        }
    }
	
	/**
	 * 验证邮箱格式是否正确
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
	
	/**
	 * json数据，listinfo的Key
	 * @author Administrator
	 *
	 */
	public final class JsonItemsKeys {
		/** ID标识【int】 **/
		public static final String ID = "ID"; 
		/** 标题【String】 **/
		public static final String Title = "Title";
		/** 说明 【String】**/
		public static final String Description = "Description";
		/** 价格【double】 **/
		public static final String Price = "Price";
		/** 图片集【String】 **/
		public static final String Images = "Images";
		/** 联系方式【String】 **/
		public static final String Phone = "Phone";
		/** QQ联系方式【String】 **/
		public static final String QQ_Skype = "QQSkype";
		/** 国家标识【int】 **/
		public static final String CountryCode = "CountryCode";
		/** 城市【String】 **/
		public static final String City = "City";
		/** 区域【String】 **/
		public static final String Region = "Region";
		/** 类别【int】 **/
		public static final String CategoryID = "CategoryID";
		/** 置顶级别【int】 **/
		public static final String Rank = "Rank";
		/** 顺序【int】 **/
		public static final String Sequence = "Sequence";
		/** 发布用户ID【int】 **/
		public static final String UserID = "UserID";
		/** 创建日期【String】 **/
		public static final String CreateDate = "CreateDate";
		/** 修改日期【String】 **/
		public static final String UpdateDate = "UpdateDate";
	}
	
}