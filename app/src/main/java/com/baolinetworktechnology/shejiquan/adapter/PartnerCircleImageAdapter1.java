package com.baolinetworktechnology.shejiquan.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.domain.DesignpictureMode;
import com.baolinetworktechnology.shejiquan.domain.OrderDesignerFileList;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 管理界面图片
 * 
 * @author Tony.An
 * 
 */
public class PartnerCircleImageAdapter1 extends BaseAdapter {
	private Context context;
	private OrderDesignerFileList list;
	private Bitmap temp;

	
	
	public PartnerCircleImageAdapter1(Context context, OrderDesignerFileList list) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	public void setItemList(OrderDesignerFileList list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.getList().size();
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
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_partner_circle_image_preview, null);
			holder.image = (ImageView) convertView.findViewById(R.id.item);
			holder.headline_tv=(TextView) convertView.findViewById(R.id.headline_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String imgurl = list.getList().get(position).Path;
		if(list.getList().get(position).Title.equals("")){
			holder.headline_tv.setText("");
		}else{
			holder.headline_tv.setText(list.getList().get(position).Title);			
		}
		BitmapUtils mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.display(holder.image, imgurl);
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		public TextView headline_tv;
	}

	private Bitmap downLoadImage(final String path) {
		try {
			File file = new File(path);
			// if (file.exists()) {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 1000;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;

			temp = BitmapFactory.decodeFile(path, o2);
			if (temp != null) {
				// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
				// 这里缩小了1/2,但图片过大时仍然会出现加载不了,但系统中一个BITMAP最大是在10M左右,我们可以根据BITMAP的大小
				// 根据当前的比例缩小,即如果当前是15M,那如果定缩小后是6M,那么SCALE= 15/6
				// Bitmap smallBitmap = zoomBitmap(temp, temp.getWidth() / 2,
				// temp.getHeight() / 2);
				// //释放原始图片占用的内存，防止out of memory异常发生
				// temp.recycle();
				int w = temp.getWidth();
				int h = temp.getHeight();
				if (w > h) {
					Matrix matrix = new Matrix();
					matrix.reset();
					matrix.setRotate(90);
					temp = Bitmap.createBitmap(temp, 0, 0, w, h, matrix, true);
				}
				// iv.setImageBitmap(temp);
				System.out.println("这个path已经有了----" + path);
				// pb.setVisibility(View.GONE);
				// iv.setVisibility(View.VISIBLE);
			}
			// }

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return temp;
	}
}
