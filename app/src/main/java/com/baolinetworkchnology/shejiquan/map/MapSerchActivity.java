package com.baolinetworkchnology.shejiquan.map;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.guojisheng.koyview.MyEditText;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MapSerchActivity extends Activity implements
         OnClickListener, InputtipsListener,TextWatcher {
	MyEditText mMyEtSerch;
	ListView mListView;
	List<Tip> MyTip=new ArrayList<Tip>();
	private HistoryAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_serch);
		findViewById(R.id.back).setOnClickListener(this);
		mMyEtSerch = (MyEditText) findViewById(R.id.myEditText);
		mMyEtSerch.addTextChangedListener(this);
		mListView = (ListView) findViewById(R.id.historyListView);
		adapter = new HistoryAdapter();
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent data = new Intent();
				String District=MyTip.get(position).getDistrict()+MyTip.get(position).getName();
				String Adcode=MyTip.get(position).getAdcode();
				data.putExtra("District", District);
				data.putExtra("Adcode", Adcode);
				setResult(AppTag.RESULT_OK, data);
				finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
    	if (!AMapUtil.IsEmptyOrNullString(newText)) {
		InputtipsQuery inputquery = new InputtipsQuery(newText, mMyEtSerch.getText().toString());
		Inputtips inputTips= new Inputtips(this, inputquery);
		inputTips.setInputtipsListener(MapSerchActivity.this);
		inputTips.requestInputtipsAsyn();
    	}else{
    		MyTip.clear();
    		adapter.notifyDataSetChanged();
    	}
		
	}
	@Override
	public void onGetInputtips(List<Tip> data, int rCode) {	
		if(rCode == 1000){
			adapter.setData(data);			
		}
	}
	public class HistoryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (MyTip == null) {
				return 0;
			}
			return MyTip.size();
		}

		public void setData(List<Tip> data){
			if(data==null)
				return;
			MyTip.clear();
			MyTip.addAll(data);
			if(MyTip!=null){
				notifyDataSetChanged();				
			}
		}
		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= MyTip.size())
				return null;
			return MyTip.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(parent.getContext(),
						R.layout.item_serch_hostry, null);
				viewHolder = new ViewHolder();
				viewHolder.title=(TextView) convertView.findViewById(R.id.tvTitle2);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.title.setText(MyTip.get(position).getDistrict()+MyTip.get(position).getName());
			return convertView;
		}
	}
	class ViewHolder {
		TextView title;
	}
}
