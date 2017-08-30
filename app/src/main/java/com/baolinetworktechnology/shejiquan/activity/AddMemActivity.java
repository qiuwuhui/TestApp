package com.baolinetworktechnology.shejiquan.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baolinetwkchnology.shejiquan.xiaoxi.SideBar;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.db.DBHelper;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.domain.SortModelList;
import com.baolinetworktechnology.shejiquan.tongxunlu.AddMenAdapter;
import com.baolinetworktechnology.shejiquan.tongxunlu.StickyRecyclerHeadersDecoration;
import com.baolinetworktechnology.shejiquan.utils.CharacterParser;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.PinyinComparator;
import com.guojisheng.koyview.MyEditText;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 标签成员添加页
 */
public class AddMemActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private AddMenAdapter mAdapter;
    private SideBar sideBar;
    private TextView dialogText;
    private PinyinComparator pinyinComparator;
    private MyEditText mClearEditText;
    private CharacterParser characterParser;
    private List<SortModel> contactList;
    private LinearLayout add_conten;
    private MyBroadcastReciver mybroad;
    private List<SortModel> AddmLists =new ArrayList<>();
    private BitmapUtils mImageUtil;
    private int addimage=0;
    private HorizontalScrollView contem_lay;
    private LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mem);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("removeMen");
        mybroad=new MyBroadcastReciver();
        AddMemActivity.this.registerReceiver(mybroad, intentFilter);

        //加载图片类
        mImageUtil = new BitmapUtils(this);
        mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxs);
        mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxs);
        mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        initView();
        initData();
        showData();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tv_title2:
                Intent data = new Intent();
                SortModelList bean= new SortModelList();
                bean.result =AddmLists;
                data.putExtra("AddmLists", bean.toString());
                setResult(1, data);
                finish();
//                addPost();
                break;
            case R.id.quxiao:
                finish();
                break;
        }
    }


    private void initView() {
        sideBar = (SideBar) findViewById(R.id.contact_sidebar);
        dialogText = (TextView) findViewById(R.id.contact_dialog);
        mRecyclerView = (RecyclerView) findViewById(R.id.contact_member);
        mClearEditText = (MyEditText) findViewById(R.id.filter_edit);
        mClearEditText.setHintTextColor(getResources().getColor(R.color.shouqi));
        findViewById(R.id.quxiao).setOnClickListener(this);
        findViewById(R.id.tv_title2).setOnClickListener(this);
        add_conten = (LinearLayout) findViewById(R.id.add_conten);
        contem_lay = (HorizontalScrollView) findViewById(R.id.contem_lay);
        setWidth();
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
        mAdapter = new AddMenAdapter(getActivity(), contactList);
        int orientation = LinearLayoutManager.VERTICAL;
        layoutManager = new LinearLayoutManager(getActivity(), orientation, false);
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
            DBHelper helper=new DBHelper(getActivity().getApplicationContext(),DBHelper.DB_NAME);
            Cursor cursor = helper.query(SJQApp.user.getGuid());
            if(contactList == null){
                contactList =new ArrayList<>();
            }else{
                contactList.clear();
            }
            while (cursor.moveToNext()) {
                SortModel sortmodel = new SortModel();
                sortmodel.setFriendGUID(cursor.getString(cursor.getColumnIndex("friendGUID")));
                sortmodel.setFromID(cursor.getInt(cursor.getColumnIndex("fromID")));
                sortmodel.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
                sortmodel.setName(cursor.getString(cursor.getColumnIndex("name")));
                sortmodel.setMarkStatus(cursor.getInt(cursor.getColumnIndex("markStatus")));
                sortmodel.setMarkName(cursor.getString(cursor.getColumnIndex("markName")));
                sortmodel.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
                String mobilesrc=cursor.getString(cursor.getColumnIndex("mobile"));
                if(!TextUtils.isEmpty(mobilesrc)){
                    String[] mobilelist = mobilesrc.split(",");
                    sortmodel.setMobile(mobilelist);
                }
                String isConcern = cursor.getString(cursor.getColumnIndex("isConcern"));
                if (isConcern.equals("1")) {
                    sortmodel.setSortLetters("@");
                    sortmodel.setConcern(true);
                } else {
                    sortmodel.setSortLetters(filledData(cursor.getString(cursor.getColumnIndex("initial"))));
                    sortmodel.setConcern(false);
                }
                sortmodel.setRemarkName(cursor.getString(cursor.getColumnIndex("remarkName")));
                contactList.add(sortmodel);
            }
            cursor.close();
            helper.close();
            String bean = getIntent().getStringExtra("newlable");
            if(!TextUtils.isEmpty(bean)){
                SortModelList sortModelList= CommonUtils.getDomain(bean, SortModelList.class);
                if(sortModelList!=null){
                    AddmLists.addAll(sortModelList.result);
                    for (int i = 0; i < AddmLists.size(); i++){
                        addLabel(AddmLists.get(i).getSmallImages("_200_200."));
                    }
                }
            }
            if(contactList.size() ==0 ){
            }else{
                Collections.sort(contactList, pinyinComparator);
                List<SortModel> addlist= new ArrayList<>();
                addlist.addAll(AddmLists);
                mAdapter.setAddList(addlist);
                mAdapter.setList(contactList);
                mAdapter.notifyDataSetChanged();
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
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = contactList;
            mAdapter.setList(filterDateList);
            mAdapter.notifyDataSetChanged();
        } else {
            filterDateList.clear();
            for (SortModel sortModel : contactList) {
                String name = sortModel.getshowName();
                if(!TextUtils.isEmpty(name)) {
                    if (name.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(name).startsWith(
                            filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
            }
            mAdapter.setfilList(filterDateList);
            mAdapter.notifyDataSetChanged();
        }
    }
    private class MyBroadcastReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //添加成员
            if (action.equals("removeMen")) {
                int position = intent.getIntExtra("position",-1);
                AddchBox(position);
            }
        }
    }
    private void AddchBox(int position) {
        if(position != -1){
            int a=0;
            SortModel dinMode =contactList.get(position);
            if(!TextUtils.isEmpty(dinMode.getFriendGUID())){
                for(int i = 0; i < AddmLists.size(); i++){
                    SortModel sortModel=AddmLists.get(i);
                    if(!TextUtils.isEmpty(sortModel.getFriendGUID())){
                        if(sortModel.getFriendGUID().equals(dinMode.getFriendGUID())){
                            a+=1;
                            View view = layoutManager.findViewByPosition(position);
                            LinearLayout layout = (LinearLayout)view;
                            CheckBox chBox= (CheckBox) layout.findViewById(R.id.add_chBox);
                            chBox.setChecked(false);
                            AddmLists.remove(i);
                            removeLabel(i);
                        }
                    }
                }
            }
            if(a==0){
                View view = layoutManager.findViewByPosition(position);
                LinearLayout layout = (LinearLayout)view;
                CheckBox chBox= (CheckBox) layout.findViewById(R.id.add_chBox);
                chBox.setChecked(true);
                AddmLists.add(dinMode);
                addLabel(dinMode.getSmallImages("_200_200."));
            }
        }
      }
    private void addLabel(String labelImage){
        ImageView imageview =getTag(labelImage);
        add_conten.addView(imageview,addimage);
        addWidth();
        addimage++;

    }
    private void removeLabel(int removeint) {
        add_conten.removeViewAt(removeint);
        removeWidth();
        addimage--;
    }
    //总宽度
    private int layWidth=0;
    //图片加起来占据的宽带
    private int contenWidth=0;
    private WindowManager mWindowManager;
    private int imageWidth;
    private void setWidth(){
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        layWidth=mWindowManager.getDefaultDisplay().getWidth();
        mClearEditText.getLayoutParams().height = 120;
        mClearEditText.getLayoutParams().width = layWidth;
    }
    private void addWidth(){
        if(layWidth-(contenWidth+85)>200){
            contenWidth+=85;
            contem_lay.getLayoutParams().width = contenWidth;
            mClearEditText.getLayoutParams().width =layWidth-contenWidth;
        }
        imageWidth +=85;
    }
    private void removeWidth(){
        if(imageWidth ==contenWidth){
            contenWidth-=85;
            contem_lay.getLayoutParams().width =contenWidth;
            mClearEditText.getLayoutParams().width =layWidth-contenWidth;
        }
        imageWidth -=85;
    }
    /**
     * 创建一个正常状态的标签
     * @return
     */
    private ImageView getTag(String labelImage) {
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imageview= new ImageView(AddMemActivity.this);
        params.setMargins(5, 5, 0, 5);
        imageview.setLayoutParams(params);
        imageview.getLayoutParams().height =80;
        imageview.getLayoutParams().width =80;
        mImageUtil.display(imageview,labelImage);
        return imageview;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
        protected void onDestroy() {
        super.onDestroy();
        if(mRecyclerView != null){
            unregisterReceiver(mybroad);
            mRecyclerView.setAdapter(null);
            add_conten.removeAllViews();
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
