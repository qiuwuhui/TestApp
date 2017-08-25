package com.baolinetworktechnology.shejiquan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppStatusManager;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.utils.CommonUtils;
import com.baolinetworktechnology.shejiquan.utils.MD5Util;
import com.baolinetworktechnology.shejiquan.view.MyDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有的Activity 最好继承此类
 * 
 * @author JiSheng.Guo
 * 
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
	public MyDialog dialog;

	/**
	 * 默认请求超时8秒
	 * 
	 * @return HttpUtils
	 */
	public HttpUtils getHttpUtils() {
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		return httpUtil;
	}

	void showDialog() {
		if (dialog != null) {
			dialog.show();
		}

	}

	void dismissDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}

	}

	/**
	 * 
	 * @param CacheExpiry
	 *            请求缓存时间
	 * @return HttpUtils
	 */
	public HttpUtils getHttpUtils(int CacheExpiry) {
		HttpUtils httpUtil = new HttpUtils(8 * 1000);
		httpUtil.configDefaultHttpCacheExpiry(CacheExpiry);
		return httpUtil;

	}

	/**
	 * 
	 * @param url
	 *            请求URL,不包含域名
	 * @return RequestParams
	 */
	public RequestParams getParams(String url) {

		RequestParams params = new RequestParams();
		if (SJQApp.user != null) {
			params.setHeader("Token", SJQApp.user.Token);
		} else {
			params.setHeader("Token", null);
		}
		params.setHeader("Client", AppTag.CLIENT);
		params.setHeader("Version", ApiUrl.VERSION);
		params.setHeader("AppAgent", ApiUrl.APP_AGENT);
		params.setHeader("Hash", MD5Util.getMD5String(url + AppTag.MD5));
		return params;

	}

	/**
	 * 快速吐丝
	 * 
	 * @param context
	 *            上下文
	 * @param text
	 *            吐丝内容
	 * @param time
	 *            吐丝显示时间
	 */

	public void toastShow(String text) {
		toastShow(this, text, Toast.LENGTH_SHORT);
	}

	Toast toast;

	protected void toastShow(int networkSorry) {
		toast = Toast.makeText(getApplicationContext(), networkSorry,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	public void toastShow(Context context, String text, int time) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context.getApplicationContext(), text, time);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 设置Titile 上的文字
	 * 
	 * @param text
	 *            标题名称
	 */
	public void setTitle(String text) {
		TextView title = (TextView) findViewById(R.id.tv_title);
		if (title != null) {
			if (title.getVisibility() != View.VISIBLE) {
				title.setVisibility(View.VISIBLE);
			}
			title.setText(text);
			findViewById(R.id.back).setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			hideInput();
			finish();
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (AppStatusManager.getInstance().getAppStatus()) {
			/**
			 * 应用被强杀
			 */
			case AppStatusConstant.STATUS_FORCE_KILLED:
				//跳到主页,主页lauchmode SINGLETASK
				restartApp();
				break;
			case AppStatusConstant.STATUS_NORMAL:
				setUpViewAndData();
				break;
		}
		dialog = new MyDialog(this);
		//魅族
		CommonUtils.setMeizuStatusBarDarkIcon(this, true);
		//小米
		CommonUtils.setMiuiStatusBarDarkMode(this, true);
		// setEdgeFromLeft();
		// setTitle(getTitle().toString());

	}
	protected abstract void setUpViewAndData();
	protected void restartApp() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(AppStatusConstant.KEY_HOME_ACTION,AppStatusConstant.ACTION_RESTART_APP);
		startActivity(intent);
	}
	@Override
	protected void onStart() {
		if (findViewById(R.id.back) != null)
			findViewById(R.id.back).setOnClickListener(this);// 后退键监听
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 获取当前Activity 名称
		// String AtyName = getRunningActivityName();
		// 友盟统计
		// MobclickAgent.onResume(this);
		// MobclickAgent.onPageStart(AtyName); //
		// 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		// TCAgent.onPageStart(this, AtyName);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 获取当前Activity 名称
		// String AtyName = getRunningActivityName();
		// MobclickAgent.onPageEnd(AtyName); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		// MobclickAgent.onPause(this);
		// TCAgent.onPageEnd(this, AtyName);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		dialog = null;
		if (toast != null) {
			toast.cancel();
		}

	}

	/**
	 * 关闭输入法弹窗
	 */
	public void hideInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (this.getCurrentFocus() != null)
			inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}

	// 获取当前Activity 名称
	String getRunningActivityName() {
		String contextString = this.toString();
		return contextString.substring(contextString.lastIndexOf(".") + 1,
				contextString.indexOf("@"));
	}

	 Context getActivity() {
		// TODO Auto-generated method stub
		return this;
	}
	private HttpClient httpClient;
	public HttpClient getHttpClient() {

		// 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）

		BasicHttpParams httpParams = new BasicHttpParams();

		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小

		HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);

		HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);

		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		// 设置重定向，缺省为 true

		HttpClientParams.setRedirecting(httpParams, true);

		// 设置 user agent

		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		// 创建一个 HttpClient 实例

		// 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient

		// 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient

		httpClient = new DefaultHttpClient(httpParams);

		return httpClient;
	}

	private final String TAG = "MPermissions";
	private int REQUEST_CODE_PERMISSION = 0x00099;

	/**
	 * 请求权限
	 *
	 * @param permissions 请求的权限
	 * @param requestCode 请求权限的请求码
	 */
	public void requestPermission(String[] permissions, int requestCode) {
		this.REQUEST_CODE_PERMISSION = requestCode;
		if (checkPermissions(permissions)) {
			permissionSuccess(REQUEST_CODE_PERMISSION);
		} else {
			List<String> needPermissions = getDeniedPermissions(permissions);
			ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
		}
	}

	/**
	 * 检测所有的权限是否都已授权
	 *
	 * @param permissions
	 * @return
	 */
	private boolean checkPermissions(String[] permissions) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(this, permission) !=
					PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 */
	private List<String> getDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissionList = new ArrayList<>();
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(this, permission) !=
					PackageManager.PERMISSION_GRANTED ||
					ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
				needRequestPermissionList.add(permission);
			}
		}
		return needRequestPermissionList;
	}


	/**
	 * 系统请求权限回调
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE_PERMISSION) {
			if (verifyPermissions(grantResults)) {
				permissionSuccess(REQUEST_CODE_PERMISSION);
			} else {
				permissionFail(REQUEST_CODE_PERMISSION);
				showTipsDialog();
			}
		}
	}

	/**
	 * 确认所有的权限是否都已授权
	 *
	 * @param grantResults
	 * @return
	 */
	private boolean verifyPermissions(int[] grantResults) {
		for (int grantResult : grantResults) {
			if (grantResult != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 显示提示对话框
	 */
	private void showTipsDialog() {
		new AlertDialog.Builder(this)
				.setTitle("提示信息")
				.setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startAppSettings();
					}
				}).show();
	}

	/**
	 * 启动当前应用设置页面
	 */
	private void startAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}

	/**
	 * 获取权限成功
	 *
	 * @param requestCode
	 */
	public void permissionSuccess(int requestCode) {
		Log.d(TAG, "获取权限成功=" + requestCode);

	}

	/**
	 * 权限获取失败
	 * @param requestCode
	 */
	public void permissionFail(int requestCode) {
		Log.d(TAG, "获取权限失败=" + requestCode);
	}
}
