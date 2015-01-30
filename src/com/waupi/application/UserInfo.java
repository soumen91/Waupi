package com.waupi.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.waupi.constants.Constant;

public class UserInfo {

	public String fname;
	public String lname;
	public String user_id;
	public String reservation_id;
	public String reservation_code;
	public String reservation_type;
	private boolean checkin_status;
	
	public SharedPreferences preference;
	
	public UserInfo(Context ctx){
		preference = ctx.getSharedPreferences(Constant.Values.USER_PREF.name(), Context.MODE_PRIVATE);
		fname = preference.getString(Constant.Values.FNAME.name(), null);
		lname = preference.getString(Constant.Values.LNAME.name(), null);
		user_id = preference.getString(Constant.Values.USER_ID.name(), null);
		reservation_id = preference.getString(Constant.Values.RESERVATION_ID.name(), null);
		reservation_code = preference.getString(Constant.Values.RESERVATION_CODE.name(), null);
		reservation_type = preference.getString(Constant.Values.RESERVATION_TYPE.name(), null);
		checkin_status = preference.getBoolean(Constant.Values.CHECKIN_STATUS.name(), false);
	
	}
	
	public void SetUserInfo(String fname, String lname, String user_id,
			String reservation_id, String reservation_code,
			String reservation_type, boolean checkin_status) {
		this.fname = fname;
		this.lname = lname;
		this.user_id = user_id;
		this.reservation_id = reservation_id;
		this.reservation_code = reservation_code;
		this.reservation_type = reservation_type;
		this.checkin_status = checkin_status;
		
		Editor edit = preference.edit();
		edit.putString(Constant.Values.FNAME.name(), fname);
		edit.putString(Constant.Values.LNAME.name(), lname);
		edit.putString(Constant.Values.USER_ID.name(), user_id);
		edit.putString(Constant.Values.RESERVATION_ID.name(), reservation_id);
		edit.putString(Constant.Values.RESERVATION_CODE.name(), reservation_code);
		edit.putString(Constant.Values.RESERVATION_TYPE.name(), reservation_type);
		edit.putBoolean(Constant.Values.CHECKIN_STATUS.name(), checkin_status);
		
		edit.commit();
	}
}
