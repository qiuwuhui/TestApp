package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;

/**
 * 导航栏弹窗适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class OpusPopuWindowAdapter extends BaseAdapter implements
		OnClickListener {
	private PopuWindowListener mOnPopuWindowListener;
	private List<CaseClass> mCaseList = new ArrayList<CaseClass>();

	public interface PopuWindowListener {
		public void onItemClickListener(int position , int id, String string);

		public void onClosePopuWindow();
	}

	public void setonPopuWindowItemClickListener(
			PopuWindowListener onPopuWindowListener) {
		this.mOnPopuWindowListener = onPopuWindowListener;
	}

	@Override
	public int getCount() {
		if (mCaseList == null)
			return 0;
		return mCaseList.size();
	}

	public void setData(List<CaseClass> caseList) {
		this.mCaseList.clear();
		if (caseList != null)
			this.mCaseList.addAll(caseList);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return mCaseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_case_class, null);
			convertView.setOnClickListener(this);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();

		}

		vh.tv_name.setText(mCaseList.get(position).title);
		vh.position = position;
		vh.id = mCaseList.get(position).id;
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		CheckBox checkBox;
		int position = 0, id;

		public ViewHolder(View convertView) {
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		}

	}

	@Override
	public void onClick(View v) {
		ViewHolder vh = (ViewHolder) v.getTag();
		if (mOnPopuWindowListener != null) {
			mOnPopuWindowListener.onItemClickListener(vh.position, vh.id,
					vh.tv_name.getText().toString());
		}

	}

	public List<CaseClass> getData() {
		// TODO Auto-generated method stub
		return mCaseList;
	}
}
