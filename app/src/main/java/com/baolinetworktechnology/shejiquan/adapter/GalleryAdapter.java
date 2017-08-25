package com.baolinetworktechnology.shejiquan.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;

public class GalleryAdapter extends BaseAdapter {
	private	Context mContext;
	private List<CaseClass> mFilmList;

	public GalleryAdapter(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public int getCount() {

		if (mFilmList == null)
			return 0;
		return mFilmList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(parent.getContext(), R.layout.view_text,
					null);
			// convertView.setLayoutParams(new
			// Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));
			vh.title = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(vh);
			// convertView.setLayoutParams(new Gallery.LayoutParams(200, 250));

		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		CaseClass f = mFilmList.get(position);
		vh.title.setText(f.title);
		// 取余，让图片循环浏览

		// 实现动画效果
		Context c = parent.getContext();
		if (select == position) {

			vh.title.setTextColor(c.getResources().getColor(
					R.color.text_pressed));
			vh.title.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.text_pressed));
		} else {
			vh.title.setTextColor(mContext.getResources().getColor(
					R.color.item_text_normal2));
			vh.title.setTextSize(mContext.getResources().getDimensionPixelSize(R.dimen.text_normal));
		}
		// Animation animation = AnimationUtils.loadAnimation(mContext,
		// R.anim.my_scale_action);
		// vh.iv.startAnimation(animation); // 选中时，这是设置的比较大
		return convertView;
	}

	class ViewHolder {
		TextView title;
	}

	public void setData(List<CaseClass> classData) {
		this.mFilmList = classData;
		notifyDataSetChanged();

	}

	int select = -1;

	public void setSelectItem(int position) {
		// TODO Auto-generated method stub
		select = position;
		notifyDataSetChanged();
	}

}
