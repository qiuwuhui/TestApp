package com.baolinetworktechnology.shejiquan.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.photo.PhotoView;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.FileUtil;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.VeDate;
import com.baolinetworktechnology.shejiquan.view.KoyViewPager;
import com.guojisheng.koyview.ActionSheetDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

/**
 * 图片查看显示Activity
 *
 * @author JiSheng.Guo
 *
 */
public class PhotoActivity extends BaseActivity {
	public static String IMAGE_URL = "imageUrl";// 单张图片Url
	public static String IMAGE_TITLES = "IMAGE_TITLES";// 多张图片标题
	public static String IMAGE_URLS = "IMAGE_URLS";// 多张图片Url
	public static String INDEX = "index";// 多张图片时 默认显示第几张

	private KoyViewPager mViewPager;
	private TextView mTvNumber, mTvTitle;// 张数，标题
	private List<String> mImageList;// 图片Url集合
	private ArrayList<String> mImageTitleList;// 图片标题集合
	private String mImageUrl = null;// 单张图Url
	boolean isInitData = false;
	BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.scale_big, R.anim.alpha1);
		setContentView(R.layout.activity_photo);
		initImageLoader();
		initView();
		initData();
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
	protected void onResume() {
		super.onResume();
		if (!isInitData) {
//			initData();
			isInitData = true;
		}
	}

	private void initData() {
		mImageList = getIntent().getStringArrayListExtra(IMAGE_URLS);
		mImageUrl = getIntent().getStringExtra(IMAGE_URL);
		mImageTitleList = getIntent().getStringArrayListExtra(IMAGE_TITLES);
		if (mImageList == null || mImageList.size() == 0) {
			if (mImageUrl != null) {

				mImageList = new ArrayList<String>();
				mImageList.add(mImageUrl);
				LogUtils.i("photo url", mImageUrl);
			} else {
				toastShow("您查看的资源不存在");

				return;
			}
		}

		if (mImageTitleList != null && mImageTitleList.size() > 0) {

			mTvTitle.setText(mImageTitleList.get(0));

		}

		mTvNumber.setText(1 + "/" + mImageList.size());
		if (mImageList.size() == 1) {
			mTvNumber.setVisibility(View.INVISIBLE);
		    mTvNumber.setVisibility(View.INVISIBLE);
		}

		int index = getIntent().getIntExtra(INDEX, 0);
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(index);
	}
	private void initData1() {
		mImageList = getIntent().getStringArrayListExtra(IMAGE_URLS);
		mImageUrl = getIntent().getStringExtra(IMAGE_URL);
		mImageTitleList = getIntent().getStringArrayListExtra(IMAGE_TITLES);
		if (mImageList == null || mImageList.size() == 0) {
			if (mImageUrl != null) {

				mImageList = new ArrayList<String>();
				mImageList.add(mImageUrl);
				LogUtils.i("photo url", mImageUrl);
			} else {
				toastShow("您查看的资源不存在");

				return;
			}
		}

		if (mImageTitleList != null && mImageTitleList.size() > 0) {

			mTvTitle.setText(mImageTitleList.get(0));

		}

		mTvNumber.setText(1 + "/" + mImageList.size());
		if (mImageList.size() == 1) {
			mTvNumber.setVisibility(View.INVISIBLE);
			((View) mTvNumber.getParent()).setVisibility(View.INVISIBLE);
		}

		int index = getIntent().getIntExtra(INDEX, 0);
		mViewPager.setAdapter(new SamplePagerAdapter1());
		mViewPager.setCurrentItem(index);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
       case R.id.back:
			finish();
		    break;
		case R.id.share:
			new SaveFileTask().execute(1);
			break;
		case R.id.save:
			requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
			break;
		default:
			break;
		}
	}

	private void initView() {
		mViewPager = (KoyViewPager) findViewById(R.id.kvp);
		mTvTitle = (TextView) findViewById(R.id.title);
		mTvNumber = (TextView) findViewById(R.id.tv);
		findViewById(R.id.share).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				if (mImageTitleList != null) {
					if (arg0 < mImageTitleList.size()) {
						mTvTitle.setText(mImageTitleList.get(arg0));
					}
				}

				mTvNumber.setText((arg0 + 1) + "/" + mImageList.size());

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}
	String newPath="";
	/**
	 * 保存图片异步类
	 *
	 * @author JiSheng.Guo
	 *
	 */
	class SaveFileTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				int current = mViewPager.getCurrentItem();
				String uri = mImageList.get(current);
				String title = VeDate.getNowDate("yyyy-MM-dd-HH-mm-ss");
				if (mImageTitleList != null) {

					if (current < mImageTitleList.size()) {
						title = mImageTitleList.get(current);
					}
				}
				String newPath = Environment.getExternalStorageDirectory()
						+ "/SheJiQuan/image/" + title + ".png";
				File file = bitmapUtils.getBitmapFileFromDiskCache(uri);
				switch (params[0]) {
				case 0:
					if (file == null)
						return 1;
					File part = new File(Environment
							.getExternalStorageDirectory().getPath()
							+ "/SheJiQuan/Image/");
					if (!part.exists()) {
						part.mkdirs();
					}
					return FileUtil.CopySdcardFile(file.getPath(), newPath);

				default:
					if (file == null)
						return 2;
					Intent share = new Intent(Intent.ACTION_SEND);
					share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));// 此处一定要用Uri.fromFile(file),其中file为File类型，否则附件无法发送成功。
					share.setType("image/jpeg");
					startActivity(Intent.createChooser(share, "分享设计圈图片"));
					break;
				}

			} catch (Exception e) {
				return 1;
			}

			return 3;
		}

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				switch (result) {
				case 0:
					toastShow("已保存到："
							+ Environment.getExternalStorageDirectory()
							+ "/SheJiQuan/image 目录下");
					break;
				case 1:
					toastShow("保存失败");
					break;
				case 2:
					toastShow("分享失败");
					break;
				default:
					break;
				}
			}

		}
	}

	class SamplePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {

			return mImageList == null ? 0 : mImageList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(PhotoActivity.this,
					R.layout.item_view_photo, null);
			displayNetwordImage(mImageList.get(position), view);
			container.addView(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	class SamplePagerAdapter1 extends PagerAdapter {
		@Override
		public int getCount() {

			return mImageList == null ? 0 : mImageList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(PhotoActivity.this,
					R.layout.item_view_photoh, null);
			displayNetwordImage1(mImageList.get(position), view);
			container.addView(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	private void displayNetwordImage(String imageUrl, View view) {
		PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
		photoView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				updateHead();
				return false;
			}
		});
		final View progressWheel = view.findViewById(R.id.progressWheel);
		bitmapUtils.display(photoView, imageUrl,
				new BitmapLoadCallBack<PhotoView>() {

					@Override
					public void onLoadCompleted(PhotoView arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						if (mViewPager == null)
							return;
						progressWheel.setVisibility(View.GONE);

						arg0.setImageBitmap(setBitmaps(arg2));
					}

					@Override
					public void onLoadFailed(PhotoView arg0, String arg1,
							Drawable arg2) {
						if (mViewPager == null)
							return;
						progressWheel.setVisibility(View.GONE);
					}
				});

	}
	private void displayNetwordImage1(String imageUrl, View view) {
		PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
		photoView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				updateHead();
				return false;
			}
		});
		final View progressWheel = view.findViewById(R.id.progressWheel);
		bitmapUtils.display(photoView, imageUrl,
				new BitmapLoadCallBack<PhotoView>() {

					@Override
					public void onLoadCompleted(PhotoView arg0, String arg1,
												Bitmap arg2, BitmapDisplayConfig arg3,
												BitmapLoadFrom arg4) {
						if (mViewPager == null)
							return;
						progressWheel.setVisibility(View.GONE);
						arg0.setImageBitmap(arg2);
					}

					@Override
					public void onLoadFailed(PhotoView arg0, String arg1,
											 Drawable arg2) {
						if (mViewPager == null)
							return;
						progressWheel.setVisibility(View.GONE);
					}
				});

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

		bitmapUtils.configDefaultLoadingImage(R.drawable.default_icon);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.default_icon);

		long pretime = CacheUtils.getLongData(this, "ImageLoader");
		long currentt = System.currentTimeMillis();

		if (currentt - pretime > 60 * 1000 * 60 * 24 * 15) {
			// 15天清除一次缓存
			bitmapUtils.clearCache();
			CacheUtils.cacheLongData(this, "ImageLoader", currentt);
		}

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.alpha1, R.anim.scale_small);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mViewPager !=null){
			mViewPager.setAdapter(null);
		}
	}
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		int mCurrentOrientation = getResources().getConfiguration().orientation;
		if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {
			setContentView(R.layout.activity_photo);
			initImageLoader();
			initView();
			initData();

		}else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE){
			setContentView(R.layout.activity_photo1);
			initImageLoader();
			initView();
			initData1();
		}
	}
	public static Bitmap setBitmaps(Bitmap bitMap){
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		if(width > 4096 || height > 4096){
			int newWidth = 0;
			int newHeight = 0;
             if(width > 4096){
				  newWidth = 4096;
			 }else {
				  newWidth =width;
			 }
			if(height > 4096){
				 newHeight = 4096;
			}else {
				 newHeight =height;
			}
			// 计算缩放比例
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;
			// 取得想要缩放的matrix参数
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 得到新的图片
			bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
					true);
			bitMap =Bitmap.createBitmap(bitMap,0,0,newWidth,newHeight);
//        bitMap =Watermark(bitMap,255);
			return bitMap ;
		}else{
			return bitMap;
		}
	}
	@Override
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);
		switch (requestCode) {
			case 0x0002:
				new SaveFileTask().execute(0);
				break;
		}

	}
	private void testClick() {
		requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
	}
	// 修改头像
	private void updateHead() {
		new ActionSheetDialog(this)
				.builder()
				.setCancelable(true)
				.setCanceledOnTouchOutside(true)
				.addSheetItem("保存图片", ActionSheetDialog.SheetItemColor.Blue,
						new ActionSheetDialog.OnSheetItemClickListener() {

							@Override
							public void onClick(int which) {
								testClick();
							}
						}).show();
	}
}
