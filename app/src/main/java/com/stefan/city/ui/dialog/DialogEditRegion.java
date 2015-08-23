package com.stefan.city.ui.dialog;

import com.stefan.city.R;
import com.stefan.city.module.entity.RegionManEntity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * DialogAddRegion
 * @author 日期：2014-9-7下午12:28:15
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class DialogEditRegion extends REDialog {
	
	private EditText /*editParent,*/ editName;
	private TextView labParent;

	private RegionManEntity entity;
	
	private Button btnOk;
	
	private Handler handler;
	
	private String parentName;

	public DialogEditRegion(Context context, Handler handler) {
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
		dialog.setContentView(R.layout.dialog_add_region);
		
		labParent = (TextView) dialog.findViewById(R.id.lab_region_man_parent_name);
		editName = (EditText) dialog.findViewById(R.id.edit_region_man_add_name);
		
//		editParent = (EditText) dialog.findViewById(R.id.edit_region_man_parent_name);
		
		btnOk = (Button) dialog.findViewById(R.id.btn_dialog_region_add);
		
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toEdit();
			}
		});
	}
	
	public void show(RegionManEntity entity) {
		super.show();
		this.entity = entity;
		editName.setText(entity.getName());
//		if(parentName != null && !parentName.equals("")) {
//			editParent.setText(parentName);
//		} else {
//			editParent.setText(R.string.lab_not);
//		}
	}
	
	private void toEdit() {
		String name = editName.getText().toString();
		if(name == null || name.equals("")) {
			Toast.makeText(context, R.string.msg_not_region_name, Toast.LENGTH_SHORT).show();
			return ;
		}
		entity.setName(name);
		dismiss();
		// 0: 为新增， 1： 编辑， 2：为删除
		Message msg = handler.obtainMessage(1, entity);
		handler.sendMessage(msg);
		editName.setText("");
	}

}
