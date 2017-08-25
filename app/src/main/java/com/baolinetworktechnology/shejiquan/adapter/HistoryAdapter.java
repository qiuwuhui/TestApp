package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
/**
 * 搜索历史记录适配器
 * @author JiSheng.Guo
 *
 */
public class HistoryAdapter extends BaseAdapter {

	private List<String> mData;

	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	public void setData(List<String> data){
		this.mData=data;
		notifyDataSetChanged();
	}
	@Override
	public Object getItem(int position) {

		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_serch_hostry, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(mData.get(position));

		return convertView;
	}

	class ViewHolder {
		TextView title;

		public ViewHolder(View v) {
			title = (TextView) v.findViewById(R.id.tvTitle2);
			//title.setVisibility(View.VISIBLE);
			//v.findViewById(R.id.tvTitle).setVisibility(View.GONE);
		}
	}

}
