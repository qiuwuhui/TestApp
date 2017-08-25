package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.MesageBean;

/**
 * 我的消息 适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class MesageSystemAdapter extends BaseAdapter {
	List<MesageBean> data = new ArrayList<MesageBean>();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public void setData(List<MesageBean> data1) {
		this.data.clear();
		if (data1 != null)
			data.addAll(data1);
	}

	public void addData(List<MesageBean> data1) {
		if (data1 != null)
			data.addAll(data1);
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_message, null);
			vh = new Holder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (Holder) convertView.getTag();
		}
		vh.setData(data.get(position));
		return convertView;
	}

	class Holder {
		TextView tvTitle, tvTime,tvConten;//tvWTNumer
		ImageView redTips;

		Holder(View view) {
			tvTitle = (TextView) view.findViewById(R.id.tvTitle);
			tvConten= (TextView) view.findViewById(R.id.tvConten);
			//tvWTNumer = (TextView) view.findViewById(R.id.tvWTNumer);
			tvTime = (TextView) view.findViewById(R.id.tvTime);
			redTips = (ImageView) view.findViewById(R.id.redTips);
			//ivLogo.setImageResource(R.drawable.mesage_weituo_2);
			//tvWTNumer.setVisibility(View.GONE);
		}

		public void setData(MesageBean mesageBean) {
			if ("0".equals(mesageBean.getIsRead())) {
				if (redTips.getVisibility() != View.VISIBLE)
					redTips.setVisibility(View.VISIBLE);

			} else {
				if (redTips.getVisibility() != View.GONE)
					redTips.setVisibility(View.GONE);
			}
			tvTitle.setText(mesageBean.getTitle());
			tvConten.setText(mesageBean.getContent());
			tvTime.setText(mesageBean.getCreateTime());

		}

	}

	public void remove(int position) {
		if (data != null) {
			if (position >= 0 && position < data.size()) {
				data.remove(position);
			}

		}

	}
}
