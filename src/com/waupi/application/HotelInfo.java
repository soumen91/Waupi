package com.waupi.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.waupi.constants.Constant;

public class HotelInfo {

	public String hotelName,hotelId,hotelCity,hotelCountry,hotelrating,hoteleview_count,hotelimage;
	
	public String wakeup_call_no,donot_disturb,laundry_pick_up,valet_no;
	public SharedPreferences pref;
	
	public HotelInfo(Context context){
		
		pref = context.getSharedPreferences(Constant.Values.HOTELINFO.name(), Context.MODE_PRIVATE);
		hotelName = pref.getString(Constant.Values.HOTEL_NAME.name(), null);
		hotelId = pref.getString(Constant.Values.HOTEL_ID.name(), null);
		hotelCity = pref.getString(Constant.Values.HOTEL_CITY.name(), null);
		hotelCountry = pref.getString(Constant.Values.HOTEL_COUNTRY.name(), null);
		hotelrating = pref.getString(Constant.Values.HOTEL_RATING.name(), null);
		hoteleview_count = pref.getString(Constant.Values.HOTEL_REVIEW_COUNT.name(), "0");
		hotelimage = pref.getString(Constant.Values.HOTEL_IMAGE.name(), null);
		wakeup_call_no = pref.getString(Constant.Values.FRONTDESK_NO.name(), null);
		wakeup_call_no = pref.getString(Constant.Values.FRONTDESK_NO.name(), null);
		donot_disturb = pref.getString(Constant.Values.DONOT_DISTURB.name(), null);
		laundry_pick_up = pref.getString(Constant.Values.LAUNDRY_PICKUP_NO.name(), null);
		valet_no = pref.getString(Constant.Values.VALET_PICKUP_NO.name(), null);
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.HOTEL_NAME.name(), hotelName);
		edit.commit();
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.HOTEL_ID.name(), hotelId);
		edit.commit();
	}

	public void setHotelCity(String hotelCity) {
		this.hotelCity = hotelCity;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.HOTEL_CITY.name(), hotelCity);
		edit.commit();
	}

	public void setHotelCountry(String hotelCountry) {
		this.hotelCountry = hotelCountry;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.HOTEL_COUNTRY.name(), hotelCountry);
		edit.commit();
	}

	public void setHoterating(String hoterating) {
		this.hotelrating = hoterating;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.HOTEL_RATING.name(), hotelrating);
		edit.commit();
	}

	public void setHoteleview_count(String hoteleview_count) {
		this.hoteleview_count = hoteleview_count;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.HOTEL_REVIEW_COUNT.name(), hoteleview_count);
		edit.commit();
	}

	public void setHotelimage(String hotelimage) {
		this.hotelimage = hotelimage;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.HOTEL_IMAGE.name(), hotelimage);
		edit.commit();
	}

	public void setWakeup_call_no(String wakeup_call_no) {
		
		this.wakeup_call_no = wakeup_call_no;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.FRONTDESK_NO.name(), wakeup_call_no);
		edit.commit();
	}

	public void setDonot_disturb(String donot_disturb) {
		this.donot_disturb = donot_disturb;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.DONOT_DISTURB.name(), donot_disturb);
		edit.commit();
	}

	public void setLaundry_pick_up(String laundry_pick_up) {
		this.laundry_pick_up = laundry_pick_up;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.LAUNDRY_PICKUP_NO.name(), laundry_pick_up);
		edit.commit();
	}

	public void setValet_no(String valet_no) {
		this.valet_no = valet_no;
		
		Editor edit = pref.edit();
		edit.putString(Constant.Values.VALET_PICKUP_NO.name(), valet_no);
		edit.commit();
	}
	
}
