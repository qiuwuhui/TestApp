package com.baolinetworktechnology.shejiquan.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.activity.AdviceActivity;
import com.baolinetworktechnology.shejiquan.activity.BoundActivity;
import com.baolinetworktechnology.shejiquan.activity.LoginActivity;
import com.baolinetworktechnology.shejiquan.activity.MainActivity;
import com.baolinetworktechnology.shejiquan.activity.MeDetailInfoActivity;
import com.baolinetworktechnology.shejiquan.activity.MyCollectionActivity;
import com.baolinetworktechnology.shejiquan.activity.MyIntegralActivity;
import com.baolinetworktechnology.shejiquan.activity.MyPostDetailsActivity;
import com.baolinetworktechnology.shejiquan.activity.MyanliActivity;
import com.baolinetworktechnology.shejiquan.activity.OwnerDetailInfoActivity;
import com.baolinetworktechnology.shejiquan.activity.RecordActivity;
import com.baolinetworktechnology.shejiquan.activity.ShenjiActivity;
import com.baolinetworktechnology.shejiquan.activity.TueiJianActivity;
import com.baolinetworktechnology.shejiquan.activity.WeiShopActivity;
import com.baolinetworktechnology.shejiquan.activity.WeizxActivity;
import com.baolinetworktechnology.shejiquan.activity.ZxDetailInfoActivity;
import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.constant.EnumDientity;
import com.baolinetworktechnology.shejiquan.domain.ReadMessageBean;
import com.baolinetworktechnology.shejiquan.fragment.MainMyFragment.MyFragmentFace;
import com.baolinetworktechnology.shejiquan.interfaces.AppLoadListener;
import com.baolinetworktechnology.shejiquan.manage.CityService;
import com.baolinetworktechnology.shejiquan.net.NetMessage;
import com.baolinetworktechnology.shejiquan.net.OnCallBackBean;
import com.baolinetworktechnology.shejiquan.utils.CacheUtils;
import com.baolinetworktechnology.shejiquan.utils.ShareUtils;
import com.guojisheng.koyview.GScrollView;
import com.guojisheng.koyview.GScrollView.IRefresh;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的界面
 *
 * @author JiSheng.Guo
 */
public class MineView extends FrameLayout implements OnClickListener, IRefresh {

    private CircleImg mUserLogo;
    private TextView mTvName;
    private TextView mTvUserId;
    private TextView mTvNumView;
    private TextView mTvNumConcer;
    private TextView mNumFans;
    private TextView IsCerti;
    private TextView isOwner;
    private TextView tvFromCity;
    private ImageView mHeadBg;
    private BitmapUtils mBitmapUtil;
    private MyFragmentFace mMyFragmentFace;
    private TextView message_msg_num;
    int dienID = 0;// 身份ID 1业主 2设计师 3装修公司
    private GScrollView reboundScrollView;
    ShareUtils mShareUtils;

    public MineView(Context context, MyFragmentFace mMyFragmentFace) {
        super(context);
        initImageUtil();
        this.mMyFragmentFace = mMyFragmentFace;
    }

    boolean isRead = true;

    // 初始化图片工具
    private void initImageUtil() {
        if (mBitmapUtil == null) {
            mBitmapUtil = new BitmapUtils(getContext());
            mBitmapUtil.configDefaultCacheExpiry(60 * 1000 * 60 * 24 * 7);
            mBitmapUtil.configDefaultLoadingImage(R.drawable.icon_morentxy);
            mBitmapUtil.configDefaultLoadFailedImage(R.drawable.icon_morentxy);
            mBitmapUtil.configDefaultBitmapConfig(Bitmap.Config.RGB_565);// 设置图片压缩类型
        }

    }

    public int getDienId() {
        return dienID;
    }

    /**
     * 设置身份信息
     *
     * @param dien
     */
    public void setDidentity(EnumDientity dien) {
        String str = android.os.Build.MODEL + "";
        if (SJQApp.user == null) {
            View view_design;
            if (str.contains("FRD-AL")) {//荣耀8显示问题
                view_design = View.inflate(getContext(),
                        R.layout.fragment_zhuanxiu2, null);

            } else {
                view_design = View.inflate(getContext(),
                        R.layout.fragment_zhuanxiu, null);
            }


            initGUI(view_design);
            dienID = 0;
            return;
        }
        switch (dien) {
            case USER_OWNER:
                if (dienID != 1) {
                    View view_owner;
                    if (str.contains("FRD-AL")){
                        view_owner = View.inflate(getContext(),
                                R.layout.fragment_owner2, null);
                        Log.e("TAGOWNER2","-------");
                    }else {
                        view_owner = View.inflate(getContext(),
                                R.layout.fragment_owner, null);
                    }
                    initOwnerUI(view_owner);
                    dienID = 1;
                }
                break;
            case USER_DESIGNER:
                if (dienID != 2) {
                    View view_design;
                    if (str.contains("FRD-AL")){
                        view_design = View.inflate(getContext(),
                                R.layout.fragment_my2, null);
                        Log.e("TAGDESIGNER2","-------");
                    }else {
                        view_design = View.inflate(getContext(),
                                R.layout.fragment_my, null);
                    }
                    initDesignUI(view_design);
                    dienID = 2;
                }
                break;
            default:
                break;
        }

    }

    // 业主UI
    private void initOwnerUI(View view) {
        removeAllViews();
        mUserLogo = (CircleImg) view.findViewById(R.id.owner_iv_head);// 头像
        mTvName = (TextView) view.findViewById(R.id.sf_tv_name);// 姓名
        mTvUserId = (TextView) view.findViewById(R.id.sf_tv_uid);// uid
        isOwner = (TextView) view.findViewById(R.id.isOwner);// 是否绑定手机号
        message_msg_num = (TextView) view.findViewById(R.id.message_msg_num);// 显示未读
        mHeadBg = (ImageView) view.findViewById(R.id.my_bg);// 背景
        mUserLogo.setOnClickListener(this);
        view.findViewById(R.id.item_my_collect).setOnClickListener(this);// 我的收藏
        view.findViewById(R.id.itemConcerOwner).setOnClickListener(this);// 我的关注
        view.findViewById(R.id.sf_rl_advice).setOnClickListener(this);// 反馈
        view.findViewById(R.id.item_share).setOnClickListener(this);// 推荐给好友
        view.findViewById(R.id.sf_rl_setting).setOnClickListener(this);// 设置
        view.findViewById(R.id.head).setOnClickListener(this);// 登录
        view.findViewById(R.id.sf_rl_bound).setOnClickListener(this);// 绑定手机号
        view.findViewById(R.id.item_my_info).setOnClickListener(this);// 业主个人资料
        view.findViewById(R.id.item_my_post).setOnClickListener(this);// 业主我的帖子
        view.findViewById(R.id.item_my_integral).setOnClickListener(this);// 我的积分
        view.findViewById(R.id.dele_shiming).setOnClickListener(this);// 隐藏绑定
        reboundScrollView = (GScrollView) view
                .findViewById(R.id.reboundScrollView);
        reboundScrollView.setImageView(mHeadBg);
        reboundScrollView.setOnRefreshListener(this);
        addView(view);
        setReadMag();
    }

    // 初始化设计师UI
    private void initDesignUI(View view) {
        removeAllViews();
        mUserLogo = (CircleImg) view.findViewById(R.id.sf_iv_user);
        mTvName = (TextView) view.findViewById(R.id.sf_tv_name);
        mTvUserId = (TextView) view.findViewById(R.id.sf_tv_uid);
        tvFromCity = (TextView) view.findViewById(R.id.tvFromCity);
        mTvNumView = (TextView) view.findViewById(R.id.numView);
        mTvNumConcer = (TextView) view.findViewById(R.id.numConcer);
        mNumFans = (TextView) view.findViewById(R.id.numFans);
        message_msg_num = (TextView) view.findViewById(R.id.message_msg_num);// 显示未读
        mUserLogo.setOnClickListener(this);
        IsCerti = (TextView) view.findViewById(R.id.isCerti);
        view.findViewById(R.id.itemRecord).setOnClickListener(this);
        view.findViewById(R.id.itemNumConcer).setOnClickListener(this);
        view.findViewById(R.id.itemNumFans).setOnClickListener(this);
        view.findViewById(R.id.sf_rl_setting).setOnClickListener(this);
        view.findViewById(R.id.sf_rl_advice).setOnClickListener(this);
        view.findViewById(R.id.item_share).setOnClickListener(this);
        view.findViewById(R.id.sf_rl_mingpian).setOnClickListener(this);
        view.findViewById(R.id.sf_rl_collect).setOnClickListener(this);
        view.findViewById(R.id.sf_rl_opus).setOnClickListener(this);
        view.findViewById(R.id.sf_rl_post).setOnClickListener(this);
        view.findViewById(R.id.sf_rl_add).setOnClickListener(this);
        view.findViewById(R.id.sf_tv_weishop).setOnClickListener(this);
        view.findViewById(R.id.head).setOnClickListener(this);
        view.findViewById(R.id.dele_shiming).setOnClickListener(this);// 隐藏提示
        view.findViewById(R.id.sf_ren_zhen).setOnClickListener(this);// 设计师认证提示
        view.findViewById(R.id.item_my_integral).setOnClickListener(this);// 我的积分
        reboundScrollView = (GScrollView) view
                .findViewById(R.id.reboundScrollView);
        mHeadBg = (ImageView) view.findViewById(R.id.logoBg);
        reboundScrollView.setImageView(mHeadBg);
        reboundScrollView.setOnRefreshListener(this);
        addView(view);
        setReadMag();
    }

    // 装修公司UI
    private void initGUI(View view) {
        removeAllViews();
        view.findViewById(R.id.sf_rl_setting).setOnClickListener(this);//设置
        view.findViewById(R.id.sf_rl_advice).setOnClickListener(this);//意见
        view.findViewById(R.id.item_share).setOnClickListener(this);//推荐
        view.findViewById(R.id.item_tv_login).setOnClickListener(this);// 登录
        reboundScrollView = (GScrollView) view
                .findViewById(R.id.reboundScrollView);
        mHeadBg = (ImageView) view.findViewById(R.id.logoBg);
        reboundScrollView.setImageView(mHeadBg);
        reboundScrollView.setOnRefreshListener(this);
        addView(view);

    }

    // 设置数据
    public void setData() {
        switch (dienID) {
            case 1:
                initOwner();
                break;
            case 2:
                initDesignData();
                break;
            case 3:
                initZhuanxiuData();
                break;
            default:
                break;
        }
    }

    //初始化装修公司信息
    private void initZhuanxiuData() {
        if (SJQApp.user != null) {
            if (SJQApp.ZhuanxiuData != null) {
                mTvNumView.setText(SJQApp.ZhuanxiuData.getNumVisit() + "");
                mTvNumConcer.setText(SJQApp.ZhuanxiuData.getNumRecommend() + "");
                mNumFans.setText(SJQApp.ZhuanxiuData.getFollowersCount() + "");
                mBitmapUtil.display(mUserLogo, SJQApp.ZhuanxiuData.getLogo());
            } else {
                ((SJQApp) (getActivity().getApplicationContext())).loadData();
                ((SJQApp) (getActivity().getApplicationContext()))
                        .setOnAppLoadListener(new AppLoadListener() {
                            @Override
                            public void onAppLoadListener(boolean loadState) {
                                if (loadState) {
                                    initZhuanxiuData();
                                }
                            }
                        });
            }
        } else {
            initLogoutUI();
        }
    }

    // 初始化业主信息
    private void initOwner() {
        if (SJQApp.user != null) {
            if (mTvUserId.getVisibility() != View.VISIBLE) {
                mTvUserId.setVisibility(View.VISIBLE);
            }
            if (SJQApp.ownerData != null) {
                showOwnerData();
            } else {
                ((SJQApp) (getActivity().getApplicationContext())).loadData();
                ((SJQApp) (getActivity().getApplicationContext()))
                        .setOnAppLoadListener(new AppLoadListener() {
                            @Override
                            public void onAppLoadListener(boolean loadState) {
                                if (loadState) {
                                    showOwnerData();
                                }
                            }
                        });
            }
        } else {
            initLogoutUI();
        }

    }

    // 显示业主账户
    private void showOwnerData() {

        if (SJQApp.ownerData != null) {
            if (mUserLogo.getTag() == null) {
                mUserLogo.setTag("");
            }
            if (!mUserLogo.getTag().toString()
                    .equals(SJQApp.ownerData.getLogo())) {
                mBitmapUtil.display(mUserLogo, SJQApp.ownerData.getLogo());
                mUserLogo.setTag(SJQApp.ownerData.getLogo());
            }
            if (SJQApp.user.isBindMobile) {// 绑定手机号;
                isOwner.setText("");
                findViewById(R.id.shiming).setVisibility(View.GONE);
            } else {// 未绑定
                isOwner.setText("绑定手机");
                findViewById(R.id.shiming).setVisibility(View.VISIBLE);
            }
            mTvName.setText(SJQApp.ownerData.getName());
        }
    }

    // 初始化设计师账户
    private void initDesignData() {
        if (SJQApp.user != null) {

            if (mTvUserId.getVisibility() != View.VISIBLE) {
                mTvName.setText(SJQApp.user.nickName);
                mTvUserId.setVisibility(View.VISIBLE);
                findViewById(R.id.sf_tv_weishop).setVisibility(View.VISIBLE);
            }

            if (SJQApp.userData != null) {

                showDesignData();
            } else {
                loadingDataDes();

            }
        } else {
            initLogoutUI();

        }
    }

    // 联网获取设计师信息
    private void loadingDataDes() {
        SJQApp app = ((SJQApp) (getActivity().getApplicationContext()));
        if (SJQApp.user != null && !app.isLoading()) {
            app.loadData();
            app.setOnAppLoadListener(new AppLoadListener() {

                @Override
                public void onAppLoadListener(boolean loadState) {

                    if (loadState) {
                        showDesignData();
                    }

                }
            });
        }
    }

    // 初始化未登录状态
    private void initLogoutUI() {

        if (SJQApp.user == null) {
//		if (mTvUserId.getVisibility() == View.GONE) {
            // 已经恢复过了

            return;
        }

        if (dienID == 1) {// 业主


        } else if (dienID == 2) {// 设计师
            mNumFans.setText("" + 0);
            mTvNumConcer.setText("" + 0);
            mTvNumView.setText("" + 0);
            findViewById(R.id.sf_tv_weishop).setVisibility(View.GONE);
        }
        mUserLogo.setImageResource(R.drawable.icon_morentxy);
        mUserLogo.setTag("");
        mTvUserId.setVisibility(View.GONE);
    }

    // 显示设计师信息
    private void showDesignData() {
        if (SJQApp.userData != null) {
            System.out.println("------设计师登录信息显示------");
            if (SJQApp.userData.getGuid() == null
                    || SJQApp.userData.getGuid()
                    .equals("00000000-0000-0000-0000-000000000000"))
                return;
            if (mTvUserId == null)
                return;
            // 显示设计师头像
            if (!SJQApp.userData.logo.equals(mUserLogo.getTag())) {
                mBitmapUtil.display(mUserLogo, SJQApp.userData.getLogo());
                mUserLogo.setTag(SJQApp.userData.getLogo());
            }
            if (SJQApp.userData.getRenzheng().equals("认证")) {
                IsCerti.setText("认证");
            } else if (SJQApp.userData.getRenzheng().equals("未认证")) {
                IsCerti.setText("未认证");
                findViewById(R.id.shiming).setVisibility(View.VISIBLE);
            } else if (SJQApp.userData.getRenzheng().equals("审核中")) {
                IsCerti.setText("审核中");
                findViewById(R.id.shiming).setVisibility(View.GONE);
            } else if (SJQApp.userData.getRenzheng().equals("已认证")) {
                IsCerti.setText("已认证");
                findViewById(R.id.shiming).setVisibility(View.GONE);
            }
            mTvName.setText(SJQApp.userData.getName());
            if (SJQApp.userData.provinceID == 0 && SJQApp.userData.cityID == 0) {
                tvFromCity.setText(CacheUtils.getStringData(getActivity(),
                        "weizhi", " "));
            } else {
                tvFromCity.setText(getfromCity(SJQApp.userData.provinceID, SJQApp.userData.cityID));
            }
            mNumFans.setText("" + SJQApp.userData.getNumFans());
            mTvNumConcer.setText("" + SJQApp.userData.getNumShare());
            mTvNumView.setText("" + SJQApp.userData.getNumView());
        }

    }

    //获取设计师所在地区
    private String getfromCity(int ProvinceID, int CityID) {
        CityService cityService = CityService.getInstance(getActivity());
        if (cityService == null)
            return null;
        String shi = cityService.fromCity(ProvinceID, CityID);
        return shi;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId() == R.id.sf_rl_setting) {
            mMyFragmentFace.setting();

            return;
        }
        if (v.getId() == R.id.sf_rl_advice) {
            // 建议反馈
//			intent.setClass(getActivity(), MaozxActivity.class);
//    		intent.setClass(getActivity(), SelectiDentityActivity.class);
            intent.setClass(getActivity(), AdviceActivity.class);
            getContext().startActivity(intent);
            return;

        }
        if (v.getId() == R.id.dele_shiming) {
            //关闭提示
            findViewById(R.id.shiming).setVisibility(View.GONE);
            return;

        }
        if (v.getId() == R.id.item_share) {
            if (SJQApp.user == null) {
                MobclickAgent.onEvent(getActivity(), "kControlUserCenterRecomment", "noUser");//个人中心推荐给朋友按钮  未登录
            } else if (dienID == 1) {
                MobclickAgent.onEvent(getActivity(), "kControlUserCenterRecomment", "owner");//个人中心推荐给朋友按钮   业主
            } else if (dienID == 2) {
                MobclickAgent.onEvent(getActivity(), "kControlUserCenterRecomment", "design");//个人中心推荐给朋友按钮  设计师
            }
            mMyFragmentFace.doShare();
            return;
        }
        if (SJQApp.user == null) {

            intent.setClass(getActivity(), LoginActivity.class);
            intent.putExtra(LoginActivity.IS_SPLASH, true);
            getActivity().startActivity(intent);
            return;
        }

        switch (v.getId()) {
            case R.id.item_my_collect:
            case R.id.sf_rl_collect:// 我的收藏
                intent.setClass(getActivity(), MyCollectionActivity.class);
//				intent.setClass(getActivity(), TueiJianActivity.class);
//				intent.putExtra("TYPE", 2);
                break;
//		case R.id.itemNumFans:// 我的粉丝
//			intent.setClass(getActivity(), DesignerListActivity.class);
//			intent.putExtra("GUID", SJQApp.user.UserGUID);
//			intent.putExtra(DesignerListActivity.ISOWNER, true);
//			intent.putExtra("TITLE", "我的粉丝");
//			intent.putExtra("ISFANS", true);
//			break;
            case R.id.itemConcerOwner:// 业主关注
//		case R.id.itemNumConcer:// 我的关注
                intent.setClass(getActivity(), TueiJianActivity.class);
                intent.putExtra("TYPE", 3);
                break;
            // 个人资料
            case R.id.item_my_info:
                MobclickAgent.onEvent(getActivity(), "kControlPersonCenterEditInfo");//业主个人中心编辑装修资料点击事件：已登录状态
                intent.setClass(getActivity(), OwnerDetailInfoActivity.class);
                break;
            case R.id.sf_rl_mingpian:
            case R.id.owner_iv_head:
            case R.id.sf_iv_user:
                if (dienID == 1) {
                    MobclickAgent.onEvent(getActivity(), "kControlPersonCenterLogo"); //业主个人中心头像点击事件：已登录状态
                    intent.setClass(getActivity(), OwnerDetailInfoActivity.class);
                } else if (dienID == 2) {
                    MobclickAgent.onEvent(getActivity(), "kControlDesignerCenterLogo"); //设计师个人中心头像点击
                    intent.setClass(getActivity(), MeDetailInfoActivity.class);
                } else {
                    intent.setClass(getActivity(), ZxDetailInfoActivity.class);
                }
                break;
            case R.id.sf_rl_bound:// 绑定手机号
                intent.putExtra(AppTag.TAG_JSON, SJQApp.user.getMobile());
                intent.setClass(getActivity(),
                        BoundActivity.class);
                break;
            case R.id.sf_ren_zhen://设计师认证
                intent.setClass(getActivity(), ShenjiActivity.class);
                break;
            case R.id.sf_rl_advice:// 建议反馈
                intent.setClass(getActivity(), AdviceActivity.class);
                break;
            case R.id.sf_rl_opus:// 我的作品
                intent.setClass(getActivity(), MyanliActivity.class);
                break;
            case R.id.item_my_integral:// 积分
                intent.setClass(getActivity(), MyIntegralActivity.class);
                break;
//			case R.id.itemRecord:// 我的访客
//				intent.setClass(getActivity(), RecordActivity.class);
//				intent.putExtra(RecordActivity.IS_Me, true);
//				intent.putExtra(RecordActivity.ALL_RECORD, mTvNumView.getText()
//						.toString());
//				break;
            case R.id.sf_rl_post:
            case R.id.item_my_post:// 我的帖子
                intent.setClass(getActivity(), MyPostDetailsActivity.class);
                break;
            case R.id.sf_rl_add:// 上传作品
                intent.setClass(getActivity(), TueiJianActivity.class);
                intent.putExtra("TYPE", 3);
//			intent.setClass(getActivity(), HelpActivity.class);
                break;
            case R.id.sf_tv_weishop://微名片
                if (dienID == 2) {
                    MobclickAgent.onEvent(getActivity(), "kControlDesignerCenterMicro"); //设计师个人中心微名片按钮点击
                    intent.setClass(getActivity(), WeiShopActivity.class);
                    intent.putExtra("IS_MY", false);
                    intent.putExtra(AppTag.TAG_GUID, SJQApp.user.guid);
                    intent.putExtra(AppTag.TAG_JSON, SJQApp.user.toString());
                } else if (dienID == 3) {
                    intent.setClass(getActivity(), WeizxActivity.class);
                    intent.putExtra("businessID", SJQApp.ZhuanxiuData.getID() + "");
                    intent.putExtra("GUID", SJQApp.user.guid);
                }
                break;
            default:
                return;
        }
        getActivity().startActivity(intent);

    }

    public void setReadMag() {
        if (SJQApp.messageBean != null && SJQApp.messageBean.getResult() != null) {
            int UnreadMsgCount = 0;
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
            for (Pair<Long, EMConversation> sortItem : sortList) {
                EMConversation emConversation = sortItem.second;
                if (!emConversation.getUserName().equals("sjq-kefu")) {
                    UnreadMsgCount += emConversation.getUnreadMsgCount();
                }
            }
            int numMessage = SJQApp.messageBean.getResult().getNumMessage();
            int numComment = SJQApp.messageBean.getResult().getNumComment();
            int numPostsGood = SJQApp.messageBean.getResult().getNumPostsGood();
            int msgnum = UnreadMsgCount + numMessage + numComment + numPostsGood;
            if (message_msg_num != null) {
                if (msgnum == 0) {
                    message_msg_num.setVisibility(View.GONE);
                } else {
                    message_msg_num.setVisibility(View.VISIBLE);
                    message_msg_num.setText(msgnum + "");
                }
            }
        }
    }

    private Context getActivity() {
        return getContext();
    }

    @Override
    public void onRefresh(int mInnerMoveH) {
        if (mInnerMoveH > 240) {
            if (dienID == 2) {
                loadingDataDes();
            }
        }
    }

    public void setTabClick() {
        if (reboundScrollView != null) {
            reboundScrollView.fullScroll(ScrollView.FOCUS_UP);
        }

    }
}
