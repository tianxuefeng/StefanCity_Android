package com.stefan.city.module.thread;

import java.util.ArrayList;
import java.util.List;

import com.rockeagle.framework.core.RockEagleApp;
import com.rockeagle.framework.core.cache.REDataService;
import com.rockeagle.framework.core.cache.REDataServiceImpl;
import com.rockeagle.framework.tools.REHttpUtility;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.service.CategoryService;

import android.content.Context;
import android.os.Handler;

/**
 * CategorySendRunnable
 * 获得一级分类和一级分类下面的二级分类信息
 * @author 日期：2014-7-3下午09:42:34
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CategorySendRunnable extends REBaseRunnable {
	
	public static final String GroupsCache = "CategoryGroups";
	
	public static final String ChildsCache = "CategoryChilds";
	
	private CategoryService categoryService;
	private Context context;
	
	private String language;
	private REDataService dataService;

	public CategorySendRunnable(Context context, String language, Handler handler) {
		super(handler);
		this.context = context;
		this.language = language;
		categoryService = new CategoryService();
		dataService = new REDataServiceImpl(RockEagleApp.app.CachePath);
	}

	@Override
	public void run() {
		int what = 0;
		List<CategoryEntity> list = null;
		CategoryEntity[][] childs = null;
		if(REHttpUtility.isCheckConOK() && REHttpUtility.isNetwork(context)){
			Object groupsObj = dataService.readCacheObj(GroupsCache);
			if(groupsObj != null) {
				list = (ArrayList<CategoryEntity>) groupsObj;
			}
			list = categoryService.getList("0", language);
			if(list != null && list.size() > 0) {
				dataService.saveCacheObj(GroupsCache, list);
				if(childs == null) {
					childs = new CategoryEntity[list.size()][];
					for (int i = 0; i < list.size(); i++) {
						CategoryEntity entity = list.get(i);
						List<CategoryEntity> entities = categoryService.getList(entity.getId()+"", language);
						if(entities != null && entities.size() > 0) {
							CategoryEntity[] array = new CategoryEntity[entities.size()];
							childs[i] = entities.toArray(array);
						}
					}
					dataService.saveCacheObj(ChildsCache, childs);
				}
				what = 1;
				sendMessage(what, new Object[]{list, childs});
			}
		} else {
			what = -1;
			sendMessage(what, null);
		}
	}

}
