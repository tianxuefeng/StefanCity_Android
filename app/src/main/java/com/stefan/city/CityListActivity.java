package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.thread.RegionManageRunnable;
import com.stefan.city.module.thread.RegionListRunnable;
import com.stefan.city.ui.adapter.RegionListAdapter;
import com.stefan.city.ui.dialog.DialogAddRegion;
import com.stefan.city.ui.dialog.DialogEditRegion;

/**
 * CityListActivity
 * 		切换当前城市信息，提供编辑的功能。实体也是region
 * @author 日期：2015-1-13下午09:32:11
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CityListActivity extends BaseActivity {

	private DialogAddRegion dialogAddCity;
	
	private DialogEditRegion dialogEditCity;
	
	private Button btnBack, btnAdd;
	
	private TextView labNotInfo;
	
	private RegionListAdapter cityListAdapter;
	
	private ListView listView;
	private List<RegionManEntity> list;
	private RegionListRunnable cityListRunnable;
	
	private boolean isIntent;
	
	private RegionManageRunnable regionManageRunnable;
	
	private int curIndex = -1;
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private Class activityClass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		setContentView(R.layout.activity_city);
		initLayout();
	}
	
	private void initLayout() {
		btnBack = (Button) findViewById(R.id.btn_manage_city_back);
		btnAdd = (Button) findViewById(R.id.btn_manage_city_add);
		
		labNotInfo = (TextView) findViewById(R.id.lab_manage_city_notinfo);
		
		listView = (ListView) findViewById(R.id.listView_manage_city);
		listView.setOnItemLongClickListener(longClickListener);
		listView.setOnItemClickListener(itemClickListener);
		
		btnBack.setOnClickListener(clickListener);
		btnAdd.setOnClickListener(clickListener);
		
		labNotInfo.setOnClickListener(clickListener);
		
		dialogAddCity = new DialogAddRegion(CityListActivity.this, regionHandler);
		
		dialogEditCity = new DialogEditRegion(CityListActivity.this, regionHandler);
		
		if(Contant.curUser == null || Contant.curUser.getMemType() < 3) {
			if(btnAdd != null) {
				btnAdd.setVisibility(View.GONE);
			}
		} else {
			if(btnAdd != null) 
				btnAdd.setVisibility(View.VISIBLE);
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_manage_city_back:
				CityListActivity.this.finish();
				break;
				
			case R.id.btn_manage_city_add:
				dialogAddCity.show(null);
				break;
				
			case R.id.lab_manage_city_notinfo:
				// 尚无数据，点击重试
				labNotInfo.setVisibility(View.GONE);
				loadData();
				break;
				
			default:
				break;
			}
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			RegionManEntity entity = list.get(arg2);
			if(entity != null) {
				Contant.curLocationEntity.setAdministrative(entity.getName());
				gotoIntent();
			}
		}
	};
	
	/**
	 * 界面跳转
	 */
	private void gotoIntent() {
		if(activityClass != null) {
			Intent intent = new Intent(CityListActivity.this, activityClass);
			startActivity(intent);
		}
		CityListActivity.this.finish();
	}
	
	private Handler regionHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			String language = SharePreferenceHelper.getSharepreferenceString(CityListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
			RegionManEntity entity = (RegionManEntity) msg.obj;
			entity.setLanguageCode(language);
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			showDialog(DIALOG_LOAD);
			String infoMsg = "";
			int operateStatus = 0;
			switch (msg.what) {
			case 0:
				entity.setParentName(Contant.curLocationEntity.getCountry());
				infoMsg = getString(R.string.msg_add_success);
				operateStatus = RegionManageRunnable.STATUS_ADD;
				break;
				
			case 1:
				infoMsg = getString(R.string.msg_edit_success);
				operateStatus = RegionManageRunnable.STATUS_UPDATE;
				break;

			default:
				break;
			}
			Toast.makeText(CityListActivity.this, infoMsg, Toast.LENGTH_SHORT).show();
			if(regionManageRunnable != null) {
				regionManageRunnable.isStop();
				regionManageRunnable = null;
			}
			regionManageRunnable = new RegionManageRunnable(manHandler, entity, operateStatus);
			showDialog(DIALOG_LOAD);
			new Thread(regionManageRunnable).start();
		};
	};
	
	private OnItemLongClickListener longClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
//			RegionManEntity entity = list.get(arg2);
			if(Contant.curUser == null || Contant.curUser.getMemType() < 3) {
				return true;
			}
			curIndex = arg2;
			showDetailDialog();
			return true;
		}
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isIntent = true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(Contant.curUser == null || Contant.curUser.getMemType() < 3) {
			if(btnAdd != null) {
				btnAdd.setVisibility(View.GONE);
			}
		} else {
			if(btnAdd != null) 
				btnAdd.setVisibility(View.VISIBLE);
		}
		if(isIntent) {
			isIntent = false;
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				Object object = bundle.get("activityClass");
				if(object != null) {
					activityClass = (Class) object;
				} else {
					activityClass = MainActivity.class;
				}
			}
			loadData();
		}
	}
	
	private void loadData() {
		if(cityListRunnable != null) {
			cityListRunnable.isStop();
			cityListRunnable = null;
		}
		String language = SharePreferenceHelper.getSharepreferenceString(CityListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
		// 这里是以国家为单位来获取城市信息
		cityListRunnable = new RegionListRunnable(handler, Contant.curLocationEntity.getCountry(), language);
		
		showDialog(DIALOG_LOAD);
		new Thread(cityListRunnable).start();
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings({ "deprecation", "unchecked" })
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.obj != null) {
				list = (ArrayList<RegionManEntity>) msg.obj;
				if(list == null || list.size() < 1) {
					labNotInfo.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				} else {
					labNotInfo.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
				if(cityListAdapter == null) {
					cityListAdapter = new RegionListAdapter(CityListActivity.this, list);
					listView.setAdapter(cityListAdapter);
				} else {
					cityListAdapter.setList(list);
					cityListAdapter.notifyDataSetChanged();
				}
			} else if(msg.what == -1 || msg.what == 0 && (list == null || list.size() == 0)) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		};
	};
	
	/**
	 * 弹出操作对话框
	 * @param entity
	 */
	private void showDetailDialog() {
		String[] arrayList = getResources().getStringArray(R.array.array_list_city);
		final Builder builder = new AlertDialog.Builder(this);
		builder.setItems(
				arrayList,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							// 编辑
							toEdit();
							break;
							
						case 1:
							// 删除
							toDelete();
							break;
							
						default:
							break;
						}
					}
				});
		builder.create().show();
	}
	
	private void toEdit() {
		if(curIndex == -1) {
			return ;
		}
		RegionManEntity entity = list.get(curIndex);
		dialogEditCity.show(entity);
	}
	
	private void toDelete() {
		/*
		 * 是否确定删除
		 */
		new AlertDialog.Builder(CityListActivity.this).setTitle(R.string.title_delete)
        .setMessage(R.string.msg_delete)
        .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				RegionManEntity entity = list.get(curIndex);
				if(regionManageRunnable != null) {
					regionManageRunnable.isStop();
					regionManageRunnable = null;
				}
				regionManageRunnable = new RegionManageRunnable(manHandler, entity, RegionManageRunnable.STATUS_DEL);
				showDialog(DIALOG_LOAD);
				new Thread(regionManageRunnable).start();
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
			if(msg.what != -1 && msg.obj != null) {
				if(msg.what == 0) {
					Toast.makeText(CityListActivity.this, R.string.msg_operate_error, Toast.LENGTH_SHORT).show();
					return ;
				}
				int status = (Integer) msg.obj;
				if(status == RegionManageRunnable.STATUS_DEL) {
					// 删除操作
					RegionManEntity entity = list.get(curIndex);
					list.remove(entity);
					cityListAdapter.notifyDataSetChanged();
				} else if (status == RegionManageRunnable.STATUS_ADD || status == RegionManageRunnable.STATUS_UPDATE) {
					String language = SharePreferenceHelper.
										getSharepreferenceString(CityListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
					if(cityListRunnable != null) {
						cityListRunnable.isStop();
						cityListRunnable = null;
					}
					cityListRunnable = new RegionListRunnable(handler, Contant.curLocationEntity.getCountry(), language);
					new Thread(cityListRunnable).start();
				}
			} else {
				// 检查网络连接
				Toast.makeText(CityListActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			CityListActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
