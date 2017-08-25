package com.baolinetworktechnology.shejiquan.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.CricleBean;
import com.baolinetworktechnology.shejiquan.domain.PostBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.domain.goodsList;
import com.baolinetworktechnology.shejiquan.domain.postcommentList;
import com.baolinetworktechnology.shejiquan.fragment.CricleFragment;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.baolinetworktechnology.shejiquan.view.NineGridTestLayout;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 朋友圈列表适配器
 * @author JiSheng.Guo
 *
 */
public class CricleListAdapter extends BaseAdapter {

	private List<CricleBean> mData =new ArrayList<>();
	Context mContext;
	BitmapUtils mImageUtil;
	private PopupWindow mPopupWindow;
	public  int postitemint;
	public CricleListAdapter(Context context) {
		this.mContext=context.getApplicationContext();
		mImageUtil = new BitmapUtils(context.getApplicationContext());
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
	}
	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	public void setData(List<CricleBean> data){
		if (this.mData != null) {
			this.mData.clear();
		}
		if (data!=null && data.size()!=0 ) {
			this.mData.addAll(data);
			notifyDataSetChanged();
		}
	}
	public void addData(List<CricleBean> data) {
		if (data != null)
			this.mData.addAll(data);
		notifyDataSetChanged();
	}
	@Override
	public CricleBean getItem(int position) {

		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(parent.getContext(),
					R.layout.circle_layout, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CricleBean bean=mData.get(position);
		mImageUtil.display(viewHolder.shequ_loge,bean.getUserInfo().logo);
		viewHolder.userName.setText(bean.getUserInfo().name);
		String Contents =bean.getContents().trim();
		if(TextUtils.isEmpty(Contents)){
			viewHolder.contents.setText("分享图片");
		}else{
			viewHolder.contents.setText(Contents);
		}
		List<String> pathList=new ArrayList<>();
		List<String> dapathList=new ArrayList<>();
		if(bean.getPostsItemInfoList().size()!=0){
			for (int c = 0; c < bean.getPostsItemInfoList().size(); c++){
				if(bean.getPostsItemInfoList().size() == 1){
					pathList.add(bean.getPostsItemInfoList().get(c).path);
				}else{
					pathList.add(bean.getPostsItemInfoList().get(c).getSmallImages("_300_300."));
				}
				dapathList.add(bean.getPostsItemInfoList().get(c).path);
			}
		}
		viewHolder.imageLayout.setUrlList(pathList);
		viewHolder.imageLayout.setdaUrlList(dapathList);
		viewHolder.imageLayout.setSpacing(10);
		viewHolder.tiem_post.setText(bean.getFromatDate());
		//加载点赞人名字
		List<goodsList> strs =bean.getGoods();
		String dzName ="";
		viewHolder.dianzan.setVisibility(View.GONE);
		viewHolder.dian_lay.setVisibility(View.GONE);
		if(strs != null && strs.size()!=0){
			viewHolder.dianzan.setVisibility(View.VISIBLE);
			viewHolder.dian_lay.setVisibility(View.VISIBLE);
			for (int i = 0; i < strs.size(); i++){
				if(i == strs.size()-1){
					dzName +=strs.get(i).getGoodUserName();
				}else{
					dzName +=strs.get(i).getGoodUserName()+"、";
				}
			}
		}
		viewHolder.dianzan.setText(dzName);
		//加载评论
		viewHolder.itemReplys.removeAllViews();
		viewHolder.itemReplys.setVisibility(View.GONE);
		List<postcommentList> commentlist= bean.getComment();
		if(commentlist !=null &&commentlist.size()!=0){
			viewHolder.itemReplys.setVisibility(View.VISIBLE);
			for (int i = 0; i < commentlist.size(); i++){
				final int num = i;
				final postcommentList comment= commentlist.get(i);
				TextView replytv = new TextView(mContext);
				replytv.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				replytv.setPadding(0, 5, 0, 5);
				replytv.setTextColor(mContext.getResources().getColor(R.color.text_normal6));
				replytv.setTextSize(13);
				replytv.setBackgroundResource(R.drawable.peng_you_text);
				String publishName =comment.getOwnerName();
				String publishName1 =comment.getReplayUserName();
				if(TextUtils.isEmpty(publishName1)){
					publishName= "<font color=\"#566a94\">"
							+publishName+"：" + "</font>";
					replytv.setText(Html.fromHtml(publishName + comment.getCommentContent()));
				}else {
					publishName= "<font color=\"#566a94\">"
							+publishName+"：" + "</font>";
					publishName1= "<font color=\"#566a94\">"
							+publishName1+"：" + "</font>";
					replytv.setText(Html.fromHtml(publishName+"回复  "+ publishName1+ comment.getCommentContent()));
				}
				viewHolder.itemReplys.addView(replytv);
				replytv.setTag(position);
				replytv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int posiItem = (int) v.getTag();
//							 发送 一个无序广播
						Intent intent = new Intent();
						intent.setAction("criclehui");
						intent.putExtra("position",posiItem);
						intent.putExtra("plpost",num);
						mContext.sendBroadcast(intent);
					}
				});
			}
		}
		viewHolder.zong_layou.setVisibility(View.GONE);
		if(commentlist !=null &&commentlist.size()!=0){
			viewHolder.zong_layou.setVisibility(View.VISIBLE);
		}
		if(strs != null && strs.size()!=0){
			viewHolder.zong_layou.setVisibility(View.VISIBLE);
		}
		final ViewHolder showHolder =viewHolder;
		viewHolder.chakan.setTag(position);
		viewHolder.chakan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				postitemint = (int) view.getTag();
				int zanWidth =showHolder.chakan.getWidth();
				int zanHeight = showHolder.chakan.getHeight();
				View popupView = LayoutInflater.from(mContext).inflate(R.layout.cricle_item_layout, null);
				    TextView jibao = (TextView) popupView.findViewById(R.id.zan_item);
			    	View pin_item =popupView.findViewById(R.id.pin_item);
					jibao.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							mPopupWindow.dismiss();
							SavePosts(postitemint);
						}
					});
				    pin_item.setOnClickListener(new View.OnClickListener() {
					  @Override
					  public void onClick(View view) {
						  mPopupWindow.dismiss();
//							 发送 一个无序广播
						  Intent intent = new Intent();
						  intent.setAction("criclePin");
						  intent.putExtra("position",postitemint);
						  mContext.sendBroadcast(intent);
					  }
				  });
				  if(mPopupWindow == null){
					  boolean focusable = true;
					  mPopupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, focusable);
					  mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				  }
				View anchor = showHolder.chakan; //指定PopupWindow在哪个控件下面显示
				int xoff = -zanWidth;//指定PopupWindow在x轴方向上的偏移量
				int yoff = -zanHeight;//指定PopupWindow在Y轴方向上的偏移量
				mPopupWindow.showAsDropDown(anchor, xoff, yoff);
			}

		});
		viewHolder.figureLay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Intent intent =null;
//				if(bean.getUserInfo().identity.equals("DESIGNER")){
//					intent = new Intent(mContext, WeiShopActivity.class);
//					intent.putExtra(AppTag.TAG_GUID, bean.getUserInfo().guid);
//					intent.putExtra(AppTag.TAG_ID,"");
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					mContext.startActivity(intent);
//				}else if(bean.getUserInfo().identity.equals("PERSONAL")){
//					intent = new Intent(mContext, OwnerShowActivity.class);
//					intent.putExtra(AppTag.TAG_GUID, bean.getUserInfo().guid);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					mContext.startActivity(intent);
//				}else if(bean.getUserInfo().identity.equals("ROBOT")){
//					intent = new Intent(mContext, OwnerShowActivity.class);
//					intent.putExtra(AppTag.TAG_GUID, bean.getUserInfo().guid);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					mContext.startActivity(intent);
//				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		NineGridTestLayout imageLayout;
		CircleImg shequ_loge;
		TextView userName,contents;
		TextView chakan,tiem_post;
		TextView dianzan,dian_lay;
		LinearLayout itemReplys,zong_layou;
		View figureLay;
		public ViewHolder(View v) {
			imageLayout = (NineGridTestLayout) v.findViewById(R.id.layout_nine_grid);
			shequ_loge = (CircleImg) v.findViewById(R.id.shequ_loge);
			userName = (TextView) v.findViewById(R.id.userName);
			contents = (TextView) v.findViewById(R.id.contents);
			chakan = (TextView) v.findViewById(R.id.chakan);
			dianzan = (TextView) v.findViewById(R.id.dianzan);
			dian_lay = (TextView) v.findViewById(R.id.dian_lay);
			itemReplys = (LinearLayout) v.findViewById(R.id.itemReplys);
			zong_layou = (LinearLayout) v.findViewById(R.id.zong_layou);
			tiem_post = (TextView) v.findViewById(R.id.tiem_post);
			figureLay = v.findViewById(R.id.figureLay);
		}
	}
	private void SavePosts(final int posiItem){
		String url = AppUrl.SAVEPOSTSGOOD;
		RequestParams params = new RequestParams();
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("classType","0");
			param.put("sendUserGuid", SJQApp.user.guid);
			param.put("userGUID", mData.get(posiItem).getUserInfo().getGuid());
			if (SJQApp.userData != null) {
				param.put("sendUserLogo", SJQApp.userData.getLogo());
				param.put("sendUserName", SJQApp.userData.getName());
			} else if (SJQApp.ownerData != null) {
				param.put("sendUserLogo", SJQApp.ownerData.getLogo());
				param.put("sendUserName", SJQApp.ownerData.getName());
			} else{
				param.put("sendUserLogo", SJQApp.user.logo);
				param.put("sendUserName", SJQApp.user.nickName);
			}
			param.put("markName", "2");
			param.put("sendUserIdentity",  SJQApp.user.markName);
			param.put("postsId",  mData.get(posiItem).getId());
			param.put("postsGuid",  mData.get(posiItem).getGuid());
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		httpUtil.send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						toastShow("当前网络连接失败");

					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {
						Gson gson = new Gson();
						SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
						if (bean != null) {
							if (bean.result.operatResult) {
								List<goodsList> strs =	mData.get(posiItem).getGoods();
								goodsList goodBena= new goodsList();
								if (SJQApp.userData != null) {
									goodBena.setGoodUserName(SJQApp.userData.getName());
									goodBena.setGoodUserMarkName("DESIGNER");
									goodBena.setGoodUserGuid(SJQApp.user.guid);
									strs.add(goodBena);
								} else if (SJQApp.ownerData != null) {
									goodBena.setGoodUserName(SJQApp.ownerData.getName());
									goodBena.setGoodUserMarkName("PERSONAL");
									goodBena.setGoodUserGuid(SJQApp.user.guid);
									strs.add(goodBena);
								} else{
									goodBena.setGoodUserName(SJQApp.user.nickName);
									goodBena.setGoodUserMarkName("PERSONAL");
									goodBena.setGoodUserGuid(SJQApp.user.guid);
									strs.add(goodBena);
								}
								mData.get(posiItem).setGoods(strs);
								notifyDataSetChanged();
							}
						}
				}
		});
	}
	public  void setCommtlist( List<postcommentList> postList,int position){
		mData.get(position).setComment(postList);
		notifyDataSetChanged();
	}
	private void toastShow(String str) {
		Toast.makeText(mContext,str,Toast.LENGTH_SHORT).show();
	}
}
