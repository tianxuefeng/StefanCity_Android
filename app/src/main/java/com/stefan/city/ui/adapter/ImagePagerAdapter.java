package com.stefan.city.ui.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stefan.city.R;
import com.stefan.city.module.Constant.ContantURL;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ImagePagerAdapter
 * @author 日期：2014-8-17上午11:55:07
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class ImagePagerAdapter extends PagerAdapter {
	
	private List<String> imgPaths;
	private Context context;
//	private AsyncImageLoader imageLoader;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	private boolean isLocal = false;	// 是否是本地文件
	private Tag tag;
	
	public ImagePagerAdapter(Context context, List<String> imgPaths) {
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.image_no_bg_small)			// 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.image_no_bg_small)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.image_no_bg_small)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)							// 设置下载的图片是否缓存在SD卡中
		.build();									// 创建配置过得DisplayImageOption对象
//		imageLoader.setScreenSize(Contant.ScreenSize);
		this.imgPaths = imgPaths;
	}
	
	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

	@Override
	public int getCount() {
		return imgPaths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == (View) obj; 
	}
	
	@Override  
    public Object instantiateItem (ViewGroup container, int position) {  
//		if(tag == null) {
//			tag = new Tag();
//			tag.imageView = new ImageView(context);
//		}
        final ImageView iv = new ImageView(context);  
        String iconUrl = imgPaths.get(position);
//        // 异步加载图片
        if(iconUrl != null && !iconUrl.equals("")){
        	iv.setTag(iconUrl);
        	if(!isLocal)
        		iconUrl = ContantURL.getUploadImageUrl(iconUrl);
        	else 
        		iconUrl = "file://" + iconUrl;
        	imageLoader.displayImage(iconUrl, iv, options, null);
//			Bitmap bitmap = null;
//			String imgUrl = iconUrl;
//			if(imgUrl != null){
//				// 延迟加载图片
//				bitmap = imageLoader.loadData(iconUrl, new AsyncImageLoader.LoadDataCallback<Bitmap>() {
//					@Override
//					public void dataLoaded(String arg0, Bitmap arg1) {
//						iv.setImageBitmap(arg1);
////						ImageView imageViewByTag = (ImageView) temp;
////						if (imageViewByTag != null && arg1!=null) {
////							imageViewByTag.setImageBitmap(arg1);
////						}
////						RECacheHandleImpl.saveCacheImg(arg1, RockEagleApp.app.CachePath, arg0);
//					}
//				});
//			}
//			if(bitmap == null){
//				iv.setImageResource(R.drawable.image_no_bg);
//			}else{
////				RECacheHandleImpl.saveCacheImg(bitmap, RockEagleApp.app.CachePath, iconUrl);
//				iv.setImageBitmap(bitmap);
//			}
//		}else{
//			iv.setImageResource(R.drawable.image_no_bg);
		}
        
        ((ViewPager)container).addView(iv);  
        return iv;  
    }  
	
	protected final class Tag {
		public ImageView imageView;
	}
	
	@Override  
    public void destroyItem (ViewGroup container, int position, Object object) {  
        container.removeView((View)object);  
    } 
}
