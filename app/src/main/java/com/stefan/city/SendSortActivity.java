package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.thread.CategorySendRunnable;
import com.stefan.city.ui.adapter.SortTreeAdapter;

/**
 * SendSortActivity
 * 发布信息选择分类界面
 * @author 日期：2014-7-3下午08:25:24
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class SendSortActivity extends BaseActivity {
	
	private ExpandableListView listView;
	private SortTreeAdapter sortTreeAdapter;
	
	private Button btnBack;
	
	private List<CategoryEntity> groups;
	private CategoryEntity[][] childs;
	private CategorySendRunnable categorySendRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.acitivity_sendsort);
		
		btnBack = (Button) findViewById(R.id.btn_sort_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 返回
				SendSortActivity.this.finish();
			}
		});
		listView = (ExpandableListView) findViewById(R.id.sort_treeListView);
		listView.setOnGroupClickListener(groupClickListener);
		listView.setOnChildClickListener(childClickListener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}
	
	private void loadData() {
		if(groups == null || groups.size() < 1) {
			if(categorySendRunnable != null) {
				categorySendRunnable.isStop();
				categorySendRunnable = null;
			}
			String language = SharePreferenceHelper.getSharepreferenceString(SendSortActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "zh");
			categorySendRunnable = new CategorySendRunnable(SendSortActivity.this, language, handler);
			new Thread(categorySendRunnable).start();
		}
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if(msg.obj != null) {
				Object[] objects = (Object[]) msg.obj;
				groups = (ArrayList<CategoryEntity>) objects[0];
				childs = (CategoryEntity[][]) objects[1];
				if(sortTreeAdapter == null) {
					sortTreeAdapter = new SortTreeAdapter(SendSortActivity.this, groups, childs, null);
					listView.setAdapter(sortTreeAdapter);
				} else {
					sortTreeAdapter.notifyDataSetChanged();
				}
			}
		};
	};
	
	/**
	 * 跳转到发布详细界面
	 */
	private void toSend(CategoryEntity entity) {
		if(entity == null) {
			return ;
		}
		Intent intent = new Intent(SendSortActivity.this, SendActivity.class);
		intent.putExtra("categoryEntity", entity);
		startActivity(intent);
		SendSortActivity.this.finish();
	}
	
	private OnGroupClickListener groupClickListener = new OnGroupClickListener() {
		
		@Override
		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {
			CategoryEntity entity = groups.get(groupPosition);
			if(childs[groupPosition] != null && childs[groupPosition].length > 0) {
				return false;
			}
			toSend(entity);
			return true;
		}
	};
	
	private OnChildClickListener childClickListener = new OnChildClickListener() {
		
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			CategoryEntity entity = childs[groupPosition][childPosition];
			toSend(entity);
			return true;
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long arg3) {
			int index = (Integer) adapterView.getTag();
			CategoryEntity infoEntity = childs[index][position];
			toSend(infoEntity);
		}
	};
}
