package com.stefan.city.ui.adapter;

import java.util.List;

import com.stefan.city.R;
import com.stefan.city.module.entity.SettingEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * SettingAdapter
 * @author 日期：2013-3-12下午09:51:08
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class SettingAdapter extends BaseAdapter {
	
	private Tag tag;
	private LayoutInflater inflater;
	private List<SettingEntity> entities;
	
	public SettingAdapter(Context context, List<SettingEntity> entities) {
		this.entities = entities;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SettingEntity entity = entities.get(position);
		if(convertView == null) {
			tag = new Tag();
			convertView = inflater.inflate(R.layout.setting_item, null);
			tag.itemText = (TextView) convertView.findViewById(R.id.setting_item_text);
			tag.itemCheck = (TextView) convertView.findViewById(R.id.setting_item_check);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		
		tag.itemText.setText(entity.getItemName());
		int status = entity.getStatus();
		if(status >= 0) {
			tag.itemCheck.setVisibility(View.VISIBLE);
			if(status == 1) {
				tag.itemCheck.setBackgroundResource(R.drawable.switch_on_normal);
			} else {
				tag.itemCheck.setBackgroundResource(R.drawable.switch_off_normal);
			}
		} else {
			tag.itemCheck.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	protected final class Tag {
		TextView itemText;
		TextView itemCheck;
	}

}
