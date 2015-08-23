package com.stefan.city.module.service.base;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;

import com.rockeagle.framework.tools.REHttpUtility;

/**
 * BaseService
 * @author 日期：2014-6-12下午03:15:52
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class BaseService {
	
	/**
	 * 以POST方式和服务端进行交互
	 * @param url
	 * @param postData
	 * @param encoding
	 * @return
	 */
	protected String postData(String url, List<NameValuePair> postData, String encoding) {
		String result = "404";
		try {
			result = REHttpUtility.httpPostString(url, null, postData, encoding, encoding);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
