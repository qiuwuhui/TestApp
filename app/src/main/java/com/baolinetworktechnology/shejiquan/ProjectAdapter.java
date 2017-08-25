package com.baolinetworktechnology.shejiquan;
import java.util.ArrayList;
import java.util.List;

import com.baolinetworktechnology.shejiquan.domain.CaseZhuanxiu;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.domain.DesignerBean;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.domain.DesignerTuei;
import com.baolinetworktechnology.shejiquan.domain.TJDesignerInfo;
import com.baolinetworktechnology.shejiquan.utils.DesignerHonorUtils;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//设计师推荐列表
public class ProjectAdapter extends BaseAdapter{
	private List<TJDesignerInfo> zhuanxiuList=new ArrayList<TJDesignerInfo>();
	BitmapUtils mImageUtil;
	BitmapUtils mImageUtil1;
	private DesignerHonorUtils honorUtils;
	private Context mContex;
	public ProjectAdapter(Context context) {
		this.mContex=context;
		honorUtils = new DesignerHonorUtils();
		
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.menu_icon_mrtx_default);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.menu_icon_mrtx_default);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//	
		
		mImageUtil1 = new BitmapUtils(context);
		mImageUtil1.configDefaultLoadingImage(R.drawable.icon_morentxs);
		mImageUtil1.configDefaultLoadFailedImage(R.drawable.icon_morentxs);
		mImageUtil1.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return zhuanxiuList.size();
	}
	public void setDate(DesignerTuei data) {
		if(data.listData==null)
			return;
		zhuanxiuList.clear();
		zhuanxiuList.addAll(data.listData);
		notifyDataSetChanged();
	}
	@Override
	public TJDesignerInfo getItem(int position) {
		if (position < 0 || position >= zhuanxiuList.size())
			return null;
		return zhuanxiuList.get(position);
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
					R.layout.item_designer, null);
			vh=new ViewHolder();
			vh.designer_iv_head=(CircleImg) convertView.findViewById(R.id.designer_iv_head);
			vh.tvName=(TextView) convertView.findViewById(R.id.tvName);
			vh.tvFromCity=(TextView) convertView.findViewById(R.id.tvFromCity);
			vh.tvCost=(TextView) convertView.findViewById(R.id.tvCost);
			vh.tvNumOrder=(TextView) convertView.findViewById(R.id.tvNumOrder);
			vh.ivHonor=(ImageView) convertView.findViewById(R.id.ivHonor);
			vh.ivGrade=(ImageView) convertView.findViewById(R.id.ivGrade);
			vh.iv1=(ImageView) convertView.findViewById(R.id.iv1);
			vh.iv2=(ImageView) convertView.findViewById(R.id.iv2);
			vh.iv3=(ImageView) convertView.findViewById(R.id.iv3);
			vh.iv_layout=convertView.findViewById(R.id.iv_layout);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		TJDesignerInfo item=zhuanxiuList.get(position);
		mImageUtil.display(vh.designer_iv_head, item.getSmallImages("_80_80."));
		vh.tvName.setText(item.name);
		vh.tvFromCity.setText(item.cityName);
		vh.tvCost.setText(item.getCost());
		vh.tvNumOrder.setText("擅长风格："+item.getStrStyle(mContex));
		honorUtils.setIcon(vh.ivHonor, item.isCertification,vh.ivGrade, item.serviceLevel);
		vh.iv_layout.setVisibility(View.VISIBLE);
		if(item.caseList==null || item.caseList.size()==0){
			vh.iv_layout.setVisibility(View.GONE);
		}else if(item.caseList.size()==1){
			mImageUtil1.display(vh.iv1, item.caseList.get(0));
			vh.iv2.setVisibility(View.INVISIBLE);	
			vh.iv3.setVisibility(View.INVISIBLE);	
		}else if(item.caseList.size()==2){
			mImageUtil1.display(vh.iv1, item.caseList.get(0));
			mImageUtil1.display(vh.iv2, item.caseList.get(1));
			vh.iv3.setVisibility(View.INVISIBLE);	
		}else if(item.caseList.size()==3){
			mImageUtil1.display(vh.iv1, item.caseList.get(0));
			mImageUtil1.display(vh.iv2, item.caseList.get(1));
			mImageUtil1.display(vh.iv3, item.caseList.get(2));
		}
		return convertView;
	}
	
	class ViewHolder {
		CircleImg designer_iv_head;
		TextView tvName,tvFromCity,tvCost,tvNumOrder;
		ImageView ivHonor,ivGrade,iv1,iv2,iv3;
		View iv_layout;
	}
}
