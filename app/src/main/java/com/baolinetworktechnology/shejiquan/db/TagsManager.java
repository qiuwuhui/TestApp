package com.baolinetworktechnology.shejiquan.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Manage the tags from SQLite database.
 * 
 * @author JiSheng.Guo
 * 
 */
public class TagsManager {
	private static TagsManager INSTANCE;

	private DatabaseHelper mDbHelper;

	private TagsManager(Context context) {
		mDbHelper = new DatabaseHelper(context);
	}

	public static TagsManager getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new TagsManager(context);
		}
		return INSTANCE;
	}

	public String[] getTags() {
		List<String> tagList = new ArrayList<String>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = db.query(TagsTable.TABLE_NAME,
				new String[] { TagsTable.TAG }, null, null, null, null, null);
		while (c.moveToNext()) {
			String tag = c.getString(c.getColumnIndex(TagsTable.TAG));
			tagList.add(tag);
		}
		c.close();
		db.close();
		return tagList.toArray(new String[] {});
	}

	public List<String> getListTags() {
		List<String> tagList = new ArrayList<String>();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = db.query(TagsTable.TABLE_NAME,
				new String[] { TagsTable.TAG }, null, null, null, null, null);
		while (c.moveToNext()) {
			String tag = c.getString(c.getColumnIndex(TagsTable.TAG));
			tagList.add(tag);
		}
		c.close();
		db.close();
		Collections.reverse(tagList);
		return tagList;
	}

	public void updateTags(CharSequence... tags) {
		clearTags();
		for (CharSequence tag : tags) {
			addTag(tag);
		}
	}

	public void addTag(CharSequence tag) {
		String[] s = getTags();
		for (int i = 0; i < s.length; i++) {
			if (tag.equals(s[i])) {
				return;
			}
		}
		ContentValues values = new ContentValues();
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		values.put(TagsTable.TAG, tag.toString());
		db.insert(TagsTable.TABLE_NAME, null, values);
		db.close();
	}

	public void clearTags() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(TagsTable.TABLE_NAME, null, null);
		db.close();
	}
}