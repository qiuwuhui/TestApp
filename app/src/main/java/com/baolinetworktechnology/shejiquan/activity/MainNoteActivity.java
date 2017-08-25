package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.NewLabelBean;
import com.baolinetworktechnology.shejiquan.domain.NewLabelList;
import com.baolinetworktechnology.shejiquan.domain.NoteLabelList;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyRelativeLayout;
import com.google.gson.Gson;
import com.guojisheng.koyview.UISwitchButton;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainNoteActivity extends BaseActivity {
    // 外围的LinearLayout容器
    private LinearLayout llContentView;
    // “-”按钮控件List
    private LinkedList<ImageView> listIBTNDel;
    // “-”按钮控件List
    private LinkedList<EditText> listIBEdit;
    // “+”按钮ID索引
    private int btnIDIndex = 1000;
    private int iETContentHeight = 0;	// EditText控件高度
    private float fDimRatio = 1.0f; // 尺寸比例（实际尺寸/xml文件里尺寸）


    private SortModel sortModel=null;
    private TextView beizhu_name,biaoqian;
    private EditText etitText;
    private TextView etiphone;
    private UISwitchButton switchbtn;
    private ImageView add_phone;
    private View lay_phone,item_about;
    private boolean ISswitch;
    private CountDownTimer time;
    private MyRelativeLayout mRoot1;
    private NoteLabelList labelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);
        String sortJson=getIntent().getStringExtra(AppTag.TAG_JSON);
        if(!TextUtils.isEmpty(sortJson)){
            sortModel = CommonUtils.getDomain(sortJson, SortModel.class);
        }else{
            toastShow("数据出错");
            finish();
            return;
        }
        initView();
    }
    private void initView() {
        listIBTNDel =new LinkedList<ImageView>();
        listIBEdit  =new LinkedList<EditText>();
        listIBTNDel.add(null);
        listIBEdit.add(null);
        findViewById(R.id.back).setOnClickListener(this);
        etitText = (EditText) findViewById(R.id.etitText);
        beizhu_name = (TextView) findViewById(R.id.beizhu_name);
        biaoqian = (TextView) findViewById(R.id.biaoqian);
        item_about = findViewById(R.id.item_about);
        item_about.setOnClickListener(this);
        item_about.setEnabled(false);
        switchbtn = (UISwitchButton) findViewById(R.id.switch_beizhu);
        lay_phone = findViewById(R.id.lay_phone);
        lay_phone.setOnClickListener(this);
        add_phone = (ImageView) findViewById(R.id.add_phone);
        add_phone.setOnClickListener(this);
        etiphone = (TextView) findViewById(R.id.etiphone);
        etiphone.setOnClickListener(this);
        llContentView = (LinearLayout) this.findViewById(R.id.content_view);
        findViewById(R.id.tv_title2).setOnClickListener(this);
        findViewById(R.id.shezhi_btn).setOnClickListener(this);
        mRoot1 = (MyRelativeLayout) findViewById(R.id.activity_main_note);
        mRoot1.setFitsSystemWindows(true);
        switchbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    ISswitch =true;
                } else {
                    ISswitch =false;
                }
            }
        });
        switchbtn.setChecked(sortModel.isConcern());
    }
    private void showModle() {
        if(sortModel ==null){
            toastShow("数据出错");
            finish();
            return;
        }
        getLabel();
        beizhu_name.setText("设置手机通讯录名字“"+sortModel.getName()+"”为备注名");
        if(!TextUtils.isEmpty(sortModel.getRemarkName())){
            etitText.setText(sortModel.getRemarkName());
        }
        String[] mobilelist = sortModel.getMobile();
        ISswitch =sortModel.isConcern();
        if(mobilelist != null){
            for (int c = 0; c < mobilelist.length; c++){
                if(listIBTNDel.size()==5){
                    lay_phone.setVisibility(View.GONE);
                }
                // 获取尺寸变化比例
                iETContentHeight = etiphone.getHeight();
                fDimRatio = iETContentHeight / 80;
                addMobile();
            }
            for (int c = 0; c < mobilelist.length; c++){
                if(listIBEdit.get(c) !=null){
                    listIBEdit.get(c).setText(mobilelist[c]);
                }
            }

        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_title2:
              addPost();
                break;
            case R.id.item_about:
                Intent intent = new Intent(getActivity(), MainlabelActivity.class);
                intent.putExtra("friendGUID",sortModel.getFriendGUID());
                intent.putExtra("notelabei",labelList.toString());
                startActivityForResult(intent, 0);
                break;
            case R.id.shezhi_btn:
                etitText.setText(sortModel.getName());
                break;
            case R.id.add_phone:
                if(listIBTNDel.size()==5){
                    lay_phone.setVisibility(View.GONE);
                }
                // 获取尺寸变化比例
                iETContentHeight = etiphone.getHeight();
                fDimRatio = iETContentHeight / 80;
                addContent(v);
                break;
            case R.id.etiphone:
                if(listIBTNDel.size()==5){
                    lay_phone.setVisibility(View.GONE);
                }
                // 获取尺寸变化比例
                iETContentHeight = etiphone.getHeight();
                fDimRatio = iETContentHeight / 80;
                addContent(v);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void getLabel(){
        String url= AppUrl.GETFRIENDLABEL+ SJQApp.user.getGuid()+"&friendGUID="+sortModel.getFriendGUID();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                toastShow(message);
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                NoteLabelList bean=gson.fromJson(responseInfo.result, NoteLabelList.class);
                if(bean!=null){
                    labelList =bean;
                    showData(bean);
                }

            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
    private void showData(NoteLabelList bean){
        String xianqin="";
        if(bean.result!=null){
            for (int i = 0; i < bean.result.size(); i++){
                if(bean.result.size()-1==i){
                    xianqin +=bean.result.get(i).getLabelName();
                }else{
                    xianqin +=bean.result.get(i).getLabelName()+",  ";
                }
            }
        }
        biaoqian.setText(xianqin);
        item_about.setEnabled(true);
    }


    private void addPost() {
        dialog.show();
       List<String> mobileList= new ArrayList<>();
        for (EditText edit:listIBEdit){
            if(edit!=null){
                if(!edit.getText().toString().trim().equals("")){
                    mobileList.add(edit.getText().toString());
                }
            }
        }
        String url = AppUrl.EDITEASEFRIEND;
        RequestParams params = getParams(url);
        params.addBodyParameter("userGuid", SJQApp.user.getGuid());
        params.addBodyParameter("fromGuid",sortModel.getFriendGUID());
        params.addBodyParameter("remarkName",etitText.getText().toString());
        if(ISswitch){
            params.addBodyParameter("isConcern","true");
        }else{
            params.addBodyParameter("isConcern","false");
        }
        if (mobileList.size() == 0 ){
            params.addBodyParameter("mobile[]","");
        }else {
            for (int c = 0; c < mobileList.size(); c++){
                params.addBodyParameter("mobile[]",mobileList.get(c));
            }
        }
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(ResponseInfo<String> n) {
                Intent intent = new Intent();
                intent.setAction("qiehuan");
                sendBroadcast(intent);
                time = new CountDownTimer(800, 800) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        finish();

                    }
                };
                time.start();
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                toastShow(msg);
            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }

    private int iIndex = 0;
    private void addContent(View v) {
        if (v == null) {
            return;
        }
        if (iIndex >= 0) {
            // 开始添加控件

            // 1.创建外围LinearLayout控件
            LinearLayout layout = new LinearLayout(MainNoteActivity.this);
            LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置margin
            layout.setLayoutParams(lLayoutlayoutParams);
            layout.setBackgroundResource(R.color.white);	// #FFFFFFFF
            layout.setOrientation(LinearLayout.VERTICAL);

            // 3.创建EditText和“-”按钮外围控件RelativeLayout
            LinearLayout rlBtn = new LinearLayout(MainNoteActivity.this);
            LinearLayout.LayoutParams rlParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            rlBtn.setLayoutParams(rlParam);
            rlBtn.setOrientation(LinearLayout.HORIZONTAL);
            //创建“-”按钮
            ImageView btnDelete = new ImageView(MainNoteActivity.this);
            btnDelete.setImageResource(R.drawable.delete_phone_img);
            LinearLayout.LayoutParams btnDeleteAddParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            btnDeleteAddParam.setMargins(24, 0, 0, 0);
            btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteContent(v);
                }
            });
            // 将“-”按钮放到RelativeLayout里
            rlBtn.addView(btnDelete, btnDeleteAddParam);
            listIBTNDel.add(iIndex, btnDelete);

            // 创建内部EditText控件
            EditText addContent = new EditText(MainNoteActivity.this);
            LinearLayout.LayoutParams addetParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, iETContentHeight);
            addContent.setLayoutParams(addetParam);
            addContent.setGravity(Gravity.CENTER_VERTICAL);
            addContent.setInputType(InputType.TYPE_CLASS_PHONE);
            addContent.setTextSize(14);
            addContent.setBackgroundResource(R.color.white);
            rlBtn.addView(addContent);

            // 将RelativeLayout放到LinearLayout里
            layout.addView(rlBtn);
            listIBEdit.add(iIndex,addContent);
            //创建内部view控件
            View etContent = new View(MainNoteActivity.this);
            LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            etParam.setMargins(30, 0, 0, 0);
            etContent.setLayoutParams(etParam);
            // 设置属性
            etContent.setBackgroundResource(R.color.line);	// #FFFFFFFF
            etContent.getLayoutParams().height = 1;
//             将EditText放到LinearLayout里
            layout.addView(etContent);
            // 7.将layout同它内部的所有控件加到最外围的llContentView容器里
            llContentView.addView(layout, iIndex);
            // 控件实际添加位置为当前触发位置点下一位
            btnIDIndex++;
            iIndex ++;
        }
    }
    private void addMobile() {

        if (iIndex >= 0) {
            // 开始添加控件

            // 1.创建外围LinearLayout控件
            LinearLayout layout = new LinearLayout(MainNoteActivity.this);
            LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置margin
            layout.setLayoutParams(lLayoutlayoutParams);
            layout.setBackgroundResource(R.color.white);	// #FFFFFFFF
            layout.setOrientation(LinearLayout.VERTICAL);

            // 3.创建EditText和“-”按钮外围控件RelativeLayout
            LinearLayout rlBtn = new LinearLayout(MainNoteActivity.this);
            LinearLayout.LayoutParams rlParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            rlBtn.setLayoutParams(rlParam);
            rlBtn.setOrientation(LinearLayout.HORIZONTAL);
            //创建“-”按钮
            ImageView btnDelete = new ImageView(MainNoteActivity.this);
            btnDelete.setImageResource(R.drawable.delete_phone_img);
            LinearLayout.LayoutParams btnDeleteAddParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            btnDeleteAddParam.setMargins(24, 0, 0, 0);
            btnDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteContent(v);
                }
            });
            // 将“-”按钮放到RelativeLayout里
            rlBtn.addView(btnDelete, btnDeleteAddParam);
            listIBTNDel.add(iIndex, btnDelete);

            // 创建内部EditText控件
            EditText addContent = new EditText(MainNoteActivity.this);
            LinearLayout.LayoutParams addetParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, iETContentHeight);
            addContent.setLayoutParams(addetParam);
            addContent.setGravity(Gravity.CENTER_VERTICAL);
            addContent.setInputType(InputType.TYPE_CLASS_PHONE);
            addContent.setTextSize(14);
            addContent.setBackgroundResource(R.color.white);
            rlBtn.addView(addContent);

            // 将RelativeLayout放到LinearLayout里
            layout.addView(rlBtn);
            listIBEdit.add(iIndex,addContent);
            //创建内部view控件
            View etContent = new View(MainNoteActivity.this);
            LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            etParam.setMargins(30, 0, 0, 0);
            etContent.setLayoutParams(etParam);
            // 设置属性
            etContent.setBackgroundResource(R.color.line);	// #FFFFFFFF
            etContent.getLayoutParams().height = 1;
//             将EditText放到LinearLayout里
            layout.addView(etContent);
            // 7.将layout同它内部的所有控件加到最外围的llContentView容器里
            llContentView.addView(layout, iIndex);
            // 控件实际添加位置为当前触发位置点下一位
            btnIDIndex++;
            iIndex ++;
        }
    }
    /**
     * 删除一组控件
     * @param v	事件触发控件，其实就是触发删除事件对应的“-”按钮
     */
    private void deleteContent(View v) {
        if (v == null) {
            return;
        }
        if(listIBTNDel.size() == 6){
            lay_phone.setVisibility(View.VISIBLE);
        }
        // 判断第几个“-”按钮触发了事件
        int deleteiIndex = -1;
        for (int i = 0; i < listIBTNDel.size(); i++) {
            if (listIBTNDel.get(i) == v) {
                deleteiIndex = i;
                break;
            }
        }
        if (deleteiIndex >= 0) {
            listIBTNDel.remove(deleteiIndex);
            listIBEdit.remove(deleteiIndex);
            // 从外围llContentView容器里删除第iIndex控件
            llContentView.removeViewAt(deleteiIndex);
        }
        iIndex --;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == 1){
                item_about.setEnabled(false);
                getLabel();
            }

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(llContentView != null){
            llContentView.removeAllViews();
        }
        mRoot1.setFitsSystemWindows(false);
    }
    private boolean IShasFocus=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            if(IShasFocus){
                showModle();
                IShasFocus=false;
            }
        }
    }
}
