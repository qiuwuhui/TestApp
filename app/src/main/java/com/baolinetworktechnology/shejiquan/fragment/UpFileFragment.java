package com.baolinetworktechnology.shejiquan.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.adapter.UpFile;
import com.baolinetworktechnology.shejiquan.adapter.UpFileAapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.HouseOrder;
import com.baolinetworktechnology.shejiquan.domain.HouseOrderFile;
import com.baolinetworktechnology.shejiquan.domain.SendDesigner;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.ActionSheetDialog;
import com.guojisheng.koyview.ActionSheetDialog.OnSheetItemClickListener;
import com.guojisheng.koyview.ActionSheetDialog.SheetItemColor;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 上传文件-订单
 * 
 * @author JiSheng.Guo
 * 
 */
public class UpFileFragment extends BaseFragment implements OnItemClickListener {
	GridView gridView;
	UpFileAapter upFileAapter;

	private File dir;
	private File pickFile;
	private String FileType;
	MyDialog dialog;

	public void setHouseOrder(HouseOrder order) {
		if (order != null) {
			OrderGUID = order.getGUID();
			OrderID = order.getID() + "";
		}
	}

	public void setFileType(int fileType) {
		FileType = fileType + "";
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public void setOrderGUID(String orderGUID) {
		OrderGUID = orderGUID;
	}

	private String OrderID;
	private String OrderGUID;

	/**
	 * 图裤请求码
	 */
	private static final int IMAGE_REQUEST_CODE = 50;//
	/**
	 * 照相请求码
	 */
	private static final int CAMERA_REQUEST_CODE = 10;//

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = View
				.inflate(getActivity(), R.layout.fragment_up_file, null);
		dialog = new MyDialog(getActivity());
		initView(view);
		return view;
	}

	private void initView(View view) {
		gridView = (GridView) view.findViewById(R.id.gridView);
		upFileAapter = new UpFileAapter(getActivity());
		gridView.setAdapter(upFileAapter);
		upFileAapter.setAddGone(isGone);
		if (FileData != null) {
			upFileAapter.addData(FileData);
		}

		gridView.setOnItemClickListener(this);
		dir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/shejiquan");
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public void setData(List<UpFile> data) {
		if (upFileAapter == null) {
			return;
		}
		upFileAapter.setData(data);
		upFileAapter.notifyDataSetChanged();
	}

	List<UpFile> FileData;

	public void addData(List<UpFile> data) {
		if (upFileAapter == null) {
			this.FileData = data;
			return;
		}
		upFileAapter.addData(data);
		upFileAapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectImageView(position);
	}

	// 选择图片
	private void selectImageView(final int position) {
		if (isGone) {
			go2Photo(position);
			return;
		}
		ActionSheetDialog actionDialog = new ActionSheetDialog(getActivity());
		actionDialog.builder();
		actionDialog.setCancelable(true);
		actionDialog.setCanceledOnTouchOutside(true);
		if (position == 0) {
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
		} else {
			if (upFileAapter.isDelete()) {
				// 显示删除图标时候 直接删除该项
				deleteFile(position);
				return;
			}
			actionDialog.addSheetItem("查看", SheetItemColor.Blue,
					new OnSheetItemClickListener() {

						@Override
						public void onClick(int which) {
							go2Photo(position);
						}

					});

			actionDialog.addSheetItem("删除", SheetItemColor.Blue,
					new OnSheetItemClickListener() {

						@Override
						public void onClick(int which) {
							deleteFile(position);
						}

					});

			actionDialog.show();

		}

	}

	private void deleteFile(final int position) {
		UpFile item = upFileAapter.getItem(position);
		String url = ApiUrl.DeleteDelegationFile + item.GUID;
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
				dialog.show("删除中");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dialog.dismiss();
				Bean bean = CommonUtils.getDomain(arg0, Bean.class);
				if (bean != null) {
					if (bean.success) {
						upFileAapter.getDatas().remove(position);
						upFileAapter.notifyDataSetChanged();
						toastShow("删除成功");
					} else {
						toastShow(bean.message);
					}
				} else {
					toastShow("删除失败");
				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dialog.dismiss();

			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);

	}

	protected void selectImage() {
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
	}

	// 拍照
	protected void pickImage() {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 设置日期格式
			String name = df.format(new Date()) + ".jpg";
			pickFile = new File(dir, name);// localTempImgDir和localTempImageFileName是自己定义的名字
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri imageViewUri = Uri.fromFile(pickFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageViewUri);
			startActivityForResult(intent, CAMERA_REQUEST_CODE);
		} catch (Exception e) {
			toastShow("当前摄像头无法访问");
		}
	}

	private void go2Photo(int position) {

		ArrayList<String> mImageList = new ArrayList<String>();
		List<UpFile> item = upFileAapter.getDatas();
		for (int i = 0; i < item.size(); i++) {
			UpFile file = item.get(i);
			if (file != null) {
				if (isGone) {
					mImageList.add(file.Path);
				} else {
					if (TextUtils.isEmpty(file.absolutePath)) {
						mImageList.add(file.Path);
					} else {
						mImageList.add("file://" + file.absolutePath);
					}
				}
			}
		}

		Intent intent = new Intent(getActivity(), PhotoActivity.class);

		intent.putStringArrayListExtra(PhotoActivity.IMAGE_URLS, mImageList);
		intent.putExtra(PhotoActivity.INDEX, position);
		startActivity(intent);

	}

	protected void go2Photo(UpFile upFile) {
		if (upFile == null)
			return;
		Intent intent = new Intent(getActivity(), PhotoActivity.class);

		if (isGone) {
			intent.putExtra(PhotoActivity.IMAGE_URL, upFile.Path);

		} else {
			if (TextUtils.isEmpty(upFile.absolutePath)) {
				intent.putExtra(PhotoActivity.IMAGE_URL, upFile.Path);
			} else {
				intent.putExtra(PhotoActivity.IMAGE_URL, "file://"
						+ upFile.absolutePath);
			}

		}
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:// 图片
				Uri2File(data.getData());

				break;
			case CAMERA_REQUEST_CODE:// 拍照
				if (isSdcardExisting()) {
					uploadFile(pickFile);

				} else {
					toastShow("未找到存储卡，无法存储照片！");
				}
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Uri->文件
	 * 
	 * @param uri
	 */
	private void Uri2File(Uri uri) {
		if (uri != null) {
			final String scheme = uri.getScheme();
			String data = null;
			if (scheme == null)
				data = uri.getPath();
			else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
				data = uri.getPath();
			} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
				Cursor cursor = getActivity().getContentResolver().query(uri,
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
			uploadFile(file);
		}

	}

	private void uploadFile(File filet) {
		if (SJQApp.user == null) {
			toastShow("您的信息获取失败，请重新登录");
			return;
		}
		final UpFile upFile = new UpFile(filet.getAbsolutePath());
		upFileAapter.addData(upFile);
		upFileAapter.notifyDataSetChanged();

		final File file = new File(filet.getAbsolutePath());
		final String url = ApiUrl.UPLOAD_FILE_ORDER;
		RequestParams params = getParams(url);

		params.addBodyParameter("file", file);

		params.addBodyParameter("UserID", SJQApp.user.id + "");
		params.addBodyParameter("LoginGuid", SJQApp.user.guid);

		params.addBodyParameter("FileType", FileType);
		params.addBodyParameter("OrderID", OrderID);
		params.addBodyParameter("OrderGUID", OrderGUID);

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onStart() {

			}

			@Override
			public void onSuccess(ResponseInfo<String> n) {
				if (upFileAapter == null)
					return;

				Log.i("上传订单文件", n.result);
				Bean data = CommonUtils.getDomain(n, Bean.class);
				if (data != null) {
					if (data.success) {

						upFile.GUID = CommonUtils.getString(n.result,
								"FileGuid");
						upFile.Path = CommonUtils.getString(n.result, "url");
						// toastShow(n.result);
					} else {
						upFile.Path = "-1";
						toastShow("上传失败");
					}
				}
				upFileAapter.notifyDataSetChanged();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);

				if (total <= 0)
					total = 1;
				int progress = (int) (current * 100 / (float) total);
				if (progress == 100) {
					progress = 98;
				} else if (progress > 100) {
					progress = 3;
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if (upFileAapter == null)
					return;
				upFile.Path = "-1";
				upFileAapter.notifyDataSetChanged();
				toastShow("无法连接到服务器，请稍后重试");

			}

		};

		new HttpUtils(1000 * 60 * 3).send(HttpMethod.POST, ApiUrl.API + url,
				params, callBack);

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

	public void setClickDelect() {
		upFileAapter.isShowDelete(!upFileAapter.isDelete());
		upFileAapter.notifyDataSetChanged();
	}

	boolean isGone;

	public void setAddGone(boolean isAddGone) {
		isGone = isAddGone;
	}

}
