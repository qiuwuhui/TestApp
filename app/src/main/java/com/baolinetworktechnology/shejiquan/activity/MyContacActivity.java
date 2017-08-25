package com.baolinetworktechnology.shejiquan.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CricleBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.goodsList;
import com.baolinetworktechnology.shejiquan.domain.postcommentList;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.NineGridTestLayout;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyContacActivity extends BaseActivity implements View.OnClickListener {
   private String PostsGuid;
   private  NineGridTestLayout imageLayout;
   private CircleImg shequ_loge;
   private TextView userName,contents;
   private TextView chakan,tiem_post;
   private TextView dianzan,dian_lay;
   private LinearLayout itemReplys,zong_layou;
   private View figureLay;
   private BitmapUtils mImageUtil;
   private PopupWindow mPopupWindow;
   private CricleBean criBean;

   private RelativeLayout mLoadingOver;
   private InputMethodManager imm;
   private View item_pingluen;
   private EditText pingluen_text;
   private Button fasong_btn;
   private String FromID="0";
   private String Fromguid="";
   private String replayUserName="";
   private String ownerGUID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contac);
        mImageUtil = new BitmapUtils(getApplicationContext());
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        PostsGuid = getIntent().getStringExtra(AppTag.TAG_GUID);
        intiView();
        Getpost();
    }

    @Override
    protected void setUpViewAndData() {

    }
    @Override
    protected void restartApp() {
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
        switch (action) {
            case AppStatusConstant.ACTION_RESTART_APP:
                restartApp();
                break;
        }
    }
    private void intiView() {
        imageLayout = (NineGridTestLayout)findViewById(R.id.layout_nine_grid);
        shequ_loge = (CircleImg) findViewById(R.id.shequ_loge);
        userName = (TextView) findViewById(R.id.userName);
        contents = (TextView) findViewById(R.id.contents);
        chakan = (TextView) findViewById(R.id.chakan);
        dianzan = (TextView) findViewById(R.id.dianzan);
        dian_lay = (TextView) findViewById(R.id.dian_lay);
        itemReplys = (LinearLayout) findViewById(R.id.itemReplys);
        zong_layou = (LinearLayout) findViewById(R.id.zong_layou);
        tiem_post = (TextView) findViewById(R.id.tiem_post);
        figureLay = findViewById(R.id.figureLay);

        item_pingluen = findViewById(R.id.item_pingluen);
        pingluen_text = (EditText) findViewById(R.id.pingluen_text);
        pingluen_text.clearFocus();
        pingluen_text.addTextChangedListener(watcher);
        fasong_btn = (Button) findViewById(R.id.fasong_btn);
        fasong_btn.setOnClickListener(this);
        imm = (InputMethodManager) pingluen_text.getContext().getSystemService(
                getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
        mLoadingOver = (RelativeLayout) findViewById(R.id.loading_over2);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPopupWindow != null){
            mPopupWindow.dismiss();
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.fasong_btn:
                if(fasong_btn.getText().toString().equals("发送")){
                    if(SJQApp.user.isBindMobile){
                        doSubmit();
                    }else{
                        toastShow("绑定手机号后才能发评论");
                    }
                }else{
                    if (item_pingluen.getVisibility() == View.VISIBLE) {
                        FromID="0";
                        Fromguid ="";
                        replayUserName="";
                        ownerGUID="";
                        pingluen_text.setHint("");
                        pingluen_text.setText("");
                        item_pingluen.setVisibility(View.GONE);
                        imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
                    }
                }
                break;
        }
    }
    private void Getpost() {
        mLoadingOver.setVisibility(View.VISIBLE);
        if(PostsGuid==null){
            toastShow("获取内容出错");
            finish();
            return;
        }

        String url = AppUrl.GETCRICLEPOSTSDETIAL+PostsGuid;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                mLoadingOver.setVisibility(View.GONE);
                toastShow("获取内容错误");
                finish();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject json;
                CricleBean bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(result1, CricleBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (bean == null) {
                    toastShow("没有获取到内容");
                    finish();
                    return;
                }
                setNews(bean);
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url), callBack);
    }
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            String str = pingluen_text.getText().toString();
            if(str.equals("")){
                fasong_btn.setText("取消");
            }else{
                fasong_btn.setText("发送");
            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }
    };
    private void setNews(CricleBean cricleBean) {
       criBean =cricleBean;
       mImageUtil.display(shequ_loge,cricleBean.getUserInfo().logo);
       userName.setText(cricleBean.getUserInfo().name);
        String Contents =cricleBean.getContents().trim();
        if(TextUtils.isEmpty(Contents)){
            contents.setText("分享图片");
        }else{
           contents.setText(Contents);
        }
        List<String> pathList=new ArrayList<>();
        List<String> dapathList=new ArrayList<>();
        if(cricleBean.getPostsItemInfoList().size()!=0){
            for (int c = 0; c < cricleBean.getPostsItemInfoList().size(); c++){
                if(cricleBean.getPostsItemInfoList().size() == 1){
                    pathList.add(cricleBean.getPostsItemInfoList().get(c).path);
                }else{
                    pathList.add(cricleBean.getPostsItemInfoList().get(c).getSmallImages("_300_300."));
                }
                dapathList.add(cricleBean.getPostsItemInfoList().get(c).path);
            }
        }
        imageLayout.setUrlList(pathList);
        imageLayout.setdaUrlList(dapathList);
        imageLayout.setSpacing(10);
        tiem_post.setText(cricleBean.getFromatDate());
        //加载点赞人名字
        List<goodsList> strs =cricleBean.getGoods();
        String dzName ="";
        dianzan.setVisibility(View.GONE);
        dian_lay.setVisibility(View.GONE);
        if(strs != null && strs.size()!=0){
            dianzan.setVisibility(View.VISIBLE);
            dian_lay.setVisibility(View.VISIBLE);
            for (int i = 0; i < strs.size(); i++){
                if(i == strs.size()-1){
                    dzName +=strs.get(i).getGoodUserName();
                }else{
                    dzName +=strs.get(i).getGoodUserName()+"、";
                }
            }
        }
        dianzan.setText(dzName);
        //加载评论
        itemReplys.removeAllViews();
        itemReplys.setVisibility(View.GONE);
        List<postcommentList> commentlist= cricleBean.getComment();
        if(commentlist !=null &&commentlist.size()!=0){
            itemReplys.setVisibility(View.VISIBLE);
            for (int i = 0; i < commentlist.size(); i++){
                final int num = i;
                final postcommentList comment= commentlist.get(i);
                TextView replytv = new TextView(this);
                replytv.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                replytv.setPadding(0, 5, 0, 5);
                replytv.setTextColor(getResources().getColor(R.color.text_normal6));
                replytv.setTextSize(13);
                replytv.setBackgroundResource(R.drawable.peng_you_text);
                String publishName =comment.getOwnerName();
                String publishName1 =comment.getReplayUserName();
                if(TextUtils.isEmpty(publishName1)){
                    publishName= "<font color=\"#566a94\">"
                            +publishName+"：" + "</font>";
                    replytv.setText(Html.fromHtml(publishName + comment.getCommentContent()));
                }else {
                    publishName= "<font color=\"#566a94\">"
                            +publishName+"：" + "</font>";
                    publishName1= "<font color=\"#566a94\">"
                            +publishName1+"：" + "</font>";
                    replytv.setText(Html.fromHtml(publishName+"回复  "+ publishName1+ comment.getCommentContent()));
                }
                itemReplys.addView(replytv);
                replytv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ownerGUID=criBean.getComment().get(num).getOwnerGUID();
                        if(ownerGUID.equals(SJQApp.user.getGuid())){
                            toastShow("自己不能回复自己");
                            return;
                        }
                        Fromguid = criBean.getGuid();
                        FromID =criBean.getComment().get(num).getCommnentID()+"";
                        replayUserName =criBean.getComment().get(num).getOwnerName();
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
                        item_pingluen.setVisibility(View.VISIBLE);
                        pingluen_text.setFocusable(true);
                        pingluen_text.setFocusableInTouchMode(true);
                        pingluen_text.requestFocus();
                        pingluen_text.setText("");
                        pingluen_text.setHint("回复"+replayUserName+"：");
                    }
                });
            }
        }
        zong_layou.setVisibility(View.GONE);
        if(commentlist !=null &&commentlist.size()!=0){
        zong_layou.setVisibility(View.VISIBLE);
        }
        if(strs != null && strs.size()!=0){
          zong_layou.setVisibility(View.VISIBLE);
        }
        chakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int zanWidth =chakan.getWidth();
                int zanHeight = chakan.getHeight();
                View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.cricle_item_layout, null);
                TextView jibao = (TextView) popupView.findViewById(R.id.zan_item);
                View pin_item =popupView.findViewById(R.id.pin_item);
                jibao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                        SavePosts();
                    }
                });
                pin_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
//							 发送 一个无序广播
                        Fromguid = criBean.getGuid();
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
                        item_pingluen.setVisibility(View.VISIBLE);
                        pingluen_text.setFocusable(true);
                        pingluen_text.setFocusableInTouchMode(true);
                        pingluen_text.requestFocus();
                        pingluen_text.setHint("");
                        pingluen_text.setText("");

                    }
                });
                if(mPopupWindow == null){
                    boolean focusable = true;
                    mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, focusable);
                    mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
                }
                View anchor = chakan; //指定PopupWindow在哪个控件下面显示
                int xoff = -zanWidth;//指定PopupWindow在x轴方向上的偏移量
                int yoff = -zanHeight;//指定PopupWindow在Y轴方向上的偏移量
                mPopupWindow.showAsDropDown(anchor, xoff, yoff);
            }

        });
        mLoadingOver.setVisibility(View.GONE);
    }
    //评论
    private void doSubmit() {
        String Contents=pingluen_text.getText().toString();
        if(Contents.trim().length() < 1){
            dialog.dismiss();
            toastShow("输入内容不能为空");
            return;
        }
        String url = AppUrl.COMMENT_SEND;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("contents",Contents);
            param.put("fromGUID", Fromguid);
            param.put("markName", "8");
            param.put("fromID", FromID);
            param.put("userGUID", SJQApp.user.guid);
            param.put("anonymous", false);
            if (SJQApp.userData != null) {
                param.put("userName",SJQApp.userData.name);
                param.put("userLogo", SJQApp.userData.logo);
            } else if (SJQApp.ownerData != null) {
                param.put("userName",SJQApp.ownerData.getName());
                param.put("userLogo", SJQApp.ownerData.getLogo());
            }
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onStart() {
                super.onStart();
                // dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        addComment();
                    }else{
                        toastShow(bean.result.operatMessage);
                    }
                }
            }
            @Override
            public void onFailure(HttpException error, String arg1) {
                toastShow("网络请求发生错误");

            }
        };

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }
    private void addComment() {
        if (pingluen_text != null) {
            postcommentList postcommentList= new postcommentList();
            if (SJQApp.userData != null) {
                postcommentList.setOwnerName(SJQApp.userData.getName());
            } else if (SJQApp.ownerData != null) {
                postcommentList.setOwnerName(SJQApp.ownerData.getName());
            } else{
                postcommentList.setOwnerName(SJQApp.user.nickName);
            }
            postcommentList.setOwnerGUID(SJQApp.user.getGuid());
            postcommentList.setReplayUserName(replayUserName);
            postcommentList.setCommentContent(pingluen_text.getText().toString());
            List<postcommentList> postList= criBean.getComment();
            postList.add(postcommentList);
            criBean.setComment(postList);
            pingluen_text.setHint("");
            pingluen_text.setText("");
            FromID="0";
            Fromguid="";
            replayUserName="";
            ownerGUID="";
            item_pingluen.setVisibility(View.GONE);
            imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
            setNews(criBean);
        }
    }
    //点赞
    private void SavePosts(){
        String url = AppUrl.SAVEPOSTSGOOD;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("classType","0");
            param.put("sendUserGuid", SJQApp.user.guid);
            param.put("userGUID", criBean.getUserInfo().getGuid());
            if (SJQApp.userData != null) {
                param.put("sendUserLogo", SJQApp.userData.getLogo());
                param.put("sendUserName", SJQApp.userData.getName());
            } else if (SJQApp.ownerData != null) {
                param.put("sendUserLogo", SJQApp.ownerData.getLogo());
                param.put("sendUserName", SJQApp.ownerData.getName());
            } else{
                param.put("sendUserLogo", SJQApp.user.logo);
                param.put("sendUserName", SJQApp.user.nickName);
            }
            param.put("markName", "2");
            param.put("sendUserIdentity",  SJQApp.user.markName);
            param.put("postsId",  criBean.getId());
            param.put("postsGuid",criBean.getGuid());
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpUtils httpUtil = new HttpUtils(8 * 1000);
        httpUtil.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        toastShow("当前网络连接失败");

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> n) {
                        Gson gson = new Gson();
                        SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                        if (bean != null) {
                            if (bean.result.operatResult) {
                                List<goodsList> strs =	criBean.getGoods();
                                goodsList goodBena= new goodsList();
                                if (SJQApp.userData != null) {
                                    goodBena.setGoodUserName(SJQApp.userData.getName());
                                    goodBena.setGoodUserMarkName("DESIGNER");
                                    goodBena.setGoodUserGuid(SJQApp.user.guid);
                                    strs.add(goodBena);
                                } else if (SJQApp.ownerData != null) {
                                    goodBena.setGoodUserName(SJQApp.ownerData.getName());
                                    goodBena.setGoodUserMarkName("PERSONAL");
                                    goodBena.setGoodUserGuid(SJQApp.user.guid);
                                    strs.add(goodBena);
                                } else{
                                    goodBena.setGoodUserName(SJQApp.user.nickName);
                                    goodBena.setGoodUserMarkName("PERSONAL");
                                    goodBena.setGoodUserGuid(SJQApp.user.guid);
                                    strs.add(goodBena);
                                }
                                criBean.setGoods(strs);
                                setNews(criBean);
                            }
                        }
                    }
            });
    }
}
