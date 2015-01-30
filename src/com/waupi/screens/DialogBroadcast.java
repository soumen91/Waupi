package com.waupi.screens;

import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class DialogBroadcast extends BroadcastReceiver {
	private Context context;

	@Override
	public void onReceive(final Context context, final Intent intent) {

		this.context = context;
		String message = intent.getStringExtra("value");

		if(isForeground()){
			Intent i = new Intent(context, DialogActivity.class);
			i.putExtra("message", message);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}

	public boolean isForeground() {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager
				.getRunningTasks(1);

		ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
		if (componentInfo.getPackageName().equals("com.waupi.screens"))
			return true;
		return false;
	}
}
