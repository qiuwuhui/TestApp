package com.baolinetworktechnology.shejiquan.activity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.drawable;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
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

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ZhuanxiuActivity extends BaseActivity implements OnClickListener{

	private EditText name_edit;
	private Button wanchen;
	private ImageView image_01;
	private String path="";
	private BitmapUtils mImageUtil;
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
		setContentView(R.layout.activity_zhuanxiu);
		findViewById(R.id.back).setOnClickListener(this);
		name_edit = (EditText) findViewById(R.id.name_edit);
		image_01 = (ImageView) findViewById(R.id.image_01);
		image_01.setOnClickListener(this);
		wanchen = (Button) findViewById(R.id.wanchen);
		wanchen.setOnClickListener(this);
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
	private void initBuitls() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String diskCachePath = Environment.getExternalStorageDirectory()
					+ "/SheJiQuan/CacheImage";
			mImageUtil = new BitmapUtils(ZhuanxiuActivity.this, diskCachePath);
		} else {
			mImageUtil = new BitmapUtils(ZhuanxiuActivity.this);
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
			selectImageView();
			break;
		case R.id.wanchen:
			if(name_edit.getText().toString().equals("")){
				toastShow("请填写公司名称");
			}else if(path.equals("")){
				toastShow("请选择企业执照");
			}else{
			   new Thread(downloadRun).start();  
			   dialog.show();	
			}
//			finish();
			break;

		default:
			break;
		}
		
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
						pickImage();
					}

				}).addSheetItem("去相册选择", SheetItemColor.Blue,
				new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						selectImage();
					}
				});
		actionDialog.show();
	}
	// 拍照
		protected void pickImage() {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 设置日期格式
					String name = df.format(new Date()) + ".jpg";
					File dir= new File(Environment.getExternalStorageDirectory().getPath()
							+ "/shejiquan");
					if (!dir.exists()) {
						dir.mkdirs();
					}
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File pickFile = new File(dir, name);// localTempImgDir和localTempImageFileName是自己定义的名字
					path=pickFile.getAbsolutePath();
					Uri imageViewUri = Uri.fromFile(pickFile);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageViewUri);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				} catch (Exception e) {
					Toast.makeText(this, "当前摄像头无法访问", Toast.LENGTH_SHORT).show();
				}
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
				path=file.toString();
				uploadFile(path);
			}

		}
		//拍照图片
	private void uploadFile(String pathimg) {
		File file =new File(pathimg);
		final String url = ApiUrl.UPLOAD_FILE;
		RequestParams params = getParams(url);
		params.addBodyParameter("file", file);
		params.addBodyParameter("UserID", SJQApp.user.id + "");
		
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
				Bean data = CommonUtils.getDomain(n, Bean.class);
				if (data != null) {
					if (data.success) {
						String path = CommonUtils
								.getString(n.result, "url");
						mImageUtil.display(image_01, path);
					} else {
						toastShow(data.message);
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
	getHttpUtils().send(HttpMethod.POST,ApiUrl.API+url, params, callBack);															
	}
	 Runnable downloadRun = new Runnable(){  
		  
		 @Override  
		 public void run() {  
			 final String url = ApiUrl.API+ApiUrl.UPDATE_BUSINESS;
				 HttpPost httpRequest =new HttpPost(url); 
			 httpRequest.addHeader("Content-Type", "application/json");		 
			  //发出HTTP request  
		     try {
//		    	 HashMap<String, String> hashMap=new HashMap<String, String>();
		    	 JSONObject jsonObject = new JSONObject(); 		    	 
		    	 jsonObject.put("BusinessID",SJQApp.ZhuanxiuData.getID()+"");
		    	 jsonObject.put("BusinessName", name_edit.getText().toString());
		    	 jsonObject.put("imgPath", path);
				httpRequest.setEntity(new StringEntity(jsonObject.toString(),HTTP.UTF_8));
				HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest); 
				 if(httpResponse.getStatusLine().getStatusCode()==200){  
					  String strResult=EntityUtils.toString(httpResponse.getEntity());
					  JSONObject json=new JSONObject(strResult);
					  boolean success=json.getBoolean("success");
					  Message message = Message.obtain();
					  if(success){
						  message.what = 1;			  
					  }else{
						  message.what = 2;
					  }
					  timeHandler.sendMessage(message);
				     }else{  
				      Message message = Message.obtain();
					  message.what = 2;
					  timeHandler.sendMessage(message);
				     }  
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
		    	 // TODO Auto-generated catch block
		    	 e.printStackTrace();
		     } catch (IOException e) {
		    	 // TODO Auto-generated catch block
		    	 e.printStackTrace();
		     } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 }  
		   };  
		   private Handler timeHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what == 1) {
						dialog.dismiss();
						toastShow("提交企业认证成功,待审核");
						Intent intent=new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}else if(msg.what == 2){
						dialog.dismiss();
						toastShow("提交企业认证失败");
					}

				}
			};	
   public static String toUtf8String(String s) {
       StringBuffer sb = new StringBuffer();
       for (int i = 0; i < s.length(); i++) {
           char c = s.charAt(i);
           if (c >= 0 && c <= 255) {
               sb.append(c);
           } else {
               byte[] b;
               try {
                   b = String.valueOf(c).getBytes("utf-8");
               } catch (Exception ex) {
                   System.out.println(ex);
                   b = new byte[0];
               }
               for (int j = 0; j < b.length; j++) {
                   int k = b[j];
                   if (k < 0)
                       k += 256;
                   sb.append("%" + Integer.toHexString(k).toUpperCase());
               }
           }
       }
       return sb.toString();
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
			case CAMERA_REQUEST_CODE:// 拍照
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
}
