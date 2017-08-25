package com.baolinetworktechnology.shejiquan.interfaces;

import java.util.Map;

import android.os.Bundle;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.exception.SocializeException;

public interface OnOAuthListener {
	void onStart(SHARE_MEDIA platform);

	void onComplete(Bundle value, SHARE_MEDIA platform);

	void onComplete(int status, Map<String, Object> info);

	void onCancel(SHARE_MEDIA platform);

	void onError(SocializeException e, SHARE_MEDIA platform);

}
