package com.stefan.city.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stefan.city.R;
import com.stefan.city.module.entity.CityModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * CityInfoAdapter
 * @author 日期：2014-7-21下午10:55:05
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CityInfoAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CityModel> list;
	
	private Map<String, Integer> alphaIndexer;
	private String[] sections;

	public CityInfoAdapter(Context context, List<CityModel> list) {

		this.inflater = LayoutInflater.from(context);
		this.list = list;
		alphaIndexer = new HashMap<String, Integer>();
		sections = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			// 当前汉语拼音首字母
			// getAlpha(list.get(i));
			String currentStr = list.get(i).getNameSort();
			// 上一个汉语拼音首字母，如果不存在为“ ”
			String previewStr = (i - 1) >= 0 ? list.get(i - 1)
					.getNameSort() : " ";
			if (!previewStr.equals(currentStr)) {
				String name = list.get(i).getNameSort();
				alphaIndexer.put(name, i);
				sections[i] = name;
			}
		}

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
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_city_list, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.item_city_alpha);
			holder.name = (TextView) convertView.findViewById(R.id.item_city_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(list.get(position).getCityName());
		String currentStr = list.get(position).getNameSort();
		String previewStr = (position - 1) >= 0 ? list.get(position - 1)
				.getNameSort() : " ";
		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView alpha;
		TextView name;
	}

	public Map<String, Integer> getAlphaIndexer() {
		return alphaIndexer;
	}

	public String[] getSections() {
		return sections;
	}
	
}
