package com.stefan.city.tools;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * REFtpFileUtil
 * 
 * @author 日期：2014-5-21下午09:22:33
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 **/
public class REFtpFileUtil {
	/**
	 * 通过ftp上传文件
	 * 
	 * @param url
	 *            ftp服务器地址 如： 192.168.1.110
	 * @param port
	 *            端口如 ： 21
	 * @param username
	 *            登录名
	 * @param password
	 *            密码
	 * @param remotePath
	 *            上到ftp服务器的磁盘路径
	 * @param fileNamePath
	 *            要上传的文件路径
	 * @param fileName
	 *            要上传的文件名
	 * @return
	 */
	public static String ftpUpload(String url, String port, String username,
			String password, String remotePath, String fileNamePath,
			String fileName) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		String returnMessage = "0";
		
		try {
			ftpClient.connect(url, Integer.parseInt(port));
			boolean loginResult = ftpClient.login(username, password);
			int returnCode = ftpClient.getReplyCode();
			if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
				ftpClient.makeDirectory(remotePath);
				// 设置上传目录
				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.enterLocalPassiveMode();
				fis = new FileInputStream(fileNamePath);
				ftpClient.storeFile(fileName, fis);

				returnMessage = "1"; // 上传成功
			} else {// 如果登录失败
				returnMessage = "0";
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP Server Error！", e);
		} finally {
			// IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Close FTP Connection Error！", e);
			}
		}
		return returnMessage;
	}
}
