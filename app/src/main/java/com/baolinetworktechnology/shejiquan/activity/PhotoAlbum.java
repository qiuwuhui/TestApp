package com.baolinetworktechnology.shejiquan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.PhotoAlbumAdapter;
import com.baolinetworktechnology.shejiquan.domain.ImageBucket;
import com.baolinetworktechnology.shejiquan.domain.ImageItem;
import com.baolinetworktechnology.shejiquan.utils.AlbumHelper;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.Pictures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PhotoAlbum extends Activity {
    public static final String MAX_PHOTO_NUM = "max_photo_num";

    private List<ImageItem> dataList;
    private List<ImageBucket> albumList;// 相册列表
    private GridView gridView;
    private PhotoAlbumAdapter adapter;
    private AlbumHelper helper;
    private TextView complete;
    private ImageButton left_buttonTitleBarBack;
    private int maxNum = 9;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(PhotoAlbum.this, "最多选择" + maxNum + "张图片",
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    private String tv_talk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        //魅族
        CommonUtils.setMeizuStatusBarDarkIcon(PhotoAlbum.this, true);
        //小米
        CommonUtils.setMiuiStatusBarDarkMode(PhotoAlbum.this, true);
        int getNum = getIntent().getIntExtra(MAX_PHOTO_NUM, 0);
        if (getNum != 0) {
            maxNum = getNum;
        }

        helper = new AlbumHelper();// AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        albumList = helper.getImagesBucketList(false);
        dataList = new ArrayList<ImageItem>();

        for (ImageBucket ib : albumList) {
            dataList.addAll(ib.imageList);
        }

        initView();
    }

    private void initView() {
        complete = (TextView) findViewById(R.id.complete);
        gridView = (GridView) findViewById(R.id.gridview);
        left_buttonTitleBarBack = (ImageButton) findViewById(R.id.left_buttonTitleBarBack);
        complete.setText("完成" + "(" + "0/" + maxNum + ")");
        complete.setTextColor(getResources().getColor(R.color.shouqi));
        complete.setBackgroundResource(R.drawable.yuanjiao_layout9);
        complete.setEnabled(false);
        adapter = new PhotoAlbumAdapter(PhotoAlbum.this, dataList, mHandler,
                maxNum);
        adapter.setTextCallback(new PhotoAlbumAdapter.TextCallback() {
            public void onListen(int count) {
                complete.setText("完成" + "(" + count + "/" + maxNum + ")");
                if (count == 0) {
                    complete.setTextColor(getResources().getColor(R.color.shouqi));
                    complete.setBackgroundResource(R.drawable.yuanjiao_layout9);
                    complete.setEnabled(false);
                } else {
                    complete.setTextColor(getResources().getColor(R.color.app_color));
                    complete.setBackgroundResource(R.drawable.yuanjiao_layout8);
                    complete.setEnabled(true);
                }
            }
        });

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                adapter.notifyDataSetChanged();
            }

        });

        complete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext();) {
                    Pictures.addrs.add(it.next());
                }
                finish();
            }
        });

        left_buttonTitleBarBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PhotoAlbum.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
