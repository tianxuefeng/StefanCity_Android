package com.stefan.city.module.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;

import com.stefan.city.module.entity.CityEntity;
import com.stefan.city.module.entity.ProvinceEntity;

/**
 * CityUtil
 * 初始化的时候，用于解析城市初始文件，生成省份和城市信息
 * @author 日期：2014-7-12下午02:06:35
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CityUtil {
	/** 城市信息文件存放目录 **/
	public static final String CITY_FILE_PATH = "city.txt";
	
	/** 城市信息文件存放目录 **/
	public static final String PROVINCE_FILE_PATH = "province.txt";
	
	/**
	 * 读取城市信息文件，并以list方式返回
	 * @param filePath
	 */
	public static List<CityEntity> readCity(Context context, String filePath) {
		List<CityEntity> list = null;
		InputStream inputStream = null;
		try {
			inputStream = context.getAssets().open(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(inputStream == null) {
			return null;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
			String str="";				
			StringBuilder sb=new StringBuilder();
//			Map<String, String> map = new HashMap<String, String>();
			list = new ArrayList<CityEntity>();
			while((str=reader.readLine())!=null){
				sb.append(str);
			}
			String reagexId = "id=\'.*?\'";
			String reagexData = "data=\'.*?\'";
			String data = sb.toString().replaceAll("\n", "");
			Pattern pattern = Pattern.compile("city=\".*?\"");
			Matcher matcher = pattern.matcher(data);
			while (matcher.find()) {
				String city = matcher.group();
				Pattern pattId = Pattern.compile(reagexId);
				Matcher matId = pattId.matcher(city);
				String parentId = null;
				if(matId.find()) {
					parentId = matId.group().replaceAll("id=\'|\'", "");
				}
				Pattern pattData = Pattern.compile(reagexData);
				Matcher matData = pattData.matcher(city);
				while (matData.find()) {
					String cityStr = matData.group().replaceAll("data=\'|\'", "");
					String[] citys = cityStr.split(";");
					for (String string : citys) {
						CityEntity entity = new CityEntity(string.substring(0, string.indexOf("\t")), string.substring(string.indexOf("\t"), string.length()).replaceAll("\t", ""), parentId);
						list.add(entity);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	/**
	 * 读取省份信息文件，并以list方式返回
	 * @param filePath
	 * @return
	 */
	public static List<ProvinceEntity> readProvince(Context context, String filePath) {
		InputStream inputStream = null;
		try {
			inputStream = context.getAssets().open(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(inputStream == null) {
			return null;
		}
//		AssetInputStream assetInputStream = 
		BufferedReader reader = null;
		List<ProvinceEntity> list = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, "gbk"));
			String str="";				
			list = new ArrayList<ProvinceEntity>();
			while((str=reader.readLine())!=null){
				if(str.indexOf("\t") != -1) {
					ProvinceEntity entity = new ProvinceEntity(str.substring(0, str.indexOf("\t")), 
							str.substring(str.indexOf("\t"), str.length()).replaceAll("\t", ""));
					list.add(entity);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
