package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.OrderDesigner;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

/**
 * 修改设计师
 * 
 * @author JiSheng.Guo
 * 
 */
public class AssignDesingAdapter {

	private BitmapUtils mImageUtil;

	private List<OrderDesigner> designerInfos;
	CityService cityService;

	public AssignDesingAdapter(Context context) {
		this(context, null);
		cityService = CityService.getInstance(context);
	}

	public AssignDesingAdapter(Context context,
			List<OrderDesigner> designerInfos) {
		this.designerInfos = designerInfos;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	public BitmapUtils getBitmapUtils() {
		return mImageUtil;

	}

	public void setData(List<OrderDesigner> designerInfos) {
		if (this.designerInfos != null) {
			this.designerInfos.clear();
		}
		this.designerInfos = designerInfos;
	}

	public void addData(List<OrderDesigner> data) {
		if (data != null)
			this.designerInfos.addAll(data);

	}

	public int getCount() {
		if (designerInfos == null) {
			return 0;
		}
		return designerInfos.size();
	}

	public Object getItem(int position) {
		if (designerInfos != null)
			return designerInfos.get(position);
		return null;
	}

	public long getItemId(int position) {
		return designerInfos.get(position).getID();
	}

	public void getView(LinearLayout convertView, Context context) {
		for (int i = 0; i < designerInfos.size(); i++) {
			View child = View.inflate(context, R.layout.item_assign_designer,
					null);
			ViewHolder holder = new ViewHolder(child);
			OrderDesigner data = designerInfos.get(i);
			holder.setData(data);
			convertView.addView(child);
		}

	}

	class ViewHolder {
		CircleImg designer_iv_head;
		TextView tvName, tvCost, tvFromCity;
		ImageView ivHonor;
		CheckBox check;

		public ViewHolder(View convertView) {
			designer_iv_head = (CircleImg) convertView
					.findViewById(R.id.designer_iv_head);
			tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvCost = (TextView) convertView.findViewById(R.id.tvCost);
			tvFromCity = (TextView) convertView.findViewById(R.id.tvFromCity);
			ivHonor = (ImageView) convertView.findViewById(R.id.ivHonor);
			check = (CheckBox) convertView.findViewById(R.id.checkBox);
		}

		public void setData(OrderDesigner data) {
			StringBuffer sb = new StringBuffer();
			if (cityService.getCityDB() != null) {
				City city = cityService.getCityDB().getCityID(
						data.getDesignerCityId() + "");
				City pro = cityService.getCityDB().getCityID(
						data.getDesignerProvinceId() + "");
				if (pro != null) {
					sb.append(pro.Title + "-");
				}
				if (city != null) {

					sb.append(city.Title);
				}
			}
			tvName.setText(data.getDesignerName());
			tvFromCity.setText(sb.toString());
			tvCost.setText("设计费用：" + data.getDesignerCost());
			if (data.getIsCertification() == 0) {

				ivHonor.setImageResource(R.drawable.weishop_admin_unhonor);
			} else {
				ivHonor.setImageResource(R.drawable.weishop_admin_honor);
			}
			mImageUtil.display(designer_iv_head, data.getDesignerlogo());

		}
	}


}
