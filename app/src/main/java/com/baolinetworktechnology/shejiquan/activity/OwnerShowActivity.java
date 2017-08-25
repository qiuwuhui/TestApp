package com.baolinetworktechnology.shejiquan.activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.SwOwnerPotAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.PostMainBean;
import com.baolinetworktechnology.shejiquan.domain.SwOwnerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwOwnerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.YouLikemode;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.PulldownFooter;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class OwnerShowActivity extends BaseFragmentActivity implements View.OnClickListener,RefreshListView.ILoadData {
    private BitmapUtils mImageUtil;
    private String guid;
    CircleImg sf_iv_user;
    TextView sf_tv_name,tvFromCity,tvMessage;
    ImageView guanzhu_tv;
    private RefreshListView mListViewOwner;
    private SwOwnerPotAdapter adapter;
    private View include;
    private ImageView backView;
    private TextView tv_chent;
    private SwOwnerDetail ownerBen;
    private int nullDdrawable = R.drawable.icon_my_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_show);
        mImageUtil = new BitmapUtils(this);
        mImageUtil.configDefaultLoadingImage(R.drawable.menu_icon_mrtx_default);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.menu_icon_mrtx_default);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        inviwe();
        getOwner();
        isConcer();
        setTvConcer();
    }

    private void inviwe() {
        guid=getIntent().getStringExtra(AppTag.TAG_GUID);
        include =findViewById(R.id.include);
        include.setOnClickListener(this);
        mListViewOwner = (RefreshListView) findViewById(R.id.listViewOwner);
        mListViewOwner.getPulldownFooter().isShowBottom(true);
        mListViewOwner.setOnLoadListener(this);
        View view_integra = LayoutInflater.from(getApplicationContext()).inflate(R.layout.owner_add_time, null);

        mListViewOwner.getRefreshableView().addHeaderView(view_integra);
        mListViewOwner.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mListViewOwner.getLoadingLayoutProxy().setLastUpdatedLabel("正在拼命加载中...");
        mListViewOwner.onRefreshComplete();
        adapter =new SwOwnerPotAdapter(getActivity());
        mListViewOwner.setAdapter(adapter);
        mListViewOwner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if(position==1){
                    return;
                }
                PostBean item=adapter.getItem(position-2);
                Intent intent = new Intent(OwnerShowActivity.this, PostDetailsActivity.class);
                intent.putExtra(AppTag.TAG_GUID,item.getGuid());
                startActivity(intent);

            }
        });
        mListViewOwner.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (is_divPage
                        && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mListViewOwner.getmPulldownFooter().setState(PulldownFooter.STATE_LOGING);
                    mListViewOwner.loadData(false);
                   }
                }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                is_divPage = (firstVisibleItem + visibleItemCount == totalItemCount);
                if(firstVisibleItem ==0){
                    include.setBackgroundColor(Color.argb((int) 0, 255,255,255));
                    backView.setImageResource(R.drawable.nav_button_fhan_default1);
                    tv_chent.setVisibility(View.GONE);
                }else{
                    include.setBackgroundColor(Color.argb((int) 255, 255,255,255));
                    backView.setImageResource(R.drawable.nav_button_fhan_default);
                    tv_chent.setVisibility(View.VISIBLE);
                }
             }
        });

        backView = (ImageView) findViewById(R.id.back);
        backView.setOnClickListener(this);
        tv_chent = (TextView) findViewById(R.id.tv_chent);
        sf_iv_user = (CircleImg) view_integra.findViewById(R.id.sf_iv_user);
        sf_tv_name = (TextView) view_integra.findViewById(R.id.sf_tv_name);
        tvFromCity = (TextView) view_integra.findViewById(R.id.tvFromCity);
        tvMessage = (TextView) view_integra.findViewById(R.id.tvMessage);
        tvMessage.setOnClickListener(this);
        guanzhu_tv = (ImageView) view_integra.findViewById(R.id.guanzhu_tv);
        guanzhu_tv.setOnClickListener(this);
    }
    private boolean is_divPage;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.include:
                setTabClick();
                break;
            case R.id.tvMessage:
                if(SJQApp.user==null){
                    ((SJQApp) getApplication()).exitAccount();
                    startActivity(new Intent(OwnerShowActivity.this,
                            LoginActivity.class));
                }else if(!SJQApp.user.isBindMobile) {
                    gotBound("免费咨询需绑定手机号");
                }
                else{
                    if(SJQApp.user.guid.equals(guid)){
                        toastShow("自己不能和自己聊天");
                    }else{
                        if(DemoHelper.getInstance().isConnected()){
                            String LoginName=CommonUtils.removeAllSpace(ownerBen.getGuid());
                            DemoHelper.getInstance().putlianxi(LoginName, ownerBen.getName(), ownerBen.getLogo());
                            Intent intent=new Intent(OwnerShowActivity.this,XiangQinActivity.class);
                            intent.putExtra("UserName", LoginName);
                            startActivity(intent);
                        }else{
                            String LoginName=CommonUtils.removeAllSpace(SJQApp.user.getGuid());
                            setdenlu(LoginName);
                            toastShow("账号离线正在重新登录");
                        }
                    }
                }
                break;
            case R.id.guanzhu_tv:
                if(SJQApp.user==null) {
                    ((SJQApp) getApplication()).exitAccount();
                    startActivity(new Intent(OwnerShowActivity.this,
                            LoginActivity.class));
                    return;
                }
                if(SJQApp.user.guid.equals(guid)) {
                    toastShow("自己不能关注自己");
                    return;
                }
                doConcer();
                break;
        }
    }
    private void getOwner(){
        String url = AppUrl.GET_PERSONAL_INFO + guid;
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SwOwnerDetailBean data= CommonUtils.getDomain(
                        responseInfo, SwOwnerDetailBean.class);
                if(data!= null && data.result != null){
                    showOwner(data.result);
                }else{
                    toastShow("业主信息错误...");
                    finish();
                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                toastShow("业主信息错误...");
                finish();
            }
        });
    }
    private void showOwner(SwOwnerDetail owner){
        ownerBen = owner;
        if(owner==null){
            toastShow("业主信息错误...");
            finish();
            return;
        }
        mImageUtil.display(sf_iv_user, owner.getLogo());
        sf_tv_name.setText(owner.getName());
        tv_chent.setText(owner.getName());
        if(owner.getnProvinceID()!=0 && owner.getnCityID()!=0){
            tvFromCity.setText(getfromCity(owner.getnProvinceID(),owner.getnCityID()));
        }else{
            tvFromCity.setText("地址暂未填写");
        }
        loadata();
//        ownerRecycFragment.setShuaxin(owner.getGuid());
    }
    private String getfromCity(int ProvinceID,int CityID){
        CityService cityService = CityService.getInstance(this);
        if (cityService == null)
            return null;
        String shengshi=cityService.fromCity(ProvinceID, CityID);
        return shengshi;
    }
     private void loadata() {
        String url = AppUrl.GETPAGELIST+"pageSize=10"+"&currentPage="+PageIndex+"&userGUID="+guid+"&IsRefresh=true";
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String message) {
                mListViewOwner.setOnComplete();
                toastShow("网络连接错误，请检查网络");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject json;
                PostMainBean bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(result1, PostMainBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mListViewOwner.setOnComplete();
                if (bean == null || bean.listData==null || bean.listData.size() ==0 ) {
                    if(bean.recordCount==0){
                        mListViewOwner.setOnNullData("还没有发布帖子!");
                        mListViewOwner.setNullData(nullDdrawable);
                    }else{
                        mListViewOwner.setOnNullNewsData();
                    }
                    mListViewOwner.setMode(PullToRefreshBase.Mode.DISABLED);
                }
                if (bean!= null &&  bean.listData != null) {
                    if(bean.listData.size()<10){
                        mListViewOwner.setMode(PullToRefreshBase.Mode.DISABLED);
                        mListViewOwner.setOnNullNewsData();
                    }
                    if(PageIndex==1){
                        adapter.setData(bean.listData);
                    }else{
                        adapter.addData(bean.listData);
                    }
                }
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);

    }
    private int PageIndex=1;
    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {		// 刷新
            PageIndex = 1;
        } else{
            PageIndex++;
        }
        loadata();
    }
    public void setTabClick() {
        if (getActivity() == null)
            return;
        if (mListViewOwner == null
                || mListViewOwner.getRefreshableView() == null) {
            go2Splash();
            return;
        }
        int position = mListViewOwner.getRefreshableView()
                .getFirstVisiblePosition();
        if (position == 0) {
        } else {
            if (position > 5) {
                mListViewOwner.getRefreshableView().setSelection(5);
            }
            mListViewOwner.getRefreshableView().smoothScrollToPosition(0);
        }

    }
    private void setdenlu(String name){
        Intent intent = new Intent();
        intent.setAction("denglu");
        intent.putExtra("name",name);
        sendBroadcast(intent);
    }
    private void gotBound(String strshwo){
        View dialogView = View.inflate(getActivity(),
                R.layout.dialog_collect, null);
        TextView titl = (TextView) dialogView
                .findViewById(R.id.dialog_title);
        titl.setText(strshwo);
        final AlertDialog ad = new AlertDialog.Builder(
                getActivity()).setView(dialogView).show();
        dialogView.findViewById(R.id.dialog_cancel)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ad.cancel();
                    }
                });
        dialogView.findViewById(R.id.dialog_ok)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra(AppTag.TAG_JSON, SJQApp.user.getMobile());
                        intent.setClass(getActivity(),
                                BoundActivity.class);
                        startActivity(intent);
                        ad.cancel();
                    }});
    }
    private boolean isConcer =false;
    public void setTvConcer(){
        guanzhu_tv.setImageResource(R.drawable.sj_guanzhu);
        isConcer = false;
    }
    public void setTvConcerTrue(){
        guanzhu_tv.setImageResource(R.drawable.sj_yi_guanzhu);
        isConcer =true;
    }
    /**
     * 关注动作
     */
    public void doConcer() {
        dialog.show();
        if (SJQApp.user == null) {
            MobclickAgent.onEvent(OwnerShowActivity.this,"kControlDesignerHomePageAttention","Notlogged_add");// 未登录点击设计师主页关注次数

            toastShow("请先登录");
            go2Login();
            return;
        }
        if (TextUtils.isEmpty(guid)) {
            toastShow("请稍候");
            return;
        }

        SJQApp.isRrefresh = true;
        String url ="";
        if (isConcer) {
            url = AppUrl.DELETAEASEFRIEND;
          // 登录点击设计师主页取消关注次数
        } else {
            url = AppUrl.ADDEASEFRIEND;
        }
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("userGuid", SJQApp.user.guid);
            param.put("fromGuid", guid);
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getHttpUtils(2*100000).send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        if (dialog == null)
                            return;
                        dialog.dismiss();
                        toastShow("当前网络连接失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> n) {
                        if (dialog == null)
                            return;
                        dialog.dismiss();
                        Gson gson = new Gson();
                        SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                        if (bean != null) {
                            if (bean.result.operatResult) {
                                if (isConcer) {
                                    toastShow("取消关注成功");
                                    setTvConcer();
                                }else{
                                    toastShow("关注成功");
                                    setTvConcerTrue();
                                }
                                Intent intent = new Intent();
                                intent.setAction("qiehuan");
                                sendBroadcast(intent);
                            }else{
                                if (isConcer) {
                                    toastShow("取消关注失败");
                                }else{
                                    toastShow("关注失败");
                                }
                            }
                        }

                    }
                });
    }
    /**
     * 是否关注
     */
    void isConcer() {
        if (SJQApp.user == null) {
            return;
        }
        String url = AppUrl.CHECKISFRIRND+SJQApp.user.guid+
                "&friendGuid="+guid;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Gson gson = new Gson();
                SwresultBen bean=gson.fromJson(arg0.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        isConcer = true;
                        setTvConcerTrue();
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
                        error.getExceptionCode() + "", "POST",
                        ApiUrl.IS_Favorite);
            }
        };
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUrl.API + url, callBack);

    }
    /**
     * 去登录
     */
    private void go2Login() {
        Intent intent = new Intent(OwnerShowActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    void go2Splash() {
        toastShow("抱歉程序出现问题，即将重启动");
        startActivity(new Intent(getActivity(), SkipActivity.class));
        finish();
    }
    protected void onDestroy() {
        super.onDestroy();
        if(mListViewOwner != null){
            mListViewOwner.setAdapter(null);
        }
    }
}
