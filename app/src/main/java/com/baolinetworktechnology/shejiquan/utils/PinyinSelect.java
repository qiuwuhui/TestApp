package com.baolinetworktechnology.shejiquan.utils;


import com.baolinetworktechnology.shejiquan.domain.DataBean;
import com.baolinetworktechnology.shejiquan.domain.SortModel;

import java.util.Comparator;

/**
 * @Description:拼音的比较器
 * @author http://blog.csdn.net/finddreams
 */ 
public class PinyinSelect implements Comparator<DataBean> {

	public int compare(DataBean o1, DataBean o2) {
		if (o1.getInitial().equals("@")
				|| o2.getInitial().equals("#")) {
			return -1;
		} else if (o1.getInitial().equals("#")
				|| o2.getInitial().equals("@")) {
			return 1;
		} else {
			return o1.getInitial().compareTo(o2.getInitial());
		}
	}

}
