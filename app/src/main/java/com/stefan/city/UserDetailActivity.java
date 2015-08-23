/**
 * 
 */
package com.stefan.city;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rockeagle.framework.core.REActivity;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.RECallback;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.thread.UserChangePawdRunnable;
import com.stefan.city.module.thread.UserDetailRunnable;
import com.stefan.city.module.thread.UserUpdateRunnable;
import com.stefan.city.ui.dialog.DialogChangePawd;

/**
 * UserDetailActivity
 * 用户详细信息
 * @author 日期：2015年7月2日下午5:19:59
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class UserDetailActivity extends REActivity {

	
	private EditText editName, editEmail, editPhone, editQQSkype,
			editMemType;
	
	private TextView labChanagePawd;
	
	private Button btnUpdate, btnBack, btnQuit;
	
	private String userId;
	private UserEntity userEntity;
	
	private boolean isIntent;
	
	private UserDetailRunnable userDetailRunnable;
	
	private UserUpdateRunnable userUpdateRunnable;
	
	private UserChangePawdRunnable changePawdRunnable; 
	
	private DialogChangePawd dialogChangePawd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
		isIntent = true;
		initLayout();
	}
	
	private void initLayout() {
		editName = (EditText) findViewById(R.id.edit_user_detail_name);
		labChanagePawd = (TextView) findViewById(R.id.edit_user_detail_pawd);
		labChanagePawd.setOnClickListener(clickListener);
		editPhone = (EditText) findViewById(R.id.edit_user_detail_phone);
		editEmail = (EditText) findViewById(R.id.edit_user_detail_email);
		editQQSkype = (EditText) findViewById(R.id.edit_user_detail_qqskype);
		editMemType = (EditText) findViewById(R.id.edit_user_detail_memType);
		
		btnBack = (Button) findViewById(R.id.btn_user_detail_back);
		btnUpdate = (Button) findViewById(R.id.btn_user_detail_update);
		btnQuit = (Button) findViewById(R.id.btn_user_detail_quit);
		
		btnBack.setOnClickListener(clickListener);
		btnUpdate.setOnClickListener(clickListener);
		btnQuit.setOnClickListener(clickListener);
		
		dialogChangePawd = new DialogChangePawd(UserDetailActivity.this, callback);
	}
	
	private RECallback callback = new RECallback() {
		
		@Override
		public void toCallback(Object obj) {
			if(obj != null) {
				String[] strings = (String[]) obj;
				if(changePawdRunnable != null) {
					changePawdRunnable.isStop();
					changePawdRunnable = null;
				}
				changePawdRunnable = new UserChangePawdRunnable(changePawdHandler, userId, strings);
				
				showDialog(DIALOG_LOAD);
				new Thread(changePawdRunnable).start();
			}
		}
	};
	
	private Handler changePawdHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1) {
				if(Contant.curUser != null && userEntity.getId().equals(Contant.curUser.getId())) {
					Contant.curUser = new UserEntity(userEntity);
					SharePreferenceHelper.setSharepreferenceString(UserDetailActivity.this, Contant.SETTINGSP, UserEntity.FIELD_EMAIL, Contant.curUser.getEmail());
					SharePreferenceHelper.setSharepreferenceString(UserDetailActivity.this, Contant.SETTINGSP, UserEntity.FIELD_PAWD, Contant.curUser.getPawd());
					SharePreferenceHelper.setSharepreferenceString(UserDetailActivity.this, Contant.SETTINGSP, UserEntity.FIELD_ID, Contant.curUser.getId());
				}
				Toast.makeText(UserDetailActivity.this, R.string.msg_edit_success, Toast.LENGTH_SHORT).show();
			} else if(msg.what == -1) {
				Toast.makeText(UserDetailActivity.this, R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(UserDetailActivity.this, R.string.msg_operate_error, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_user_detail_back:
			case R.id.btn_user_detail_quit:
				UserDetailActivity.this.finish();
				break;
				
			case R.id.btn_user_detail_update:
				// 修改用户信息
				update();
				break;
				
			case R.id.edit_user_detail_pawd:
				// 修改用户密码
				changePawd();
				break;

			default:
				break;
			}
		}
	};
	
	
	private void changePawd() {
		dialogChangePawd.show();
	}
	
	private void update() {
		String name = editName.getText().toString();
		if(name == null || name.equals("")) {
			editName.setFocusable(true);
			Toast.makeText(UserDetailActivity.this, R.string.msg_send_not_name, Toast.LENGTH_SHORT).show();
			return ;
		}
		String email = editEmail.getText().toString();
		if(email == null || email.equals("")) {
			editEmail.setFocusable(true);
			Toast.makeText(UserDetailActivity.this, R.string.msg_login_not_email, Toast.LENGTH_SHORT).show();
			return ;
		} else {
			if(!Contant.isEmail(email)) {
				editEmail.setFocusable(true);
				Toast.makeText(UserDetailActivity.this, R.string.msg_login_check_email, Toast.LENGTH_SHORT).show();
				return ;
			}
		}
		String phone = editPhone.getText().toString();
		if(phone == null || phone.equals("")) {
			editPhone.setFocusable(true);
			Toast.makeText(UserDetailActivity.this, R.string.msg_send_not_phone, Toast.LENGTH_SHORT).show();
			return ;
		}
		String qq = editQQSkype.getText().toString();
		UserEntity entity = new UserEntity(userEntity);
		entity.setEmail(email);
		entity.setName(name);
//		if(pawd != null && !pawd.equals("")) {
//			String pawdMd5 = RESecretUtility.getMD5Str(pawd);
//			entity.setPawd(pawdMd5);
//		}
		entity.setPhone(phone);
		entity.setQqSkype(qq);
		userEntity = new UserEntity(entity);
		if(userUpdateRunnable != null) {
			userUpdateRunnable.isStop();
			userUpdateRunnable = null;
		}
		userUpdateRunnable = new UserUpdateRunnable(updateHandler, entity);
		showDialog(DIALOG_LOAD);
		new Thread(userUpdateRunnable).start();
	}
	
	private Handler updateHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1) {
				Toast.makeText(UserDetailActivity.this, R.string.msg_edit_success, Toast.LENGTH_SHORT).show();
			} else if(msg.what == 0) {
				Toast.makeText(UserDetailActivity.this, R.string.msg_edit_error, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(UserDetailActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private void updateView() {
		if(userEntity != null) {
			editName.setText(userEntity.getName());
			editEmail.setText(userEntity.getEmail());
//			editPawd.setText(userEntity.getPawd());
			editPhone.setText(userEntity.getPhone());
			editQQSkype.setText(userEntity.getQqSkype());
			String memType = null;
			switch (userEntity.getMemType()) {
			case 0:
				memType = getString(R.string.lab_user_memtype_0);
				break;
				
			case 1:
				memType = getString(R.string.lab_user_memtype_1);
				break;

			default:
				memType = getString(R.string.lab_user_memtype_2);
				break;
			}
			editMemType.setText(memType);
		}
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
				userId = bundle.getString("userId");
				loadData();
			}
		}
	}
	
	private void loadData() {
		if(userDetailRunnable != null) {
			userDetailRunnable.isStop();
			userDetailRunnable = null;
		}
		userDetailRunnable = new UserDetailRunnable(handler, userId);
		showDialog(DIALOG_LOAD);
		new Thread(userDetailRunnable).start();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.what == 1) {
				if(msg.obj != null) {
					userEntity = (UserEntity) msg.obj;
					updateView();
				}
			} else {
				// 检查网络连接
				Toast.makeText(UserDetailActivity.this, R.string.msg_net_notinfo, Toast.LENGTH_SHORT).show();
			}
		};
	};
}
