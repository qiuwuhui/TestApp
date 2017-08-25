package com.baolinetworktechnology.shejiquan.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.adapter.HistoryAdapter;
import com.baolinetworktechnology.shejiquan.adapter.ViewPagerAdapter;
import com.baolinetworktechnology.shejiquan.constant.AppStatusConstant;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.MarkName;
import com.baolinetworktechnology.shejiquan.db.TagsManager;
import com.baolinetworktechnology.shejiquan.view.KoyTab;
import com.baolinetworktechnology.shejiquan.view.KoyTab.OnTabChangeListener;
import com.baolinetworktechnology.shejiquan.view.ListViewCase;
import com.baolinetworktechnology.shejiquan.view.ListViewDesigner;
import com.baolinetworktechnology.shejiquan.view.ListViewNews;
import com.baolinetworktechnology.shejiquan.view.ListViewzhuangxiu;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog;
import com.baolinetworktechnology.shejiquan.view.MyAlertDialog.DialogOnListener;
import com.baolinetworktechnology.shejiquan.view.NoScrollViewPager;
import com.guojisheng.koyview.MyEditText;

/**
 * 搜索模块
 * 
 * @author JiSheng.Guo
 * 
 */
public class SerchActivity extends BaseActivity implements OnClickListener
		 {
	private MyEditText mMyEtSerch;
	private KoyTab mSerchTab;
	private TextView mTvTitle;
	private NoScrollViewPager mViewPage;
	private ViewPagerAdapter mViewPageAdapter;
	private View mItemFooter;
	private ListViewDesigner mDesignerListView;
	private ListViewzhuangxiu mZhuangxiuListView;
	private ListViewNews mListViewNews;
	private ListViewCase mListViewCase;
	private ListView mListView;// 历史记录
	private TagsManager mTagsManager;// 历史记录 数据管理者
	private List<String> mListHistoryData;
	private HistoryAdapter mArrayAdapter;// 历史记录ListView 适配器
	private boolean isFirst = true;

	// private int mRecoInitSucc = 0;
	// 表示目前所处的状态 0:空闲状态，可进行识别； 1：正在进行录音; 2：者处于语音识别; 3：处于取消状态
	// private int mRecoState = 0;
	// private mRecognizerResult;// 识别结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.aty_slide_bottom_in, R.anim.alpha1);
		setContentView(R.layout.activity_serch);
		mTagsManager = TagsManager.getInstance(this);
		mMyEtSerch = (MyEditText) findViewById(R.id.myEditText);
		mMyEtSerch.setFocusable(true);
		mMyEtSerch.setFocusableInTouchMode(true);
		mMyEtSerch.requestFocus();
		mListView = (ListView) findViewById(R.id.historyListView);
		mViewPage = (NoScrollViewPager) findViewById(R.id.viewPage);
		mItemFooter = findViewById(R.id.itemFooter);
		mSerchTab = (KoyTab) findViewById(R.id.serchTab);
		mTvTitle = (TextView) findViewById(R.id.title_serch);
		mTvTitle.setOnClickListener(this);
		mItemFooter.setVisibility(View.GONE);

		mSerchTab.setTabText(0, "作品");
		mSerchTab.setTabText(1, "设计师");
//		mSerchTab.setTabText(1, "装修公司");
		mSerchTab.setTabText(2, "资讯");
		mSerchTab.setShowDivid();
		List<View> views = new ArrayList<View>();

		View designerView = View.inflate(this, R.layout.view_designer_list,
				null);
//		View designerzxView = View.inflate(this, R.layout.view_designer_zhuanx,
//				null);
		View designerCaseView = View.inflate(this, R.layout.view_designer_case,
				null);
		View designerNewsView = View.inflate(this, R.layout.view_designer_news,
				null);

		mListViewCase = (ListViewCase) designerCaseView
				.findViewById(R.id.listViewCase);
		mListViewCase.setNullDataTips("抱歉，没有找到相关的作品");
		mListViewCase.setNullDdrawable(R.drawable.list_null_data);
		mDesignerListView = (ListViewDesigner) designerView
				.findViewById(R.id.designerListView);
		mDesignerListView.setNullDataTips("抱歉，没有找到相关的设计师");
		mDesignerListView.setNullDdrawable(R.drawable.list_null_data);
//		mZhuangxiuListView = (ListViewzhuangxiu) designerzxView
//				.findViewById(R.id.zhuanxiuListView);
//		mZhuangxiuListView.setNullDataTips("抱歉，没有找到您要的内容");


		mListViewNews = (ListViewNews) designerNewsView
				.findViewById(R.id.listViewNews);
		mListViewNews.setNullDataTips("抱歉，没有找到您要的资讯");


		views.add(designerCaseView);
		views.add(designerView);
		views.add(designerNewsView);
//		views.add(designerzxView);

		mViewPageAdapter = new ViewPagerAdapter(views);
		mViewPage.setAdapter(mViewPageAdapter);

		mMyEtSerch
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							doSerch(mSerchTab.getCurrtent());
							addHistory();
							return true;
						}
						return false;
					}
				});

		mMyEtSerch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (findViewById(R.id.itemHistory).getVisibility() != View.VISIBLE) {
					findViewById(R.id.itemHistory).setVisibility(View.VISIBLE);
					mTvTitle.setVisibility(View.VISIBLE);
					mTvTitle.setText("取消");
				}
			}
		});
		mArrayAdapter = new HistoryAdapter();
		mListHistoryData = mTagsManager.getListTags();
		if (mListHistoryData.size() == 0) {
			findViewById(R.id.historyNull).setVisibility(View.VISIBLE);
			// findViewById(R.id.historyClean).setVisibility(View.GONE);
		} else {
			// View view = View.inflate(this, R.layout.view_clear, null);
			// view.setOnClickListener(this);
			// mListView.addFooterView(view);
		}
		findViewById(R.id.ivClear).setOnClickListener(this);
		mArrayAdapter.setData(mListHistoryData);
		mListView.setAdapter(mArrayAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mMyEtSerch.setText((String) mArrayAdapter.getItem(position));

				doSerch(mSerchTab.getCurrtent());

			}
		});
		mSerchTab.setOnTabChangeListener(new OnTabChangeListener() {

			@Override
			public void onTabChange(RadioGroup group, int index) {

				mViewPage.setCurrentItem(index);
				doSerch(index);
			}
		});
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
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ivClear:
			new MyAlertDialog(this).setBtnCancel("取消").setTitle("是否清空搜索历史记录？")
					.setBtnOk("清空").setBtnOnListener(new DialogOnListener() {

						@Override
						public void onClickListener(boolean isOk) {
							if (isOk) {
								mListHistoryData.clear();
								mTagsManager.clearTags();
								mArrayAdapter.notifyDataSetChanged();
							}

						}
					}).show();

			break;
		case R.id.title_serch:

			if (mTvTitle.getText().toString().equals("取消")) {
				hideInput();
				findViewById(R.id.itemHistory).setVisibility(View.GONE);
				mTvTitle.setText("搜索");
				mTvTitle.setVisibility(View.VISIBLE);
			} else {
				mTvTitle.setText("取消");
				String KeyWord = mMyEtSerch.getText().toString();
				if (KeyWord.length() < 1) {
					toastShow("请输入关键词");
					return;
				}
				// serchHistory.addTags(KeyWord);
				addHistory();
				doSerch(mSerchTab.getCurrtent());
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mTvTitle.getText().toString().equals("取消")) {
			findViewById(R.id.itemHistory).setVisibility(View.GONE);
			mTvTitle.setText("搜索");
			mTvTitle.setVisibility(View.INVISIBLE);
		} else {

			super.onBackPressed();
		}

	}

	// 添加到搜索记录
	private void addHistory() {
		String KeyWord = mMyEtSerch.getText().toString();
		if (!KeyWord.trim().equals("")) {
			mListHistoryData.clear();
			mTagsManager.addTag(KeyWord);
			mListHistoryData.addAll(mTagsManager.getListTags());
			mArrayAdapter.notifyDataSetChanged();
		}
		if (findViewById(R.id.historyNull).getVisibility() != View.GONE)
			findViewById(R.id.historyNull).setVisibility(View.GONE);
	}

	Handler mHandler;

	// 搜索功能
	void doSerch(final int i) {
		String KeyWord = mMyEtSerch.getText().toString();
		if (KeyWord.length() < 1) {
			toastShow("请输入关键词");
			return;
		}
		hideInput();
		if (findViewById(R.id.itemHistory).getVisibility() != View.GONE) {
			findViewById(R.id.itemHistory).setVisibility(View.GONE);
			mTvTitle.setText("搜索");
		}
		if (mItemFooter.getVisibility() != View.VISIBLE) {
			mItemFooter.setVisibility(View.VISIBLE);
			if (isFirst) {
				int index = getIntent().getIntExtra(AppTag.TAG_ID, 0);
				mSerchTab.setTab(index);
				isFirst = false;
				if (index == 0)
					startSerch(i);

			} else {
				startSerch(i);
			}

		} else {
			startSerch(i);
		}
		// startSerch(i);

	}

	private void startSerch(int i) {
		String KeyWord = mMyEtSerch.getText().toString();
		switch (i) {
		case 0:
			if(KeyWord.equals("")){
				toastShow("请输入关键词");
			}else {
				mListViewCase.setUrlParams(KeyWord);
			}
			break;
			case 1:
				if(KeyWord.equals("")){
					toastShow("请输入关键词");
				}else{
				mDesignerListView.setUrlParams(KeyWord);
//				mZhuangxiuListView.setUrlParams(KeyWord);
			    }
			break;
//		case 2:
//				mListViewCase.setUrlParams(KeyWord);
//			break;
		case 2:
			if(KeyWord.equals("")){
				toastShow("请输入关键词");
			}else {
				mListViewNews.setCaceKey("");
				mListViewNews.setUrlParams(KeyWord);
			}
			break;
		}

	}

	// Tab 选项事件
	// @Override
	// public void onTagClick(String tag) {
	// mMyEtSerch.setText(tag);
	// doSerch(0);
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Gson gson = new Gson();
		// 数据保存
		// CacheUtils.cacheStringData(this,
		// "Serch",gson.toJson(SerchBean.getInstance(this)));
	}

//	@Override
//	public void onGetError(int arg0) {
//		if (arg0 != 0) {
//			dialog.dismiss();
//		}
//		switch (arg0) {
//		case 0:
//
//			break;
//		case -201:
//			toastShow("没有检测到网络，请检查网络后再试");
//			break;
//		case -301:
//			toastShow("没有听到您的声音");
//			break;
//		case -302:
//			toastShow("您的语音过长");
//			break;
//		case -304:
//			toastShow("无法使用麦克风");
//			break;
//		default:
//			toastShow("获取语音错误");
//			break;
//		}
//
//	}

//	@Override
//	public void onGetResult(VoiceRecognizerResult result) {
//		String mRecognizerResult = "";
//		if (result != null && result.words != null) {
//			int wordSize = result.words.size();
//			StringBuilder results = new StringBuilder();
//			for (int i = 0; i < wordSize; ++i) {
//				Word word = (Word) result.words.get(i);
//				if (word != null && word.text != null) {
//					results.append(word.text.replace(" ", ""));
//					results.append("\r\n");
//				}
//			}
//			mRecognizerResult = results.toString().replace("。", "");
//		}
//
//		mMyEtSerch.setText(mRecognizerResult);
//		dialog.dismiss();
//		doSerch(mSerchTab.getCurrtent());
//	}
//
//	@Override
//	public void onGetVoiceRecordState(VoiceRecordState arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onVolumeChanged(int arg0) {
//		// TODO Auto-generated method stub
//	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.alpha1, R.anim.aty_slide_bottom_out);
	}
}
