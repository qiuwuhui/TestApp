package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.LabelList;
import com.baolinetworktechnology.shejiquan.domain.NewLabelBean;
import com.baolinetworktechnology.shejiquan.domain.NewLabelList;
import com.baolinetworktechnology.shejiquan.domain.NoteLabelList;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainlabelActivity extends BaseActivity  {
    private FlowLayout flowLayout;//上面的flowLayout
    private TagFlowLayout allFlowLayout;//所有标签的TagFlowLayout
    private List<LabelList> label_list = new ArrayList<>();//上面的标签列表
    private List<LabelList> all_label_List = new ArrayList<>();//所有标签列表
    final List<TextView> labels = new ArrayList<>();//存放标签
    final List<Boolean> labelStates = new ArrayList<>();//存放标签状态
    final Set<Integer> set = new HashSet<>();//存放选中的
    private TagAdapter<LabelList> tagAdapter;//标签适配器
    private LinearLayout.LayoutParams params;
    private EditText editText;
    private TextView tv_title2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlabel);
        initView();
        initData();
        initEdittext();
        initAllLeblLayout();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_title2:
                AddMulti();
                break;
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

    /**
     * 初始化View
     */
    private void initView() {
        flowLayout = (FlowLayout) findViewById(R.id.id_flowlayout);
        allFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout_two);
        findViewById(R.id.back).setOnClickListener(this);
        tv_title2 = (TextView) findViewById(R.id.tv_title2);
        tv_title2.setEnabled(false);
        tv_title2.setOnClickListener(this);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 10, 0, 10);
        flowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextContent = editText.getText().toString();
                if (TextUtils.isEmpty(editTextContent)) {
                    tagNormal();
                } else {
                    if(editText.getText().toString().length()>19){
                        toastShow("标签名字不能超过18个字");
                        return;
                    }
                    if(addLabel(editText)){
                        LabelList labelList = new LabelList();
                        labelList.setLabelName(labels.get(labels.size()-1).getText().toString());
                        labelList.setLabelGUID("");
                        label_list.add(labelList);
                        IsArrayList();
                    }
                }
            }
        });
    }
    /**
     * 初始化数据
     */
    private NoteLabelList noteLabelList=null;
    private String friendGUID="";
    private void initData(){
        friendGUID =getIntent().getStringExtra("friendGUID");
        String sortJson=getIntent().getStringExtra("notelabei");
        if(!TextUtils.isEmpty(sortJson)){
             noteLabelList = CommonUtils.getDomain(sortJson, NoteLabelList.class);
        }
        //初始化上面标签
        if(noteLabelList!=null && noteLabelList.result!=null){
            label_list.addAll(noteLabelList.result);
        }

        //初始化下面标签列表
        all_label_List.addAll(label_list);
        String lablestr= CacheUtils.getStringData(getActivity(), "lablelist", null);
        if (lablestr != null) {
            Gson gson = new Gson();
            NewLabelList bean = gson.fromJson(lablestr, NewLabelList.class);
            if (bean != null && bean.result!=null) {
                List<NewLabelBean> list =bean.result;
                for (int j = 0; j < all_label_List.size() ; j++){
                    for (int c = 0; c < list.size() ; c++){
                        if(all_label_List.get(j).getLabelName().equals(list.get(c).getLabelName())){
                            list.remove(c);
                        }
                    }
                 }
                for (int i = 0; i < list.size() ; i++) {
                    LabelList labelList =new LabelList();
                    labelList.setLabelName(list.get(i).getLabelName());
                    labelList.setLabelGUID(list.get(i).getLabelGUID());
                    all_label_List.add(labelList);
                }
            }
        }
        for (int i = 0; i < label_list.size() ; i++) {
            editText = new EditText(getApplicationContext());//new 一个EditText
            editText.setText(label_list.get(i).getLabelName());

            addLabel(editText);//添加标签
        }
        IsArrayList();
    }

    /**
     * 初始化默认的添加标签
     */
    private void initEdittext(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 10, 0, 10);
        editText = new EditText(MainlabelActivity.this);
        editText.setHint("添加标签");
        editText.setTextSize(12);
        //设置shape
        editText.setBackgroundResource(R.drawable.label_eti_normal);
        editText.setHintTextColor(Color.parseColor("#b4b4b4"));
        editText.setTextColor(Color.parseColor("#000000"));
        editText.setLayoutParams(params);
        //添加到layout中
        flowLayout.addView(editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagNormal();
                String biantv=editText.getText().toString();
                if(!TextUtils.isEmpty(biantv)){
                    editText.setBackgroundResource(R.drawable.label_add);
                }else{
                    editText.setBackgroundResource(R.drawable.label_eti_normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 初始化所有标签列表
     */
    private void initAllLeblLayout() {
        //初始化适配器
        tagAdapter = new TagAdapter<LabelList>(all_label_List) {
            @Override
            public View getView(FlowLayout parent, int position, LabelList s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.flag_adapter,
                        allFlowLayout, false);
                tv.setText(s.getLabelName());
                return tv;
            }
        };

        allFlowLayout.setAdapter(tagAdapter);

        //根据上面标签来判断下面的标签是否含有上面的标签
        for (int i = 0; i < label_list.size(); i++) {
            for (int j = 0; j < all_label_List.size(); j++) {
                if (label_list.get(i).getLabelName().equals(
                        all_label_List.get(j).getLabelName())) {
                    tagAdapter.setSelectedList(i);//设为选中
                }
            }
        }
        tagAdapter.notifyDataChanged();


        //给下面的标签添加监听
        allFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (labels.size() == 0) {
                    editText.setText(all_label_List.get(position).getLabelName());
                    if(addLabel(editText)){
                        LabelList labelList = new LabelList();
                        labelList.setLabelName(all_label_List.get(position).getLabelName());
                        labelList.setLabelGUID(all_label_List.get(position).getLabelGUID());
                        label_list.add(labelList);
                        IsArrayList();
                    }
                    return false;
                }
                List<String> list = new ArrayList<>();
                for (int i = 0; i < labels.size(); i++) {
                    list.add(labels.get(i).getText().toString());
                }
                //如果上面包含点击的标签就删除
                if (list.contains(all_label_List.get(position).getLabelName())) {
                    for (int i = 0; i < list.size(); i++) {
                        if (all_label_List.get(position).getLabelName().equals(list.get(i))) {
                            flowLayout.removeView(labels.get(i));
                            label_list.remove(i);
                            labels.remove(i);
                        }
                    }
                } else {
                    editText.setText(all_label_List.get(position).getLabelName());
                    if(addLabel(editText)){
                        LabelList labelList = new LabelList();
                        labelList.setLabelName(all_label_List.get(position).getLabelName());
                        labelList.setLabelGUID(all_label_List.get(position).getLabelGUID());
                        label_list.add(labelList);
                    }
                }
                IsArrayList();
                return false;
            }
        });

        //已经选中的监听
        allFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {

            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                set.clear();
                set.addAll(selectPosSet);
            }
        });
    }

    /**
     * 添加标签
     * @param editText
     * @return
     */
    private boolean addLabel(EditText editText) {
        String editTextContent = editText.getText().toString();
        //判断输入是否为空
        if (editText.getText().toString().toString().equals("")){
            toastShow("输入为空");
            return false;
        }
        //判断是否重复
        for (TextView tag : labels) {
            String tempStr = tag.getText().toString();
            if (tempStr.equals(editTextContent)) {
                toastShow("标签名字重复");
                return false;
            }
        }
        //添加标签
        final TextView temp = getTag(editText.getText().toString());
        labels.add(temp);
        labelStates.add(false);
        //添加点击事件，点击变成选中状态，选中状态下被点击则删除
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curIndex = labels.indexOf(temp);
                if (!labelStates.get(curIndex)) {
                    //显示 ×号删除
                    temp.setText(temp.getText() + " ×");
                    temp.setBackgroundResource(R.drawable.label_del);
                    temp.setTextColor(Color.parseColor("#ffffff"));
                    //修改选中状态
                    labelStates.set(curIndex, true);
                } else {
                    delByTest(temp.getText().toString());
                    flowLayout.removeView(temp);
                    labels.remove(curIndex);
                    labelStates.remove(curIndex);
                     for (int j = 0; j < label_list.size(); j++) {
                         String a=label_list.get(j).getLabelName()+ " ×";
                          if (a.equals(temp.getText().toString())) {
                                label_list.remove(j);
                           }
                     }
                    for (int i = 0; i < label_list.size(); i++) {
                        for (int j = 0; j < labels.size(); j++) {
                            if (label_list.get(i).getLabelName().equals(
                                    labels.get(j).getText())) {
                                if(!TextUtils.isEmpty(label_list.get(i).getLabelGUID())){
                                    tagAdapter.setSelectedList(i);
                                }
                            }
                        }
                    }
                    tagAdapter.notifyDataChanged();
                    IsArrayList();
                }
            }
        });
        flowLayout.addView(temp);
        //让输入框在最后一个位置上
        editText.bringToFront();
        //清空编辑框
        editText.setText("");
        editText.requestFocus();
        return true;

    }
    /**
     * 根据字符删除标签
     * @param text
     */
    private void delByTest(String text) {

        for (int i = 0; i < all_label_List.size(); i++) {
            String a = all_label_List.get(i).getLabelName()+ " ×";
            if (a.equals(text)) {
                set.remove(i);
            }
        }
        tagAdapter.setSelectedList(set);//重置选中的标签

    }


    /**
     * 标签恢复到正常状态
     */
    private void tagNormal() {
        //输入文字时取消已经选中的标签
        for (int i = 0; i < labelStates.size(); i++) {
            if (labelStates.get(i)) {
                TextView tmp = labels.get(i);
                tmp.setText(tmp.getText().toString().replace(" ×", ""));
                labelStates.set(i, false);
                tmp.setBackgroundResource(R.drawable.label_normal);
                tmp.setTextColor(Color.parseColor("#FD6530"));
            }
        }
    }


    /**
     * 创建一个正常状态的标签
     * @param label
     * @return
     */
    private TextView getTag(String label) {
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(12);
        textView.setBackgroundResource(R.drawable.label_normal);
        textView.setTextColor(Color.parseColor("#FD6530"));
        textView.setText(label);
        textView.setLayoutParams(params);
        return textView;
    }
    private void AddMulti() {
        if(editText.getText().toString().length()>19){
            toastShow("标签名字不能超过18个字");
            return;
        }
        if(!TextUtils.isEmpty(editText.getText().toString().trim())){
            toastShow("请点击白色区域确认标签输入完成");
            return;
        }
        dialog.show();
        String url = AppUrl.ADDMULTIPLEEASELABEL;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            JSONArray path =new JSONArray();
            for (int c = 0; c < label_list.size(); c++){
                JSONObject jo =new JSONObject();
                jo.put("labelGUID",label_list.get(c).getLabelGUID());
                jo.put("userGUID",SJQApp.user.getGuid());
                jo.put("labelName",label_list.get(c).getLabelName());
                jo.put("friendGUID",friendGUID);
                path.put(jo);
            }
            param.put("multipeLabel", path);
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
                Gson gson = new Gson();
                SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
                if (bean != null) {
                    if (bean.result.operatResult) {
                        loadata();
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
    private void loadata() {
        String url= AppUrl.GETEASELABLE+ SJQApp.user.getGuid();
        RequestCallBack<String> callBack = new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String message) {
                dialog.dismiss();
                toastShow(message);
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog == null)
                    return;
                dialog.dismiss();
                Gson gson = new Gson();
                NewLabelList bean=gson.fromJson(responseInfo.result, NewLabelList.class);
                if(bean!=null){
                    CacheUtils.cacheStringData( MainlabelActivity.this, "lablelist",responseInfo.result);
                    Intent data = new Intent();
                    setResult(1, data);
                    finish();
                }

            }
        };
        getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
                callBack);
    }
    private void IsArrayList(){
        if(label_list.size()==0){
            tv_title2.setEnabled(false);
            tv_title2.setTextColor(getResources().getColor(R.color.app_color1));
        }else{
            tv_title2.setEnabled(true);
            tv_title2.setTextColor(getResources().getColor(R.color.app_color));
        }
    }
}
