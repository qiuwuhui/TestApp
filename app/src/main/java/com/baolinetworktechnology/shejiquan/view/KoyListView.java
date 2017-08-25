package com.baolinetworktechnology.shejiquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 * 嵌套ListView ScoView 使用
 * @author JiSheng.Guo
 *
 */
public class KoyListView extends ListView {

	public KoyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public KoyListView(Context context, AttributeSet attrs) {

		super(context, attrs);
	}
	public KoyListView(Context context) {
		super(context);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
