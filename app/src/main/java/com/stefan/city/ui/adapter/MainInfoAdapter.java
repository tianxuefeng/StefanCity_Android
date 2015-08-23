package com.stefan.city.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stefan.city.R;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.Constant.ContantURL;
import com.stefan.city.module.entity.InfoItemEntity;
import com.stefan.city.tools.ParseStringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * MainInfoAdapter
 * 	发布信息列表适配器
 * @author 日期：2014-4-8下午10:51:10
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class MainInfoAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<InfoItemEntity> list;
	private Tag tag;
	
//	private AsyncImageLoader imageLoader;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public MainInfoAdapter(Context context) {
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.image_no_bg_small)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.image_no_bg_small)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.image_no_bg_small)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.build();									// 创建配置过得DisplayImageOption对象
	}

	@Override
	public int getCount() {
		return (list != null) ? list.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	public List<InfoItemEntity> getList() {
		return list;
	}

	public void setList(List<InfoItemEntity> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			tag = new Tag();
			convertView = inflater.inflate(R.layout.item_main_info, null);
			tag.imgIcon = (ImageView) convertView.findViewById(R.id.item_infoIcon);
			tag.labTitle = (TextView) convertView.findViewById(R.id.item_infoTitle);
			tag.labDesc = (TextView) convertView.findViewById(R.id.item_infoDesc);
			tag.labPrice = (TextView) convertView.findViewById(R.id.item_infoPrice);
			tag.labTime = (TextView) convertView.findViewById(R.id.item_infoTime);
			tag.labAddress = (TextView) convertView.findViewById(R.id.item_infoAddress);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		String iconUrl = null;
		InfoItemEntity entity = list.get(position);
		if(entity != null) {
			iconUrl = entity.getImages();
			if(iconUrl != null) {
				List<String> icons = ParseStringUtil.strToArray(iconUrl, "|");
				if(icons != null && icons.size() > 0) {
					iconUrl = icons.get(0).trim();	// 取得第一个图片路径
					if(iconUrl != null && !iconUrl.equals("")) {
						iconUrl = ContantURL.getUploadSmallImageUrl(iconUrl);
					}
				}
			}
			tag.labTitle.setText(entity.getTitle());
			tag.labDesc.setText(entity.getDescription());
			tag.labAddress.setText(entity.getAddress());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			tag.labTime.setText(dateFormat.format(entity.getUpdateDate()));
			if(entity.getPrice() <= 0) {
				tag.labPrice.setVisibility(View.GONE);
			} else {
				tag.labPrice.setVisibility(View.VISIBLE);
			}
			tag.labPrice.setText(Contant.formatFloatNumber(entity.getPrice()));
		}
		// 异步加载图片
		/**
		 * 显示图片
		 * 参数1：图片url
		 * 参数2：显示图片的控件
		 * 参数3：显示图片的设置
		 * 参数4：监听器
		 */
		imageLoader.displayImage(iconUrl, tag.imgIcon, options, null);
//		final View tempview = convertView;
//		if(iconUrl != null && !iconUrl.equals("")){
//			tag.imgIcon.setTag(iconUrl);
//			Bitmap bitmap = null;
//			String imgUrl = iconUrl;
//			if(imgUrl != null){
//				// 延迟加载图片
//				bitmap = imageLoader.loadData(iconUrl, new AsyncImageLoader.LoadDataCallback<Bitmap>() {
//					@Override
//					public void dataLoaded(String arg0, Bitmap arg1) {
//						tag.imgIcon.setImageBitmap(arg1);
//						ImageView imageViewByTag = (ImageView) tempview
//						.findViewWithTag(arg0);
//						if (imageViewByTag != null && arg1!=null) {
//							imageViewByTag.setImageBitmap(arg1);
//						}
//						
//					}
//				});
//			}
//			if(bitmap == null){
//				tag.imgIcon.setImageResource(R.drawable.image_no_bg_small);
//			}else{
//				tag.imgIcon.setImageBitmap(bitmap);
//			}
//		}else{
//			tag.imgIcon.setImageResource(R.drawable.image_no_bg_small);
//		}
		
		return convertView;
	}

	protected class Tag {
		public TextView labTitle;
		public ImageView imgIcon;
		public TextView labDesc;
		public TextView labTime;
		public TextView labPrice;
		public TextView labAddress;
	}
}
