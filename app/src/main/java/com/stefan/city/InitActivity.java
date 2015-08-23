package com.stefan.city;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.rockeagle.framework.core.REActivityManager;
import com.rockeagle.framework.core.RockEagleApp;
import com.rockeagle.framework.core.contant.REContant;
import com.rockeagle.framework.tools.REHttpUtility;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.service.MemberService;
import com.stefan.city.module.service.RegionService;
import com.umeng.analytics.MobclickAgent;

/**
 * LoginActivity
 * 程序初始化
 * @author 日期：2014-5-13下午08:34:58
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class InitActivity extends BaseActivity {
	
	private boolean isLogin = true;
	
	private boolean flag = true;
	private ImageView[] imgIcons;
	private int count = 6;
	private final int EDIT_TYPE_SELECTED = 1;     //选中的   
	private final int EDIT_TYPE_NO_SELECTED = 2;  //未选中的
	private final int EDIT_TYPE_CLEAR = 3; // 清空状态
	
	private long time;
	private long sumTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // sd card 不可用    
            Toast.makeText(InitActivity.this, R.string.storage_unavailable, Toast.LENGTH_SHORT).show();
            InitActivity.this.finish();
            REActivityManager.getActivityManager().exit();
        }
		MobclickAgent.setDebugMode(true);
		REContant.TestURL = ContantURL.URL_SERVER;
		imgIcons = new ImageView[count];
		imgIcons[0] = (ImageView) findViewById(R.id.load_paint01);
		imgIcons[1] = (ImageView) findViewById(R.id.load_paint02);
		imgIcons[2] = (ImageView) findViewById(R.id.load_paint03);
		imgIcons[3] = (ImageView) findViewById(R.id.load_paint04);
		imgIcons[4] = (ImageView) findViewById(R.id.load_paint05);
		imgIcons[5] = (ImageView) findViewById(R.id.load_paint06);
		loadAnim.start();
		time = System.currentTimeMillis();
		initialization();
	}
	
	@Override
	public void initBack(int result) {
		if(!Contant.IsNetStatus) {
			showNotNet();
		} else {
			Intent intent = null;
			String language = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "");
			if(language == null || language.equals("")) {
				intent = new Intent(InitActivity.this, WelcomeActivity.class);
			} else {
				if(Contant.curLocationEntity != null) {
					intent = new Intent(InitActivity.this, MainActivity.class);
				} else {
					intent = new Intent(InitActivity.this, GPSLocationActivity.class);
					intent.putExtra("isMain", true);
				}
			}
			if(language.equalsIgnoreCase("zh-Hans")) {
				Contant.LANGUAGE_PARAMETER = "zh-CN";
				switchLanguage(Locale.CHINA);
			} else if(language.equalsIgnoreCase("zh-Hant")) {
				Contant.LANGUAGE_PARAMETER = "zh";
				switchLanguage(Locale.CHINESE);
			} else if(language.equalsIgnoreCase("en")) {
				switchLanguage(Locale.ENGLISH);
			}
			startActivity(intent);
			InitActivity.this.finish();
		}
	}
	
	private void showNotNet() {
    	new AlertDialog.Builder(InitActivity.this)
		.setTitle(R.string.app_name)
		.setMessage(R.string.msg_init_not_net)
		.setCancelable(false)
		.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				RockEagleApp.isInit = false;
				initialization();
			}
		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
            	dialog.dismiss();
            	REActivityManager.getActivityManager().exit();
            }
        }).create().show();
    }
	
	/**
	 * 选择语言
	 * @param locale
	 */
	public void switchLanguage(Locale locale) {
        Resources resources = getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; // 简体中文
        resources.updateConfiguration(config, dm);
    }
	
	/**
     * 还原进度条
     */
    public void resete(){
    	int i;
    	for(i=0; i < count; i++){
    		imgIcons[i].setBackgroundResource(R.drawable.load_step);
        }
    }
    
    private Thread loadAnim = new Thread(){
    	@Override
	     public void run(){
    		Message msg;
    		while(flag){
    			for(int i= 0 ; i < count ; i++){
    				msg = new Message();
    				msg.what = EDIT_TYPE_SELECTED;
    				msg.arg1 = i;
    				myHandler.sendMessage(msg);
    				msg = new Message();
    				SystemClock.sleep(500);
    			}
    			msg = new Message();
    			msg.what = EDIT_TYPE_CLEAR;
				myHandler.sendMessage(msg);
				SystemClock.sleep(500);
    		}
    		
	     }
    };
    
    public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(!flag){
				return ;
			}
			switch(msg.what)
			{
			case EDIT_TYPE_SELECTED:
				imgIcons[msg.arg1].setBackgroundResource(R.drawable.load_footprint);
				break;
			case EDIT_TYPE_NO_SELECTED:
				imgIcons[msg.arg1].setBackgroundResource(R.drawable.load_step);
				break;
			case EDIT_TYPE_CLEAR:
				resete();
			}
		}
    };
    
	@Override
	public void isInit() {
		super.isInit();
		String result = null;
		try {
			result = REHttpUtility.httpGetString(ContantURL.TestURL);
			// 提供一次重试
			if(result == null || result.equals("")) {
				result = REHttpUtility.httpGetString(ContantURL.TestURL);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(result == null || result.equals("")){
			Contant.IsNetStatus = false;
			return ;
		} else {
			Contant.IsNetStatus = true;
		}
		// 获得定位信息
		String country = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, 
				Contant.SETTINGSP, Contant.PREF_COUNTRY, null);
		if(country != null) {
			String city = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, 
					Contant.SETTINGSP, Contant.PREF_CITY, null);
			
			String locality = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, 
					Contant.SETTINGSP, Contant.PREF_LOCALITY, null);
			
			String street = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, 
					Contant.SETTINGSP, Contant.PREF_STREET, null);
			
			String detail = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, 
					Contant.SETTINGSP, Contant.PREF_DETAIL, null);
			
			String latitude = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, 
					Contant.SETTINGSP, Contant.PREF_LATITUDE, null);
			
			String longitude = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, 
					Contant.SETTINGSP, Contant.PREF_LONGITUDE, null);
			
			if(city != null && detail != null) {
				LocationEntity locationEntity = new LocationEntity(country, city, locality, street, longitude, latitude, detail);
				Contant.curLocationEntity = locationEntity;
				RegionService regionService = new RegionService();
				// 获得当前区域列表
				String language = SharePreferenceHelper.getSharepreferenceString(
						InitActivity.this, Contant.SETTINGSP,
						Contant.PREF_LANGUAGE, "zh-Hans");
				List<RegionManEntity> list = regionService.getList(locationEntity.getAdministrative(), language);
				if(list == null || list.size() <= 0) {
					list = new ArrayList<RegionManEntity>();
//					curRegionEntity
					RegionManEntity entity = new RegionManEntity();
					entity.setName(locationEntity.getLocality());
					list.add(entity);
				}
				for (RegionManEntity regionManEntity : list) {
					// 得到当前选中的地区
					if(regionManEntity.getName().equalsIgnoreCase(locationEntity.getLocality())) {
						Contant.curRegionEntity = regionManEntity;
					}
				}
				Contant.regionManEntities = list;
				
//				temp = (System.currentTimeMillis() - time);
//				sumTime += temp;
//				System.out.println("获得当前地区信息：" + temp);
			}
		}
		
		// 检查登陆信息
		if (REHttpUtility.isCheckConOK()) {
			String email = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, Contant.SETTINGSP, UserEntity.FIELD_EMAIL, null);
			String pawd = SharePreferenceHelper.getSharepreferenceString(InitActivity.this, Contant.SETTINGSP, UserEntity.FIELD_PAWD, null);
			if(email != null && !email.equals("") && pawd != null && !pawd.equals("")) {
				MemberService memberService = new MemberService();
				Contant.curUser = memberService.toLoginMD5(email, pawd);
			}
		}
		int[] size = RockEagleApp.system.getDisplaySize(getResources());
		// 得到屏幕大小
		Contant.ScreenSize[0] = size[0];
		Contant.ScreenSize[1] = size[1];
		
		initImageLoader();
		
		
	}
	
	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
	    .Builder(InitActivity.this)  
	    .threadPoolSize(3)//线程池内加载的数量  
	    .threadPriority(Thread.NORM_PRIORITY - 2)  
	    .denyCacheImageMultipleSizesInMemory()  
	    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
	    .memoryCacheSize(2 * 1024 * 1024)    
	    .discCacheSize(50 * 1024 * 1024)    
	    .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
	    .tasksProcessingOrder(QueueProcessingType.LIFO)  
	    .discCacheFileCount(100) //缓存的文件数量  
//	    .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径  
	    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
	    .imageDownloader(new BaseImageDownloader(InitActivity.this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
	    .writeDebugLogs() // Remove for release app  
	    .build();//开始构建  
		ImageLoader.getInstance().init(config);//全局初始化此配置 
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			flag = false;
			REActivityManager.getActivityManager().exit();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		flag = false;
		super.onDestroy();
	}
	
}
