package com.baolinetworktechnology.shejiquan.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.baolinetwkchnology.shejiquan.xiaoxi.SideBar;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.NewLabelBean;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.domain.SortModelList;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.tongxunlu.LabelRecyclerView;
import com.baolinetworktechnology.shejiquan.tongxunlu.MemberAdapter;
import com.baolinetworktechnology.shejiquan.tongxunlu.StickyRecyclerHeadersDecoration;
import com.baolinetworktechnology.shejiquan.utils.CharacterParser;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.PinyinComparator;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 标签成员操作页
 */
public class MembersActivity extends BaseActivity {
    private BitmapUtils mImageUtil;
    private List<SortModel> contactList;
    private LabelRecyclerView mRecyclerView;
    private MemberAdapter mAdapter;
    private SideBar sideBar;
    private TextView dialogText;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private MyBroadcastReciver mybroad;
    String lableName ="";
    String lableguid ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("AddMen");
        intentFilter.addAction("LableName");
        mybroad=new MyBroadcastReciver();
        MembersActivity.this.registerReceiver(mybroad, intentFilter);
        initView();
        initData();
        showData();
    }

    private void initView() {
        //加载图片类
        mImageUtil = new BitmapUtils(this);
        mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

        //控件加载
        sideBar = (SideBar) findViewById(R.id.contact_sidebar);
        dialogText = (TextView) findViewById(R.id.contact_dialog);
        mRecyclerView = (LabelRecyclerView) findViewById(R.id.contact_lable);

        findViewById(R.id.tv_title2).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }
    private void initData() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialogText);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener()
        {

            @Override
            public void onTouchingLetterChanged(String s)
            {
                if (mAdapter != null) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mRecyclerView.getLayoutManager().scrollToPosition(position);
                }

            }
        });
        if(contactList == null){
            contactList =new ArrayList<>();
        }else{
            contactList.clear();
        }
        mAdapter = new MemberAdapter(this, contactList);
        int orientation = LinearLayoutManager.VERTICAL;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), orientation, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onChanged()
            {
                headersDecor.invalidateHeaders();
            }
        });
    }
    private void showData() {

        if(SJQApp.user != null){
            if(contactList == null){
                contactList =new ArrayList<>();
            }else{
                contactList.clear();
            }
            String bean = getIntent().getStringExtra("newlable");
            if(!TextUtils.isEmpty(bean)){
                NewLabelBean sortModelList= CommonUtils.getDomain(bean, NewLabelBean.class);
                if(sortModelList!=null){
                    contactList=sortModelList.getListFriend();
                    lableName =sortModelList.getLabelName();
                    lableguid =sortModelList.getLabelGUID();
                    for(SortModel sortModel:contactList){
                        sortModel.setSortLetters(filledData(sortModel.getSortLetters()));
                    }
                }
            }

            if(contactList.size() ==0 ){
                Collections.sort(contactList, pinyinComparator);
                SortModel sortModel = new SortModel();
                sortModel.setSortLetters("%");
                sortModel.setName(lableName);
                contactList.add(0,sortModel);
                mAdapter.setList(contactList);
                mAdapter.notifyDataSetChanged();
                setHeader(mRecyclerView);
            }else{
                Collections.sort(contactList, pinyinComparator);
                SortModel sortModel = new SortModel();
                sortModel.setSortLetters("%");
                sortModel.setName(lableName);
                contactList.add(0,sortModel);
                mAdapter.setList(contactList);
                mAdapter.notifyDataSetChanged();
                setHeader(mRecyclerView);
            }
        }
    }
    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private String filledData(String date) {
        if(TextUtils.isEmpty(date)){
            return "#";
        }else{
            SortModel sortModel = new SortModel();
            sortModel.setName(date);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                return sortString.toUpperCase();
            } else {
                return "#";
            }
        }
    }
    private class MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //添加成员
            if (action.equals("AddMen")) {
                lableName =mAdapter.Label();
                Intent addintent = new Intent(getActivity(), AddMemActivity.class);
                SortModelList bean= new SortModelList();
                List<SortModel> addlist=new ArrayList<>() ;
                addlist.addAll(contactList);
                addlist.remove(0);
                bean.result = addlist;
                addintent.putExtra("newlable", bean.toString());
                startActivityForResult(addintent,0);
            }
            if (action.equals("LableName")) {
                lableName =mAdapter.Label();
                Intent addintent = new Intent(getActivity(), LabelNameActivity.class);
                addintent.putExtra("LableName",lableName);
                startActivityForResult(addintent,1);
            }
        }
    }
    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.add_lable_header, view, false);
        mAdapter.setHeaderView(header);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data == null)
                return;
            String bean= data.getStringExtra("AddmLists");
            SortModelList sortModelList= CommonUtils.getDomain(bean, SortModelList.class);
            contactList =sortModelList.result;
            if(contactList.size() ==0 ){
                Collections.sort(contactList, pinyinComparator);
                SortModel sortModel = new SortModel();
                sortModel.setSortLetters("%");
                contactList.add(0,sortModel);
                sortModel.setName(lableName);
                mAdapter.setList(contactList);
                mAdapter.notifyDataSetChanged();
                setHeader(mRecyclerView);
            }else{
                Collections.sort(contactList, pinyinComparator);
                SortModel sortModel = new SortModel();
                sortModel.setSortLetters("%");
                sortModel.setName(lableName);
                contactList.add(0,sortModel);
                mAdapter.setList(contactList);
                mAdapter.notifyDataSetChanged();
                setHeader(mRecyclerView);
            }

        }else if(requestCode == 1){
            if (data == null)
                return;
           }
         lableName=data.getStringExtra("LableName");
         mAdapter.lableName(lableName);
        }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                doBack();
                break;
            case R.id.tv_title2:
                addPost();
                break;
        }
    }
    private void addPost() {
        dialog.show();
        String labelName=mAdapter.Label();
        if(TextUtils.isEmpty(labelName)){
            dialog.dismiss();
            toastShow("名字不能为空");
            return;
        }
        if(contactList.size()==1){
            dialog.dismiss();
            toastShow("请选择成员");
            return;
        }
        String url = AppUrl.ADDEASELABLE;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("labelGUID",lableguid);
            param.put("userGUID", SJQApp.user.guid);
            param.put("labelName", labelName);
            JSONArray path =new JSONArray();
            for (int c = 0; c < contactList.size(); c++){
                if(contactList.get(c).getFriendGUID() != null){
                    JSONObject jo =new JSONObject();
                    jo.put("labelGUID",lableguid);
                    jo.put("friendGUID",contactList.get(c).getFriendGUID());
                    path.put(jo);
                }
            }
            param.put("labelFriendList", path);
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
                if (dialog != null) {
                    dialog.setCancelable(false);
                    dialog.show("请稍后");
                }
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
                        Intent intent = new Intent();
                        intent.setAction("showlable");
                        sendBroadcast(intent);
                        finish();
                    }else{
                        toastShow("服务器开小差...");
                    }
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null)
                    dialog.dismiss();
                toastShow("服务器开小差...");
            }

        };
        getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }

    private boolean isBack = true;
    // 返回
    private void doBack() {
        if (isBack) {
            new MyAlertDialog(this).setTitle("提示").setContent("保存本次编辑？")
                    .setBtnOk("继续编辑").setBtnCancel("放弃编辑")
                    .setBtnOnListener(new MyAlertDialog.DialogOnListener() {

                        @Override
                        public void onClickListener(boolean isOk) {
                            if (isOk) {

                            } else {
                                finish();
                            }

                        }
                    }).show();

        } else {
            finish();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRecyclerView !=null){
            unregisterReceiver(mybroad);
            mRecyclerView.setAdapter(null);
        }
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
}
