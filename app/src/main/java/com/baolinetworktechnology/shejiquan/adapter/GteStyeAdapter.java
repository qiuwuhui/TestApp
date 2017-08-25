package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;

import java.util.ArrayList;
import java.util.List;

public class GteStyeAdapter extends BaseAdapter{
	private ArrayList<CaseClass> mUserChannelList = new ArrayList<CaseClass>();
	Context mContext;
	List<CaseClass> addlist=new ArrayList<CaseClass>();
	public GteStyeAdapter(Context context) {
		this.mContext=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUserChannelList.size();
	}
	public void setData(List<CaseClass> ChannelItem){
		this.mUserChannelList.clear();
		if(ChannelItem!=null){
			mUserChannelList.addAll(ChannelItem);
		}
		notifyDataSetChanged();
	}
	@Override
	public Object getItem(int position) {
		if (position < 0 || position >= mUserChannelList.size())
			return null;
		return mUserChannelList.get(position);
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
					R.layout.stage_layout, null);
			vh = new Holder();
			vh.CheckView=(CheckBox) convertView.findViewById(R.id.CheckView);
			convertView.setTag(vh);
		} else {
			vh = (Holder) convertView.getTag();
		}
		final CaseClass channe=mUserChannelList.get(position);
		vh.CheckView.setText(channe.title);
		vh.CheckView.setChecked(false);
		for (int i = 0; i < addlist.size(); i++) {
			if(channe.id==addlist.get(i).id){
				vh.CheckView.setChecked(true);
			}
		}
		vh.CheckView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setPosition(channe);
			}
		});
		return convertView;
	}
	class Holder {
		CheckBox CheckView ;
	}
	public void setPosition(CaseClass caseclass) {
		for (int i = 0; i < addlist.size(); i++) {
			if (caseclass.id == addlist.get(i).id) {
				addlist.remove(i);
				notifyDataSetChanged();
				return;
			}
		}
		addlist.clear();
		addlist.add(caseclass);
		notifyDataSetChanged();
	}
	public List<CaseClass> getPosition() {
		if(addlist.size()==0){
			return null;
		}
		return	addlist;
	}
	public void coloce(){
		addlist.clear();
	}
}
