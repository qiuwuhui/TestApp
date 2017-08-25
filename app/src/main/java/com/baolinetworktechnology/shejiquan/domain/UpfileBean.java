package com.baolinetworktechnology.shejiquan.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class UpfileBean {
    public boolean success;
    public List<data> result;
    public class data{
        public String Message;
        public String Url;
        public int ImgWidth;
        public int ImgHeight;
        public boolean Ok;
        public String FileName;
    }
}
