package com.stefan.city.module.db;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stefan.city.module.entity.CityModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * RECityData
 * @author 日期：2014-7-21下午11:05:50
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RECityData {

	private SQLiteDatabase database;
	
	public RECityData() {
	}
	
	public void openOrCreateDatabase(String path) {
		database = SQLiteDatabase.openOrCreateDatabase(path, null);
	}
	
	/**
	 * 获得城市数据
	 * @param con
	 * @return
	 */
	public ArrayList<CityModel> getSelectCityNames(String con) {
		ArrayList<CityModel> names = new ArrayList<CityModel>();
		//判断查询的内容是不是汉字
		Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p_str.matcher(con);
		String sqlString = null;
		if (m.find() && m.group(0).equals(con)) {
			sqlString = "SELECT * FROM T_city WHERE AllNameSort LIKE " + "\""
					+ con + "%" + "\"" + " ORDER BY CityName";
		} else {
			sqlString = "SELECT * FROM T_city WHERE NameSort LIKE " + "\""
					+ con + "%" + "\"" + " ORDER BY CityName";
		}
		Cursor cursor = database.rawQuery(sqlString, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CityModel cityModel = new CityModel();
			cityModel.setCityName(cursor.getString(cursor
					.getColumnIndex("AllNameSort")));
			cityModel.setNameSort(cursor.getString(cursor
					.getColumnIndex("CityName")));
			names.add(cityModel);
		}
		cursor.close();
		return names;
	}
	
	/**
	 * 从数据库获取城市数据
	 * 
	 * @return
	 */
	public ArrayList<CityModel> getCityNames() {
		ArrayList<CityModel> names = new ArrayList<CityModel>();
		Cursor cursor = database.rawQuery(
				"SELECT * FROM T_city ORDER BY CityName", null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CityModel cityModel = new CityModel();
			cityModel.setCityName(cursor.getString(cursor
					.getColumnIndex("AllNameSort")));
			cityModel.setNameSort(cursor.getString(cursor
					.getColumnIndex("CityName")));
			names.add(cityModel);
		}
		cursor.close();
		return names;
	}
	
	public void close() {
		if(database != null && database.isOpen()) {
			database.close();
		}
	}
}
