package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.rockeagle.framework.core.REListActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.thread.ItemsByUserRunnable;
import com.stefan.city.module.thread.ItemsDeleteRunnable;
import com.stefan.city.ui.adapter.MainInfoAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * UserSendListActivity
 * 用户所发布的信息
 * @author 日期：2015-3-9下午06:08:20
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserSendListActivity extends REListActivity {
	
	private TextView labTitle, labNotInfo;
	private ListView listView;
	
	private Button btnBack, btnSend;
	
	private List<InfoItemEntity> list;
	private MainInfoAdapter infoAdapter;

	private boolean isLoad;
	
	private ItemsByUserRunnable byUserRunnable;
	
	private int curIndex = -1;
	private ItemsDeleteRunnable itemsDeleteRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_send);
		
		initLayout();
	}
	
	private void initLayout() {
		labTitle = (TextView) findViewById(R.id.lab_userSend_title);
		btnBack = (Button) findViewById(R.id.btn_userSend_back);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserSendListActivity.this, MainActivity.class);
				intent.putExtra("viewIndex", Contant.VIEW_USER);
				startActivity(intent);
				UserSendListActivity.this.finish();
			}
		});
		
		btnSend = (Button) findViewById(R.id.btn_userSend);
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserSendListActivity.this, SendActivity.class);
				startActivity(intent);
				UserSendListActivity.this.finish();
			}
		});
		
		listView = (ListView) findViewById(R.id.userSend_listView);
		infoAdapter = new MainInfoAdapter(UserSendListActivity.this);
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InfoItemEntity entity = list.get(arg2);
				Intent intent = new Intent(UserSendListActivity.this, DetailActivity.class);
				intent.putExtra("entity", entity);
				startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(itemLongClickListener);
		labNotInfo = (TextView) findViewById(R.id.lab_userlist_notinfo);
		labNotInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 尚无数据，点击重试
				labNotInfo.setVisibility(View.GONE);
				loadData();
			}
		});
	}
	
	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			curIndex = arg2;
//			InfoItemEntity entity = list.get(arg2);
//			if(entity != null) {
////				itemsDeleteRunnable
//			}
			toDelete();
			return false;
		}
	};
	
	private void toDelete() {
		/*
		 * 是否确定删除
		 */
		new AlertDialog.Builder(UserSendListActivity.this).setTitle(R.string.title_delete)
        .setMessage(R.string.msg_delete)
        .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				InfoItemEntity entity = list.get(curIndex);
				if(itemsDeleteRunnable != null) {
					itemsDeleteRunnable.isStop();
					itemsDeleteRunnable = null;
				}
				itemsDeleteRunnable = new ItemsDeleteRunnable(manHandler, entity.getId()+"");
				showDialog(DIALOG_LOAD);
				new Thread(itemsDeleteRunnable).start();
			}
		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }
        }).create().show();
	}
	
	private Handler manHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what != -1) {
				if(msg.what == 1 && curIndex != -1) {
					list.remove(curIndex);
					infoAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(UserSendListActivity.this, R.string.msg_operate_error, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(UserSendListActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(this);
		loadData();
	}
	
	@Override
	protected void initVal() {
		
	}

	@Override
	protected void loadData() {
		if(list == null || list.size() < 1) {
			if(Contant.curUser != null) {
				if(byUserRunnable != null) {
					byUserRunnable.isStop();
					byUserRunnable = null;
				}
				byUserRunnable = new ItemsByUserRunnable(handler, Contant.curUser.getId());
				showDialog(DIALOG_LOAD);
				new Thread(byUserRunnable).start();
			} else {
				// 跳转到用户登陆界面
				Intent intent = new Intent(UserSendListActivity.this, LoginActivity.class);
				intent.putExtra("viewIndex", Contant.VIEW_USER);
				startActivity(intent);
				UserSendListActivity.this.finish();
			}
		}
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.obj != null) {
				list = (ArrayList<InfoItemEntity>) msg.obj;
				if(list == null || list.size() < 1) {
					labNotInfo.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				} else {
					labNotInfo.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
				infoAdapter.setList(list);
				infoAdapter.notifyDataSetChanged();
			} else if(msg.what == -1) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				Toast.makeText(UserSendListActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0 && (list == null || list.size() == 0)) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		};
	};

	@Override
	protected void pageLoadData() {
		
	}

}
