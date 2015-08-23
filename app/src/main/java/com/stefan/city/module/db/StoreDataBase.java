package com.stefan.city.module.db;

import java.util.ArrayList;
import java.util.List;

import com.stefan.city.module.entity.StoreEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * StoreDataBase
 * 历史记录
 * @author 日期：2013-3-5下午09:49:08
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021 All Rights Reserved.
 **/
public class StoreDataBase extends REDataBase {
	public static final String TABLE_HISTORY_NAME = "table_store";
	public static final String HISTORY_FIELD_ITEM_ID = "itemId";
	public static final String HISTORY_FIELD_TITLE = "title";
	public static final String HISTORY_FIELD_TYPE = "type";

	public StoreDataBase(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlHistory = "CREATE TABLE " + TABLE_HISTORY_NAME + " (" + HISTORY_FIELD_ITEM_ID
				+ " text primary key, " + HISTORY_FIELD_TITLE + " text, " + HISTORY_FIELD_TYPE
				+ " INTEGER" + ");";
		db.execSQL(sqlHistory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sqlHistory = "DROP IF EXISTS " + TABLE_HISTORY_NAME;
		db.execSQL(sqlHistory);
		onCreate(db);
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
				bean.setType(cursor.getInt(2));
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
				bean.setType(cursor.getInt(2));
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
		if (cursor != null && cursor.getCount() > 0) {
			entity = new StoreEntity();
			entity.setItemId(cursor.getString(0));
			entity.setTitle(cursor.getString(1));
			entity.setType(cursor.getInt(2));
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
