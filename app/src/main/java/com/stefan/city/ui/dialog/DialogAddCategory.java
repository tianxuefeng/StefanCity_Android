package com.stefan.city.ui.dialog;

import com.stefan.city.R;
import com.stefan.city.module.entity.CategoryEntity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * DialogAddCategory
 * @author 日期：2014-9-7下午12:28:15
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class DialogAddCategory extends REDialog {
	
	private EditText editName;
	private TextView labParent;

	private String parentId;
	
	private Button btnOk;
	
	private Handler handler;

	public DialogAddCategory(Context context) {
		super(context);
	}

	@Override
	protected void initDialog() {
		dialog = new Dialog(context);
		dialog.show();
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); 
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.dialog_add_category);
		
		labParent = (TextView) dialog.findViewById(R.id.lab_category_man_parent_name);
		editName = (EditText) dialog.findViewById(R.id.edit_category_man_add_name);
		
		btnOk = (Button) dialog.findViewById(R.id.btn_category_add);
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toAdd();
			}
		});
	}
	
	private void toAdd() {
		String name = editName.getText().toString();
		if(name == null || name.equals("")) {
			Toast.makeText(context, R.string.msg_not_category_name, Toast.LENGTH_SHORT).show();
			return ;
		}
		CategoryEntity entity = new CategoryEntity();
		entity.setParentId(parentId);
		entity.setTitle(name);
	}

}
