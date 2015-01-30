package com.waupi.screens;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.Session;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.waupi.adapter.ViewPagerAdapter;
import com.waupi.bean.AmenitiesBean;
import com.waupi.bean.AtmBean;
import com.waupi.bean.DealsBean;
import com.waupi.bean.EventsBean;
import com.waupi.bean.RestaurantBean;
import com.waupi.bean.RestaurantMenuBean;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;

public class ExploreHotel extends BaseScreen {

	private LinearLayout ll_slider;
	private SlidingMenu slidingMenu;
	private Intent mIntent = null;
	private String imageurl = null;
	private LinearLayout ll_etashuttle, ll_checkin, ll_housekeeping,
			ll_exploreicon, ll_connect, ll_logout, ll_feedback,ll_mycoupon;

	private ViewPager pager;
	private ViewPagerAdapter _adapter;
	private LinearLayout ll_aminity_bar, ll_aminity_bg, ll_restaurant_bar,
			ll_restaurent_bg, ll_atm_bar, ll_atm_bg, ll_events_bar,
			ll_events_bg, ll_events, ll_atm, ll_restaurant, ll_aminity;
	private ImageView iv_restaurant, iv_atm, iv_event, iv_aminity;
	private TextView tv_events_text, tv_atm_text, tv_restaurant_text,
			tv_aminity_text;
	private TextView tv_hotel_name,tv_username,tv_reviews;
	
	private RatingBar review_rating;
	private ImageView iv_hotel;
	private ImageLoader imageloader;
	
	private ArrayList<RestaurantBean> restaurantList = new ArrayList<RestaurantBean>();
	private ArrayList<AtmBean> atmList = new ArrayList<AtmBean>();
	private ArrayList<EventsBean> eventList = new ArrayList<EventsBean>();
	private ArrayList<AmenitiesBean> amenitiesList = new ArrayList<AmenitiesBean>();
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.explore_hotel);
		
		imageloader = new ImageLoader(ExploreHotel.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(ExploreHotel.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setShadowWidth(15);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.dashboard_slider);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		ll_slider = (LinearLayout) findViewById(R.id.ll_slider);
		ll_slider.setOnClickListener(this);

		ll_etashuttle = (LinearLayout) findViewById(R.id.ll_etashuttle);
		ll_etashuttle.setOnClickListener(this);

		ll_exploreicon = (LinearLayout) findViewById(R.id.ll_expolreicon);
		ll_exploreicon.setOnClickListener(this);

		ll_checkin = (LinearLayout) findViewById(R.id.ll_checkin);
		ll_checkin.setOnClickListener(this);
		
		ll_mycoupon = (LinearLayout)findViewById(R.id.ll_mycoupon);
		ll_mycoupon.setOnClickListener(this);

		ll_housekeeping = (LinearLayout) findViewById(R.id.ll_housekeeping);
		ll_housekeeping.setOnClickListener(this);

		ll_connect = (LinearLayout) findViewById(R.id.ll_connect);
		ll_connect.setOnClickListener(this);

		ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);
		ll_feedback.setOnClickListener(this);

		ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
		ll_logout.setOnClickListener(this);
		
		tv_hotel_name = (TextView)findViewById(R.id.tv_hotel_name);
		tv_username = (TextView)findViewById(R.id.tv_username);
		
		review_rating = (RatingBar)findViewById(R.id.review_rating);
        tv_reviews = (TextView)findViewById(R.id.tv_reviews);
        
        iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
        iv_hotel.setOnClickListener(this);

	}

	@Override
	public void init() {
		if(!app.getTutorialInfo().isAuthenticated()){
			tv_username.setText("Welcome "+app.getUserInfo().fname);
			review_rating.setRating(Float.parseFloat("3"));			
			tv_reviews.setText("10 Reviews");	
			
			if(restaurantList.size() == 0){
				fetchExploreHotelInfo("demo");
			}
		}else{
			if(restaurantList.size() == 0){
				fetchExploreHotelInfo("real");
				
				tv_hotel_name.setText(app.getHotelInfo().hotelName);
				tv_username.setText("Welcome "+app.getUserInfo().fname);
				
				tv_hotel_name.setText(app.getHotelInfo().hotelName);
				review_rating.setRating(Float.parseFloat(app.getHotelInfo().hotelrating));
				if(app.getHotelInfo().hoteleview_count.equalsIgnoreCase("0")){
					tv_reviews.setText(app.getHotelInfo().hoteleview_count+" Review");
				}else{
					tv_reviews.setText(app.getHotelInfo().hoteleview_count+" Reviews");
				}
				if(app.getHotelInfo().hotelimage.length() <= 0){
					iv_hotel.setImageResource(R.drawable.hotel_no_img);
				}else{
					imageloader.DisplayImage(Constant.URL+app.getHotelInfo().hotelimage, iv_hotel);
				}
			}
		}
		super.init();
	}
	
	private void fetchExploreHotelInfo(final String requestType) {
		Thread t = new Thread(){
			public void run(){
				doShowLoading();
				callWebservice(requestType);
				doRemoveLoading();
			}
		};
		t.start();
	}
	
	private void callWebservice(String requestType) {
		try {
			String response = null;
			if(requestType.equalsIgnoreCase("demo")){
				response = HttpClient.getInstance(getApplicationContext()).SendHttpGet(Constant.DEMO_EXPLORE_HOTEL);
			}else if(requestType.equalsIgnoreCase("real")){
				response = HttpClient.getInstance(getApplicationContext()).SendHttpGet(Constant.EXPLORE_HOTEL+"5");
			}
			if(response != null){
				JSONObject obj = new JSONObject(response);
				JSONArray arr1 = obj.getJSONArray("restaurants");
				for(int i = 0;i< arr1.length();i++){
					JSONObject c = arr1.getJSONObject(i);
					
					JSONObject object = c.getJSONObject("icon");
					if(object.getString("url") != null){
						imageurl = object.getString("url");
					}else{
						imageurl = "";
					}						
					JSONArray arr = c.getJSONArray("restaurant_menus");
					ArrayList<RestaurantMenuBean> menuBean = new ArrayList<RestaurantMenuBean>();
					for(int j = 0;j<arr.length();j++){
						JSONObject jk = arr.getJSONObject(j);
						JSONObject jimage = jk.getJSONObject("item_img");
						String image = jimage.getString("url");
						String rating;
						if(!jk.isNull("rating")){
							rating = Integer.toString(jk.getInt("rating"));
						}else{
							rating = "0.0f";
						}
						menuBean.add(new RestaurantMenuBean(jk.getString("id"),
								jk.getString("item_name"),
								rating,
								jk.getString("item_desc"),
								image));
					}
					
					JSONArray dealsarr = c.getJSONArray("coupons");
					ArrayList<DealsBean> dealsBean = new ArrayList<DealsBean>();
					for(int j = 0;j<dealsarr.length();j++){
						JSONObject jk = dealsarr.getJSONObject(j);
						JSONObject jimage = jk.getJSONObject("image");
						String image ;
						if(jimage.isNull("url")){
							image = "";
						}else{
							image = jimage.getString("url");
						}
						String desc = jk.getString("content");
						String validity = jk.getString("validity");
						String[] validityarr = validity.split("\\s+");
						
						dealsBean.add(new DealsBean(jk.getString("id"),
								"00", desc, 
								image,
								jk.getString("restaurant_id"),
								validityarr[0]));
					}
					
					restaurantList.add(new RestaurantBean(c.getString("id"), 
							c.getString("name"),
							c.getString("address"),
							c.getString("rating"),
							imageurl,
							c.getString("four_square_id"),
							menuBean,
							dealsBean));
				}
				Constant.totalList = restaurantList;
				
				JSONArray arr2 = obj.getJSONArray("atms");
				for(int i = 0;i< arr2.length();i++){
					JSONObject c = arr2.getJSONObject(i);
					
					atmList.add(new AtmBean(c.getString("id"),
							c.getString("name"),
							c.getString("address"),
							c.getString("latitude"),
							c.getString("longitude")));
				}
				
				JSONArray arr3 = obj.getJSONArray("events");
				for(int i = 0;i< arr3.length();i++){
					JSONObject c = arr3.getJSONObject(i);
					
					JSONObject object = c.getJSONObject("icon");
					if(object.getString("url") != null){
						imageurl = object.getString("url");
					}else{
						imageurl = "";
					}					
					eventList.add(new EventsBean(c.getString("id"),
							c.getString("name"),
							c.getString("description"),
							c.getString("rating"),
							imageurl));
				}
				
				JSONArray arr4 = obj.getJSONArray("amenities");
				for(int i = 0;i< arr4.length();i++){
					JSONObject c = arr4.getJSONObject(i);
					
					JSONObject object = c.getJSONObject("icon");
					if(!object.isNull("url")){
						imageurl = object.getString("url");
					}else{
						imageurl = "";
					}
					amenitiesList.add(new AmenitiesBean(c.getString("id"),
							c.getString("name"),
							c.getString("created_at"),
							c.getString("description"),
							imageurl,
							c.getString("rating")));
				}
				setUpView(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setUpView(final int arg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pager = (ViewPager) findViewById(R.id.viewPager);
				_adapter = new ViewPagerAdapter(ExploreHotel.this, getSupportFragmentManager(),restaurantList,atmList,eventList,amenitiesList);
				pager.setAdapter(_adapter);
				pager.setCurrentItem(arg, false);
				initImage();
			}
		});
	}

	private void initImage() {
		ll_events = (LinearLayout) findViewById(R.id.ll_events);
		ll_atm = (LinearLayout) findViewById(R.id.ll_atm);
		ll_restaurant = (LinearLayout) findViewById(R.id.ll_restaurant);
		ll_aminity = (LinearLayout) findViewById(R.id.ll_aminity);

		ll_restaurant_bar = (LinearLayout) findViewById(R.id.ll_restaurant_bar);
		ll_restaurent_bg = (LinearLayout) findViewById(R.id.ll_restaurent_bg);
		iv_restaurant = (ImageView) findViewById(R.id.iv_restaurant);
		tv_restaurant_text = (TextView) findViewById(R.id.tv_restaurant_text);

		ll_atm_bar = (LinearLayout) findViewById(R.id.ll_atm_bar);
		ll_atm_bg = (LinearLayout) findViewById(R.id.ll_atm_bg);
		iv_atm = (ImageView) findViewById(R.id.iv_atm);
		tv_atm_text = (TextView) findViewById(R.id.tv_atm_text);

		ll_events_bar = (LinearLayout) findViewById(R.id.ll_events_bar);
		ll_events_bg = (LinearLayout) findViewById(R.id.ll_events_bg);
		iv_event = (ImageView) findViewById(R.id.iv_event);
		tv_events_text = (TextView) findViewById(R.id.tv_events_text);

		ll_aminity_bar = (LinearLayout) findViewById(R.id.ll_aminity_bar);
		ll_aminity_bg = (LinearLayout) findViewById(R.id.ll_aminity_bg);
		iv_aminity = (ImageView) findViewById(R.id.iv_aminity);
		tv_aminity_text = (TextView) findViewById(R.id.tv_aminity_text);

		ll_events.setOnClickListener(this);
		ll_atm.setOnClickListener(this);
		ll_restaurant.setOnClickListener(this);
		ll_aminity.setOnClickListener(this);

		ImageAction(0);

		setTab();
	}

	private void setTab() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				ImageAction(position);
			}
		});
	}

	private void ImageAction(int action) {
		switch (action) {
		case 0:

			ll_restaurant_bar.setBackgroundColor(Color.BLACK);
			ll_restaurent_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_restaurant.setImageResource(R.drawable.restaurent_icon);
			tv_restaurant_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_atm_bar.setBackgroundColor(Color.BLACK);
			ll_atm_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_atm.setImageResource(R.drawable.atm_icon);
			tv_atm_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_events_bar.setBackgroundColor(Color.BLACK);
			ll_events_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_event.setImageResource(R.drawable.event_icon);
			tv_events_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_aminity_bar.setBackgroundColor(Color.WHITE);
			ll_aminity_bg.setBackgroundColor(Color.parseColor("#28a495"));
			iv_aminity.setImageResource(R.drawable.aminities_icon);
			tv_aminity_text.setTextColor(Color.WHITE);

			break;
		case 1:

			ll_restaurant_bar.setBackgroundColor(Color.WHITE);
			ll_restaurent_bg.setBackgroundColor(Color.parseColor("#28a495"));
			iv_restaurant.setImageResource(R.drawable.restaurent_icon_hover);
			tv_restaurant_text.setTextColor(Color.WHITE);

			ll_atm_bar.setBackgroundColor(Color.BLACK);
			ll_atm_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_atm.setImageResource(R.drawable.atm_icon);
			tv_atm_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_events_bar.setBackgroundColor(Color.BLACK);
			ll_events_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_event.setImageResource(R.drawable.event_icon);
			tv_events_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_aminity_bar.setBackgroundColor(Color.BLACK);
			ll_aminity_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_aminity.setImageResource(R.drawable.aminities_icon_hover);
			tv_aminity_text.setTextColor(Color.parseColor("#39cbb9"));

			break;

		case 2:

			ll_restaurant_bar.setBackgroundColor(Color.BLACK);
			ll_restaurent_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_restaurant.setImageResource(R.drawable.restaurent_icon);
			tv_restaurant_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_atm_bar.setBackgroundColor(Color.WHITE);
			ll_atm_bg.setBackgroundColor(Color.parseColor("#28a495"));
			iv_atm.setImageResource(R.drawable.atm_icon_hover);
			tv_atm_text.setTextColor(Color.WHITE);

			ll_events_bar.setBackgroundColor(Color.BLACK);
			ll_events_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_event.setImageResource(R.drawable.event_icon);
			tv_events_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_aminity_bar.setBackgroundColor(Color.BLACK);
			ll_aminity_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_aminity.setImageResource(R.drawable.aminities_icon_hover);
			tv_aminity_text.setTextColor(Color.parseColor("#39cbb9"));

			break;

		case 3:
			ll_restaurant_bar.setBackgroundColor(Color.BLACK);
			ll_restaurent_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_restaurant.setImageResource(R.drawable.restaurent_icon);
			tv_restaurant_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_atm_bar.setBackgroundColor(Color.BLACK);
			ll_atm_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_atm.setImageResource(R.drawable.atm_icon);
			tv_atm_text.setTextColor(Color.parseColor("#39cbb9"));

			ll_events_bar.setBackgroundColor(Color.WHITE);
			ll_events_bg.setBackgroundColor(Color.parseColor("#28a495"));
			iv_aminity.setImageResource(R.drawable.event_icon);
			tv_events_text.setTextColor(Color.WHITE);

			ll_aminity_bar.setBackgroundColor(Color.BLACK);
			ll_aminity_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_aminity.setImageResource(R.drawable.aminities_icon_hover);
			tv_aminity_text.setTextColor(Color.parseColor("#39cbb9"));

			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_restaurant:
			ImageAction(1);
			pager.setCurrentItem(1, false);
			break;

		case R.id.ll_atm:
			ImageAction(2);
			pager.setCurrentItem(2, false);
			break;

		case R.id.ll_events:
			ImageAction(3);
			pager.setCurrentItem(3, false);
			break;

		case R.id.ll_aminity:
			ImageAction(0);
			pager.setCurrentItem(0, false);
			break;

		case R.id.ll_slider:
			slidingMenu.toggle();
			break;

		case R.id.ll_etashuttle:
			mIntent = new Intent(ExploreHotel.this, EtaShuttle.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_checkin:
			mIntent = new Intent(ExploreHotel.this, CheckIn.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_housekeeping:
			mIntent = new Intent(ExploreHotel.this, Housekeeping.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_mycoupon:
			mIntent = new Intent(ExploreHotel.this,MyCoupon.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_expolreicon:
			slidingMenu.toggle();
			break;

		case R.id.ll_connect:
			mIntent = new Intent(ExploreHotel.this, Connect.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_feedback:
			mIntent = new Intent(ExploreHotel.this, Feedback.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_logout:
			sendLogoutSatusToserver();
			break;
			
		case R.id.iv_hotel:
			slidingMenu.toggle();
			break;
		}
	}

	private void sendLogoutSatusToserver() {
		Thread t = new Thread() {
			public void run() {
				try {
					String response = HttpClient.getInstance(
							getApplicationContext()).SendHttpDelete(
							Constant.LOGOUT);
					if (response != null) {
						JSONObject obj = new JSONObject(response);
						if (obj.getBoolean("status")) {
							app.getLogininfo().isLogin = false;
							app.getLogininfo().setLoginInfo(false);
							if(Session.getActiveSession() != null){
								Session.getActiveSession().closeAndClearTokenInformation();
								Session.setActiveSession(null);
							}
							gotoLoginScreen();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	private void gotoLoginScreen() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(ExploreHotel.this, DashBorad.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("status", true);
				startActivity(i);
				overridePendingTransition(R.anim.anim1, R.anim.anim2);
				finish();
			}
		});
	}

	@Override
	public void startActivityFromFragment(Fragment fragment, Intent intent,
			int requestCode) {
		removeMapFragment();
		super.startActivityFromFragment(fragment, intent, requestCode);
	}

	private void removeMapFragment() {
		Fragment map = getSupportFragmentManager().findFragmentById(R.id.map);
		if (map != null) {
			getSupportFragmentManager().beginTransaction().remove(map)
					.commitAllowingStateLoss();
		}
	}
	
	public void showAccessAcknowledgeDialog(final String message) {
		final Dialog dialog = new Dialog(ExploreHotel.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.user_authaccess_dialog);
		dialog.setCancelable(false);

		TextView tv_text = (TextView) dialog.findViewById(R.id.tv_text);
		tv_text.setText(message);

		Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(ExploreHotel.this,ExploreHotel.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				dialog.cancel();
			}
		});
		dialog.show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(pref.getBoolean("DUMMY_ACCESS", false)){
			Editor edit = pref.edit();
			edit.putBoolean("DUMMY_ACCESS", false);
			edit.commit();
			Intent i = new Intent(ExploreHotel.this, ExploreHotel.class);
			startActivity(i);
			ExploreHotel.this.finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		removeMapFragment();
		super.onBackPressed();
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		finish();
	}
}
