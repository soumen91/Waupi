package com.waupi.application;

import android.app.Application;

public class WaupiApplication extends Application{
	
	public boolean init = false;
	
	public UserInfo userInfo;
	public LoginInfo logininfo;
	public HotelInfo hotelInfo;
	public DeviceIdInfo deviceInfo;
	public TutorialInfo tutorialInfo;
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public LoginInfo getLogininfo() {
		return logininfo;
	}
	public void setLogininfo(LoginInfo logininfo) {
		this.logininfo = logininfo;
	}
	public HotelInfo getHotelInfo() {
		return hotelInfo;
	}
	public void setHotelInfo(HotelInfo hotelInfo) {
		this.hotelInfo = hotelInfo;
	}
	public boolean isInit() {
		return init;
	}
	public void setInit(boolean init) {
		this.init = init;
	}
	public DeviceIdInfo getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(DeviceIdInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public TutorialInfo getTutorialInfo() {
		return tutorialInfo;
	}
	public void setTutorialInfo(TutorialInfo tutorialInfo) {
		this.tutorialInfo = tutorialInfo;
	}
}
