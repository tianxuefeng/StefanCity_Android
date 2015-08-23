package com.stefan.city.ui.adapter;

import java.util.List;

//import com.google.android.gms.internal.en;
import com.stefan.city.R;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.UserEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * UserListAdapter
 * @author 日期：2015-4-13下午12:05:15
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserListAdapter extends BaseAdapter {
	
	private List<UserEntity> list;
	private Context context;
	
	private LayoutInflater inflater;
	
	private Tag tag;
	
	public UserListAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return (list != null) ? list.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserEntity entity = list.get(position);
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.item_user_list, null);
			tag = new Tag();
			tag.labName = (TextView) convertView.findViewById(R.id.item_user_name);
			tag.labMemtype = (TextView) convertView.findViewById(R.id.item_user_memtype);
			tag.labCurrent = (TextView) convertView.findViewById(R.id.item_user_current);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		if(entity != null) {
			tag.labName.setText(entity.getName());
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if(Contant.curUser != null && Contant.curUser.getId().equals(entity.getId())) {
				tag.labCurrent.setVisibility(View.VISIBLE);
				tag.labCurrent.setText(R.string.lab_current_user);
			} else {
				tag.labCurrent.setVisibility(View.GONE);
			}
			String memtype = null;
			switch (entity.getMemType()) {
			case 0:
				memtype = context.getString(R.string.lab_user_memtype_0);
				tag.labMemtype.setTextColor(0xFFff3333);
				break;
				
			case 1:
				memtype = context.getString(R.string.lab_user_memtype_1);
				tag.labMemtype.setTextColor(0xFF333333);
				break;
				
			default:
				memtype = context.getString(R.string.lab_user_memtype_2);
				tag.labMemtype.setTextColor(0xFF33aa33);
				break;
			}
			tag.labMemtype.setText(memtype);
		}
		return convertView;
	}

	public List<UserEntity> getList() {
		return list;
	}

	public void setList(List<UserEntity> list) {
		this.list = list;
	}

	protected class Tag {
		TextView labName;
		TextView labCurrent;
		TextView labMemtype;
	}
}
