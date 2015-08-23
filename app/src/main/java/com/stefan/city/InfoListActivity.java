package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rockeagle.framework.core.REListActivity;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.thread.CategoryRunnable;
import com.stefan.city.module.thread.ItemsGetRunnable;
import com.stefan.city.module.thread.RegionTwoListRunnable;
import com.stefan.city.ui.adapter.MainInfoAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * SortActivity
 * 分类信息
 * @author 日期：2014-6-27下午08:21:03
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class InfoListActivity extends REListActivity {
	
	private TextView labTitle, labNotInfo;
	private ListView listView;
	
	private Button btnBack, btnSend;
	
	private TextView labSelCategory, labSelAddress, labSelStreet, labSelType;
	
	private ItemsGetRunnable listInfoRunnable;
	private List<InfoItemEntity> list;
	
	private MainInfoAdapter infoAdapter;
	
	private CategoryEntity categoryEntity;
	
	private RegionManEntity regionManEntity;
	
//	private RegionListRunnable regionListRunnable;
	
	private RegionTwoListRunnable regionTwoListRunnable;
	private RegionManEntity[] manEntities;
	private String[] regionTwos;
	
	private boolean isLoad;
	
	protected View footviewLayout;	// listview的加载提示View
	// 点击加载更多信息
	private TextView labLoadMore;
	
	private CategoryRunnable categoryRunnable;
	
	/** 当前页数 **/
	private int curPage = 1;
	/** 最大数据量 **/
	private int totalSize;
	/** 最大页数  **/
	private int totalPage = 1;
	private int pageSize = 10;
	
	// 默认为1
	private String type = "1";
	// 最大页数
	protected boolean isInitLoad = false;
	
	private String[] regions;
	
	private String[] streets;
	
	private String[] types;
	
	private int curRegionIndex, curStreetIndex, curCategoryIndex, curTypeIndex;
	
	private List<CategoryEntity> categoryList;
	private String[] categorys;
	// 进来的大类ID
	private String parentId;
	
	private String parentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLoad = true;
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.acitivty_list);
		
		labTitle = (TextView) findViewById(R.id.info_list_title);
		btnBack = (Button) findViewById(R.id.info_list_btnaddress);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InfoListActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		btnSend = (Button) findViewById(R.id.info_list_btnsend);
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InfoListActivity.this, SendActivity.class);
				CategoryEntity categoryParentEntity = new CategoryEntity(categoryEntity);
				categoryParentEntity.setId(parentId);
				intent.putExtra("categoryParentEntity", categoryParentEntity);
				if(categoryList != null && categoryList.size() > 0) {
					CategoryEntity entity = null;
					if(curCategoryIndex < 0) {
						entity = new CategoryEntity();
						entity.setParentId("0");
						entity.setId(parentId);
						entity.setTitle(parentName);
					} else {
						entity = categoryList.get(curCategoryIndex);
					}
					intent.putExtra("categoryEntity", entity);
				}
				
				if(manEntities != null && manEntities.length >= (curRegionIndex+1)) {
					intent.putExtra("regionManEntity", manEntities[curRegionIndex]);
				}
				startActivity(intent);
//				InfoListActivity.this.finish();
			}
		});
		
		labNotInfo = (TextView) findViewById(R.id.lab_list_notinfo);
		labNotInfo.setOnClickListener(clickListener);
		
		listView = (ListView) findViewById(R.id.main_listView);
		infoAdapter = new MainInfoAdapter(InfoListActivity.this);
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InfoItemEntity entity = list.get(arg2);
				Intent intent = new Intent(InfoListActivity.this, DetailActivity.class);
				intent.putExtra("entity", entity);
				startActivity(intent);
			}
		});
		
		LayoutInflater inflater = LayoutInflater.from(InfoListActivity.this);
		View footView = inflater.inflate(R.layout.footview, null);
		footviewLayout = (View) footView.findViewById(R.id.recomBottomPreLayout);
		labLoadMore = (TextView) footView.findViewById(R.id.labLoadMore);
		labLoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				footviewLayout.setVisibility(View.VISIBLE);
				pageLoadData();
			}
		});
		
		labSelCategory = (TextView) findViewById(R.id.lab_list_sel_category);
		labSelCategory.setOnClickListener(clickListener);
		labSelAddress = (TextView) findViewById(R.id.lab_list_sel_address);
		labSelAddress.setOnClickListener(clickListener);
		
		labSelStreet = (TextView) findViewById(R.id.lab_list_sel_street);
		labSelStreet.setOnClickListener(clickListener);
		
		labSelType = (TextView) findViewById(R.id.lab_list_sel_type);
		labSelType.setOnClickListener(clickListener);
		
//		footviewLayout = (View) headView.findViewById(R.id.recomBottomPreLayout);
//		listView.addHeaderView(headView);
		listView.addFooterView(footView);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.lab_list_sel_category:
				showSelectCategory();
				break;

			case R.id.lab_list_sel_address:
				showSelectAddress();
				break;
				
			case R.id.lab_list_sel_street:
				showSelectStreet();
				break;
				
			case R.id.lab_list_sel_type:
				showSelectType();
				break;
				
			case R.id.lab_list_notinfo:
				// 尚无数据，点击重试
				labNotInfo.setVisibility(View.GONE);
				loadData();
				break;
				
			default:
				break;
			}
		}
	};
	
	/**
	 * 选择分类
	 */
	private void showSelectCategory() {
		final Builder builder = new AlertDialog.Builder(this);
		builder.setItems(categorys, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(categoryList != null) {
					if(categoryList.size() == 1 && categoryList.get(0).getTitle().equals(categoryEntity.getTitle())) {
						labSelCategory.setText(categoryEntity.getTitle());
						return ;
					}
				}
				if(categorys != null && categorys.length > which) {
					curCategoryIndex = which-1;
					if(which == 0) {
						categoryEntity.setParentId("0");
						categoryEntity.setId(parentId);
						categoryEntity.setTitle(categorys[which]);
					} else {
						CategoryEntity entity = categoryList.get(which-1);
						categoryEntity.setTitle(entity.getTitle());
						categoryEntity.setParentId(parentId);
						categoryEntity.setId(entity.getId());
					}
					labSelCategory.setText(categoryEntity.getTitle());
					loadData();
				}
			}
		});
		builder.create().show();
	}
	
	private void showSelectAddress() {
		final Builder builder = new AlertDialog.Builder(this);
		builder.setItems(regionTwos, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(regionTwos != null && regionTwos.length > which) {
					curRegionIndex = which;
					String region = regionTwos[which].trim();
//					regionManEntity = Contant.regionManEntities.get(which);
					labSelAddress.setText(region);
//					loadRegionStreet(region);
					loadData();
				}
			}
		});
		builder.create().show();
	}
	
	/**
	 * 获得当前选中的地区下面的街道信息
	 */
	private void loadRegionStreet(String regionName) {
		if(regionTwoListRunnable != null) {
			regionTwoListRunnable.isStop();
			regionTwoListRunnable = null;
		}
		showDialog(DIALOG_LOAD);
		String language = SharePreferenceHelper.getSharepreferenceString(InfoListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "zh");
		regionTwoListRunnable = new RegionTwoListRunnable(streetHandler, regionName, language);
		
		new Thread(regionTwoListRunnable).start();
	}
	
	private Handler streetHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1 && msg.obj != null) {
				Object[] objects = (Object[]) msg.obj;
				if(objects != null && objects.length > 0) {
					regionTwos = (String[]) objects[0];
					manEntities = (RegionManEntity[]) objects[1];
				}
//				showSelectStreet();
			} else if(msg.what == 0) {
				// 该地区下面没有街道信息
//				streets = new String[]{getString(R.string.lab_not_info)};
				Toast.makeText(InfoListActivity.this, R.string.msg_not_streer_info, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(InfoListActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private void showSelectStreet() {
		if(streets == null || streets.length < 1) {
			Toast.makeText(InfoListActivity.this, R.string.msg_not_streer_info, Toast.LENGTH_SHORT).show();
			return ;
		}
		final Builder builder = new AlertDialog.Builder(this);
		builder.setItems(streets, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(streets != null && streets.length > which) {
					curStreetIndex = which;
					String street = streets[which];
//					regionStreetEntity = Contant.regionStreetEntities.get(which);
					labSelStreet.setText(street);
					loadData();
				}
			}
		});
		builder.create().show();
	}
	
	private void showSelectType() {
		final Builder builder = new AlertDialog.Builder(this);
		builder.setItems(types, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(types != null && types.length > which) {
					type = (which+1)+"";
					curTypeIndex = which;
					labSelType.setText(types[which]);
					loadData();
				}
			}
		});
		builder.create().show();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isLoad = true;
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
		if(isLoad) {
			// 防止多次加载
			isLoad = false;
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				Object cateObj = bundle.get("categoryEntity");
				if(cateObj != null) {
//					categoryEntity.setParentId(categoryEntity.getId()+"");
					if(categoryEntity == null) {
						categoryEntity = (CategoryEntity) cateObj;
						labTitle.setText(categoryEntity.getTitle());
						parentId = categoryEntity.getId();
						parentName = categoryEntity.getTitle();
					} else {
						categoryEntity = (CategoryEntity) cateObj;
					}
				}
				Object regionObj = bundle.get("regionManEntity");
				if(regionObj != null) {
					regionManEntity = (RegionManEntity) regionObj;
				}
			}
			if(regionManEntity == null) {
				regionManEntity = Contant.curRegionEntity;
			}
			if(regionManEntity != null && regionManEntity.getName() != null) {
				labSelAddress.setText(regionManEntity.getName());
			}
			if(Contant.regionManEntities != null) {
				regions = new String[Contant.regionManEntities.size()];
				for (int i = 0; i < regions.length; i++) {
					regions[i] = Contant.regionManEntities.get(i).getName();
				}
			}
			if(Contant.regionStreetEntities != null && Contant.regionStreetEntities.size() > 0) {
				streets = new String[Contant.regionStreetEntities.size()+1];
				streets[0] = getString(R.string.lab_sel_street);
				for (int i = 1; i < streets.length; i++) {
					streets[i] = Contant.regionStreetEntities.get(i-1).getName();
				}
				curStreetIndex = 0;
			}
			// 加载街道信息
			if(regionTwos == null || regionTwos.length < 1) {
				loadRegionStreet(Contant.curLocationEntity.getAdministrative());
			}
			
			Resources res = getResources();
			types = res.getStringArray(R.array.array_list_type);
			if(categoryEntity != null) {
				if(categoryRunnable != null) {
					categoryRunnable.isStop();
					categoryRunnable = null;
				}
				btnSend.setEnabled(false);
				String language = SharePreferenceHelper.getSharepreferenceString(InfoListActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "zh");
				categoryRunnable = new CategoryRunnable(categoryHandler, parentId, language);
				new Thread(categoryRunnable).start();
			} else {
				loadData();
			}
			
		}
	}
	
	private Handler categoryHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			btnSend.setEnabled(true);
			if(msg.obj != null) {
				categoryList = (ArrayList<CategoryEntity>) msg.obj;
			} else if(msg.what == -1) {
				Toast.makeText(InfoListActivity.this, R.string.msg_category_error, Toast.LENGTH_SHORT).show();
			}
			if(categoryList == null || categoryList.size() < 1) {
				categoryList = new ArrayList<CategoryEntity>();
				categoryList.add(categoryEntity);
			}
			categorys = new String[categoryList.size()];
			categorys[0] = getString(R.string.lab_list_category_all);
			for (int i = 1; i < categoryList.size(); i++) {
				categorys[i] = categoryList.get(i-1).toString();
			}
//			categoryEntity.setParentId(parentId);
//			categoryEntity.setId(categoryList.get(curCategoryIndex).getId());
//			labSelCategory.setText(categoryList.get(curCategoryIndex).getTitle());
			loadData();
		};
	};
	
	private Handler handler = new Handler() {
		@SuppressWarnings({ "unchecked", "deprecation" })
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
			} else if(msg.what == -1 || msg.what == 0 && (list == null || list.size() == 0)) {
				labNotInfo.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
//				Toast.makeText(InfoListActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void initVal() {
		
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void loadData() {
		if(listInfoRunnable != null) {
			listInfoRunnable.isStop();
			listInfoRunnable = null;
		}
		String cityName = Contant.curLocationEntity.getAdministrative();
		String reName = null;
		String street = null;
		
		if(manEntities != null && curRegionIndex != -1 && manEntities.length > 0) {
			RegionManEntity entity = manEntities[curRegionIndex];
			if(entity.getParentName().trim().equals(cityName)) {
				reName = entity.getName().trim();
				labSelAddress.setText(reName);
			} else {
				reName = entity.getParentName().trim();
				street = entity.getName().trim();
				labSelAddress.setText(street);
			}
		}
//		if(curStreetIndex != 0 && streets != null && streets.length > 0) {
//			street = streets[curStreetIndex];
//		}
		if(isProgress()) {
			dismissDialog(DIALOG_LOAD);
		}
		showDialog(DIALOG_LOAD);
		listInfoRunnable = new ItemsGetRunnable(handler, cityName, reName, street, categoryEntity.getParentId(), categoryEntity.getId()+"", type, 1, pageSize);
		new Thread(listInfoRunnable).start();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void pageLoadData() {
		if(curPage < totalPage) {
			showDialog(DIALOG_LOAD);
			curPage ++;
			if(listInfoRunnable != null) {
				listInfoRunnable.isStop();
				listInfoRunnable = null;
			}
			String cityName = Contant.curLocationEntity.getAdministrative();
			String reName = regionManEntity.getName();
			String street = null;
			if(curStreetIndex != 0 && streets != null && streets.length > 0) {
				street = streets[curStreetIndex];
			}
			listInfoRunnable = new ItemsGetRunnable(handler, cityName, reName, street, categoryEntity.getParentId(), categoryEntity.getId()+"", type, curPage, pageSize);
			new Thread(listInfoRunnable).start();
		} else {
			labLoadMore.setVisibility(View.GONE);
			footviewLayout.setVisibility(View.GONE);
			Toast.makeText(InfoListActivity.this, R.string.msg_LoasLast, Toast.LENGTH_SHORT).show();
		}
	}
	
}
