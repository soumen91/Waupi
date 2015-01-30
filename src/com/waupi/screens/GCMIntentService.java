package com.waupi.screens;

import static com.waupi.util.CommonUtilities.SENDER_ID;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.waupi.util.ServerUtilities;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "snomada";
	public SharedPreferences preference;
	public int notifyId = 1;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	protected void onRegistered(Context context, String registrationId) {
		preference = context.getSharedPreferences("DEVICE_INFO",Context.MODE_PRIVATE);
		Editor edit = preference.edit();
		edit.putString("DEVICE_ID", registrationId);
		edit.commit();
		if (registrationId != null) {
			ServerUtilities.register(context, registrationId);
		}
	}

	protected void onUnregistered(Context context, String registrationId) {
		ServerUtilities.unregister(context, registrationId);
	}

	protected void onMessage(Context context, Intent intent) {
		String message = intent.getExtras().getString("message");
		System.out.println("!!the message here:" + message);
		generateNotification(context, message);
	}

	protected void onDeletedMessages(Context context, int total) {
		String message = getString(R.string.gcm_deleted, total);
		generateNotification(context, message);
	}

	public void onError(Context context, String errorId) {
	}

	protected boolean onRecoverableError(Context context, String errorId) {
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	@SuppressWarnings("deprecation")
	private void generateNotification(final Context context, String message) {
		/*NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder nb = new NotificationCompat.Builder(context);
		nb.setSmallIcon(R.drawable.notify_icon);
		nb.setContentTitle("Waupi");
		nb.setContentText(message);
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		nb.setSound(alarmSound);
		nb.setAutoCancel(true);

		Intent intent = new Intent(context, MyCoupon.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TaskStackBuilder builder = TaskStackBuilder.create(context);
		builder.addParentStack(MyCoupon.class);
		builder.addNextIntent(intent);

		PendingIntent resultPendingIntent = builder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		nb.setContentIntent(resultPendingIntent);
		int randInt = new Random().nextInt(2) + 1;
		noti.notify(randInt, nb.build());*/
		/*Intent intent = new Intent(context, MyCoupon.class)
		.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pintent = PendingIntent.getActivity(context, 0, intent, 0);
		long when = System.currentTimeMillis();
		Notification notification = new Notification(R.drawable.notify_icon, message, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notification.setLatestEventInfo(context, "Waupi", message, pintent);
		//notification.contentIntent = pintent;
		notificationManager.notify(0, notification);*/
		
		
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.tickerText = message;
		notification.icon = R.drawable.notify_icon;
		notification.when = when;
		notification.ledARGB = 0xff00ff00;
		notification.ledOnMS = 300;
		notification.ledOffMS = 1000;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		//notification.defaults |= Notification.DEFAULT_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults = Notification.DEFAULT_ALL;

		Intent intent = new Intent(context,MyCoupon.class)
		.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		PendingIntent pintent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.setLatestEventInfo(context, "Waupi", message, pintent);
		notificationManager.notify((int) System.currentTimeMillis(),
				notification);
		
		preference = context.getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		Editor edit = preference.edit();
		System.out.println("### "+ "Preference from gcm intent true updated");
		edit.putBoolean("ISUSER_AUTHENTICATED", true);
		//edit.commit();
		
		//preference = context.getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		//Editor edit1 = preference.edit();
		edit.putLong("ACCESS_START_TIME", System.currentTimeMillis());
		edit.putBoolean("DUMMY_ACCESS", true);
		
		edit.commit();
		
		Intent notificationIntent = new Intent("com.waupi.dialogreceiver");
		notificationIntent.putExtra("value", message);
		context.sendBroadcast(notificationIntent);
	}
}