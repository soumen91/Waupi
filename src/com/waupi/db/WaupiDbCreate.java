package com.waupi.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class WaupiDbCreate {
	
	public static final String DATABASE_WAUPI = "CREATE TABLE "
			+ TableConstantName.TABLE_NAME + "("
			+ TableConstantName.ID 
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ TableConstantName.SHORTCUT_FLAG 
			+ " TEXT);";

	public static void onCreate(SQLiteDatabase database){
		
		database.beginTransaction();
		try{
			database.execSQL(DATABASE_WAUPI);
			database.setTransactionSuccessful();
		}catch(SQLiteException e){
			throw e;
		}finally{
			database.endTransaction();
		}
	}
	
	public static void onUpgrade(SQLiteDatabase database,int oldversion, int newversion){
		database.execSQL("DROP TABLE IF EXIST"+TableConstantName.TABLE_NAME);
		onCreate(database);
	}
}
