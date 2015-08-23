package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rockeagle.framework.core.REActivity;
import com.rockeagle.framework.core.REActivityManager;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.entity.StoreEntity;
import com.stefan.city.module.service.HistoryService;
import com.stefan.city.module.thread.HistoryRunnable;
import com.stefan.city.module.thread.ItemDetailRunnable;
import com.stefan.city.ui.adapter.StoreAdpater;

/**
 * HistoryActivity
 * 历史浏览记录
 * @author 日期：2015-3-9下午08:06:47
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class HistoryActivity extends BaseActivity {
	
	private HistoryRunnable historyRunnable;
	
	private TextView labNotInfo;

	private Button btnBack, btnDeletes;
	
	private List<StoreEntity> list;
	private ListView listView;
	private StoreAdpater storeAdpater;
	
	private boolean isIntent = false;
	
	private ItemDetailRunnable itemDetailRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		setContentView(R.layout.activity_history);
		initLayout();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		isIntent = true;
		setIntent(intent);
	}
	
	private void initLayout() {
		btnBack = (Button) findViewById(R.id.btn_history_back);
		btnBack.setOnClickListener(clickListener);
		
		btnDeletes = (Button) findViewById(R.id.btn_history_deletes);
		btnDeletes.setOnClickListener(clickListener);
		
		listView = (ListView) findViewById(R.id.history_listView);
		listView.setOnItemClickListener(itemClickListener);
		listView.setOnItemLongClickListener(itemLongClickListener);
		
		labNotInfo = (TextView) findViewById(R.id.lab_history_notinfo);
		labNotInfo.setOnClickListener(clickListener);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_history_back:
				HistoryActivity.this.finish();
				break;
				
			case R.id.btn_history_deletes:
				clear();
				break;
				
			case R.id.lab_history_notinfo:
				// 尚无数据，点击重试
				labNotInfo.setVisibility(View.GONE);
				loadData();
				break;
				
			default:
				break;
			}
		}
	};
	
	private void clear() {
		new AlertDialog.Builder(HistoryActivity.this).setTitle(R.string.title_clear)
        .setMessage(R.string.msg_clear)
        .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				HistoryService historyService = new HistoryService(HistoryActivity.this);
				historyService.deletes();
				list.clear();
				storeAdpater.notifyDataSetChanged();
			}
		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }
        }).create().show();
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			StoreEntity entity = list.get(arg2);
			if(entity != null) {
				showDialog(DIALOG_LOAD);
				if(itemDetailRunnable != null) {
					itemDetailRunnable.isStop();
					itemDetailRunnable = null;
				}
				itemDetailRunnable = new ItemDetailRunnable(detailHandler, entity.getItemId());
				new Thread(itemDetailRunnable).start();
			}
		}
	};
	
	private Handler detailHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what != -1) {
				if(msg.obj != null) {
					InfoItemEntity entity = (InfoItemEntity) msg.obj;
					toDetail(entity);
				} else {
					Toast.makeText(HistoryActivity.this, R.string.msg_history_info_error, Toast.LENGTH_SHORT).show();
				}
			} else {
				// 网络不通
				Toast.makeText(HistoryActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			return false;
		}
	};
	
	/**
	 * 查看详细信息
	 * @param entity
	 */
	private void toDetail(InfoItemEntity entity) {
		Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
		intent.putExtra("entity", entity);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			HistoryActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			loadData();
		}
	};
	
	@SuppressWarnings("deprecation")
	private void loadData() {
		showDialog(DIALOG_LOAD);
		if(historyRunnable != null) {
			historyRunnable.isStop();
			historyRunnable = null;
		}
		historyRunnable = new HistoryRunnable(handler, HistoryActivity.this);
		new Thread(historyRunnable).start();
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.obj != null) {
				if(list != null && list.size() > 0) {
					list.clear();
				} else {
					list = new ArrayList<StoreEntity>();
				}
				list.addAll((ArrayList<StoreEntity>) msg.obj);
				if(list == null || list.size() < 1) {
					labNotInfo.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				} else {
					labNotInfo.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
				if(storeAdpater == null) {
					storeAdpater = new StoreAdpater(HistoryActivity.this, list);
					listView.setAdapter(storeAdpater);
				} else {
					storeAdpater.notifyDataSetChanged();
				}
			} else {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		};
	};
}
