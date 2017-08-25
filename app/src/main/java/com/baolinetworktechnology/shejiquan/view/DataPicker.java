package com.baolinetworktechnology.shejiquan.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.baolinetworktechnology.shejiquan.R;
import com.baolinetworktechnology.shejiquan.view.ScrollerNumberPicker.OnSelectListener;

/**
 * 自定义数据Picker
 * 
 * @author JiSheng.Guo
 * 
 */
public class DataPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;

	private ArrayList<String> data1;
	private SparseArray<ArrayList<String>> map;

	public DataPicker(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public DataPicker(Context context) {
		super(context);

	}

	public void setData(ArrayList<String> data1,
			SparseArray<ArrayList<String>> map, int index, int index2) {
		this.data1 = data1;
		this.map = map;
		initView(index, index2);
	}

	StringBuffer sb1, sb2;

	protected void initView(int index, int index2) {
		if (data1 == null)
			return;
		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
		cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);

		provincePicker.setData(data1);
		provincePicker.setDefault(index);
		cityPicker.setData(map.get(0));
		cityPicker.setDefault(index2);
		sb1 = new StringBuffer(data1.get(index));
		sb2 = new StringBuffer(map.get(0).get(index2));

		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {

				if (text.equals("") || text == null || id == data1.size() - 1)
					return;

				if (tempProvinceIndex != id) {
					String selectDay = cityPicker.getSelectedText();
					if (selectDay == null || selectDay.equals(""))
						return;
					sb1.delete(0, sb1.length());
					sb1.append(text);
					cityPicker.setData(map.get(id));
					cityPicker.setDefault(0);
					sb2.delete(0, sb2.length());
					sb2.append(map.get(id).get(0));
					Message message = new Message();
					message.what = REFRESH_VIEW;
					handler.sendMessage(message);
				}
				tempProvinceIndex = id;

			}

			@Override
			public void selecting(int id, String text) {
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				if (text.equals("") || text == null || id == data1.size() - 1)
					return;
				sb2.delete(0, sb2.length());
				sb2.append(text);
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {

			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}

	public String getText() {
		// TODO Auto-generated method stub
		return sb1.toString() + " " + sb2.toString();
	}
}
