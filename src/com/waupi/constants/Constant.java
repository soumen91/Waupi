package com.waupi.constants;

import java.util.ArrayList;

import com.waupi.bean.RestaurantBean;

public class Constant {
	
	public static String URL = "http://waupi.com/";
	
	public static String REGISTER = URL +"api/v1/create";
	public static String LOGIN = URL +"api/v1/sign_in.json";
	public static String SOCIAL_LOGIN = URL +"api/v1/social_login.json";
	public static String LOGOUT = URL + "api/v1/sign_out.json";
	
	public static String DASHBOARD_HOTEL_LIST = URL + "api/v1/hotels.json";
	public static String CHECKIN_LIST_INFO = "http://waupi.com/api/v1/hotels/5.json";
	
	public static String ETA_SHUTTLE = URL + "api/v1/calculate_time_for_shuttle.json";
	public static String DEMO_ETA_SHUTTLE = URL + "api/demo/calculate_time_for_shuttle.json";
	
	public static String FEEDBACK = URL + "api/v1/hotels/feedback";
	
	public static String EXPLORE_HOTEL = URL + "api/v1/hotels/explore?id=";
	public static String DEMO_EXPLORE_HOTEL = URL + "api/demo/hotels/explore.json";
	
	public static String SHUTTLE_REQUEST = URL + "api/v1/hotels/request_shuttle";
	public static String COUON_REQUEST = URL + "api/v1/request_coupon.json";
	public static String SERVER_URL = URL + "api/v1/request_coupon.json";
	//public static String SERVER_URL = "";
	public static String COUPON_LIST = "api/v1/my_coupons.json";
	
	public static String REQUEST_USER_VALIDATION = URL + "api/v1/hotels/request_access.json";
	
	public static boolean mIsInForegroundMode = false;
	
	public static int DEVICE_HEIGHT;
	public static int DEVICE_WIDTH;
	public static double LAT;
	public static double LNG; 
	
	public static ArrayList<RestaurantBean> totalList = new ArrayList<RestaurantBean>();
	public static int position = -1;
	
	public static boolean isLoginWithTripit = false;
	
	public enum Values{
		USER_NAME,
		PASSWORD,
		RESERVATION_STATUS,
		HOTEL_ID,
		HOTEL_NAME,
		HOTEL_CITY,
		HOTEL_COUNTRY,
		HOTEL_RATING,
		HOTEL_REVIEW_COUNT,
		HOTEL_IMAGE,
		ROOM_NO,
		CHECK_IN_DATE,
		CHECK_IT_TIME,
		CHECK_OUT_DATE,
		CHECK_OUT_TIME,
		USER_PREF,
		LOGIN_INFO,
		SIGNIN_CHECK,
		ISSIGNIN,
		ISFBLOGIN,
		ISGPLUSLOGIN,
		FNAME,
		LNAME,
		USER_ID,
		RESERVATION_ID,
		RESERVATION_TYPE,
		CHECKIN_STATUS,
		RESERVATION_CODE,
		TRAVERS_HOTEL_LIST,
		HOTELINFO,
		APP_PREFERENCE,
		IS_ICON_CREATED,
		FRONTDESK_NO,
		LAUNDRY_PICKUP_NO,
		VALET_PICKUP_NO,
		DONOT_DISTURB,
		DEVICE_INFO,
		DEVICE_ID,
		TUTORIALTRAVERSE,
		ISTRAVERSETUTORIAL,
		ISUSER_AUTHENTICATED;
	}
}
