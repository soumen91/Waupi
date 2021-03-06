package com.waupi.screens;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;

public class DoNotDisturbScreen extends BaseScreen /*implements OnCheckedChangeListener*/{

	//private ToggleButton tgBtn;
	private Button btn_back;
	//private ImageView iv_toggle_image;
	private TextView tv_user_name,tv_roomno;
	private ImageView iv_hotel,iv_disturb_hotel;
	private LinearLayout ll_slider;
	private ImageLoader imageloader;
	//private SlidingMenu slidingMenu;
	
	private SharedPreferences pref;
	
	public Intent mIntent = null;
	private LinearLayout ll_etashuttle,ll_checkin,ll_housekeeping,ll_exploreicon,ll_connect,ll_feedback;
	
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donot_disturb);
		this.overridePendingTransition(R.anim.anim1, R.anim.anim2);
		
		imageloader = new ImageLoader(DoNotDisturbScreen.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(DoNotDisturbScreen.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		
		/*slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);       
        slidingMenu.setShadowWidth(15);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.dashboard_slider);*/
        
       /* ll_slider = (LinearLayout)findViewById(R.id.ll_slider);
        ll_slider.setOnClickListener(this);*/
		
		//iv_toggle_image = (ImageView)findViewById(R.id.iv_toggle_image);
		
		tv_roomno = (TextView)findViewById(R.id.tv_roomno);
		
		//iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
		tv_user_name = (TextView)findViewById(R.id.tv_user_name);
		iv_disturb_hotel = (ImageView)findViewById(R.id.iv_disturb_hotel);
		
		//tgBtn = (ToggleButton)findViewById(R.id.tg_btn);
		//tgBtn.setOnCheckedChangeListener(this);
		
	/*	ll_slider = (LinearLayout)findViewById(R.id.ll_slider);
        ll_slider.setOnClickListener(this);
        
        ll_etashuttle = (LinearLayout)findViewById(R.id.ll_etashuttle);
        ll_etashuttle.setOnClickListener(this);
        
        ll_exploreicon = (LinearLayout)findViewById(R.id.ll_expolreicon);
        ll_exploreicon.setOnClickListener(this);
        
        ll_checkin = (LinearLayout)findViewById(R.id.ll_checkin);
        ll_checkin.setOnClickListener(this);
        
        ll_housekeeping = (LinearLayout)findViewById(R.id.ll_housekeeping);
        ll_housekeeping.setOnClickListener(this);
        
        ll_connect = (LinearLayout)findViewById(R.id.ll_connect);
        ll_connect.setOnClickListener(this);
        
        ll_feedback = (LinearLayout)findViewById(R.id.ll_feedback);
        ll_feedback.setOnClickListener(this);*/
	}
	
	@Override
	public void init(){
		if(!app.getTutorialInfo().isAuthenticated()){
			tv_user_name.setText("Welcome "+app.getUserInfo().fname);		
		}else{
			
			tv_roomno.setText("");
			tv_user_name.setText("Welcome "+app.getUserInfo().fname);
			if(app.getHotelInfo().hotelimage.length() <= 0){
				iv_disturb_hotel.setImageResource(R.drawable.hotel_no_img);
			}else{
				imageloader.DisplayImage(Constant.URL+app.getHotelInfo().hotelimage, iv_disturb_hotel);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*case R.id.ll_slider:
			slidingMenu.toggle();
			break;*/
			
		case R.id.ll_etashuttle:
			mIntent = new Intent(DoNotDisturbScreen.this,EtaShuttle.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_checkin:
			mIntent = new Intent(DoNotDisturbScreen.this,CheckIn.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_housekeeping:
			mIntent = new Intent(DoNotDisturbScreen.this,Housekeeping.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_expolreicon:
			mIntent = new Intent(DoNotDisturbScreen.this,ExploreHotel.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_connect:
			mIntent = new Intent(DoNotDisturbScreen.this,Connect.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_feedback:
			mIntent = new Intent(DoNotDisturbScreen.this,Feedback.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_logout:
			sendLogoutSatusToserver();
			break;
		}
	}
	
	/*@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
				iv_toggle_image.setImageResource(R.drawable.dndon_icon);
				Toast.makeText(DoNotDisturbScreen.this, "Hotel staff won’t bother you", Toast.LENGTH_SHORT).show();
			}else{
				iv_toggle_image.setImageResource(R.drawable.off_icon);
			}
	}*/
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		//mIntent = new Intent(DoNotDisturbScreen.this,Housekeeping.class);
		//startActivity(mIntent);
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(pref.getBoolean("DUMMY_ACCESS", false)){
			Editor edit = pref.edit();
			edit.putBoolean("DUMMY_ACCESS", false);
			edit.commit();
			Intent i = new Intent(DoNotDisturbScreen.this, DoNotDisturbScreen.class);
			startActivity(i);
			DoNotDisturbScreen.this.finish();
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
							app.getLogininfo().setLoginInfo(false);
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
				Intent i = new Intent(DoNotDisturbScreen.this,DashBorad.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("status", true);
				startActivity(i);
				finish();
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
