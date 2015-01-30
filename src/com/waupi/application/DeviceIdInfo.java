package com.waupi.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.waupi.constants.Constant;

public class DeviceIdInfo {

	public String device_id ;
	
	public SharedPreferences preference;
	
	public DeviceIdInfo(Context ctx){
		preference = ctx.getSharedPreferences(Constant.Values.DEVICE_INFO.name(), Context.MODE_PRIVATE);
		device_id = preference.getString(Constant.Values.DEVICE_ID.name(), null);
	}
	
	public void SetDeviceIdInfo(String device_id) {
		this.device_id = device_id;
		
		Editor edit = preference.edit();
		edit.putString(Constant.Values.DEVICE_ID.name(), device_id);
		
		edit.commit();
	}
	
	public String getDeviceId(){
		
		return preference.getString(Constant.Values.DEVICE_ID.name(), null);
	}
}
