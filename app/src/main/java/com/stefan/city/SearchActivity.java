package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rockeagle.framework.core.REListActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.thread.ItemSearchRunnable;
import com.stefan.city.ui.adapter.MainInfoAdapter;
import com.umeng.analytics.MobclickAgent;

/**
 * SearchActivity
 * @author 日期：2015-2-14上午11:19:25
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class SearchActivity extends REListActivity {
	
	private TextView labTitle, labNotInfo;
	private ListView listView;
	
	private EditText editSearch;
	// 搜索的关键字
	private String keyword;
	
	private Button btnBack, btnSend;
	
	private ImageButton btnSearch;
	
	private ItemSearchRunnable itemSearchRunnable;
	private List<InfoItemEntity> list;
	
	private MainInfoAdapter infoAdapter;
	
//	private RegionEntity regionEntity;
	private boolean isLoad;
	
	protected View footviewLayout;	// listview的加载提示View
	// 点击加载更多信息
	private TextView labLoadMore;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLoad = true;
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.activity_search);
		
		labTitle = (TextView) findViewById(R.id.info_search_title);
		btnBack = (Button) findViewById(R.id.info_search_btnaddress);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
			}
		});
		
		btnSend = (Button) findViewById(R.id.info_search_btnsend);
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SearchActivity.this, SendActivity.class);
				startActivity(intent);
				SearchActivity.this.finish();
			}
		});
		
		editSearch = (EditText) findViewById(R.id.edit_seatch);
		
		btnSearch = (ImageButton) findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(clickListener);
		
		labNotInfo = (TextView) findViewById(R.id.lab_search_notinfo);
		labNotInfo.setOnClickListener(clickListener);
		
		listView = (ListView) findViewById(R.id.search_listView);
		infoAdapter = new MainInfoAdapter(SearchActivity.this);
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InfoItemEntity entity = list.get(arg2);
				Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
				intent.putExtra("entity", entity);
				startActivity(intent);
			}
		});
		
		LayoutInflater inflater = LayoutInflater.from(SearchActivity.this);
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
		
//		footviewLayout = (View) headView.findViewById(R.id.recomBottomPreLayout);
//		listView.addHeaderView(headView);
		listView.addFooterView(footView);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.btn_search: 
				toSearch();
				break;
				
			case R.id.lab_search_notinfo:
				// 尚无数据，点击重试
				labNotInfo.setVisibility(View.GONE);
				loadData();
				break;
				
			default:
				break;
			}
		}
	};
	
	private void toSearch() {
		keyword = editSearch.getText().toString();
		if(keyword == null || keyword.equals("")) {
			// msg_keyword_is_not_null
			Toast.makeText(SearchActivity.this, R.string.msg_keyword_is_not_null, Toast.LENGTH_SHORT).show();
		} else {
			loadData();
		}
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
		labTitle.setText(Contant.curLocationEntity.getAdministrative());
		if(isLoad) {
			// 防止多次加载
			isLoad = false;
			loadData();
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

	@Override
	protected void loadData() {
		showDialog(DIALOG_LOAD);
		curPage = 1;
		if(itemSearchRunnable != null) {
			itemSearchRunnable.isStop();
			itemSearchRunnable = null;
		}
		String cityName = Contant.curLocationEntity.getAdministrative();
		itemSearchRunnable = new ItemSearchRunnable(handler, cityName, keyword, curPage, pageSize);
		new Thread(itemSearchRunnable).start();
	}

	@Override
	protected void pageLoadData() {
		if(curPage < totalPage) {
			showDialog(DIALOG_LOAD);
			curPage ++;
			if(itemSearchRunnable != null) {
				itemSearchRunnable.isStop();
				itemSearchRunnable = null;
			}
			String cityName = Contant.curLocationEntity.getAdministrative();
			itemSearchRunnable = new ItemSearchRunnable(handler, cityName, keyword, curPage, pageSize);
			new Thread(itemSearchRunnable).start();
		} else {
			labLoadMore.setVisibility(View.GONE);
			footviewLayout.setVisibility(View.GONE);
			Toast.makeText(SearchActivity.this, R.string.msg_LoasLast, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			SearchActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
