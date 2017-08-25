package com.baolinetworktechnology.shejiquan.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.RecordDesigner;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

/**
 * 访客记录
 * 
 * @author JiSheng.Guo
 * 
 */
public class RecordAdapter extends BaseAdapter {

	List<RecordDesigner> mData;
	BitmapUtils mImageUtil;

	@Override
	public int getCount() {
		if (mData == null)
			return 0;
		return mData.size();
	}

	public RecordAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	public void addData(List<RecordDesigner> data) {
		if (data != null)
			this.mData.addAll(data);
		notifyDataSetChanged();

	}

	@Override
	public Object getItem(int position) {

		if (mData == null)
			return null;
		if (position >= mData.size()) {
			return null;
		}
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_record, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		RecordDesigner item = mData.get(position);
		
		
		if (vh.itemDate.getVisibility() != View.GONE)
			vh.itemDate.setVisibility(View.GONE);
		if (vh.designer_iv_head.getTag() != null
				|| vh.designer_iv_head.getTag() != item.VisitorLogo) {
			mImageUtil.display(vh.designer_iv_head, item.VisitorLogo);
			vh.designer_iv_head.setTag(item.VisitorLogo);
		}
		// 判断是否业主
		if (!item.isDesigner()) {// 业主
			//if (vh.ivNext.getVisibility() != View.GONE) {
				vh.ivNext.setVisibility(View.GONE);
				vh.tvName.setText(item.Visitor+"（业主）");
				vh.relayout.setBackgroundColor(0xA8FFFFFF);
			//}
		} else {
			//if (vh.ivNext.getVisibility() != View.VISIBLE) {
				vh.ivNext.setVisibility(View.VISIBLE);
				vh.relayout.setBackgroundResource(R.drawable.item_list);
				vh.tvName.setText(item.Visitor);
			//}
		}
		String[] strings = item.CreateTime.split(" ");
		if (position == 0) {

			if (strings.length > 1) {
				if (getNowDate().equals(strings[0])) {
					vh.setTitle("今天");
				} else {
					vh.setTitle(item.CreateTime);
				}
			}
		} else {

			RecordDesigner preItem = mData.get(position - 1);
			String[] preItems = preItem.CreateTime.split(" ");

			if (!preItems[0].equals(strings[0])) {

				vh.setTitle(strings[0]);
			}

		}
		if (strings.length > 0) {
			vh.tvDate.setText(strings[1]);
		} else {
			vh.tvDate.setText(item.CreateTime);
		}
//	
//		if (item.IsCertification == 0) {
//			vh.ivHonor.setVisibility(View.GONE);
//		} else {
//			vh.ivHonor.setVisibility(View.VISIBLE);
//		}
		return convertView;
	}

	String nowDate;//现在日期

	public String getNowDate() {
		if (TextUtils.isEmpty(nowDate)) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			nowDate = df.format(new Date());
		}

		return nowDate;
	}

	class ViewHolder {
		TextView tvName, tvDate, tvDateTitle;
		View itemDate;
		CircleImg designer_iv_head;
		View  ivNext, relayout;//ivHonor

		public ViewHolder(View convertView) {
			tvName = (TextView) convertView.findViewById(R.id.tvName);
			tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			tvDateTitle = (TextView) convertView.findViewById(R.id.tvDateTitle);
			itemDate = convertView.findViewById(R.id.itemDate);
			itemDate.setOnClickListener(null);
			designer_iv_head = (CircleImg) convertView
					.findViewById(R.id.designer_iv_head);
		//	ivHonor = convertView.findViewById(R.id.ivHonor);
			ivNext = convertView.findViewById(R.id.ivNext);
			relayout = convertView.findViewById(R.id.relayout);
		}

		void setTitle(String date) {
			tvDateTitle.setText(date);
			itemDate.setVisibility(View.VISIBLE);
			//String[] strings = date.split("-");
//			if (strings.length > 2) {
//				tvDateTitle.setText(date);//strings[1] + "-" + strings[2] + "-"
//			} else {
//				tvDateTitle.setText(date);
//			}
		}
	}

	public void setData(List<RecordDesigner> data2) {
		this.mData = data2;
		notifyDataSetChanged();

	}

}
