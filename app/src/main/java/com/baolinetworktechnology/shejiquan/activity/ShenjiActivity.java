package com.baolinetworktechnology.shejiquan.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.UpfileBean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.Pictures;
import com.google.gson.Gson;
import com.guojisheng.koyview.ActionSheetDialog;
import com.guojisheng.koyview.ActionSheetDialog.OnSheetItemClickListener;
import com.guojisheng.koyview.ActionSheetDialog.SheetItemColor;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

public class ShenjiActivity extends BaseActivity implements OnClickListener{

	private EditText name_edit;
	private EditText haoma_edit;
	private Button wanchen;
	private ImageView image_01;
	private ImageView image_02;
	private ImageView image_03;
	private String imageFile01="";
	private String imageFile02="";
	private String imageFile03="";
	private int indxImge=0;
	private BitmapUtils mImageUtil;
	private String path;
	public static final int GET_PHOTO_FROM_CAMERA = 0;
	private String saveDir = Environment.getExternalStorageDirectory()
			+ File.separator + "rndchina" + File.separator + "jstx"
			+ File.separator + "picture" + File.separator;
	/**
	 * 图裤请求码
	 */
	private static final int IMAGE_REQUEST_CODE = 50;//
	/**
	 * 照相请求码
	 */
	private static final int CAMERA_REQUEST_CODE = 10;//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shenji);
		findViewById(R.id.back).setOnClickListener(this);
		initView();
		initBuitls();
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
	private void initView() {
		name_edit = (EditText) findViewById(R.id.name_edit);
		name_edit.clearFocus();
		haoma_edit = (EditText) findViewById(R.id.haoma_edit);
		wanchen = (Button) findViewById(R.id.shiming);
		wanchen.setOnClickListener(this);
		image_01 = (ImageView) findViewById(R.id.image_01);
		image_01.setOnClickListener(this);
		image_02 = (ImageView) findViewById(R.id.image_02);
		image_02.setOnClickListener(this);
		image_03 = (ImageView) findViewById(R.id.image_03);
		image_03.setOnClickListener(this);
	}
	private void initBuitls() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String diskCachePath = Environment.getExternalStorageDirectory()
					+ "/SheJiQuan/CacheImage";
			mImageUtil = new BitmapUtils(ShenjiActivity.this, diskCachePath);
		} else {
			mImageUtil = new BitmapUtils(ShenjiActivity.this);
		}
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();			
			break;
		case R.id.image_01:
			indxImge=1;
			selectImageView();
			break;
		case R.id.image_02:	
			indxImge=2;
			selectImageView();
			break;
		case R.id.image_03:	
			indxImge=3;
			selectImageView();
			break;
		case R.id.shiming:	
			if(name_edit.getText().toString().equals("")){
				MobclickAgent.onEvent(ShenjiActivity.this,"kControlDesignerInfoCompleteInfo"); //  设计师个人资料点击认证弹出的完善资料警告框事件
				toastShow("名字不能为空");
			}else if(haoma_edit.getText().toString().equals("")){
				MobclickAgent.onEvent(ShenjiActivity.this,"kControlDesignerInfoCompleteInfo"); //  设计师个人资料点击认证弹出的完善资料警告框事件
				toastShow("身份号码不能为空");
			}else if(!test(haoma_edit.getText().toString())){
				MobclickAgent.onEvent(ShenjiActivity.this,"kControlDesignerInfoCompleteInfo"); //  设计师个人资料点击认证弹出的完善资料警告框事件
				toastShow("身份号码输入错误");
			}else if(imageFile02.equals("")
					|| imageFile03.equals("")){
				MobclickAgent.onEvent(ShenjiActivity.this,"kControlDesignerInfoCompleteInfo"); //  设计师个人资料点击认证弹出的完善资料警告框事件
				toastShow("请上传完整的照片");				
			}else{
				doSave();
			}
			break;
		default:
			break;
		}
		
	}
	// 提交保存到服务器 	
		private void doSave() {
			if (SJQApp.user == null) {
				finish();
				return;
			}
			if (SJQApp.userData == null) {
				toastShow("数据未获取到，请稍候");
				return;
			}
			if (name_edit.getText().toString().length() < 2) {
				MobclickAgent.onEvent(ShenjiActivity.this,"kControlDesignerInfoCompleteInfo"); //  设计师个人资料点击认证弹出的完善资料警告框事件
				name_edit.setError("姓名至少两位数");
				return;
			}
			dialog.show();
			final String url = AppUrl.CardAuthentication;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("designerGUID",SJQApp.user.guid);
				param.put("realName",name_edit.getText().toString());
				param.put("cardNo",haoma_edit.getText().toString());
				param.put("picCardHand",imageFile01);
				param.put("picCardUp",imageFile02);
				param.put("picCardDown",imageFile03);
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

//			if (SJQApp.userData != null)
//			params.addBodyParameter("realName", name_edit.getText().toString());
//			params.addBodyParameter("cardNo", haoma_edit.getText().toString());
//			params.addBodyParameter("picCardHand", imageFile01);
//			params.addBodyParameter("picCardUp", imageFile02);
//			params.addBodyParameter("picCardDown", imageFile03);
			RequestCallBack<String> callBack = new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> n) {
					if (dialog == null)
						return;
					Gson gson = new Gson();
					SwresultBen data=gson.fromJson(n.result, SwresultBen.class);
					if (data != null) {
						toastShow(data.result.operatMessage);
						if (data.result.operatResult) {
							MobclickAgent.onEvent(ShenjiActivity.this,"kControlDesignerCertification"); // 设计师认证提交事件。注：提交成功才记录
							toastShow("上传认证信息成功");
							Intent intent=new Intent();
							setResult(RESULT_OK, intent);
							finish();
						}else{
							toastShow("上传认证信息失败,请稍后");
						}
					}else{
						toastShow("上传认证信息失败,请稍后");
					}
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					if (dialog == null)
						return;
					dialog.dismiss();
					toastShow("上传认证信息失败,请稍后");
					AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
							error.getExceptionCode() + "", "POST", url);
				}
			};
			hideInput();
			getHttpUtils()
					.send(HttpMethod.POST, AppUrl.API + url, params, callBack);
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:// 图片
				Uri2File(data.getData());
				break;
			case GET_PHOTO_FROM_CAMERA:// 拍照
				if (isSdcardExisting()) {									
					uploadFile(path);
				} else {
					Toast.makeText(this, "未找到存储卡，无法存储照片!", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	//相册图片
	private void Uri2File(Uri uri) {
		if (uri != null) {
			final String scheme = uri.getScheme();
			String data = null;
			if (scheme == null)
				data = uri.getPath();
			else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
				data = uri.getPath();
			} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
				Cursor cursor = this.getContentResolver().query(uri,
						new String[] { ImageColumns.DATA }, null, null, null);
				if (null != cursor) {
					if (cursor.moveToFirst()) {
						int index = cursor.getColumnIndex(ImageColumns.DATA);
						if (index > -1) {
							data = cursor.getString(index);
						}
					}
					cursor.close();
				}
			}
			File file = new File(data);
			String strurl=file.toString();		
			uploadFile(strurl);
		}

	}
	//拍照图片
  private void uploadFile(String pathimg) {
	File file =new File(pathimg);
	final String url = AppUrl.UPLOAD_FILE;
	RequestParams params = getParams(url);
	params.addBodyParameter("file", file);
	RequestCallBack<String> callBack = new RequestCallBack<String>() {
		@Override
		public void onStart() {
			if (dialog == null)
				return;
			dialog.show("上传中");
		}

		@Override
		public void onSuccess(ResponseInfo<String> n) {
			if (dialog == null)
				return;
			dialog.dismiss();
			UpfileBean data = CommonUtils.getDomain(n, UpfileBean.class);
			if (data != null) {
				if (data.success) {
					String path =data.result.get(0).Url;
					if(indxImge==1){
						mImageUtil.display(image_01, path);
						imageFile01=path;
					}else if(indxImge==2){
						mImageUtil.display(image_02, path);
						imageFile02=path;
					}else if(indxImge==3){
						mImageUtil.display(image_03, path);
						imageFile03=path;
					}
				} else {
					toastShow(data.result.get(0).Message);
				}
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {			
			if (dialog == null)
				return;
			dialog.dismiss();
			AppErrorLogUtil.getErrorLog(getApplicationContext()).postError(
					error.getExceptionCode() + "", "POST", url);
			toastShow("无法连接到服务器，请稍后重试");
		}

	};
	getHttpUtils().send(HttpMethod.POST,AppUrl.API+url, params, callBack);
   }
	private void selectImageView() {
		ActionSheetDialog actionDialog = new ActionSheetDialog(this);
		actionDialog.builder();
		actionDialog.setCancelable(true);
		actionDialog.setCanceledOnTouchOutside(true);
		actionDialog.addSheetItem("拍照", SheetItemColor.Blue,
				new OnSheetItemClickListener() {

					@Override
					public void onClick(int which) {
						requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0003);
					}

				}).addSheetItem("去相册选择", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
					}
				});
		actionDialog.show();
	}


	//图库
	protected void selectImage() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
	}
	/**
	 * 储存卡是否可用
	 * 
	 * @return
	 */
	private boolean isSdcardExisting() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
     public boolean test(String str){
    	 if(str.length()==18){
    		 Scanner in=new Scanner(str);
    		 String s="^[0-9]{17}([0-9]|x)";//正则表达式
    		 String str1=in.next();
    		 while(!str1.matches(s)){//用mathes方法匹配正则表达式，判断是否合法
    			 str1=in.next();
    			 return false;
    		 }    		 
    	 }else{
    		 return false;
    	 }
	 return true;
	}
	@Override
	protected void onPause() {
		super.onPause();
		InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(name_edit.getWindowToken(), 0); //强制隐藏键盘
		MobclickAgent.onPageEnd("ShenjiActivity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ShenjiActivity");
	}
	@Override
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);
		switch (requestCode) {
			case 0x0002:
				selectImage();
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
						Toast.makeText(ShenjiActivity.this, "没有找到储存目录",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ShenjiActivity.this, "没有储存卡",
							Toast.LENGTH_LONG).show();
				}
				break;
		}

	}
	private String createPhotoName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
}
