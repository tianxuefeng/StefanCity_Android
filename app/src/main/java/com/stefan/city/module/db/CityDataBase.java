package com.stefan.city.module.db;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.stefan.city.module.entity.CityEntity;
import com.stefan.city.module.entity.ProvinceEntity;
import com.stefan.city.module.entity.StoreEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * CityDataBase
 * 城市数据操作
 * @author 日期：2014-7-11下午05:58:37
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CityDataBase extends REDataBase {
	/** 城市 **/
	public static final String TABLE_CITY = "table_city";
	/** 城市ID **/
	public static final String TABLE_CITY_ID = "city_id";
	/** 城市名称 **/
	public static final String TABLE_CITY_NAME = "city_name";
	/** 城市所属省份 **/
	public static final String TABLE_CITY_PARENT_ID = "parent_id";
	
	/** 省份 **/
	public static final String TABLE_PROVINCE = "table_province";
	/** 省份ID **/
	public static final String TABLE_PROVINCE_ID = "province_id";
	/** 省份名称 **/
	public static final String TABLE_PROVINCE_NAME = "province_name";
	
	public static final String TABLE_HISTORY_NAME = "table_store";
	public static final String HISTORY_FIELD_ITEM_ID = "itemId";
	public static final String HISTORY_FIELD_TITLE = "title";
	public static final String HISTORY_FIELD_TIME = "time";
	public static final String HISTORY_FIELD_TYPE = "type";
	
	public CityDataBase(Context context) {
		super(context);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlCity = "CREATE TABLE " +TABLE_CITY+" ("+
		TABLE_CITY_ID + " text primary key, " +
		TABLE_CITY_NAME + " text, " +
		TABLE_CITY_PARENT_ID + " text " +
			");";
		String sql = "CREATE TABLE " +TABLE_PROVINCE+" ("+
		TABLE_PROVINCE_ID + " text primary key, " +
		TABLE_PROVINCE_NAME + " text " +
			");";
		
		String sqlHistory = "CREATE TABLE " + TABLE_HISTORY_NAME + " (" + HISTORY_FIELD_ITEM_ID
		+ " text primary key, " 
		+ HISTORY_FIELD_TITLE + " text, "
		+ HISTORY_FIELD_TIME + " text, " 
		+ HISTORY_FIELD_TYPE
		+ " INTEGER" + ");";
		
		db.execSQL(sql);
		db.execSQL(sqlCity);
		db.execSQL(sqlHistory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sqlCity = "DROP IF EXISTS "+TABLE_CITY;
		String sql = "DROP IF EXISTS "+TABLE_PROVINCE;
		String sqlHistory = "DROP IF EXISTS " + TABLE_HISTORY_NAME;
		db.execSQL(sqlCity);
		db.execSQL(sql);
		db.execSQL(sqlHistory);
		onCreate(db);
	}
	
	/**
	 *  获得所有省份信息
	 *  数据库没有数据则返回 null
	 * @return List<StoreEntity>
	 */
	public List<ProvinceEntity> findProAll(){
		List<ProvinceEntity> list = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PROVINCE, null, null, null, null, null, null);
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
			int counts = cursor.getCount();
			list = new ArrayList<ProvinceEntity>();
			for (int i = 0; i < counts; i++) {
				ProvinceEntity bean = new ProvinceEntity();
				bean.setProvinceId(cursor.getString(0));
				bean.setName(cursor.getString(1));
				list.add(bean);
				cursor.moveToNext();
			}
		}
		if(cursor != null)
			cursor.close();
		if(db != null)
			db.close();
		return list;
	}

	/**
	 * 指定条件和值来搜索省份信息
	 * @param where
	 * @param value
	 * @return
	 */
	public List<ProvinceEntity> searchPro(String field, String value){
		List<ProvinceEntity> list = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String where = field + " = ? ";
		String[] whereValue = {value};
		
		Cursor cursor = db.query(TABLE_PROVINCE, null, where, whereValue, null, null, null);
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
			int counts = cursor.getCount();
			list = new ArrayList<ProvinceEntity>();
			for (int i = 0; i < counts; i++) {
				ProvinceEntity bean = new ProvinceEntity();
				bean.setProvinceId(cursor.getString(0));
				bean.setName(cursor.getString(1));
				list.add(bean);
				cursor.moveToNext();
			}
		}
		if(cursor != null)
			cursor.close();
		if(db != null)
			db.close();
		
		return list;
	}
	
	/**
	 *  添加指定信息
	 * @param bean 需要添加的数据
	 * @return long
	 */
	public long insertPro(ProvinceEntity bean){
		long row = 0;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(TABLE_PROVINCE_ID, bean.getProvinceId());
		values.put(TABLE_PROVINCE_NAME, bean.getName());
		row = db.insert(TABLE_PROVINCE, null, values);
		
		if(values != null)
			values.clear();
		if(db != null)
			db.close();
		
		return row;
	}
	
	/**
	 *  修改指定数据
	 * @param bean 需要修改的数据
	 * @return row 影响的行数
	 */
	public int updatePro(ProvinceEntity bean){
		int row = 0;
		SQLiteDatabase db = getWritableDatabase();

		// 设置要修改的数据
		ContentValues values = new ContentValues();
		values.put(TABLE_PROVINCE_ID, bean.getProvinceId());
		values.put(TABLE_PROVINCE_NAME, bean.getName());
		
		// 设置条件
		String where = TABLE_PROVINCE_ID+"=?";
		String[] whereValue = {bean.getProvinceId()};
		
		row = db.update(TABLE_PROVINCE, values, where, whereValue);
		values.clear();
		db.close();
		
		return row;
	}
	
	/**
	 *  删除指定数据
	 * @param id 需要删除的数据ID
	 * @return row 影响的行数
	 */
	public int deletePro(String id){
		SQLiteDatabase db = getWritableDatabase();
		int row = 0;
		String where = TABLE_PROVINCE_ID+"=?";
		String[] whereValue = {id};
		
		row = db.delete(TABLE_PROVINCE, where, whereValue);
		if(db != null)
			db.close();
		
		return row;
	}
	
	/**
	 *  获得所有省份信息
	 *  数据库没有数据则返回 null
	 * @return List<StoreEntity>
	 */
	public List<CityEntity> findCityAll(){
		List<CityEntity> list = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_CITY, null, null, null, null, null, null);
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
			int counts = cursor.getCount();
			list = new ArrayList<CityEntity>();
			for (int i = 0; i < counts; i++) {
				CityEntity bean = new CityEntity();
				bean.setCityId(cursor.getString(0));
				bean.setCityName(cursor.getString(1));
				bean.setParentId(cursor.getString(2));
				list.add(bean);
				cursor.moveToNext();
			}
		}
		if(cursor != null)
			cursor.close();
		if(db != null)
			db.close();
		return list;
	}

	/**
	 * 指定条件和值来搜索省份信息
	 * @param where
	 * @param value
	 * @return
	 */
	public List<CityEntity> searchCity(String field, String value){
		List<CityEntity> list = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String where = field + " = ? ";
		String[] whereValue = {value};
		
		Cursor cursor = db.query(TABLE_CITY, null, where, whereValue, null, null, null);
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
			int counts = cursor.getCount();
			list = new ArrayList<CityEntity>();
			for (int i = 0; i < counts; i++) {
				CityEntity bean = new CityEntity();
				bean.setCityId(cursor.getString(0));
				bean.setCityName(cursor.getString(1));
				bean.setParentId(cursor.getString(2));
				list.add(bean);
				cursor.moveToNext();
			}
		}
		if(cursor != null)
			cursor.close();
		if(db != null)
			db.close();
		
		return list;
	}
	
	/**
	 * 指定条件和值来搜索省份信息
	 * @param where
	 * @param value
	 * @return
	 */
	public List<CityEntity> searchByParent(String parentId){
		List<CityEntity> list = null;
		SQLiteDatabase db = this.getReadableDatabase();
		
		String where = TABLE_CITY_PARENT_ID + " = ? ";
		String[] whereValue = {parentId};
		
		Cursor cursor = db.query(TABLE_CITY, null, where, whereValue, null, null, null);
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
			int counts = cursor.getCount();
			list = new ArrayList<CityEntity>();
			for (int i = 0; i < counts; i++) {
				CityEntity bean = new CityEntity();
				bean.setCityId(cursor.getString(0));
				bean.setCityName(cursor.getString(1));
				bean.setParentId(cursor.getString(2));
				list.add(bean);
				cursor.moveToNext();
			}
		}
		if(cursor != null)
			cursor.close();
		if(db != null)
			db.close();
		
		return list;
	}
	
	/**
	 *  添加指定信息
	 * @param bean 需要添加的数据
	 * @return long
	 */
	public long insertCity(CityEntity bean){
		long row = 0;
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(TABLE_CITY_ID, bean.getCityId());
		values.put(TABLE_CITY_NAME, bean.getCityName());
		values.put(TABLE_CITY_PARENT_ID, bean.getParentId());
		row = db.insert(TABLE_CITY, null, values);
		
		if(values != null)
			values.clear();
		if(db != null)
			db.close();
		
		return row;
	}
	
	/**
	 *  修改指定数据
	 * @param bean 需要修改的数据
	 * @return row 影响的行数
	 */
	public int updateCity(CityEntity bean){
		int row = 0;
		SQLiteDatabase db = getWritableDatabase();

		// 设置要修改的数据
		ContentValues values = new ContentValues();
		values.put(TABLE_CITY_ID, bean.getCityId());
		values.put(TABLE_CITY_NAME, bean.getCityName());
		values.put(TABLE_CITY_PARENT_ID, bean.getParentId());
		
		// 设置条件
		String where = TABLE_CITY_ID+"=?";
		String[] whereValue = {bean.getCityId()};
		
		row = db.update(TABLE_CITY, values, where, whereValue);
		values.clear();
		db.close();
		
		return row;
	}
	
	/**
	 *  删除指定数据
	 * @param id 需要删除的数据ID
	 * @return row 影响的行数
	 */
	public int deleteCity(String id){
		SQLiteDatabase db = getWritableDatabase();
		int row = 0;
		String where = TABLE_CITY_ID+"=?";
		String[] whereValue = {id};
		
		row = db.delete(TABLE_CITY, where, whereValue);
		if(db != null)
			db.close();
		
		return row;
	}
	
	/**
	 * 获得所有数据 数据库没有数据则返回 null
	 * 
	 * @return List<StoreEntity>
	 */
	public List<StoreEntity> findHistoryAll() {
		List<StoreEntity> list = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_HISTORY_NAME, null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			int counts = cursor.getCount();
			list = new ArrayList<StoreEntity>();
			for (int i = 0; i < counts; i++) {
				StoreEntity bean = new StoreEntity();
				bean.setItemId(cursor.getString(0));
				bean.setTitle(cursor.getString(1));
				bean.setTime(cursor.getString(2));
				bean.setType(cursor.getInt(3));
				list.add(bean);
				cursor.moveToNext();
			}
		}
		if (cursor != null)
			cursor.close();
		if (db != null)
			db.close();
		return list;
	}

	/**
	 * 指定条件和值来搜索数据
	 * 
	 * @param where
	 * @param value
	 * @return
	 */
	public List<StoreEntity> searchHistory(String field, String value) {
		List<StoreEntity> list = null;
		SQLiteDatabase db = this.getReadableDatabase();

		String where = field + " = ? ";
		String[] whereValue = { value };

		Cursor cursor = db.query(TABLE_HISTORY_NAME, null, where, whereValue, null,
				null, null);
		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			int counts = cursor.getCount();
			list = new ArrayList<StoreEntity>();
			for (int i = 0; i < counts; i++) {
				StoreEntity bean = new StoreEntity();
				bean.setItemId(cursor.getString(0));
				bean.setTitle(cursor.getString(1));
				bean.setTime(cursor.getString(2));
				bean.setType(cursor.getInt(3));
				list.add(bean);
				cursor.moveToNext();
			}
		}
		if (cursor != null)
			cursor.close();
		if (db != null)
			db.close();

		return list;
	}

	/**
	 * 根据主键ID获取指定数据
	 * 
	 * @param where
	 * @param value
	 * @return StoreEntity
	 */
	public StoreEntity searchHistory(String id) {
		StoreEntity entity = null;
		SQLiteDatabase db = this.getReadableDatabase();

		String where = HISTORY_FIELD_ITEM_ID + " = ? ";
		String[] whereValue = { id };
		Cursor cursor = db.query(TABLE_HISTORY_NAME, null, where, whereValue, null,
				null, null);
		if(!cursor.moveToFirst()) {
			return null;
		}
		if (cursor != null && cursor.getCount() > 0) {
			entity = new StoreEntity();
			entity.setItemId(cursor.getString(0));
			entity.setTitle(cursor.getString(1));
			entity.setTime(cursor.getString(2));
			entity.setType(cursor.getInt(3));
		}
		if (cursor != null)
			cursor.close();
		if (db != null)
			db.close();

		return entity;
	}

	/**
	 * 添加指定信息
	 * 
	 * @param bean
	 *            需要添加的数据
	 * @return long
	 */
	public long insertHistory(StoreEntity bean) {
		long row = 0;
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(HISTORY_FIELD_ITEM_ID, bean.getItemId());
		values.put(HISTORY_FIELD_TITLE, bean.getTitle());
		values.put(HISTORY_FIELD_TIME, bean.getTime());
		values.put(HISTORY_FIELD_TYPE, bean.getType());
		row = db.insert(TABLE_HISTORY_NAME, null, values);

		if (values != null)
			values.clear();
		if (db != null)
			db.close();

		return row;
	}

	/**
	 * 修改数据，根据指定的条件修改指定数据
	 * 
	 * @param values
	 * @param where
	 * @param whereVal
	 * @return
	 */
	public int updateHistory(ContentValues values, String where, String[] whereVal) {
		int row = 0;
		SQLiteDatabase db = getWritableDatabase();

		row = db.update(TABLE_HISTORY_NAME, values, where, whereVal);

		values.clear();
		db.close();

		return row;
	}

	/**
	 * 删除指定数据
	 * 
	 * @param id
	 *            需要删除的数据ID
	 * @return row 影响的行数
	 */
	public int deleteHistory(String id) {
		SQLiteDatabase db = getWritableDatabase();
		int row = 0;
		String where = HISTORY_FIELD_ITEM_ID + "=?";
		String[] whereValue = { id };

		row = db.delete(TABLE_HISTORY_NAME, where, whereValue);
		if (db != null)
			db.close();

		return row;
	}

	/**
	 * 根据指定条件删除指定数据
	 * 
	 * @param field
	 *            条件
	 * @param val
	 *            条件的值
	 * @return row 影响的行数
	 */
	public int deleteHistory(String field, String val) {
		SQLiteDatabase db = getWritableDatabase();
		int row = 0;
		String where = field + " = ?";
		String[] whereValue = { val };

		row = db.delete(TABLE_HISTORY_NAME, where, whereValue);
		if (db != null)
			db.close();

		return row;
	}

	/**
	 * 根据指定条件删除指定数据
	 * 
	 * @param field
	 *            条件
	 * @param val
	 *            条件的值
	 * @return row 影响的行数
	 */
	public int deleteHistoryByWhere(String where, String[] whereValue) {
		SQLiteDatabase db = getWritableDatabase();
		int row = 0;

		row = db.delete(TABLE_HISTORY_NAME, where, whereValue);
		if (db != null)
			db.close();

		return row;
	}
}
