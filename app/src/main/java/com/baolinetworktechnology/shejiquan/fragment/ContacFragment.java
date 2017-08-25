package com.baolinetworktechnology.shejiquan.fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.baolinetwkchnology.shejiquan.xiaoxi.SideBar;
import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.AnewfriendActivity;
import com.baolinetworktechnology.shejiquan.activity.MyFragmentActivity.IBackPressed;
import com.baolinetworktechnology.shejiquan.activity.NewLabelActivity;
import com.baolinetworktechnology.shejiquan.activity.SearchFriendsActivity;
import com.baolinetworktechnology.shejiquan.activity.SkipActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppUrl;
import com.baolinetworktechnology.shejiquan.db.DBHelper;
import com.baolinetworktechnology.shejiquan.domain.AnewModel;
import com.baolinetworktechnology.shejiquan.domain.AnewfrienList;
import com.baolinetworktechnology.shejiquan.domain.SortModel;
import com.baolinetworktechnology.shejiquan.domain.SortModelList;
import com.baolinetworktechnology.shejiquan.domain.SwresultBen;
import com.baolinetworktechnology.shejiquan.tongxunlu.ContactAdapter;
import com.baolinetworktechnology.shejiquan.tongxunlu.StickyRecyclerHeadersDecoration;
import com.baolinetworktechnology.shejiquan.tongxunlu.TouchableRecyclerView;
import com.baolinetworktechnology.shejiquan.utils.CharacterParser;
import com.baolinetworktechnology.shejiquan.utils.PinyinComparator;
import com.baolinetworktechnology.shejiquan.view.CircleImg;
import com.baolinetworktechnology.shejiquan.view.IconCenterEditText;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 联系人列表-通讯录
 * @author JiSheng.Guo
 */
public class ContacFragment extends BaseMainFragment implements
		IBackPressed {
	private TouchableRecyclerView mRecyclerView;
	private ContactAdapter mAdapter;
	private SideBar sideBar;//a-z
	private TextView dialogText;//a
	private PinyinComparator pinyinComparator;
	private IconCenterEditText mClearEditText;
	private CharacterParser characterParser;
	private View view,list_tongzhi,list_add,list_biaoqian;
	private ImageView add_item;
    private DBHelper helper;
	private List<SortModel> contactList;
	private CircleImg Image_long;
	private TextView add_name,add_py_mun;
	private AnewModel comanewModel=null;
	private BitmapUtils mImageUtil;
	private CountDownTimer time;
	private MyDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(view == null){
			view = inflater.inflate(R.layout.contactext_fragment_layout, container,
					false);
			initView(view);//
			initData(view);//
			getAnewList();
			// 实现TextWatcher监听即可
			mClearEditText.setOnSearchClickListener(new IconCenterEditText.OnSearchClickListener() {
				@Override
				public void onSearchClick(View view) {

				}
			});
			// 根据输入框输入值的改变来过滤搜索
			mClearEditText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
										  int before, int count) {
					// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
					filterData(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
											  int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});
		}
		return view;
	}

	private void showData() {
		if(list_tongzhi.getVisibility() == View.VISIBLE){
			list_tongzhi.setVisibility(View.GONE);
		}
		if(list_biaoqian.getVisibility() == View.VISIBLE){
			list_biaoqian.setVisibility(View.GONE);
		}
		if(SJQApp.user != null){
			Cursor cursor = helper.query(SJQApp.user.getGuid());
			if(contactList == null){
				contactList =new ArrayList<>();
			}else{
				contactList.clear();
			}
			 while (cursor.moveToNext()) {
					SortModel sortmodel = new SortModel();
					sortmodel.setFriendGUID(cursor.getString(cursor.getColumnIndex("friendGUID")));
					sortmodel.setFromID(cursor.getInt(cursor.getColumnIndex("fromID")));
					sortmodel.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
					sortmodel.setName(cursor.getString(cursor.getColumnIndex("name")));
					sortmodel.setMarkStatus(cursor.getInt(cursor.getColumnIndex("markStatus")));
					sortmodel.setMarkName(cursor.getString(cursor.getColumnIndex("markName")));
					sortmodel.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
				    String mobilesrc=cursor.getString(cursor.getColumnIndex("mobile"));
				    if(!TextUtils.isEmpty(mobilesrc)){
					   String[] mobilelist = mobilesrc.split(",");
					   sortmodel.setMobile(mobilelist);
				    }
					String isConcern = cursor.getString(cursor.getColumnIndex("isConcern"));
				    if (isConcern.equals("1")) {
					    sortmodel.setSortLetters("%");
						sortmodel.setConcern(true);
				    } else {
						sortmodel.setSortLetters(filledData(cursor.getString(cursor.getColumnIndex("initial"))));
					    sortmodel.setConcern(false);
				    }
					sortmodel.setRemarkName(cursor.getString(cursor.getColumnIndex("remarkName")));
					contactList.add(sortmodel);
				}
			    cursor.close();
			    if(contactList.size() ==0 ){
					loadata();
				}else{
					if(list_tongzhi.getVisibility() == View.VISIBLE){
						list_tongzhi.setVisibility(View.GONE);
					}
					if(list_add.getVisibility() == View.VISIBLE){
						list_add.setVisibility(View.GONE);
					}
					if(list_biaoqian.getVisibility() == View.VISIBLE){
						list_biaoqian.setVisibility(View.GONE);
					}
					Collections.sort(contactList, pinyinComparator);
					mAdapter.setList(contactList);
					mAdapter.notifyDataSetChanged();
					setHeader(mRecyclerView);
				}
		 }
	}
	private void shuaxinData() {
		  if(list_tongzhi.getVisibility() == View.VISIBLE){
			 list_tongzhi.setVisibility(View.GONE);
		    }
		if(list_biaoqian.getVisibility() == View.VISIBLE){
			list_biaoqian.setVisibility(View.GONE);
		}
			Cursor cursor = helper.query(SJQApp.user.getGuid());
		    if(contactList == null){
			   contactList =new ArrayList<>();
	    	}else{
			   contactList.clear();
		    }
			while (cursor.moveToNext()) {
				SortModel sortmodel = new SortModel();
				sortmodel.setFriendGUID(cursor.getString(cursor.getColumnIndex("friendGUID")));
				sortmodel.setFromID(cursor.getInt(cursor.getColumnIndex("fromID")));
				sortmodel.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
				sortmodel.setName(cursor.getString(cursor.getColumnIndex("name")));
				sortmodel.setMarkStatus(cursor.getInt(cursor.getColumnIndex("markStatus")));
				sortmodel.setMarkName(cursor.getString(cursor.getColumnIndex("markName")));
				sortmodel.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
				String mobilesrc=cursor.getString(cursor.getColumnIndex("mobile"));
				if(!TextUtils.isEmpty(mobilesrc)){
					String[] mobilelist = mobilesrc.split(",");
					sortmodel.setMobile(mobilelist);
				}
				String isConcern = cursor.getString(cursor.getColumnIndex("isConcern"));
				if (isConcern.equals("1")) {
					sortmodel.setSortLetters("%");
					sortmodel.setConcern(true);
				} else {
					sortmodel.setSortLetters(filledData(cursor.getString(cursor.getColumnIndex("initial"))));
					sortmodel.setConcern(false);
				}
				sortmodel.setRemarkName(cursor.getString(cursor.getColumnIndex("remarkName")));
				contactList.add(sortmodel);
			}
				// 根据a-z进行排序源数据
		if(contactList.size() ==0){
			if(comanewModel != null && comanewModel.getIsHint()!=0){
				list_add.setVisibility(View.VISIBLE);
			}else{
				list_tongzhi.setVisibility(View.VISIBLE);
			}
			list_biaoqian.setVisibility(View.VISIBLE);
		}else {
			if(list_tongzhi.getVisibility() == View.VISIBLE){
				list_tongzhi.setVisibility(View.GONE);
			}
			if(list_add.getVisibility() == View.VISIBLE){
				list_add.setVisibility(View.GONE);
			}
			if(list_biaoqian.getVisibility() == View.VISIBLE){
				list_biaoqian.setVisibility(View.GONE);
			}
			Collections.sort(contactList, pinyinComparator);
			mAdapter.setList(contactList);
			mAdapter.notifyDataSetChanged();
			setHeader(mRecyclerView);
		}

	}

	private void initData(View view) {
		helper=new DBHelper(getActivity().getApplicationContext(),DBHelper.DB_NAME);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar.setTextView(dialogText);

		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener()
		{

			@Override
			public void onTouchingLetterChanged(String s)
			{
				if (mAdapter != null) {
					mAdapter.closeOpenedSwipeItemLayoutWithAnim();
				}
				int position = mAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mRecyclerView.getLayoutManager().scrollToPosition(position);
				}

			}
		});
			if(contactList == null){
				contactList =new ArrayList<>();
			}else{
				contactList.clear();
			}
			mAdapter = new ContactAdapter(getActivity(), contactList);
			int orientation = LinearLayoutManager.VERTICAL;
			final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), orientation, false);
			mRecyclerView.setLayoutManager(layoutManager);
			mRecyclerView.setAdapter(mAdapter);
			final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
			mRecyclerView.addItemDecoration(headersDecor);
//			mRecyclerView.addItemDecoration(new DividerDecoration(getActivity()));
			mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
			{
				@Override
				public void onChanged()
				{
					headersDecor.invalidateHeaders();
				}
			});
	}

	private void initView(View view) {
		//加载图片类
		mImageUtil = new BitmapUtils(getActivity());
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型

		dialog =new MyDialog(getActivity());
		//控件加载
		sideBar = (SideBar) view.findViewById(R.id.contact_sidebar);
		dialogText = (TextView) view.findViewById(R.id.contact_dialog);
		mRecyclerView = (TouchableRecyclerView) view.findViewById(R.id.contact_member);
		mClearEditText = (IconCenterEditText) view.findViewById(R.id.filter_edit);
		mClearEditText.setHintTextColor(getResources().getColor(R.color.shouqi));
		add_item = (ImageView) view.findViewById(R.id.add_item);
		add_item.setOnClickListener(this);
		list_tongzhi = view.findViewById(R.id.list_tongzhi);
		list_tongzhi.setOnClickListener(this);
		list_add = view.findViewById(R.id.list_add);
		list_add.setOnClickListener(this);
		list_biaoqian = view.findViewById(R.id.list_biaoqian);
		list_biaoqian.setOnClickListener(this);
		Image_long = (CircleImg) view.findViewById(R.id.Image_long);
		add_name = (TextView) view.findViewById(R.id.add_name);
		add_py_mun = (TextView) view.findViewById(R.id.add_py_mun);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
			case R.id.list_tongzhi:
				Intent intent1 = new Intent(getActivity(), AnewfriendActivity.class);
				getActivity().startActivity(intent1);
				break;
			case R.id.list_add:
				time = new CountDownTimer(500, 500) {

					@Override
					public void onTick(long millisUntilFinished) {

					}
					@Override
					public void onFinish() {
						list_add.setVisibility(View.GONE);
						list_tongzhi.setVisibility(View.VISIBLE);
					}
				};
				time.start();
				Intent addintent = new Intent(getActivity(), AnewfriendActivity.class);
				getActivity().startActivity(addintent);
				break;
			case R.id.add_item:
				Intent intent = new Intent(getActivity(), SearchFriendsActivity.class);
				getActivity().startActivity(intent);
				break;
			case R.id.list_biaoqian:
				Intent intent2 = new Intent(getActivity(), NewLabelActivity.class);
				getActivity().startActivity(intent2);
				break;
		}
	}



	void go2Splash() {
		toastShow("抱歉程序出现问题，即将重启动");
		startActivity(new Intent(getActivity(), SkipActivity.class));
		getActivity().finish();
	}

	public void setTabClick() {


	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ContactFragment");
	}

	@Override
	protected void lazyLoad() {

	}
	public void setShowData() {
		loadata();
	}
	public void ShowFriend() {
		ShowAnewList();
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ContactFragment");
	}
	@Override
	public void backPressed() {
	}
	private void loadata() {
		String url= AppUrl.GETEASEFRIEND+SJQApp.user.getGuid();
		RequestCallBack<String> callBack = new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String message) {
				toastShow(message);
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Gson gson = new Gson();
				SortModelList bean=gson.fromJson(responseInfo.result, SortModelList.class);
				if(bean !=null && bean.result!=null){
					helper.delete(SJQApp.user.getGuid());
					for (int i = 0; i < bean.result.size(); i++) {
						SortModel sortmodel= bean.result.get(i);
						ContentValues values=new ContentValues();
						//在value中添加信息
						if(sortmodel.getFriendGUID()!=null)
						values.put("friendGUID",sortmodel.getFriendGUID());
						values.put("fromID", sortmodel.getFromID());
						if(sortmodel.getLogo()!=null)
						values.put("logo", sortmodel.getLogo());
						if(sortmodel.getName()!=null)
						values.put("name", sortmodel.getName());
						values.put("markStatus", sortmodel.getMarkStatus());
						if(sortmodel.getMarkName()!=null)
						values.put("markName", sortmodel.getMarkName());
						if(sortmodel.getNickName()!=null)
						values.put("nickName", sortmodel.getNickName());
						if(sortmodel.isConcern()){
							values.put("isConcern", "1");
						}else{
							values.put("isConcern", "0");
						}
						String[] mobileList = sortmodel.getMobile();
						if(mobileList!=null){
							StringBuffer mobile = new StringBuffer();
							for (int c = 0; c < mobileList.length; c++){
								mobile.append(mobileList[c]+",");
							}
							values.put("mobile", mobile.toString());
						}
						if(sortmodel.getRemarkName()!=null)
						values.put("remarkName", sortmodel.getRemarkName());
						if(sortmodel.getSortLetters()!=null)
						values.put("initial", sortmodel.getSortLetters());
						values.put("guid", SJQApp.user.getGuid());
						DBHelper helper=new DBHelper(getActivity().getApplicationContext(),DBHelper.DB_NAME);
						helper.insert(values);
					}
					shuaxinData();
				}
			}
		};
		getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
				callBack);
	}
	/**
	 * 为ListView填充数据
	 *
	 * @param date
	 * @return
	 */
	private String filledData(String date) {
		     if(TextUtils.isEmpty(date)){
				 return "#";
			 }else{
				 SortModel sortModel = new SortModel();
				 sortModel.setName(date);
				 // 汉字转换成拼音
				 String pinyin = characterParser.getSelling(date);
				 String sortString = pinyin.substring(0, 1).toUpperCase();

				 // 正则表达式，判断首字母是否是英文字母
				 if (sortString.matches("[A-Z]")) {
					 return sortString.toUpperCase();
				 } else {
					 return "#";
				 }
			 }
	    }
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 *
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = contactList;
			// 根据a-z进行排序
			Collections.sort(filterDateList, pinyinComparator);
			mAdapter.serList1(filterDateList);
			mAdapter.notifyDataSetChanged();
			setHeader(mRecyclerView);
		} else {
			filterDateList.clear();
			for (SortModel sortModel : contactList) {
				String name = sortModel.getName();
				if(!TextUtils.isEmpty(name)){
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
							filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}else{
					String NickName = sortModel.getNickName();
					if(!TextUtils.isEmpty(NickName)){
						if (NickName.indexOf(filterStr.toString()) != -1
								|| characterParser.getSelling(NickName).startsWith(
								filterStr.toString())) {
							filterDateList.add(sortModel);
						}
					}
				}
			}
			// 根据a-z进行排序
			Collections.sort(filterDateList, pinyinComparator);
			mAdapter.serList(filterDateList);
			mAdapter.notifyDataSetChanged();
		}

//		// 根据a-z进行排序
//		Collections.sort(filterDateList, pinyinComparator);
//		mAdapter.serList(filterDateList);
//		mAdapter.notifyDataSetChanged();
	}
	public void deleteUser(final int position)
	{
		if(position == -1){
			toastShow("数据出错");
		}else{
			doConcer(position);
		}

	}
	public void addpengyou() {
		time = new CountDownTimer(500, 500) {

			@Override
			public void onTick(long millisUntilFinished) {

			}
			@Override
			public void onFinish() {
				showData();
			}
		};
		time.start();
	}
	/**
	 * 关注动作
	 */
	private String designerGudi="";
	public void doConcer(final int position) {
		dialog.show();
		String markStatus =contactList.get(position).getMarkName();
		designerGudi = contactList.get(position).getFriendGUID();
		if(markStatus.equals("PERSONAL") || markStatus.equals("ROBOT")){
			String url = AppUrl.DELETAEASEFRIEND;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("userGuid", SJQApp.user.guid);
				param.put("fromGuid", designerGudi);
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException error, String msg) {
							dialog.dismiss();
							toastShow("取消关注失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> n) {
							dialog.dismiss();
							Gson gson = new Gson();
							SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
							if (bean != null) {
								if (bean.result.operatResult) {
									helper.del(designerGudi);
									contactList.remove(position);
									mAdapter.remove(mAdapter.getItem(position));
									Intent intent = new Intent();
									intent.setAction("delete");
									getActivity().sendBroadcast(intent);
								}else{
									toastShow("取消关注失败");
								}
							}
						}
					});

		}else if(markStatus.equals("DESIGNER")){
			String url = AppUrl.FAVORITE_ADD;
			RequestParams params = new RequestParams();
			params.setHeader("Content-Type","application/json");
			try {
				JSONObject param  = new JSONObject();
				param.put("classType","1");
				param.put("userGUID", SJQApp.user.guid);
				param.put("fromGUID", designerGudi);
				param.put("favoriteMark", "1");
				param.put("operate", "0");
				StringEntity sEntity = new StringEntity(param.toString(), "utf-8");
				params.setBodyEntity(sEntity);
			}catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getHttpUtils().send(HttpRequest.HttpMethod.POST, AppUrl.API + url, params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException error, String msg) {
							dialog.dismiss();
							toastShow("当前网络连接失败");
						}
						@Override
						public void onSuccess(ResponseInfo<String> n) {
							dialog.dismiss();
							Gson gson = new Gson();
							SwresultBen bean=gson.fromJson(n.result, SwresultBen.class);
							if (bean != null) {
								if (bean.result.operatResult) {
									helper.del(designerGudi);
									contactList.remove(position);
									mAdapter.remove(mAdapter.getItem(position));
									Intent intent = new Intent();
									intent.setAction("delete");
									getActivity().sendBroadcast(intent);
								}else{
									toastShow("取消关注失败");
								}
							}

						}
					});
		}
	}
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				View v = getActivity().getCurrentFocus();
				if (isShouldHideKeyboard(v, ev)) {
					hideKeyboard(v.getWindowToken());
			    }
		}
		return false;
	}
	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
	 *
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideKeyboard(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = {0, 0};
			v.getLocationInWindow(l);
			int left = l[0],
					top = l[1],
					bottom = top + v.getHeight(),
					right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				v.clearFocus();
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
		return false;
	}
	private void getAnewList() {
		if(SJQApp.user!= null){
			String url= AppUrl.ADVISEEASEFRI+ SJQApp.user.getGuid();
			RequestCallBack<String> callBack = new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String message) {
					toastShow(message);
					showData();
				}
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Gson gson = new Gson();
					AnewfrienList bean=gson.fromJson(responseInfo.result, AnewfrienList.class);
					if(bean !=null && bean.result!=null && bean.result.size()!=0){
						comanewModel =new AnewModel();
						List<AnewModel> anewModelList =bean.result;
						int mun=0;
						for(AnewModel anewModel:anewModelList){
							if(anewModel.getIsHint() == 1){
								mun++;
								if(mun==1){
									comanewModel.setName(anewModel.getName());
									comanewModel.setLogo(anewModel.getLogo());
								}
							}
						}
						comanewModel.setIsHint(mun);
						mImageUtil.display(Image_long,comanewModel.getLogo());
						add_name.setText(comanewModel.getName());
						add_py_mun.setText(comanewModel.getIsHint()+"");
						mAdapter.setHeaderModel(comanewModel);
						showData();
					}else{
						showData();
					}
				}
			};
			getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
					callBack);
		}
	}
	private void ShowAnewList() {
		if(SJQApp.user!= null){
			String url= AppUrl.ADVISEEASEFRI+ SJQApp.user.getGuid();
			RequestCallBack<String> callBack = new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String message) {
					toastShow(message);
					loadata();
				}
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Gson gson = new Gson();
					AnewfrienList bean=gson.fromJson(responseInfo.result, AnewfrienList.class);
					if(bean !=null && bean.result!=null && bean.result.size()!=0){
						comanewModel =new AnewModel();
						List<AnewModel> anewModelList =bean.result;
						int mun=0;
						for(AnewModel anewModel:anewModelList){
							if(anewModel.getIsHint() == 1){
								mun++;
								if(mun==1){
									comanewModel.setName(anewModel.getName());
									comanewModel.setLogo(anewModel.getLogo());
								}
							}
						}
						comanewModel.setIsHint(mun);
						mImageUtil.display(Image_long,comanewModel.getLogo());
						add_name.setText(comanewModel.getName());
						add_py_mun.setText(comanewModel.getIsHint()+"");
						mAdapter.setHeaderModel(comanewModel);
						loadata();
					}else{
						loadata();
					}
				}
			};
			getHttpUtils().send(HttpRequest.HttpMethod.GET, AppUrl.API + url, getParams(url),
					callBack);
		}
	}
	/**
	 * 获取InputMethodManager，隐藏软键盘
	 *
	 * @param token
	 */
	private void hideKeyboard(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mRecyclerView != null){
			mRecyclerView.setAdapter(null);
		}
		if (helper != null){
			helper.close();
		}
	}
	private void setHeader(RecyclerView view) {
		View header = LayoutInflater.from(getActivity()).inflate(R.layout.add_recyclerview_header, view, false);
		mAdapter.setHeaderView(header);
	}
}