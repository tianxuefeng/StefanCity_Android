package com.stefan.city.module.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.rockeagle.framework.core.REActivityManager;
import com.rockeagle.framework.core.RockEagleApp;
import com.rockeagle.framework.core.files.REFileHandle;
import com.rockeagle.framework.core.thread.REBaseRunnable;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.GPSLocationActivity;
import com.stefan.city.MainActivity;
import com.stefan.city.R;
import com.stefan.city.SettingActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.LocationEntity;
import com.stefan.city.module.entity.SettingEntity;
import com.stefan.city.module.service.HistoryService;
import com.stefan.city.module.thread.LocationRunnable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * SettingHandler
 * @author 日期：2014-7-23下午04:25:41
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class SettingHandler {

	private List<SettingEntity> entities = null;
	
	private Context context;
	
	private int curIndex = 0;
	
	private HistoryService historyService;
	
	private CacheRunnable cacheRunnable;
	
	private LocationRunnable locationRunnable;
	
	// 百度定位
//	private LocationUtils locationUtils;
	
	private String language;
	private String languageParameter;
	private Locale locale;
	
	public SettingHandler(Context context) {
		this.context = context;
		historyService = new HistoryService(context);
		initData();
	}
	
	private void initData() {
		entities = new ArrayList<SettingEntity>();
		int index = 1;
		SettingEntity[] settingItems = new SettingEntity[9];
		
		settingItems[0] = new SettingEntity(index++, context.getString(R.string.setting_clear_history), -1, -1);
		settingItems[1] = new SettingEntity(index++, context.getString(R.string.setting_clear_cache), -1, -1);
		settingItems[2] = new SettingEntity(index++, context.getString(R.string.setting_clear_temp), -1, -1);
		settingItems[3] = new SettingEntity(index++, context.getString(R.string.lab_gps_location), -1, -1);
		
		settingItems[4] = new SettingEntity(index++, context.getString(R.string.setting_language), -1, -1);
		
		for (SettingEntity settingEntity : settingItems) {
			entities.add(settingEntity);
		}
	}
	
	/**
	 * 触发设置事件
	 * @param position
	 */
	public void settingItem(int position) {
		SettingEntity entity = entities.get(position);
		if(entity.getType() >= 0) {
			int status = entity.getStatus();
			String text = null;
			if(status == 0) {
				status = 1;
				text = context.getString(R.string.setting_prompt_on);
				text = text.replaceAll("\\s", entity.getItemName());
			} else {
				status = 0;
				text = context.getString(R.string.setting_prompt_off);
				text = text.replaceAll("\\s", entity.getItemName());
			}
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			entity.setStatus(status);
		}
		switch(entity.getId()) {
		case 1:
			// 清空浏览记录
			clearStore(entity.getItemName());
			break;
			
		case 2:
			// 清除缓存
			clearCache();
			break;
			
		case 3:
			// 清除临时文件
			clearHis();
			break;
			
		case 4:
			// 重新定位
			settingLocation();
			break;
			
		case 5:
			// 设置语言
			settingLanguage();
			break;
		}
	}
	
	/**
	 * 重新定位
	 */
	private void settingLocation() {
		Intent intent = new Intent(context, GPSLocationActivity.class);
		intent.putExtra("activityClass", SettingActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 设置语言
	 */
	private void settingLanguage() {
		new AlertDialog.Builder(context)
		.setItems(R.array.array_list_language, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				language = "zh-Hans";
				languageParameter = "zh-CN";
				locale = Locale.CHINA;
				switch(which) {
				case 0:
					// 简体中文
					languageParameter = "zh-CN";
					locale = Locale.CHINA;
					break;
				case 1:
					// 繁体中文
					language = "zh-Hant";
					languageParameter = "zh";
					locale = Locale.CHINESE;
					break;
					
				case 2:
					// 英文
					language = "en";
					languageParameter = "en";
					locale = Locale.ENGLISH;
					break;
				}
				// 保存语言设置
				SharePreferenceHelper.setSharepreferenceString(context, Contant.SETTINGSP, Contant.PREF_LANGUAGE, language);
//				TelephonyManager tm = (TelephonyManager)context.getSystemService(Activity.TELEPHONY_SERVICE);
//		        String countryCodeValue = tm.getNetworkCountryIso();
//		        String localeCur = context.getResources().getConfiguration().locale.getCountry();
//				if(countryCodeValue.equalsIgnoreCase("cn") || (countryCodeValue == "" && !localeCur.equalsIgnoreCase("CN"))) {
//					Contant.LANGUAGE_PARAMETER = languageParameter;
//					switchLanguage(locale);
//					Intent intent = new Intent(context, MainActivity.class);
//					intent.putExtra("curIndex", curIndex);
//					context.startActivity(intent);
//					((Activity)context).finish();
//				} else {
//				}
				if(locationRunnable != null) {
					locationRunnable.isStop();
					locationRunnable = null;
				}
				String latitude = SharePreferenceHelper.getSharepreferenceString(context, 
						Contant.SETTINGSP, Contant.PREF_LATITUDE, null);
				
				String longitude = SharePreferenceHelper.getSharepreferenceString(context, 
						Contant.SETTINGSP, Contant.PREF_LONGITUDE, null);
				if(latitude == null || longitude == null) {
					REActivityManager.getActivityManager().exit();
				}
				locationRunnable = new LocationRunnable(context, handler, Double.parseDouble(latitude), Double.parseDouble(longitude), languageParameter);
				((SettingActivity) context).showProgress();
				new Thread(locationRunnable).start();
			}
		}).create().show();
	}
	
	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			((SettingActivity) context).dismissProgress();
			
			String msgInfo = context.getString(R.string.msg_local_error);
			boolean isSuccess = false;
			if (msg.what == 5) {
				if(msg.obj != null) {
					LocationEntity entity = (LocationEntity) msg.obj;
					if(entity != null) {
						Contant.curLocationEntity = entity;
						isSuccess = true;
						msgInfo = context.getString(R.string.msg_local_success);
						// 将当前的定位信息保存下来
						SharePreferenceHelper.setSharepreferenceString(context, 
								Contant.SETTINGSP, Contant.PREF_COUNTRY, entity.getCountry());
						
						SharePreferenceHelper.setSharepreferenceString(context, 
								Contant.SETTINGSP, Contant.PREF_CITY, entity.getAdministrative());
						
						SharePreferenceHelper.setSharepreferenceString(context, 
								Contant.SETTINGSP, Contant.PREF_LOCALITY, entity.getLocality());
						
						SharePreferenceHelper.setSharepreferenceString(context, 
								Contant.SETTINGSP, Contant.PREF_STREET, entity.getRegion());
						
						SharePreferenceHelper.setSharepreferenceString(context, 
								Contant.SETTINGSP, Contant.PREF_DETAIL, entity.getDetailInfo());
						
						SharePreferenceHelper.setSharepreferenceString(context, 
								Contant.SETTINGSP, Contant.PREF_LATITUDE, entity.getLatitude()+"");
						
						SharePreferenceHelper.setSharepreferenceString(context, 
								Contant.SETTINGSP, Contant.PREF_LONGITUDE, entity.getLongitude()+"");
						
						SharePreferenceHelper.setSharepreferenceString(context, Contant.SETTINGSP, Contant.PREF_LANGUAGE, language);
					}
				}
			}
			Toast.makeText(context, msgInfo, Toast.LENGTH_SHORT).show();
			Contant.LANGUAGE_PARAMETER = languageParameter;
			switchLanguage(locale);
			Intent intent = new Intent(context, MainActivity.class);
			context.startActivity(intent);
			((Activity)context).finish();
//			if(isSuccess) {
//			}
		};
	};
	
	/**
	 * 选择语言
	 * @param locale
	 */
	public void switchLanguage(Locale locale) {
        Resources resources = context.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; // 简体中文
        resources.updateConfiguration(config, dm);
    }
	
	private void clearHis() {
		new AlertDialog.Builder(context)
		.setTitle(R.string.btn_clear)
		.setMessage(R.string.msg_clear_temp)
		.setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cacheRunnable != null)
					cacheRunnable.isStop();
				cacheRunnable = new CacheRunnable(RockEagleApp.app.HistoryPath, cacheHandler);
				new Thread(cacheRunnable).start();
			}
		}).create().show();
	}
	
	/**
	 * 通过指定isBool值，清空收藏/浏览记录
	 * @param isBool		
	 * @param resContent	弹出框的提示信息
	 */
	private void clearStore(String resContent) {
		new AlertDialog.Builder(context)
		.setTitle(R.string.btn_clear)
		.setMessage(resContent+"?")
		.setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String info = context.getString(R.string.msg_clear_error);
				if(historyService.deletes() > -1){
					info = context.getString(R.string.msg_clear_success);
				}
				Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
			}
		}).create().show();
	}
	
	/**
	 * 清空缓存文件
	 */
	private void clearCache() {
		new AlertDialog.Builder(context)
		.setTitle(R.string.btn_clear)
		.setMessage(R.string.msg_clear_cache)
		.setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(cacheRunnable != null)
					cacheRunnable.isStop();
				cacheRunnable = new CacheRunnable(RockEagleApp.app.CachePath, cacheHandler);
				new Thread(cacheRunnable).start();
			}
		}).create().show();
	}
	
	private class CacheRunnable extends REBaseRunnable {

		private String path;
		
		public CacheRunnable(String path, Handler handler) {
			super(handler);
			this.path = path;
		}

		@Override
		public void run() {
			String info = context.getString(R.string.msg_clear_error);
			int what = -1;
			if(REFileHandle.deleteDir(path, false)){
				info = context.getString(R.string.msg_clear_success);
				what = 0;
			}
			sendMessage(what, info);
		}
	}
	
	private Handler cacheHandler = new Handler(){
		public void handleMessage(Message msg) {
			String info = (String) msg.obj;
			Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		};
	};

	public List<SettingEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<SettingEntity> entities) {
		this.entities = entities;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}
	
}
