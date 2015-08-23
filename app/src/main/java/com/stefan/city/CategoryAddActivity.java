package com.stefan.city;

import java.io.File;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.rockeagle.framework.core.files.REFileHandle;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.thread.CategoryAddRunnable;

/**
 * CategoryAddActivity
 * 添加分类信息
 * @author 日期：2014-9-9下午08:31:02
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class CategoryAddActivity extends BaseActivity {
	
	private CategoryAddRunnable categoryAddRunnable;
	private CategoryEntity parent;
	
	private EditText editName, editParent, editDesc, editUser, editSequence;
	
	private RadioButton radioType0, radioType1;
	
	private View btnAddImg;
	private ImageButton btnDeleteImg;
	private ImageView imgIcon;
	
	private Button btnBack, btnSubmit;
	
	private String imgPath;
	
	private boolean isIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIntent = true;
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.activity_add_category);
		editName = (EditText) findViewById(R.id.edit_category_man_add_name);
		editDesc = (EditText) findViewById(R.id.edit_category_man_add_desc);
		editParent = (EditText) findViewById(R.id.edit_category_man_add_parent_name);
		editSequence = (EditText) findViewById(R.id.edit_category_man_add_sequence);
		editUser = (EditText) findViewById(R.id.edit_category_man_add_user);
		
		btnBack = (Button) findViewById(R.id.btn_add_category_back);
		
		btnSubmit = (Button) findViewById(R.id.btn_category_add_submit);
		
		btnBack.setOnClickListener(clickListener);
		btnSubmit.setOnClickListener(clickListener);
		
		btnAddImg = findViewById(R.id.view_category_add_img);
		btnDeleteImg = (ImageButton) findViewById(R.id.category_add_img_delete);
		btnDeleteImg.setOnClickListener(clickListener);
		
		imgIcon = (ImageView) findViewById(R.id.category_add_imgs);
//		imgIcon.setBackgroundColor(0xffdfdfdf);
		imgIcon.setOnClickListener(clickListener);
		
		radioType0 = (RadioButton) findViewById(R.id.radio_category_man_add_type0);
		radioType1 = (RadioButton) findViewById(R.id.radio_category_man_add_type1);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_add_category_back:
				gotoManage(false);
				break;
				
			case R.id.btn_category_add_submit:
				// 提交添加的信息
				toSubmit();
				break;
				
			case R.id.category_add_img_delete:
				if(imgPath == null) {
					return ;
				}
				// 删除图标
				imgIcon.setImageResource(R.drawable.icon_send);
				imgPath = null;
				btnDeleteImg.setVisibility(View.GONE);
				break;
				
			case R.id.category_add_imgs:
				// 选择图片
				selectedImg();
				break;
				
			default:
				break;
			}
		}
	};
	
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		isIntent = true;
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isIntent) {
			isIntent = false;
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				Object obj = bundle.get("parentCatetory");
				if(obj != null) {
					parent = (CategoryEntity) obj;
					editParent.setText(parent.getTitle());
				}
			}
		}
		if(imgPath != null && !imgPath.equals("")) {
			btnDeleteImg.setVisibility(View.VISIBLE);
		} else {
			btnDeleteImg.setVisibility(View.GONE);
		}
		if(Contant.curUser != null) {
			editUser.setText(Contant.curUser.getName());
		}
	}
	
	/**
	 * 选择图片
	 */
	private void selectedImg() {
		// 建立"选择档案Action" 的Intent
		Intent intent = new Intent(Intent.ACTION_PICK);
		// 过滤档案格式
		intent.setType("image/*");
		// 建立"档案选择器" 的Intent (第二个参数: 选择器的标题)
		Intent destIntent = Intent.createChooser(intent, getText(R.string.btn_select_file));
		// 切换到档案选择器(它的处理结果, 会触发onActivityResult 事件)
		startActivityForResult(destIntent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 有选择档案
		if (resultCode == RESULT_OK) {
			if(requestCode == 0) {
				// 取得档案的Uri
				Uri uri = data.getData();
				if (uri != null) {
					// 通过URI获取到绝对路径
					imgPath = getAbsoluteImagePath(uri);
					File file = new File(imgPath);
					BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
					// 只读取图片的大小，不读取像素
					bitmapFactoryOptions.inJustDecodeBounds = true;
					Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapFactoryOptions);
					// 计算缩放比例
					int be = (int)(bitmapFactoryOptions.outWidth / (float)100);
			        if (be <= 0)
			            be = 1;
					bitmapFactoryOptions.inSampleSize = be;
					bitmapFactoryOptions.inJustDecodeBounds = false;
					bm = REFileHandle.getBitmapFD(file.getAbsolutePath(), bitmapFactoryOptions);
					
					imgIcon.setImageBitmap(bm);
					btnDeleteImg.setVisibility(View.VISIBLE);
				} else {
					btnDeleteImg.setVisibility(View.GONE);
					// 无效的图片
				}
			}
		}
	}
	
	private void toSubmit() {
		String desc = editDesc.getText().toString();
		if(desc == null || desc.equals("")) {
			editDesc.setFocusable(true);
			Toast.makeText(CategoryAddActivity.this, R.string.msg_send_not_desc, Toast.LENGTH_SHORT).show();
			return ;
		}
		String name = editName.getText().toString();
		if(name == null || name.equals("")) {
			editName.setFocusable(true);
			Toast.makeText(CategoryAddActivity.this, R.string.msg_send_not_name, Toast.LENGTH_SHORT).show();
			return ;
		}
//		if(imgPath == null || imgPath.equals("")) {
//			btnAddImg.setFocusable(true);
//			Toast.makeText(CategoryAddActivity.this, R.string.msg_select_file, Toast.LENGTH_SHORT).show();
//			return ;
//		}
		String sequence = editSequence.getText().toString();
		if(sequence == null || sequence.equals("")) {
			editSequence.setFocusable(true);
			Toast.makeText(CategoryAddActivity.this, R.string.msg_not_sequence, Toast.LENGTH_SHORT).show();
			return ;
		}
		
		boolean boolType = radioType0.isChecked();
		String type = "1";
		if(boolType) {
			type = "0";
		}
		
		showDialog(DIALOG_LOAD);
		String parentId = (parent == null) ? "0" : parent.getId();
		String language = SharePreferenceHelper.getSharepreferenceString(CategoryAddActivity.this, Contant.SETTINGSP, Contant.PREF_LANGUAGE, "zh-Hans");
		CategoryEntity categoryEntity = new CategoryEntity(null, name, desc, parentId, imgPath, type, sequence, language, Contant.curUser.getId(), null, null, null);
		if(categoryAddRunnable != null) {
			categoryAddRunnable.isStop();
			categoryAddRunnable = null;
		}
		categoryAddRunnable = new CategoryAddRunnable(handler, categoryEntity);
		new Thread(categoryAddRunnable).start();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			String message = null;
			switch (msg.what) {
			case 1:
				// 添加成功
				message = getString(R.string.msg_add_success);
				gotoManage(true);
				break;

			case -1:
				// 网络连接失败
				message = getString(R.string.msg_not_net);
				break;

			case -2:
				// 图片上传失败
				message = getString(R.string.msg_upload_img_error);
				break;
				
			case 0:
				// 添加失败
				message = getString(R.string.msg_add_error);
				break;

			default:
				break;
			}
			Toast.makeText(CategoryAddActivity.this, message, Toast.LENGTH_SHORT).show();
		};
	};
	
	private void gotoManage(boolean isUpdate) {
		Intent intent = new Intent(CategoryAddActivity.this, CategoryManActivity.class);
		if(isUpdate) {
			intent.putExtra("isUpdate", true);
		}
		startActivity(intent);
		CategoryAddActivity.this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			gotoManage(false);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	protected String getAbsoluteImagePath(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
}
