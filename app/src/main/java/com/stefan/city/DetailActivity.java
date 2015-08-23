package com.stefan.city;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.entity.MessageEntity;
import com.stefan.city.module.entity.StoreEntity;
import com.stefan.city.module.thread.FavoriteManageRunnable;
import com.stefan.city.module.thread.HistoryManageRunnable;
import com.stefan.city.module.thread.MessageRunnable;
import com.stefan.city.ui.adapter.PageItemAdapter;
import com.stefan.city.ui.page.detail.PageDetailInfoView;
import com.stefan.city.ui.page.detail.PageDetialMessageView;
import com.stefan.city.ui.page.view.BasePageView;

/**
 * DetailActivity 详细内容
 * 
 * @author 日期：2014-6-27下午08:34:39
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 **/
public class DetailActivity extends BaseActivity {

	private static final int PAGE_VIEW_INFO = 0;

	private static final int PAGE_VIEW_MSG = 1;

	private boolean isLoad;
	private InfoItemEntity entity;

	private Button btnBack, btnFavorite;

	private TextView labTitle, labView;

//	private HistoryService historyService;
//	private FavoriteService favoriteService;
	
	private HistoryManageRunnable historyManageRunnable;
	
	private FavoriteManageRunnable favoriteManageRunnable;

	// 切换界面
	private PageDetailInfoView detailInfoView;

	private PageDetialMessageView detailMessageView;

	// 顶部选项按钮
	private RadioGroup mRadioGroupLoc;
	// 详细信息
	private RadioButton mRadioBtnDetailInfo;
	// 评论
	private RadioButton mRadioBtnDetailMsg;

	private ViewPager viewPager;
	private List<BasePageView> pageViews;
	private PageItemAdapter pageItemAdapter;

	private MessageRunnable messageRunnable;
	private List<MessageEntity> messageList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLoad = true;
		initLayout();
	}

	private void initLayout() {
		setContentView(R.layout.activity_detail);
		btnBack = (Button) findViewById(R.id.detail_back);
		btnBack.setOnClickListener(clickListener);

		btnFavorite = (Button) findViewById(R.id.detail_favorite);
		btnFavorite.setOnClickListener(clickListener);

		labTitle = (TextView) findViewById(R.id.detail_title);
		labView = (TextView) findViewById(R.id.txt_detail_info_reads);

		mRadioGroupLoc = (RadioGroup) findViewById(R.id.mRadioGroupDetailLoc);

		mRadioBtnDetailInfo = (RadioButton) mRadioGroupLoc
				.findViewById(R.id.mRadioBtnDetailInfo);
		mRadioBtnDetailInfo.setOnClickListener(mOnClickListener);
		mRadioBtnDetailMsg = (RadioButton) mRadioGroupLoc
				.findViewById(R.id.mRadioBtnDetailMsg);
		mRadioBtnDetailMsg.setOnClickListener(mOnClickListener);

		detailInfoView = new PageDetailInfoView(DetailActivity.this);
		detailInfoView.setTag(PAGE_VIEW_INFO);
		detailMessageView = new PageDetialMessageView(DetailActivity.this);
		detailMessageView.setTag(PAGE_VIEW_MSG);

		pageViews = new ArrayList<BasePageView>();
		pageViews.add(detailInfoView);
		pageViews.add(detailMessageView);

		pageItemAdapter = new PageItemAdapter(pageViews);

		viewPager = (ViewPager) findViewById(R.id.detailPager);
		viewPager.setAdapter(pageItemAdapter);
		viewPager.setOnPageChangeListener(pageChangeListener);

		BasePageView basePageView = (BasePageView) pageViews
				.get(Contant.VIEW_MAIN);
		basePageView.onResume();
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mRadioBtnDetailInfo: {
				// BasePageView basePageView = (BasePageView)
				// pageViews.get(Contant.VIEW_MAIN);
				// basePageView.onResume();
				changeCheckStatus(PAGE_VIEW_INFO);
				viewPager.setCurrentItem(PAGE_VIEW_INFO);
			}
				break;
			case R.id.mRadioBtnDetailMsg: {
				changeCheckStatus(PAGE_VIEW_MSG);
				viewPager.setCurrentItem(PAGE_VIEW_MSG);
				// BasePageView basePageView = (BasePageView)
				// pageViews.get(Contant.VIEW_SCORE);
				// basePageView.onResume();
			}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 点击切换选项面板
	 * 
	 * @param checkedId
	 */
	private void changeCheckStatus(int checkedId) {

		switch (checkedId) {
		case PAGE_VIEW_INFO:
		case R.id.mRadioBtnDetailInfo: {
			BasePageView basePageView = (BasePageView) pageViews
					.get(Contant.VIEW_MAIN);
			if (basePageView != null) {
				basePageView.onResume();
			}
			mRadioBtnDetailInfo.setBackgroundResource(R.drawable.tab_selected);
			toDetail(mRadioBtnDetailMsg);
		}
			break;
		case PAGE_VIEW_MSG:
		case R.id.mRadioBtnDetailMsg: {
			BasePageView basePageView = (BasePageView) pageViews
					.get(Contant.VIEW_SCORE);
			if (basePageView != null) {
				basePageView.onResume();
			}
			mRadioBtnDetailMsg.setBackgroundResource(R.drawable.tab_selected);
			toDetail(mRadioBtnDetailInfo);
		}
			break;
		default:
			break;
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			changeCheckStatus(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private void toDetail(RadioButton view) {
		view.setBackgroundResource(R.color.color_transparent);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.detail_back:
				DetailActivity.this.finish();
				break;

			case R.id.detail_favorite:
				addFavorite();
				break;

			default:
				break;
			}
		}
	};
	
	private void addFavorite() {
		if (Contant.curUser != null) {
			showDialog(DIALOG_LOAD);
			if(favoriteManageRunnable != null) {
				favoriteManageRunnable.isStop();
				favoriteManageRunnable = null;
			}
			favoriteManageRunnable = new FavoriteManageRunnable(manHandler, Contant.curUser.getId(), 
					entity.getId()+"", null, FavoriteManageRunnable.STATUS_ADD);
			new Thread(favoriteManageRunnable).start();
		} else {
			Toast.makeText(DetailActivity.this, R.string.msg_not_login,
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private Handler manHandler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			if (isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what != -1) {
				if (msg.what > 0) {
					Toast.makeText(DetailActivity.this,
							R.string.msg_add_favorite_success, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(DetailActivity.this,
							R.string.msg_operate_error, Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				// 检查网络连接
				Toast.makeText(DetailActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
		
	};

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isLoad = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isLoad) {
			isLoad = false;
			loadData();
		}
	}

	private void loadData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			Object object = bundle.get("entity");
			if (object != null) {
				
				entity = (InfoItemEntity) bundle.get("entity");
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				StoreEntity storeEntity = new StoreEntity(entity.getTitle(),
						entity.getId() + "", dateFormat.format(new Date()), 1);
				if(historyManageRunnable != null) {
					historyManageRunnable.isStop();
					historyManageRunnable = null;
				}
				historyManageRunnable = new HistoryManageRunnable(null, DetailActivity.this, storeEntity);
				new Thread(historyManageRunnable).start();
				
				detailInfoView.setEntity(entity);
				detailMessageView.setItemId(entity.getId() + "");
				messageRunnable = new MessageRunnable(handler, entity);
				showDialog(DIALOG_LOAD);
				updateUI();
				new Thread(messageRunnable).start();
			}/*
			 * else { String itemId = bundle.getString("itemId", null);
			 * if(itemId != null) { itemInfoService. messageRunnable = new
			 * MessageRunnable(handler, itemId); } }
			 */
		}
		viewPager.setCurrentItem(0);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if (msg.obj != null) {
				Object[] objects = (Object[]) msg.obj;
				messageList = (ArrayList<MessageEntity>) objects[1];
				detailMessageView.setList(messageList);
			}
		};
	};

	private void updateUI() {
		if (entity != null) {
			labTitle.setText(entity.getTitle());
			// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			// labTime.setText(dateFormat.format(entity.getCreateDate()));
			labView.setText("0");
		}
	}
	
	public void showLoading() {
		showDialog(DIALOG_LOAD);
	}
	
	public void dismissLoading() {
		if(isProgress()) {
			dismissDialog(DIALOG_LOAD);
		}
	}

}
