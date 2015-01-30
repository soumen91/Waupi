package com.waupi.screens;

import static com.waupi.util.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.waupi.util.CommonUtilities.EXTRA_MESSAGE;
import static com.waupi.util.CommonUtilities.SENDER_ID;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.crittercism.app.Crittercism;
import com.google.android.gcm.GCMRegistrar;
import com.waupi.constants.Constant;
import com.waupi.db.WaupiDbAdapter;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.WakeLocker;

public class SplashScreen extends BaseScreen {

	private Handler handler = new Handler();
	private SharedPreferences pref;
	public Runnable runnable;

	/**Device id */
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	String regId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Intent i = new Intent(SplashScreen.this,RemoveAccessService.class);
		startService(i);

		pref = getSharedPreferences(Constant.Values.APP_PREFERENCE.name(),
				Context.MODE_PRIVATE);
		Crittercism.initialize(getApplicationContext(),
				"5433c82983fb79100a000002");
		adapter = WaupiDbAdapter.databseHelperInstance(getApplicationContext());
	}

	@Override
	public void init() {
		super.init();
		getPushNotificationDeviceID();
		if (app.getLogininfo().isLogin == true) {
			goNextScreen(true);
		} else {
			goNextScreen(false);
		}
		if (!pref.getBoolean(Constant.Values.IS_ICON_CREATED.name(), false)) {
			addShortcut();
			pref.edit()
					.putBoolean(Constant.Values.IS_ICON_CREATED.name(), true)
					.commit();
		}
	}

	@Override
	public void goNextScreen(final boolean flg) {
		if (flg) {
			Intent i = new Intent(SplashScreen.this, DashBorad.class);
			startActivity(i);
			finish();
		} else {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					pref = getSharedPreferences("DEVICE_INFO", Context.MODE_PRIVATE);
					String device_id = pref.getString("DEVICE_ID", null);
					app.getDeviceInfo().SetDeviceIdInfo(device_id);
					Intent i = new Intent(SplashScreen.this,
							LoginRegister.class);
					startActivity(i);
					overridePendingTransition(R.anim.anim1, R.anim.anim2);
					finish();
				}
			};
			handler.postDelayed(runnable, 4000);
		}
	}

	private void addShortcut() {
		//Adding shortcut for MainActivity 
		//on Home screen
		Intent shortcutIntent = null;

		String createResult = adapter.getShortCutStatus();
		System.out.println("!!create it:" + createResult);

		if (createResult != null) {
			if (createResult.equalsIgnoreCase("true")) {
				shortcutIntent = new Intent(getApplicationContext(),
						DashBorad.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			} else {

			}
			shortcutIntent.setAction(Intent.ACTION_MAIN);
		} else {
			shortcutIntent = new Intent(getApplicationContext(),
					SplashScreen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			adapter.insertShortCreateInfo("true");
		}

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Waupi");
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(
						getApplicationContext(), R.drawable.app_logo));

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);
	}

	public void getPushNotificationDeviceID() {
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(SplashScreen.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		final String regId = GCMRegistrar.getRegistrationId(this);
		
		if (regId.equals("")) {
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
			System.out.println("!! this is register id:"
					+ GCMRegistrar.getRegistrationId(this));

		} else {
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
		}
	}

	// Push notification purpose receiver
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			//unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
		}
	}
}
