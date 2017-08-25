package com.baolinetworktechnology.shejiquan.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.PathBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.UpfileBean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.Pictures;
import com.baolinetworktechnology.shejiquan.utils.SDPathUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReleaseCricleActivity extends BaseActivity implements OnClickListener {
    private GridView noScrollgridview;
    private IDCardPreviewAdapter adapter;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private View parentView;
    public static final int GET_PHOTO_FROM_CAMERA = 0;
    public static final int GET_PHOTO_FROM_ALBUM = 1;
    private BitmapUtils bitmapUtils;
    private String path;
    private String saveDir = Environment.getExternalStorageDirectory()
            + File.separator + "rndchina" + File.separator + "jstx"
            + File.separator + "picture" + File.separator;
    private int maxNum = 9;
    private EditText etitConten;
    private TextView tv_title2;
    private List<PathBean> path_list; // 保存上传后的图片地址
    private List<File> filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pictures.clearCache();
        parentView = getLayoutInflater().inflate(
                R.layout.activity_release_cricle, null);
        setContentView(parentView);
        //魅族
        CommonUtils.setMeizuStatusBarDarkIcon(ReleaseCricleActivity.this, true);
        //小米
        CommonUtils.setMiuiStatusBarDarkMode(ReleaseCricleActivity.this, true);
        TextView tv_title= (TextView) findViewById(R.id.tv_title);
        tv_title.setFocusable(true);
        tv_title.setFocusableInTouchMode(true);
        tv_title.requestFocus();
        initImageLoader();
        inview();

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
    private void inview() {
        ImageView back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        tv_title2= (TextView) findViewById(R.id.tv_title2);
        tv_title2.setOnClickListener(this);
        tv_title2.setEnabled(false);
        etitConten = (EditText) findViewById(R.id.etitConten);
        etitConten.addTextChangedListener(watcher);
        pop = new PopupWindow(ReleaseCricleActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
                null);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setContentView(view);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0003);
            }
        });

        bt2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
            }
        });
        bt3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        noScrollgridview = (GridView) findViewById(R.id.publishImage);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new IDCardPreviewAdapter(this, handler, 9);
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if (position == Pictures.addrs.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(
                            ReleaseCricleActivity.this,
                            R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(
                            ReleaseCricleActivity.this.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    Intent intent = new Intent(ReleaseCricleActivity.this,
                            PhotoActivity.class);
                    intent.putExtra(PhotoActivity.IMAGE_URL, Pictures.addrs.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()){
                case R.id.back:
                    finish();
                    Pictures.clearCache();
                    break;
                case R.id.tv_title2:
                    if (SJQApp.user == null) {
                        toastShow("请先登录");
                        go2Login();
                        return;
                    }
                    filepath=new ArrayList<File>();
                    for (int c = 0; c <  Pictures.cache_addrs.size(); c++){
                        filepath.add(new File(Pictures.cache_addrs.get(c)));
                    }
                    tv_title2.setEnabled(false);
                    if(filepath.size()!=0){
                        uploadFile(filepath);
                    }else{
                       ArrayList<PathBean> pathlist=new ArrayList<PathBean>();
                        addPost(pathlist);
                    }
                    break;
            }
    }
    // 上传文件
    public void uploadFile(List<File> file) {
        final String url = AppUrl.UPLOAD_FILE;
        RequestParams params = getParams(url);
        for (int c = 0; c < file.size(); c++){
            params.addBodyParameter("file"+c,file.get(c));
        }
        RequestCallBack<String> callBack = new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null) {
                    dialog.setCancelable(false);
                    dialog.show("信息上传中");

                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> n) {
                if (dialog == null)
                    return;
                dialog.dismiss();
                UpfileBean data = CommonUtils.getDomain(n, UpfileBean.class);
                if (data != null) {
                    if (data.success) {
                           path_list=new ArrayList<PathBean>();
                        for (int c = 0; c < data.result.size(); c++){
                            PathBean pathBean = new PathBean();
                            pathBean.setUrl(data.result.get(c).Url);
                            pathBean.setHeigth(data.result.get(c).ImgHeight);
                            pathBean.setWidth(data.result.get(c).ImgWidth);
                            path_list.add(pathBean);
                        }
                        addPost(path_list);
                    } else {
                        toastShow("图片上传失败");
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null)
                    dialog.dismiss();
                tv_title2.setEnabled(true);
                AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
                        error.getExceptionCode() + "", "POST", url);
                toastShow("无法连接到服务器，请稍后重试");
            }

        };
        getHttpUtils(2 * 10000).send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }
    private void addPost(final List<PathBean> logoUrl) {
        dialog.show();
        String url = AppUrl.ADDPOSTS;
        RequestParams params = new RequestParams();
        params.setHeader("Content-Type","application/json");
        try {
            JSONObject param  = new JSONObject();
            param.put("userGUID", SJQApp.user.guid);
            param.put("enumMarkName", "2");
            param.put("contents",etitConten.getText().toString());
            if (SJQApp.userData != null) {
                param.put("author", SJQApp.userData.getName());
                param.put("logo", SJQApp.userData.getLogo());
            }else if (SJQApp.ownerData != null) {
                param.put("author", SJQApp.ownerData.getName());
                param.put("logo", SJQApp.ownerData.getLogo());
            }
            JSONArray path =new JSONArray();
            for (int c = 0; c < logoUrl.size(); c++){
                JSONObject jo =new JSONObject();
                jo.put("url",logoUrl.get(c).getUrl());
                jo.put("heigth",logoUrl.get(c).getHeigth());
                jo.put("width",logoUrl.get(c).getWidth());
                path.put(jo);
            }
            param.put("path", path);
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
                    dialog.show("发布中...");
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
                        toastShow("发布成功");
                        Pictures.clearCache();
//							 发送 一个无序广播
                        Intent intent = new Intent();
                        intent.setAction("setRefresh");
                        sendBroadcast(intent);
                        finish();
                    }else{
                        tv_title2.setEnabled(true);
                        toastShow("发布失败请重新点击");
                    }
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (dialog != null)
                    dialog.dismiss();
                tv_title2.setEnabled(true);
                toastShow("无法连接到服务器，请稍后重试");
            }

        };
        getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params, callBack);
    }
    /**
     * 去登录
     */
    private void go2Login() {
        Intent intent = new Intent(ReleaseCricleActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public class IDCardPreviewAdapter extends BaseAdapter {
        public static final int REFRESH_LIST = 0;
        private Context context;
        private Handler handler;
        private int max;

        public IDCardPreviewAdapter(Context context, Handler handler, int max) {
            super();
            // TODO Auto-generated constructor stub
            this.context = context;
            this.handler = handler;
            this.max = max;
        }

        public void loading() {
            // TODO Auto-generated method stub

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return (Pictures.cache_addrs.size() + 1);
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context,
                        R.layout.item_idcard_preview, null);
                holder = new ViewHolder();
                holder.image=(ImageView) convertView.findViewById(R.id.item);
                holder.isselected = (ImageView) convertView.findViewById(R.id.isselected);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == Pictures.cache_addrs.size()) {
                holder.image.setImageBitmap(null);
                holder.isselected.setVisibility(View.GONE);
                if (position == max) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.isselected.setVisibility(View.VISIBLE);
                holder.image.setImageBitmap(Pictures.bmps.get(position));
            }
            final int a=position;
            holder.isselected.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pictures.cache_addrs.remove(a);
                    Pictures.bmps.remove(a);
                    Pictures.addrs.remove(a);
                    Pictures.max -=1;
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView  isselected;
        }
    }
    /**
     * disable caches
     */
    private void initImageLoader() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String diskCachePath = Environment.getExternalStorageDirectory()
                    + "/SheJiQuan/CacheImage";
            bitmapUtils = new BitmapUtils(this, diskCachePath);
        } else {
            bitmapUtils = new BitmapUtils(this);
        }

        bitmapUtils.configDefaultLoadingImage(R.drawable.icon_morentxs);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon_morentxs);

        long pretime = CacheUtils.getLongData(this, "ImageLoader");
        long currentt = System.currentTimeMillis();
        if (currentt - pretime > 60 * 1000 * 60 * 24 * 15) {
            // 15天清除一次缓存
            bitmapUtils.clearCache();
            CacheUtils.cacheLongData(this, "ImageLoader", currentt);
        }

    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IDCardPreviewAdapter.REFRESH_LIST:

                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String createPhotoName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_PHOTO_FROM_CAMERA:
                if (resultCode == RESULT_OK) {
                    Pictures.addrs.add(path);
                    adapter.notifyDataSetChanged();
                    loadingss();
                }
                break;
            case GET_PHOTO_FROM_ALBUM:
                adapter.notifyDataSetChanged();
                loadingss();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etitConten.getWindowToken(), 0); //强制隐藏键盘
    }
    @Override
    protected void onDestroy() {
        Pictures.clearCache();
        if(pop !=null){
            pop.dismiss();
        }
        super.onDestroy();
    }
    public void loadingss() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Pictures.max == Pictures.addrs.size()) {
                        handler.sendEmptyMessage(IDCardPreviewAdapter.REFRESH_LIST);
                        break;
                    } else {
                        try {
                            String path = Pictures.addrs.get(Pictures.max);
                            Matrix matrix = new Matrix();
                            matrix.setRotate(readPictureDegree(path));
                            Bitmap tmpBitmap = Pictures.revitionImageSize(path);
                            int width = tmpBitmap.getWidth();
                            int height = tmpBitmap.getHeight();
                            Bitmap bm = Bitmap.createBitmap( tmpBitmap, 0, 0, width, height, matrix, true );
                            if (bm == null) {
                                bm = BitmapFactory.decodeResource(
                                        getResources(),
                                        R.drawable.icon_morentxs);
                            }
                            Pictures.bmps.add(bm);
                            String cacheImage = "sjqpost" + System.currentTimeMillis() + ".JPEG";
                            SDPathUtils.saveBitmap(bm, cacheImage);
                            Pictures.cache_addrs.add(SDPathUtils.getCachePath() + cacheImage);
                            Pictures.max += 1;
                            handler.sendEmptyMessage(IDCardPreviewAdapter.REFRESH_LIST);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0002:
                    Intent intent1 = new Intent(ReleaseCricleActivity.this,
                            PhotoAlbum.class);
                    intent1.putExtra(PhotoAlbum.MAX_PHOTO_NUM, maxNum
                            - Pictures.addrs.size());
                    startActivityForResult(intent1, GET_PHOTO_FROM_ALBUM);
                    pop.dismiss();
                    ll_popup.clearAnimation();
                break;
            case 0x0003:
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {
                        try {
                            File dir = new File(saveDir);
                            if (!dir.exists())
                                dir.mkdirs();
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(saveDir + createPhotoName());
                            // callback.callBack(file.getPath());
                            path = file.getPath();
                            Uri imageUri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, GET_PHOTO_FROM_CAMERA);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(ReleaseCricleActivity.this, "没有找到储存目录",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ReleaseCricleActivity.this, "没有储存卡",
                                Toast.LENGTH_LONG).show();
                    }
                    pop.dismiss();
                    ll_popup.clearAnimation();
                break;
        }

    }
    private String comment="";
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable arg0) {
        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            comment =etitConten.getText().toString().trim();
            if(!TextUtils.isEmpty(comment)){
                tv_title2.setEnabled(true);
                tv_title2.setTextColor(getResources().getColor(R.color.app_color));
                tv_title2.setBackgroundResource(R.drawable.yuanjiao_layout8);
            }
            if(TextUtils.isEmpty(comment)){
                tv_title2.setEnabled(false);
                tv_title2.setTextColor(getResources().getColor(R.color.shouqi));
                tv_title2.setBackgroundResource(R.drawable.yuanjiao_layout9);
            }
        }
    };
}
