package com.hyphenate.easeui.widget.photoview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 嵌套ListView ScoView 使用
 * @author JiSheng.Guo
 *
 */
public class KoyListView extends ListView {

	public KoyListView(Context context) {
		super(context);
	}

	public KoyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public KoyListView(Context context, AttributeSet attrs,int defStyleAttr) {
		super(context, attrs,defStyleAttr);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		//setMeasuredDimension(widthMeasureSpec, expandSpec);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
