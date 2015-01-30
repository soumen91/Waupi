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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;

public class Feedback extends BaseScreen implements /*OnTouchListener*/ OnRatingBarChangeListener{
	private LinearLayout ll_slider,ll_submit;
	private Intent mIntent = null; 
	private ImageView iv_hotel,iv_hotel_image;
	private SlidingMenu slidingMenu;
	private TextView tv_hotel_name;
	private EditText et_comment;
	private RatingBar experiance_rating,rooms_rating,customerservice_rating,overall_rating;
	private TextView tv_phone_num,tv_name,tv_roomno,tv_username,tv_user_name,tv_reviews;
	private LinearLayout ll_etashuttle,ll_mycoupon,ll_checkin,ll_housekeeping,ll_exploreicon,ll_connect,ll_feedback,ll_logout;
	String comment = null;
	int exp_rating,romms_rating,customer_service_rating,over_all_rating = 0;
	
	private RatingBar review_rating;
	private ImageLoader imageloader;
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		imageloader = new ImageLoader(Feedback.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Feedback.this,
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
        
        iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
        iv_hotel_image = (ImageView)findViewById(R.id.iv_hotel_image);
      	tv_name = (TextView)findViewById(R.id.tv_name);
      	
      	review_rating = (RatingBar)findViewById(R.id.review_rating);
        tv_reviews = (TextView)findViewById(R.id.tv_reviews);
        
        iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
        iv_hotel.setOnClickListener(this);
      	
      	tv_phone_num = (TextView)findViewById(R.id.tv_phone_num);
      	tv_roomno = (TextView)findViewById(R.id.tv_roomno);
      	
      	ll_slider = (LinearLayout)findViewById(R.id.ll_slider);
        ll_slider.setOnClickListener(this);
        
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
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_username = (TextView)findViewById(R.id.tv_username);
        
        et_comment = (EditText)findViewById(R.id.et_comment);
        
        ll_submit = (LinearLayout)findViewById(R.id.ll_submit);
        ll_submit.setOnClickListener(this);
        
        experiance_rating = (RatingBar)findViewById(R.id.experiance_rating);
        //experiance_rating.setOnTouchListener(this);
        experiance_rating.setOnRatingBarChangeListener(this);
        exp_rating = (int)experiance_rating.getRating();
        
        rooms_rating = (RatingBar)findViewById(R.id.rooms_rating);
       // rooms_rating.setOnTouchListener(this);
        romms_rating = (int)rooms_rating.getRating();
        
        customerservice_rating = (RatingBar)findViewById(R.id.customerservice_rating);
      //  customerservice_rating.setOnTouchListener(this);
        customer_service_rating = (int)customerservice_rating.getRating();
        
        overall_rating = (RatingBar)findViewById(R.id.overall_rating);
       // overall_rating.setOnTouchListener(this);
        over_all_rating = (int)overall_rating.getRating();
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!app.getTutorialInfo().isAuthenticated()){
			tv_username.setText("Welcome "+app.getUserInfo().fname);
			tv_name.setText("Welcome "+app.getUserInfo().fname);
			review_rating.setRating(Float.parseFloat("3"));			
			tv_reviews.setText("10 Reviews");	
			ll_submit.setClickable(false);
		}else{
			ll_submit.setClickable(true);
			
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
			if(app.getHotelInfo().hotelimage.equalsIgnoreCase("")){
				iv_hotel.setImageResource(R.drawable.hotel_no_img);
				iv_hotel_image.setImageResource(R.drawable.hotel_no_img);
			}else{
				imageloader.DisplayImage(Constant.URL+app.getHotelInfo().hotelimage, iv_hotel);
				imageloader.DisplayImage(Constant.URL+app.getHotelInfo().hotelimage, iv_hotel_image);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_slider:
			slidingMenu.toggle();
			break;
			
		case R.id.ll_etashuttle:
			mIntent = new Intent(Feedback.this,EtaShuttle.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_mycoupon:
			mIntent = new Intent(Feedback.this,MyCoupon.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_checkin:
			mIntent = new Intent(Feedback.this,CheckIn.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_housekeeping:
			mIntent = new Intent(Feedback.this,Housekeeping.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_expolreicon:
			mIntent = new Intent(Feedback.this,ExploreHotel.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_connect:
			mIntent = new Intent(Feedback.this,Connect.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_feedback:
			slidingMenu.toggle();
			break;
			
		case R.id.ll_logout:
			sendLogoutSatusToserver();
			break;
			
		case R.id.ll_submit:
			sendFeedbackToServer();
			break;
			
		case R.id.iv_hotel:
			slidingMenu.toggle();
			break;
		}
	}
	
	private void sendFeedbackToServer() {
		if(isValid()){
		
			Thread t = new Thread(){
				public void run(){
					doShowLoading();
					callServerToSendFeedback();
					doRemoveLoading();
				}
			};
			t.start();
		}
	}
	
	private void callServerToSendFeedback() {
		try {
			JSONObject ob = new JSONObject();
			
			ob.put("user_id", app.getUserInfo().user_id);
			ob.put("hotel_id", "5");
			JSONObject obj = new JSONObject();
			if(exp_rating == 0){
				obj.put("experience", "");
			}else{
				obj.put("experience", ""+exp_rating);
			}
			if(romms_rating == 0){
				obj.put("rooms", "");
			}else{
				obj.put("rooms", ""+romms_rating);
			}
			if(customer_service_rating == 0){
				obj.put("customer_service", "");
			}else{
				obj.put("customer_service", ""+customer_service_rating);
			}
			if(over_all_rating == 0){
				obj.put("overall", "");
			}else{
				obj.put("overall", ""+over_all_rating);
			}
		
			obj.put("comment", comment);
			ob.put("feedback", obj);
			
			String response = HttpClient.getInstance(getApplicationContext()).SendHttpPost(Constant.FEEDBACK, ob.toString());
			if(response != null){
				JSONObject object = new JSONObject(response);
				if(object.getBoolean("status")){
					ShowSuccessMessage();
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void ShowSuccessMessage() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Feedback.this, "Thanks for your feedback!!", 5000).show();
				experiance_rating.setRating(1);
		        rooms_rating.setRating(1);
		        customerservice_rating.setRating(1);
		        overall_rating.setRating(1);		        
			}
		});
	}
	private boolean isValid() {
		boolean flag = true;
		if(et_comment.getText().toString().trim().length() == 0){
			et_comment.setError("Please enter your comment..");
			flag = false;
		}else{
			comment = et_comment.getText().toString().trim();
		}
		return flag;
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

	private void gotoLoginScreen() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Feedback.this,DashBorad.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("status", true);
				startActivity(i);
				overridePendingTransition(R.anim.anim1, R.anim.anim2);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		finish();
	}
	
	/*@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.experiance_rating:
			if(event.getAction() == MotionEvent.ACTION_UP){
				float touchpositionX = event.getX();
				float width = experiance_rating.getX();
				float starsrate = (touchpositionX/width) * 5.0f;
				exp_rating = (int)starsrate +1;
				experiance_rating.setRating(exp_rating);
			}if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setPressed(true);
            }

            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setPressed(false);
            }
			break;
			
		case R.id.rooms_rating:
			if(event.getAction() == MotionEvent.ACTION_UP){
				float motionPoint = event.getX();
				float width = rooms_rating.getX();
				float actualRating = (motionPoint/width) * 5.0f;
				romms_rating = (int)actualRating +1;
				rooms_rating.setRating(romms_rating);
			}if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setPressed(true);
			}if(event.getAction() == MotionEvent.ACTION_CANCEL){
				v.setPressed(false);
			}
			break;
			
		case R.id.customerservice_rating:
			if(event.getAction() == MotionEvent.ACTION_UP){
				float motionPoint = event.getX();
				float width = customerservice_rating.getX();
				float actualRating = (motionPoint/width) * 5.0f;
				customer_service_rating = (int)actualRating +1;
				customerservice_rating.setRating(customer_service_rating);
			}if(event.getAction() == MotionEvent.ACTION_DOWN){
				v.setPressed(true);
			}if(event.getAction() == MotionEvent.ACTION_CANCEL){
				v.setPressed(false);
			}
			break;
			
		case R.id.overall_rating:
			if(event.getAction() == MotionEvent.ACTION_UP){
				float motionpoint = event.getX();
				float width = overall_rating.getX();
				float overallRating = (motionpoint/width) * 5.0f;
				over_all_rating = (int)overallRating +1;
				overall_rating.setRating(over_all_rating);
			}if(event.getAction()== MotionEvent.ACTION_DOWN){
				v.setPressed(true);
			}if(event.getAction() == MotionEvent.ACTION_CANCEL){
				v.setPressed(false);
			}
		}
		return true;
	}*/
	
	public void showAccessAcknowledgeDialog(final String message) {
		if(!app.getTutorialInfo().isUserAuthenticated){
			final Dialog dialog = new Dialog(Feedback.this);
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
					app.getTutorialInfo().setUserAuthenticationInfo(true);
					Intent i = new Intent(Feedback.this,Feedback.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
					dialog.cancel();
				}
			});
			dialog.show();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(pref.getBoolean("DUMMY_ACCESS", false)){
			Editor edit = pref.edit();
			edit.putBoolean("DUMMY_ACCESS", false);
			edit.commit();
			Intent i = new Intent(Feedback.this, Feedback.class);
			startActivity(i);
			Feedback.this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
		switch(arg0.getId()){
		case R.id.customerservice_rating:
			//customerservice_rating.setRating(arg1);
		}
	}
}
