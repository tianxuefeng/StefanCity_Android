package com.stefan.city;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.SettingEntity;
import com.stefan.city.module.tools.SettingHandler;
import com.stefan.city.ui.adapter.SettingAdapter;

/**
 * SettingActivity
 * @author 日期：2014-7-23下午03:33:11
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class SettingActivity extends BaseActivity {
	
	private ListView listViewTop;
	private SettingAdapter topAdapter;
	private Button btnBack;
	
	private SettingHandler settingHandler;
	
	private boolean isIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		isIntent = true;
		settingHandler = new SettingHandler(SettingActivity.this);
		initLayout();
	}
	
	private void initLayout() {
		List<SettingEntity> entities = settingHandler.getEntities();
		btnBack = (Button) findViewById(R.id.btn_setting_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this, MainActivity.class);
				startActivity(intent);
				SettingActivity.this.finish();
			}
		});
		listViewTop = (ListView) findViewById(R.id.settingList_Top);
		topAdapter = new SettingAdapter(SettingActivity.this, entities.subList(0, 5));
		listViewTop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				settingHandler.settingItem(position);
				topAdapter.notifyDataSetChanged();
			}
		});
		listViewTop.setAdapter(topAdapter);
	}
	
	@Override
	public void setIntent(Intent newIntent) {
		super.setIntent(newIntent);
		setIntent(newIntent);
		isIntent = true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			int viewIndex = 0;
			Bundle bundle = getIntent().getExtras();
    		if(bundle != null) {
    			viewIndex = bundle.getInt("viewIndex", Contant.VIEW_MAIN);
    		}
    		settingHandler.setCurIndex(viewIndex);
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		Intent intent = new Intent(SettingActivity.this, MainActivity.class);
			startActivity(intent);
			SettingActivity.this.finish();
    	}
    	return super.onKeyDown(keyCode, event);
	}
	
	public void showProgress() {
		showDialog(SettingActivity.DIALOG_LOAD);
	}
	
	public void dismissProgress() {
		if(isProgress()) {
			dismissDialog(DIALOG_LOAD);
		}
	}
}
