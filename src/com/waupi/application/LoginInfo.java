package com.waupi.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.waupi.constants.Constant;

public class LoginInfo {

		public boolean isLogin = false;
		public boolean isTraversHotelList = false;
		public SharedPreferences preferences;
		
		public LoginInfo(Context ctx){
			preferences = ctx.getSharedPreferences(Constant.Values.LOGIN_INFO.name(), Context.MODE_PRIVATE);
			isLogin = preferences.getBoolean(Constant.Values.ISSIGNIN.name(), false);
			isTraversHotelList = preferences.getBoolean(Constant.Values.TRAVERS_HOTEL_LIST.name(), false);
		}
		
		public void setLoginInfo(boolean isLogin){
			this.isLogin = isLogin;
			
			Editor edit = preferences.edit();
			edit.putBoolean(Constant.Values.ISSIGNIN.name(), isLogin);
			edit.commit();
		}

		public void setTraversHotelList(boolean isTraversHotelList) {
			this.isTraversHotelList = isTraversHotelList;
			
			Editor edit = preferences.edit();
			edit.putBoolean(Constant.Values.TRAVERS_HOTEL_LIST.name(), isTraversHotelList);
			edit.commit();
		}
}
