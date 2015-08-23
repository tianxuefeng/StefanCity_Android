package com.stefan.city.ui.floatview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.stefan.city.CitySelectActivity;
import com.stefan.city.MainActivity;
import com.stefan.city.R;
import com.stefan.city.SendActivity;
import com.stefan.city.module.entity.RegionManEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

/**
 * FloatLocatCityView
 * 城市信息浮动框，用于选择城市
 * @author 日期：2015-3-9下午02:37:05
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class FloatLocatCityView {

	private Context context;
	private View contentView;
	private PopupWindow popupWindow;
	private GridView gridView;
	private View parent = null;
	
	private Button btnBackLocat;
	
	private List<RegionManEntity> entities;
	
	public FloatLocatCityView(Context context, OnItemClickListener itemClickListener) {
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		contentView = inflater.inflate(R.layout.floatview_sendsort, null);
		gridView = (GridView) contentView.findViewById(R.id.float_sortGridView);
		popupWindow = new PopupWindow(contentView);

		gridView.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		gridView.setOnItemClickListener(itemClickListener);
		
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);                  
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
		ColorDrawable dw = new ColorDrawable(-00000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
			}
		});
		
		btnBackLocat = (Button) contentView.findViewById(R.id.btn_category_back_locat);
		btnBackLocat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FloatLocatCityView.this.context, CitySelectActivity.class);
				if((FloatLocatCityView.this.context) instanceof MainActivity) {
					intent.putExtra("activityClass", MainActivity.class);
				} else if((FloatLocatCityView.this.context) instanceof SendActivity) {
					intent.putExtra("activityClass", SendActivity.class);
				}
				FloatLocatCityView.this.context.startActivity(intent);
				dismiss();
			}
		});
	}
	
	/**
	 * 设置View，以供popupwindow布局
	 * @param parent
	 */
	public void setView(View parent){
		this.parent = parent;
	}
	
	/**
	 *  自定义位置弹出菜单
	 */
	public void showView(){
		if(popupWindow != null && !popupWindow.isShowing()){
			popupWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, parent.getHeight() + 10);
		}
	}
	
	// 隐藏菜单
	public void dismiss(){
		if(popupWindow != null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}
	}
	
	public boolean isShowing(){
		return popupWindow.isShowing();
	}
	
	/**
	 * 构造菜单Adapter
	 * 
	 * @param menuNameArray
	 *            名称
	 * @param imageResourceArray
	 *            图片
	 * @return SimpleAdapter
	 */
	private SimpleAdapter getMenuAdapter() {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < entities.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemText", entities.get(i).getName());
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(context, data,
				R.layout.item_send_sort, new String[] {"itemText" },
				new int[] {R.id.grid_sortName });
		return simperAdapter;
	}

	public List<RegionManEntity> getList() {
		return entities;
	}

	public void setList(List<RegionManEntity> regions) {
		this.entities = regions;
		if(this.entities == null || this.entities.size() < 1) {
			this.entities = new ArrayList<RegionManEntity>();
		}
		gridView.setAdapter(getMenuAdapter());
	}
}
