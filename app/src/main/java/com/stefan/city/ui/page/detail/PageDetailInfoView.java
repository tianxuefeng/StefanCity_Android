package com.stefan.city.ui.page.detail;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stefan.city.DetailActivity;
import com.stefan.city.PhotoActivity;
import com.stefan.city.R;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.module.thread.UserBockRunnable;
import com.stefan.city.tools.ParseStringUtil;
import com.stefan.city.ui.page.view.BasePageView;

/**
 * PageDetailInfoView
 * 详细信息界面
 * @author 日期：2014-7-15下午05:29:37
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class PageDetailInfoView extends BasePageView {
	
	private boolean isUpdate;	// 是否强制更新数据
	
	private InfoItemEntity entity;

	private LinearLayout detailGallery;
	
	private Button btnDeleteInfo, btnRestrict;
	
//	private TextView labTitle;
	private TextView txtTime;
	private TextView txtPrice, labPrice;
	private TextView txtQQ, labQQ;
	private TextView txtAddress;
	private TextView txtContent;
	private TextView txtPhone;
	
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	private UserBockRunnable userBockRunnable;
	
	public PageDetailInfoView(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		initLayout();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.image_no_bg_small)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.image_no_bg_small)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.image_no_bg_small)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.build();
	}
	
	private void initLayout() {
		View view = inflate(getContext(), R.layout.page_detail_info, null);
		txtTime = (TextView) view.findViewById(R.id.detailSendTimeTxt);
		txtPrice = (TextView) view.findViewById(R.id.detailPriceTxt);
		labPrice = (TextView) view.findViewById(R.id.detailPriceLab);
		
		txtQQ = (TextView) view.findViewById(R.id.detailQQTxt);
		labQQ = (TextView) view.findViewById(R.id.detailQQLab);
		txtAddress = (TextView) view.findViewById(R.id.detailAddressTxt);
		txtContent = (TextView) view.findViewById(R.id.detailDetailTxt);
		txtPhone = (TextView) view.findViewById(R.id.detailPhoneTxt);
		
		btnDeleteInfo = (Button) view.findViewById(R.id.btn_detail_delete);
		btnRestrict = (Button) view.findViewById(R.id.btn_detail_restrict);
		btnRestrict.setVisibility(View.GONE);
		txtPhone.setOnClickListener(clickListener);
		
		btnDeleteInfo.setOnClickListener(clickListener);
		btnRestrict.setOnClickListener(clickListener);
		
		detailGallery = (LinearLayout) view.findViewById(R.id.detailItemGallery);
		
//		if(Contant.curUser == null || Contant.curUser.getMemType() < 3) {
//			if(btnRestrict != null) {
//				btnRestrict.setVisibility(View.GONE);
//			}
//		} else {
//			if(btnRestrict != null) 
//				btnRestrict.setVisibility(View.VISIBLE);
//		}
		addView(view);
	}
	
	private void updateUI() {
		if(entity.getPrice() <= 0) {
			txtPrice.setVisibility(View.GONE);
			labPrice.setVisibility(View.GONE);
		} else {
			txtPrice.setVisibility(View.VISIBLE);
			labPrice.setVisibility(View.VISIBLE);
			txtPrice.setText(Contant.formatFloatNumber(entity.getPrice()));
		}
		if(entity.getCreateDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			txtTime.setText(dateFormat.format(entity.getCreateDate()));
		}
		
		txtAddress.setText(entity.getAddress());
		String qq = entity.getQqSkype();
		if(qq == null || qq.equals("")) {
			txtQQ.setVisibility(View.GONE);
			labQQ.setVisibility(View.GONE);
		} else {
			txtQQ.setVisibility(View.VISIBLE);
			labQQ.setVisibility(View.VISIBLE);
			txtQQ.setText(qq);
		}
		txtContent.setText(entity.getDescription());
		txtPhone.setText(entity.getPhone());
		
//		if(Contant.curUser == null || Contant.curUser.getMemType() < 3) {
//			if(btnRestrict != null) {
//				btnRestrict.setVisibility(View.GONE);
//			}
//		} else {
//			if(btnRestrict != null) 
//				btnRestrict.setVisibility(View.VISIBLE);
//		}
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_detail_restrict:
				if(Contant.curUser == null) {
					Toast.makeText(getContext(), R.string.msg_not_login, Toast.LENGTH_SHORT).show();
					return ;
				} else {
					if(Contant.curUser.getMemType() < 3) {
						Toast.makeText(getContext(), R.string.msg_operate_authority_lacking, Toast.LENGTH_SHORT).show();
					} else {
						// 不可禁用自己
						if(Contant.curUser.getId().equals(entity.getUserId())) {
							Toast.makeText(getContext(), R.string.msg_cur_user_nonmaskable, Toast.LENGTH_SHORT).show();
						} else {
							// 屏蔽用户信息
							if(userBockRunnable != null) {
								userBockRunnable.isStop();
								userBockRunnable = null;
							}
							((DetailActivity) getContext()).showLoading();
							userBockRunnable = new UserBockRunnable(handler, entity.getUserId());
							new Thread(userBockRunnable).start();
						}
					}
				}
				break;
				
			case R.id.btn_detail_delete:
				
				break;
				
			case R.id.detailPhoneTxt:
				String phone = txtPhone.getText().toString();
				if(phone != null && !phone.equals("")) {
//					gotoPhone(phone);
					showPhoneDialog();
				}
				break;

			default:
				break;
			}
		}
	};
	
	private void showPhoneDialog() {
		final Builder builder = new AlertDialog.Builder(getContext());
		builder.setItems(R.array.array_phone_control, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String phone = txtPhone.getText().toString();
				switch (which) {
				case 0:
					gotoPhone(phone);
					break;

				case 1: {
					Uri smsToUri = Uri.parse("smsto:"+phone);  
					Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
					getContext().startActivity(intent);  
				}
					break;
					
				default:
					break;
				}
			}
		});
		builder.create().show();
	}
	
	/**
	 * 进入拨打电话的界面
	 * @param phone
	 */
	private void gotoPhone(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone)); 
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    getContext().startActivity(intent);
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			((DetailActivity) getContext()).dismissLoading();
			if(msg.what == 1) {
				Toast.makeText(getContext(), R.string.msg_operate_success, Toast.LENGTH_SHORT).show();
			} else if(msg.what == -1) {
				// 网络不通，服务器连接失败
				Toast.makeText(getContext(), R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void loadData() {
		
	}

	@Override
	protected void initVal() {
		
	}

	@Override
	protected void pageLoadData() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void onResume() {
		if(entity != null) {
			updateUI();
		}
//		if(Contant.curUser == null || Contant.curUser.getMemType() < 3) {
//			if(btnRestrict != null) {
//				btnRestrict.setVisibility(View.GONE);
//			}
//		} else {
//			if(btnRestrict != null) 
//				btnRestrict.setVisibility(View.VISIBLE);
//		}
	}

	public void setEntity(InfoItemEntity entity) {
		this.entity = entity;
		isUpdate = true;
		updateUI();
		updateImg();
	}
	
	private void updateImg() {
		String images = entity.getImages();
		detailGallery.removeAllViews();
		if(images != null && !images.equals("")) {
			List<String> imgPaths = ParseStringUtil.strToArray(images, "|");
			if(imgPaths == null || imgPaths.size() < 1) {
				detailGallery.setVisibility(View.GONE);
				return ;
			}
			detailGallery.setVisibility(View.VISIBLE);
			for (int i = 0; i<imgPaths.size(); i++) {
				String iconUrl = imgPaths.get(i);
				if(iconUrl == null || iconUrl.trim().equals("")) {
					continue;
				}
				// 异步加载图片
				final ImageView imgView = (ImageView) inflater.inflate(R.layout.item_images, null);
				if(iconUrl != null && !iconUrl.equals("")){
					iconUrl = ContantURL.getUploadSmallImageUrl(iconUrl);
					imageLoader.displayImage(iconUrl, imgView, options, null);
//					Bitmap bitmap = null;
//					String imgUrl = iconUrl;
//					if(imgUrl != null){
//						// 延迟加载图片
//						bitmap = imageLoader.loadData(iconUrl, new AsyncImageLoader.LoadDataCallback<Bitmap>() {
//							@Override
//							public void dataLoaded(String arg0, Bitmap arg1) {
//								imgView.setImageBitmap(arg1);
//								ImageView imageViewByTag = (ImageView) imgView;
//								if (imageViewByTag != null && arg1!=null) {
//									imageViewByTag.setImageBitmap(arg1);
//								}
//								
//							}
//						});
//					}
//					if(bitmap == null){
//						imgView.setImageResource(R.drawable.image_no_bg_small);
//					}else{
//						imgView.setImageBitmap(bitmap);
//					}
				} else {
					imgView.setImageResource(R.drawable.image_no_bg_small);
				}
				LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(10, 0, 10, 0);
				imgView.setTag(i);
				imgView.setOnClickListener(imgClickListener);
				detailGallery.addView(imgView);
			}
		} else {
			detailGallery.setVisibility(View.GONE);
		}
	}
	
	private OnClickListener imgClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag();
			Intent intent = new Intent(getContext(), PhotoActivity.class);
			intent.putExtra("images", entity.getImages());
			intent.putExtra("index", index);
			getContext().startActivity(intent);
		}
	};

	@Override
	public void onResume(boolean isLoad) {
		if(isLoad) {
			onResume();
		}
	}
}
