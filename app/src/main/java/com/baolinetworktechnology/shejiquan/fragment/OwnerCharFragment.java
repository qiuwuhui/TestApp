package com.baolinetworktechnology.shejiquan.fragment;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baolinetwkchnology.shejiquan.xiaoxi.ChatFragment1;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.domain.SwOwnerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwOwnerDetailBean;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.EaseConstant;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerCharFragment extends BaseMainFragment{
    public OwnerCharFragment() {
    }
    private String designerGudi = "";
    private View view;
    public  String LoginName="";//联系人标识
    public  String NickName="";//联系人名字
    public  String UserLogo="";//联系人头像
    public  String UserGUID="";//联系GUID
//    private CountDownTimer time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null){
            view= inflater.inflate(R.layout.fragment_char, container, false);
            designerGudi=getActivity().getIntent().getStringExtra(AppTag.TAG_GUID);
        }
        return view;
    }
    private void showDesignData(final SwOwnerDetail owner) {

//        time = new CountDownTimer(500, 500) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//            @Override
//            public void onFinish() {
                 LoginName= CommonUtils.removeAllSpace(owner.getGuid());
                 NickName=owner.getName();
                 UserLogo=owner.getLogo();
                 UserGUID=owner.getGuid();

                if(SJQApp.user!=null){
                    if(DemoHelper.getInstance().isLoggedIn()){
                        if(!LoginName.equals("")){
                            DemoHelper.getInstance().putlianxi(LoginName, NickName, UserLogo);
//                            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//                            List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
//                            for (EMConversation conversation : conversations.values()) {
//                                if (conversation.getAllMessages().size() != 0) {
//                                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
//                                }
//                            }
//                            int aaaa=sortList.size();
//                            if(	sortList.size()==0){
//                                EMMessage msg1 = createReceivedTextMsg(LoginName);
//                                List<EMMessage> ms = new ArrayList<EMMessage>();
//                                ms.add(msg1);
//                                EMClient.getInstance().chatManager().importMessages(ms);
//                            }else {
//                                for (Pair<Long, EMConversation> sortItem : sortList) {
//                                    EMConversation emConversation=sortItem.second;
//                                    if(!emConversation.getUserName().equals(LoginName)){
//                                        -- aaaa;
//                                        if(aaaa==0){
//                                            EMMessage msg1 = createReceivedTextMsg(LoginName);
//                                            List<EMMessage> ms = new ArrayList<EMMessage>();
//                                            ms.add(msg1);
//                                            EMClient.getInstance().chatManager().importMessages(ms);
//                                        }
//                                    }else{
//                                        ++ aaaa;
//                                    }
//                                }
//                            }
                            ChatFragment1 chatFragment = new ChatFragment1();
                            Bundle args = new Bundle();
                            args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                            args.putString(EaseConstant.EXTRA_USER_ID, LoginName);
                            args.putString(EaseConstant.SHE_JI_SHI_GUID, UserGUID);
                            chatFragment.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatFragment).commit();
                        }else{
                            toastShow("暂时无法联系");
                        }
                    }else{
                        toastShow("账号聊天未开通，请联系客服开通账号");
                    }
                }
            }
//        };
//        time.start();
//    }
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
    public void GetDesigner() {
        String url = AppUrl.GET_PERSONAL_INFO + designerGudi;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SwOwnerDetailBean data= CommonUtils.getDomain(
                        responseInfo, SwOwnerDetailBean.class);
                if(data!= null && data.result != null){
                    showDesignData(data.result);
                }else {
                    toastShow("业主信息错误...");
                    getActivity().finish();
                }

            }
            @Override
            public void onFailure(HttpException error, String arg1) {
                toastShow("业主信息错误...");
                getActivity().finish();
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url), callBack);
    }

    @Override
    protected void lazyLoad() {
        GetDesigner();
    }
}
