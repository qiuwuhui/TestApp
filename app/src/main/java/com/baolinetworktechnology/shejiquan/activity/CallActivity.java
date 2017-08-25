package com.baolinetworktechnology.shejiquan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.domain.City;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetail;
import com.baolinetworktechnology.shejiquan.domain.DesignerDetailBean;
import com.baolinetworktechnology.shejiquan.domain.DesignerInfo;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.NetCallCenter;
import com.baolinetworktechnology.shejiquan.net.OnCallBack;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.DesignerHonorUtils;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CallActivity extends BaseActivity {

	private BitmapUtils mImageUtil;
	private String userLogoUrl;
	private TextView tvName, tvFromCity, tvTips;
	private View designer_iv_head;
	private DesignerInfo designer;

	private String userGuid;
	private String myPhone;
	private String orderPhone;
	View tvCall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);

		mImageUtil = new BitmapUtils(this);
		mImageUtil.configDefaultLoadingImage(R.drawable.xiaotu);
		mImageUtil.configDefaultLoadFailedImage(R.drawable.xiaotu);
		mImageUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//
		if (SJQApp.user == null) {
			// 是设计师 扣费设计师
			toastShow("登录后才可通话");
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;

		}
		initView();
		initData();

	}

	@Override
	protected void setUpViewAndData() {

	}
	@Override
	protected void restartApp() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
		switch (action) {
			case AppStatusConstant.ACTION_RESTART_APP:
				restartApp();
				break;
		}
	}
	private void initData() {
		userGuid = getIntent().getStringExtra(AppTag.TAG_GUID);
		designer = CommonUtils
				.getDomain(getIntent().getStringExtra(AppTag.TAG_JSON),
						DesignerInfo.class);
		if (TextUtils.isEmpty(userGuid) && designer != null)
			userGuid = designer.GUID;

		// 订单手机号
		orderPhone = getIntent().getStringExtra(AppTag.TAG_PHONE);

		if (TextUtils.isEmpty(myPhone) && SJQApp.user != null) {
			myPhone = SJQApp.user.Mobile;
			if (TextUtils.isEmpty(myPhone)) {// 原型要求拨打注册手机号
				if (SJQApp.ownerData != null)
					myPhone = SJQApp.ownerData.getMobile();
			}

			if (SJQApp.userData != null) {
				// myPhone = SJQApp.userData.getPhone();
				if (TextUtils.isEmpty(myPhone)) {
					// myPhone = SJQApp.user.Mobile;
					myPhone = SJQApp.userData.mobile;
				}
			}

		}

		if (!TextUtils.isEmpty(userGuid) && designer == null) {
			loadData(userGuid);
		}
		if (designer != null) {
			setData();
		}

	}

	private void initView() {
		designer_iv_head = findViewById(R.id.designer_iv_head);
		tvName = (TextView) findViewById(R.id.tvName);
		tvTips = (TextView) findViewById(R.id.tvTips);
		tvFromCity = (TextView) findViewById(R.id.tvFromCity);
		tvCall = findViewById(R.id.tvCall);
		tvCall.setAnimation(AnimationUtils.loadAnimation(this, R.anim.cycle));

	}

	private void setData() {
		if (tvName == null)
			return;
		doCall();
		String userName = "";
		String fromCity = "";
		if (designer != null) {
			userName = designer.Name;
			if (TextUtils.isEmpty(designer.FromCity)) {
				fromCity = getFromCity(this);
			} else {
				fromCity = designer.FromCity;
			}
			userLogoUrl = designer.Logo;
			new DesignerHonorUtils().setIcon(
					(ImageView) findViewById(R.id.ivHonor),
					designer.IsCertification,
					(ImageView) findViewById(R.id.ivGrade),
					designer.getServiceStatus());
		}
		tvName.setText(userName);
		if (TextUtils.isEmpty(fromCity)) {
			tvFromCity.setVisibility(View.GONE);
		} else {
			tvFromCity.setText(fromCity);
		}
		tvTips.setText("请" + myPhone + "接听4001513299来电");
		mImageUtil.display(designer_iv_head, userLogoUrl);

	}

	String getFromCity(Context context) {
		if (TextUtils.isEmpty(designer.FromCity)) {
			StringBuffer sb = new StringBuffer();
			City city1 = CityService.getInstance(context).getCityDB()
					.getCityID(designer.ProvinceID + "");

			City city2 = CityService.getInstance(context).getCityDB()
					.getCityID(designer.CityID + "");

			if (city1 != null) {
				sb.append(city1.Title);
			}

			if (city2 != null) {
				sb.append("-" + city2.Title);
			}

			designer.FromCity = sb.toString();
		}
		return designer.FromCity;
	}

	// 获取用户信息
	private void loadData(String guid) {

		HttpUtils httpUtil = new HttpUtils();
		String url = ApiUrl.GET_DESIGNER_INFO + guid;
		RequestParams params = getParams(url);
		httpUtil.send(HttpMethod.GET, ApiUrl.API + url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						toastShow("服务器繁忙,请稍后");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// dismissDialog();
						DesignerDetailBean data = CommonUtils.getDomain(
								responseInfo, DesignerDetailBean.class);
						if (data != null) {
//							designer = data.data;
							setData();

						}
					}

				});

	}

	/**
	 * 进行拨号
	 */
	void doCall() {
		// 设计师
		String PayerGUID = userGuid;

		// 当前未登录->关闭拨号页—>进行登录
		if (SJQApp.user == null) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			return;
		}
//		// 如果登录用户是设计师 扣费是当前登录用户
//		if (SJQApp.user.isDesinger()) {
//			PayerGUID = SJQApp.user.UserGUID;
//		}

		new NetCallCenter(new OnCallBack() {

			@Override
			public void onSuccess(String mesa) {
				toastShow(mesa);
			}

			@Override
			public void onNetStart() {

				// dialog.show("拨号中");

			}

			@Override
			public void onFailure(String mesa) {
				doFailure(mesa);
			}

			@Override
			public void onError(String json) {
				doFailure("");
			}

			@Override
			public void onError(HttpException arg0, String mesa) {
				doFailure("");
			}
			//打电话的GUID
		}, NetCallCenter.CallerGUID, SJQApp.user.guid,
		//被呼叫的GUID
		NetCallCenter.CalledGUID, userGuid,
		//付费GUID
		NetCallCenter.PayerGUID,PayerGUID,
		NetCallCenter.Mobile, orderPhone);

		// System.out.println("-->>CallerGUID=" + SJQApp.user.UserGUID
		// + ",CalledGUID=" + userGuid + "PayerGUID=" + PayerGUID
		// + ",Mobile=" + orderPhone);
	}

	protected void doFailure(String content) {
		if (tvName == null)
			return;
		new MyAlertDialog(this).setTitle("抱歉,暂时无法连通该用户").setContent(content)
				.setBtnOk("关闭").setBtnOnListener(new DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						finish();

					}
				}).show();

	}

	public void toastShow(String text) {

		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnCancel:
			tvName = null;
			finish();

			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		tvName = null;
		super.onDestroy();
	}
}
