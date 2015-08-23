package com.stefan.city.ui.dialog;

import com.stefan.city.R;
import com.stefan.city.module.entity.CategoryEntity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * DialogEditCategory
 * @author 日期：2014-9-3下午08:43:14
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class DialogEditCategory extends REDialog {
	
	private EditText oldName, editName;
	
	private Button btnOk;
	
	private CategoryEntity entity;
	
	private Handler handler;

	public DialogEditCategory(Context context, Handler handler) {
		super(context);
		this.handler = handler;
	}

	@Override
	protected void initDialog() {
		dialog = new Dialog(context);
		dialog.show();
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0)); 
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(R.layout.dialog_edit_category);
		
		oldName = (EditText) dialog.findViewById(R.id.edit_category_man_old_name);
		editName = (EditText) dialog.findViewById(R.id.edit_category_man_new_name);
		btnOk = (Button) dialog.findViewById(R.id.btn_category_edit);
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String newName = editName.getText().toString();
				if(newName == null || newName.equals("")) {
					Toast.makeText(context, R.string.msg_not_category_name, Toast.LENGTH_SHORT).show();
					return ;
				}
				Message msg = handler.obtainMessage();
				msg.what = 1;
				entity.setTitle(newName);
				msg.obj = entity;
				handler.sendMessage(msg);
			}
		});
	}
	
	public void show(CategoryEntity entity) {
		if(dialog == null){
			initDialog();
		}else{
			dialog.show();
		}
		this.entity = entity;
		oldName.setText(this.entity.getTitle());
	}

}
