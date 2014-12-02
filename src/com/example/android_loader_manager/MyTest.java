package com.example.android_loader_manager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

public class MyTest extends AndroidTestCase {

	public MyTest() {
		// TODO Auto-generated constructor stub
	}

	public void add() {
		ContentResolver contentResolver = getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put("name", "liulianhua");
		Uri url = Uri
				.parse("content://com.example.android_loader_manager.StudentContentProvider/student");
		contentResolver.insert(url, values);
	}

	public void delete() {
		ContentResolver contentResolver = getContext().getContentResolver();

		Uri url = Uri
				.parse("content://com.example.android_loader_manager.StudentContentProvider/student/1");
		int count = contentResolver.delete(url, null, null);
		System.out.println("---count-->>" + count);
	}

	public void query() {
		ContentResolver contentResolver = getContext().getContentResolver();
		Uri url = Uri
				.parse("content://com.example.android_loader_manager.StudentContentProvider/student");
		Uri uri = ContentUris.withAppendedId(url, 2);
		
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		while (cursor.moveToNext()) {
			System.out.println("--->>"
					+ cursor.getString(cursor.getColumnIndex("name")));
		}
	}
}
