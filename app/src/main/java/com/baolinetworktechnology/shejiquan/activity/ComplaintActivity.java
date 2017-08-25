package com.baolinetworktechnology.shejiquan.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ComplaintActivity extends BaseActivity implements View.OnClickListener{
   private CheckBox chBox1,chBox2,chBox3,chBox4,chBox5,chBox6;
   private int userID;
   private String userGUID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        userGUID = getIntent().getStringExtra("userGUID");
        userID =getIntent().getIntExtra("userID",0);
        invIvew();
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
    private void invIvew() {
         chBox1 = (CheckBox) findViewById(R.id.chBox1);
         chBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                 if(b){
                     chBox2.setChecked(false);
                     chBox3.setChecked(false);
                     chBox4.setChecked(false);
                     chBox5.setChecked(false);
                     chBox6.setChecked(false);
                 }
             }
         });
         chBox2 = (CheckBox) findViewById(R.id.chBox2);
         chBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    chBox1.setChecked(false);
                    chBox3.setChecked(false);
                    chBox4.setChecked(false);
                    chBox5.setChecked(false);
                    chBox6.setChecked(false);
                }
            }
        });
         chBox3 = (CheckBox) findViewById(R.id.chBox3);
         chBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    chBox2.setChecked(false);
                    chBox1.setChecked(false);
                    chBox4.setChecked(false);
                    chBox5.setChecked(false);
                    chBox6.setChecked(false);
                }
            }
        });
         chBox4 = (CheckBox) findViewById(R.id.chBox4);
         chBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    chBox2.setChecked(false);
                    chBox3.setChecked(false);
                    chBox1.setChecked(false);
                    chBox5.setChecked(false);
                    chBox6.setChecked(false);
                }
            }
        });
         chBox5 = (CheckBox) findViewById(R.id.chBox5);
         chBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    chBox2.setChecked(false);
                    chBox3.setChecked(false);
                    chBox4.setChecked(false);
                    chBox1.setChecked(false);
                    chBox6.setChecked(false);
                }
            }
        });
        chBox6 = (CheckBox) findViewById(R.id.chBox6);
        chBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    chBox2.setChecked(false);
                    chBox3.setChecked(false);
                    chBox4.setChecked(false);
                    chBox5.setChecked(false);
                    chBox1.setChecked(false);
                }
            }
        });
         findViewById(R.id.jubaoBtn).setOnClickListener(this);
         findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.jubaoBtn:
                AddReport();
                break;
        }
    }

    String contents="";
    private void AddReport() {
        dialog.show();
        if(chBox1.isChecked()){
            contents +="垃圾营销,";
        }
        if(chBox2.isChecked()){
            contents +="不实信息,";
        }
        if(chBox3.isChecked()){
            contents +="有害信息,";
        }
        if(chBox4.isChecked()){
            contents +="违法信息,";
        }
        if(chBox5.isChecked()){
            contents +="淫秽色情,";
        }
        if(chBox6.isChecked()){
            contents +="人身攻击,";
        }
        if(TextUtils.isEmpty(contents)){
            dialog.dismiss();
            toastShow("请选择举报原因");
            return;
        }
        String url = AppUrl.ADDREPORT;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("postsID",userID+"");
            param.put("userGUID", SJQApp.user.getGuid());
            param.put("contents", contents);
            StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
            params.setBodyEntity(sEntity);
        }catch (JSONException e) {
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
                        contents="";
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
                                toastShow("举报成功");
                                finish();
                            }else{
                                toastShow("举报失败，请重新举报");
                            }
                        }
                    }
                });
    }
}
