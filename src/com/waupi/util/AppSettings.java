package com.waupi.util;

import android.app.Application;

import com.waupi.application.LoginInfo;
import com.waupi.application.UserInfo;

public class AppSettings extends Application{

	public LoginInfo loginInfo;
	public UserInfo userinfo;
	public boolean init = false; 	
	
	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}
	
	public UserInfo getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
}
