/**
 * 
 */
package com.stefan.city.ui.dialog;

import com.stefan.city.R;
import com.stefan.city.base.RECallback;
import com.stefan.city.module.entity.UserEntity;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * DialogChangePawd
 * @author 日期：2015年7月13日下午4:12:26
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class DialogChangePawd extends REDialog {
	
	private Button btnOK;
	
	private EditText editOldPawd, editNewPawd, editNewPawd2;
	
	private RECallback handler;

	/**
	 * @param context
	 */
	public DialogChangePawd(Context context, RECallback handler) {
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
		dialog.setContentView(R.layout.dialog_change_pawd);
		
		btnOK = (Button) dialog.findViewById(R.id.btn_user_change_pawd);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toUpdate();
			}
		});
		editOldPawd = (EditText) dialog.findViewById(R.id.edit_change_old_pawd);
		editNewPawd = (EditText) dialog.findViewById(R.id.edit_change_new_pawd);
		editNewPawd2 = (EditText) dialog.findViewById(R.id.edit_change_new_pawd2);
	}
	
	private void toUpdate() {
		String oldPawd = editOldPawd.getText().toString();
		if(oldPawd == null || oldPawd.trim().length() <= 0) {
			editOldPawd.setFocusable(true);
			Toast.makeText(context, R.string.msg_change_not_old_pawd, Toast.LENGTH_SHORT).show();
			return ;
		}
		String newPawd = editNewPawd.getText().toString();
		if(newPawd == null || newPawd.trim().length() <= 0) {
			editNewPawd.setFocusable(true);
			Toast.makeText(context, R.string.msg_change_not_new_pawd, Toast.LENGTH_SHORT).show();
			return ;
		}
		String newPawd2 = editNewPawd2.getText().toString();
		if(newPawd2 == null || newPawd2.trim().length() <= 0) {
			editNewPawd2.setFocusable(true);
			Toast.makeText(context, R.string.msg_login_not_pawd2, Toast.LENGTH_SHORT).show();
			return ;
		} else {
			if(!newPawd.equals(newPawd2)) {
				Toast.makeText(context, R.string.msg_register_not_pawd_error, Toast.LENGTH_SHORT).show();
				return ;
			}
		}
		dismiss();
		handler.toCallback(new String[]{oldPawd, newPawd});
		editOldPawd.setText("");
		editNewPawd.setText("");
		editNewPawd2.setText("");
	}

}
