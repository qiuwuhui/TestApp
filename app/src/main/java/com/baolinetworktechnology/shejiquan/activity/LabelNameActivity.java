package com.baolinetworktechnology.shejiquan.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.domain.LabelList;
import com.baolinetworktechnology.shejiquan.domain.NewLabelBean;
import com.baolinetworktechnology.shejiquan.domain.NewLabelList;
import com.baolinetworktechnology.shejiquan.domain.SortModelList;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.google.gson.Gson;

import java.util.List;

public class LabelNameActivity extends BaseActivity {
    private String LableName="";
    private EditText etitName;
    private List<NewLabelBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_name);
        LableName=getIntent().getStringExtra("LableName");
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.tv_title2).setOnClickListener(this);
        etitName = (EditText) findViewById(R.id.etitName);
        if(!TextUtils.isEmpty(LableName)){
            etitName.setText(LableName);
        }
        showData();
    }

    private void showData() {
        String lablestr= CacheUtils.getStringData(getActivity(), "lablelist", null);
        if (lablestr != null) {
            Gson gson = new Gson();
            NewLabelList bean = gson.fromJson(lablestr, NewLabelList.class);
            if (bean != null && bean.result!=null) {
                list = bean.result;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_title2:
                if (TextUtils.isEmpty(etitName.getText().toString().trim())) {
                    toastShow("标签名字不能为空");
                    return;
                }
                for (int i = 0; i < list.size() ; i++){
                    if( list.get(i).getLabelName().equals(etitName.getText().toString())){
                        toastShow("标签名字不能重复");
                        return;
                    }
                }
                Intent data = new Intent();
                data.putExtra("LableName", etitName.getText().toString());
                setResult(1, data);
                finish();
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
}
