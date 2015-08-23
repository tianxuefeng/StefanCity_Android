package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.thread.CategoryRunnable;
import com.stefan.city.ui.adapter.CategoryListAdapter;

/**
 * SendCategoryActivity
 * @author 日期：2014-7-21下午03:46:40
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class SendCategoryChildActivity extends BaseActivity {

	private Button btnBack;
	
	private TextView labNotInfo;
	
	private CategoryListAdapter categoryListAdapter;
	private ListView listView;
	private List<CategoryEntity> list; 
	
	private CategoryRunnable categoryRunnable;
	private CategoryEntity entity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.activity_category);
		listView = (ListView) findViewById(R.id.send_category_listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CategoryEntity entity = list.get(arg2);
				toSend(entity);
			}
			
		});
		
		View view = findViewById(R.id.btn_man_category_add);
		view.setVisibility(View.GONE);
		
		labNotInfo = (TextView) findViewById(R.id.lab_category_notinfo);
		
		btnBack = (Button) findViewById(R.id.btn_send_category_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SendCategoryChildActivity.this.finish();
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	/**
	 * 跳转到发布详细界面
	 */
	private void toSend(CategoryEntity entity) {
		if(entity == null) {
			return ;
		}
		Intent intent = new Intent(SendCategoryChildActivity.this, SendActivity.class);
		intent.putExtra("categoryEntity", entity);
		startActivity(intent);
		SendCategoryChildActivity.this.finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}
	
	private void initData() {
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			Object obj = bundle.get("categoryEntity");
			if(obj != null) {
				entity = (CategoryEntity) obj;
			}
		}
		if(entity == null) {
			SendCategoryChildActivity.this.finish();
		} else {
			if(list == null || list.size() < 1) {
				if(categoryRunnable != null) {
					categoryRunnable.isStop();
					categoryRunnable = null;
				} 
				String language = SharePreferenceHelper.getSharepreferenceString(SendCategoryChildActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
				categoryRunnable = new CategoryRunnable(handler, entity.getId()+"", language);
				showDialog(DIALOG_LOAD);
				new Thread(categoryRunnable).start();
			}
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1) {
				if(msg.obj != null) {
					list = (ArrayList<CategoryEntity>) msg.obj;
					if(list == null || list.size() < 1) {
						labNotInfo.setVisibility(View.VISIBLE);
						listView.setVisibility(View.GONE);
					} else {
						labNotInfo.setVisibility(View.GONE);
						listView.setVisibility(View.VISIBLE);
					}
					if(categoryListAdapter == null) {
						categoryListAdapter = new CategoryListAdapter(SendCategoryChildActivity.this, list);
						listView.setAdapter(categoryListAdapter);
					} else {
						categoryListAdapter.notifyDataSetChanged();
					}
				}
			} else if(msg.what == -1) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				Toast.makeText(SendCategoryChildActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			} else {
				// what == 0; 
				toSend(entity);
			}
		};
	};
}
