package com.baolinetworktechnology.shejiquan.adapter;

import com.baolinetworktechnology.shejiquan.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class UpFileAapter extends CommonAdapter<UpFile> {

	boolean isDelete;

	public void isShowDelete(boolean isShow) {
		isDelete = isShow;
		notifyDataSetChanged();
	}

	public boolean isDelete() {

		return isDelete;
	}

	public UpFileAapter(Context context) {
		super(context, R.layout.item_up_file);
		UpFile up = new UpFile(R.drawable.add_image);
		mDatas.add(up);
		mImageUtil.configDefaultBitmapMaxSize(80, 80);
		mImageUtil.configDefaultShowOriginal(false);
	}

	@Override
	public void convert(ViewHolder holder, UpFile item) {
		if (isAddGone) {
			setImageUrl(holder.getView(R.id.ivPic), item.Path);
		} else {
			int position = holder.getPosition();
			if (position == 0) {
				holder.setImageResource(R.id.ivPic,R.drawable.add_image);
			} else {
				
				setImageUrl(holder.getView(R.id.ivPic), item.getAbsolutePath());
				TextView tvUp = holder.getView(R.id.tvUp);

				View ivDelete = holder.getView(R.id.ivDelete);
				if (isDelete) {
					ivDelete.setVisibility(View.VISIBLE);
				} else {
					ivDelete.setVisibility(View.GONE);
				}
				if (TextUtils.isEmpty(item.Path)) {
					tvUp.setText("上传中");
					tvUp.setVisibility(View.VISIBLE);
				} else {
					if ("-1".equals(item.Path)) {
						tvUp.setText("上传失败");
						tvUp.setVisibility(View.VISIBLE);
					} else {
						tvUp.setVisibility(View.GONE);
					}

				}
			}
		}
	}

	boolean isAddGone = false;

	public void setAddGone(boolean isGone) {
		isAddGone = isGone;
		if (isGone) {
			mDatas.remove(0);
			notifyDataSetChanged();
		}
	}
}
