package com.stefan.city;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rockeagle.framework.core.RockEagleApp;
import com.rockeagle.framework.core.files.REFileHandle;
import com.rockeagle.framework.tools.SharePreferenceHelper;
import com.stefan.city.base.BaseActivity;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.CategoryEntity;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.entity.RegionManEntity;
import com.stefan.city.module.thread.ItemsEditRunnable;
import com.stefan.city.module.thread.RegionListRunnable;
import com.stefan.city.module.thread.RegionTwoListRunnable;
import com.stefan.city.ui.dialog.DialogSelectImg;
import com.stefan.city.ui.floatview.FloatLocatCityView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * SendActivity
 * 测试发布信息
 * @author 日期：2014-4-10下午09:31:37
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021 All Rights Reserved.
 **/
public class SendActivity extends BaseActivity {

	private Button btnSend, btnBack, btnSelParentCategory, btnSelCategory, btnLocal;
	
	private View btnAddImg;
	
	private TextView labCategory;
	
	private EditText editTitle, editDesc, editPrice, 
		editPhone, editContact, editQQ;
	
	private Button btnAddress, btnCity, btnStreet;
	
	private View layoutPrice, layoutPayType;
	
	private LayoutInflater inflater;
	
	private View modelView;
	private View showView;	// 用于弹出PopupWindow
	private DialogSelectImg dialogSelectImg;
	
	private LinearLayout viewPager;
	
	private RadioGroup radioGroup;

	private ItemsEditRunnable itemsEditRunnable;
	
	private boolean isIntent;
	private CategoryEntity categoryParentEntity;
	private CategoryEntity categoryEntity;
	
	private Long curTime;
	private String curImgPath;
	
	// 存放用户拍照或者选中的图片路径
	private Map<Long, String> imgMap;
	private Map<Long, View> imgViews;
	
	// 当前选中的地区
	private RegionManEntity regionCityEntity, regionAddress;
	private String selCity, selRegion, selStreet;
	
	private FloatLocatCityView floatLocatCityView/*, floatLocatRegionView, floatLocatStreetView*/;
	
	private InputMethodManager inputMethodManager;
	
	private RegionListRunnable regionListRunnable;
	
	private RegionTwoListRunnable regionTwoListRunnable;
	private String[] regionTwos;
	private RegionManEntity[] regionTwoEntitys;
	private List<RegionManEntity> cityList/*, regionList, streetList*/;
	
	@SuppressWarnings({ "rawtypes" })
	private Class activityClass;
	
	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		isIntent = true;
		
		imgMap = new HashMap<Long, String>();
		imgViews = new HashMap<Long, View>();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initLayout();
	}
	
	private void initLayout() {
		setContentView(R.layout.activity_send);
		viewPager = (LinearLayout) findViewById(R.id.send_imgsViewPager);
		inflater = LayoutInflater.from(SendActivity.this);
		btnAddImg = inflater.inflate(R.layout.send_add_image, null);
		btnAddImg.setOnClickListener(clickListener);
		viewPager.addView(btnAddImg);
		
		btnBack = (Button) findViewById(R.id.btn_send_back);
		btnBack.setOnClickListener(clickListener);
		
		btnSend = (Button) findViewById(R.id.btnToSend);
		btnSend.setOnClickListener(clickListener);
		
		btnLocal = (Button) findViewById(R.id.btn_localAddress);
		btnLocal.setOnClickListener(clickListener);
		
		btnSelParentCategory = (Button) findViewById(R.id.btn_sendParentCategory);
		btnSelParentCategory.setOnClickListener(clickListener);
		
		btnSelCategory = (Button) findViewById(R.id.btn_sendCategory);
		btnSelCategory.setOnClickListener(clickListener);
		
		labCategory = (TextView) findViewById(R.id.lab_send_title);
		
		editTitle = (EditText) findViewById(R.id.edit_sendTitle);
		btnAddress = (Button) findViewById(R.id.btn_sendAddress);
		
		btnCity = (Button) findViewById(R.id.btn_sendCity);
		btnStreet = (Button) findViewById(R.id.btn_sendStreet);
		
		btnCity.setOnClickListener(clickListener);
		btnAddress.setOnClickListener(clickListener);
		btnStreet.setOnClickListener(clickListener);
		
		editContact = (EditText) findViewById(R.id.edit_sendContact);
		editDesc = (EditText) findViewById(R.id.edit_sendDesc);
		editPhone = (EditText) findViewById(R.id.edit_sendPhone);
		editPrice = (EditText) findViewById(R.id.edit_sendPrice);
		editQQ = (EditText) findViewById(R.id.edit_sendQQ);
		
		btnSend = (Button) findViewById(R.id.btnToSend);
		
		layoutPrice = findViewById(R.id.layout_send_price);
		
		layoutPayType = findViewById(R.id.layout_send_pay_type);
		
		modelView = findViewById(R.id.send_view_Model);
		modelView.setOnClickListener(clickListener);
		
		radioGroup = (RadioGroup) findViewById(R.id.radio_sendPayType);
		
		showView = findViewById(R.id.send_showing_View);
		dialogSelectImg = new DialogSelectImg(SendActivity.this, dialogCall);
		
		floatLocatCityView = new FloatLocatCityView(SendActivity.this, new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 切换城市信息
				floatLocatCityView.dismiss();
				regionCityEntity = cityList.get(arg2);
				selCity = regionCityEntity.getName();
				btnCity.setText(selCity);
				// 清空已经选择的地区和街道
				selRegion = null;
				selStreet = null;
				btnAddress.setText(R.string.hint_sel_region);
				btnStreet.setText(R.string.hint_sel_street);
				cutoverAddress(regionHandler, selCity);
			}
		});
		floatLocatCityView.setView(btnCity);
		
//		floatLocatRegionView = new FloatLocatCityView(SendActivity.this, new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				floatLocatRegionView.dismiss();
//				// 切换地区信息
//				RegionManEntity entity = regionList.get(arg2);
//				selRegion = entity.getName();
//				btnAddress.setText(selRegion);
//				
//				selStreet = null;
//				btnStreet.setText(R.string.hint_sel_street);
//				
//				cutoverAddress(streetHandler, selRegion);
//			}
//		});
//		floatLocatRegionView.setView(btnAddress);
//		
//		floatLocatStreetView = new FloatLocatCityView(SendActivity.this, new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				floatLocatStreetView.dismiss();
//				// 切换地区信息
//				RegionManEntity entity = streetList.get(arg2);
//				selStreet = entity.getName();
//				btnStreet.setText(selStreet);
//			}
//		});
//		floatLocatStreetView.setView(btnStreet);
	}
	
	private void showCutoverAddress() {
		final Builder builder = new AlertDialog.Builder(this);
		builder.setItems(regionTwos, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(regionTwos != null && regionTwos.length > which) {
					String region = regionTwos[which].trim();
					regionAddress = regionTwoEntitys[which];
					
					btnAddress.setText(region);
				}
			}
		});
		builder.create().show();
	}
	
	/**
	 * 切换地区
	 * @param handler
	 * @param name
	 */
	@SuppressWarnings("deprecation")
	private void cutoverAddress(Handler handler, String name) {
		showDialog(DIALOG_LOAD);
		// 获得当前国家所有城市信息
		if(regionTwoListRunnable != null) {
			regionTwoListRunnable.isStop();
			regionTwoListRunnable = null;
		}
		String language = SharePreferenceHelper.getSharepreferenceString(
				SendActivity.this, Contant.SETTINGSP,
				Contant.PREF_LANGUAGE, "zh-Hans");
		regionTwoListRunnable = new RegionTwoListRunnable(handler, name, language);
		new Thread(regionTwoListRunnable).start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
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
			Bundle bundle = getIntent().getExtras();
			if(bundle != null) {
				Object objParent = bundle.get("categoryParentEntity");
				if(objParent != null) {
					categoryParentEntity = (CategoryEntity) objParent;
					btnSelParentCategory.setText(categoryParentEntity.getTitle());
				}
				Object category = bundle.get("categoryEntity");
				if(category != null) {
					categoryEntity = (CategoryEntity) category;
					btnSelCategory.setText(categoryEntity.getTitle());
				} else {
					if(objParent != null) {
						btnSelCategory.setText("");
					}
				}
				
				Object regionObj = bundle.get("regionManEntity");
				if(regionObj != null) {
					regionAddress = (RegionManEntity) regionObj;
					if(regionAddress != null) {
						selRegion = regionAddress.getName().trim();
						btnAddress.setText(selRegion);
					}
				}
				Object object = bundle.get("activityClass");
				if(activityClass == null) {
					if(object != null) {
						activityClass = (Class) object;
					} else {
						activityClass = InfoListActivity.class;
					}
				}
			}
			if(categoryParentEntity != null) {
				if(categoryParentEntity.getType().equals("1")) {
					layoutPrice.setVisibility(View.GONE);
					layoutPayType.setVisibility(View.GONE);
				} else {
					layoutPrice.setVisibility(View.VISIBLE);
					layoutPayType.setVisibility(View.VISIBLE);
				}
			}
			if(Contant.curRegionEntity != null && regionAddress == null) {
				regionAddress = Contant.curRegionEntity;
				selRegion = regionAddress.getName().trim();
				btnAddress.setText(selRegion);
			}
			if(Contant.curLocationEntity != null) {
				if(selCity == null || selCity.equals("")) {
					selCity = Contant.curLocationEntity.getAdministrative();
					btnCity.setText(selCity);
				}
				if(regionCityEntity == null) {
					regionCityEntity = new RegionManEntity();
					regionCityEntity.setName(selCity);
					regionCityEntity.setParentName(Contant.curLocationEntity.getCountry());
				}
			}
			if(Contant.curStreetRegion != null && (selStreet == null || selStreet.equals(""))) {
				selStreet = Contant.curStreetRegion.getName();
				btnStreet.setText(selStreet);
			}
			
			if(Contant.curUser != null) {
				editPhone.setText(Contant.curUser.getPhone());
				editContact.setText(Contant.curUser.getName());
				editQQ.setText(Contant.curUser.getQqSkype());
			}
			
			if(Contant.curLocationEntity != null) {
				// 获得当前国家所有城市信息
				showDialog(DIALOG_LOAD);
				if(regionListRunnable != null) {
					regionListRunnable.isStop();
					regionListRunnable = null;
				}
				String language = SharePreferenceHelper.getSharepreferenceString(
						SendActivity.this, Contant.SETTINGSP,
						Contant.PREF_LANGUAGE, "zh-Hans");
				regionListRunnable = new RegionListRunnable(cityHandler, Contant.curLocationEntity.getCountry(), language);
				new Thread(regionListRunnable).start();
				// 获得当前城市的地区信息
				if(Contant.curLocationEntity.getAdministrative() != null && (regionTwos == null)) {
					RegionTwoListRunnable regionListRunnable = new RegionTwoListRunnable(regionHandler, regionCityEntity.getName(), language);
					new Thread(regionListRunnable).start();
				}
			}
		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(v == btnAddImg) {
				if(modelView.getVisibility() == View.GONE) {
					dialogSelectImg.show(showView);
					modelView.setVisibility(View.VISIBLE);
				} else {
					modelView.setVisibility(View.GONE);
				}
			} else if(v == btnCity) {
				// 选择城市
				floatLocatCityView.showView();
			} else if(v == btnAddress) {
				showCutoverAddress();
			} else if(v == btnStreet) {
				// 选择街道信息
//				floatLocatStreetView.showView();
			} else if(v == btnBack) {
				SendActivity.this.finish();
			} else if(v == modelView) {
				modelView.setVisibility(View.GONE);
				dialogSelectImg.dismiss();
			} else if(v == btnSelParentCategory) {
				Intent intent = new Intent(SendActivity.this, SendCategoryActivity.class);
				startActivity(intent);
			} else if(v == btnSelCategory) {
				if(categoryParentEntity != null) {
					Intent intent = new Intent(SendActivity.this, SendCategoryChildActivity.class);
					intent.putExtra("categoryEntity", categoryParentEntity);
					startActivity(intent);
				} else {
					// 需要选择上级分类
					btnSelParentCategory.setFocusable(true);
					Toast.makeText(SendActivity.this, R.string.msg_send_not_category, Toast.LENGTH_SHORT).show();
					return ;
				}
			} else if(v == btnLocal) {
				toLocal();
			} else {
				switch (v.getId()) {
				case R.id.btnToSend:
					
					if(Contant.curUser != null && Contant.curUser.getMemType() < 1) {
						Toast.makeText(SendActivity.this, R.string.msg_send_user_blocked, Toast.LENGTH_SHORT).show();
					} else {
						toUpload();
					}
					break;
					
				case R.layout.send_add_image:
				default:
					break;
				}
			}
		}
	};
	
	private void toLocal() {
//		Intent intent = new Intent(SendActivity.this, RegionListActivity.class);
//		intent.putExtra("activityClass", SendActivity.class);
//		startActivity(intent);
//		btnAddress.setEnabled(false);
//		btnAddress.setText(getString(R.string.lab_localing));
//		if(locationUntils == null) {
//			locationUntils = new LocationUntils(SendActivity.this, localHandler);
//			locationUntils.onCreate();
//			locationUntils.start();
//		} else {
//			if(locationUntils.isStarted()) {
//				locationUntils.stop();
//			}
//			locationUntils.start();
//		}
	}
	
	private Handler cityHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.obj != null) {
				cityList = (ArrayList<RegionManEntity>) msg.obj;
				floatLocatCityView.setList(cityList);
			}
		};
	};
	
	private Handler regionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			if(msg.obj != null) {
				Object[] objects = (Object[]) msg.obj;
				if(objects != null && objects.length > 0) {
					regionTwos = (String[]) objects[0];
					if(objects.length > 1){
						regionTwoEntitys = (RegionManEntity[]) objects[1];
					}
				}
			}
		};
	};
	
	private Handler dialogCall = new Handler () {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// 拍照
				modelView.setVisibility(View.GONE);
				gotoCamera();
				break;

			case 2:
				// 选择图片
				modelView.setVisibility(View.GONE);
				selectedImg();
			case 0:
				// 取消
				modelView.setVisibility(View.GONE);
			default:
				break;
			}
		};
	};
	
	/**
	 * 发布信息
	 */
	private void toUpload() {
		if(categoryParentEntity == null) {
			btnSelParentCategory.setFocusable(true);
			Toast.makeText(SendActivity.this, R.string.msg_send_not_category, Toast.LENGTH_SHORT).show();
			return ;
		}
		if(categoryEntity == null) {
			btnSelCategory.setFocusable(true);
			Toast.makeText(SendActivity.this, R.string.msg_send_not_category, Toast.LENGTH_SHORT).show();
			return ;
		}
		String title = editTitle.getText().toString();
		if(title == null || title.equals("")) {
			editTitle.setFocusable(true);
			Toast.makeText(SendActivity.this, R.string.msg_send_not_title, Toast.LENGTH_SHORT).show();
			return ;
		} else {
			// 标题太短
			if(title.length() < 2) {
				editTitle.setFocusable(true);
				Toast.makeText(SendActivity.this, R.string.msg_send_title_too_short, Toast.LENGTH_SHORT).show();
				return ;
			}
		}
		String desc = editDesc.getText().toString();
		if(desc == null || desc.equals("")) {
			editDesc.setFocusable(true);
			Toast.makeText(SendActivity.this, R.string.msg_send_not_desc, Toast.LENGTH_SHORT).show();
			return ;
		} else {
			// 标题太短
			if(desc.length() < 10) {
				editDesc.setFocusable(true);
				Toast.makeText(SendActivity.this, R.string.msg_send_desc_too_short, Toast.LENGTH_SHORT).show();
				return ;
			}
		}
		String priceStr = editPrice.getText().toString();
		double price = 0d;
		if(layoutPrice.getVisibility() == View.VISIBLE) {
			if(priceStr == null || priceStr.equals("")) {
				editPrice.setFocusable(true);
				Toast.makeText(SendActivity.this, R.string.msg_send_not_price, Toast.LENGTH_SHORT).show();
				return ;
			} else {
				price = Double.parseDouble(priceStr);
			}
		}
		if(selCity == null || selCity.equals("") || regionCityEntity == null || regionCityEntity.getName() == null) {
			btnCity.setFocusable(true);
			Toast.makeText(SendActivity.this, R.string.msg_send_not_address, Toast.LENGTH_SHORT).show();
			return ;
		}
		String contact = editContact.getText().toString();
		if(contact == null || contact.equals("")) {
			editContact.setFocusable(true);
			Toast.makeText(SendActivity.this, R.string.msg_send_not_username, Toast.LENGTH_SHORT).show();
			return ;
		}
		String phone = editPhone.getText().toString();
		if(phone == null || phone.equals("")) {
			editPhone.setFocusable(true);
			Toast.makeText(SendActivity.this, R.string.msg_send_not_phone, Toast.LENGTH_SHORT).show();
			return ;
		}
		String qq = editQQ.getText().toString();
		
		int type = 0;
		int radioButtonId = radioGroup.getCheckedRadioButtonId();
		//根据ID获取RadioButton的实例
		switch (radioButtonId) {
		case R.id.radio_sendPayBuyers:
			type = 1;
			break;

		default:
			type = 2;
			break;
		}
		String userId = "0";	// 0表示当前用户未登录
		if(Contant.curUser != null) {
			userId = Contant.curUser.getId();
		}
		String language = SharePreferenceHelper.getSharepreferenceString(
				SendActivity.this, Contant.SETTINGSP,
				Contant.PREF_LANGUAGE, "zh-Hans");
		
		// 如果当前选择的地区父级是当前选择的城市，那么该地区是第二级别，可能会有三级区域
		if(regionAddress.getParentName().trim().equals(regionCityEntity.getName())) {
			selRegion = regionAddress.getName().trim();
			selStreet = null;
		} else {
			selRegion = regionAddress.getParentName();
			selStreet = regionAddress.getName().trim();
		}
		
		InfoItemEntity entity = new InfoItemEntity(title, desc, price, null, 
				phone, qq, language, selCity, selRegion, selStreet, categoryEntity.getId(), 0, 0, userId, null, null);
		entity.setType(type);
		entity.setAddress(selCity + " " + selRegion + " " + (selStreet == null ? "" : selStreet));
		if(itemsEditRunnable != null) {
			itemsEditRunnable.isStop();
			itemsEditRunnable = null;
		}
		modelView.setVisibility(View.VISIBLE);
		itemsEditRunnable = new ItemsEditRunnable(handler, imgMap, entity);
		
		showDialog(DIALOG_LOAD);
		new Thread(itemsEditRunnable).start();
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isProgress()) {
				dismissDialog(DIALOG_LOAD);
			}
			modelView.setVisibility(View.GONE);
			if(msg.what == 1) {
				Toast.makeText(SendActivity.this, R.string.msg_send_success, Toast.LENGTH_SHORT).show();
				// 是否继续发布？
	    		new AlertDialog.Builder(SendActivity.this).setTitle(R.string.title_message)
	            .setMessage(R.string.msg_send_success_to_msg)
	            .setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
	    			
	    			public void onClick(DialogInterface dialog, int which) {
	    				dialog.dismiss();
	    				resetView();
	    			}
	    		}).setNegativeButton(R.string.btnNO, new DialogInterface.OnClickListener() {  
	                public void onClick(DialogInterface dialog, int which) {  
	                    dialog.dismiss(); 
	                    gotoBack();
	                }  
	            }).create().show();
				
			} else if(msg.what == -2) {
				Toast.makeText(SendActivity.this, R.string.msg_send_user_blocked, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(SendActivity.this, R.string.msg_send_error, Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private void resetView() {
		editDesc.setText("");
		editTitle.setText("");
		editPrice.setText("");

		if(Contant.curLocationEntity != null && regionCityEntity == null) {
			regionCityEntity = Contant.curRegionEntity;
			btnAddress.setText(Contant.curLocationEntity.getDetailInfo());
		}
		
		floatLocatCityView.setList(Contant.regionManEntities);
		if(Contant.curUser != null) {
			editPhone.setText(Contant.curUser.getPhone());
			editContact.setText(Contant.curUser.getName());
			editQQ.setText(Contant.curUser.getQqSkype());
		}
		
		viewPager.removeAllViews();
		viewPager.addView(btnAddImg);
		
		imgMap.clear();
		imgViews.clear();
	}
	
	private void gotoBack() {
		if(activityClass != null) {
			Intent intent = new Intent(SendActivity.this, activityClass);
			intent.putExtra("categoryEntity", categoryParentEntity);
			startActivity(intent);
		} else {
			Intent intent = new Intent(SendActivity.this, InfoListActivity.class);
			intent.putExtra("categoryEntity", categoryParentEntity);
			startActivity(intent);
		}
		SendActivity.this.finish();
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
	
	/**
	 * 调用系统相机拍照
	 */
	private void gotoCamera() {
//		// 建立"选择档案Action" 的Intent
//		Intent intent = new Intent(Intent.ACTION_PICK);
//		// 过滤档案格式
//		intent.setType("image/*");
		// 建立"档案选择器" 的Intent (第二个参数: 选择器的标题)
		curTime = System.currentTimeMillis();
		curImgPath = RockEagleApp.app.DataPath + curTime + "temp.jpg";
		
		Intent intent = new Intent(SendActivity.this, TakePhotoActivity.class);
		intent.putExtra("filePath", curImgPath);
		startActivityForResult(intent, 1);
		
//		File file = new File(curImgPath);
//		Uri u = Uri.fromFile(file); 
//		Intent destIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		destIntent.putExtra(MediaStore.EXTRA_OUTPUT, u); 
//		// 切换到档案选择器(它的处理结果, 会触发onActivityResult 事件)
//		startActivityForResult(destIntent, 1);
		
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
					Long l = System.currentTimeMillis();
					View view = inflater.inflate(R.layout.send_img_item, null);
					// 通过URI获取到绝对路径
					ImageView imageView = (ImageView) view.findViewById(R.id.send_item_imgs);
					imageView.setBackgroundColor(0xffdfdfdf);
					imageView.setImageURI(uri);
					imageView.setOnClickListener(imgClickListener);
					imageView.setTag(l);
					
					ImageButton btnView = (ImageButton) view.findViewById(R.id.send_item_delete);
					btnView.setOnClickListener(imgClickListener);
					btnView.setTag(l);
					
					imgMap.put(l, getAbsoluteImagePath(uri));
					
					LayoutParams layoutParams = new LayoutParams(btnAddImg.getWidth(), btnAddImg.getHeight());
					layoutParams.setMargins(10, 0, 10, 0);
					viewPager.addView(view, layoutParams);
					
					imgViews.put(l, view);
				} else {
					// 无效的图片
				}
			} else if (requestCode == 1) {
//				Uri uri = data.getData();
//				System.out.println(curImgPath);
				// 取得档案的Uri
				String imgPath = curImgPath;
				
				View view = inflater.inflate(R.layout.send_img_item, null);
				// 通过URI获取到绝对路径
				ImageView imageView = (ImageView) view.findViewById(R.id.send_item_imgs);
				imageView.setBackgroundColor(0xffdfdfdf);
				// 压缩图片
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
				
				imageView.setImageBitmap(bm);
				imageView.setOnClickListener(imgClickListener);
				imageView.setTag(curTime);
				
				ImageButton btnView = (ImageButton) view.findViewById(R.id.send_item_delete);
				btnView.setOnClickListener(imgClickListener);
				btnView.setTag(curTime.longValue());
				
				LayoutParams layoutParams = new LayoutParams(btnAddImg.getWidth(), btnAddImg.getHeight());
				layoutParams.setMargins(10, 0, 10, 0);
				viewPager.addView(view, layoutParams);
				// 取得档案的Uri
				imgMap.put(curTime.longValue(), imgPath.toString());
			}
		} else {
			// 失败，请重新操作
		}
	}
	
	@Override 
    public void onConfigurationChanged(Configuration config) { 
		super.onConfigurationChanged(config); 
    } 
	
	private OnClickListener imgClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v instanceof ImageButton) {
				Long tag = (Long) v.getTag();
				View parent = imgViews.remove(tag);
				viewPager.removeView(parent);
				imgMap.remove(tag);
			} else if (v instanceof ImageView) {
				Intent intent = new Intent(SendActivity.this, PhotoActivity.class);
				intent.putExtra("imgMaps", (Serializable)imgMap);
				intent.putExtra("index", (Long)v.getTag());
				startActivity(intent);
			}
		}
	};
	
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
