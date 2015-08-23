package com.stefan.city.module.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * REDataBase
 * <br>描述：顶级数据库操作类，定义了数据库的名称和版本
 * @author 日期：2014-7-11下午04:15:09
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class REDataBase extends SQLiteOpenHelper {
	
	public final static String DB_NAME = "StefanCity.db";
	public final static int DB_VERSION = 1;
	

	public REDataBase(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
