package com.baolinetwkchnology.shejiquan.xiaoxi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.activity.BoundActivity;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.MessageActivity1;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity;
import com.baolinetworktechnology.shejiquan.activity.MyPinLuenActivity;
import com.baolinetworktechnology.shejiquan.activity.ThumbUpActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.util.DateUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class HuiHuaFragment extends EaseBaseFragment implements OnClickListener{
	
	private final static int MSG_REFRESH = 2;
//    protected EditText query;
//    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;
    private MyBroadcastReciver mybroad;
    protected EMConversationListener convListener = new EMConversationListener(){

		@Override
		public void onCoversationUpdate() {
			refresh();
		}
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    protected void initView() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ReadMag");
        intentFilter.addAction("showView");
        mybroad=new MyBroadcastReciver();
        getActivity().registerReceiver(mybroad, intentFilter);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        conversationListView.setFocusable(false);
//      query = (EditText) getView().findViewById(R.id.query);
//      button to clear content in search bar
//      clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
        getView().findViewById(R.id.list_banding).setOnClickListener(this);
        message_msg= (TextView) getView().findViewById(R.id.message_msg);
        message_msg.setVisibility(View.GONE);
        pinluen_msg = (TextView) getView().findViewById(R.id.pinluen_msg);
        pinluen_msg.setVisibility(View.GONE);
        dianzan_msg = (TextView) getView().findViewById(R.id.dianzan_msg);
        dianzan_msg.setVisibility(View.GONE);
        getView().findViewById(R.id.list_dianzan).setOnClickListener(this);
        getView().findViewById(R.id.list_tongzhi).setOnClickListener(this);
        getView().findViewById(R.id.list_pingluen).setOnClickListener(this);
        getView().findViewById(R.id.list_kefu).setOnClickListener(this);
        sheji_time = (TextView) getView().findViewById(R.id.sheji_time);
        sheji_xx = (TextView) getView().findViewById(R.id.sheji_xx);
        setReadMag();
        showView();
    }
    @Override
    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(DemoHelper.getInstance().isConnected()){
                        EMConversation conversation = conversationListView.getItem(position);
                        String strname=conversation.getUserName();
                        String GUID=conversation.getExtField();
//                    if(GUID.equals("")){
                        // 普通个人
                        Intent intent=new Intent(getActivity(),XiangQinActivity.class);
                        intent.putExtra("UserName",strname);
                        startActivity(intent);
//                    }
                    }else{
                        String LoginName= CommonUtils.removeAllSpace(SJQApp.user.getGuid());
                        setdenlu(LoginName);
                    }
                }
            });
            conversationListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					 EMConversation conversation = conversationListView.getItem(position);
	                 final String strname = conversation.getUserName();
					new MyAlertDialog(getActivity()).setTitle("是否删除该会话")
					.setBtnCancel("取消").setBtnOk("删除")
					.setBtnOnListener(new DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						if (isOk)
						EMClient.getInstance().chatManager().deleteConversation(strname, true);
						conversationList.clear();
		                conversationList.addAll(loadConversationList());
		                conversationListView.refresh();
						}
					}).show();
					return true;
				}
			});
        
        EMClient.getInstance().addConnectionListener(connectionListener);
        
//        query.addTextChangedListener(new TextWatcher() {
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                conversationListView.filter(s);
//                if (s.length() > 0) {
//                    clearSearch.setVisibility(View.VISIBLE);
//                } else {
//                    clearSearch.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        clearSearch.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                query.getText().clear();
//                hideSoftKeyboard();
//            }
//        });
        
        conversationListView.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }
    
    
    protected EMConnectionListener connectionListener = new EMConnectionListener() {
        
        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
               handler.sendEmptyMessage(0);
            }
        }
        
        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;
    
    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                onConnectionDisconnected();
                break;
            case 1:
                onConnectionConnected();
                break;
            
            case MSG_REFRESH:
	            {
	            	conversationList.clear();
	                conversationList.addAll(loadConversationList());
	                conversationListView.refresh();
	                break;
	            }
            default:
                break;
            }
        }
    };
	private TextView sheji_time;
	private TextView sheji_xx;
    private TextView message_msg,pinluen_msg,dianzan_msg;
    
    /**
     * connected to server
     */
    protected void onConnectionConnected(){
        errorItemContainer.setVisibility(View.GONE);
    }
    
    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected(){
        errorItemContainer.setVisibility(View.VISIBLE);
    }
    

    /**
     * refresh ui
     */
    public void refresh() {
    	if(!handler.hasMessages(MSG_REFRESH)){
    		handler.sendEmptyMessage(MSG_REFRESH);
    	}
    }
    
    /**
     * load conversation list
     * 
     * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +    */
    protected List<EMConversation> loadConversationList(){
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            EMConversation emConversation=sortItem.second;
        	if(!emConversation.getUserName().equals("sjq-kefu")){
                list.add(sortItem.second);
            }else{
                EMMessage lastMessage = emConversation.getLastMessage();
                sheji_xx.setVisibility(View.VISIBLE);
                sheji_xx.setText(EaseSmileUtils.getSmiledText(getContext(),
                        EaseCommonUtils.getMessageDigest(lastMessage, (getActivity()))));
                sheji_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            }
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     * 
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
    
   protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mybroad!=null){
            getActivity().unregisterReceiver(mybroad);
        }
        if(connectionListener!=null) {
            EMClient.getInstance().removeConnectionListener(connectionListener);
        }
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isConflict){
            outState.putBoolean("isConflict", true);
        }
    }
    
    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }
    
    /**
     * set conversation list item click listener
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }

	@Override
	public void onClick(View v) {
        Intent intent=null;
		switch (v.getId()) {
        case R.id.list_banding:
            intent = new Intent();
            intent.setClass(getActivity(),
                    BoundActivity.class);
            startActivity(intent);
             break;
        case R.id.list_dianzan:
             message_msg.setVisibility(View.GONE);
             intent=new Intent(getActivity(),ThumbUpActivity.class);
             startActivity(intent);
             break;
		case R.id.list_tongzhi:
            message_msg.setVisibility(View.GONE);
		    intent=new Intent(getActivity(),MessageActivity1.class);
            startActivity(intent);
			break;
		case R.id.list_pingluen:
            pinluen_msg.setVisibility(View.GONE);
			go2Fragment(MyPinLuenActivity.PinluenFragment);
			break;
		case R.id.list_kefu:
                if(DemoHelper.getInstance().isLoggedIn()){
                    Intent intent1=new Intent(getActivity(),XiangQinActivity.class);
                    intent1.putExtra("UserName","sjq-kefu");
                    intent1.putExtra("SJQ", 1);
                    startActivity(intent1);
                }else{
                    toastShow("账号异常，请退出后重新登录");
                }
			break;
		default:
			break;
		}
		
	}
	private void go2Fragment(int type) {
		Intent intent = new Intent(getContext(), MyPinLuenActivity.class);
		intent.putExtra(MyFragmentActivity.TYPE, type);
		getContext().startActivity(intent);
	}
    private void showView(){
        if(SJQApp.user !=null){
            if(SJQApp.user.isBindMobile){
                getView().findViewById(R.id.list_banding).setVisibility(View.GONE);
            }else{
                getView().findViewById(R.id.list_banding).setVisibility(View.VISIBLE);
            }
        }
    }
    private void setReadMag() {
        if(SJQApp.messageBean != null && SJQApp.messageBean.getResult() != null) {
            if (SJQApp.user != null) {
                int numMessage = SJQApp.messageBean.getResult().getNumMessage();
                int numComment = SJQApp.messageBean.getResult().getNumComment();
                int numPostsGood = SJQApp.messageBean.getResult().getNumPostsGood();
                if (numMessage == 0) {
                    message_msg.setVisibility(View.GONE);
                } else {
                    message_msg.setVisibility(View.VISIBLE);
                    message_msg.setText(numMessage + "");
                }
                if (numComment == 0) {
                    pinluen_msg.setVisibility(View.GONE);
                } else {
                    pinluen_msg.setVisibility(View.VISIBLE);
                    pinluen_msg.setText(numComment + "");
                }
                if (numPostsGood == 0) {
                    dianzan_msg.setVisibility(View.GONE);
                } else {
                    dianzan_msg.setVisibility(View.VISIBLE);
                    dianzan_msg.setText(numPostsGood + "");
                }

            }
        }else{
            message_msg.setVisibility(View.GONE);
            pinluen_msg.setVisibility(View.GONE);
            dianzan_msg.setVisibility(View.GONE);
        }
    }
    private void toastShow(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT)
                .show();
    }
    private class MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("ReadMag")){
                setReadMag();
            }
            if(action.equals("showView")){
                showView();
            }
        }
    }
    private void setdenlu(String name){
        Intent intent = new Intent();
        intent.setAction("denglu");
        intent.putExtra("name",name);
        getActivity().sendBroadcast(intent);
    }
}
