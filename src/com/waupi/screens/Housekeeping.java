package com.waupi.screens;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;

public class Housekeeping extends BaseScreen{
	
	private LinearLayout ll_setwakeup_call,ll_valet,ll_donot_disturb,ll_laundry_pickup;
	private LinearLayout ll_slider;
	private SlidingMenu slidingMenu;
	private Intent mIntent = null; 
	private ImageView iv_hotel,iv_image_hotel;
	private TextView tv_name,tv_roomno;
	private TextView tv_hotel_name,tv_username,tv_user_name,tv_reviews;
	private LinearLayout ll_etashuttle,ll_mycoupon,ll_checkin,ll_logout,ll_housekeeping,ll_exploreicon,ll_connect,ll_feedback;
	
	private RatingBar review_rating;
	private ImageLoader imageloader;
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.house_keeping);		
		
		imageloader = new ImageLoader(Housekeeping.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Housekeeping.this,
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
		
		ll_setwakeup_call = (LinearLayout)findViewById(R.id.ll_setwakeup_call);
		ll_setwakeup_call.setOnClickListener(this);
		
		ll_valet = (LinearLayout)findViewById(R.id.ll_valet);
		ll_valet.setOnClickListener(this);
		
		ll_donot_disturb = (LinearLayout)findViewById(R.id.ll_donot_disturb);
		ll_donot_disturb.setOnClickListener(this);
		
		ll_slider = (LinearLayout)findViewById(R.id.ll_slider);
        ll_slider.setOnClickListener(this);
        
        ll_laundry_pickup = (LinearLayout)findViewById(R.id.ll_laundry_pickup);
        ll_laundry_pickup.setOnClickListener(this);
        
        iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
        iv_hotel.setOnClickListener(this);
        
      	tv_name = (TextView)findViewById(R.id.tv_name);
      	tv_roomno = (TextView)findViewById(R.id.tv_roomno);
      	
      	ll_etashuttle = (LinearLayout)findViewById(R.id.ll_etashuttle);
        ll_etashuttle.setOnClickListener(this);
        
        ll_exploreicon = (LinearLayout)findViewById(R.id.ll_expolreicon);
        ll_exploreicon.setOnClickListener(this);
        
        ll_checkin = (LinearLayout)findViewById(R.id.ll_checkin);
        ll_checkin.setOnClickListener(this);
        
        ll_housekeeping = (LinearLayout)findViewById(R.id.ll_housekeeping);
        ll_housekeeping.setOnClickListener(this);
        
        ll_mycoupon = (LinearLayout)findViewById(R.id.ll_mycoupon);
		ll_mycoupon.setOnClickListener(this);
        
        ll_connect = (LinearLayout)findViewById(R.id.ll_connect);
        ll_connect.setOnClickListener(this);
        
        ll_feedback = (LinearLayout)findViewById(R.id.ll_feedback);
        ll_feedback.setOnClickListener(this);
        
        ll_logout = (LinearLayout)findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this);
        
        tv_hotel_name = (TextView)findViewById(R.id.tv_hotel_name);
        tv_username = (TextView)findViewById(R.id.tv_username);
        
        review_rating = (RatingBar)findViewById(R.id.review_rating);
        tv_reviews = (TextView)findViewById(R.id.tv_reviews);
        iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
        iv_image_hotel = (ImageView)findViewById(R.id.iv_image_hotel);
        
        tv_roomno = (TextView)findViewById(R.id.tv_roomno);
	}
	
	@Override
	public void init() {
		super.init();
		if(!app.getTutorialInfo().isAuthenticated()){
			tv_username.setText("Welcome "+app.getUserInfo().fname);
			tv_name.setText("Welcome "+app.getUserInfo().fname);
			review_rating.setRating(Float.parseFloat("3"));			
			tv_reviews.setText("10 Reviews");			
		}else{
			
			tv_roomno.setText("");
			
			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			tv_name.setText("Welcome "+app.getUserInfo().fname);
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
				iv_image_hotel.setImageResource(R.drawable.hotel_no_img);
			}else{
				imageloader.DisplayImage(Constant.URL+app.getHotelInfo().hotelimage, iv_hotel);
				imageloader.DisplayImage(Constant.URL+app.getHotelInfo().hotelimage, iv_image_hotel);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_slider:
			slidingMenu.toggle();
			break;
			
		case R.id.ll_valet:
			mIntent = new Intent(Housekeeping.this,Valet.class);				
			startActivity(mIntent);
			//finish();
			break;
			
		case R.id.ll_laundry_pickup:
			mIntent = new Intent(Housekeeping.this,SchedulePickup.class);				
			startActivity(mIntent);
			//finish();
			break;
			
		case R.id.ll_donot_disturb:
			mIntent = new Intent(Housekeeping.this,DoNotDisturbScreen.class);				
			startActivity(mIntent);
			//finish();
			break;
			
		case R.id.ll_setwakeup_call:
			mIntent = new Intent(Housekeeping.this,WakeUpCall.class);				
			startActivity(mIntent);
			//finish();
			break;
			
		case R.id.ll_mycoupon:
			mIntent = new Intent(Housekeeping.this,MyCoupon.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_etashuttle:
			mIntent = new Intent(Housekeeping.this,EtaShuttle.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_checkin:
			mIntent = new Intent(Housekeeping.this,CheckIn.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_housekeeping:
			slidingMenu.toggle();
			break;
			
		case R.id.ll_expolreicon:
			mIntent = new Intent(Housekeeping.this,ExploreHotel.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_connect:
			mIntent = new Intent(Housekeeping.this,Connect.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_feedback:
			mIntent = new Intent(Housekeeping.this,Feedback.class);				
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
		Thread t = new Thread(){
			public void run(){
				try {
					String response = HttpClient.getInstance(getApplicationContext()).SendHttpDelete(Constant.LOGOUT);
					if(response != null){
						JSONObject obj = new JSONObject(response);
						if(obj.getBoolean("status")){
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
	
	public void showAccessAcknowledgeDialog(final String message) {
		final Dialog dialog = new Dialog(Housekeeping.this);
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

				Intent i = new Intent(Housekeeping.this,Housekeeping.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				dialog.cancel();
			}
		});
		dialog.show();
	}

	private void gotoLoginScreen() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Housekeeping.this,DashBorad.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("status", true);
				startActivity(i);
				overridePendingTransition(R.anim.anim1, R.anim.anim2);
				finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(pref.getBoolean("DUMMY_ACCESS", false)){
			Editor edit = pref.edit();
			edit.putBoolean("DUMMY_ACCESS", false);
			edit.commit();
			Intent i = new Intent(Housekeeping.this, Housekeeping.class);
			startActivity(i);
			Housekeeping.this.finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		finish();
	}
}
