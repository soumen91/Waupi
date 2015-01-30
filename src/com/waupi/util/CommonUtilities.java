package com.waupi.util;

import com.waupi.constants.Constant;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	public static final String SERVER_URL = Constant.SERVER_URL;
	public static final String SENDER_ID = "400815854979";
	public static final String TAG = "snomada";
	public static final String DISPLAY_MESSAGE_ACTION = "com.androidhive.pushnotifications1.DISPLAY_MESSAGE";
	public static final String DISPLAY_CHAT_MESSAGE_ACTION = "com.androidhive.pushnotifications1.DISPLAY_CHAT_MESSAGE";
	
	public static final String EXTRA_MESSAGE = "message";
	public static final String CHAT_MESSAGE = "chat";

	public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}
}
