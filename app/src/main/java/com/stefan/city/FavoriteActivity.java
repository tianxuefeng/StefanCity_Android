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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.FavoriteEntity;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.thread.FavoriteClearRunnable;
import com.stefan.city.module.thread.FavoriteManageRunnable;
import com.stefan.city.module.thread.FavoriteRunnable;
import com.stefan.city.ui.adapter.FavoriteAdapter;

/**
 * FavoriteActivity
 * 用户收藏信息管理
 * @author 日期：2015-3-9下午09:29:20
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class FavoriteActivity extends BaseActivity {
	
	private Button btnBack, btnDelete;
	
	private TextView labNotInfo;
	
	private FavoriteRunnable favoriteRunnable;
	private List<FavoriteEntity> list;
	
	private ListView listView;
	private FavoriteAdapter mainInfoAdapter;
	
//	private FavoriteService favoriteService;
	private int curIndex;
	private boolean isUpdate;
	
	private FavoriteManageRunnable favoriteManageRunnable;
	
	private FavoriteClearRunnable favoriteClearRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
//		favoriteService = new FavoriteService();
		initLayout();
	}
	
	private void initLayout() {
		listView = (ListView) findViewById(R.id.favorite_listView);
		listView.setOnItemClickListener(itemClickListener);
		listView.setOnItemLongClickListener(itemLongClickListener);
		btnBack = (Button) findViewById(R.id.btn_favorite_back);
		btnBack.setOnClickListener(clickListener);
		
		btnDelete = (Button) findViewById(R.id.btn_favorite_clear);
		btnDelete.setOnClickListener(clickListener);
		
		labNotInfo = (TextView) findViewById(R.id.lab_favorite_notinfo);
		labNotInfo.setOnClickListener(clickListener);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			InfoItemEntity entity = list.get(index);
			Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
			intent.putExtra("entity", entity);
			startActivity(intent);
		}
	};
	
	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			curIndex = arg2;
			toDelete();
			return true;
		}
	};
	
	private void toDelete() {
		if(curIndex < 0) {
			return ;
		}
		/*
		 * 是否确定删除
		 */
		new AlertDialog.Builder(FavoriteActivity.this).setTitle(R.string.title_delete)
        .setMessage(R.string.msg_delete)
        .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDialog(DIALOG_LOAD);
				FavoriteEntity entity = list.get(curIndex);
				if(favoriteManageRunnable != null) {
					favoriteManageRunnable.isStop();
					favoriteManageRunnable = null;
				}
				favoriteManageRunnable = new FavoriteManageRunnable(manHandler, Contant.curUser.getId(), null,
						entity.getFavoriteId(), FavoriteManageRunnable.STATUS_DEL);
				new Thread(favoriteManageRunnable).start();
			}
		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        }).create().show();
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_favorite_back:
				FavoriteActivity.this.finish();
				break;

			case R.id.lab_favorite_notinfo:
				// 尚无数据，点击重试
				labNotInfo.setVisibility(View.GONE);
				loadData();
				break;
				
			case R.id.btn_favorite_clear:
				toClear();
				break;
				
			default:
				break;
			}
		}
	};
	
	private void toClear() {
		if(Contant.curUser == null) {
			return ;
		}
		/*
		 * 是否确定删除
		 */
		new AlertDialog.Builder(FavoriteActivity.this).setTitle(R.string.title_clear)
        .setMessage(R.string.msg_clear)
        .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDialog(DIALOG_LOAD);
				if(favoriteClearRunnable != null) {
					favoriteClearRunnable.isStop();
					favoriteClearRunnable = null;
				}
				favoriteClearRunnable = new FavoriteClearRunnable(clearHandler, Contant.curUser.getId());
				new Thread(favoriteClearRunnable).start();
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
				if(msg.what == 0) {
					Toast.makeText(FavoriteActivity.this, R.string.msg_operate_error, Toast.LENGTH_SHORT).show();
					return ;
				}
				// 删除操作
				list.remove(curIndex);
				mainInfoAdapter.notifyDataSetChanged();
			} else {
				// 检查网络连接
				Toast.makeText(FavoriteActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private Handler clearHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what >= 0) {
				list.clear();
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				mainInfoAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(FavoriteActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}
	
	private void loadData() {
		if(isUpdate || list == null || list.size() < 1) {
			if(favoriteRunnable != null) {
				favoriteRunnable.isStop();
				favoriteRunnable = null;
			}
			if(Contant.curUser != null) {
				favoriteRunnable = new FavoriteRunnable(handler, Contant.curUser.getId());
				showDialog(DIALOG_LOAD);
				new Thread(favoriteRunnable).start();
			}
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.obj != null) {
				list = (ArrayList<FavoriteEntity>) msg.obj;
				if(list == null || list.size() < 1) {
					labNotInfo.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				} else {
					labNotInfo.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
				if(mainInfoAdapter == null) {
					mainInfoAdapter = new FavoriteAdapter(FavoriteActivity.this);
					mainInfoAdapter.setList(list);
					listView.setAdapter(mainInfoAdapter);
				} else {
					mainInfoAdapter.notifyDataSetChanged();
				}
			} else if(msg.what == -1) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				Toast.makeText(FavoriteActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			} else if(msg.obj == null && (list == null || list.size() < 1)) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		};
	};
}
