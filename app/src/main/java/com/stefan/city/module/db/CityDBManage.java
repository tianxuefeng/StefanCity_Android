package com.stefan.city.module.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.stefan.city.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 
 * RECityData
 * 
 * @author 日期：2014-7-21下午10:49:03
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 * 
 */
public class CityDBManage {
	private final int BUFFER_SIZE = 400000;
	private static final String PACKAGE_NAME = "com.stefan.city";
	public static final String DB_NAME = "china_city.db";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // 存放路径
	private Context mContext;
	private SQLiteDatabase database;

	public CityDBManage(Context context) {
		this.mContext = context;
	}

	/**
	 * 被调用方法
	 */
	public void openDateBase() {
		this.database = this.openDateBase(DB_PATH + "/" + DB_NAME);

	}

	/**
	 * 打开数据库
	 * 
	 * @param dbFile
	 * @return SQLiteDatabase
	 * @author sy
	 */
	private SQLiteDatabase openDateBase(String dbFile) {
		File file = new File(dbFile);
		if (!file.exists()) {
			// // 打开raw中得数据库文件，获得stream流
			InputStream stream = this.mContext.getResources().openRawResource(
					R.raw.china_city);
			try {

				// 将获取到的stream 流写入道data中
				FileOutputStream outputStream = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = stream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, count);
				}
				outputStream.close();
				stream.close();
				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbFile,
						null);
				return db;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return database;
	}

	public void closeDatabase() {
		if (database != null && database.isOpen()) {
			this.database.close();
		}
	}
}
