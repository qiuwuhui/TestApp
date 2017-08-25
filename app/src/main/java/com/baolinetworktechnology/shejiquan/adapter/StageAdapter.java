package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.StageActivity;
import com.guojisheng.koyview.domain.ChannelItem;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class StageAdapter extends BaseAdapter{
	private ArrayList<ChannelItem> mUserChannelList = new ArrayList<ChannelItem>();
	Context mContext;
	int getlist;
	String channeName;
	public StageAdapter(Context context,int getlist) {
		this.mContext=context;
		this.getlist=getlist;
	}
	public void Stename(String name){
		channeName=name;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUserChannelList.size();
	}
	public void setData(List<ChannelItem> ChannelItem){
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
		final ChannelItem channe=mUserChannelList.get(position);
		vh.CheckView.setText(channe.getName());
		if(channeName.equals(channe.getName())){
			vh.CheckView.setChecked(true);
		}else{
			vh.CheckView.setChecked(false);
		}
		final int postbox=position;
		vh.CheckView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {				
			((StageActivity) mContext).SetChannelItem(postbox,channe.getId(),getlist);
				
			}
		});
		return convertView;
	}
	class Holder {
		CheckBox CheckView ;
	}
}
