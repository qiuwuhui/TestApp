package com.baolinetworktechnology.shejiquan.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.BrandHome;
import com.baolinetworktechnology.shejiquan.domain.BrandHomelBean;
import com.baolinetworktechnology.shejiquan.domain.BrandModel;
import com.baolinetworktechnology.shejiquan.domain.BrandModelBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.socialize.utils.Log;

public class BrandDetailsActivity extends BaseActivity {
    private String guid;
    private TextView tv_title;
    private WebView xq_webView,ry_webView,wh_webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_details);
        initView();
        GetDesigner();
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
    private void initView() {
        guid =getIntent().getStringExtra("guid");

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("品牌详情");
        tv_title.setTextSize(19);

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
                    addImageClickListner2();
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

        ry_webView = (WebView) findViewById(R.id.ry_webView);
        WebSettings settings = ry_webView.getSettings();
        settings.setJavaScriptEnabled(true);// 能够执行JS代码
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//
        // 提高渲染的优先级
        settings.setBlockNetworkImage(true);
        // 设置 缓存模式
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);

        ry_webView.setWebViewClient(new WebViewClient() {
            // @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (ry_webView == null)
                    return;
                // mWebview.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                if (ry_webView.getSettings().getBlockNetworkImage()) {
                    addImageClickListner();
                    ry_webView.getSettings().setBlockNetworkImage(false);
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ry_webView.loadUrl(url); //在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                return true;
            }
            //加载https时候，需要加入 下面代码
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("onReceivedSslError: ",""+error.getPrimaryError());
                handler.proceed(); //接受所有证书
            }
        });

        wh_webView = (WebView) findViewById(R.id.wh_webView);
        WebSettings settings1 = wh_webView.getSettings();
        settings1.setJavaScriptEnabled(true);// 能够执行JS代码
        settings1.setRenderPriority(WebSettings.RenderPriority.HIGH);//
        // 提高渲染的优先级
        settings1.setBlockNetworkImage(true);
        // 设置 缓存模式
        settings1.setAppCacheEnabled(true);
        settings1.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings1.setDomStorageEnabled(true);
        wh_webView.setWebViewClient(new WebViewClient() {
            // @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (wh_webView == null)
                    return;
                // mWebview.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                if (wh_webView.getSettings().getBlockNetworkImage()) {
                    addImageClickListner1();
                    wh_webView.getSettings().setBlockNetworkImage(false);
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wh_webView.loadUrl(url); //在2.3上面不加这句话，可以加载出页面，在4.0上面必须要加入，不然出现白屏
                return true;
            }
            //加载https时候，需要加入 下面代码
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.d("onReceivedSslError: ",""+error.getPrimaryError());
                handler.proceed(); //接受所有证书
            }
        });

    }
    private void GetDesigner() {
        String url = AppUrl.GETBRANDHONOR + guid;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BrandHomelBean detailBean = CommonUtils.getDomain(
                        responseInfo, BrandHomelBean.class);

                if(detailBean!=null && detailBean.success){
                    if(detailBean!=null){
                        showDesignData(detailBean.result);
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
    private void showDesignData(BrandHome result) {
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
        htmlString2.append(result.getBrandIntro());
        htmlString2.append("</body></html>");
        if(xq_webView!=null){
            xq_webView.loadDataWithBaseURL(null, htmlString2.toString(), "text/html","UTF-8", null);
        }
        StringBuffer htmlString= new StringBuffer();
        htmlString.append("<!DOCTYPE html><html><head>");
        htmlString.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        htmlString.append("<meta name=\"description\" />");
        htmlString.append("<meta name=\"keywords\" />");
        htmlString.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.1, maximum-scale=5.0, user-scalable=no\" />");
        htmlString.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">");
        htmlString.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content='black'>");
        htmlString.append("<meta name=\"format-detection\" content=\"telephone=no\">");
        htmlString.append("<script type=\"text/javascript\" src=\"http://m.sjq315.com/Scripts/jquery-1.10.2.min.js\"></script>");
        htmlString.append("<style type=\"text/css\">");
        htmlString.append("body{ font-size:16px; line-height:1.5em; padding:16px 0px; color:#3c3c46;}");
        htmlString.append("img { max-width:98%;padding:0.2em 0;width:auto!important;height:auto!important;}");
        htmlString.append("p{padding:0 2px;}");
        htmlString.append("</style>");
        htmlString.append("</head><body style=\"min-height:200px;\">");
        htmlString.append(result.getHonor());
        htmlString.append("</body></html>");
        if(ry_webView!=null){
            ry_webView.loadDataWithBaseURL(null, htmlString.toString(), "text/html","UTF-8", null);
        }

        StringBuffer htmlString1= new StringBuffer();
        htmlString1.append("<!DOCTYPE html><html><head>");
        htmlString1.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        htmlString1.append("<meta name=\"description\" />");
        htmlString1.append("<meta name=\"keywords\" />");
        htmlString1.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.1, maximum-scale=5.0, user-scalable=no\" />");
        htmlString1.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">");
        htmlString1.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content='black'>");
        htmlString1.append("<meta name=\"format-detection\" content=\"telephone=no\">");
        htmlString1.append("<script type=\"text/javascript\" src=\"http://m.sjq315.com/Scripts/jquery-1.10.2.min.js\"></script>");
        htmlString1.append("<style type=\"text/css\">");
        htmlString1.append("body{ font-size:16px; line-height:1.5em; padding:16px 0px; color:#3c3c46;}");
        htmlString1.append("img { max-width:98%;padding:0.2em 0;width:auto!important;height:auto!important;}");
        htmlString1.append("p{padding:0 2px;}");
        htmlString1.append("</style>");
        htmlString1.append("</head><body style=\"min-height:200px;\">");
        htmlString1.append(result.getCulture());
        htmlString1.append("</body></html>");
        if(wh_webView!=null){
            wh_webView.loadDataWithBaseURL(null, htmlString1.toString(), "text/html","UTF-8", null);
        }
    }
    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        ry_webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "     window.imagelistner.addImage(objs[i].src);  "
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");
    }
    // 注入js函数监听
    private void addImageClickListner1() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        wh_webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "     window.imagelistner.addImage(objs[i].src);  "
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");
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
        removeWebView1();
        removeWebView2();
    }
    private void removeWebView() {
        // 一定要销毁，否则无法停止播放
        if (ry_webView != null) {
            if (ry_webView.getRootView() != null) {
                ((ViewGroup) ry_webView.getRootView()).removeAllViews();
            }
            ry_webView.removeAllViews();
            ry_webView.destroy();
            ry_webView = null;
        }
    }
    private void removeWebView1() {
        // 一定要销毁，否则无法停止播放
        if (wh_webView != null) {
            if (wh_webView.getRootView() != null) {
                ((ViewGroup) wh_webView.getRootView()).removeAllViews();
            }
            wh_webView.removeAllViews();
            wh_webView.destroy();
            wh_webView = null;
        }
    }
    private void removeWebView2() {
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
