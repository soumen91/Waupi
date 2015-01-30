package com.waupi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WaupiDbAdapter {

	public static WaupiDbAdapter adapter;
	public static WaupiDbHelper helper;
	public static Context sContect;
	public static SQLiteDatabase sDb;
	
	public WaupiDbAdapter(Context context) {
		sContect = context;
	}

	public static WaupiDbAdapter databseHelperInstance(final Context context){
		if(adapter == null){
			adapter = new WaupiDbAdapter(context);
			Open();
		}
		return adapter;
	}

	private static void Open() {
		helper = new WaupiDbHelper(sContect);
		sDb = helper.getWritableDatabase();
	}
	
	public void close() {
		sDb.close();		
	}
	
	/////////////////////For insert into Table ////////////////////
	
	public long insertShortCreateInfo(final String value){
		
		final ContentValues values = new ContentValues();
		values.put(TableConstantName.SHORTCUT_FLAG, value);
		
		try{
			sDb.beginTransaction();
			final long state = sDb.insert(TableConstantName.TABLE_NAME, null, values);
			sDb.setTransactionSuccessful();
			return state;
		}catch(SQLException e){
			throw e;
		}finally{
			sDb.endTransaction();
		}
	}
	
	////////////// For fetch data from table /////////////////////
	
	public String getShortCutStatus(){
		String value = null;
		Cursor cursor = sDb.rawQuery("SELECT "+TableConstantName.SHORTCUT_FLAG+" FROM "+ TableConstantName.TABLE_NAME, null);
		try {
			if(cursor.getCount() > 0){
				
				cursor.moveToFirst();
				while(!cursor.isAfterLast()){
					value = cursor.getString(cursor.getColumnIndex(TableConstantName.ID)); 
					cursor.moveToNext();					
				}				
			}
		} catch (SQLException e) {
			throw e;
		}finally{
			cursor.close();
		}
		return value;
	}
}
