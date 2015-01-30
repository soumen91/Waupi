package com.waupi.screens;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

public class RemoveAccessService extends Service {

	String tag = "Waupi";
	
	SharedPreferences pref ;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(tag, "in oncreate");
		
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1 * 1000);
						boolean isActive = pref.getBoolean("ISUSER_AUTHENTICATED", false);
						long start_time = pref.getLong("ACCESS_START_TIME", 00);
						
						if(isActive){
							
							Log.i(tag, "active true");
							
							if((System.currentTimeMillis() - start_time) > 300000){
								
								Log.i(tag, "active true preference updated");
								
								Editor edit = pref.edit();
								edit.putBoolean("ISUSER_AUTHENTICATED", false);
								edit.putBoolean("DUMMY_ACCESS", true);
								edit.commit();
								
								Intent notificationIntent = new Intent("com.waupi.dialogreceiver");
								notificationIntent.putExtra("value", "Your access time has been expired.Please request again!!");
								sendBroadcast(notificationIntent);
							}else{
								Log.i(tag, "active false");
							}
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.i(tag, "in onstartCommnad");
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(tag, "in ondestroy");
	}
}
