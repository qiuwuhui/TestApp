package com.baolinetworktechnology.shejiquan;
import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.activity.StyleActivity;
import com.baolinetworktechnology.shejiquan.activity.WeizxActivity;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.Bean;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.domain.CaseZhuanxiu;
import com.baolinetworktechnology.shejiquan.domain.Casezx;
import com.baolinetworktechnology.shejiquan.domain.GongSiBean;
import com.baolinetworktechnology.shejiquan.fragment.BaseFragment;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class CompanyFragment extends BaseFragment implements OnClickListener{
	String APP_FIRST = "APP_FIRST";
	private View layout;
	private ListView mylView;
	private ScrollView myscrollView;
	String AttrStyle="0";
	String AttrStyleName="";
	String ArrayAttrHouseType="0";
	String ArraTypeName="";
	String CityID="0";
	String Size="0";
	private TextView xuqiu;
	private MyDialog dialog;
	private CompanyAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(layout==null){
			layout = inflater.inflate(R.layout.fragment_company, container, false);
			dialog = new MyDialog(getActivity());
			myscrollView = (ScrollView) layout.findViewById(R.id.myscrollView);
			Button sheji_but=(Button) layout.findViewById(R.id.sheji_but);
			sheji_but.setOnClickListener(this);
			boolean isFirst = CacheUtils.getBooleanData(getActivity(), "APP_TIAO", false);
			if(isFirst){
				sheji_but.setVisibility(View.VISIBLE);
			}else{
				sheji_but.setVisibility(View.GONE);
			}
			xuqiu = (TextView) layout.findViewById(R.id.xuqiu);
			TextView xiugai = (TextView) layout.findViewById(R.id.xiugai);
			xiugai.setOnClickListener(this);
			mylView = (ListView) layout.findViewById(R.id.myKoylist);
			mylView.setFocusable(false);
			adapter = new CompanyAdapter(getActivity());
			mylView.setAdapter(adapter);
			mylView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Casezx casezx=adapter.getItem(position);
					if (casezx == null)
						return;
					Intent intent = new Intent(getActivity(),WeizxActivity.class);
					intent.putExtra("businessID", casezx.getId()+"");
					intent.putExtra("GUID", casezx.getGUID()+"");
					getActivity().startActivity(intent);
				}
			});
			addCommy();
			loadata();
		}
		myscrollView.smoothScrollTo(0, 20);
		return layout;
	}

	private void addCommy() {
		String Style=CacheUtils.getStringData(getActivity(),AppTag.TJ_Style,null);
		CaseClassList StyleList= CommonUtils.getDomain(Style,
		CaseClassList.class);
		if(StyleList!=null){
			AttrStyle="";
			for (int i = 0; i < StyleList.List.size(); i++) {
				AttrStyle+=StyleList.List.get(i).title+",";
				if(StyleList.List.size()==2){
					if(i==0){
						AttrStyleName+=StyleList.List.get(i).title+"/";
					}else{
						AttrStyleName+=StyleList.List.get(i).title;
					}				
				}else{
					AttrStyleName+=StyleList.List.get(i).title;
				}
				
			}				
		}
		String Type=CacheUtils.getStringData(getActivity(),AppTag.TJ_Type,null);
		CaseClassList TypeList= CommonUtils.getDomain(Type,
		CaseClassList.class);
		if(TypeList!=null){
			for (int i = 0; i < TypeList.List.size(); i++) {
				ArrayAttrHouseType=TypeList.List.get(i).title+"";
				ArraTypeName=TypeList.List.get(i).title;
			}				
		}
		CityID=CacheUtils.getStringData(getActivity(),AppTag.TJ_CityID,"");
		String CityName=CacheUtils.getStringData(getActivity(),AppTag.TJ_CityName,"未选择");
		Size=CacheUtils.getStringData(getActivity(),AppTag.TJ_SIZE,"");
		if(Size.equals("")){
			xuqiu.setText(CityName+"，"+ArraTypeName+"，"+"未填写，"+AttrStyleName);
		}else{
			xuqiu.setText(CityName+"，"+ArraTypeName+"，"+Size+"㎡，"+AttrStyleName);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sheji_but:
			go2Splash();
			break;
        case R.id.xiugai:
        	CacheUtils.cacheBooleanData(getContext(), "APP_TIAO",
					false);
        	Intent intent = new Intent(getActivity(), StyleActivity.class);
			startActivity(intent);
//			boolean isFirst = CacheUtils.getBooleanData(getActivity(), "APP_TIAO", false);
//			if(!isFirst){
//				ExitAppliation.getInstance().exit();				
//			}
			break;

		default:
			break;
		}
		
	}
	private void loadata() {

		String Token=CacheUtils.getStringData(getActivity(), AppTag.APP_TOKEN, "");
		String url = ApiUrl.RECOMMENDEDBUSSINESS+"AttrStyle="+AttrStyle+"&AttrHouseType="
				+ArrayAttrHouseType+"&CityID="+CityID+"&Size="+Size+"&Token="+Token+"&IsRefresh=true"; 
		dialog.show();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
				dialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				JSONObject json;
				CaseZhuanxiu data=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
				    data = gson.fromJson(result1, CaseZhuanxiu.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (data!= null) {               
					adapter.setDate(data);   
				   }
				dialog.dismiss();
			}
		};
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				callBack);
		
	}
	// 跳转到启动页
	protected void go2Splash() {
	  Intent intent = new Intent(getActivity(), SkipActivity.class);
	  intent.putExtra(AppTag.TAG_ID, 1);
	  startActivity(intent);
	  getActivity().finish();
	}
}
