package com.baolinetworktechnology.shejiquan.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baolinetwkchnology.shejiquan.xiaoxi.XiangQinActivity;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.BoundActivity;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiActivity;
import com.baolinetworktechnology.shejiquan.adapter.SJanliownAdater;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.DataBen;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.GongSiMyanliBean;
import com.baolinetworktechnology.shejiquan.domain.SheJiShianliBean;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.SwDesignerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.sjanliBean;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.NetAddRecord;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.DesignerHonorUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.HomeHeadsj;
import com.baolinetworktechnology.shejiquan.view.ListViewShop;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.PulldownFooter;
import com.baolinetworktechnology.shejiquan.view.RefreshListView;
import com.google.gson.Gson;
import com.guojisheng.koyview.GScrollView;
import com.hyphenate.chatuidemo.DemoHelper;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeiShopFragment extends BaseFragment implements View.OnClickListener{
    public WeiShopFragment() {
    }
    private ShareUtils mShareUtils;// 分享工具
    SwDesignerDetail mDesigner=new SwDesignerDetail();
    private GScrollView mScroll;
    private TextView tite_name,name,tvFromCity,tvCost,numView,numConcer
            ,fenxian,tv_conten,feng_ge,chong_ye,jian_jie,quan_bu,anli_shu,tvcontuer,no_anli;
    private View title_view,quan_layout,itemRecord,itemNumConcer,itemNumFans;
    private CircleImg mUserLogo;
    private BitmapUtils mImageUtil;
    private CheckBox tvConcer;
    private String designerGudi = "";
    private String designerID = "";
    private ImageView zi_ge,ren_zhen;
    boolean isMy = false;
    boolean isquanbu = false;
    private ListViewShop mListView;
    private int PageIndex=1;
//    private DesignerHonorUtils honorUtils;
    public  String LoginName="";//联系人标识
    public  String NickName="";//联系人名字
    public  String UserLogo="";//联系人头像
    private MyDialog dialog;
    public int Certification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout=inflater.inflate(R.layout.fragment_wei_shop, container, false);
//        honorUtils = new DesignerHonorUtils();
        mImageUtil = new BitmapUtils(getActivity());
        mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        designerGudi=getActivity().getIntent().getStringExtra(AppTag.TAG_GUID);
        designerID=getActivity().getIntent().getStringExtra(AppTag.TAG_ID);
        dialog=new MyDialog(getActivity());
        inviwe(layout);
        isConcer();
        GetDesigner();
        loadWeb();
        return layout;
    }
    private void inviwe(View layout) {
        mListView = (ListViewShop)layout.findViewById(R.id.myListView);
        HomeHeadsj head = new HomeHeadsj(getActivity());
        mListView.getRefreshableView().addHeaderView(head);
        mScroll = (GScrollView) layout.findViewById(R.id.scroll);
        tite_name = (TextView) layout.findViewById(R.id.tite_name);
        title_view = layout.findViewById(R.id.title_view);
        layout.findViewById(R.id.itemConcer).setOnClickListener(this);
        layout.findViewById(R.id.itemWeiTuo).setOnClickListener(this);
        tvConcer = (CheckBox) layout.findViewById(R.id.tvConcer);
        tvcontuer =(TextView) layout.findViewById(R.id.tvcontuer);
        mUserLogo = (CircleImg) layout.findViewById(R.id.userLogo);
        ren_zhen =(ImageView) layout.findViewById(R.id.ren_zhen);
        zi_ge =(ImageView) layout.findViewById(R.id.zi_ge);
        name = (TextView) layout.findViewById(R.id.name);
        tvFromCity = (TextView) layout.findViewById(R.id.tvFromCity);
        tvCost = (TextView) layout.findViewById(R.id.tvCost);
        layout.findViewById(R.id.itemRecord).setOnClickListener(this);
        layout.findViewById(R.id.itemNumConcer).setOnClickListener(this);
        layout.findViewById(R.id.itemNumFans).setOnClickListener(this);
        numView = (TextView) layout.findViewById(R.id.numView);
        numConcer = (TextView) layout.findViewById(R.id.numConcer);
        fenxian = (TextView) layout.findViewById(R.id.fenxian);
        tv_conten = (TextView) layout.findViewById(R.id.tv_conten);
        feng_ge = (TextView) layout.findViewById(R.id.feng_ge);
        chong_ye = (TextView) layout.findViewById(R.id.chong_ye);
        jian_jie = (TextView) layout.findViewById(R.id.jian_jie);
        quan_bu = (TextView) layout.findViewById(R.id.quan_bu);
        quan_bu.setOnClickListener(this);
        anli_shu = (TextView) layout.findViewById(R.id.anli_shu);
        no_anli = (TextView) layout.findViewById(R.id.no_anli);
        quan_layout=layout.findViewById(R.id.quan_layout);
        mListView.setDesignerGudi(designerGudi);
    }
    public void setTvConcer(){
        tvConcer.setText("关注");
        tvcontuer.setVisibility(View.VISIBLE);
    }
    public void setTvConcerTrue(){
        tvConcer.setText("已关注");
        tvcontuer.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.back:
//                finish();
//                break;
            case R.id.quan_bu:
                if(isquanbu){
                    isquanbu=false;
//                    TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                            -1.0f);
//                    mHiddenAction.setDuration(500);
//                    quan_layout.startAnimation(mHiddenAction);
                    quan_layout.setVisibility(View.GONE);
                    quan_bu.setText("更多简介");
                }else{
                    isquanbu=true;
//                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//                    mShowAction.setDuration(500);
//                    quan_layout.startAnimation(mShowAction);
                    quan_layout.setVisibility(View.VISIBLE);
                    quan_bu.setText("收起");
                }
                break;
            case R.id.itemConcer:
                doConcer();
                break;
            case R.id.itemWeiTuo:
                MobclickAgent.onEvent(getActivity(),"kControlDesignerHomePageConslution","zhixun");//设计师主页聊天界面调用次数
                if(SJQApp.user==null){
                    go2Login();
                }else if(TextUtils.isEmpty(SJQApp.user.Mobile)){
                    gotBound("免费咨询需绑定手机号");
                }else if(Certification !=3){
                    toastShow("该设计师尚未开通此服务");
                }else{
                    if(SJQApp.user.guid.equals(designerGudi)){
                        toastShow("自己不能和自己聊天");
                    }else{
                        if(DemoHelper.getInstance().isLoggedIn()){
                            if(!LoginName.equals("")){
                                  WeiActivity  weiActivity= (WeiActivity) getActivity();
                                  weiActivity.setMessamg();
//                                DemoHelper.getInstance().putlianxi(LoginName, NickName, UserLogo);
//                                Intent intent=new Intent(getActivity(),XiangQinActivity.class);
//                                intent.putExtra("UserName", LoginName);
//                                startActivity(intent);
                            }else{
                                toastShow("暂时无法联系");
                            }
                        }else{
                            toastShow("账号异常，请退出后重新登录");
                        }
                    }
                }
                break;
            default:
                break;
        }

    }
    /**
     * 关注动作
     */
    public void doConcer() {
        if (SJQApp.user == null) {
            MobclickAgent.onEvent(getActivity(),"kControlDesignerHomePageAttention","Notlogged_add");// 未登录点击设计师主页关注次数
            toastShow("请先登录");
            go2Login();
            return;
        }
        if (TextUtils.isEmpty(designerGudi)) {
            toastShow("请稍候");
            return;
        }
        SJQApp.isRrefresh = true;
        if ("已关注".equals(tvConcer.getText().toString())) {
            MobclickAgent.onEvent(getActivity(),"kControlDesignerHomePageAttention","logged_canceol");// 登录点击设计师主页取消关注次数
            // 取消关注
            tvConcer.setText("取消中..");
            String url = ApiUrl.FAVORITE_CANCEL;
            RequestParams params = getParams(url);
            params.addBodyParameter("ClassType", AppTag.Sql_sheji);
            params.addBodyParameter("Type", AppTag.Sql_gz);
            params.addBodyParameter("UserGUID", SJQApp.user.guid);
            params.addBodyParameter("FromGUID", designerGudi);

            RequestCallBack<String> callBack = new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    LogUtils.i("设计师详情头部", arg0.result);
                    DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
                    if (data != null) {
                        if (data.success) {
                            setTvConcer();
                            tvConcer.setChecked(false);
                            if (SJQApp.userData != null) {
                                SJQApp.userData.numConcer--;
                            }
                        } else {
                            setTvConcerTrue();
                            tvConcer.setChecked(true);
                            toastShow("取消失败");
                            isConcer();
                        }
                    } else {
                        toastShow("取消失败");
                        setTvConcerTrue();
                        tvConcer.setChecked(true);

                    }
                }

                @Override
                public void onFailure(HttpException error, String arg1) {
                    LogUtils.i("erro:设计师详情头部", arg1);
                    setTvConcer();
                    tvConcer.setChecked(false);
                    toastShow(getResources().getString(R.string.network_error));
                    AppErrorLogUtil.getErrorLog(getActivity()).postError(
                            error.getExceptionCode() + "", "POST",
                            ApiUrl.FAVORITE_CANCEL);
                }
            };
            getHttpUtils().send(HttpRequest.HttpMethod.POST, ApiUrl.API + url, params,
                    callBack);

        } else {
            MobclickAgent.onEvent(getActivity(),"kControlDesignerHomePageAttention","logged_add");// 登录点击设计师主页关注次数
            // 加关注
            if (SJQApp.user.guid.equals(designerGudi)) {
                toastShow("无需关注自己");
                return;
            }
            tvConcer.setText("关注中..");
            String url = ApiUrl.FAVORITE_ADD;
            RequestParams params = getParams(url);
            params.addBodyParameter("Type", "1");
            params.addBodyParameter("ClassType", "5");
            params.addBodyParameter("UserGUID", SJQApp.user.guid);
            params.addBodyParameter("FromGUID", designerGudi);

            RequestCallBack<String> callBack = new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> arg0) {
                    DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
                    if (data != null) {
                        if (data.success) {
                            setTvConcerTrue();
                            if (SJQApp.userData != null) {
                                SJQApp.userData.numConcer++;
                            }
                            tvConcer.setChecked(true);

                        } else {
                            setTvConcer();
                            tvConcer.setChecked(false);

                            toastShow("关注失败");
                            isConcer();
                        }
                    } else {
                        toastShow("关注失败");
                        setTvConcer();
                        tvConcer.setChecked(false);

                    }
                }

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    setTvConcer();
                    tvConcer.setChecked(false);

                    toastShow(getResources().getString(R.string.network_error));
                }
            };
            getHttpUtils().send(HttpRequest.HttpMethod.POST, ApiUrl.API + url, params,
                    callBack);

        }
    }
    /**
     * 去登录
     */
    private void go2Login() {
        MobclickAgent.onEvent(getActivity(),"kControlDesignerHomePageLoginAlert");//设计师主页登录警告框弹出次数
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mShareUtils == null) {
            mShareUtils = new ShareUtils(getActivity());
        }
    }
    private void loadWeb() {
        isConcer();
        new NetAddRecord(designerGudi);// 添加到访客记录
    }
    /**
     * 是否关注
     */
    void isConcer() {
        if (SJQApp.user == null) {

            return;
        }
        if (isMy)
            return;
        String url = ApiUrl.IS_Favorite;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = getParams(url);
        params.addBodyParameter("Type", AppTag.Sql_gz);
        params.addBodyParameter("ClassType", AppTag.Sql_sheji);
        params.addBodyParameter("UserGUID", SJQApp.user.guid);
        if (!TextUtils.isEmpty(designerGudi)) {
            params.addBodyParameter("FromGUID", designerGudi);
        } else if (mDesigner != null) {
            params.addBodyParameter("FromGUID", mDesigner.guid);
        } else {
            return;
        }
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                DataBen data = CommonUtils.getDomain(arg0, DataBen.class);
                if (data != null) {
                    if (data.success) {

                        setTvConcerTrue();
                        tvConcer.setChecked(true);
                    } else {
                        setTvConcer();
                        tvConcer.setChecked(false);

                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
//                AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
//                        error.getExceptionCode() + "", "POST",
//                        ApiUrl.IS_Favorite);
            }
        };
        httpUtils.send(HttpRequest.HttpMethod.POST, ApiUrl.API + url, params, callBack);
    }
    private void GetDesigner() {
        String url = AppUrl.GET_DESIGNER_INFO + designerID;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SwDesignerDetailBean detailBean = CommonUtils.getDomain(
                        responseInfo, SwDesignerDetailBean.class);

                if(detailBean!=null && detailBean.success){
                    if(detailBean!=null){
                        showDesignData(detailBean.result);
                        mDesigner=detailBean.result;
                    }
                }
            }
            @Override
            public void onFailure(HttpException error, String arg1) {
                toastShow("获取设计师信息失败");
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url), callBack);

    }
    //显示数据
    private void showDesignData(SwDesignerDetail data) {
        name.setText(data.name);
        mImageUtil.display(mUserLogo, data.logo);
        if(data.cityID==0){
            tvFromCity.setText("未填写地址"+"　"+getWorkYear(data.officeTime)+"年设计经验");
        }else{
            tvFromCity.setText(getfromCity(data.cityID)+"　"+getWorkYear(data.officeTime)+"年设计经验");
        }
        tvCost.setText("设计费用：" + data.getCost());
        fenxian.setText("" + data.getNumShare());
        numConcer.setText("" + data.getNumFans());
        numView.setText("" + data.getNumVisit());
        tv_conten.setText(data.designing);
        if(data.desStyle==null){
            feng_ge.setText("擅长风格："+"未填写");
        }else{
            feng_ge.setText("擅长风格："+data.getStrStyle(getActivity()));
        }
        if(data.officeTime!=null && !data.officeTime.equals("") ){
            chong_ye.setText("从业时间："+getWorkYear(data.officeTime)+"年");
        }
        jian_jie.setText(data.comment);
        Certification=data.isCertification;
//      honorUtils.setIcon(ren_zhen,data.IsCertification,zi_ge ,data.getServiceStatus());
        anli_shu.setText(data.numCase+" 套案例");
//        if(data.LoginName!=null){
//            LoginName=data.LoginName;
//            NickName=data.NickName;
//            UserLogo=data.UserLogo;
//        }
    }
   public  int  getWorkYear(String OfficeTime){
       if(OfficeTime.equals("")){
           return 0;
       }
       int Time=Integer.parseInt(OfficeTime);
       final Calendar c = Calendar.getInstance();
       int mYear = c.get(Calendar.YEAR); //获取当前年份
       return mYear-Time;
   }
    //增加设计师被分享数
    public void addDesign() {

        if(designerGudi.equals("")){
            toastShow("数据出错");
            return;
        }
        String url = ApiUrl.UPDATEDESIGN+designerGudi;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET 	, ApiUrl.API + url, getParams(url),
                callBack);
    }
    //获取设计师所在地区
    private String getfromCity(int CityID){
        CityService cityService = CityService.getInstance(getActivity());
        if (cityService == null)
            return null;
        String shi=cityService.fromCityID(CityID);
        return shi;
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
    /**
     * 分享
     */
    public void doShare() {
        if (mShareUtils != null) {
            if (mDesigner != null) {
                mShareUtils
                        .setShareUrl("http://www.sjq315.com/works/"+mDesigner.id+".html")
                        .setShareTitle(mDesigner.name + "的微名片")
                        .setShareContent(
                                mDesigner.profession + "\n"
                                        + mDesigner.workYear + "\n"
                                        + mDesigner.fromCityName)
                        .setImageUrl(mDesigner.logo).addToSocialSDK().doShare();
            }else {
                toastShow("数据出错");
            }
        }
    }
}
