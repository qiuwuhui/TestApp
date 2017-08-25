package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

/**
 * 修改设计师
 * 
 * @author JiSheng.Guo
 * 
 */
public class ChangeDesingAdapter extends BaseAdapter {

	private BitmapUtils mImageUtil;

	private List<DesignerInfo> designerInfos;
	CityService cityService;

	public ChangeDesingAdapter(Context context) {
		this(context, null);
		cityService = CityService.getInstance(context);
	}

	boolean isShow = true;

	public void isCheck(boolean isShow) {
		this.isShow = isShow;
	}

	public ChangeDesingAdapter(Context context, List<DesignerInfo> designerInfos) {
		this.designerInfos = designerInfos;
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	public BitmapUtils getBitmapUtils() {
		return mImageUtil;

	}

	public void setData(List<DesignerInfo> designerInfos) {
		if (this.designerInfos != null) {
			this.designerInfos.clear();
		}
		check = -1;
		this.designerInfos = designerInfos;
		notifyDataSetChanged();
	}

	public void addData(List<DesignerInfo> data) {
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
		if (designerInfos != null)
			return designerInfos.get(position);
		return null;
	}

	public DesignerInfo getDesignerInfo(int position) {
		if (designerInfos == null || position >= designerInfos.size())
			return null;
		return designerInfos.get(position);
	}

	public String getGuId(int position) {
		if (designerInfos == null || position >= designerInfos.size())
			return "";
		return designerInfos.get(position).GUID;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return designerInfos.get(position).ID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = View.inflate(parent.getContext(),
					R.layout.item_change_designer, null);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		DesignerInfo data = designerInfos.get(position);
		StringBuffer sb = new StringBuffer();
		holder.tvName.setText(data.Name);
		if (!isShow) {
			holder.check.setVisibility(View.GONE);
		}
		if (cityService.getCityDB() != null) {
			City city = cityService.getCityDB().getCityID(data.CityID + "");
			City pro = cityService.getCityDB().getCityID(data.ProvinceID + "");
			if (pro != null) {
				sb.append(pro.Title + "-");
			}
			if (city != null) {

				sb.append(city.Title);
			}
		}
		holder.tvFromCity.setText(sb.toString());
		holder.tvCost.setText("设计费用：" + data.Cost);
		if (data.IsCertification == 0) {

			holder.ivHonor.setImageResource(R.drawable.weishop_admin_unhonor);
		} else {
			holder.ivHonor.setImageResource(R.drawable.weishop_admin_honor);
		}
		mImageUtil.display(holder.designer_iv_head, data.Logo);
		if (check == position) {
			holder.check.setChecked(true);
		} else {
			holder.check.setChecked(false);
		}
		return convertView;
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

	}

	int check = -1;

	public int getCheckIndex(){
		return check;
	}
	public void setCheck(int position) {
		if (check == position) {
			check = -1;
		} else {
			check = position;
		}
		notifyDataSetChanged();
	}

}
