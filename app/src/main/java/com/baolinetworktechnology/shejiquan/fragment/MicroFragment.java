package com.baolinetworktechnology.shejiquan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.WeiShopWebActivity;
import com.baolinetworktechnology.shejiquan.constant.AppTag;

/**
 * 别人的微名片
 * 
 * @author JiSheng.Guo
 * 
 */
public class MicroFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = View.inflate(getActivity(), R.layout.fragment_cards, null);

		if (getArguments() != null) {

			String json = getArguments().getString(AppTag.TAG_JSON, "");
			String userGuid = getArguments().getString("USER_GUID", null);
			String userID = getArguments().getString("USER_ID", null);
			Intent intent = new Intent(getActivity(), WeiShopWebActivity.class);
			intent.putExtra(AppTag.TAG_JSON, json);
			intent.putExtra(AppTag.TAG_GUID, userGuid);
			intent.putExtra(AppTag.TAG_ID, userID);
			startActivity(intent);
			getActivity().finish();

		}
		return view;
	}

}