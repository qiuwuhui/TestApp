package com.baolinetwkchnology.shejiquan.xiaoxi;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.util.DateUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class XiangQinActivity extends EaseBaseActivity {

	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiang_qin);
		//		//魅族
		CommonUtils.setMeizuStatusBarDarkIcon(XiangQinActivity.this, true);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(XiangQinActivity.this, true);
		userName = getIntent().getStringExtra("UserName");
		int SJQ=getIntent().getIntExtra("SJQ", 0);
		Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0) {
				sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
			}
		}
		for (Pair<Long, EMConversation> sortItem : sortList) {
			EMConversation emConversation = sortItem.second;
			if (emConversation.getUserName().equals(userName)) {
				emConversation.markAllMessagesAsRead();
				Intent intent = new Intent();
				intent.setAction("ReadMag");
				sendBroadcast(intent);
			}
		}
		if(SJQ==1){
			SJQChatFragment chatFragment = new SJQChatFragment();
			Bundle args = new Bundle();
			args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
			args.putString(EaseConstant.EXTRA_USER_ID, userName);
			chatFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatFragment).commit();
		}else{
//			Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//			List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
//			for (EMConversation conversation : conversations.values()) {
//				if (conversation.getAllMessages().size() != 0) {
//					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
//				}
//			}
//			int aaaa=sortList.size();
//			if(	sortList.size()==0){
//				EMMessage msg1 = createReceivedTextMsg(userName);
//				List<EMMessage> ms = new ArrayList<EMMessage>();
//				ms.add(msg1);
//				EMClient.getInstance().chatManager().importMessages(ms);
//			}else {
//				for (Pair<Long, EMConversation> sortItem : sortList) {
//					EMConversation emConversation=sortItem.second;
//					if(!emConversation.getUserName().equals(userName)){
//						-- aaaa;
//						if(aaaa==0){
//							EMMessage msg1 = createReceivedTextMsg(userName);
//							List<EMMessage> ms = new ArrayList<EMMessage>();
//							ms.add(msg1);
//							EMClient.getInstance().chatManager().importMessages(ms);
//						}
//					}else{
//						++ aaaa;
//					}
//				}
//			}
			ChatFragment chatFragment = new ChatFragment();
			Bundle args = new Bundle();
			args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
			args.putString(EaseConstant.EXTRA_USER_ID, userName);
			chatFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatFragment).commit();
		}
	}
	//创建一条接收TextMsg
	private EMMessage createReceivedTextMsg(String from) {
		EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
		EMTextMessageBody body = new EMTextMessageBody("你好有什么可以帮助您的?");
		msg.addBody(body);
		msg.setFrom(from);
		String UserName= CommonUtils.removeAllSpace(SJQApp.user.getGuid());
		msg.setTo(UserName);
		msg.setMsgTime(System.currentTimeMillis());
		return msg;
	}
}
