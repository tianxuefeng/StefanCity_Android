package com.stefan.city;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rockeagle.framework.core.REActivityManager;
import com.rockeagle.framework.core.REFragmentActivity;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.thread.UserHasAdminRunnable;
import com.stefan.city.ui.adapter.PageItemAdapter;
import com.stefan.city.ui.page.view.BasePageView;
import com.stefan.city.ui.page.view.PageCategoryView;
import com.stefan.city.ui.page.view.PageScoreView;
import com.stefan.city.ui.page.view.PageUserView;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends REFragmentActivity {

	// 顶部选项按钮
	private RadioGroup  mRadioGroupLoc;
	// 主界面
	private RadioButton mRadioBtnMain;
	// 推荐阅读
	private RadioButton mRadioBtnScore;
	// 阅读排行
	private RadioButton mRadioBtnUser;
	
	// 主界面
	private PageCategoryView pageMainView;
	// 积分中心
	private PageScoreView pageScoreView;
	// 个人中心
	private PageUserView pageUserView;
	
	private ViewPager viewPager;
	private List<BasePageView> pageViews;
	private PageItemAdapter pageItemAdapter;
	
	private int categoryId;
	private LocationEntity locationEntity;
	
	private View btnSend;
	private boolean isShowing = false;
	
	private boolean isIntent = false;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	// 用于保存当前界面的索引
	private int curIndex = 0;
	
	private UserHasAdminRunnable userManageRunnable;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isIntent = true;
        setContentView(R.layout.activity_main);
        initLayout();
    }

    private void initLayout() {
    	
    	mRadioGroupLoc = (RadioGroup) findViewById(R.id.mRadioGroupLoc);
		
		mRadioBtnScore = (RadioButton) mRadioGroupLoc.findViewById(R.id.mRadioBtnScore);
		mRadioBtnScore.setOnClickListener(mOnClickListener);
		mRadioBtnScore.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_score, 0, 0);
		mRadioBtnMain = (RadioButton) mRadioGroupLoc.findViewById(R.id.mRadioBtnMain);
		mRadioBtnMain.setOnClickListener(mOnClickListener);
		mRadioBtnMain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_home, 0, 0);
		mRadioBtnUser = (RadioButton) mRadioGroupLoc.findViewById(R.id.mRadioBtnUser);
		mRadioBtnUser.setOnClickListener(mOnClickListener);
		mRadioBtnUser.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_user, 0, 0);
		
		pageMainView = new PageCategoryView(MainActivity.this);
		pageMainView.setTag(Contant.VIEW_MAIN);
		pageScoreView = new PageScoreView(MainActivity.this);
		pageScoreView.setTag(Contant.VIEW_SCORE);
		pageUserView = new PageUserView(MainActivity.this);
		pageUserView.setTag(Contant.VIEW_USER);
		
    	pageViews = new ArrayList<BasePageView>();
    	pageViews.add(pageMainView);
    	pageViews.add(pageScoreView);
    	pageViews.add(pageUserView);
    	
    	pageItemAdapter = new PageItemAdapter(pageViews);
    	
    	viewPager = (ViewPager)findViewById(R.id.mainPager);
    	viewPager.setAdapter(pageItemAdapter);
    	viewPager.setOnPageChangeListener(pageChangeListener);
    	viewPager.setCurrentItem(0);
    	BasePageView basePageView = (BasePageView) pageViews.get(Contant.VIEW_MAIN);
		basePageView.onResume();
		
	    btnSend = findViewById(R.id.layout_bottomRight);
	    btnSend.setOnClickListener(clickListener);
//	    pageIndicatorEx = (UnderlinePageIndicator)findViewById(R.id.underline_indicator);
//	    pageIndicatorEx.setViewPager(viewPager);
//	    pageIndicatorEx.setFades(false);
//	     
//	    mTabPageIndicator.setOnPageChangeListener(pageIndicatorEx);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		// 退出系统
    		new AlertDialog.Builder(MainActivity.this).setTitle(R.string.title_exit)
            .setMessage(R.string.msg_exit)
            .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
    			
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    				REActivityManager.getActivityManager().exit();
    			}
    		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int which) {  
                    dialog.dismiss();  
                }  
            }).create().show();
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	setIntent(intent);
    	isIntent = true;
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
    	int viewIndex = -1;
    	if(isIntent) {
    		isIntent = false;
    		Bundle bundle = getIntent().getExtras();
    		if(bundle != null) {
    			Object proObj = bundle.get("locationEntity");
    			if(proObj != null) {
    				locationEntity = (LocationEntity) proObj;
    				if(locationEntity != null && Contant.curLocationEntity == null) {
    					Contant.curLocationEntity = locationEntity;
    				}
    			}
    			viewIndex = bundle.getInt("viewIndex", Contant.VIEW_MAIN);
    			int isInit = bundle.getInt("isInit", 0);
    			String oldVersion = SharePreferenceHelper.getSharepreferenceString(MainActivity.this, Contant.SETTINGSP, Contant.PREF_VERSION, null);
    			String newVersion = null;
    			int isShowDialog = SharePreferenceHelper.getSharepreferenceInt(MainActivity.this, Contant.SETTINGSP, Contant.PREF_SHOW_DIALOG);
    			try {
					newVersion = Contant.getVersionName(MainActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 每次更新后的版本只会弹出一次，提示用户是否成为管理员
				if((oldVersion == null || (newVersion != null && !newVersion.equals(oldVersion))) || isShowDialog == 0) {
					if(isInit > 0) {
						if(Contant.curUser == null && Contant.curLocationEntity != null && Contant.curLocationEntity.getAdministrative() != null) {
							// 对当前城市进行管理员判断
							if(userManageRunnable != null) {
								userManageRunnable.isStop();
								userManageRunnable = null;
							}
							userManageRunnable = new UserHasAdminRunnable(handler, Contant.curLocationEntity.getAdministrative());
							new Thread(userManageRunnable).start();
						}
					}
				}
    		}
    	}
    	if(viewIndex != -1) {
    		changeCheckStatus(viewIndex);
    	} else if (viewIndex == -1 && curIndex != -1) {
    		BasePageView basePageView = (BasePageView) pageViews.get(curIndex);
			if(basePageView != null){
				basePageView.onResume(false);
			}
    	}
    }
    
    /**
	 * 点击切换选项面板
	 * @param checkedId
	 */
	private void changeCheckStatus(int checkedId)
	{

		switch (checkedId) {
		case Contant.VIEW_MAIN:
		case R.id.mRadioBtnMain:{
			curIndex = Contant.VIEW_MAIN;
			BasePageView basePageView = (BasePageView) pageViews.get(Contant.VIEW_MAIN);
			if(basePageView != null){
				basePageView.onResume();
			}
			mRadioBtnMain.setTextColor(getResources().getColor(R.color.font_nav_selected));
			mRadioBtnMain.setBackgroundResource(R.color.theme_color);
			toDetail(mRadioBtnScore);
			toDetail(mRadioBtnUser);
		}
			break;
		case Contant.VIEW_SCORE:
		case R.id.mRadioBtnScore:{
			curIndex = Contant.VIEW_SCORE;
			BasePageView basePageView = (BasePageView) pageViews.get(Contant.VIEW_SCORE);
			if(basePageView != null){
				basePageView.onResume();
			}
			mRadioBtnScore.setTextColor(getResources().getColor(R.color.font_nav_selected));
			mRadioBtnScore.setBackgroundResource(R.color.theme_color);
			toDetail(mRadioBtnUser);
			toDetail(mRadioBtnMain);
		}
			break;
		case Contant.VIEW_USER:
		case R.id.mRadioBtnUser:{
			curIndex = Contant.VIEW_USER;
			BasePageView basePageView = (BasePageView) pageViews.get(Contant.VIEW_USER);
			basePageView.onResume();
			mRadioBtnUser.setTextColor(getResources().getColor(R.color.font_nav_selected));
			mRadioBtnUser.setBackgroundResource(R.color.theme_color);
			toDetail(mRadioBtnMain);
			toDetail(mRadioBtnScore);
		}
			break;
		default:
			break;
		}
		viewPager.setCurrentItem(checkedId);
	}
	
	private OnClickListener mOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mRadioBtnMain:{
				curIndex = Contant.VIEW_MAIN;
				viewPager.setCurrentItem(Contant.VIEW_MAIN);
				BasePageView basePageView = (BasePageView) pageViews.get(Contant.VIEW_MAIN);
				basePageView.onResume();
			}
				break;
			case R.id.mRadioBtnScore:{
				curIndex = Contant.VIEW_SCORE;
				viewPager.setCurrentItem(Contant.VIEW_SCORE);
				BasePageView basePageView = (BasePageView) pageViews.get(Contant.VIEW_SCORE);
				basePageView.onResume();
			}
				break;
			case R.id.mRadioBtnUser:{
				curIndex = Contant.VIEW_USER;
				viewPager.setCurrentItem(Contant.VIEW_USER);
				BasePageView basePageView = (BasePageView) pageViews.get(Contant.VIEW_USER);
				basePageView.onResume();
			}
				break;
			default:
				break;
			}
		}
	};
	
	private void toDetail(RadioButton view) {
		view.setTextColor(getResources().getColor(R.color.font_nav_selected));
		view.setBackgroundResource(R.color.color_transparent);
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
    
    private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_bottomRight:
				Intent intent = new Intent(MainActivity.this, SendActivity.class);
				intent.putExtra("activityClass", MainActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0) {
				String newVersion = null;
				try {
					newVersion = Contant.getVersionName(MainActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				SharePreferenceHelper.setSharepreferenceString(MainActivity.this, Contant.SETTINGSP, Contant.PREF_VERSION, newVersion);
				SharePreferenceHelper.setSharepreferenceInt(MainActivity.this, Contant.SETTINGSP, Contant.PREF_SHOW_DIALOG, 1);
				// 弹出对话框，提示用户是否注册成为管理员
				// 退出系统
	    		new AlertDialog.Builder(MainActivity.this).setTitle(R.string.title_message)
	            .setMessage(R.string.msg_hasAdmin_text)
	            .setNeutralButton(R.string.btn_have_an_account, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 跳转到登陆界面
						dialog.dismiss();
						Intent intent = new Intent(MainActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				}).setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
	    			
	    			public void onClick(DialogInterface dialog, int which) {
	    				// 跳转到注册界面
	    				dialog.dismiss();
	    				Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
	    				intent.putExtra("memType", 3);
						startActivity(intent);
	    			}
	    		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
	                public void onClick(DialogInterface dialog, int which) {  
	                    dialog.dismiss();  
	                }  
	            }).create().show();
			}
		};
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:{
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			intent.putExtra("curIndex", curIndex);
			startActivity(intent);
			MainActivity.this.finish();
		}
			break;
			
		case R.id.action_exit:
			// 退出系统
    		new AlertDialog.Builder(MainActivity.this).setTitle(R.string.title_exit)
            .setMessage(R.string.msg_exit)
            .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
    			
    			public void onClick(DialogInterface dialog, int which) {
    				dialog.dismiss();
    				REActivityManager.getActivityManager().exit();
    			}
    		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int which) {  
                    dialog.dismiss();  
                }  
            }).create().show();
			break;
			
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
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
