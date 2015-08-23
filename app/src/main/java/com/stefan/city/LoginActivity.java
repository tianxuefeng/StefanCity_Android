package com.stefan.city;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.thread.UserLoginRunnable;

/**
 * LoginActivity
 * 用户登陆
 * @author 日期：2014-7-25上午10:34:47
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class LoginActivity extends BaseActivity {
	
	private Button btnLogin, btnRegister;
	private EditText editEmail, editPawd;
	
	private UserLoginRunnable userLoginRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.activity_login);
		editEmail = (EditText) findViewById(R.id.edit_login_email);
		editPawd = (EditText) findViewById(R.id.edit_login_pawd);
		btnLogin = (Button) findViewById(R.id.btn_login_submit);
		btnLogin.setOnClickListener(clickListener);
		
		btnRegister = (Button) findViewById(R.id.btn_login_register);
		btnRegister.setOnClickListener(clickListener);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_login_register:
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
				break;
				
			case R.id.btn_login_submit:
				toLogin();
				break;

			default:
				break;
			}
		}
	};
	
	private void toLogin() {
		String email = editEmail.getText().toString();
		if(email == null || email.equals("")) {
			editEmail.setFocusable(true);
			Toast.makeText(LoginActivity.this, R.string.msg_login_not_email, Toast.LENGTH_SHORT).show();
			return ;
		}/* else {
			if(!email.matches("\\w+@\\w+\\.\\w+")) {
				Toast.makeText(getContext(), R.string.msg_login_check_email, Toast.LENGTH_SHORT).show();
				return ;
			}
		}*/
		String pawd = editPawd.getText().toString();
		if(pawd == null || pawd.equals("")) {
			editPawd.setFocusable(true);
			Toast.makeText(LoginActivity.this, R.string.msg_login_not_pawd, Toast.LENGTH_SHORT).show();
			return ;
		}
		showDialog(DIALOG_LOAD);
		if(userLoginRunnable != null) {
			userLoginRunnable.isStop();
			userLoginRunnable = null;
		}
		btnLogin.setEnabled(false);
		btnLogin.setText(R.string.btn_loging);
		userLoginRunnable = new UserLoginRunnable(handler, email, pawd);
		
		new Thread(userLoginRunnable).start();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			btnLogin.setEnabled(true);
			btnLogin.setText(R.string.btn_login);
			if(msg.obj != null) {
				Contant.curUser = (UserEntity) msg.obj;
				editPawd.setText("");
				btnRegister.setVisibility(View.GONE);
				SharePreferenceHelper.setSharepreferenceString(LoginActivity.this, Contant.SETTINGSP, UserEntity.FIELD_EMAIL, Contant.curUser.getEmail());
				SharePreferenceHelper.setSharepreferenceString(LoginActivity.this, Contant.SETTINGSP, UserEntity.FIELD_PAWD, Contant.curUser.getPawd());
				SharePreferenceHelper.setSharepreferenceString(LoginActivity.this, Contant.SETTINGSP, UserEntity.FIELD_ID, Contant.curUser.getId());
				Toast.makeText(LoginActivity.this, R.string.msg_login_success, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				intent.putExtra("viewIndex", Contant.VIEW_USER);
				startActivity(intent);
				LoginActivity.this.finish();
			} else if(msg.obj == null && msg.what != 1) {
				Toast.makeText(LoginActivity.this, R.string.msg_login_error, Toast.LENGTH_SHORT).show();
			}
		};
	};
}
