package com.stefan.city;

import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rockeagle.framework.core.REActivityManager;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.ui.layout.REScrollLayout;
import com.stefan.city.ui.listener.OnViewChangeListener;

/**
 * WelcomeActivity
 * 	第一次安装或者更新版本后的引导界面
 * @author 日期：2014-6-24上午11:22:59
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class WelcomeActivity extends BaseActivity 
					implements OnViewChangeListener {
	
	private REScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private Button startBtn;
	private LinearLayout pointLLayout;
	// 选择语言
	private RadioGroup languageGroup;
	// 当前选中的语言
	private String curLanguage;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }
    
	private void initView() {
		mScrollLayout  = (REScrollLayout) findViewById(R.id.welcomeConlayout);
		pointLLayout = (LinearLayout) findViewById(R.id.llayout);
		pointLLayout.setVisibility(View.GONE);
//		mainRLayout = (RelativeLayout) findViewById(R.id.welcomeLayout);
		
		languageGroup = (RadioGroup) findViewById(R.id.welcomeSelLanguage);
		
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(onClick);
		count = mScrollLayout.getChildCount();
		imgs = new ImageView[count];
		for(int i = 0; i< count; i++) {
			imgs[i] = (ImageView) pointLLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}
	
	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
				mScrollLayout.setVisibility(View.GONE);
				pointLLayout.setVisibility(View.GONE);
				// 存储用户选中的语言
				int radioButtonId = languageGroup.getCheckedRadioButtonId();
				//根据ID获取RadioButton的实例
				RadioButton radioButton = (RadioButton)WelcomeActivity.this.findViewById(radioButtonId);
				curLanguage = radioButton.getTag().toString();
				if(curLanguage.equalsIgnoreCase("zh-hans")) {
					Contant.LANGUAGE_PARAMETER = "zh-CN";
					switchLanguage(Locale.CHINA);
				} else if(curLanguage.equalsIgnoreCase("zh-Hant")) {
					Contant.LANGUAGE_PARAMETER = "zh";
					switchLanguage(Locale.CHINESE);
				} else if(curLanguage.equalsIgnoreCase("en")) {
					switchLanguage(Locale.ENGLISH);
				}
				SharePreferenceHelper.setSharepreferenceString(WelcomeActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, curLanguage);
				Intent intent = new Intent(WelcomeActivity.this, GPSLocationActivity.class);
				intent.putExtra("isMain", true);
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
				break;
			}
		}
	};
	
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

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if(position < 0 || position > count -1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			REActivityManager.getActivityManager().exit();
		}
		return super.onKeyDown(keyCode, event);
	}
}
