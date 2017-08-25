package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.News;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.utils.WindowsUtil;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 收藏资讯适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectNewsAdapter extends BaseAdapter {
	final int BIG_IMAGE = 0;// 有图
	final int NULL_IMAGE = 1;// 无图
	List<News> mData;
	BitmapUtils mImageUtil;
	MyDialog mDialog;
	int whith;

	public CollectNewsAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		mDialog = new MyDialog(context);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.default_icon);
		// mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		// 设置图片压缩类型
		whith = WindowsUtil.dip2px(context, 85);
	}

	public void setData(List<News> data) {
		if (this.mData != null) {
			this.mData.clear();
		}
		this.mData = data;
		notifyDataSetChanged();
	}

	public void addData(List<News> data) {
		if (data != null)
			this.mData.addAll(data);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mData == null)
			return 0;
		return mData.size();
	}

	@Override
	public int getItemViewType(int position) {
		switch (mData.get(position).TypeID) {
		case 0:
			return NULL_IMAGE;

		default:
			return BIG_IMAGE;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mData.get(position).ID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		Holder1 holder1 = null;
		Holder2 holder2 = null;

		News news = mData.get(position);
		if (convertView == null) {
			// 选择某一个样式。。
			switch (type) {
			case BIG_IMAGE:
				convertView = View.inflate(parent.getContext(),
						R.layout.item_collect_news, null);

				holder1 = new Holder1(convertView);
				holder1.setData(news);
				if (isChange) {
					holder1.cancel.setTag(news.GUID);
					holder1.cancel.setTag(R.id.tag_first, position);

					holder1.showDelete(convertView);
				} else {
					holder1.hide(convertView);
				}
				convertView.setTag(holder1);
				break;
			case NULL_IMAGE:
				convertView = View.inflate(parent.getContext(),
						R.layout.item_collect_news_null_image, null);

				holder2 = new Holder2(convertView);
				holder2.setData(news);

				if (isChange) {
					holder2.cancel.setTag(news.GUID);
					holder2.cancel.setTag(R.id.tag_first, position);
					holder2.showDelete(convertView);
				} else {
					holder2.hide(convertView);
				}
				convertView.setTag(holder2);
				break;
			default:
				break;
			}

		} else {

			switch (type) {
			case BIG_IMAGE:
				holder1 = (Holder1) convertView.getTag();
				holder1.setData(news);
				if (isChange) {
					holder1.showDelete(convertView);
					holder1.cancel.setTag(news.GUID);
					holder1.cancel.setTag(R.id.tag_first, position);
				} else {
					holder1.hide(convertView);
				}
				break;
			case NULL_IMAGE:

				holder2 = (Holder2) convertView.getTag();
				holder2.setData(news);
				if (isChange) {
					holder2.cancel.setTag(news.GUID);
					holder2.cancel.setTag(R.id.tag_first, position);
					holder2.showDelete(convertView);
				} else {
					holder2.hide(convertView);
				}
				break;

			default:
				break;
			}

		}

		if (convertView.getAlpha() != 1) {
			convertView.setAlpha(1);
			convertView.setScaleX(1);
			convertView.setScaleY(1);
			convertView.setTranslationX(0);

		}
		return convertView;
	}

	private void doCollect(final View v) {
		// mDialog.show();
		if (SJQApp.user != null) {
			String FromGUID = (String) v.getTag();
			String url = ApiUrl.FAVORITE_CANCEL;
			RequestParams params = getParams(url);
			// NEWS
			params.addBodyParameter("ClassType", "1");
			params.addBodyParameter("UserGUID", SJQApp.user.guid);
			params.addBodyParameter("FromGUID", FromGUID);
			// RequestCallBack<String> callBack = new RequestCallBack<String>()
			// {
			//
			// @Override
			// public void onSuccess(ResponseInfo<String> responseInfo) {
			// mDialog.dismiss();
			// Bean data2 = CommonUtils
			// .getDomain(responseInfo, Bean.class);
			// if (data2 != null) {
			// if (data2.success) {
			// mData.remove(position);
			// notifyDataSetChanged();
			// } else {
			// Toast.makeText(v.getContext(), "取消失败",
			// Toast.LENGTH_SHORT).show();
			// }
			// }
			// }
			//
			// @Override
			// public void onFailure(HttpException arg0, String arg1) {
			// mDialog.dismiss();
			// }
			// };
			getHttpUtils()
					.send(HttpMethod.POST, ApiUrl.API + url, params, null);
			final int position = (Integer) v.getTag(R.id.tag_first);
			mExplosionField.clear();
			mExplosionField.explode((View) v.getParent());
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					mDialog.dismiss();
					mData.remove(position);
					notifyDataSetChanged();
				}
			};
			Handler handler = new Handler();
			handler.postDelayed(runnable, 600);
		}

	}

	RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		if (SJQApp.user != null) {
			params.setHeader("Token", SJQApp.user.Token);
		} else {
			params.setHeader("Token", null);
		}

		params.setHeader("Version", "1.0");
		params.setHeader("AppAgent", "ANDROID_SHEJIQUAN_Ver.1.0");
		params.setHeader("Hash", MD5Util.getMD5String(url + ApiUrl.MD5));
		return params;

	}

	HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(6 * 1000);
		// httpUtil.configDefaultHttpCacheExpiry(1000 * 2);
		return httpUtil;

	}

	class Holder1 implements OnClickListener {
		TextView tv_title, tv_time, tv_class, cancel, tvText;
		ImageView iv_image;
		View liner_collect;

		Holder1(View view) {
			iv_image = (ImageView) view.findViewById(R.id.collect_iv_image);
			tvText = (TextView) view.findViewById(R.id.tvText);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_class = (TextView) view.findViewById(R.id.tv_class);
			cancel = (TextView) view.findViewById(R.id.cancel);
			liner_collect = view.findViewById(R.id.liner_collect);
			cancel.setOnClickListener(this);

		}

		public void setData(News news) {
			tvText.setText(news.Descriptions);
			tv_title.setText(news.Title);
			tv_time.setText(news.FromatDate);
			if ("DESIGNER".equals(news.MarkName)) {
				tv_class.setText("「 设计案例」");

			} else if ("CASE".equals(news.MarkName)) {
				tv_class.setText("「家居美图」");

			} else {
				tv_class.setText("「 设计案例」");
			}
			mImageUtil.display(iv_image, news.Images);

		}

		public void hide(View parent) {

			if (cancel.getVisibility() != View.GONE) {
				ObjectAnimator anim2 = ObjectAnimator//
						.ofFloat(liner_collect, "x", -whith, 0)//
						.setDuration(250);//
				anim2.start();
				cancel.setAnimation(AnimationUtils.loadAnimation(
						parent.getContext(), R.anim.trans_out));
				cancel.setVisibility(View.GONE);
			}

		}

		public void showDelete(View parent) {
			if (cancel.getVisibility() != View.VISIBLE) {
				cancel.setVisibility(View.VISIBLE);
				ObjectAnimator anim2 = ObjectAnimator//
						.ofFloat(liner_collect, "x", 0, -whith)//
						.setDuration(250);//
				anim2.start();
				cancel.setAnimation(AnimationUtils.loadAnimation(
						parent.getContext(), R.anim.trans_in));

			}

		}

		@Override
		public void onClick(View v) {
			doCollect(v);
		}

	}

	class Holder2 implements OnClickListener {
		TextView tv_title, tv_time, tv_class, tv_text, cancel;
		View liner_collect;

		Holder2(View view) {
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_class = (TextView) view.findViewById(R.id.tv_class);
			tv_text = (TextView) view.findViewById(R.id.tv_text);
			cancel = (TextView) view.findViewById(R.id.cancel);
			cancel.setOnClickListener(this);
			liner_collect = view.findViewById(R.id.liner_collect);
		}

		public void setData(News news) {
			tv_text.setText(news.Descriptions);
			tv_title.setText(news.Title);
			tv_time.setText(news.FromatDate);
			tv_class.setText(news.ClassTitle);

		}

		@Override
		public void onClick(View v) {
			doCollect(v);

		}

		public void hide(View parent) {

			if (cancel.getVisibility() != View.GONE) {
				ObjectAnimator anim2 = ObjectAnimator//
						.ofFloat(liner_collect, "x", -whith, 0)//
						.setDuration(250);//
				anim2.start();
				cancel.setAnimation(AnimationUtils.loadAnimation(
						parent.getContext(), R.anim.trans_out));
				cancel.setVisibility(View.GONE);
			}

		}

		public void showDelete(View parent) {
			if (cancel.getVisibility() != View.VISIBLE) {
				ObjectAnimator anim2 = ObjectAnimator//
						.ofFloat(liner_collect, "x", 0, -whith)//
						.setDuration(250);//
				anim2.start();
				cancel.setAnimation(AnimationUtils.loadAnimation(
						parent.getContext(), R.anim.trans_in));
				cancel.setVisibility(View.VISIBLE);
			}

		}
	}

	private boolean isChange = false;

	public void setChangData(boolean isChange) {
		this.isChange = isChange;
		notifyDataSetChanged();

	}

	ExplosionField mExplosionField;

	public void setChangData(boolean is, ExplosionField mExplosionField) {
		// TODO Auto-generated method stub
		this.isChange = is;
		this.mExplosionField = mExplosionField;
		notifyDataSetChanged();
	}
}
