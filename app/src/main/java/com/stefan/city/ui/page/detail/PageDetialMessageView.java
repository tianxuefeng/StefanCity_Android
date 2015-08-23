package com.stefan.city.ui.page.detail;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.stefan.city.R;
import com.stefan.city.module.Constant.Contant;
import com.stefan.city.module.entity.MessageEntity;
import com.stefan.city.module.thread.MessageSendRunnable;
import com.stefan.city.ui.adapter.MsgInfoAdapter;
import com.stefan.city.ui.page.view.BasePageView;

/**
 * PageDetialMessageView
 * 评论信息界面
 * @author 日期：2014-7-15下午05:56:42
 * @author 作者：岩鹰
 * @author 邮箱：jyanying@163.com
 * @version 0.1
 * @author (C) Copyright 岩鹰 Corporation 2014 - 2024
 *               All Rights Reserved.
 **/
public class PageDetialMessageView extends BasePageView {
	
	private List<MessageEntity> list;
	private String itemId;
	
	private Button btnSend;
	
	private EditText editMsg;
	
	private MsgInfoAdapter msgInfoAdapter;
	private ListView listView;
	
	private MessageSendRunnable messageSendRunnable;
	
	public PageDetialMessageView(Context context) {
		super(context);
		initLayout();
	}
	
	private void initLayout() {
		View view = inflate(getContext(), R.layout.page_detail_msg, null);
		
		listView = (ListView) view.findViewById(R.id.detail_msg_listView);
		btnSend = (Button) view.findViewById(R.id.btn_detail_sendMsg);
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toSend();
			}
		});
		editMsg = (EditText) view.findViewById(R.id.txt_detail_msg);
		
		addView(view);
	}
	
	private void toSend() {
		if(Contant.curUser == null) {
			Toast.makeText(getContext(), R.string.msg_not_login, Toast.LENGTH_SHORT).show();
			return ;
		}
		String msg = editMsg.getText().toString();
		if(msg == null || msg.equals("")) {
			editMsg.setFocusable(true);
			Toast.makeText(getContext(), R.string.msg_send_not_msg, Toast.LENGTH_SHORT).show();
			return ;
		}
		if(messageSendRunnable != null) {
			messageSendRunnable.isStop();
			messageSendRunnable = null;
		}
		messageSendRunnable = new MessageSendRunnable(handler, itemId, msg, Contant.curUser.getId());
		new Thread(messageSendRunnable).start();
		btnSend.setEnabled(false);
		btnSend.setText(getContext().getString(R.string.btn_loading));
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			btnSend.setEnabled(true);
			btnSend.setText(getContext().getString(R.string.btn_send_msg));
			if(msg.what == 1 && msg.obj != null) {
				if(list == null) {
					list = new ArrayList<MessageEntity>();
				}
				list.add(0, (MessageEntity) msg.obj);
				msgInfoAdapter.notifyDataSetChanged();
				editMsg.setText("");
				Toast.makeText(getContext(), R.string.msg_send_msg_success, Toast.LENGTH_SHORT).show();
			} else if(msg.what == -1) {
				Toast.makeText(getContext(), R.string.msg_not_net, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), R.string.msg_send_msg_error, Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	protected void loadData() {}

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

	public List<MessageEntity> getList() {
		return list;
	}

	public void setList(List<MessageEntity> list) {
		this.list = list;
		if(this.list == null) {
			this.list = new ArrayList<MessageEntity>();
		}
		if(msgInfoAdapter == null) {
			msgInfoAdapter = new MsgInfoAdapter(getContext(), this.list);
			listView.setAdapter(msgInfoAdapter);
		} else {
			msgInfoAdapter.notifyDataSetChanged();
		}
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@Override
	public void onResume(boolean isLoad) {
		if(isLoad) {
			onResume();
		}
	}
}
