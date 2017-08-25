package com.baolinetworktechnology.shejiquan.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.DailyChartActivity;
import com.baolinetworktechnology.shejiquan.activity.PhotoActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.adapter.CaseAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CasePageList;
import com.baolinetworktechnology.shejiquan.domain.DailyBean;
import com.baolinetworktechnology.shejiquan.domain.Dailylise;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.FileUtil;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.OrientedViewPager;
import com.baolinetworktechnology.shejiquan.view.ScrollGridLayoutManager;
import com.baolinetworktechnology.shejiquan.view.VerticalStackTransformer;
import com.baolinetworktechnology.shejiquan.view.WrapContentHeightViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 案例收藏
 * 
 * @author JiSheng.Guo
 * 
 */
public class DailyCharFragment extends BaseFragment implements View.OnClickListener {
	private List<Dailylise> mImageList=new ArrayList<Dailylise>();// 图片Url集合
	private List<View> ViewList=new ArrayList<View>();// View集合
	private BitmapUtils mImageUtil;
	private OrientedViewPager mViewPager;
	private static final String SAVE_PIC_PATH= Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)
			? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
	private static final String SAVE_REAL_PATH = SAVE_PIC_PATH+ "/good/savePic";//保存的确切位置
	private DailyPagerAdapter dailyPagerAdapter;
	private boolean isFor=true;
	private String isTime="";
	private ShareUtils mShareUtils;// 分享工具
	private View view;
	private MyDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view == null){
			view = View.inflate(getActivity(), R.layout.activity_daily_chart,
					null);
			if (mShareUtils == null) {
				mShareUtils = new ShareUtils(getActivity());
			}
			mImageUtil = new BitmapUtils(getActivity());
			//加载图片类
			mImageUtil.configDefaultLoadingImage(R.drawable.defualt_trans);
			mImageUtil.configDefaultLoadFailedImage(R.drawable.defualt_trans);
			mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
			dialog =new MyDialog(getActivity());
			String Daily= CacheUtils.getStringData(getActivity(), AppTag.DAILY,null);
			if(Daily!=null){
				DailyBean DailyList= CommonUtils.getDomain(Daily,DailyBean.class);
				mImageList.addAll(DailyList.listData);
			}else{

			}
			inview(view);
		}
		return view;
	}
	View progressWheel;
	View daily_lay;
	private void inview(View layuot) {
		ImageView back= (ImageView) layuot.findViewById(R.id.back);
		back.setOnClickListener(this);
		daily_lay =layuot.findViewById(R.id.daily_lay);
		daily_lay.setOnClickListener(this);
		boolean isFirst = CacheUtils.getBooleanData(getActivity(), "Daily", true);
		if (isFirst) {
			daily_lay.setVisibility(View.VISIBLE);
		}
		progressWheel = layuot.findViewById(R.id.progressWheel);
		layuot.findViewById(R.id.fenxiang_btn).setOnClickListener(this);
		mViewPager= (OrientedViewPager) layuot.findViewById(R.id.vpager);
		//设置viewpager的方向为竖直
		mViewPager.setOrientation(OrientedViewPager.Orientation.VERTICAL);
//        mViewPager.setOffscreenPageLimit(0);
		mViewPager.setPageTransformer(true, new VerticalStackTransformer(getActivity().getApplicationContext()));
		dailyPagerAdapter=new DailyPagerAdapter();
		mViewPager.setAdapter(dailyPagerAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				MobclickAgent.onEvent(getActivity(),"kControlDayMarkNext");//一期一会下一张按钮
				if (mImageList != null) {
					if(isFor){
						if (arg0+3==mImageList.size()) {
							String date=getDateStr(mImageList.get(mImageList.size()-1).showDate,1);
							if(!isTime.equals(date)){
								loadata(date);
							}
						}
					}
				}
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
	class DailyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mImageList == null ? 0 : mImageList.size();
		}
		public void setData( List<Dailylise> data){
			mImageList.addAll(data);
			notifyDataSetChanged();
		}
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view =View.inflate(getActivity(), R.layout.item_view_photo1, null);
			displayNetwordImage(mImageList.get(position).photoUrl, view);
			container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
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
		ImageView photoView = (ImageView) view.findViewById(R.id.photoView);
		photoView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				testClick();
			}
		});
		int current = mViewPager.getCurrentItem();
		mImageUtil.display(photoView, imageUrl,
				new BitmapLoadCallBack<ImageView>() {

					@Override
					public void onLoadCompleted(ImageView arg0, String arg1,
												Bitmap arg2, BitmapDisplayConfig arg3,
												BitmapLoadFrom arg4) {
						if (mViewPager == null)
							return;
						arg2= setBitmaps(arg2);
						arg0.setImageBitmap(arg2);
						progressWheel.setVisibility(View.GONE);
					}

					@Override
					public void onLoadFailed(ImageView arg0, String arg1,
											 Drawable arg2) {
						if (mViewPager == null)
							return;
						progressWheel.setVisibility(View.GONE);
					}
				});

	}
	public static Bitmap setBitmaps(Bitmap bitMap){
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		// 设置想要的大小
		int newWidth = 750;
		int newHeight = 1334;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
				true);
		bitMap =Bitmap.createBitmap(bitMap,0,0,750,1334);
//        bitMap =Watermark(bitMap,255);
		return bitMap ;
	}
	public static Bitmap Watermark(Bitmap src,int alpha) {
		if (src == null) {
			return null;
		}
		int w = 750;
		int h = 1203;
		Paint paint=new Paint();
		paint.setAlpha(alpha);
		paint.setAntiAlias(true);
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas cv = new Canvas(newb);
		cv.drawColor(Color.WHITE);
		cv.drawBitmap(src, 0, 0, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newb;
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.back:
				finish();
				break;
			case R.id.daily_lay:
				daily_lay.setVisibility(View.GONE);
				CacheUtils.cacheBooleanData(getActivity(), "Daily",
						false);
				break;
			case R.id.fenxiang_btn:
				new SaveFileTask().execute(0);
				MobclickAgent.onEvent(getActivity(),"kControlDayMarkShare");// 一期一会分享按钮
//				requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
				break;
		}
	}
	//Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
	public static String getDateStr(String day,int Num) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = null;
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//如果需要向后计算日期 -改为+
		Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}
	private void loadata(String date) {

		isTime=date;
		String url = AppUrl.ExpectCover+"beginTime="+date+"&pageSize=5&CurrentPage=1";
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				DailyBean bean=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
					bean=gson.fromJson(result1, DailyBean.class);
					if(bean!=null && bean.listData !=null){
						dailyPagerAdapter.setData(bean.listData);
					}else {
						isFor=false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}


		};
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("DailyChartActivity");
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("DailyChartActivity");
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
				String uri = mImageList.get(current).photoUrl;
				String title = "yiqiyihui";
//                String title = VeDate.getNowDate("yyyy-MM-dd-HH-mm-ss");
				newPath = Environment.getExternalStorageDirectory()
						+ "/SheJiQuan/image/" + title + ".png";
				File file = mImageUtil.getBitmapFileFromDiskCache(uri);
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
						Uri uri = Uri.fromFile(new File(newPath));
						Intent share = new Intent(Intent.ACTION_SEND);
						share.putExtra(Intent.EXTRA_STREAM, uri);// 此处一定要用Uri.fromFile(file),其中file为File类型，否则附件无法发送成功。
						share.setType("image/*");
						startActivity(Intent.createChooser(share, "分享设计圈图片"));
						break;
					default:
						break;
				}
			}

		}
	}
//	@Override
//	public void permissionSuccess(int requestCode) {
//		super.permissionSuccess(requestCode);
//		switch (requestCode) {
//			case 0x0002:
//				new SaveFileTask().execute(0);
//				break;
//		}
//
//	}
	public void testClick() {
		int current = mViewPager.getCurrentItem();
		String uri = mImageList.get(current).photoUrl;
		Intent intent = new Intent(getActivity(), PhotoActivity.class);
		intent.putExtra(PhotoActivity.IMAGE_URL, uri);
		getActivity().startActivity(intent);
	}
}
