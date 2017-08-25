package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/4/19.
 */

public class postsItemInfoList {
    public String path;
    public String width;
    public String height;
    //图片剪切
    private String SmallImages;
    public String getSmallImages() {
        return getSmallImages("_500_500.");
    }
    public String getSmallImages(String clip) {
        if (TextUtils.isEmpty(SmallImages)) {
            SmallImages = getSuffix(clip);
            return SmallImages;
        }
        return SmallImages;
    }
    private String getSuffix(String clip) {
        if (path != null && path.length() > 5) {
            String temp = path.substring(path.length() - 6);
            String[] tems = temp.split("\\.");
            if (tems != null && tems.length > 1) {
                String suffix = tems[1];

                return path.replace("." + suffix, clip) + suffix;

            } else {
                return path;
            }
        } else {
            return path;
        }
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
