package com.stefan.city.ui.page.view;

import android.content.Context;
import android.view.View;

import com.stefan.city.R;

/**
 * PageScoreView
 * @author 日期：2014-4-12下午12:38:30
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2011 - 2021
 *               All Rights Reserved.
 **/
public class PageScoreView extends BasePageView {
	
//	private FavoriteService favoriteService;
//	
//	private MemberService memberService;
//	
//	private UploadService uploadService;
	
	private View view;
	
//	private StringBuffer buffer = new StringBuffer();
	
	public PageScoreView(Context context) {
		super(context);
//		favoriteService = new FavoriteService();
//		memberService = new MemberService();
//		uploadService = new UploadService();
		view = inflate(context, R.layout.page_score, null);
		this.addView(view);
	}
	
	@Override
	protected void loadData() {
//		buffer.delete(0, buffer.length());
//		// =========  测试创建用户   ============
//		UserEntity user = new UserEntity("测试2", "123456", "www.12@12235.com", "123", "13342442452", "qq", 1, "deviceToken");
//		buffer.append("\n注册一个新用户  ");
//		int rows = memberService.register(user);
//		if(rows == 1)
//			buffer.append("注册成功！  ");
//		else
//			buffer.append("注册失败！  ");
//		// 登陆
//		buffer.append("\n\n测试登陆： ");
//		Contant.curUser = memberService.toLogin("www.12@12235.com", "123456");
//		if(Contant.curUser != null)
//			buffer.append("登陆成功！用户名称：  "+Contant.curUser.getName());
//		else
//			buffer.append("登陆失败！  ");
//		// =========  测试收藏信息   ============
//		List<InfoItemEntity> favoriteList = favoriteService.getListByUser("1");
//		buffer.append("\n\n查看用户ID为 1 的收藏夹信息：");
//		if(favoriteList != null && favoriteList.size() > 0) {
//			buffer.append("\n收藏夹有 "+favoriteList.size()+" 条信息");
//			buffer.append("第一条是"+favoriteList.get(0).getTitle());
//		} else {
//			buffer.append("\n暂无信息");
//		}
//		// 新增收藏信息
//		rows = favoriteService.insert("1", "16");
//		if(rows == 1) {
//			buffer.append("\n新增一条收藏信息，收藏成功");
//		} else {
//			buffer.append("\n新增一条收藏信息，收藏失败");
//		}
//		// =========  测试发布信息   ============
//		ItemInfoService infoService = new ItemInfoService();
//		buffer.append("\n\n获得ID为1的用户发布的信息：");
//		List<InfoItemEntity> list = infoService.getByUserId("1");
//		if(list != null && list.size() > 0) {
//			buffer.append("发布了 "+list.size()+" 条信息，第一条为："+list.get(0).getTitle());
//		} else {
//			buffer.append("暂无信息");
//		}
//		String SDCradPath = android.os.Environment.getExternalStorageDirectory()
//		.getAbsolutePath();
//		String filePath = SDCradPath + "/105.png";
//		buffer.append("\n\n上传图片");
//		rows = uploadService.upload(filePath, "106.png");
//		if(rows == 1) {
//			buffer.append("上传成功");
//		} else {
//			buffer.append("上传失败");
//		}
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
		
	}
	
	@Override
	public void onResume(boolean isLoad) {
		if(isLoad) {
			onResume();
		}
	}
	
}
