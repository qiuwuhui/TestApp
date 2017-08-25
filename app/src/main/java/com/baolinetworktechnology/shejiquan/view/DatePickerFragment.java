package com.baolinetworktechnology.shejiquan.view;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	IDatePickerFragment mIDatePickerFragment;

	public interface IDatePickerFragment {
		void onDateSetOk(int year, int month, int day);
	}

	public void setIDatePickerFragment(IDatePickerFragment iDatePickerFragment) {
		mIDatePickerFragment = iDatePickerFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		++month;
		if (mIDatePickerFragment != null)
			mIDatePickerFragment.onDateSetOk(year, month, day);
	}
}
