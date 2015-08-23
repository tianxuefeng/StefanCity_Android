package com.stefan.city.ui.page.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.stefan.city.DetailActivity;
import com.stefan.city.R;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.thread.ItemsGetRunnable;
import com.stefan.city.ui.adapter.MainInfoAdapter;

/**
 * PageMainView
 * 	主界面
 * @author 日期：2014-4-8下午09:00:11
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class PageMainView extends BasePageView {
	
	private ListView listView;
	
	private ItemsGetRunnable listInfoRunnable;
	private List<InfoItemEntity> list;
	private boolean isUpdate;	// 是否强制更新数据
	
	private MainInfoAdapter infoAdapter;
	
	private int categoryId;
	
	public PageMainView(Context context) {
		super(context);
		initLayout();
	}
	
	private void initLayout() {
		View view = inflate(getContext(), R.layout.acitivty_list, null);
		listView = (ListView) view.findViewById(R.id.main_listView);
		infoAdapter = new MainInfoAdapter(getContext());
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InfoItemEntity entity = list.get(arg2);
				Intent intent = new Intent(getContext(), DetailActivity.class);
				intent.putExtra("entity", entity);
				getContext().startActivity(intent);
			}
			
		});
		this.addView(view);
	}
	
	@Override
	public void onResume() {
		loadData();
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if(msg.obj != null) {
				list = (ArrayList<InfoItemEntity>) msg.obj;
				infoAdapter.setList(list);
				infoAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(getContext(), R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void loadData() {
		if(list == null || list.size() < 1 || isUpdate) {
			isUpdate = false;
			if(listInfoRunnable != null) {
				listInfoRunnable.isStop();
				listInfoRunnable = null;
			}
//			listInfoRunnable = new ItemsGetRunnable(handler, "NJ", "baixia", categoryId+"", "1", null, 1, 10);
//			new Thread(listInfoRunnable).start();
		}
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
		isUpdate = true;
	}

	@Override
	protected void initVal() {
		
	}

	@Override
	protected void pageLoadData() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void onResume(boolean isLoad) {
		if(isLoad) {
			onResume();
		}
	}
	
}
