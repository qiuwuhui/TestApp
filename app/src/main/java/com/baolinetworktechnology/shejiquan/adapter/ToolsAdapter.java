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
import com.baolinetworktechnology.shejiquan.domain.NewClass;
import com.lidroid.xutils.BitmapUtils;

/**
 * 工具界面适配器
 * 
 * @author JiSheng.Guo
 * 
 */
public class ToolsAdapter extends BaseAdapter {

	private BitmapUtils mImageUtil;
	List<NewClass> data = new ArrayList<NewClass>();

	public ToolsAdapter(Context context) {
		initBuitls(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public NewClass getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getValue();
	}

	@Override
	public View getView(int position, View rootView, ViewGroup arg2) {
		// 正常调用
		ViewHolder vh = null;
		if (rootView == null) {
			rootView = View.inflate(arg2.getContext(), R.layout.item_tools,
					null);
			vh = new ViewHolder(rootView);
			rootView.setTag(vh);
		} else {
			vh = (ViewHolder) rootView.getTag();
		}
	
		
		if (position == arg2.getChildCount()) {
			vh.setData(data.get(position), position);
			
		}

		return rootView;
	}

	class ViewHolder {
		TextView title;
		ImageView icon;

		public ViewHolder(View v) {
			//title = (TextView) v.findViewById(R.id.tvName);
			icon = (ImageView) v.findViewById(R.id.ivIcon);
		}

		public void setData(NewClass item, int position) {
			//title.setText(item.getName());
			switch (position) {
			case 0:
				setDefaultLoadingImage(R.drawable.ic_home_class_1);
				break;
			case 1:
				setDefaultLoadingImage(R.drawable.ic_home_class_2);
				break;
			case 2:
				setDefaultLoadingImage(R.drawable.ic_home_class_3);
				break;
			case 3:
				setDefaultLoadingImage(R.drawable.ic_home_class_4);
				break;
			default:
				break;
			}
			if (icon.getTag() == null || !icon.getTag().equals(item.getText())) {
				mImageUtil.display(icon, item.getText());
				icon.setTag(item.getText());
			}
		}
	}

	public void setDefaultLoadingImage(int icHomeClass1) {
		mImageUtil.configDefaultLoadingImage(icHomeClass1);
		mImageUtil.configDefaultLoadFailedImage(icHomeClass1);

	}

	private void initBuitls(Context mContext) {
		mImageUtil = new BitmapUtils(mContext);
		mImageUtil.configDefaultLoadingImage(R.drawable.default_icon);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.default_icon);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}

	public void setData(List<NewClass> data2) {
		if (data2 != null)
			data.addAll(data2);

	}
}
