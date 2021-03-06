package com.stefan.city.ui.adapter;

import java.util.List;

import com.stefan.city.R;
import com.stefan.city.module.entity.CategoryEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * CategoryListAdapter
 * @author 日期：2014-7-21下午04:08:11
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CategoryListAdapter extends BaseAdapter {
	
	private List<CategoryEntity> list;
	private LayoutInflater layoutInflater;
	
	private Tag tag;
	
	public CategoryListAdapter(Context context, List<CategoryEntity> list) {
		layoutInflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_local, null);
			tag = new Tag();
			tag.labName = (TextView) convertView.findViewById(R.id.local_pro_name);
			
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag(); 
		}
		CategoryEntity entity = list.get(position);
		String name = entity.getTitle();
		tag.labName.setText(name);
		return convertView;
	}

	protected class Tag {
		public TextView labName;
	}
}
