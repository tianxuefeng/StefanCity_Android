package com.stefan.city.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rockeagle.framework.view.REGridView;
import com.stefan.city.R;
import com.stefan.city.module.entity.CategoryEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * SortTreeAdapter 分类树状list适配器
 * 
 * @author 日期：2014-7-3下午09:00:25
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024 All Rights Reserved.
 **/
public class SortTreeAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater = null;

	private List<CategoryEntity> groups;
	private CategoryEntity[][] childs;

	private TAG childsTAG;

	private Context context;
	private OnItemLongClickListener itemLongClickListener = null;

	public SortTreeAdapter(Context context, List<CategoryEntity> groups,
			CategoryEntity[][] childs,
			OnItemLongClickListener itemLongClickListener) {
		this.context = context;
		this.groups = groups;
		this.childs = childs;
		this.itemLongClickListener = itemLongClickListener;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			childsTAG = new TAG();
			convertView = inflater.inflate(R.layout.sort_childs, null);
			childsTAG.gridView = (REGridView) convertView
					.findViewById(R.id.sortChildsGridView);
			if (itemLongClickListener != null)
				childsTAG.gridView
						.setOnItemLongClickListener(itemLongClickListener);

			convertView.setTag(R.id.btn_category_add_submit, childsTAG);
			convertView.setTag(R.id.btn_category_add, groupPosition);
			convertView.setTag(R.id.btn_category_edit, childPosition);
		} else {
			childsTAG = (TAG) convertView.getTag(R.id.btn_category_add_submit);
		}
		CategoryEntity[] entities = childs[groupPosition];
		if (entities != null) {
			// 设置菜单Adapter
			childsTAG.gridView.setAdapter(getMenuAdapter(entities));
			childsTAG.gridView.setTag(groupPosition);
			childsTAG.gridView.setVisibility(View.VISIBLE);
		} else {
			childsTAG.gridView.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (childs == null)
			return 0;
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groups == null)
			return null;
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if (groups == null)
			return 0;
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		CategoryEntity entity = groups.get(groupPosition);
		if (entity == null)
			return convertView;
		TAG groupTAG = new TAG();
		convertView = inflater.inflate(R.layout.sort_groups, null);
		groupTAG.labItem = (TextView) convertView
				.findViewById(R.id.sort_GroupsItem);

		String name = entity.getTitle();
		if (name != null) {
			groupTAG.labItem.setText(name);
		}
		convertView.setTag(groupPosition);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private SimpleAdapter getMenuAdapter(CategoryEntity[] list) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < list.length; i++) {
			CategoryEntity entity = list[i];
			if (entity == null) {
				continue;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemText", entity.getTitle());
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(context, data,
				R.layout.sort_childs_item, new String[] { "itemText" },
				new int[] { R.id.sort_ChildsItem });
		return simperAdapter;
	}

	protected class TAG {
		public TextView labItem;
		public REGridView gridView;
	}
}
