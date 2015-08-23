package com.stefan.city.ui.adapter;

import java.util.List;

import com.stefan.city.R;
import com.stefan.city.module.entity.StoreEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * StoreAdpater
 * 
 * @author 日期：2013-3-5下午09:51:30
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021 All Rights Reserved.
 **/
public class StoreAdpater extends BaseAdapter {
	private List<StoreEntity> storeArray;
	private LayoutInflater inflater;

	public StoreAdpater(Context context, List<StoreEntity> storeArray) {
		inflater = LayoutInflater.from(context);
		this.storeArray = storeArray;
	}

	public int getCount() {
		return (storeArray != null) ? storeArray.size() : 0;
	}

	public Object getItem(int position) {
		return storeArray.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		StoreEntity entity = storeArray.get(position);
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = inflater.inflate(R.layout.item_store, null);
			tag.lab_Title = (TextView) convertView
					.findViewById(R.id.lab_StoretTitle);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		tag.lab_Title.setText(entity.getTitle());
		return convertView;
	}

	protected class Tag {
		TextView lab_Title;
	}
}
