package com.baolinetworktechnology.shejiquan.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.PostCommentAdapter;
import com.baolinetworktechnology.shejiquan.adapter.PostGridTesAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.CommentBean;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.ReplayUser;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyRelativeLayout;
import com.baolinetworktechnology.shejiquan.view.ScrollGridLayoutManager;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.socialize.utils.Log;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 社区--帖子xiangqin
 */
public class PostDetailsActivity extends BaseActivity implements View.OnClickListener {
    private PopupWindow mPopupWindow;
    private boolean mCbSor = false;
    private TextView jibao;
    private ImageView share_show;
    private TextView text;
    private String guid, isPost;
    private String FromID = "0";
    private String Fromguid = "";
    private String FromName = "";
    private String FromComment = "";
    private String UserGUID = "";
    private CircleImg userLogo;
    private TextView title, detaName, timeTV, contents_tv, numComment;
    private BitmapUtils mImageUtil;
    private RelativeLayout mLoadingOver;
    private PostBean postbean;
    private int mItemPosition = 0;
    private int mCommentNumber; // 评论数
//    private int mCollectionNumber;//收藏数
//    private int mFeelFabulousNumber;//点赞数
    private View item_pingluen;
    private EditText pingluen_text;
    private TextView fasong_btn;
    private MyRelativeLayout mRoot1;
    private InputMethodManager imm;
    private View my_shafa, No_layout;
    private TextView mCollect;
    private ImageView tvGood;
    private TextView numGoodTv, not_Tv, detailSCnumber;
    //    private boolean isGoodbool;
    private PullToRefreshScrollView sc_scrollview;


    public static String POSITION = "position";
    public static String SWCASESTRING = "swcasestring";
    private String ISCOLLECTION = "iscollection";//是否收藏
    private String COLLECTIONNUM = "collectionnum";
    private String ISFABULOUS = "isfabulous";
    private String FABULOUSNUM = "fabulousnum";
    private String COMMENTNUM = "commentnum";//
    private int Position, CollectionNum, FabulousNum, CommentNum;//行号，收藏数，点赞数，评论数
    private boolean IsCollection, IsFabulous;//是否收藏，点赞
    public static String NEEDOPENKEYBORD = "needopenkeybord";//判断是否从帖子列表的评论按钮进入,是则打开键盘
    private String FormWhich;//来源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        text = (TextView) findViewById(R.id.text);
        share_show = (ImageView) findViewById(R.id.share_show);
        detailSCnumber = (TextView) findViewById(R.id.detailSCnumber);
        share_show.setOnClickListener(this);
        findViewById(R.id.activity_post_details).setOnClickListener(this);
        guid = getIntent().getStringExtra(AppTag.TAG_GUID);
        isPost = getIntent().getStringExtra("IsPost");
        FormWhich = getIntent().getStringExtra(NEEDOPENKEYBORD);
        mImageUtil = new BitmapUtils(this);
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        invPull();//
        intiView();
        showPoputon();
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

    //图片 RecyclerView
    private RecyclerView mRecyclerView;
    private ScrollGridLayoutManager mLayoutManager;
    private PostGridTesAdapter mAdapter;
    //评论 RecyclerView
    private RecyclerView PlRecyclerView;
    private ScrollGridLayoutManager PlmLayoutManager;
    private PostCommentAdapter mCommentAdapter;

    private void intiView() {
        mLoadingOver = (RelativeLayout) findViewById(R.id.loading_over2);
        userLogo = (CircleImg) findViewById(R.id.userLogo);
        title = (TextView) findViewById(R.id.title);
        detaName = (TextView) findViewById(R.id.detaName);
        timeTV = (TextView) findViewById(R.id.timeTV);
        contents_tv = (TextView) findViewById(R.id.daodu_tv);
        numComment = (TextView) findViewById(R.id.numComment);
        No_layout = findViewById(R.id.No_layout);
        No_layout.setOnClickListener(this);
        findViewById(R.id.delde_back).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.postView);
        mLayoutManager = new ScrollGridLayoutManager(getApplicationContext(), 1);
        mLayoutManager.setScrollEnabled(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostGridTesAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        //评论 RecyclerView
        PlRecyclerView = (RecyclerView) findViewById(R.id.pinluenView);
        PlmLayoutManager = new ScrollGridLayoutManager(getApplicationContext(), 1);
        PlmLayoutManager.setScrollEnabled(false);
        PlRecyclerView.setLayoutManager(PlmLayoutManager);
        mCommentAdapter = new PostCommentAdapter(this);
        PlRecyclerView.setAdapter(mCommentAdapter);
        mCommentAdapter.setOnItemClickListener(new PostCommentAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Comment data) {
                if (SJQApp.user == null) {
                    startActivity(new Intent(PostDetailsActivity.this,
                            LoginActivity.class));
                } else {
                    FromID = data.getId() + "";
                    FromName = data.getUserName();
                    UserGUID = data.getUserGUID();
                    if (data.getUserGUID().equals("00000000-0000-0000-0000-000000000000")) {
                        dialog.dismiss();
                        toastShow("不能回复游客");
                        return;
                    }
                    if (SJQApp.user.getGuid().equals(data.getUserGUID())) {
                        dialog.dismiss();
                        toastShow("自己不能回复自己");
                        return;
                    }
                    pingluen_text.setHint("回复" + FromName + "：");
                    FromComment = data.getContents();
                    if (SJQApp.user == null) {
                        startActivity(new Intent(PostDetailsActivity.this,
                                LoginActivity.class));
                        return;
                    }
                    item_pingluen.setVisibility(View.VISIBLE);
                    pingluen_text.setFocusable(true);
                    pingluen_text.setFocusableInTouchMode(true);
                    pingluen_text.requestFocus();
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
                }
            }
        });


        mCollect = (TextView) findViewById(R.id.collect);
        mCollect.setOnClickListener(this);
        tvGood = (ImageView) findViewById(R.id.tvGood);
        numGoodTv = (TextView) findViewById(R.id.numGood);
        findViewById(R.id.dianz_Lay).setOnClickListener(this);

        my_shafa = findViewById(R.id.my_shafa);
        my_shafa.setVisibility(View.GONE);
        not_Tv = (TextView) findViewById(R.id.not_Tv);
        not_Tv.setVisibility(View.GONE);
        findViewById(R.id.item_comment).setOnClickListener(this);
        item_pingluen = findViewById(R.id.item_pingluen);
        pingluen_text = (EditText) findViewById(R.id.pingluen_text);
        pingluen_text.clearFocus();
        pingluen_text.addTextChangedListener(watcher);
        fasong_btn = (TextView) findViewById(R.id.fasong_btn);
        fasong_btn.setOnClickListener(this);
        imm = (InputMethodManager) pingluen_text.getContext().getSystemService(
                this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
        mRoot1 = (MyRelativeLayout) findViewById(R.id.activity_post_details);
        mRoot1.setFitsSystemWindows(true);
    }

    private void invPull() {
        sc_scrollview = (PullToRefreshScrollView) findViewById(R.id.sc_scrollview);
        //设置刷新的模式
        sc_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//both  可以上拉、可以下拉
        setPullToRefreshLable();
        sc_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                mPageIndex++;
                loadata();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_show:
                if (!mCbSor) {
                    mCbSor = true;
                    mPopupWindow.showAsDropDown(text, 0, 0);
                } else {
                    mCbSor = false;
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.activity_post_details:
                mCbSor = false;
                mPopupWindow.dismiss();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.delde_back:
                finish();
                break;
            case R.id.jubao_post:
                mCbSor = false;
                mPopupWindow.dismiss();
                if (postbean == null) {
                    toastShow("数据出错,请重新加载...");
                    return;
                }
                if (SJQApp.user == null) {
                    toastShow("举报请先登录...");
                    startActivity(new Intent(PostDetailsActivity.this,
                            LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(this, ComplaintActivity.class);
                intent.putExtra("userGUID", postbean.getGuid());
                intent.putExtra("userID", postbean.getId());
                startActivity(intent);
                break;
            case R.id.collect:
                // 收藏
                mCollect.setClickable(false);
                doCollect();
                break;
            case R.id.dianz_Lay:
                if (!IsFabulous) {
                    // 点赞
                    SavePosts();
                }
                break;
            case R.id.item_comment:
                if (SJQApp.user == null) {
                    startActivity(new Intent(PostDetailsActivity.this,
                            LoginActivity.class));
                    return;
                }
                item_pingluen.setVisibility(View.VISIBLE);
                pingluen_text.setFocusable(true);
                pingluen_text.setFocusableInTouchMode(true);
                pingluen_text.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
                break;
            case R.id.fasong_btn:
                if (fasong_btn.getText().toString().equals("发送")) {
                    if (SJQApp.user.isBindMobile) {
                        doSubmit();
                    } else {
                        toastShow("绑定手机号后才能发评论");
                    }
                } else {
                    if (item_pingluen.getVisibility() == View.VISIBLE) {
                        FromID = "0";
                        FromName = "";
                        UserGUID = "";
                        pingluen_text.setHint("");
                        FromComment = "";
                        item_pingluen.setVisibility(View.GONE);
                        imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
                    }
                }
                break;
        }
    }

    public void showPoputon() {
        View popupView = getLayoutInflater().inflate(R.layout.post_item_layout, null);
        mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        jibao = (TextView) popupView.findViewById(R.id.jubao_post);
        jibao.setOnClickListener(this);
        View popoton_view = popupView.findViewById(R.id.post_view);
        mPopupWindow.dismiss();
        popoton_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCbSor = false;
                mPopupWindow.dismiss();
            }
        });
    }

    private int mPageIndex = 1;

    private void setPullToRefreshLable() {
        sc_scrollview.getLoadingLayoutProxy().setLastUpdatedLabel("正在拼命加载中...");
        sc_scrollview.onRefreshComplete();
    }

    public void loadata() {
        HttpUtils httpUtils = new HttpUtils();
        final String url = AppUrl.COMMENT_LIST + "&fromGUID=" + postbean.getGuid()
                + "&currentPage=" + mPageIndex + "&pageSize=10" + "&markName=7";
        RequestParams params = getParams(url);
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                if (PlRecyclerView == null || mCommentAdapter == null)
                    return;
                JSONObject json;
                CommentBean comments = null;
                try {
                    json = new JSONObject(arg0.result);
                    String result1 = json.getString("result");
                    Gson gson = new Gson();
                    comments = gson.fromJson(result1, CommentBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sc_scrollview.onRefreshComplete();
                if (comments != null && comments.getListData() != null && comments.getListData().size() != 0) {
                    my_shafa.setVisibility(View.GONE);
                    mCommentNumber = comments.getRecordCount();
                    numComment.setText("全部评论 " + mCommentNumber);
                    if (mPageIndex == 1) {
                        mCommentAdapter.setList(comments.getListData());
                    } else {
                        mCommentAdapter.addList(comments.getListData());
                    }
                    mCommentAdapter.notifyDataSetChanged();
                }
                if (comments.getListData() == null || comments.getListData().size() == 0) {
                    if (mPageIndex == 1) {
                        my_shafa.setVisibility(View.VISIBLE);
                    } else {
                        not_Tv.setVisibility(View.VISIBLE);
                    }
                    sc_scrollview.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
                }

            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                if (PlRecyclerView == null || mCommentAdapter == null)
                    return;
                sc_scrollview.onRefreshComplete();
            }
        };
        httpUtils.send(HttpRequest.HttpMethod.GET, AppUrl.API + url, params, callBack);
    }

    private void Getpost() {
        mLoadingOver.setVisibility(View.VISIBLE);
        if (guid == null) {
            toastShow("获取内容出错");
            finish();
            return;
        }

        String url = AppUrl.GETPOSTSDETIAL + guid;
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
                PostBean bean = null;
                try {
                    json = new JSONObject(responseInfo.result);
                    String result1 = json.getString("result");
                    Gson gson = new Gson();
                    bean = gson.fromJson(result1, PostBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (bean == null) {
                    No_layout.setVisibility(View.VISIBLE);
                    return;
                }
                setNews(bean);
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url), callBack);
    }

//    private int numGood;
    private int NumComment;

    private void setNews(PostBean postBean) {
        postbean = postBean;
        Log.e("TAG2", postbean.toString());
        Fromguid = postBean.getGuid();
        if (SJQApp.user != null) {
            if (postbean.getUserInfo().getGuid().equals(SJQApp.user.getGuid())) {
                share_show.setVisibility(View.GONE);
            }
        }
        mImageUtil.display(userLogo, postBean.getUserInfo().getLogo());
        title.setText(postBean.getTitle());
        detaName.setText(postBean.getUserInfo().getName() + " |");
        timeTV.setText(postBean.timeData2(postBean.getCreateTime()));
        if (TextUtils.isEmpty(postBean.getContents())) {
            contents_tv.setText("分享图片");
        } else {
            contents_tv.setText(postBean.getContents());
        }
        if (postBean.getPostsItemInfoList().size() != 0) {
            mAdapter.setList(postBean.getPostsItemInfoList());
            mAdapter.notifyDataSetChanged();

        }
        NumComment = postBean.getNumComment();
        numComment.setText("全部评论　" + NumComment);

        //判断是否收藏,收藏数
        IsCollection = postBean.isFavorite();
        CollectionNum = postbean.getnumFavorite();
        //判断是否点赞,点赞数
        IsFabulous = postbean.isGood();
        FabulousNum = postbean.getNumGood();
        //评论数
        CommentNum = postBean.getNumComment();

//        detailSCnumber.setText(CollectionNum + "");
        if (IsCollection) {//已经收藏
            mCollect.setBackgroundResource(R.drawable.nav_button_wsce_on);
            detailSCnumber.setTextColor(getResources().getColor(R.color.app_color));
        } else {
            mCollect.setBackgroundResource(R.drawable.nav_button_wsce_default);
            detailSCnumber.setTextColor(getResources().getColor(R.color.text_normal6));
        }
        numGoodTv.setText(FabulousNum + "");
        if (IsFabulous) {//已经点赞
            tvGood.setClickable(false);
            tvGood.setBackgroundResource(R.drawable.dian_zan_jie_on);
            numGoodTv.setTextColor(getResources().getColor(R.color.app_color));
        } else {
            tvGood.setBackgroundResource(R.drawable.dian_zan_jie);
            numGoodTv.setTextColor(getResources().getColor(R.color.text_normal6));
        }
        isFavorite();
        loadata();
        isGood();
        mLoadingOver.setVisibility(View.GONE);
//        numGood = postBean.getNumGood();
//        numGoodTv.setText(numGood + "");
        //弹出键盘
        if (FormWhich != null && FormWhich.equals("NEED")) {
            if (SJQApp.user == null) {
                startActivity(new Intent(PostDetailsActivity.this,
                        LoginActivity.class));
                return;
            }
            item_pingluen.setVisibility(View.VISIBLE);
            pingluen_text.setFocusable(true);
            pingluen_text.setFocusableInTouchMode(true);
            pingluen_text.requestFocus();
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 显示输入法
        }
        mItemPosition = getIntent().getIntExtra(POSITION, -1);
//        mCollectionNumber = getIntent().getIntExtra("COLLECTIONNUMBER", -2);
//        mFeelFabulousNumber = getIntent().getIntExtra("FABULOUSNUMBER", -2);
    }

    private void doSubmit() {
        dialog.show();
        String Contents = pingluen_text.getText().toString();
        if (Contents.trim().length() < 1) {
            dialog.dismiss();
            toastShow("输入内容不能为空");
            return;
        }
        String url = AppUrl.COMMENT_SEND;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type", "application/json");
        try {
            JSONObject param = new JSONObject();
            param.put("contents", Contents);
            param.put("fromGUID", Fromguid);
            param.put("markName", "7");
            param.put("fromID", FromID);
            param.put("userGUID", SJQApp.user.guid);
            param.put("anonymous", false);
            if (SJQApp.userData != null) {
                param.put("userName", SJQApp.userData.name);
                param.put("userLogo", SJQApp.userData.logo);
            } else if (SJQApp.ownerData != null) {
                param.put("userName", SJQApp.ownerData.getName());
                param.put("userLogo", SJQApp.ownerData.getLogo());
            }
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        } catch (JSONException e) {
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
                dialog.dismiss();
                if (mCommentAdapter == null)
                    return;
                Gson gson = new Gson();
                SwresultBen bean = gson.fromJson(arg0.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        mCommentAdapter.notifyDataSetChanged();
                        toastShow(bean.result.operatMessage);
                        mCommentNumber = mCommentNumber + 1;
                        numComment.setText("全部评论　" + mCommentNumber);
//                        Intent intent = new Intent();
//                        intent.setAction("CommentSuCCESS");//社区 评论成功
//                        intent.putExtra("POISTION", mItemPosition);
//                        intent.putExtra("NUMBER", mCommentNumber - 1);//
//                        sendBroadcast(intent);
                        addComment();
                    } else {
                        toastShow(bean.result.operatMessage);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String arg1) {
                toastShow("网络请求发生错误");
                dialog.dismiss();
//                pingluen_text.setText("");
//                item_pingluen.setVisibility(View.GONE);
//                imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);
            }
        };

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }

    private void addComment() {
        if (my_shafa.getVisibility() == View.VISIBLE) {
            my_shafa.setVisibility(View.GONE);
        }
        mCommentNumber++;

        if (pingluen_text != null) {

            Comment comment = new Comment();
            if (SJQApp.userData != null) {
                comment.userName = SJQApp.userData.name;
                comment.userLogo = SJQApp.userData.logo;
            } else if (SJQApp.ownerData != null) {
                comment.userName = SJQApp.ownerData.getName();
                comment.userLogo = SJQApp.ownerData.getLogo();
            } else {
                comment.userName = SJQApp.user.nickName;
                comment.userLogo = SJQApp.user.logo;
            }
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            comment.setUserGUID(SJQApp.user.getGuid());
            if (postbean.getUserInfo().getGuid().equals(SJQApp.user.getGuid())) {
                comment.setAuthor(true);
            }
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String str = formatter.format(curDate);
            comment.setCreateTime(str);
            comment.contents = pingluen_text.getText().toString();
            if (!FromID.equals("0")) {
                ReplayUser replayUser = new ReplayUser();
                replayUser.setReplayUserName(FromName);
                replayUser.setReplyContents(FromComment);
                comment.replayUser = replayUser;
            }
            NumComment = NumComment + 1;
            numComment.setText("全部评论　" + NumComment);
            mCommentAdapter.addList(comment);
            mCommentAdapter.notifyDataSetChanged();
            pingluen_text.setText("");
            pingluen_text.setHint("说点什么吧...");
            FromID = "0";
            UserGUID = "";
            FromName = "";
            FromComment = "";
            item_pingluen.setVisibility(View.GONE);
            imm.hideSoftInputFromWindow(pingluen_text.getWindowToken(), 0);// 隐藏输入法
        }
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            String str = pingluen_text.getText().toString();
            if (str.equals("")) {
                fasong_btn.setText("取消");
            } else {
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

    public String timeData(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        String Time = "";
        try {
            date = format.parse(time);
            Time = sdf.format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return Time;
    }

    private void doCollect() {
        if (SJQApp.user != null) {
            dialog.show();
            String url = AppUrl.OPERATEPOSTADD;
            RequestParams params = new RequestParams();
            params.setHeader("Content-Type", "application/json");
            try {
                JSONObject param = new JSONObject();
                param.put("classType", "0");
                param.put("userGUID", SJQApp.user.guid);
                param.put("fromGUID", guid);
                if (!IsCollection) {//判断是否已经收藏
                    param.put("operate", "1");
                } else {
                    param.put("operate", "0");
                }
                param.put("favoriteMark", "0");
                StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
                params.setBodyEntity(sEntity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            if (dialog == null)
                                return;
                            dialog.dismiss();
                            toastShow("当前网络连接失败");

                            mCollect.setClickable(true);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> n) {
                            if (dialog == null)
                                return;
                            dialog.dismiss();
                            Gson gson = new Gson();
                            SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                            if (bean != null) {
                                if (bean.result.operatResult) {
                                    if (IsCollection == false) {
                                        toastShow("收藏成功");
                                        IsCollection = true;
                                        CollectionNum = CollectionNum + 1;
                                        mCollect.setBackgroundResource(R.drawable.nav_button_wsce_on);
                                        detailSCnumber.setTextColor(getResources().getColor(R.color.app_color));
//                                        detailSCnumber.setText(CollectionNum+"");
//                                        detailDZnumber.setText(DZNumber+1 +"");
//                                        if(!TextUtils.isEmpty(isPost)){
//                                        Intent intent = new Intent();
////                                            intent.setAction("Postshow");
//                                        intent.putExtra("POISTION", mItemPosition);
//                                        intent.putExtra("NUMBER", mCollectionNumber+1);
//                                        intent.setAction("CollectionOn");
//                                        sendBroadcast(intent);
//                                        }
                                    } else {
                                        toastShow("取消收藏成功");
                                        IsCollection = false;
                                        if (CollectionNum >0){
                                            CollectionNum = CollectionNum-1;
                                        }
                                        mCollect.setBackgroundResource(R.drawable.nav_button_wsce_default);
                                        detailSCnumber.setTextColor(getResources().getColor(R.color.text_normal6));
//                                        detailSCnumber.setText(CollectionNum+"");
//                                        if(!TextUtils.isEmpty(isPost)){
//                                        Intent intent = new Intent();
//                                        if (DZNumber >0){
//                                            detailDZnumber.setText(DZNumber-1 +"");
//                                        }
//                                        intent.putExtra("POISTION", mItemPosition);
//                                        intent.putExtra("NUMBER", mCollectionNumber-1);
////                                            intent.setAction("Postshow");
//                                        intent.setAction("CollectionOff");
//                                        sendBroadcast(intent);
//                                        }
                                    }
                                }
                                mCollect.setClickable(true);
                            }

                        }
                    });
        } else {
            mCollect.setClickable(false);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }

    }

    // 是否收藏
    private void isFavorite() {
        if (SJQApp.user == null) {
            return;
        }
        String url = AppUrl.ISPOSTSFAVAOR;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type", "application/json");
        try {
            JSONObject param = new JSONObject();
            param.put("userGUID", SJQApp.user.guid);
            param.put("fromGUID", guid);
            param.put("classType", "0");
            param.put("favoriteMark", "0");
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                SwresultBen bean = gson.fromJson(responseInfo.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        mCollect.setClickable(true);
                        //已经收藏
                        IsCollection = true;
                        mCollect.setBackgroundResource(R.drawable.nav_button_wsce_on);
                        detailSCnumber.setTextColor(getResources().getColor(R.color.app_color));

                    }
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                toastShow("网络请求失败");
                mCollect.setClickable(false);
            }
        };
        getHttpUtils(0).send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }

    private void SavePosts() {
        if (postbean == null) {
            toastShow("数据还在获取中");
            return;
        }
        if (SJQApp.user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        dialog.show();
        String url = AppUrl.SAVEPOSTSGOOD;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type", "application/json");
        try {
            JSONObject param = new JSONObject();
            param.put("classType", "0");
            param.put("sendUserGuid", SJQApp.user.guid);
            param.put("userGUID", postbean.getUserInfo().getGuid());
            if (SJQApp.userData != null) {
                param.put("sendUserLogo", SJQApp.userData.getLogo());
                param.put("sendUserName", SJQApp.userData.getName());
            } else if (SJQApp.ownerData != null) {
                param.put("sendUserLogo", SJQApp.ownerData.getLogo());
                param.put("sendUserName", SJQApp.ownerData.getName());
            } else {
                param.put("sendUserLogo", SJQApp.user.logo);
                param.put("sendUserName", SJQApp.user.logo);
            }
            param.put("markName", "1");
            param.put("sendUserIdentity", SJQApp.user.markName);
            param.put("postsId", postbean.getId());
            param.put("postsGuid", postbean.getGuid());
            param.put("postsTitle", postbean.getTitle());
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
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
                        SwresultBen bean = gson.fromJson(n.result, SwresultBen.class);
                        if (bean != null) {
                            if (bean.result.operatResult) {
//                                numGood++;
                                numGoodTv.setTextColor(getResources().getColor(R.color.app_color1));
                                tvGood.setBackgroundResource(R.drawable.dian_zan_tuer);
                                IsFabulous = true;
                                FabulousNum = FabulousNum + 1;
                                numGoodTv.setText(FabulousNum + "");
                            } else {
                                toastShow(bean.result.operatMessage);
                            }
                        }

                    }
                });
    }

    private void isGood() {
        if (SJQApp.user == null) {
            return;
        }
        String url = AppUrl.ISPOSTSGOOD + "sendUserGuid=" + SJQApp.user.guid +
                "&postsId=" + postbean.getId();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                SwresultBen bean = gson.fromJson(responseInfo.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        numGoodTv.setTextColor(getResources().getColor(R.color.app_color1));
                        tvGood.setBackgroundResource(R.drawable.dian_zan_tuer);
                        IsFabulous = true;
                    }
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                toastShow("网络请求失败");
                mCollect.setClickable(false);
            }
        };
        getHttpUtils(10).send(HttpRequest.HttpMethod.GET, AppUrl.API + url, callBack);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        mRoot1.setFitsSystemWindows(false);
        mRecyclerView.setAdapter(null);
        PlRecyclerView.setAdapter(null);
        Intent intent = new Intent();
        SwCase bean = new SwCase();
        bean.setFavorite(IsCollection);
        bean.setNumFavorite(CollectionNum);
        bean.setGood(IsFabulous);
        bean.setNumGood(FabulousNum);
        bean.setNumComment((CommentNum+mCommentNumber)/2);
        intent.putExtra(POSITION, mItemPosition);//行号
        intent.putExtra(SWCASESTRING, bean.toString());//
        Log.e("TAGonDestroy",mItemPosition+"bean:"+bean.toString());
        intent.setAction("DETAILSUPDATA");
        sendBroadcast(intent);
    }
}
