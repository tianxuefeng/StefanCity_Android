package com.stefan.city;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.thread.RegisterRunnable;

/**
 * RegisterActivity
 * 用户注册
 * @author 日期：2015-3-9下午11:16:40
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class RegisterActivity extends BaseActivity {
	
	private EditText editEmail, editName, editPawd, editPawd2, editPhone, editQQ;
	
	private Button btnBack, btnSubmit;
	
	private RegisterRunnable registerRunnable;
	
	private boolean isIntent = false;
	
	private int memType = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		setContentView(R.layout.activity_register);
		initLayout();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		isIntent = true;
		setIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			Bundle bundle = getIntent().getExtras();
    		if(bundle != null) {
    			memType = bundle.getInt("memType", 1);
    		}
		}
	}
	
	private void initLayout() {
		editEmail = (EditText) findViewById(R.id.edit_register_email);
		editName = (EditText) findViewById(R.id.edit_registerName);
		editPawd = (EditText) findViewById(R.id.edit_register_pawd);
		editPawd2 = (EditText) findViewById(R.id.edit_register_pawd2);
		editPhone = (EditText) findViewById(R.id.edit_registerPhone);
		editQQ = (EditText) findViewById(R.id.edit_registerQQ);
		
		btnBack = (Button) findViewById(R.id.btn_userRegister_back);
		btnBack.setOnClickListener(clickListener);
		btnSubmit = (Button) findViewById(R.id.btn_register_submit);
		btnSubmit.setOnClickListener(clickListener);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_userRegister_back:
				RegisterActivity.this.finish();
				break;
				
			case R.id.btn_register_submit: 
				toSubmit();
				break;

			default:
				break;
			}
		}
	};
	
	private void toSubmit() {
		String name = editName.getText().toString();
		if(name == null || name.equals("")) {
			editName.setFocusable(true);
			Toast.makeText(RegisterActivity.this, R.string.msg_send_not_name, Toast.LENGTH_SHORT).show();
			return ;
		}
		String email = editEmail.getText().toString();
		if(email == null || email.equals("")) {
			editEmail.setFocusable(true);
			Toast.makeText(RegisterActivity.this, R.string.msg_login_not_email, Toast.LENGTH_SHORT).show();
			return ;
		} else {
			if(!isEmail(email)) {
				editEmail.setFocusable(true);
				Toast.makeText(RegisterActivity.this, R.string.msg_login_check_email, Toast.LENGTH_SHORT).show();
				return ;
			}
		}
		String pawd = editPawd.getText().toString();
		if(pawd == null || pawd.equals("")) {
			editPawd.setFocusable(true);
			Toast.makeText(RegisterActivity.this, R.string.msg_login_not_pawd, Toast.LENGTH_SHORT).show();
			return ;
		}
		String pawd2 = editPawd2.getText().toString();
		if(pawd2 == null || pawd2.equals("")) {
			editPawd2.setFocusable(true);
			Toast.makeText(RegisterActivity.this, R.string.msg_login_not_pawd, Toast.LENGTH_SHORT).show();
			return ;
		}
		if(!pawd.equals(pawd2)) {
			editPawd2.setFocusable(true);
			Toast.makeText(RegisterActivity.this, R.string.msg_register_not_pawd_error, Toast.LENGTH_SHORT).show();
			return ;
		}
		String phone = editPhone.getText().toString();
		if(phone == null || phone.equals("")) {
			editPhone.setFocusable(true);
			Toast.makeText(RegisterActivity.this, R.string.msg_send_not_phone, Toast.LENGTH_SHORT).show();
			return ;
		}
		String qq = editQQ.getText().toString();
		showDialog(DIALOG_LOAD);
		UserEntity entity = new UserEntity(name, pawd, email, "", phone, qq, memType, Contant.curLocationEntity.getAdministrative(), "");
		if(registerRunnable != null) {
			registerRunnable.isStop();
			registerRunnable = null;
		}
		btnSubmit.setEnabled(false);
		btnSubmit.setText(R.string.btn_register_signin);
		registerRunnable = new RegisterRunnable(handler, entity, true);
		new Thread(registerRunnable).start();
	}
	
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			btnSubmit.setText(R.string.btn_register);
			btnSubmit.setEnabled(true);
			if(msg.what == 1) {
				Toast.makeText(RegisterActivity.this, R.string.msg_register_success, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
				intent.putExtra("viewIndex", Contant.VIEW_USER);
				startActivity(intent);
				RegisterActivity.this.finish();
			} else if(msg.what == -2) {
				Toast.makeText(RegisterActivity.this, R.string.msg_register_success_login_error, Toast.LENGTH_SHORT).show();
				RegisterActivity.this.finish();
			} else if(msg.what == -1) {
				Toast.makeText(RegisterActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(RegisterActivity.this, R.string.msg_register_error, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
}
