package com.baolinetworktechnology.shejiquan;

import org.json.JSONException;
import org.json.JSONObject;

import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.activity.StyleActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.CaseClassList;
import com.baolinetworktechnology.shejiquan.domain.DesignerTuei;
import com.baolinetworktechnology.shejiquan.domain.TJDesignerInfo;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
public class ProjectFragment extends BaseFragment implements OnClickListener{
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
	String APP_FIRST = "APP_FIRST";
	private ProjectAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(layout==null){
			layout = inflater.inflate(R.layout.fragment_project, container, false);
			dialog = new MyDialog(getActivity());
			Button sheji_btn=(Button) layout.findViewById(R.id.sheji_btn);
			sheji_btn.setOnClickListener(this);
			boolean isFirst = CacheUtils.getBooleanData(getActivity(), "APP_TIAO", false);
			if(isFirst){
				sheji_btn.setVisibility(View.VISIBLE);
			}else{
				sheji_btn.setVisibility(View.GONE);
			}
			TextView xiugai=(TextView) layout.findViewById(R.id.xiugai);
			xiugai.setOnClickListener(this);
			xuqiu = (TextView) layout.findViewById(R.id.xuqiu);
			myscrollView = (ScrollView) layout.findViewById(R.id.myscrollView1);
			mylView = (ListView) layout.findViewById(R.id.myKoylist);
			mylView.setFocusable(false);
			adapter = new ProjectAdapter(getActivity());
			mylView.setAdapter(adapter);
			mylView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					TJDesignerInfo item = adapter.getItem(position);

					if (item == null)
						return;
					Intent intent = new Intent(getActivity(),
							WeiShopActivity.class);// WeiShopActivity WeiShopWebActivity2.1版
					intent.putExtra("IS_MY", false);
					intent.putExtra(AppTag.TAG_GUID, item.guid);
					intent.putExtra(AppTag.TAG_ID, item.id + "");
					intent.putExtra(AppTag.TAG_JSON, item.toString());
					getContext().startActivity(intent);
					
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
				AttrStyle+=StyleList.List.get(i).id+",";
				if(StyleList.List.size()==1){					
					AttrStyleName+=StyleList.List.get(i).title;
				}else{
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
		}
		String Type=CacheUtils.getStringData(getActivity(),AppTag.TJ_Type,null);
		CaseClassList TypeList= CommonUtils.getDomain(Type,
		CaseClassList.class);
		if(TypeList!=null){
			for (int i = 0; i < TypeList.List.size(); i++) {
				ArrayAttrHouseType=TypeList.List.get(i).id+"";
				ArraTypeName=TypeList.List.get(i).title;
			}				
		}
		CityID=CacheUtils.getStringData(getActivity(),AppTag.TJ_CityID,"0");
		String CityName=CacheUtils.getStringData(getActivity(),AppTag.TJ_CityName,"未选择");
		Size=CacheUtils.getStringData(getActivity(),AppTag.TJ_SIZE,"0");
		if(Size.equals("")){
			Size="未填写";
		}else{
			Size=Size+"㎡";
		}
		xuqiu.setText(CityName+"，"+ArraTypeName+"，"+Size+"，"+AttrStyleName);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sheji_btn:
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
		String url = ApiUrl.RECOMMENDEDSINGNER+"AttrStyleIDS="+AttrStyle
				+"&CityID="+CityID+"&Size="+Size+"&Token="+Token+"&IsRefresh=true"; 
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
				DesignerTuei data=null;
				try {
					json = new JSONObject(responseInfo.result);
					String result1=json.getString("result");
					Gson gson = new Gson();
				    data = gson.fromJson(result1, DesignerTuei.class);
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
