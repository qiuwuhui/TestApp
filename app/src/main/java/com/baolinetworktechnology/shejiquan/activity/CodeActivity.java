package com.baolinetworktechnology.shejiquan.activity;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.camera.CameraManager;
import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.MessageIDs;
import com.baolinetworktechnology.shejiquan.decoding.InactivityTimer;
import com.baolinetworktechnology.shejiquan.decoding.RGBLuminanceSource;
import com.baolinetworktechnology.shejiquan.utils.FileUtil;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.baolinetworktechnology.shejiquan.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * 首页-二维码扫描
 * 
 * @author JiSheng.Guo
 * 
 */
public class CodeActivity extends Activity implements Callback {

	public static final String QR_RESULT = "RESULT";

	private CaptureActivityHandler mCaptureHandler;
	private ViewfinderView mViewfinderView;
	private SurfaceView mSurfaceView;
	private boolean mHasSurface;
	private Vector<BarcodeFormat> mDecodeFormats;
	private String mCharacterSet;
	private InactivityTimer mInactivityTimer;
	private MediaPlayer mMediaPlayer;
	private boolean mPlayBeep;
	// private static final float BEEP_VOLUME = 0.10f;
	private boolean mVibrate;
	CameraManager mCameraManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.scale_big, R.anim.alpha1);
		setContentView(R.layout.activity_code);

		if (!checkCameraDevice(this)) {
			finish();
			return;
		}
		if (!isCame()) {
			finish();
			return;
		}
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinderview);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mHasSurface = false;
		mInactivityTimer = new InactivityTimer(this);

	}

	/** * 检测Android设备是否支持摄像机 */
	public boolean checkCameraDevice(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	boolean isCame() {
		PackageManager pkm = getPackageManager();
		boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm
				.checkPermission("android.permission.CAMERA",
						"com.baolinetworktechnology.shejiquan"));
		if (!has_permission) {
			return false;
		}
		return true;
	}

	final int REQUEST_CODE = 20;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.more:
			Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
			if (Build.VERSION.SDK_INT < 19) {
				innerIntent.setAction(Intent.ACTION_GET_CONTENT);
			} else {
				innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
			}

			innerIntent.setType("image/*");

			Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");

			startActivityForResult(wrapperIntent, REQUEST_CODE);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计
		// MobclickAgent.onResume(this);
		// MobclickAgent.onPageStart("CodeActivity"); //
		// 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// CameraManager.init(getApplication());
		mCameraManager = new CameraManager(getApplication());

		mViewfinderView.setCameraManager(mCameraManager);

		SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
		if (mHasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		mDecodeFormats = null;
		mCharacterSet = null;

		mPlayBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			mPlayBeep = false;
		}
		initBeepSound();
		mVibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd("CodeActivity"); //
		// （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		// MobclickAgent.onPause(this);
		if (mCaptureHandler != null) {
			mCaptureHandler.quitSynchronously();
			mCaptureHandler = null;
		}
		mCameraManager.closeDriver();
	}

	@Override
	protected void onDestroy() {
		mInactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.alpha1, R.anim.scale_small);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			// CameraManager.get().openDriver(surfaceHolder);
			mCameraManager.openDriver(surfaceHolder);
		} catch (IOException ioe) {
			Toast.makeText(getApplicationContext(), "获取摄像头权限失败",
					Toast.LENGTH_SHORT).show();
			finish();

			return;
		} catch (RuntimeException e) {
			new MyAlertDialog(this).setTitle("提示")
					.setContent("无法获取摄像头数据，请检查是否已经打开摄像头权限").setBtnOk("确定")
					.setBtnOnListener(new DialogOnListener() {

						@Override
						public void onClickListener(boolean isOk) {
							finish();

						}
					}).show();

			return;
		}
		if (mCaptureHandler == null) {
			mCaptureHandler = new CaptureActivityHandler(this, mDecodeFormats,
					mCharacterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!mHasSurface) {
			mHasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHasSurface = false;

	}

	public CameraManager getCameraManager() {
		return mCameraManager;
	}

	public ViewfinderView getViewfinderView() {
		return mViewfinderView;
	}

	public Handler getHandler() {
		return mCaptureHandler;
	}

	public void drawViewfinder() {
		mViewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		mInactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		showResult(obj, barcode);
	}

	private Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {

			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {

			e.printStackTrace();

		} catch (ChecksumException e) {

			e.printStackTrace();

		} catch (FormatException e) {

			e.printStackTrace();

		}

		return null;

	}

	private void showResult(final Result rawResult, Bitmap barcode) {
		final String result = rawResult.getText();
		// 判断是否微名片
		// 判断是否微名片
		String urlwei = result.toLowerCase();
		Pattern p1 = Pattern
				.compile("[a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}");
		Matcher m1 = p1.matcher(urlwei);
		if (m1.find()) {
			Intent intent = new Intent(this, WeiShopWebActivity.class);
			intent.putExtra(AppTag.TAG_GUID, m1.group());
			startActivity(intent);
			finish();
			return;
		}

		if (result.indexOf("shop/des") != -1) {
			Pattern p = Pattern.compile("[1-9][0-9]{5,}");
			Matcher m = p.matcher(result);
			if (m.find()) {
				Intent intent = new Intent(this, WeiShopWebActivity.class);
				intent.putExtra(AppTag.TAG_ID, m.group());
				startActivity(intent);
				finish();
				return;
			}

		}

		// 判断是否为网页url
		if (result.matches("[a-zA-z]+://[^\\s]*")) {

			if (result.indexOf("/news") != -1) {
				Pattern pattern = Pattern.compile("[0-9]{4,}");
				Matcher matcher = pattern.matcher(result);
				if (matcher.find()) {
					Intent intent = new Intent(CodeActivity.this,
							WebDetailActivity.class);
					String url = ApiUrl.DETAIL_NEWS + matcher.group();
					intent.putExtra("WEB_URL", url);
					intent.putExtra(WebDetailActivity.GUID, "");
					startActivity(intent);
					finish();
					return;
				}

			}

			if (result.indexOf("/works") != -1) {
				Pattern pattern = Pattern.compile("[0-9]{4,}");
				Matcher matcher = pattern.matcher(result);
				if (matcher.find()) {
					Intent intent = new Intent(CodeActivity.this,
							WebDetailActivity.class);
					String url = ApiUrl.DETAIL_CASE2 + matcher.group();
					intent.putExtra("WEB_URL", url);
					startActivity(intent);
					finish();
					return;
				}

			}
			Intent intent = new Intent(CodeActivity.this, WebActivity.class);
			intent.putExtra(WebActivity.URL, result);
			startActivity(intent);
			finish();
			return;
		}

		if (result.matches("[1-9][0-9]{6,15}")) {
			Intent intent = new Intent(CodeActivity.this, WebActivity.class);
			intent.putExtra(WebActivity.URL,
					"http://wap.sogou.com/web/searchList.jsp?&keyword="
							+ result);
			startActivity(intent);
			finish();
			return;
		}

		MyAlertDialog myDialog = new MyAlertDialog(this);
		myDialog.setTitle("扫描的结果").setContent(rawResult.getText())
				.setBtnOk("复制").setBtnCancel("取消")
				.setBtnOnListener(new DialogOnListener() {

					@Override
					public void onClickListener(boolean isOk) {
						if (isOk) {
							// 得到剪贴板管理器
							ClipboardManager cmb = (ClipboardManager) CodeActivity.this
									.getSystemService(Context.CLIPBOARD_SERVICE);
							cmb.setText(rawResult.getText().toString());
							finish();
						} else {
							restartPreviewAfterDelay(0L);
						}

					}
				}).show();
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// if (barcode != null) {
		// Drawable drawable = new BitmapDrawable(barcode);
		// builder.setIcon(drawable);
		// }
		// "类型:" + rawResult.getBarcodeFormat() +
		// builder.setTitle("扫描的结果");
		// builder.setMessage(rawResult.getText());
		// builder.setPositiveButton("复制", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		//
		// // 得到剪贴板管理器
		// ClipboardManager cmb = (ClipboardManager) CodeActivity.this
		// .getSystemService(Context.CLIPBOARD_SERVICE);
		// cmb.setText(rawResult.getText().toString());
		// dialog.dismiss();
		// finish();
		// }
		// });
		// builder.setNegativeButton("重新扫描", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// restartPreviewAfterDelay(0L);
		// }
		// });
		// builder.setCancelable(false);
		// builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (mCaptureHandler != null) {
			mCaptureHandler.sendEmptyMessageDelayed(MessageIDs.restart_preview,
					delayMS);
		}
	}

	private void initBeepSound() {
		if (mPlayBeep && mMediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(beepListener);

			try {
				AssetFileDescriptor fileDescriptor = getAssets().openFd(
						"qrbeep.ogg");
				this.mMediaPlayer.setDataSource(
						fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());
				this.mMediaPlayer.setVolume(0.1F, 0.1F);
				this.mMediaPlayer.prepare();
			} catch (IOException e) {
				this.mMediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (mPlayBeep && mMediaPlayer != null) {
			mMediaPlayer.start();
		}
		if (mVibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	private String photo_path;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {

			case REQUEST_CODE:

				String[] proj = { MediaStore.Images.Media.DATA };
				// 获取选中图片的路径
				Cursor cursor = getContentResolver().query(data.getData(),
						proj, null, null, null);

				if (cursor.moveToFirst()) {

					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					photo_path = cursor.getString(column_index);
					if (photo_path == null) {
						photo_path = FileUtil.getPath(getApplicationContext(),
								data.getData());
					}

				}

				cursor.close();
				// if (handler == null) {
				// handler = new MyHandler();
				// }
				new Thread(new Runnable() {

					@Override
					public void run() {

						Result result = scanningImage(photo_path);

						if (result == null) {
							Looper.prepare();
							new MyAlertDialog(CodeActivity.this)
									.setTitle("无法识别该图片").setBtnOk("确定").show();
							Looper.loop();
						} else {
							// 数据返回
							Looper.prepare();
							showResult(result, null);
							Looper.loop();

						}
					}
				}).start();
				break;

			}

		}

	}

	// MyHandler handler;
	//
	// class MyHandler extends Handler {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// // TODO Auto-generated method stub
	// super.handleMessage(msg);
	// }
	// }

}
