package com.byl.datepicker.citywheelview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.byl.datepicker.R;

/**
 * Adapter for countries
 */
public class CountryAdapter extends AbstractWheelTextAdapter {
	// Countries names
	private String countries[] = AddressData.PROVINCES;

	/**
	 * Constructor
	 */
	protected CountryAdapter(Context context) {
		super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
		setItemTextResource(R.id.wheelcity_country_name);
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}

	@Override
	public int getItemsCount() {
		return countries.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return countries[index];
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return null;
	}
}