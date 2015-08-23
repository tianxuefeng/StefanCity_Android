package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.thread.UserListRunnable;
import com.stefan.city.module.thread.UserTypeRunnable;
import com.stefan.city.ui.adapter.UserListAdapter;

/**
 * UserManageActivity
 * @author 日期：2015-4-13上午11:30:05
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserManageActivity extends BaseActivity {
	
	private TextView labNotInfo;
	private Button btnBack;
	private ListView listView;
	private UserListAdapter userListAdapter;
	private List<UserEntity> list;
	
	private UserListRunnable userListRunnable;
	
	private boolean isIntent = false;
	
	private int curIndex = -1;
	
	private UserTypeRunnable userTypeRunnable;
	
	private int tempType = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		curIndex = -1;
		setContentView(R.layout.activity_user_list);
		initLayout();
	}
	
	private void initLayout() {
		btnBack = (Button) findViewById(R.id.btn_manage_user_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserManageActivity.this.finish();
			}
		});
		
		labNotInfo = (TextView) findViewById(R.id.lab_manage_user_notinfo);
		
		listView = (ListView) findViewById(R.id.listView_manage_user);
		listView.setOnItemClickListener(itemClickListener);
		listView.setOnItemLongClickListener(itemLongClickListener);
		
		userListAdapter = new UserListAdapter(UserManageActivity.this);
		listView.setAdapter(userListAdapter);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			curIndex = arg2; 
			UserEntity entity = list.get(arg2);
			Intent intent = new Intent(UserManageActivity.this, UserDetailActivity.class);
			intent.putExtra("userId", entity.getId());
			startActivity(intent);
			
		}
	};
	
	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			curIndex = position; 
			UserEntity entity = list.get(position);
			if(entity.getId().equals((Contant.curUser.getId()))) {
				Toast.makeText(UserManageActivity.this, R.string.msg_cur_user_nonmaskable, Toast.LENGTH_SHORT).show();
			} else {
				// 判断权限
				if(Contant.curUser != null && Contant.curUser.getMemType() > entity.getMemType()) {
					bockUser(entity);
				} else {
					// 级别不足
					Toast.makeText(UserManageActivity.this, R.string.msg_operate_authority_lacking, Toast.LENGTH_SHORT).show();
				}
			}
			return false;
		}
	};
	
	private void bockUser(UserEntity entity) {
		if(entity.getMemType() > 0) {
			String msg = getString(R.string.msg_user_bock);
			msg = String.format(msg, entity.getName());
			tempType = 0;
			// 屏蔽用户信息
			new AlertDialog.Builder(UserManageActivity.this).setTitle(R.string.title_message)
			.setMessage(msg)
			.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(curIndex > -1) {
						UserEntity entity = list.get(curIndex);
						// 执行屏蔽操作
						if(userTypeRunnable != null) {
							userTypeRunnable.isStop();
							userTypeRunnable = null;
						}
						showDialog(DIALOG_LOAD);
						userTypeRunnable = new UserTypeRunnable(bockHandler, entity.getId(), tempType);
						new Thread(userTypeRunnable).start();
					}
				}
			}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {  
					dialog.dismiss();  
				}  
			}).create().show();
		} else {
			String msg = getString(R.string.msg_user_unbock);
			msg = String.format(msg, entity.getName());
			tempType = 1;
			// 屏蔽用户信息
			new AlertDialog.Builder(UserManageActivity.this).setTitle(R.string.title_message)
			.setMessage(msg)
			.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(curIndex > -1) {
						UserEntity entity = list.get(curIndex);
						// 执行屏蔽操作
						if(userTypeRunnable != null) {
							userTypeRunnable.isStop();
							userTypeRunnable = null;
						}
						showDialog(DIALOG_LOAD);
						userTypeRunnable = new UserTypeRunnable(bockHandler, entity.getId(), tempType);
						new Thread(userTypeRunnable).start();
					}
				}
			}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int which) {  
					dialog.dismiss();  
				}  
			}).create().show();
		}
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1 && msg.obj != null) {
				list = (ArrayList<UserEntity>) msg.obj;
				if(list == null || list.size() < 1) {
					labNotInfo.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				} else {
					labNotInfo.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
				userListAdapter.setList(list);
				userListAdapter.notifyDataSetChanged();
			} else if(msg.what == 0) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			} else {
				Toast.makeText(UserManageActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private Handler bockHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1) {
				UserEntity entity = list.get(curIndex);
				entity.setMemType(tempType);
				userListAdapter.notifyDataSetChanged();
			} else if(msg.what == 0) {
				Toast.makeText(UserManageActivity.this, R.string.msg_operate_error, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(UserManageActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		isIntent = true;
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			curIndex = -1;
			loadData();
		}
	}
	
	private void loadData() {
		if(Contant.curUser != null && Contant.curUser.getCity() != null) {
			showDialog(DIALOG_LOAD);
			if(userListRunnable != null) {
				userListRunnable.isStop();
				userListRunnable = null;
			}
			userListRunnable = new UserListRunnable(handler, Contant.curUser.getCity());
			new Thread(userListRunnable).start();
		}
	}
	
}
