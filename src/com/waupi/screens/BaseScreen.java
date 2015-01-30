package com.waupi.screens;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.waupi.application.DeviceIdInfo;
import com.waupi.application.HotelInfo;
import com.waupi.application.LoginInfo;
import com.waupi.application.TutorialInfo;
import com.waupi.application.UserInfo;
import com.waupi.application.WaupiApplication;
import com.waupi.constants.Constant;
import com.waupi.db.WaupiDbAdapter;
import com.waupi.network.HttpClient;

public class BaseScreen extends FragmentActivity implements OnClickListener{

	public WaupiApplication app = null;
	public WaupiDbAdapter adapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
	}

	@Override
	protected void onStart() {
		super.onStart();

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Constant.DEVICE_HEIGHT = metrics.heightPixels;
		Constant.DEVICE_WIDTH = metrics.widthPixels;
		
		app = (WaupiApplication) getApplication();

		if (!app.init) {
			app.init = true;
			app.setLogininfo(new LoginInfo(this));
			app.setUserInfo(new UserInfo(this));
			app.setHotelInfo(new HotelInfo(this));
			app.setDeviceInfo(new DeviceIdInfo(this));
			app.setTutorialInfo(new TutorialInfo(this));
		}
		init();
	}

	public void init() {
	}

	@Override
	public void onClick(View v) {
	}

	public void hideKeyBoard(EditText et) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	private ProgressDialog mDialog = null;

	public void doShowLoading() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mDialog = new ProgressDialog(BaseScreen.this);
				mDialog.setMessage("Please wait......");
				mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mDialog.setIndeterminate(true);
				mDialog.setCancelable(false);
				mDialog.show();
			}
		});
	}

	public void doRemoveLoading() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mDialog.cancel();
			}
		});
	}

	public void doLogIn(String username, String password) {
		try {
			JSONObject json = new JSONObject();
			json.put("email", username);
			json.put("password", password);
			json.put("device_id", app.getDeviceInfo().getDeviceId());
			json.put("device_type", "android");
			String response = HttpClient.getInstance(this).SendHttpPost(
					Constant.LOGIN, json.toString());
			if (response != null) {
				JSONObject ob = new JSONObject(response);
				if (!ob.isNull("status")) {
					if (ob.getBoolean("status")) {
						String first_name = ob.getString("first_name");
						String last_name = ob.getString("last_name");
						String user_id = ob.getString("user_id");
						String reservation_type = ob
								.getString("reservation_type");
						boolean checkin_status = ob
								.getBoolean("checkin_status");

						String rev_id = null, reservation_code = null;
						JSONArray object = ob
								.getJSONArray("reservation_detail");
						for (int i = 0; i < object.length(); i++) {
							rev_id = object.getJSONObject(i).getString(
									"reservation_id");
							reservation_code = object.getJSONObject(i)
									.getString("reservation_code");
						}

						app.getUserInfo().SetUserInfo(first_name, last_name,
								user_id, rev_id, reservation_code,
								reservation_type, checkin_status);

						app.getLogininfo().setLoginInfo(true);
						
						goNextScreen(false);
					}
				} else {
					goNextScreen(true);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void goNextScreen(final boolean flg) {
	}
}
