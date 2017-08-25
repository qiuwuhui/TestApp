package com.baolinetworktechnology.shejiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.PostDetailsActivity;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.view.NineGridTestLayout;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 业主我的帖子列表适配器
 * @author JiSheng.Guo
 *
 */
public class OwnerPotAdapter extends  RecyclerView.Adapter<OwnerPotAdapter.ViewHolder> {
	private List<PostBean> mList =new ArrayList<>();
	protected LayoutInflater inflater;
	BitmapUtils mImageUtil;
	List<String> pathList;
	List<String> dapathList;
	private Context mContext;
	public OwnerPotAdapter(Context context) {
		mImageUtil = new BitmapUtils(context);
		mImageUtil.configDefaultLoadingImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.zixun_tu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		mImageUtil.configDefaultShowOriginal(false);
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	public void setList(List<PostBean> list) {
		if(list==null){
			return;
		}
		mList.clear();
		mList.addAll(list);
	}
	public void addList(List<PostBean> list) {
		if(list==null){
			return;
		}
		mList.addAll(list);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View convertView = inflater.inflate(R.layout.owner_post_layout_reci, parent, false);
		ViewHolder viewHolder = new ViewHolder(convertView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder( ViewHolder holder, int position) {
		final PostBean bean=mList.get(position);
		if(bean.getPostsItemInfoList().size()!=0){
			pathList = new ArrayList<>();
			dapathList =new ArrayList<>();
				for (int c = 0; c < bean.getPostsItemInfoList().size(); c++){
					pathList.add(bean.getPostsItemInfoList().get(c).getSmallImages("_300_300."));
					dapathList.add(bean.getPostsItemInfoList().get(c).getPath());
			}
			holder.imageLayout.setdaUrlList(dapathList);
			holder.imageLayout.setUrlList(pathList);
			holder.imageLayout.setSpacing(10);
		}
		holder.Post_name.setText(bean.getTitle());
		if(TextUtils.isEmpty(bean.getContents())){
			holder.contents.setText("分享图片");
		}else{
			holder.contents.setText(bean.getContents());
		}
		holder.chakan.setText(bean.getHits()+"");
		holder.pinluen.setText(bean.getNumComment()+"");
		holder.dianzan.setText(bean.getNumGood()+"");
		holder.tiem_post.setText(bean.getFromatDate());
		holder.itemLay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, PostDetailsActivity.class);
                intent.putExtra(AppTag.TAG_GUID,bean.getGuid());
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
	}
	public class ViewHolder extends RecyclerView.ViewHolder {
		NineGridTestLayout imageLayout;
		TextView Post_name, contents;
		TextView chakan,pinluen,dianzan,tiem_post;
		View itemLay;
		public ViewHolder(View itemView) {
			super(itemView);
			itemLay = itemView.findViewById(R.id.itemLay);
			imageLayout = (NineGridTestLayout) itemView.findViewById(R.id.layout_nine_grid);
			Post_name = (TextView) itemView.findViewById(R.id.Post_name);
			contents = (TextView) itemView.findViewById(R.id.contents);
			chakan = (TextView) itemView.findViewById(R.id.chakan);
			pinluen = (TextView) itemView.findViewById(R.id.pinluen);
			dianzan = (TextView) itemView.findViewById(R.id.dianzan);
			tiem_post = (TextView) itemView.findViewById(R.id.tiem_post);
		}
	}
	@Override
	public int getItemCount() {
		return getListSize(mList);
	}
	private int getListSize(List<PostBean> list) {
		if (list == null || list.size() == 0) {
			return 0;
		}
		return list.size();
	}
}
