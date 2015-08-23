package com.stefan.city.module.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.service.base.BaseService;

/**
 * UploadService 用于上传文件
 * 
 * @author 日期：2014-6-15下午03:01:31
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 **/
public class UploadService extends BaseService {

	/** 文件上传服务器路径Server URL **/
	public static final String UPLOAD_URL = ContantURL.URL_SERVER
			+ "upload.aspx";

	public UploadService() {
		// HttpClient httpclient = new DefaultHttpClient();
		// httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
		// HttpVersion.HTTP_1_1);
		//
		// HttpPost httppost = new HttpPost(UPLOAD_URL);
		//
		// String SDCradPath =
		// android.os.Environment.getExternalStorageDirectory()
		// .getAbsolutePath();
		// String filePath = SDCradPath + "/105.png";
		// File file = new File(filePath);
		//
		// FileEntity reqEntity = new FileEntity(file, "binary/octet-stream");
		//
		// httppost.setEntity(reqEntity);
		// reqEntity.setContentType("binary/octet-stream");
		// System.out.println("executing request " + httppost.getRequestLine());
		// HttpResponse response = null;
		// try {
		// response = httpclient.execute(httppost);
		// HttpEntity resEntity = response.getEntity();
		//
		// System.out.println(response.getStatusLine());
		// if (resEntity != null) {
		// System.out.println(EntityUtils.toString(resEntity));
		// }
		// if (resEntity != null) {
		// resEntity.consumeContent();
		// }
		// } catch (ClientProtocolException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// httpclient.getConnectionManager().shutdown();
	}

	/**
	 * 上传文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @param mimeType
	 * @return -1:上传失败，-2：服务器链接失败，1：上传成功
	 */
	public int upload(String filePath, String fileName) {
		Bitmap bitmap = compressImageFromFile(filePath);
		if(bitmap == null) {
			return -1;
		}
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(UPLOAD_URL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// con.setConnectTimeout(6000); // 6秒延迟判断
			// // 检查服务器是否能连通
//			if(con.getResponseCode() != 200){
//			
//			}
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + fileName + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
			InputStream fStream = new ByteArrayInputStream(baos.toByteArray());
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			/* 将Response显示于Dialog */
			/* 关闭DataOutputStream */
			ds.close();
			con.connect();
			bitmap.recycle();
			bitmap = null;
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 压缩图片
	 * @param srcPath
	 * @return
	 */
	private Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 800f;//
		float ww = 480f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		
		return BitmapFactory.decodeFile(srcPath, newOpts);
	}
}
