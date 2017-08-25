package com.baolinetworktechnology.shejiquan.domain;

import android.content.Context;
import android.text.TextUtils;

import com.baolinetworktechnology.shejiquan.app.SJQApp;
import com.baolinetworktechnology.shejiquan.net.GetClassList;

import java.util.List;

public class CaseItem {
    public String title;
    public String path;

    public String SmallImages;
    /**
     * _500_250.
     *
     * @return
     */
    public String getSmallImages() {

        return getSmallImages("_600_600.");
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
}
