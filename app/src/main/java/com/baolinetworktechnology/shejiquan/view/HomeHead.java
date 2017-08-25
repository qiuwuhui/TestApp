package com.baolinetworktechnology.shejiquan.view;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetwkchnology.shejiquan.xiaoxi.ReceiveFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.DailyChartActivity;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.adapter.CommonAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewHolder;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.DesignerBean;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.domain.Items;
import com.baolinetworktechnology.shejiquan.domain.LunBoBean;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.TueiJianBean;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.utils.AppErrorLogUtil;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.LogUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 灵感ListView 头部
 * 
 * @author JiSheng.Guo
 * 
 */
public class HomeHead extends FrameLayout implements OnClickListener {
//	KoySliding mKoySliding;
	private MyGridView hzListView;
	private String NEWS_CLASS = "NEWS_CLASS";
//	private CommonAdapter<Items> hzAdapter;
	private HomeAdater hzAdapter;
	private List<Items> listData = new ArrayList<>();
	private BitmapUtils mImageUtil;
	private int postItem;
//	private int curDay;
	public HomeHead(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HomeHead(Context context) {
		super(context);
		initView();
	}

//	public KoySliding getKoySliding() {
//		return mKoySliding;
//	}

	boolean isMeasure = false;
//	private TextView tvNumber;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		if (mKoySliding != null && !isMeasure) {
//			int mh = (int) (getWidth() / 2.5);
//			if (mh > 0) {
//				isMeasure = true;
//				mKoySliding.getLayoutParams().height = mh;
//			}
//
//		}
	}
//	private TextView tv4;
	private void initView() {
		View view = View.inflate(getContext(), R.layout.home_head, null);

		mImageUtil = new BitmapUtils(getContext());
		//加载图片类
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
//		tv4= (TextView) view.findViewById(R.id.tv4);
//		int day = CacheUtils.getIntData(getContext(),"shareSinaDay", -1);
//		curDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
//		if ((day == -1 || day != curDay)) {
//			Drawable drawable= getResources().getDrawable(R.drawable.ri_qian_true);
//			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//			tv4.setCompoundDrawables(null,drawable,null,null);
//		}else{
//			Drawable drawable= getResources().getDrawable(R.drawable.ri_qian_fash);
//			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//			tv4.setCompoundDrawables(null,drawable,null,null);
//		}
//		mKoySliding = (KoySliding) view.findViewById(R.id.koySliding);
		hzListView = (MyGridView) view.findViewById(R.id.hzListView);
//		tvNumber = (TextView) view.findViewById(R.id.tvNumber);
//		view.findViewById(R.id.tv1).setOnClickListener(this);
//		view.findViewById(R.id.tv2).setOnClickListener(this);
//		view.findViewById(R.id.tv3).setOnClickListener(this);
//		tv4.setOnClickListener(this);
		view.findViewById(R.id.she_ji_quan).setOnClickListener(this);
		hzAdapter = new HomeAdater();
//		hzAdapter = new CommonAdapter<Items>(getContext(),
//				R.layout.item_lunbo_designer) {
//			@Override
//			public void convert(ViewHolder holder, Items item) {
//				holder.getView(R.id.guanzhu_tv).setVisibility(View.VISIBLE);
//				if(TextUtils.isEmpty(item.title)){
//					setImageUrl(holder.getView(R.id.ivIcon),"assets/home_xu_wei.png");
//					holder.setText(R.id.tvName, "欢迎加入");
//					holder.getView(R.id.guanzhu_tv).setVisibility(View.INVISIBLE);
//				}else{
//					setImageUrl(holder.getView(R.id.ivIcon), item.getSmallImages("_300_300."));
//					holder.setText(R.id.tvName, item.title);
//				}
//			}
//		};
//		hzAdapter.setDefaultLoadingImage(R.drawable.icon_morentxy);
		hzListView.setAdapter(hzAdapter);
		addView(view);
		hzListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Items item = hzAdapter.getItem(position);
				if(!TextUtils.isEmpty(item.title)){
					Intent intent = new Intent(getContext(),
							WeiShopActivity.class);
					intent.putExtra(AppTag.TAG_GUID, "");
					intent.putExtra(AppTag.TAG_ID, item.parameter);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getContext().startActivity(intent);
				}
			}
		});
//		// 缓存读取
//		showLunBo();
		// 加载轮播图数据
//		loadLunBo(10019);// test17 19
		DesignershowLunBo();
	}

	// 加载轮播图数据
	private void  loadLunBo(final int id) {
		String url = AppUrl.ADVERT + id;
//		System.out.println("-->>"+url);
//		if (id == 10015) {
//			url = ApiUrl.GetRecommendDesigner + 0;
//		}

		RequestParams params = new RequestParams();
		params.setHeader("Token", null);
		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		HttpUtils http = new HttpUtils();
		http.configDefaultHttpCacheExpiry(1000 * 5);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("-->>"+responseInfo.result);
					// 加载轮播图数据
					if (getContext() == null)
						return;
					CacheUtils.cacheStringData(getContext(), "LunBo",
							responseInfo.result);// 缓存数据
//					showLunBo();
				}
			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("-->>"+error.getExceptionCode());
				if (getContext() == null)
					return;
				AppErrorLogUtil.getErrorLog(getContext()).postError(
						error.getExceptionCode() + "", "GET", ApiUrl.ADVERT);
				Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT)
						.show();
			}
		};
		http.send(HttpMethod.GET, AppUrl.API + url, params, callBack);

	}
	// 加载推荐设计师数据
	private void  loadSJLunBo(final int cityID) {
		String url = "";
		if(SJQApp.user ==null){
		url = AppUrl.GETRECOMMEND +"cityID="+ cityID+"&pageSize=8";
		}else{
	    url = AppUrl.GETRECOMMEND +"cityID="+ cityID+"&pageSize=8"+"&userGuid="+SJQApp.user.getGuid();
		}
		RequestParams params = new RequestParams();
		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		HttpUtils http = new HttpUtils();
		http.configDefaultHttpCacheExpiry(1000 * 5);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("-->>"+responseInfo.result);
						if (getContext() == null)
							return;
						CacheUtils.cacheStringData(getContext(), "LunBoDesigner",
								responseInfo.result);// 缓存数据
						showDesigner();
				}
			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("-->>"+error.getExceptionCode());
				if (getContext() == null)
					return;
				AppErrorLogUtil.getErrorLog(getContext()).postError(
						error.getExceptionCode() + "", "GET", ApiUrl.ADVERT);
				Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT)
						.show();
			}
		};
		http.send(HttpMethod.GET, AppUrl.API + url, params, callBack);
	}
	// 显示设计师数据
	protected void showDesigner() {
		String cace = CacheUtils.getStringData(getContext(), "LunBoDesigner",
				"");
		JSONObject json;
		TueiJianBean dataList=null;
		try {
			json = new JSONObject(cace);
			String result1=json.getString("result");
			Gson gson = new Gson();
			dataList=gson.fromJson(result1, TueiJianBean.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (dataList != null && dataList.listData != null && dataList.listData.size() !=0) {
			List<Items> listData = dataList.listData;
			if(listData.size() < 8) {
				for (int i = 0; i < 8; i++) {
					if (listData.size() < 8) {
						Items items = new Items();
						items.setTitle("");
						items.setWatch(false);
						listData.add(items);
					}
				}
			}
			hzAdapter.setList(listData);
			hzAdapter.notifyDataSetChanged();
		} else {
			List<Items> listData = new ArrayList<>();
			if(listData.size() < 8) {
				for (int i = 0; i < 8; i++) {
					if (listData.size() < 8) {
						Items items = new Items();
						items.setTitle("");
						items.setWatch(false);
						listData.add(items);
					}
				}
			}
			hzAdapter.setList(listData);
			hzAdapter.notifyDataSetChanged();
//			loadSJLunBo(0);
		}

	}
//	// 显示轮播图数据
//	private void showLunBo() {
//		String cace = CacheUtils.getStringData(getContext(), "LunBo", "");
//		LunBoBean dataList = CommonUtils.getDomain(
//				cace, LunBoBean.class);
//		if (dataList != null && dataList.result != null) {
//			mKoySliding.setDatas(dataList);// 设置数据
//			mKoySliding.setAutoPlay();// 设置自动滚动
//		} else {
//			LogUtils.i("lunbo", cace);
//		}
//	}
   public void DesignershowLunBo(){
	   // 加载推荐设计师数据
	   if(!CommonUtils.hasNetWorkInfo(getContext())){
		   showDesigner();
	   }else{
		   City city = SJQApp.getCity();
		   CityService mApplication= CityService.getInstance(getContext());
		   if (city ==null) {
			   loadSJLunBo(0);
		   }else{
			   int TJCityID = mApplication.getTJCityID(city.Title);
			   loadSJLunBo(TJCityID);
		   }
	   }
   }
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.she_ji_quan:
			Intent intent = new Intent();
			intent.setAction("shejiquan");
			getContext().sendBroadcast(intent);
			break;
//		case R.id.tv1:
//			go2Fragment(MyFragmentActivity.EntrustFastFragment);
//			break;
//		case R.id.tv2:
//			go2Fragment(MyFragmentActivity.MAIN_DESIGN);
//			break;
//		case R.id.tv3:
//			go2Fragment(MyFragmentActivity.MAIN_ORDER);
//			break;
//			case R.id.tv4:
//				Drawable drawable= getResources().getDrawable(R.drawable.ri_qian_fash);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				tv4.setCompoundDrawables(null,drawable,null,null);
//				CacheUtils.cacheIntData(getContext(),"shareSinaDay", curDay);
//				MobclickAgent.onEvent(getContext(),"kControlHomeDayMark");//首页日签按钮点击数
//				Intent intent1 = new Intent(getContext(), DailyChartActivity.class);
//				getContext().startActivity(intent1);
//
//			break;
		default:
			break;
		}
	}
	private class HomeAdater extends BaseAdapter {

		@Override
		public int getCount() {
			return listData.size();
		}
		@Override
		public Items getItem(int position) {
			if (position < 0 || position >= listData.size())
				return null;
			return listData.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		public void setList(List<Items> list) {
			if(list==null){
				return;
			}
			listData.clear();
			listData.addAll(list);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder vh = null;
			if (convertView == null) {
				convertView = View.inflate(parent.getContext(),
						R.layout.item_lunbo_designer, null);
				vh = new Holder();
				vh.ivIcon = (CircleImg) convertView.findViewById(R.id.ivIcon);
				vh.tvName=(TextView) convertView.findViewById(R.id.tvName);
				vh.guanzhu_tv=(TextView) convertView.findViewById(R.id.guanzhu_tv);
				vh.xuwei_im = (ImageView) convertView.findViewById(R.id.xuwei_im);
				convertView.setTag(vh);
			} else {
				vh = (Holder) convertView.getTag();
			}
			Items items = listData.get(position);
			vh.guanzhu_tv.setVisibility(View.INVISIBLE);
			vh.xuwei_im.setVisibility(View.INVISIBLE);
			vh.ivIcon.setVisibility(View.INVISIBLE);
			vh.tvName.setVisibility(View.INVISIBLE);
			if(TextUtils.isEmpty(items.title)){
				vh.xuwei_im.setVisibility(View.VISIBLE);
				mImageUtil.display(vh.xuwei_im,"assets/home_xu_wei.png");
				vh.tvName.setText("");
			}else{
				vh.ivIcon.setVisibility(View.VISIBLE);
				vh.guanzhu_tv.setVisibility(View.VISIBLE);
				vh.tvName.setVisibility(View.VISIBLE);
				mImageUtil.display(vh.ivIcon,items.getSmallImages("_300_300."));
				vh.tvName.setText(items.title);
			}
			if(items.isWatch){
				vh.guanzhu_tv.setText("已关注");
				vh.guanzhu_tv.setTextColor(getResources().getColor(R.color.shouqi));
				vh.guanzhu_tv.setBackgroundResource(R.drawable.yuanjiao_layout19);
			}else{
				vh.guanzhu_tv.setText("+关注");
				vh.guanzhu_tv.setTextColor(getResources().getColor(R.color.app_color));
				vh.guanzhu_tv.setBackgroundResource(R.drawable.yuanjiao_layout18);
			}
//			if(TextUtils.isEmpty(items.title)){
//				mImageUtil.display(vh.ivIcon,"assets/home_xu_wei.png");
//				vh.tvName.setText("欢迎加入");
//			}else{
//				mImageUtil.display(vh.ivIcon,items.getSmallImages("_300_300."));
//				vh.tvName.setText(items.title);
//			}
			vh.guanzhu_tv.setTag(position);
			vh.guanzhu_tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(SJQApp.user ==null){
						go2Login();
					}else{
						postItem = (int) view.getTag();
						Items items = listData.get(postItem);
						if(items.isWatch){
							showCall();
						}else{
							SavePosts(postItem);
						}
					}
				}
			});
			return convertView;
		}

	}
	class Holder {
		TextView tvName,guanzhu_tv;
		ImageView xuwei_im;
		CircleImg ivIcon;
	}
	private void go2Fragment(int type) {
		Intent intent = new Intent(getContext(), MyFragmentActivity.class);
		intent.putExtra(MyFragmentActivity.TYPE, type);
		getContext().startActivity(intent);

	}
	private void go2Login() {
		Intent intent = new Intent(getContext(), LoginActivity.class);
		getContext().startActivity(intent);
	}
	//取消关注
	private void showCall() {
		new MyAlertDialog(getContext()).setTitle("是否取消关注设计师")
				.setBtnCancel("取消").setBtnOk("确定")
				.setBtnOnListener(new MyAlertDialog.DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						if (isOk)
							QuxiaoPosts(postItem);
					}
				}).show();

	}
	private void QuxiaoPosts(final int posiItem){
		String designerGudi = listData.get(posiItem).getDesignerGuid();
		String url = AppUrl.FAVORITE_ADD;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("classType","1");
			param.put("userGUID", SJQApp.user.guid);
			param.put("fromGUID", designerGudi);
			param.put("favoriteMark", "1");
			param.put("operate", "0");
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		httpUtil.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						toastShow("当前网络连接失败");
					}
					@Override
					public void onSuccess(ResponseInfo<String> n) {
						Gson gson = new Gson();
						SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
						if (bean != null) {
							if (bean.result.operatResult) {
								listData.get(posiItem).setWatch(false);
								hzAdapter.notifyDataSetChanged();
								Intent intent = new Intent();
								intent.setAction("addFriend");
								getContext().sendBroadcast(intent);
							}else{
								toastShow("取消关注失败");
							}
						}
					}
				});
	}
	//关注
	private void SavePosts(final int posiItem){
		String designerGudi = listData.get(posiItem).getDesignerGuid();
		if(SJQApp.user.guid.equals(designerGudi)){
			toastShow("自己不能关注自己");
			return;
		}
		String url = AppUrl.FAVORITE_ADD;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("classType","1");
			param.put("userGUID", SJQApp.user.guid);
			param.put("fromGUID", designerGudi);
			param.put("favoriteMark", "1");
			param.put("operate", "1");
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		httpUtil.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						toastShow("当前网络连接失败");
					}
					@Override
					public void onSuccess(ResponseInfo<String> n) {
						Gson gson = new Gson();
						SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
						if (bean != null) {
							if (bean.result.operatResult) {
								listData.get(posiItem).setWatch(true);
								hzAdapter.notifyDataSetChanged();
								Intent intent = new Intent();
								intent.setAction("addFriend");
								getContext().sendBroadcast(intent);
							}else{
								toastShow("关注失败");
							}
						}
					}
				});
	}
	private void toastShow(String str) {
		Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
	}
}
