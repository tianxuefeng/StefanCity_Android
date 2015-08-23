package com.stefan.city.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.stefan.city.R;
import com.stefan.city.module.entity.MessageEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * MsgInfoAdapter
 * 评论适配器
 * @author 日期：2014-7-21上午10:35:59
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class MsgInfoAdapter extends BaseAdapter {
	
	private List<MessageEntity> list;
	private LayoutInflater inflater;
	
	private Tag tag;
	
	public MsgInfoAdapter(Context context, List<MessageEntity> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
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
	public View getView(int arg0, View view, ViewGroup arg2) {
		if(view == null) {
			view = inflater.inflate(R.layout.item_detail_msg, null);
			tag = new Tag();
			tag.labName = (TextView) view.findViewById(R.id.lab_item_msg_name);
			tag.labTime = (TextView) view.findViewById(R.id.lab_item_msg_time);
			tag.labMsg = (TextView) view.findViewById(R.id.lab_item_msg_info);
			
			view.setTag(tag);
		} else {
			tag = (Tag) view.getTag();
		}
		MessageEntity entity = list.get(arg0);
		if(entity != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = entity.getPostTime();
			if(date != null) {
				tag.labTime.setText(dateFormat.format(date));
			}
			tag.labMsg.setText(entity.getMsg());
		}
		return view;
	}
	
	private final class Tag {
		public TextView labName;
		public TextView labTime;
		public TextView labMsg;
	}

}
