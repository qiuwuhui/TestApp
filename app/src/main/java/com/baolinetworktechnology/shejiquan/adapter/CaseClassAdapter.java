package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;

public class CaseClassAdapter extends BaseAdapter implements OnClickListener {

	List<CaseClass> mCaseList = new ArrayList<CaseClass>();
	public List<CaseClass> mCheckCaseList;

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
		// TODO Auto-generated method stub
		return mCaseList.get(position);
	}

	public List<CaseClass> getCheckCaseList() {
		// TODO Auto-generated method stub
		return mCheckCaseList;
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
					R.layout.item_case_class, null);
			if (check) {
				convertView.findViewById(R.id.checkBox).setVisibility(
						View.VISIBLE);
				convertView.setOnClickListener(this);
			}
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();

		}

		vh.tv_name.setText(mCaseList.get(position).title);
		if (check) {
			vh.checkBox.setChecked(mCaseList.get(position).isCheck);

			convertView.setId(position);
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		CheckBox checkBox;

		public ViewHolder(View convertView) {
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		}

	}

	boolean check = false;

	public void setCheck() {
		mCheckCaseList = new ArrayList<CaseClass>();
		check = true;
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {

		if (mCaseList.get(v.getId()).isCheck) {
			mCheckCaseList.remove(mCaseList.get(v.getId()));
			mCaseList.get(v.getId()).isCheck = false;
			notifyDataSetChanged();
		} else {

			if (mCheckCaseList.size() < 4) {
				mCaseList.get(v.getId()).isCheck = true;
				mCheckCaseList.add(mCaseList.get(v.getId()));
				notifyDataSetChanged();
			} else {
				Toast.makeText(v.getContext(), "最多只能选4个", Toast.LENGTH_SHORT)
						.show();
			}

		}

	}

}
