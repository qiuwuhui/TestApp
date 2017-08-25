package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.OrderLog;

/**
 * 我的消息 适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class MesageAdapter extends BaseAdapter {
	List<OrderLog> data = new ArrayList<OrderLog>();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public void setData(List<OrderLog> data1) {
		this.data.clear();
		if (data1 != null)
			data.addAll(data1);
	}

	public void addData(List<OrderLog> data1) {
		if (data1 != null)
			data.addAll(data1);
	}

	@Override
	public Object getItem(int position) {
		if (position < 0 || position >= data.size())
			return null;
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
		TextView tvTitle, tvTime;
		// ImageView ivLogo;
		View redTips;

		Holder(View view) {
			tvTitle = (TextView) view.findViewById(R.id.tvTitle);
			// tvWTNumer = (TextView) view.findViewById(R.id.tvWTNumer);
			tvTime = (TextView) view.findViewById(R.id.tvTime);
			// ivLogo = (ImageView) view.findViewById(R.id.ivLogo);
			redTips = view.findViewById(R.id.redTips);
			// if (isOwner) {
			// ivLogo.setImageResource(R.drawable.owner_message_bg);
			// }
		}

		public void setData(OrderLog mesageBean) {
			if ("0".equals(mesageBean.getIsRead())) {
				if (redTips.getVisibility() != View.VISIBLE)
					redTips.setVisibility(View.VISIBLE);

			} else {
				if (redTips.getVisibility() != View.GONE)
					redTips.setVisibility(View.GONE);
			}
		
			tvTitle.setText(mesageBean.getContents());
			tvTime.setText(mesageBean.getCreateTime());
			//tvWTNumer.setText("委托编号：" + mesageBean.getOrderNumber());
		}

	}

	boolean isOwner = false;

	/**
	 * 是否是业主
	 */
	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;

	}

	public void setOwner() {
		setOwner(true);

	}

	public void remove(int position) {
		if (data != null) {
			if (position >= 0 && position < data.size()) {
				data.remove(position);
			}

		}

	}
}
