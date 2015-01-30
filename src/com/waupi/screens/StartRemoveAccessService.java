package com.waupi.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartRemoveAccessService extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent i = new Intent(context,RemoveAccessService.class);
		context.startService(i);
	}
}
