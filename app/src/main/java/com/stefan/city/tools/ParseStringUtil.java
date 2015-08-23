package com.stefan.city.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ParseStringUtil {
	
	/**
	 * 将指定字符分割的字符串解析成List
	 * @param string
	 * @return
	 */
	public static List<String> strToArray(String string, String regex){
		List<String> strings = new ArrayList<String>();
		int index = 0;
		int last = 0;
		boolean bool = true;
		while(bool){
			last = string.indexOf(regex, last);
			if(last == -1 && last < string.length()){
				String str = string.substring(index, string.length());
				if(str != null && !str.trim().equals("")) {
					strings.add(str);
				}
				bool = false;
				break; 
			}
			String str = string.substring(index, last);
			last += 1;
			index = last;
			if(str != null && !str.trim().equals("")) {
				strings.add(str);
			}
		}
		return strings;
	}
	
	/**
	 * 将数组转成指定字符分割的字符串
	 * @param array
	 * @return
	 */
	public static String arrayToString(String[] array, String regex){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			buffer.append(array[i]);
			if(i != array.length-1){
				buffer.append(regex);
			}
		}
		return buffer.toString();
	}
}
