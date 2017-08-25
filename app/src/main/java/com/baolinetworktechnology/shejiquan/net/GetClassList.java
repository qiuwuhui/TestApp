package com.baolinetworktechnology.shejiquan.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.domain.AttributeListBean;
import com.baolinetworktechnology.shejiquan.domain.CaseClass;
import com.baolinetworktechnology.shejiquan.domain.ClassListBean;
import com.baolinetworktechnology.shejiquan.domain.SwClassListBean;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取分类字典
 * 
 * @author JiSheng.Guo
 * 
 */
public class GetClassList extends BaseNet {
	String KEY_TAG = "GetClassList";
	HashMap<String, List<CaseClass>> hashMap = new HashMap<String, List<CaseClass>>();
	Context context;

	/**
	 * 获取分类字典
	 * 
	 * @param context
	 */
	public GetClassList(Context context) {
		this.context = context;
		String cache = CacheUtils.getStringData(context, "CLASS_LIST", KEY_TAG,
				"");
		SwClassListBean mClassListBean = CommonUtils.getDomain(cache,
				SwClassListBean.class);
		if (mClassListBean != null && mClassListBean.listHashMap != null) {
			hashMap=mClassListBean.getListHashMap();
		} else {
			 loadData();
		}
		checkData();
	}

	private void checkData() {

		String url = ApiUrl.CASE_VERSION;
		getHttpUtils().send(HttpMethod.GET, ApiUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						loadData();
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {

						String version = CommonUtils
								.getString(n.result, "data");
						String older = CacheUtils.getStringData(context,
								"CASE_VERSION", "");
						if (TextUtils.isEmpty(version)) {
							version = "-1";
						}
						if (!version.equals(older)) {
							CacheUtils.cacheStringData(context, "CASE_VERSION",
									version + "");
							loadData();
						}

					}

				});

	}

	public List<CaseClass> getList(String key) {
		if (hashMap != null)
			return hashMap.get(key);
		return null;
	}

	private void loadData() {
		String url = AppUrl.CASE_CLASS_LIST;
		getHttpUtils().send(HttpMethod.GET, AppUrl.API + url, getParams(url),
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("-->>GetClassList onFailure" + msg);
					}

					@Override
					public void onSuccess(ResponseInfo<String> n) {

						ClassListBean mClassListBean = CommonUtils.getDomain(n,
								ClassListBean.class);
							ArrayList<CaseClass> kongjian=new ArrayList<CaseClass>();
						    ArrayList<CaseClass> fengge=new ArrayList<CaseClass>();
						    ArrayList<CaseClass> sjfenge=new ArrayList<CaseClass>();
						    ArrayList<CaseClass> huxing=new ArrayList<CaseClass>();
					    	ArrayList<CaseClass> mianji=new ArrayList<CaseClass>();
						    ArrayList<CaseClass> gediao=new ArrayList<CaseClass>();
						    ArrayList<CaseClass> yusuan=new ArrayList<CaseClass>();
						    ArrayList<CaseClass> shangchang=new ArrayList<CaseClass>();
						if (mClassListBean != null
								&& mClassListBean.result != null) {
							for (int i = 0; i < mClassListBean.result.size(); i++) {
								    CaseClass  caseclass=new CaseClass();
								  CaseClass  caseclass1=new CaseClass();

								if (mClassListBean.result.get(i).parentID == 2){
									//空间
									caseclass.setId(mClassListBean.result.get(i).getId());
									caseclass.setTitle(mClassListBean.result.get(i).getTitle());
									caseclass.setOrderNum(mClassListBean.result.get(i).getOrderNum());
									kongjian.add(caseclass);
								}else if(mClassListBean.result.get(i).parentID == 25){
									//风格
									caseclass.setId(mClassListBean.result.get(i).getId());
									caseclass.setTitle(mClassListBean.result.get(i).getTitle());
									caseclass.setOrderNum(mClassListBean.result.get(i).getOrderNum());

									caseclass1.setId(mClassListBean.result.get(i).getId());
									caseclass1.setTitle(mClassListBean.result.get(i).getTitle());
									caseclass1.setOrderNum(mClassListBean.result.get(i).getOrderNum());
									fengge.add(caseclass);
									sjfenge.add(caseclass1);
								}else if(mClassListBean.result.get(i).parentID == 71){
									//户型
									caseclass.setId(mClassListBean.result.get(i).getId());
									caseclass.setTitle(mClassListBean.result.get(i).getTitle());
									caseclass.setOrderNum(mClassListBean.result.get(i).getOrderNum());
									huxing.add(caseclass);
								}else if(mClassListBean.result.get(i).parentID == 2224){
									//面积
									caseclass.setId(mClassListBean.result.get(i).getId());
									String sss = mClassListBean.result.get(i).getTitle();
									sss = sss.replace("平米","㎡");
									caseclass.setTitle(sss);
									caseclass.setOrderNum(mClassListBean.result.get(i).getOrderNum());
									mianji.add(caseclass);

								}else if(mClassListBean.result.get(i).parentID == 125){
									//格调
									caseclass.setId(mClassListBean.result.get(i).getId());
									caseclass.setTitle(mClassListBean.result.get(i).getTitle());
									caseclass.setOrderNum(mClassListBean.result.get(i).getOrderNum());
									gediao.add(caseclass);
								}else if(mClassListBean.result.get(i).parentID == 87){
									//预算
									caseclass.setId(mClassListBean.result.get(i).getId());
									caseclass.setTitle(mClassListBean.result.get(i).getTitle());
									caseclass.setOrderNum(mClassListBean.result.get(i).getOrderNum());
									yusuan.add(caseclass);
								}else if(mClassListBean.result.get(i).parentID == 2241){
									//擅长空间
									caseclass.setId(mClassListBean.result.get(i).getId());
									caseclass.setTitle(mClassListBean.result.get(i).getTitle());
									caseclass.setOrderNum(mClassListBean.result.get(i).getOrderNum());
									shangchang.add(caseclass);
								}
							}
							Collections.sort(kongjian, new SortByAge());
							Collections.sort(fengge, new SortByAge());
							Collections.sort(sjfenge, new SortByAge());
							Collections.sort(huxing, new SortByAge());
							Collections.sort(mianji, new SortByAge());
							Collections.sort(gediao, new SortByAge());
							Collections.sort(yusuan, new SortByAge());
							Collections.sort(shangchang, new SortByAge());
							CaseClass c1 = new CaseClass(0, "全部");
							c1.setCheck(true);
							kongjian.add(0, c1);
							fengge.add(0, c1);
							sjfenge.add(0, c1);
							huxing.add(0, c1);
							mianji.add(0, c1);
							gediao.add(0, c1);
							yusuan.add(0, c1);
							shangchang.add(0, c1);
							hashMap.put("空间",kongjian);
							hashMap.put("风格",fengge);
							hashMap.put("设计师风格",sjfenge);
							hashMap.put("户型",huxing);
							hashMap.put("面积",mianji);
							hashMap.put("格调",gediao);
							hashMap.put("预算",yusuan);
							hashMap.put("擅长空间",shangchang);
							getHOUSEATTRI();
						}
					}

				});
	}
	private void  getHOUSEATTRI(){
		String url = AppUrl.ATTRIBUTELIST;
		RequestParams params = new RequestParams();
		params.setHeader("apptype","android_shejiquan");
		UUID guid = UUID.randomUUID();
		params.setHeader("appid",guid.toString());
		long time = new Date().getTime();
		params.setHeader("timestamp",time/1000+"");
		params.setHeader("signature",getSignature(guid.toString(),time/1000+""));
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("classID","2");
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				AttributeListBean mClassListBean = CommonUtils.getDomain(arg0,
						AttributeListBean.class);
				if(mClassListBean !=null && mClassListBean.result!=null){
					ArrayList<CaseClass> quanfenggeList=new ArrayList<CaseClass>();
					for (int i = 0; i < mClassListBean.result.size(); i++) {
						CaseClass caseClass =new CaseClass();
						caseClass.setTitle(mClassListBean.result.get(i).getTitle());
						caseClass.setId(mClassListBean.result.get(i).getId());
//						caseClass.setOrderNum(0);
						quanfenggeList.add(caseClass);
					}
					CaseClass c1 = new CaseClass(0, "全部");
					c1.setCheck(true);
					quanfenggeList.add(0, c1);
					hashMap.put("全屋风格",quanfenggeList);
					getquanHu();
				}
			}
			@Override
			public void onFailure(HttpException error, String arg1) {
				Toast.makeText(context,arg1,Toast.LENGTH_SHORT).show();
			}};
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST,url, params, callBack);
	}
	private void  getquanHu(){
		String url = AppUrl.ATTRIBUTELIST;
		RequestParams params = new RequestParams();
		params.setHeader("apptype","android_shejiquan");
		UUID guid = UUID.randomUUID();
		params.setHeader("appid",guid.toString());
		long time = new Date().getTime();
		params.setHeader("timestamp",time/1000+"");
		params.setHeader("signature",getSignature(guid.toString(),time/1000+""));
		params.setHeader("Content-Type","application/json");
		try {
			JSONObject param  = new JSONObject();
			param.put("classID","4");
			StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
			params.setBodyEntity(sEntity);
		}catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		RequestCallBack<String> callBack = new RequestCallBack<String>() {
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				AttributeListBean mClassListBean = CommonUtils.getDomain(arg0,
						AttributeListBean.class);
				if(mClassListBean !=null && mClassListBean.result!=null){
					ArrayList<CaseClass> quanfenggeList=new ArrayList<CaseClass>();
					for (int i = 0; i < mClassListBean.result.size(); i++) {
						CaseClass caseClass =new CaseClass();
						caseClass.setTitle(mClassListBean.result.get(i).getTitle());
						caseClass.setId(mClassListBean.result.get(i).getId());
//						caseClass.setOrderNum(0);
						quanfenggeList.add(caseClass);
					}
					CaseClass c1 = new CaseClass(0, "全部");
					c1.setCheck(true);
					quanfenggeList.add(0, c1);
					hashMap.put("全屋户型",quanfenggeList);

					SwClassListBean swClassListBean=new SwClassListBean();
					swClassListBean.setListHashMap(hashMap);
					cace(swClassListBean.toString());
				}
			}
			@Override
			public void onFailure(HttpException error, String arg1) {
				Toast.makeText(context,arg1,Toast.LENGTH_SHORT).show();
			}};
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST,url, params, callBack);
	}
	public String getSignature( String deviceID,String timeStamp) {
		String signature = "android_shejiquan_bzw315appnewversion" + "android_shejiquan"
				+ deviceID + timeStamp+"/api/Common/SystemAttributeList";
		return MD5Util.MD5(signature.toLowerCase());
	}
	protected void cace(String result) {
		if (context != null)
			CacheUtils.cacheStringData(context, "CLASS_LIST", KEY_TAG, result);
	}
	class SortByAge implements Comparator {
		public int compare(Object o1, Object o2) {
			CaseClass s1 = (CaseClass) o1;
			CaseClass s2 = (CaseClass) o2;
			if (s1.getOrderNum() > s2.getOrderNum())
				return 1;
			else if (s1.getOrderNum() == s2.getOrderNum()) {
				return 0;
			}
			return -1;
		}
	}
}
