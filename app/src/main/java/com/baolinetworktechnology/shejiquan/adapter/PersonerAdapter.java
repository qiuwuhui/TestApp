package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.domain.DesignerItemInfo;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

/**
 * 设计师列表适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class PersonerAdapter extends BaseAdapter {

	private BitmapUtils mImageUtil;

	private List<DesignerItemInfo> designerInfos;

	public PersonerAdapter(Context context) {
		this(context, null);
	}

	public PersonerAdapter(Context context, List<DesignerItemInfo> designerInfos) {
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
		// TODO Auto-generated method stub
		return null;
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

			convertView = View.inflate(parent.getContext(), R.layout.item_fans,
					null);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		DesignerItemInfo data = designerInfos.get(position);
		holder.tvName.setText(data.name);
		if (isPerson) {
			if (holder.il.getVisibility() != View.GONE) {
				holder.tvProfession.setVisibility(View.GONE);
				holder.tvScore.setVisibility(View.GONE);
				holder.ratingBar.setVisibility(View.GONE);
				holder.tvCompany.setVisibility(View.GONE);
				holder.ivHonor.setVisibility(View.GONE);
				holder.il.setVisibility(View.GONE);
				holder.tvp.setVisibility(View.GONE);
			}
		} else {
			if (holder.il.getVisibility() != View.VISIBLE) {
				holder.il.setVisibility(View.VISIBLE);
				holder.tvProfession.setVisibility(View.VISIBLE);
				holder.tvScore.setVisibility(View.VISIBLE);
				holder.ratingBar.setVisibility(View.VISIBLE);
				holder.tvCompany.setVisibility(View.VISIBLE);
				holder.ivHonor.setVisibility(View.VISIBLE);
				holder.tvp.setVisibility(View.VISIBLE);

			}
//			holder.tvProfession.setText(data.getProfession());
//			holder.tvFromCity.setText(data.FromCity);
//			holder.tvNumCase.setText(data.numCase + "");
//			//holder.tvScore.setText(data.StarLevel + "");
//			holder.tvNumFans.setText(data.numFans + "");
//			holder.tvNumView.setText(data.NumView + "");
//			//holder.ratingBar.setRating(data.StarLevel);
//			holder.tvCompany.setText(data.WorkCompany);
			if (data.isCertification == 0) {
				holder.ivHonor.setVisibility(View.GONE);
			} else {
				holder.ivHonor.setVisibility(View.VISIBLE);
			}
		}
		mImageUtil.display(holder.designer_iv_head, data.logo);
		return convertView;
	}

	class ViewHolder {
		CircleImg designer_iv_head;
		TextView tvName, tvProfession, tvScore, tvNumCase, tvNumView,
				tvNumFans, tvFromCity, tvCompany;
		RatingBar ratingBar;
		View ivHonor, il,tvp;

		public ViewHolder(View convertView) {
			designer_iv_head = (CircleImg) convertView
					.findViewById(R.id.designer_iv_head);
			ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
			tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvProfession = (TextView) convertView
					.findViewById(R.id.tvProfession);
			tvScore = (TextView) convertView.findViewById(R.id.tvScore);
			tvNumCase = (TextView) convertView.findViewById(R.id.tvNumCase);
			tvNumView = (TextView) convertView.findViewById(R.id.tvNumView);
			tvNumFans = (TextView) convertView.findViewById(R.id.tvNumFans);
			tvFromCity = (TextView) convertView.findViewById(R.id.tvFromCity);
			ivHonor = convertView.findViewById(R.id.ivHonor);
			tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
			tvp= convertView.findViewById(R.id.tvp);
			il = convertView.findViewById(R.id.il);
		}

	}

	public void delete(int index) {
		if (designerInfos != null) {
			if (index >= 0 && index < designerInfos.size()) {
				designerInfos.remove(index);
			}
		}
	}

	boolean isPerson = false;

	public void setIsPersoner(boolean isPerson) {
		this.isPerson = isPerson;

	}

}
