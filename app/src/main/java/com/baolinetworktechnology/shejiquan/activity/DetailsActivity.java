package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.DeatilsAdapter;
import com.baolinetworktechnology.shejiquan.adapter.NineGridTest2Adapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.BrandBean;
import com.baolinetworktechnology.shejiquan.domain.BrandModel;
import com.baolinetworktechnology.shejiquan.domain.BrandModelBean;
import com.baolinetworktechnology.shejiquan.domain.DetailsModel;
import com.baolinetworktechnology.shejiquan.domain.DetailsModelBean;
import com.baolinetworktechnology.shejiquan.domain.brandSidleList;
import com.baolinetworktechnology.shejiquan.domain.paramsMoel;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.KoyListView;
import com.baolinetworktechnology.shejiquan.view.ScrollGridLayoutManager;
import com.guojisheng.koyview.GScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.socialize.utils.Log;

import java.util.List;

public class DetailsActivity extends BaseActivity implements View.OnClickListener{
    private BitmapUtils mImageUtil;
    private String sjName;
    private String sjImage;
    private String spGuid;
    private GScrollView mScroll;
    private View mTitleBg,title_wed;
    private ImageView mImageView,backView;
    private TextView text_titvi,deta_title,tv_user;
    private CircleImg userLogo;
    private CommonAdapter<paramsMoel> hzAdapter;
    private KoyListView myKoylist;
    private WebView xq_webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mImageUtil = new BitmapUtils(this);
        mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

        sjName =getIntent().getStringExtra("sjName");
        sjImage =getIntent().getStringExtra("sjImage");
        spGuid =getIntent().getStringExtra("spGuid");

        inviwe();
        GetDesigner();
        mScrollOnTou();
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
    private void inviwe() {
        mTitleBg = findViewById(R.id.titleBg);
        mScroll = (GScrollView) findViewById(R.id.scroll);
        mImageView = (ImageView) findViewById(R.id.ivBg);
        backView = (ImageView) findViewById(R.id.back);
        backView.setOnClickListener(this);
        title_wed = findViewById(R.id.title_wed);
        title_wed.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
        text_titvi = (TextView) findViewById(R.id.text_titvi);
        deta_title = (TextView) findViewById(R.id.deta_title);
        userLogo = (CircleImg) findViewById(R.id.userLogo);
        tv_user = (TextView) findViewById(R.id.tv_user);
        findViewById(R.id.tvFromCity).setOnClickListener(this);

        xq_webView = (WebView) findViewById(R.id.xq_webView);
        WebSettings settings2 = xq_webView.getSettings();
        settings2.setJavaScriptEnabled(true);// 能够执行JS代码
        settings2.setRenderPriority(WebSettings.RenderPriority.HIGH);//
        // 提高渲染的优先级
        settings2.setBlockNetworkImage(true);
        // 设置 缓存模式
        settings2.setAppCacheEnabled(true);
        settings2.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings2.setDomStorageEnabled(true);
        xq_webView.setWebViewClient(new WebViewClient() {
            // @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (xq_webView == null)
                    return;
                // mWebview.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                if (xq_webView.getSettings().getBlockNetworkImage()) {
//                    addImageClickListner2();
                    xq_webView.getSettings().setBlockNetworkImage(false);
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                xq_webView.loadUrl(url); //在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                return true;
            }
            //加载https时候，需要加入 下面代码
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("onReceivedSslError: ",""+error.getPrimaryError());
                handler.proceed(); //接受所有证书
            }
        });

        myKoylist = (KoyListView) findViewById(R.id.myKoylist);
        hzAdapter = new CommonAdapter<paramsMoel>(DetailsActivity.this,
                R.layout.deta_list_item) {

            @Override
            public void convert(ViewHolder holder, paramsMoel item) {
                holder.setText(R.id.chenpin, item.getParamKey());
                holder.setText(R.id.xianqin, item.getParamValue());
            }
        };
        myKoylist.setAdapter(hzAdapter);
        myKoylist.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mScroll.setImageView(mImageView);
        mScroll.setMaxHeight(260);
        mTitleBg.setAlpha(1);
    }

    private void mScrollOnTou() {
        mScroll.setScrollListener(new GScrollView.ScrollListener() {

            @Override
            public void scrollOritention(int l, int t, int oldl, int oldt) {
                int t1=t;
                if(t1<255){
                    title_wed.setBackgroundColor(Color.argb((int) t, 255,255,255));
                    if(t1>150){

                        text_titvi.setVisibility(View.VISIBLE);
                        backView.setImageResource(R.drawable.nav_button_fhan_default1);
                    }else{
                        //魅族
                        CommonUtils.setMeizuStatusBarDarkIcon(DetailsActivity.this, false);
                        //小米
                        CommonUtils.setMiuiStatusBarDarkMode(DetailsActivity.this, false);
                        text_titvi.setVisibility(View.GONE);
                        backView.setImageResource(R.drawable.nav_button_fhan_default1);
                    }
                }else{
                    //		//魅族
                    CommonUtils.setMeizuStatusBarDarkIcon(DetailsActivity.this, true);
                    //小米
                    CommonUtils.setMiuiStatusBarDarkMode(DetailsActivity.this, true);
                    title_wed.setBackgroundColor(Color.argb((int) 255, 255,255,255));
                    backView.setImageResource(R.drawable.nav_button_fhan_default);
                    text_titvi.setVisibility(View.VISIBLE);
                }
                if(t1 <= 0){
                    title_wed.setBackgroundColor(Color.argb((int) 0, 255,255,255));
                    text_titvi.setVisibility(View.GONE);
                    backView.setImageResource(R.drawable.nav_button_fhan_default1);
                }
            }
        });
    }

    private void GetDesigner() {
        String url = AppUrl.GETBRANDPRODUCTDETIAL + spGuid;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                DetailsModelBean detailBean = CommonUtils.getDomain(
                        responseInfo, DetailsModelBean.class);

                if(detailBean!=null && detailBean.success){
                    if(detailBean!=null){
                        showDesignData(detailBean.result);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                toastShow(arg1);
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url), callBack);
    }
    private void showDesignData(DetailsModel result) {
        text_titvi.setText(result.getTitle());
        mImageUtil.display(mImageView, result.getSmallImages());
        mImageUtil.display(userLogo, sjImage);
        tv_user.setText(sjName);
        deta_title.setText(result.getTitle());
        hzAdapter.setData(result.getParams());
        hzAdapter.notifyDataSetChanged();

        StringBuffer htmlString2= new StringBuffer();
        htmlString2.append("<!DOCTYPE html><html><head>");
        htmlString2.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        htmlString2.append("<meta name=\"description\" />");
        htmlString2.append("<meta name=\"keywords\" />");
        htmlString2.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.1, maximum-scale=5.0, user-scalable=no\" />");
        htmlString2.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">");
        htmlString2.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content='black'>");
        htmlString2.append("<meta name=\"format-detection\" content=\"telephone=no\">");
        htmlString2.append("<script type=\"text/javascript\" src=\"http://m.sjq315.com/Scripts/jquery-1.10.2.min.js\"></script>");
        htmlString2.append("<style type=\"text/css\">");
        htmlString2.append("body{ font-size:16px; line-height:1.5em; padding:16px 0px; color:#3c3c46;}");
        htmlString2.append("img { max-width:98%;padding:0.2em 0;width:auto!important;height:auto!important;}");
        htmlString2.append("p{padding:0 2px;}");
        htmlString2.append("</style>");
        htmlString2.append("</head><body style=\"min-height:200px;\">");
        htmlString2.append(result.getContents());
        htmlString2.append("</body></html>");
        if(xq_webView!=null){
            xq_webView.loadDataWithBaseURL(null, htmlString2.toString(), "text/html","UTF-8", null);
        }
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tvFromCity:
                finish();
                break;
        }
    }
    // 注入js函数监听
    private void addImageClickListner2() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        xq_webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "     window.imagelistner.addImage(objs[i].src);  "
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeWebView();
    }
    private void removeWebView() {
        // 一定要销毁，否则无法停止播放
        if (xq_webView != null) {
            if (xq_webView.getRootView() != null) {
                ((ViewGroup) xq_webView.getRootView()).removeAllViews();
            }
            xq_webView.removeAllViews();
            xq_webView.destroy();
            xq_webView = null;
        }
    }
}
