package com.baolinetwkchnology.shejiquan.xiaoxi;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.R.id;
import com.baolinetworktechnology.shejiquan.R.layout;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class WeiFragment extends Fragment {
	private View layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(layout==null){
		layout = inflater.inflate(R.layout.fragment_wei, container, false);
		Button btnOK=(Button) layout.findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				((SJQApp) getActivity().getApplication()).exitAccount();
				startActivity(new Intent(getActivity(),
						LoginActivity.class));				
			}
		});
		}
		return layout;
	}

}
