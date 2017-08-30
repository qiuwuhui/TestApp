package com.baolinetworktechnology.shejiquan.utils;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.baolinetworktechnology.shejiquan.constant.ApiUrl;
import com.baolinetworktechnology.shejiquan.constant.AppTag;
import com.baolinetworktechnology.shejiquan.view.DialogShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 *
 * 快速分享工具类
 *
 * @author JiSheng.Guo
 *
 */
public class ShareUtils {
	private UMWXHandler mWxHandler;
	private UMWXHandler mWxCircleHandler;
	private UMQQSsoHandler mQQSsoHandler;
	private QZoneSsoHandler mQZoneSsoHandler;
	SinaSsoHandler mSinaSsoHandler;
	UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private Activity mContext;
	private String mWebUrl, mText, mTitle;// , mText, mImageUrl;
	DialogShare mDialogShare;
	IWXAPI mIWXAPI;

	public ShareUtils(Activity context) {
		mContext = context;
		mController.getConfig().cleanListeners();

		// 添加微信平台
		mWxHandler = new UMWXHandler(context, AppTag.WeChatAppID,
				AppTag.WeChatAppSecret);

		// 添加微信朋友圈
		mWxCircleHandler = new UMWXHandler(context, AppTag.WeChatAppID,
				AppTag.WeChatAppSecret);
		mWxCircleHandler.setToCircle(true);

		mQQSsoHandler = new UMQQSsoHandler(context, AppTag.QQAppID,
				AppTag.QQAppSecret);

		mQZoneSsoHandler = new QZoneSsoHandler(context, AppTag.QQAppID,
				AppTag.QQAppSecret);
		mSinaSsoHandler = new SinaSsoHandler(context);

		mController.setAppWebSite(SHARE_MEDIA.RENREN, ApiUrl.SHARE_SHEJIQUAN);
		mDialogShare = new DialogShare(context, this);
		mIWXAPI = WXAPIFactory.createWXAPI(context, AppTag.WeChatAppID, true);
		mIWXAPI.registerApp(AppTag.WeChatAppID);
		mController.getConfig().setSsoHandler(mSinaSsoHandler);
	}

	/**
	 * 添加到分享平台
	 */
	public ShareUtils addToSocialSDK() {
		mQZoneSsoHandler.addToSocialSDK();
		mWxHandler.addToSocialSDK();
		mWxCircleHandler.addToSocialSDK();
		mQQSsoHandler.addToSocialSDK();
		mSinaSsoHandler.addToSocialSDK();
		return this;
	}

	/**
	 * 设置分享内容图片链接
	 *
	 * @param imageUrl
	 *            分享图片链接
	 * @return ShareUtils
	 */
	public ShareUtils setImageUrl(String imageUrl) {
		// this.mImageUrl = imageUrl;
		mController.setShareMedia(new UMImage(mContext, imageUrl));
		return this;
	}

	public ShareUtils setImageFilePath(String path) {
		mController.setShareMedia(new UMImage(mContext, BitmapFactory
				.decodeFile(path)));
		return this;
	}

	public ShareUtils setImageFile(File file) {
		mController.setShareMedia(new UMImage(mContext, BitmapFactory
				.decodeFile(file.getAbsolutePath())));
		return this;
	}

	/**
	 * 设置分享图片
	 *
	 * @param imageId
	 *            本地图片id
	 * @return ShareUtils
	 */
	public ShareUtils setImageUrl(int imageId) {
		//
		mController.setShareMedia(new UMImage(mContext, imageId));
		return this;
	}

	/**
	 * 设置分享内容链接
	 *
	 * @param webUrl
	 *            网页链接
	 * @return ShareUtils
	 */
	public ShareUtils setShareUrl(String webUrl) {
		this.mWebUrl = webUrl;
		mWxCircleHandler.setTargetUrl(webUrl);
		mWxHandler.setTargetUrl(webUrl);
		mQQSsoHandler.setTargetUrl(webUrl);
		mQZoneSsoHandler.setTargetUrl(webUrl);
		mSinaSsoHandler.setTargetUrl(webUrl);
		return this;
	}

	/**
	 * 设置分享标题
	 *
	 * @param title
	 *            分享标题
	 * @return ShareUtils
	 */
	public ShareUtils setShareTitle(String title) {
		this.mTitle = title;
		mQQSsoHandler.setTitle(title);
		mWxHandler.setTitle(title);
		mWxCircleHandler.setTitle(title);
		return this;
	}

	/**
	 * 设置分享内容
	 *
	 * @param content
	 *            分享内容
	 * @return ShareUtils
	 */
	public ShareUtils setShareContent(String content) {
		this.mText = content;
		mController.setShareContent(content + "查看详情:" + mWebUrl);
		return this;
	}

	public ShareUtils setShareContent2(String content) {
		this.mText = content;
		mController.setShareContent(content);
		return this;
	}

	/**
	 * 开启分享对话版
	 *
	 */
	public void doShare() {
		// 是否只有已登录用户才能打开分享选择页
		// doShare(false);
		mDialogShare.show();
	}
	public void doDismiss() {
		// 关闭分享页
		// doShare(false);
		mDialogShare.dismiss();
	}
	/**
	 * 开启分享对话版
	 *
	 * @param islogin
	 *            是否只有已登录用户才能打开分享选择页
	 */
	public void doShare(boolean islogin) {
		// 是否只有已登录用户才能打开分享选择页
		mController.setShareContent(mText + " 查看详情:" + mWebUrl);

		mController.openShare(mContext, islogin);
	}

	/**
	 * 分享QQ好友
	 */
	public void doShareQQ() {
		// QQShareContent qzone = new QQShareContent();
		// // 设置分享文字
		// qzone.setShareContent(mText);
		// // 设置点击消息的跳转URL
		// qzone.setTargetUrl(mWebUrl);
		// // 设置分享内容的标题
		// qzone.setTitle(mTitle);
		// // 设置分享图片
		// qzone.setShareImage(new UMImage(mContext, mImageUrl));
		// mController.setShareMedia(qzone);
		if (SHARE_MEDIA.QQ != null) {
			try {
				mController.postShare(mContext, SHARE_MEDIA.QQ, null);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	/**
	 * 分享微信好友
	 */
	public void doShareWeChat() {
		// // 设置微信好友分享内容
		// WeiXinShareContent weixinContent = new WeiXinShareContent();
		// // 设置title
		// weixinContent.setTitle(mTitle);
		// // 设置分享文字
		// weixinContent.setShareContent(mText);
		// // 设置分享内容跳转URL
		// weixinContent.setTargetUrl(mWebUrl);
		// // 设置分享图片
		// weixinContent.setShareImage(new UMImage(mContext, mImageUrl));
		// mController.setShareMedia(weixinContent);
		mController.postShare(mContext, SHARE_MEDIA.WEIXIN, null);
	}

	/**
	 * 分享微信朋友圈
	 */
	public void doShareWeChatCircle() {
		// // 设置微信好友分享内容
		// WeiXinShareContent weixinContent = new WeiXinShareContent();
		// // 设置title
		// weixinContent.setTitle(mTitle);
		// // 设置分享文字
		// weixinContent.setShareContent(mText);
		// // 设置分享内容跳转URL
		// weixinContent.setTargetUrl(mWebUrl);
		// // 设置分享图片
		// weixinContent.setShareImage(new UMImage(mContext, mImageUrl));
		// mController.setShareMedia(weixinContent);
		mController.postShare(mContext, SHARE_MEDIA.WEIXIN_CIRCLE, null);

	}

	/**
	 * 分享新浪微博好友
	 */
	public void doShareSina() {
		// SinaShareContent sinaShareContent = new SinaShareContent();
		// // 设置分享文字
		// sinaShareContent.setShareContent(mTitle + " 查看详情:" + mWebUrl);
		// // 设置title
		// sinaShareContent.setTitle(mTitle);
		// // 设置分享内容跳转URL
		// sinaShareContent.setTargetUrl(mWebUrl);
		//
		// // 设置分享图片
		// sinaShareContent.setShareImage(new UMImage(mContext, mImageUrl));
		// mController.setShareMedia(sinaShareContent);
		mController.setShareContent(mTitle + " " + mText + " 查看详情:" + mWebUrl);
		mController.postShare(mContext, SHARE_MEDIA.SINA, null);

	}

	public void doShareQQZ() {
		mController.postShare(mContext, SHARE_MEDIA.QZONE, null);
	}

	public void doShareWeiXinS() {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = mWebUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = mTitle;
		msg.description = mText;
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "doShareWeiXinS";
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneFavorite;
		mIWXAPI.sendReq(req);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/** 使用SSO授权必须添加如下代码 */
		if (mController == null)
			return;
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
