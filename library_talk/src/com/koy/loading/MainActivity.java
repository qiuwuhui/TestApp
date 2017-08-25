package com.koy.loading;

import com.baolinetworktechnology.library_talk.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		new AlertDialog.Builder(this).setView(
				View.inflate(this, R.layout.diao, null)).show();
	}

}
