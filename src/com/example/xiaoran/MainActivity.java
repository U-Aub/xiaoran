package com.example.xiaoran;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.xiaoran.bean.ChatMessage;
import com.example.xiaoran.bean.ChatMessage.Type;
import com.example.xiaoran.utils.HttpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity{
	private ListView mMsgs;
	private ChatMessageAdapter mAdapter;
	private List<ChatMessage> mDatas;
	
	private EditText mInputMsg;
	private Button mSendMsg;
	
	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg){
			ChatMessage fromMessge = (ChatMessage) msg.obj;
			mDatas.add(fromMessge);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size()-1);
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		initView();
		initDatas();
		
		initListener();
	}

	private void initListener() {
	mSendMsg.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final String toMsg=mInputMsg.getText().toString();
			if(TextUtils.isEmpty(toMsg)){
				return;
			}
			ChatMessage toMessage = new ChatMessage();
			toMessage.setDate(new Date());
			toMessage.setMsg(toMsg);
			toMessage.setType(Type.OUTCOMING);
			mDatas.add(toMessage);
			mAdapter.notifyDataSetChanged();
			mMsgs.setSelection(mDatas.size()-1);
			
			mInputMsg.setText("");
			
			new Thread(){
				public void run(){
					ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
					Message m = Message.obtain();
					m.obj = fromMessage;
					mHandler.sendMessage(m);
					
				}
			}.start();
		}
	});
		
	}

	private void initDatas() {
		mDatas=new ArrayList<ChatMessage>();
		mDatas.add(new ChatMessage("主人，有什么需要帮助的吗？",Type.INCOMING,new Date()));
		mAdapter=new ChatMessageAdapter(this, mDatas);
		
		mMsgs.setAdapter(mAdapter);
	}

	private void initView() {
		mMsgs=(ListView) findViewById(R.id.id_listview_msgs);
		mInputMsg=(EditText) findViewById(R.id.id_input_msg);
		mSendMsg=(Button) findViewById(R.id.id_send_msg);
	}
}
