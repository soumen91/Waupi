package com.waupi.screens;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.waupi.adapter.RestaurantViewPagerAdapter;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;

public class Restaurant_Details extends BaseScreen{

	private LinearLayout ll_slider;
	//private SlidingMenu slidingMenu;
	private Intent mIntent = null; 	
	private LinearLayout ll_etashuttle,ll_checkin,ll_housekeeping,ll_exploreicon,ll_connect,ll_logout,ll_feedback;
	private int pos;
	private ViewPager pager;
	private RestaurantViewPagerAdapter _adapter;
	private LinearLayout ll_menu_bar,ll_menu_bg,ll_review_bar,ll_review_bg,ll_deals_bar,ll_deals_bg,ll_res_menu,ll_reviews,ll_deals;
	private ImageView iv_menu,iv_review,iv_deals;
	private TextView tv_menu_text,tv_review_text,tv_deals_text;
	private RatingBar review_rating;
	private TextView tv_restaurant_name,tv_restaurant_address,tv_restaurant_phno,tv_restaurant_reviews;
	public String four_square_id = null;
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.restaurant_details);
		this.overridePendingTransition(R.anim.anim1, R.anim.anim2);
		
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Restaurant_Details.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		
		System.out.println("!!the value for position is:"+Constant.position);
		
		
		four_square_id = Constant.totalList.get(Constant.position).getFoursquare_id();
		/*slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);       
        slidingMenu.setShadowWidth(15);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.dashboard_slider);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);*/
		
		tv_restaurant_name = (TextView)findViewById(R.id.tv_restaurant_name);
		tv_restaurant_name.setText(Constant.totalList.get(pos).getRestaurant_name());
		
		tv_restaurant_address = (TextView)findViewById(R.id.tv_restaurant_address);
		tv_restaurant_address.setText(Constant.totalList.get(pos).getRestaurant_address());
		
		tv_restaurant_phno = (TextView)findViewById(R.id.tv_restaurant_ph_no);
		//tv_restaurant_phno.setText(Constant.totalList.get(pos).getRestaurant)
		tv_restaurant_reviews = (TextView)findViewById(R.id.tv_restaurant_review);
		//tv_restaurant_reviews.setText(Constant.totalList.get(pos).getRestaurant)
		
		review_rating = (RatingBar)findViewById(R.id.review_rating);
		review_rating.setRating(Float.parseFloat(Constant.totalList.get(pos).getRestaurant_rating()));
        
        /*ll_slider = (LinearLayout)findViewById(R.id.ll_slider);
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
        ll_feedback.setOnClickListener(this);
        
        ll_logout = (LinearLayout)findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this);*/
		
		setUpView(0);	
	}
	private void setUpView(int arg) {
		pager = (ViewPager)findViewById(R.id.viewPager);
		_adapter = new RestaurantViewPagerAdapter(this,getSupportFragmentManager(),Constant.totalList.get(Constant.position).getMenuBean(),Constant.totalList.get(Constant.position).getDealsbean(),four_square_id);
		pager.setAdapter(_adapter);
		pager.setCurrentItem(arg,false);
		initImage();
	}
	private void initImage() {
		ll_res_menu = (LinearLayout) findViewById(R.id.ll_res_menu);
		ll_reviews = (LinearLayout) findViewById(R.id.ll_reviews);
		ll_deals = (LinearLayout) findViewById(R.id.ll_deals);
		
		ll_menu_bar = (LinearLayout)findViewById(R.id.ll_menu_bar);
		ll_menu_bg = (LinearLayout) findViewById(R.id.ll_menu_bg);
		iv_menu = (ImageView) findViewById(R.id.iv_menu);
		tv_menu_text = (TextView) findViewById(R.id.tv_menu_text);
		
		ll_review_bar = (LinearLayout)findViewById(R.id.ll_review_bar);
		ll_review_bg = (LinearLayout) findViewById(R.id.ll_review_bg);
		iv_review = (ImageView) findViewById(R.id.iv_review);
		tv_review_text = (TextView) findViewById(R.id.tv_review_text);
		
		ll_deals_bar = (LinearLayout)findViewById(R.id.ll_deals_bar);
		ll_deals_bg = (LinearLayout) findViewById(R.id.ll_deals_bg);
		iv_deals = (ImageView) findViewById(R.id.iv_deals);
		tv_deals_text = (TextView) findViewById(R.id.tv_deals_text);
		
		ll_res_menu.setOnClickListener(this);
		ll_reviews.setOnClickListener(this);
		ll_deals.setOnClickListener(this);

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
			ll_menu_bar.setBackgroundColor(Color.WHITE);
			ll_menu_bg.setBackgroundColor(Color.parseColor("#28a495"));
			iv_menu.setImageResource(R.drawable.food_icon);
			tv_menu_text.setTextColor(Color.WHITE);
			
			ll_review_bar.setBackgroundColor(Color.BLACK);
			ll_review_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_review.setImageResource(R.drawable.reviews_icon_hover);
			tv_review_text.setTextColor(Color.parseColor("#39cbb9"));
			
			ll_deals_bar.setBackgroundColor(Color.BLACK);
			ll_deals_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_deals.setImageResource(R.drawable.deals_icon_hover);
			tv_deals_text.setTextColor(Color.parseColor("#39cbb9"));
			
			break;
		case 1:
			ll_menu_bar.setBackgroundColor(Color.BLACK);
			ll_menu_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_menu.setImageResource(R.drawable.food_icon_hover);
			tv_menu_text.setTextColor(Color.parseColor("#39cbb9"));
			
			ll_review_bar.setBackgroundColor(Color.WHITE);
			ll_review_bg.setBackgroundColor(Color.parseColor("#28a495"));
			iv_review.setImageResource(R.drawable.reviews_icon);
			tv_review_text.setTextColor(Color.WHITE);
			
			ll_deals_bar.setBackgroundColor(Color.BLACK);
			ll_deals_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_deals.setImageResource(R.drawable.deals_icon_hover);
			tv_deals_text.setTextColor(Color.parseColor("#39cbb9"));
			
			break;
		case 2:
			ll_menu_bar.setBackgroundColor(Color.BLACK);
			ll_menu_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_menu.setImageResource(R.drawable.food_icon_hover);
			tv_menu_text.setTextColor(Color.parseColor("#39cbb9"));
			
			ll_review_bar.setBackgroundColor(Color.BLACK);
			ll_review_bg.setBackgroundColor(Color.parseColor("#186259"));
			iv_review.setImageResource(R.drawable.reviews_icon_hover);
			tv_review_text.setTextColor(Color.parseColor("#39cbb9"));
			
			ll_deals_bar.setBackgroundColor(Color.WHITE);
			ll_deals_bg.setBackgroundColor(Color.parseColor("#28a495"));
			iv_deals.setImageResource(R.drawable.deals_icon);
			tv_deals_text.setTextColor(Color.WHITE);
			
			break;

		}
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_res_menu:
			ImageAction(0);
			pager.setCurrentItem(0,false);
			break;

		case R.id.ll_reviews:
			ImageAction(1);
			pager.setCurrentItem(1,false);
			break;

		case R.id.ll_deals:
			ImageAction(2);
			pager.setCurrentItem(2,false);
			break;
			
		/*case R.id.ll_slider:
			slidingMenu.toggle();
			break;*/
			
		case R.id.ll_etashuttle:
			mIntent = new Intent(Restaurant_Details.this,EtaShuttle.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_checkin:
			mIntent = new Intent(Restaurant_Details.this,CheckIn.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_housekeeping:
			mIntent = new Intent(Restaurant_Details.this,Housekeeping.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_expolreicon:
			mIntent = new Intent(Restaurant_Details.this,ExploreHotel.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_connect:
			mIntent = new Intent(Restaurant_Details.this,Connect.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_feedback:
			mIntent = new Intent(Restaurant_Details.this,Feedback.class);				
			startActivity(mIntent);
			finish();
			break;
			
		case R.id.ll_logout:
			sendLogoutSatusToserver();
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
			Intent i = new Intent(Restaurant_Details.this,DashBorad.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("status", true);
			startActivity(i);
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
		Intent i = new Intent(Restaurant_Details.this, Restaurant_Details.class);
		startActivity(i);
		Restaurant_Details.this.finish();
	}
}

@Override
public void onBackPressed() {
	super.onBackPressed();
	overridePendingTransition(R.anim.anim3, R.anim.anim4);
	//mIntent = new Intent(Restaurant_Details.this,ExploreHotel.class);
	//startActivity(mIntent);
	finish();
}
}
