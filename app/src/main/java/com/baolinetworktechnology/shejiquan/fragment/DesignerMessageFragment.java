package com.baolinetworktechnology.shejiquan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.adapter.CommentAdapter;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.Comment;
import com.baolinetworktechnology.shejiquan.domain.CommentBean;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.VeDate;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 设计师详情页-评论（废除）
 * 
 * @author JiSheng.Guo
 * 
 */
public class DesignerMessageFragment extends BaseFragment {
	private ListView listView;
	private CommentAdapter adapter;
	private TextView tv_comment_size;
	private EditText etComment;
	private int mSize;
	private int PageIndex = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_designer_message, null);
		tv_comment_size = (TextView) view.findViewById(R.id.tv_comment_size);
		view.findViewById(R.id.btn_submit).setOnClickListener(this);
		listView = (ListView) view.findViewById(R.id.listView);
		etComment = (EditText) view.findViewById(R.id.etComment);
		adapter = new CommentAdapter(getActivity());
		listView.setAdapter(adapter);
		loadData();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			doSubmit();
			break;

		default:
			break;
		}
	}

	private void doSubmit() {
		if (SJQApp.user == null) {
			toastShow("请先登录");
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			return;
		}
		String url = ApiUrl.COMMENT_SEND;
		RequestParams params = getParams(url);
		String Contents = etComment.getText().toString();

		if (Contents.length() < 3) {
			toastShow("您输入的内容太少了");
			return;

		}
		String FromGuid = getArguments().getString("GUID");
		params.addBodyParameter("Contents", Contents);
		// params.addBodyParameter("FromID", Contents);
		params.addBodyParameter("FromGuid", FromGuid);
		params.addBodyParameter("UserID", "" + SJQApp.user.id);
		params.addBodyParameter("UserName", SJQApp.user.nickName);
		params.addBodyParameter("Star", "5");
		params.addBodyParameter("Evaluate", Contents);
		params.addBodyParameter("ClassType", "5");

		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Bean data = CommonUtils.getDomain(arg0, Bean.class);
				if (data != null) {
					if (data.success) {
						mSize++;
						tv_comment_size.setText("所有评论（" + mSize + "）");
						String Contents = etComment.getText().toString();
						Comment comment = new Comment();
						comment.contents = Contents;
						comment.createTime = VeDate.getNowDate();
						comment.userName = SJQApp.user.nickName;
						adapter.addData(comment);
					}
					toastShow(data.message);
				}
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				toastShow("网络请求发生错误");

			}
		};
		getHttpUtils()
				.send(HttpMethod.POST, ApiUrl.API + url, params, callBack);

	}

	void loadData() {
		String UserGUID = getArguments().getString("GUID");
		String url = ApiUrl.COMMENT_DESIGNER_LIST + UserGUID + "&PageIndex="
				+ PageIndex + "&PageSize=10";
		RequestParams params = getParams(url);
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				CommentBean comments = CommonUtils.getDomain(arg0,
						CommentBean.class);
				if (comments != null && comments.listData != null) {
					mSize = comments.recordCount;
					tv_comment_size.setText("所有评论（" + comments.recordCount + "）");

//					adapter.setData(comments.data);

				}

			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, params, callBack);
	}

}
