package com.baolinetworktechnology.shejiquan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.lidroid.xutils.BitmapUtils;
/**
 * 风格选择适配器
 * @author JiSheng.Guo
 *
 */
public class StyleGvAdapter extends BaseAdapter {

	ArrayList<CaseClass> data = new ArrayList<CaseClass>();
	BitmapUtils mImageUtil;

	public StyleGvAdapter(Context context) {
		mImageUtil=new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.icon_morentxs);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxs);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	public void setData(List<CaseClass> data) {
		this.data.clear();
		this.data.addAll(data);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	public CaseClass getGv(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.item_gv_style, null);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.setData(data.get(position));
		return convertView;
	}

	class Holder {
		ImageView imageView, ivCheck;
		TextView title;

		public Holder(View view) {
			imageView = (ImageView) view.findViewById(R.id.item_iv_bg);
			title = (TextView) view.findViewById(R.id.item_tv_name);
			ivCheck = (ImageView) view.findViewById(R.id.item_iv_check);
			imageView.setTag("");

		}

		public void setData(CaseClass data) {
			if(!imageView.getTag().toString().equals( data.Images)){
				mImageUtil.display(imageView, data.Images);
				imageView.setTag(data.Images);
			}
			
		
			title.setText(data.title);
			if (data.isCheck) {

				if (ivCheck.getVisibility() != View.VISIBLE)
					ivCheck.setVisibility(View.VISIBLE);
			} else {

				if (ivCheck.getVisibility() != View.GONE)
					ivCheck.setVisibility(View.GONE);
			}

		}
	}



}
