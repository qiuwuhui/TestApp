package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.BrandAcBean;
import com.baolinetworktechnology.shejiquan.domain.BrandAcListBean;
import com.baolinetworktechnology.shejiquan.domain.BrandBean;
import com.baolinetworktechnology.shejiquan.domain.BrandListBean;
import com.baolinetworktechnology.shejiquan.domain.BrandModel;
import com.baolinetworktechnology.shejiquan.domain.BrandModelBean;
import com.baolinetworktechnology.shejiquan.domain.Items;
import com.baolinetworktechnology.shejiquan.domain.brandSidleList;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.KoySliBrand;
import com.google.gson.Gson;
import com.guojisheng.koyview.GScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class BrandActivity extends BaseActivity implements View.OnClickListener {
    private String guid;
    private KoySliBrand mKoySliding;
    private CircleImg userLogo;
    private TextView tv_user,tvFromCity,tv_desc,munCase,text_titvi;
    private BitmapUtils mImageUtil;
    private PullToRefreshScrollView sc_scrollview;
    private int PageInde=1;
    private GridView pp_gridview;
    private View title_wed;
    private ImageView backView;
    private CommonAdapter<BrandAcBean> hzAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        mImageUtil = new BitmapUtils(this);
        mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxy);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxy);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

        guid =getIntent().getStringExtra("guid");
        if(TextUtils.isEmpty(guid)){
            toastShow("数据出错");
            return;
        }
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
        mKoySliding = (KoySliBrand) findViewById(R.id.koySliding);
        userLogo = (CircleImg) findViewById(R.id.userLogo);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tvFromCity = (TextView) findViewById(R.id.tvFromCity);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        munCase = (TextView) findViewById(R.id.munCase);
        pp_gridview = (GridView) findViewById(R.id.pp_gridview);
        title_wed = findViewById(R.id.title_wed);
        backView = (ImageView) findViewById(R.id.back);
        backView.setOnClickListener(this);
        title_wed.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
        text_titvi = (TextView) findViewById(R.id.text_titvi);
        hzAdapter = new CommonAdapter<BrandAcBean>(this,
                R.layout.item_brand) {

            @Override
            public void convert(ViewHolder holder, BrandAcBean item) {
                setImageUrl(holder.getView(R.id.zhenshu), item.getImages());
                holder.setText(R.id.spName, item.getTitle());
            }
        };
        hzAdapter.setDefaultLoadingImage(R.drawable.zixun_tu);
        pp_gridview.setAdapter(hzAdapter);
        pp_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        pp_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BrandAcBean item = hzAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(),
                        DetailsActivity.class);
                intent.putExtra("sjName", brandModel.getBrandName());
                intent.putExtra("sjImage",brandModel.getBrandLogo());
                intent.putExtra("spGuid", item.getGuid());
                startActivity(intent);
            }
        });

        sc_scrollview=(PullToRefreshScrollView) findViewById(R.id.scroll);
        //设置刷新的模式
        sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//both  可以上拉、可以下拉
        setPullToRefreshLable();
        sc_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                    PageInde++;
                    loadata();
            }
        });
        loadata();
    }
    private void mScrollOnTou() {
        sc_scrollview.setOnScrollChanged(new PullToRefreshScrollView.OnScrollChanged() {

            @Override
            public void onScroll(int scrollX, int scrollY) {
                int t1=scrollY;
                if(t1<255){
                    title_wed.setBackgroundColor(Color.argb((int) scrollY, 255,255,255));
                    if(t1>150){

                        backView.setImageResource(R.drawable.nav_button_fhan_default1);
                    }else{
                        //魅族
                        CommonUtils.setMeizuStatusBarDarkIcon(BrandActivity.this, false);
                        //小米
                        CommonUtils.setMiuiStatusBarDarkMode(BrandActivity.this, false);
                        text_titvi.setVisibility(View.GONE);
                        backView.setImageResource(R.drawable.nav_button_fhan_default1);
                    }
                }else{
                    //		//魅族
                    CommonUtils.setMeizuStatusBarDarkIcon(BrandActivity.this, true);
                    //小米
                    CommonUtils.setMiuiStatusBarDarkMode(BrandActivity.this, true);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_title2:

                break;

            default:
                break;
        }
    }
    private void GetDesigner() {
        String url = AppUrl.GETBRANDMODEL + guid;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BrandModelBean detailBean = CommonUtils.getDomain(
                        responseInfo, BrandModelBean.class);

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
    private  BrandModel brandModel;
    private void showDesignData(BrandModel result) {
        brandModel =result;
        brandSidleList dataList=new  brandSidleList();
        dataList.result =result.getBrandSidle();
        if ( dataList.result != null &&  dataList.result.size() != 0) {
			mKoySliding.setDatas(dataList);// 设置数据
//			mKoySliding.setAutoPlay();// 设置自动滚动
		}
        mImageUtil.display(userLogo, result.getBrandLogo());
        tv_user.setText(result.getBrandName());
        tvFromCity.setText(result.getBrandSlogan());
        if(!TextUtils.isEmpty(result.getBrandIntro())){
            String BrandSlogan=result.getBrandIntro().replaceAll("(<[^>]*>)|\\r|\\n|\\t|\\s*","");
//            String BrandSlogan=result.getBrandIntro().replaceAll("<p>|\\s*|\\t|\\n|\\r|\\s|(<[^>]*>)","");
            SpannableString spanableInfo = null;
            tv_desc.setHighlightColor(getResources().getColor(android.R.color.transparent));
            int BrandSleng=BrandSlogan.length();
            if(BrandSleng <= 50){
                spanableInfo = new SpannableString(BrandSlogan+"　查看更多");
                spanableInfo.setSpan(new Clickable(clickListener),BrandSleng,BrandSleng+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else{
                String BrandSlogan1 =BrandSlogan.substring(0,47);
                BrandSlogan1 =BrandSlogan1+"...";
                int BrandSleng1 =BrandSlogan1.length();
                spanableInfo = new SpannableString(BrandSlogan1+"　查看更多");
                spanableInfo.setSpan(new Clickable(clickListener),BrandSleng1,BrandSleng1+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv_desc.setText(spanableInfo);
            tv_desc.setMovementMethod(LinkMovementMethod.getInstance());
        }
        text_titvi.setText(result.getBrandName());
    }
    private void setPullToRefreshLable() {
        sc_scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("正在拼命加载中...");
        sc_scrollview.onRefreshComplete();
    }
    //获取设计师
    private void loadata() {
        String url = AppUrl.GETBRANDPRODUCTLIST+guid+"&pageSize=10"+"&currentPage="+PageInde;
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                sc_scrollview.onRefreshComplete();
               toastShow(message);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                JSONObject json;
                BrandAcListBean bean=null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1=json.getString("result");
                    Gson gson = new Gson();
                    bean=gson.fromJson(result1, BrandAcListBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (bean == null || bean.listData==null || bean.listData.size() ==0 ) {

                    if(PageInde==1){
                        sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                    }else{
                        sc_scrollview.setMode(PullToRefreshBase.Mode.DISABLED);
                        toastShow("没有更多数据了");
                    }
                }
                if (bean!= null && bean.listData != null) {
                   String RecordCount = "<font color=\"#fd6530\">"
                            +bean.recordCount+ "</font>";
                    munCase.setText(Html.fromHtml("共"+RecordCount+"件商品"));
                    if(PageInde==1){
                        hzAdapter.setData(bean.listData);
                    }else{
                        hzAdapter.addData(bean.listData);
                    }
                    hzAdapter.notifyDataSetChanged();
                }
                sc_scrollview.onRefreshComplete();
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
    @Override
    protected void onStop() {
		super.onStop();
		if (mKoySliding != null) {
            mKoySliding.cancelAutoPlay();
		}
	}

	@Override
    protected void onStart() {
		super.onStart();
		if (mKoySliding != null) {
            mKoySliding.restart();
		}
	}
    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),
                    BrandDetailsActivity.class);
            intent.putExtra("guid", guid);
            startActivity(intent);
        }
    };

    class Clickable extends ClickableSpan {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.xiangxi));
        }
    }
}
