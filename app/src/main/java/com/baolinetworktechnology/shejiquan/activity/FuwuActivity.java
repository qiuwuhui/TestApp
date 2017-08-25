package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.manage.CityService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

public class FuwuActivity extends Activity implements OnClickListener{
	List<City> CityListqu=new ArrayList<City>();
	List<City> FuwuList=new ArrayList<City>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fuwu);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.tv_title2).setOnClickListener(this);
		SJQApp.ZhuanxiuData.getCityID();
		CityService mApplication=CityService.getInstance(this);
		if(SJQApp.ZhuanxiuData.getCityID()!=0){
			CityListqu=mApplication.getfuwuCity(SJQApp.ZhuanxiuData.getCityID());			
		}else{
			int CityID=getIntent().getIntExtra("CityID", 0);
			CityListqu=mApplication.getfuwuCity(CityID);	
		}
		ListView MyList=(ListView) findViewById(R.id.MyList);
		HistoryAdapter adapter=new HistoryAdapter();
		MyList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.tv_title2:
			Intent intent=new Intent();
			String Title="";
			String CityID="";
			for (int i = 0; i < FuwuList.size(); i++) {
				if(i+1==FuwuList.size()){
					Title+=FuwuList.get(i).Title;
				}else{
					Title+=FuwuList.get(i).Title+",";					
				}
			}
			for (int i = 0; i < FuwuList.size(); i++) {
				if(i+1==FuwuList.size()){
					CityID+=FuwuList.get(i).CityID+"";	
				}else{
					CityID+=FuwuList.get(i).CityID+",";					
				}
			}			
			intent.putExtra(AppTag.TAG_TEXT, Title);
			intent.putExtra("AreaIds", CityID);
			setResult(RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
		
	}
	public class HistoryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (CityListqu == null) {
				return 0;
			}
			return CityListqu.size();
		}

		public void setData(List<City> data){
			if(data==null)
				return;
			CityListqu.clear();
			CityListqu.addAll(data);
			if(CityListqu!=null){
				notifyDataSetChanged();				
			}
		}
		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= CityListqu.size())
				return null;
			return CityListqu.get(position);
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
						R.layout.fuwu_layout, null);
				viewHolder = new ViewHolder();
				convertView.setTag(viewHolder);
				viewHolder.checkbox=(CheckBox) convertView.findViewById(R.id.checkbox);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.checkbox.setText(CityListqu.get(position).Title);
			final int post=position;
			viewHolder.checkbox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {					
					City city=CityListqu.get(post);
					for (int i = 0; i < FuwuList.size(); i++) {
						if(city.CityID==FuwuList.get(i).CityID){
							FuwuList.remove(i);
							return;
						}
					}
					FuwuList.add(city);						
				}
			});
			return convertView;
		}
	}
	class ViewHolder {
		CheckBox checkbox;
	}
}
