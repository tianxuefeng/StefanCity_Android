package com.stefan.city.ui.page.view;

import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.CategoryManActivity;
import com.stefan.city.FavoriteActivity;
import com.stefan.city.HistoryActivity;
import com.stefan.city.LoginActivity;
import com.stefan.city.R;
import com.stefan.city.SearchActivity;
import com.stefan.city.UserDetailActivity;
import com.stefan.city.UserManageActivity;
import com.stefan.city.UserSendListActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.UserEntity;
import com.stefan.city.module.thread.UserLoginRunnable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * PageUserView
 * @author 日期：2014-4-12下午12:38:57
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class PageUserView extends BasePageView {
	// 用户登陆
//	private View viewLogin;
//	private EditText editEmail, editPawd;
	private Button /*btnLogin, */btnLogout, btnRegister;
	
	private TextView labUsername;
	// 用户中心
//	private View viewUserInfo;
	
	// 发布记录
	private View labUserSends, labHistory, labFavorite, labCategoryManage, 
			labSearch, labUserManage;
	
	private UserLoginRunnable userLoginRunnable;

	public PageUserView(Context context) {
		super(context);
		initLayout();
	}
	
	private void initLayout() {
		View view = inflate(getContext(), R.layout.page_user, null);
//		viewLogin = view.findViewById(R.id.layout_user_login);
		
		labUsername = (TextView) view.findViewById(R.id.lab_user_name);
		labUsername.setOnClickListener(clickListener);
		
		btnRegister = (Button) view.findViewById(R.id.btn_user_login);
		btnRegister.setOnClickListener(clickListener);
		
//		viewUserInfo = view.findViewById(R.id.layout_userinfo);
		btnLogout = (Button) view.findViewById(R.id.btn_logout);
		
//		btnLogin.setOnClickListener(clickListener);
		btnLogout.setOnClickListener(clickListener);
		
		labUserSends = view.findViewById(R.id.lab_user_send);
		labUserSends.setOnClickListener(clickListener);
		
		labHistory = view.findViewById(R.id.lab_user_history);
		labHistory.setOnClickListener(clickListener);
		
		labFavorite = view.findViewById(R.id.lab_user_favorites);
		labFavorite.setOnClickListener(clickListener);
		
		labCategoryManage = view.findViewById(R.id.lab_catrgory_manage);
		labCategoryManage.setOnClickListener(clickListener);
		
		labUserManage = view.findViewById(R.id.lab_user_manage);
		labUserManage.setOnClickListener(clickListener);
		
		labSearch = view.findViewById(R.id.lab_user_search);
		labSearch.setOnClickListener(clickListener);
		
		addView(view);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.lab_user_manage: {
				// 用户需要登录
				if(Contant.curUser == null) {
					Toast.makeText(getContext(), R.string.msg_not_login, Toast.LENGTH_SHORT).show();
					return ;
				} else {
					// 权限不足
					if(Contant.curUser.getMemType() < 3) {
						Toast.makeText(getContext(), R.string.msg_operate_authority_lacking, Toast.LENGTH_SHORT).show();
						return ;
					} else {
						// 进入用户管理
						Intent intent = new Intent(getContext(), UserManageActivity.class);
						getContext().startActivity(intent);
					}
				}
			}
				break;
//				
			case R.id.lab_user_search :
				toSearch();
				break;
				
			case R.id.lab_user_name :
				toDetailUser();
				break;
			
			case R.id.btn_logout:
				if(Contant.curUser == null) {
					return ;
				}
				toLogout();
				break;
				
			case R.id.btn_user_login:{
				Intent intent = new Intent(getContext(), LoginActivity.class);
				getContext().startActivity(intent);
			}
				break;
				
			case R.id.lab_user_favorites:{
				// 进入我的收藏
				if(Contant.curUser == null) {
					Toast.makeText(getContext(), R.string.msg_not_login, Toast.LENGTH_SHORT).show();
					return ;
				}
				Intent intent = new Intent(getContext(), FavoriteActivity.class);
				getContext().startActivity(intent);
			}
				break;
				
			case R.id.lab_user_send:{
				// 我的发布
				if(Contant.curUser == null) {
					Toast.makeText(getContext(), R.string.msg_not_login, Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(getContext(), UserSendListActivity.class);
					getContext().startActivity(intent);
				}
			}
				break;
				
			case R.id.lab_user_history:{
				// 进入浏览记录
				Intent intent = new Intent(getContext(), HistoryActivity.class);
				getContext().startActivity(intent);
			}
				break;

			case R.id.lab_catrgory_manage: {
				// 进入分类管理
				if(Contant.curUser == null) {
					Toast.makeText(getContext(), R.string.msg_not_login, Toast.LENGTH_SHORT).show();
				} else {
					// 权限不足
					if(Contant.curUser.getMemType() < 3) {
						Toast.makeText(getContext(), R.string.msg_operate_authority_lacking, Toast.LENGTH_SHORT).show();
						return ;
					} else {
						Intent intent = new Intent(getContext(), CategoryManActivity.class);
						getContext().startActivity(intent);
						((Activity) getContext()).finish();
					}
				}
			}
				break;
			default:
				break;
			}
		}
	};
	
	private void toDetailUser() {
		if(Contant.curUser != null) {
			Intent intent = new Intent(getContext(), UserDetailActivity.class);
			intent.putExtra("userId", Contant.curUser.getId());
			getContext().startActivity(intent);
		}
	}
	
	private void toSearch() {
		Intent intent = new Intent(getContext(), SearchActivity.class);
		getContext().startActivity(intent);
	}
	
//	private void toLogin() {
//		String email = editEmail.getText().toString();
//		if(email == null || email.equals("")) {
//			editEmail.setFocusable(true);
//			Toast.makeText(getContext(), R.string.msg_login_not_email, Toast.LENGTH_SHORT).show();
//			return ;
//		}/* else {
//			if(!email.matches("\\w+@\\w+\\.\\w+")) {
//				Toast.makeText(getContext(), R.string.msg_login_check_email, Toast.LENGTH_SHORT).show();
//				return ;
//			}
//		}*/
//		String pawd = editPawd.getText().toString();
//		if(pawd == null || pawd.equals("")) {
//			editPawd.setFocusable(true);
//			Toast.makeText(getContext(), R.string.msg_login_not_pawd, Toast.LENGTH_SHORT).show();
//			return ;
//		}
//		if(userLoginRunnable != null) {
//			userLoginRunnable.isStop();
//			userLoginRunnable = null;
//		}
//		btnLogin.setEnabled(false);
//		btnLogin.setText(R.string.btn_loging);
//		userLoginRunnable = new UserLoginRunnable(handler, email, pawd);
//		
//		new Thread(userLoginRunnable).start();
//	}
	
	private void toLogout() {
		Contant.curUser = null;
//		viewLogin.setVisibility(View.VISIBLE);
//		viewUserInfo.setVisibility(View.GONE);
		SharePreferenceHelper.setSharepreferenceString(getContext(), Contant.SETTINGSP, UserEntity.FIELD_EMAIL, null);
		SharePreferenceHelper.setSharepreferenceString(getContext(), Contant.SETTINGSP, UserEntity.FIELD_PAWD, null);
		SharePreferenceHelper.setSharepreferenceString(getContext(), Contant.SETTINGSP, UserEntity.FIELD_ID, null);
		btnLogout.setVisibility(View.GONE);
		
		labUsername.setVisibility(View.GONE);
		btnRegister.setVisibility(View.VISIBLE);
	}

	@Override
	protected void loadData() {}

	@Override
	protected void initVal() {}

	@Override
	protected void pageLoadData() {}

	@Override
	public void onDestroy() {
		if(userLoginRunnable != null) {
			userLoginRunnable.isStop();
		}
	}

	@Override
	public void onResume() {
		if(Contant.curUser != null) {
			btnLogout.setVisibility(View.VISIBLE);
//			viewUserInfo.setVisibility(View.VISIBLE);
			labUsername.setText(Contant.curUser.getName());
			labUsername.setVisibility(View.VISIBLE);
			btnRegister.setVisibility(View.GONE);
		} else {
			btnLogout.setVisibility(View.GONE);
//			viewUserInfo.setVisibility(View.GONE);
			labUsername.setVisibility(View.GONE);
			btnRegister.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onResume(boolean isLoad) {
		if(isLoad) {
			onResume();
		}
		if(Contant.curUser != null) {
			labUsername.setText(Contant.curUser.getName());
		}
	}
	
}
