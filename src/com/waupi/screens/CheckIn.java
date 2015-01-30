package com.waupi.screens;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

public class CheckIn extends BaseScreen{

	private LinearLayout ll_slider;
	private SlidingMenu slidingMenu;
	private Intent mIntent = null; 
	private TextView tv_hotel_name,tv_phone_no,tv_address,tv_user_name,tv_username,tv_reviews,tv_checkin_reviews;
	private LinearLayout ll_etashuttle,ll_checkin,ll_mycoupon,ll_housekeeping,ll_exploreicon,ll_connect,ll_feedback,ll_logout;
	private RatingBar review_rating,rb_rating;
	private ImageView iv_hotel;
	private ImageLoader imageloader;
	private LinearLayout ll_bg;
	int hotel_id = 5;
	Drawable dr;
	String address,rating,phone_no,reviews,image_url;
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkin);
		
		imageloader = new ImageLoader(CheckIn.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(CheckIn.this,
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
        
        ll_slider = (LinearLayout)findViewById(R.id.ll_slider);
        ll_slider.setOnClickListener(this);
        
        review_rating = (RatingBar)findViewById(R.id.review_rating);
        rb_rating = (RatingBar)findViewById(R.id.rb_rating);
        tv_reviews = (TextView)findViewById(R.id.tv_reviews);
        
        iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
        iv_hotel.setOnClickListener(this);
        
        ll_etashuttle = (LinearLayout)findViewById(R.id.ll_etashuttle);
        ll_etashuttle.setOnClickListener(this);
        
        ll_mycoupon = (LinearLayout)findViewById(R.id.ll_mycoupon);
		ll_mycoupon.setOnClickListener(this);
        
        ll_exploreicon = (LinearLayout)findViewById(R.id.ll_expolreicon);
        ll_exploreicon.setOnClickListener(this);
        
        ll_checkin = (LinearLayout)findViewById(R.id.ll_checkin);
        ll_checkin.setOnClickListener(this);
        
        ll_housekeeping = (LinearLayout)findViewById(R.id.ll_housekeeping);
        ll_housekeeping.setOnClickListener(this);
        
        ll_connect = (LinearLayout)findViewById(R.id.ll_connect);
        ll_connect.setOnClickListener(this);
        
        ll_feedback = (LinearLayout)findViewById(R.id.ll_feedback);
        ll_feedback.setOnClickListener(this);
        
        ll_logout = (LinearLayout)findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this);
        
        tv_hotel_name = (TextView)findViewById(R.id.tv_hotel_name);
        tv_user_name = (TextView)findViewById(R.id.tv_user_name);
        tv_username = (TextView)findViewById(R.id.tv_username);
        
        tv_checkin_reviews = (TextView)findViewById(R.id.tv_checkin_reviews);
        tv_address = (TextView)findViewById(R.id.tv_address);
        tv_phone_no = (TextView)findViewById(R.id.tv_phone_no);
        ll_bg = (LinearLayout)findViewById(R.id.ll_bg);
        
	}
	
	private void gethotelCheckIninfo() {
		Thread t = new Thread(){
			public void run(){
				doShowLoading();
				callInfoDetail();
				doRemoveLoading();
			}
		};
		t.start();
	}
	
	private void callInfoDetail() {
		String response = HttpClient.getInstance(CheckIn.this).SendHttpGet(Constant.CHECKIN_LIST_INFO);
		if(response != null){
			try {
				JSONObject ob = new JSONObject(response);
				address = ob.getString("address");
				rating = ob.getString("rating");
				phone_no = ob.getString("front_desk_no");
				reviews = ob.getString("reviews_count");
				
				JSONObject obj = ob.getJSONObject("picture");
				JSONObject object = obj.getJSONObject("image");
				image_url = object.getString("url");
				Bitmap myImage = getBitmapFromURL(Constant.URL+image_url);
				dr = new BitmapDrawable(myImage);
				
				UpdateUi();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void UpdateUi() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				tv_address.setText(address);
				tv_phone_no.setText(phone_no);
				tv_checkin_reviews.setText(reviews+" Reviews");
				rb_rating.setRating(Float.parseFloat(rating));
				//imageloader.DisplayImage(Constant.URL+image_url, ll_bg);
				
				ll_bg.setBackgroundDrawable(dr);
			}
		});
	}

	public Bitmap getBitmapFromURL(String imageUrl) {
	    try {
	        URL url = new URL(imageUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!app.getTutorialInfo().isAuthenticated()){
			tv_user_name.setText("Welcome "+app.getUserInfo().fname);
			tv_username.setText("Welcome "+app.getUserInfo().fname);
			review_rating.setRating(Float.parseFloat("3"));			
			tv_reviews.setText("10 Reviews");	
			tv_phone_no.setText("314-XXX-XXXX");
		}else{
			
			gethotelCheckIninfo();
			
			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			tv_user_name.setText(""+app.getUserInfo().fname+" "+app.getUserInfo().lname);
			tv_username.setText("Welcome "+app.getUserInfo().fname);
			
			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			review_rating.setRating(Float.parseFloat(app.getHotelInfo().hotelrating));
			if(app.getHotelInfo().hoteleview_count.equalsIgnoreCase("0")){
				tv_reviews.setText(app.getHotelInfo().hoteleview_count+" Review");
			}else{
				tv_reviews.setText(app.getHotelInfo().hoteleview_count+" Reviews");
			}
			if(app.getHotelInfo().hotelimage.equalsIgnoreCase("")){
				iv_hotel.setImageResource(R.drawable.hotel_no_img);
			}else{
				imageloader.DisplayImage(Constant.URL+app.getHotelInfo().hotelimage, iv_hotel);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_slider:
			slidingMenu.toggle();
			break;
			
		case R.id.ll_mycoupon:
			mIntent = new Intent(CheckIn.this,MyCoupon.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_etashuttle:
			mIntent = new Intent(CheckIn.this,EtaShuttle.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_checkin:
			slidingMenu.toggle();
			break;
			
		case R.id.ll_housekeeping:
			mIntent = new Intent(CheckIn.this,Housekeeping.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_expolreicon:
			mIntent = new Intent(CheckIn.this,ExploreHotel.class);				
			startActivity(mIntent);
			slidingMenu.toggle();
			finish();
			break;
			
		case R.id.ll_connect:
			mIntent = new Intent(CheckIn.this,Connect.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_feedback:
			mIntent = new Intent(CheckIn.this,Feedback.class);				
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
		final Dialog dialog = new Dialog(CheckIn.this);
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

				Intent i = new Intent(CheckIn.this,CheckIn.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
				Intent i = new Intent(CheckIn.this,DashBorad.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
			Intent i = new Intent(CheckIn.this, CheckIn.class);
			startActivity(i);
			CheckIn.this.finish();
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
