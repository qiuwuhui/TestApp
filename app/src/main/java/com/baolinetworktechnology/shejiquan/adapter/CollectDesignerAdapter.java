package com.baolinetworktechnology.shejiquan.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.CollectsjFragment;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.RoundRecImageView;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.fragment.CollectCaseFragment;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.google.gson.Gson;
import com.guojisheng.koyview.ExplosionField;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设计师列表适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class CollectDesignerAdapter extends BaseAdapter {
	private ExplosionField mExplosionField;
	private BitmapUtils mImageUtil;
	private Context mContex;
	private List<DesignerItemInfo> designerInfos=new ArrayList<DesignerItemInfo>();

	public CollectDesignerAdapter(Context context) {
		this(context, null);
	}

	public CollectDesignerAdapter(Context context,
			List<DesignerItemInfo> designerInfos) {
		this.mContex =context;
		this.designerInfos = designerInfos;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	public BitmapUtils getBitmapUtils() {
		return mImageUtil;

	}

	public void setData(List<DesignerItemInfo> designerInfos) {
		if (this.designerInfos != null) {
			this.designerInfos.clear();
		}
		this.designerInfos = designerInfos;
		notifyDataSetChanged();
	}

	public void addData(List<DesignerItemInfo> data) {
		if (data != null)
			this.designerInfos.addAll(data);
		notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		if (designerInfos == null) {
			return 0;
		}
		return designerInfos.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < 0 || position >= designerInfos.size())
			return null;
		return designerInfos.get(position);
	}

	public DesignerItemInfo getDesignerInfo(int position) {
		if (designerInfos == null || position >= designerInfos.size())
			return null;
		return designerInfos.get(position);
	}

	public String getGuId(int position) {
		if (designerInfos == null || position >= designerInfos.size()
				|| position < 0)
			return "";
		return designerInfos.get(position).guid;
	}

	@Override
	public long getItemId(int position) {
		return designerInfos.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = View.inflate(parent.getContext(),
					R.layout.sw_item_designer, null);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		if (convertView.getAlpha() != 1) {
			convertView.setAlpha(1);
			convertView.setScaleX(1);
			convertView.setScaleY(1);
			convertView.setTranslationX(0);
			convertView.setEnabled(true);
		}

		DesignerItemInfo data = designerInfos.get(position);
		holder.setData(data);
		return convertView;
	}

	public void doCollect(final int position, final View view) {
		if (SJQApp.user != null) {
			DesignerItemInfo news = designerInfos.get(position);
			String url = AppUrl.FAVORITE_ADD;
			String FromGUID = news.guid;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("classType","1");
				param.put("userGUID", SJQApp.user.guid);
				param.put("fromGUID", FromGUID);
				param.put("favoriteMark", "1");
				param.put("operate", "0");
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(mContex,"取消关注设计师失败",Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> n) {
							Gson gson = new Gson();
							SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
							if (bean != null) {
								if (bean.result.operatResult) {
									mExplosionField.clear();
									mExplosionField.explode(view);
									Runnable runnable = new Runnable() {
										@Override
										public void run() {
											designerInfos.remove(position);
											notifyDataSetChanged();
											if(designerInfos.size()==0){
												CollectsjFragment.vienin();
											}
										}
									};
									Handler handler = new Handler();
									handler.postDelayed(runnable, 800);
									Toast.makeText(mContex,"取消关注设计师成功",Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(mContex,"取消关注设计师失败",Toast.LENGTH_SHORT).show();
								}
							}
						}
					});
	     	}

	}

	HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(6 * 1000);
		return httpUtil;

	}
	public void setChangData(boolean isChange) {

	}
	public void setChangData(boolean is, ExplosionField mExplosionField) {
		this.mExplosionField = mExplosionField;
	}
	class ViewHolder {
		CircleImg designer_iv_head;
		TextView tvName, tvFromCity,tvCost,tvNumOrder,tvdesigning;
		View iv_layout;
		public ViewHolder(View convertView) {
			designer_iv_head = (CircleImg) convertView
					.findViewById(R.id.designer_iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvFromCity = (TextView) convertView.findViewById(R.id.tvFromCity);
			tvCost = (TextView) convertView.findViewById(R.id.tvCost);
			tvNumOrder = (TextView) convertView.findViewById(R.id.tvNumOrder);
			tvdesigning = (TextView) convertView.findViewById(R.id.tvdesigning);
			iv_layout = convertView.findViewById(R.id.iv_layout);
		}
		public void setData(DesignerItemInfo data) {

			if (data != null) {
				mImageUtil.display(designer_iv_head, data.logo);
				tvName.setText(data.name);
				tvFromCity.setText(data.cityName);
				if(data.getCost().equals("面议")){
					tvCost.setText(data.getCost());
				}else if(data.getCost().contains("¥")){
					tvCost.setText(data.getCost());
				}else{
					tvCost.setText("¥"+data.getCost());
				}
				tvNumOrder.setText("擅长风格：" +data.getStrStyle(mContex));
				tvdesigning.setText(data.designing);
			}
		}
	}
}
