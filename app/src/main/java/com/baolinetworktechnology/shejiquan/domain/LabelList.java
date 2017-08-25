package com.baolinetworktechnology.shejiquan.domain;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class LabelList {
        private String labelName;

        private String labelGUID;

        public String getLabelGUID() {
            return labelGUID;
        }

        public void setLabelGUID(String labelGUID) {
            this.labelGUID = labelGUID;
        }

        public String getLabelName() {
            return labelName;
        }

        public void setLabelName(String labelName) {
            this.labelName = labelName;
        }
}
