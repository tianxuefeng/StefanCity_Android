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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.thread.RegionListRunnable;
import com.stefan.city.module.thread.RegionManageRunnable;
import com.stefan.city.ui.adapter.RegionListAdapter;
import com.stefan.city.ui.dialog.DialogAddRegion;
import com.stefan.city.ui.dialog.DialogEditRegion;

/**
 * RegionlISTActivity
 * 地区列表，用于选择当前城市下其他地区，可以新增，但是不可用修改和删除
 * @author 日期：2014-11-5下午09:08:54
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegionListActivity extends BaseActivity {
	
	private DialogAddRegion dialogAddRegion;
	
	private DialogEditRegion dialogEditRegion;
	
	private Button btnBack, btnAdd, btnCutover, btnSelStreet;
	
	private TextView labNotInfo;
	
	private RegionListAdapter regionListAdapter;
	
	private ListView listView;
	private List<RegionManEntity> list;
	private RegionListRunnable regionListRunnable;
	
	private boolean isIntent;
	
	private RegionManageRunnable regionManageRunnable;
	
	private int curIndex = -1;
	
	@SuppressWarnings("rawtypes")
	private Class activityClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		setContentView(R.layout.activity_region);
		initLayout();
	}
	
	private void initLayout() {
		btnBack = (Button) findViewById(R.id.btn_manage_region_back);
		btnAdd = (Button) findViewById(R.id.btn_manage_region_add);
		
		btnCutover = (Button) findViewById(R.id.btn_region_other_city);
		
		btnSelStreet = (Button) findViewById(R.id.btn_manage_other_street);
		
		labNotInfo = (TextView) findViewById(R.id.lab_manage_region_notinfo);
		
		listView = (ListView) findViewById(R.id.listView_manage_region);
		listView.setOnItemLongClickListener(longClickListener);
		listView.setOnItemClickListener(itemClickListener);
		
		btnBack.setOnClickListener(clickListener);
		btnAdd.setOnClickListener(clickListener);
		btnCutover.setOnClickListener(clickListener);
		btnSelStreet.setOnClickListener(clickListener);
		btnSelStreet.setVisibility(View.GONE);
		
		labNotInfo.setOnClickListener(clickListener);
		
		dialogAddRegion = new DialogAddRegion(RegionListActivity.this, regionHandler);
		
		dialogEditRegion = new DialogEditRegion(RegionListActivity.this, regionHandler);
		
		if(Contant.curUser == null || Contant.curUser.getMemType() < 3) {
			btnAdd.setVisibility(View.GONE);
		} else {
			btnAdd.setVisibility(View.VISIBLE);
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_manage_region_back:
				RegionListActivity.this.finish();
				break;
				
			case R.id.btn_manage_region_add:
				dialogAddRegion.show(null);
				break;
				
			case R.id.lab_manage_region_notinfo:
				// 尚无数据，点击重试
				labNotInfo.setVisibility(View.GONE);
				loadData();
				break;
				
			case R.id.btn_manage_other_street:{
				// 切换街道信息
				Intent intent = new Intent(RegionListActivity.this, RegionStreetActivity.class);
//				intent.putExtra("activityClass", RegionListActivity.class);
				startActivity(intent);
				RegionListActivity.this.finish();
			}
				break;
				
			case R.id.btn_region_other_city: 
				// 切换城市
				Intent intent = new Intent(RegionListActivity.this, CityListActivity.class);
				intent.putExtra("activityClass", RegionListActivity.class);
				startActivity(intent);
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
				Contant.curRegionEntity = entity;
//				Contant.curLocationEntity.setLocality(entity.getName());
				// 切换地区后，需要进入到切换街道界面中
//				Intent intent = new Intent(RegionListActivity.this, RegionStreetActivity.class);
//				startActivity(intent);
				RegionListActivity.this.finish();
			}
		}
	};
	
	private Handler regionHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			String language = SharePreferenceHelper.getSharepreferenceString(RegionListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
			RegionManEntity entity = (RegionManEntity) msg.obj;
			entity.setLanguageCode(language);
			showDialog(DIALOG_LOAD);
			String infoMsg = "";
			int operateStatus = 0;
			switch (msg.what) {
			case 0:
				entity.setParentName(Contant.curLocationEntity.getAdministrative());
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
			Toast.makeText(RegionListActivity.this, infoMsg, Toast.LENGTH_SHORT).show();
			if(regionManageRunnable != null) {
				regionManageRunnable.isStop();
				regionManageRunnable = null;
			}
			regionManageRunnable = new RegionManageRunnable(manHandler, entity, operateStatus);
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
	
	private Handler manHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what != -1 && msg.obj != null) {
				if(msg.what == 0) {
					Toast.makeText(RegionListActivity.this, R.string.msg_operate_error, Toast.LENGTH_SHORT).show();
					return ;
				}
				int status = (Integer) msg.obj;
				if(status == RegionManageRunnable.STATUS_DEL) {
					// 删除操作
					RegionManEntity entity = list.get(curIndex);
					list.remove(entity);
					regionListAdapter.notifyDataSetChanged();
				} else if (status == RegionManageRunnable.STATUS_ADD || status == RegionManageRunnable.STATUS_UPDATE) {
					String language = SharePreferenceHelper.
										getSharepreferenceString(RegionListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
					if(regionListRunnable != null) {
						regionListRunnable.isStop();
						regionListRunnable = null;
					}
					regionListRunnable = new RegionListRunnable(handler, Contant.curLocationEntity.getAdministrative(), language);
					new Thread(regionListRunnable).start();
				}
			} else {
				// 检查网络连接
				Toast.makeText(RegionListActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
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
				if(activityClass == GPSLocationActivity.class) {
					btnCutover.setVisibility(View.GONE);
				} else {
					btnCutover.setVisibility(View.VISIBLE);
				}
			}
			loadData();
		}
	}
	
	private void loadData() {
		if(regionListRunnable != null) {
			regionListRunnable.isStop();
			regionListRunnable = null;
		}
		String language = SharePreferenceHelper.getSharepreferenceString(RegionListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
		regionListRunnable = new RegionListRunnable(handler, Contant.curLocationEntity.getAdministrative(), language);
		showDialog(DIALOG_LOAD);
		
		new Thread(regionListRunnable).start();
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings({ "deprecation", "unchecked" })
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.obj != null) {
				list = (ArrayList<RegionManEntity>) msg.obj;
				Contant.regionManEntities = new ArrayList<RegionManEntity>(list);
				if(list == null || list.size() < 1) {
					labNotInfo.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				} else {
					labNotInfo.setVisibility(View.GONE);
					listView.setVisibility(View.VISIBLE);
				}
				if(regionListAdapter == null) {
					regionListAdapter = new RegionListAdapter(RegionListActivity.this, list);
					listView.setAdapter(regionListAdapter);
				} else {
					regionListAdapter.setList(list);
					regionListAdapter.notifyDataSetChanged();
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
		String[] arrayList = getResources().getStringArray(R.array.array_list_region);
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
							
						case 2:
							// 进入子区域界面
							toSubregion();
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
		dialogEditRegion.show(entity);
	}
	
	private void toSubregion() {
		if(curIndex == -1) {
			return ;
		}
		RegionManEntity entity = list.get(curIndex);
		Intent intent = new Intent(RegionListActivity.this, RegionStreetActivity.class);
		intent.putExtra("curRegion", entity);
		startActivity(intent);
	}
	
	private void toDelete() {
		/*
		 * 是否确定删除
		 */
		new AlertDialog.Builder(RegionListActivity.this).setTitle(R.string.title_delete)
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			RegionListActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
