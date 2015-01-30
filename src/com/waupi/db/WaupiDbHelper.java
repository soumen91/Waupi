package com.waupi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WaupiDbHelper extends SQLiteOpenHelper{

	public WaupiDbHelper(Context context) {
		super(context, TableConstantName.DATABASENAME, null, TableConstantName.VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		WaupiDbCreate.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		WaupiDbCreate.onUpgrade(db, oldVersion, newVersion);
	}
}
