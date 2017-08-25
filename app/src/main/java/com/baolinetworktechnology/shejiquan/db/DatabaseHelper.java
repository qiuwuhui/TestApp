package com.baolinetworktechnology.shejiquan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite database open helper.
 * 
 * @author JiSheng.Guo
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static int DB_VERSION = 3;

	public DatabaseHelper(Context context) {
		super(context, "shejiquan.db", null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TagsTable.TABLE_NAME + "(" + TagsTable._ID
				+ " INTEGER PRIMARY KEY," + TagsTable.TAG + " TEXT" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}