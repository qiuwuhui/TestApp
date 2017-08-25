package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.OpusPopuWindowAdapter.PopuWindowListener;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;

/**
 * 订单大厅-分类列表适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class ClassGvAdapter extends BaseAdapter {

	List<CaseClass> data = new ArrayList<CaseClass>();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		if (position < 0 || position >= data.size())
			return null;
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_gv_class, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.setData(position, data.get(position));
		return convertView;
	}

	class ViewHolder implements OnClickListener {
		CheckBox cbTile;
		int position, id;
		View line;

		public ViewHolder(View v) {
			cbTile = (CheckBox) v.findViewById(R.id.cbTile);
			cbTile.setOnClickListener(this);
			line = v.findViewById(R.id.line);
		}

		public void setData(int n, CaseClass item) {
			cbTile.setText(item.title);
//			cbTile.setChecked(false);
//			if(item.isCheck){
			cbTile.setChecked(item.isCheck);
//			}else{
//				cbTile.setChecked(false);
//			}
			position = n;
			id = item.id;
			if (NumColumn == 1) {
				if (line.getVisibility() != View.VISIBLE) {
					line.setVisibility(View.VISIBLE);
					cbTile.setBackgroundColor(0x00000000);
				}
			} else {
				if (line.getVisibility() != View.GONE) {
					line.setVisibility(View.GONE);
					cbTile.setBackgroundResource(R.drawable.selector_red_gree_radu);
				}
			}
		}

		@Override
		public void onClick(View v) {
			for (int i = 0; i < data.size(); i++) {
				data.get(i).isCheck = false;
				if (i == position) {
					data.get(i).isCheck = true;
				}
			}
			notifyDataSetChanged();
			if (cbTile != null) {
				mOnPopuWindowListener.onItemClickListener(id, position, cbTile
						.getText().toString());
			} else {
				mOnPopuWindowListener.onItemClickListener(id, position, "");

			}
		}

	}

	PopuWindowListener mOnPopuWindowListener;

	public void setonPopuWindowItemClickListener(
			PopuWindowListener popuWindowListener) {
		mOnPopuWindowListener = popuWindowListener;
	}

	public void setData(List<CaseClass> caseList) {

		if(data.size()==0){
			for (int i = 0; i < caseList.size(); i++) {
				    caseList.get(i).setCheck(false);
				if (i == 0) {
					caseList.get(i).setCheck(true);
				}
			}
		}
		if(caseList != null){
			int check=0;
			for (int i = 0; i < caseList.size(); i++) {
				if(caseList.get(i).isCheck()){
					check++;
				}
			}
			if(check == 0){
				caseList.get(0).setCheck(true);
			}
			if(check == 2){
				caseList.get(0).setCheck(false);
			}
		}
		data.clear();
		if (caseList != null) {
		data.addAll(caseList);
		}
		notifyDataSetChanged();
	}

	public void setItemClick(int position) {

	}

	public List<CaseClass> getData() {
		// TODO Auto-generated method stub
		return data;
	}

	int NumColumn;

	public void setNumColumns(int i) {
		NumColumn = i;

	}
}
