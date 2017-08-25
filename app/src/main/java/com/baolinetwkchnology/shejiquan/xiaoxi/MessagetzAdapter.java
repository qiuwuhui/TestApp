package com.baolinetwkchnology.shejiquan.xiaoxi;

import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.domain.OrderLog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessagetzAdapter extends BaseAdapter{
	private List<OrderLog> messgaelist=new ArrayList<OrderLog>();
	
	public void setData(List<OrderLog> OrderLog){
		this.messgaelist.clear();
		if(OrderLog!=null){
			messgaelist.addAll(OrderLog);
		}
	}
	public void addData(List<OrderLog> OrderLog){
		if(OrderLog!=null){
			messgaelist.addAll(OrderLog);
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messgaelist.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < 0 || position >= messgaelist.size())
			return null;
		return messgaelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_message, null);
			vh = new Holder();
			vh.tvTime=(TextView) convertView.findViewById(R.id.tvTime);
			vh.tvTitle=(TextView) convertView.findViewById(R.id.tvTitle);
			vh.tvConten=(TextView) convertView.findViewById(R.id.tvConten);
			vh.redTips = convertView.findViewById(R.id.redTips);
			convertView.setTag(vh);
		} else {
			vh = (Holder) convertView.getTag();
		}
		OrderLog orderLog=messgaelist.get(position);
		vh.tvTime.setText(orderLog.getCreateTime());
		vh.tvTitle.setText(orderLog.getTitle());
		vh.tvConten.setText(orderLog.getContents());
		if ("0".equals(orderLog.getIsRead())) {
			if (vh.redTips.getVisibility() != View.VISIBLE)
				vh.redTips.setVisibility(View.VISIBLE);

		} else {
			if (vh.redTips.getVisibility() != View.INVISIBLE)
				vh.redTips.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	class Holder {
		TextView tvTitle, tvTime,tvConten;
		View redTips;
	}
}
