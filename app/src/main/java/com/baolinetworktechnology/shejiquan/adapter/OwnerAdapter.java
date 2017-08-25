package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.utils.DesignerHonorUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

/**
 * 业主关注的- 设计师列表适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class OwnerAdapter extends BaseAdapter {

	private BitmapUtils mImageUtil;

	private List<DesignerItemInfo> designerInfos;

//	DesignerHonorUtils mHonorUtil;

	public OwnerAdapter(Context context) {
		this(context, null);
//		mHonorUtil = new DesignerHonorUtils();
	}

	public OwnerAdapter(Context context, List<DesignerItemInfo> designerInfos) {
		this.designerInfos = designerInfos;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
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
		// TODO Auto-generated method stub
		return designerInfos.get(position);
	}

	public DesignerItemInfo getDesignerInfo(int position) {
		if (designerInfos == null || position >= designerInfos.size())
			return null;
		return designerInfos.get(position);
	}

	public String getGuId(int position) {
		if (designerInfos == null || position >= designerInfos.size())
			return "";
		return designerInfos.get(position).guid;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return designerInfos.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = View.inflate(parent.getContext(),
					R.layout.item_designer, null);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		DesignerItemInfo data = designerInfos.get(position);
		holder.setData(data);
		if (convertView.getAlpha() != 1) {
			convertView.setAlpha(1);
			convertView.setScaleX(1);
			convertView.setScaleY(1);
			convertView.setTranslationX(0);
		}
		return convertView;
	}

	class ViewHolder {
		CircleImg designer_iv_head;
		TextView tvName, tvFromCity,tvNumOrder,tvNumComment,tvNumOPus,tvCost;
		ImageView ivHonor, ivGrade;
		View il;
		public ViewHolder(View convertView) {
			designer_iv_head = (CircleImg) convertView
					.findViewById(R.id.designer_iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvFromCity = (TextView) convertView.findViewById(R.id.tvFromCity);
			tvNumOPus= (TextView) convertView.findViewById(R.id.tvNumOPus);
			tvNumOrder= (TextView) convertView.findViewById(R.id.tvNumOrder);
			tvNumComment= (TextView) convertView.findViewById(R.id.tvNumComment);
			tvCost= (TextView) convertView.findViewById(R.id.tvCost);
			ivHonor = (ImageView) convertView.findViewById(R.id.ivHonor);
			ivGrade = (ImageView) convertView.findViewById(R.id.ivGrade);
			il =  convertView.findViewById(R.id.il);
			designer_iv_head.setTag("1");
		}

		public void setData(DesignerItemInfo data) {
			if (isPerson) {
				if (il.getVisibility() != View.GONE) {
					tvFromCity.setVisibility(View.GONE);
					ivHonor.setVisibility(View.GONE);
					ivGrade.setVisibility(View.GONE);
					il.setVisibility(View.GONE);
					tvCost.setVisibility(View.GONE);
				}
			} else {
				if (il.getVisibility() != View.VISIBLE) {
					tvFromCity.setVisibility(View.VISIBLE);
					ivHonor.setVisibility(View.VISIBLE);
					ivGrade.setVisibility(View.VISIBLE);
					il.setVisibility(View.VISIBLE);
					tvCost.setVisibility(View.VISIBLE);
				}
				tvFromCity.setText(data.cityName);
				tvNumOrder.setText("委托："+data.numOrder);
				tvNumOPus.setText("作品："+data.numCase);
				tvNumComment.setText("评价："+data.numComment);
				tvCost.setText(data.getCost());
//				mHonorUtil.setIcon(ivHonor, data.IsCertification, ivGrade,
//						data.getServiceStatus());
			}
			tvName.setText(data.name);

			if (!designer_iv_head.getTag().equals(data.logo)) {
				mImageUtil.display(designer_iv_head, data.logo);
				designer_iv_head.setTag(data.logo);
			}

		}

	}

	boolean isPerson = false;

	public void setIsPersoner(boolean isPerson) {
		this.isPerson = isPerson;

	}

	public void delete(int index) {
		if (designerInfos != null) {
			if (index >= 0 && index < designerInfos.size()) {
			
				designerInfos.remove(index);
			}
		}
	}

}
