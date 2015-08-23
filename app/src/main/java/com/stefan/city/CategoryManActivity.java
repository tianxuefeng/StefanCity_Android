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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.thread.CategoryDeleteRunnable;
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
public class CategoryManActivity extends BaseActivity {
	
	private ExpandableListView listView;
	private SortTreeAdapter sortTreeAdapter;
	
	private Button btnBack, btnAdd;
	
	private List<CategoryEntity> groups;
	private CategoryEntity[][] childs;
	private CategorySendRunnable categorySendRunnable;
	
	private int groupIndex, childIndex;
	private boolean isChilds;
	
	private CategoryEntity curEntity;
	
//	private CategoryService categoryService;
	private CategoryDeleteRunnable categoryDeleteRunnable;
	
	private boolean isUpdate, isIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.acitivity_sendsort);
		
		btnBack = (Button) findViewById(R.id.btn_sort_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 返回
				gotoMain();
			}
		});
		listView = (ExpandableListView) findViewById(R.id.sort_treeListView);
//		listView.setOnGroupClickListener(groupClickListener);
//		listView.setOnChildClickListener(childClickListener);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View arg1,
					int arg2, long arg3) {
				int index = (Integer) arg1.getTag();
				isChilds = false;
				groupIndex = index;
				childIndex = 0;
				curEntity = groups.get(index);
				showDetailDialog();
				return true;
			}
		});
		
		btnAdd = (Button) findViewById(R.id.btn_man_category_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CategoryManActivity.this, CategoryAddActivity.class);
				startActivity(intent);
//				CategoryManActivity.this.finish();
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isIntent = true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				isUpdate = bundle.getBoolean("isUpdate", false);
			}
		}
		loadData();
	}
	
	private void loadData() {
		if(isUpdate || groups == null || groups.size() < 1) {
			if(categorySendRunnable != null) {
				categorySendRunnable.isStop();
				categorySendRunnable = null;
			}
			String language = SharePreferenceHelper.getSharepreferenceString(CategoryManActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "zh");
			categorySendRunnable = new CategorySendRunnable(CategoryManActivity.this, language, handler);
			
			showDialog(DIALOG_LOAD);
			new Thread(categorySendRunnable).start();
		}
	}
	
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1 && msg.obj != null) {
				Object[] objects = (Object[]) msg.obj;
				if(groups != null) {
					groups.clear();
					groups.addAll((ArrayList<CategoryEntity>) objects[0]);
				} else {
					groups = (ArrayList<CategoryEntity>) objects[0];
				}
				childs = (CategoryEntity[][]) objects[1];
				sortTreeAdapter = null;
				if(sortTreeAdapter == null) {
					sortTreeAdapter = new SortTreeAdapter(CategoryManActivity.this, groups, childs, itemClickListener);
					listView.setAdapter(sortTreeAdapter);
				} /*else {
					sortTreeAdapter.notifyDataSetChanged();
				}*/
			} else if (msg.what == 5) {
				// 删除操作
				if(isChilds) {
					childs[groupIndex][childIndex] = null;
					CategoryEntity[] temps = new CategoryEntity[childs[groupIndex].length-1];
					
					System.arraycopy(childs[groupIndex], 0, temps, 0, childIndex);
					System.arraycopy(childs[groupIndex], childIndex+1, temps, childIndex, temps.length-childIndex);
					
					childs[groupIndex] = temps;
				} else {
					if(childs[groupIndex] != null && childs[groupIndex].length > 0) {
						Toast.makeText(CategoryManActivity.this, R.string.msg_category_delete_error, Toast.LENGTH_SHORT).show();
						return ;
					}
					groups.remove(groupIndex);
				}
				sortTreeAdapter.notifyDataSetChanged();
			} else if (msg.what == -1) {
				Toast.makeText(CategoryManActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private OnItemLongClickListener itemClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			int index = (Integer) arg0.getTag();
			isChilds = true;
			groupIndex = index;
			childIndex = position;
			curEntity = childs[index][position];
			showDetailDialog();
//			toSend(infoEntity);
			return true;
		}
	};
	
	/**
	 * 弹出操作对话框
	 * @param entity
	 */
	private void showDetailDialog() {
		String[] arrayList = null;
		if(isChilds) {
			arrayList = getResources().getStringArray(R.array.array_list_category2);
		} else {
			arrayList = getResources().getStringArray(R.array.array_list_category);
		}
		final Builder builder = new AlertDialog.Builder(this);
		builder.setItems(
				arrayList,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if(isChilds) {
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
						} else {
							switch (which) {
							case 0:
								// 在该分类下新增子分类
								toAdd();
								break;
								
							case 1:
								// 编辑
								toEdit();
								break;
								
							case 2:
								// 删除
								toDelete();
								break;
								
							default:
								break;
							}
						}
					}
				});
		builder.create().show();
	}
	
	private void toAdd() {
		Intent intent = new Intent(CategoryManActivity.this, CategoryAddActivity.class);
		intent.putExtra("parentCatetory", curEntity);
		startActivity(intent);
//		CategoryManActivity.this.finish();
	}
	
	private void toEdit() {
		Intent intent = new Intent(CategoryManActivity.this, CategoryEditActivity.class);
		intent.putExtra("parentCatetory", curEntity);
		startActivity(intent);
//		CategoryManActivity.this.finish();
//		CategoryEntity entity = list.get(curIndex);
//		dialogEditCategory.show(entity);
	}
	
	/**
	 * 回到主界面，因为需要重新加载分类信息，所以不能直接退出
	 */
	private void gotoMain() {
		Intent intent = new Intent(CategoryManActivity.this, MainActivity.class);
		startActivity(intent);
		CategoryManActivity.this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			gotoMain();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 删除分类信息
	 */
	private void toDelete() {
		if(curEntity == null) {
			return ;
		}
		/*
		 * 是否确定删除
		 */
		new AlertDialog.Builder(CategoryManActivity.this).setTitle(R.string.title_delete)
        .setMessage(R.string.msg_delete)
        .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDialog(DIALOG_LOAD);
				if(categoryDeleteRunnable != null) {
					categoryDeleteRunnable.isStop();
					categoryDeleteRunnable = null;
				}
				categoryDeleteRunnable = new CategoryDeleteRunnable(handler, curEntity.getId());
				new Thread(categoryDeleteRunnable).start();
			}
		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
            }  
        }).create().show();
	}
}
