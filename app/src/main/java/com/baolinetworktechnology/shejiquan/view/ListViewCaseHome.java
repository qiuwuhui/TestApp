package com.baolinetworktechnology.shejiquan.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.baolinetworktechnology.shejiquan.activity.WebDetailActivity;
import com.baolinetworktechnology.shejiquan.activity.WebOpusActivity;
import com.baolinetworktechnology.shejiquan.adapter.OpusHomenAdapter;
import com.baolinetworktechnology.shejiquan.adapter.OpusPublicAdapter;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Case;
import com.baolinetworktechnology.shejiquan.domain.SwCase;
import com.baolinetworktechnology.shejiquan.net.BaseNet.OnCallBackList;
import com.baolinetworktechnology.shejiquan.net.NetCaseSearch;
import com.baolinetworktechnology.shejiquan.view.RefreshListView.ILoadData;
import com.lidroid.xutils.exception.HttpException;

/**
 * 家装修
 * 
 * @author JiSheng.Guo
 * 
 */
public class ListViewCaseHome extends RefreshListView implements
		OnClickListener, OnCallBackList<SwCase>, ILoadData {
	private NetCaseSearch mNetCaseSearch;
	private String Tags = "0-0-0-0";
	int OrderBy = 0, ItemID = 0, ClassID = 6;
	private String MarkName = AppTag.MARKNAME_DESIGNER;
	private int PageIndex = 1;
	private OpusHomenAdapter mAdapter;
	private OpusPublicAdapter mPublicAdapter;
	public int CLASS_ID_PUBLIC = 6;
	public int CLASS_ID_HOME = 5;

	public ListViewCaseHome(Context context, AttributeSet attrs) {

		super(context, attrs);

		mNetCaseSearch = new NetCaseSearch();

		setOnLoadListener(this);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SwCase item = null;
				if (mAdapter != null) {
					item = mAdapter.getItem(position - 1);
				} else if (mPublicAdapter != null) {
					item = mPublicAdapter.getItem(position - 1);
				}
				if (item == null)
					return;
				Intent intent = new Intent(getContext(), WebOpusActivity.class);
				String url = ApiUrl.DETAIL_CASE2 + item.id+"&r="+System.currentTimeMillis();
				intent.putExtra("WEB_URL", url);
				intent.putExtra("classTitle", "");
				intent.putExtra(AppTag.TAG_ID, item.id);
				intent.putExtra(WebDetailActivity.GUID, item.guid);
				intent.putExtra(WebDetailActivity.ISCASE, true);
				intent.putExtra(AppTag.TAG_JSON, item.toString());
				getContext().startActivity(intent);
			}
		});

		initAdapter();
	}

	public void setData(String... strings) {
		mNetCaseSearch.SearchCase(this, strings);
	}

	public void setClassID(int classID) {
		ClassID = classID;

		initAdapter();

	}

	public void initAdapter() {
		if (getRefreshableView().getAdapter() == null) {
			if (ClassID == CLASS_ID_PUBLIC) {
				if (mPublicAdapter == null) {
					mPublicAdapter = new OpusPublicAdapter(getContext());
				}
				mAdapter = null;
				setAdapter(mPublicAdapter);
			} else {
				if (mAdapter == null) {
					mAdapter = new OpusHomenAdapter(getContext());
				}
				mPublicAdapter = null;
				setAdapter(mAdapter);
			}
		}
	}

	@Override
	public void loadData(boolean isRefresh) {
		String r = "r";
		if (isRefresh) {
			PageIndex = 1;
			r = System.currentTimeMillis() + "";
		} else {
			if (mNetCaseSearch.isLoading)
				return;
			PageIndex++;
		}

//		mNetCaseSearch.SearchCase(this, "ClassID", ClassID + "", "Tags", Tags,
//				"OrderBy", OrderBy + "", "ItemID", ItemID + "", "PageIndex",
//				PageIndex + "", "PageSize", "10", "MarkName", MarkName, "R", r);
		mNetCaseSearch.SearchCase(this, "ClassID", ClassID + "", "Tags", Tags,
				"OrderBy", OrderBy + "", "ItemID", ItemID + "", "PageIndex",
				PageIndex + "", "PageSize", "10", "MarkName", MarkName);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onNetStart() {

	}

	@Override
	public void onNetSuccess(List<SwCase> data) {
		setOnComplete();
		if (PageIndex == 1) {
			if (data == null || data.size() == 0) {
				setOnNullData("抱歉，没有找到您要的案例");

			}
			if (mAdapter != null)
				mAdapter.setData(data);
			if (mPublicAdapter != null)
				mPublicAdapter.setData(data);

		} else {
			if (data == null || data.size() == 0) {
				setOnNullNewsData();
			}
			if (mAdapter != null)
				mAdapter.addData(data);
			if (mPublicAdapter != null)
				mPublicAdapter.addData(data);

		}
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();

		if (mPublicAdapter != null)
			mPublicAdapter.notifyDataSetChanged();

	}

	@Override
	public void onNetFailure(String mesa) {
		setOnComplete();
		setOnFailure();
	}

	@Override
	public void onNetError(HttpException arg0, String mesa) {
		setOnComplete();
		setOnNullData("加载失败");
	}

	@Override
	public void onNetError(String json) {
		setOnComplete();
		setOnNullData("加载失败");
	}

	public void setTags(String tags2) {
		Tags = tags2;
	}

	public void setOrderBy(int id) {
		OrderBy = id;

	}
}
