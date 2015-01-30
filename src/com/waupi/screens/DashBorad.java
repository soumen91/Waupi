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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.waupi.adapter.DashBoardHotelListAdapter;
import com.waupi.adapter.SelectedHotelListAdapter;
import com.waupi.bean.DashBoardHotelListBean;
import com.waupi.bean.SelectedHotelListBean;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;

public class DashBorad extends BaseScreen implements OnItemClickListener,
		TextWatcher, AnimationListener {

	private SlidingMenu slidingMenu;
	private LinearLayout ll_slider;
	private Intent mIntent = null;
	private Button btn_eta_shuttle, btn_check_in, btn_housekeeping,
			btn_explore_hotel, btn_connect, btn_feedback;
	private LinearLayout ll_etashuttle, ll_checkin, ll_housekeeping,
			ll_exploreicon, ll_connect, ll_feedback, ll_logout;
	private LinearLayout ll_hotel_search, ll_mycoupon;
	private ListView ll_hotellist;
	private AutoCompleteTextView et_search_hotel;
	private ImageView iv_search, iv_hotel, iv_db_search;
	private Animation animation, animation1;
	private RatingBar review_rating;
	private ImageLoader imageloader;
	private TextView tv_hotel_name, tv_username, tv_reviews;
	private DashBoardHotelListAdapter adapter;
	private ArrayList<DashBoardHotelListBean> hotelList = new ArrayList<DashBoardHotelListBean>();

	private SelectedHotelListAdapter selectedAdapter;
	private ArrayList<SelectedHotelListBean> selectedList = new ArrayList<SelectedHotelListBean>();

	private ImageView iv_shuttel, iv_explore, iv_search_list;
	private TextView tv_shuttle_text, tv_explore_text, tv_search_list_text;
	private Typeface custom_font;

	private RelativeLayout ll_coach_mark;
	private LinearLayout ll_ok_button;

	protected PowerManager.WakeLock mWakeLock;
	private SharedPreferences pref;

	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		imageloader = new ImageLoader(DashBorad.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE", Context.MODE_PRIVATE);

		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(DashBorad.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}

		boolean logoutStatus = getIntent().getBooleanExtra("status", false);
		if (logoutStatus) {
			Intent mIntent = new Intent(DashBorad.this, Login.class);
			startActivity(mIntent);
			finish();
		}

		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"waupi");
		this.mWakeLock.acquire();

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

		custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/girls_have_many_secrets.ttf");
		ll_coach_mark = (RelativeLayout) findViewById(R.id.ll_coach_mark);

		ll_ok_button = (LinearLayout) findViewById(R.id.ll_ok_button);
		ll_ok_button.setOnClickListener(this);

		iv_shuttel = (ImageView) findViewById(R.id.iv_shuttel);
		iv_explore = (ImageView) findViewById(R.id.iv_explore);
		iv_search_list = (ImageView) findViewById(R.id.iv_search_list);

		tv_shuttle_text = (TextView) findViewById(R.id.tv_shuttle_text);
		tv_explore_text = (TextView) findViewById(R.id.tv_explore_text);
		tv_search_list_text = (TextView) findViewById(R.id.tv_search_list_text);

		ll_slider = (LinearLayout) findViewById(R.id.ll_slider);
		ll_slider.setOnClickListener(this);

		review_rating = (RatingBar) findViewById(R.id.review_rating);
		tv_reviews = (TextView) findViewById(R.id.tv_reviews);
		
		iv_hotel = (ImageView) findViewById(R.id.iv_hotel);
		iv_hotel.setOnClickListener(this);

		btn_eta_shuttle = (Button) findViewById(R.id.btn_eta_shuttle);
		btn_eta_shuttle.setOnClickListener(this);

		btn_check_in = (Button) findViewById(R.id.btn_check_in);
		btn_check_in.setOnClickListener(this);

		btn_housekeeping = (Button) findViewById(R.id.btn_housekeeping);
		btn_housekeeping.setOnClickListener(this);

		btn_explore_hotel = (Button) findViewById(R.id.btn_explore_hotel);
		btn_explore_hotel.setOnClickListener(this);

		btn_connect = (Button) findViewById(R.id.btn_connect);
		btn_connect.setOnClickListener(this);

		btn_feedback = (Button) findViewById(R.id.btn_feedback);
		btn_feedback.setOnClickListener(this);

		tv_hotel_name = (TextView) findViewById(R.id.tv_hotel_name);
		tv_username = (TextView) findViewById(R.id.tv_username);

		ll_etashuttle = (LinearLayout) findViewById(R.id.ll_etashuttle);
		ll_etashuttle.setOnClickListener(this);

		ll_exploreicon = (LinearLayout) findViewById(R.id.ll_expolreicon);
		ll_exploreicon.setOnClickListener(this);

		ll_checkin = (LinearLayout) findViewById(R.id.ll_checkin);
		ll_checkin.setOnClickListener(this);

		ll_housekeeping = (LinearLayout) findViewById(R.id.ll_housekeeping);
		ll_housekeeping.setOnClickListener(this);

		ll_connect = (LinearLayout) findViewById(R.id.ll_connect);
		ll_connect.setOnClickListener(this);

		ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback);
		ll_feedback.setOnClickListener(this);

		ll_mycoupon = (LinearLayout) findViewById(R.id.ll_mycoupon);
		ll_mycoupon.setOnClickListener(this);

		ll_hotel_search = (LinearLayout) findViewById(R.id.ll_hotel_search);
		ll_hotel_search.setOnClickListener(this);

		ll_hotellist = (ListView) findViewById(R.id.lv_hotellist);
		ll_hotellist.setOnItemClickListener(this);

		iv_search = (ImageView) findViewById(R.id.iv_search);
		iv_search.setOnClickListener(this);

		et_search_hotel = (AutoCompleteTextView) findViewById(R.id.et_search_hotel);
		et_search_hotel.addTextChangedListener(this);

		ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
		ll_logout.setOnClickListener(this);

		iv_db_search = (ImageView) findViewById(R.id.iv_db_search);
		iv_db_search.setOnClickListener(this);

		animation = AnimationUtils.loadAnimation(DashBorad.this,
				R.anim.top_bottom);
		animation.setAnimationListener(this);

		animation1 = AnimationUtils.loadAnimation(DashBorad.this,
				R.anim.bottom_top);
		animation1.setAnimationListener(this);

	}

	@Override
	public void init() {
		super.init();

		/*if(pref.getBoolean("ISUSER_AUTHENTICATED", false)){
			app.getTutorialInfo().setUserAuthenticationInfo(true);
			Editor edit = pref.edit();
			edit.putBoolean("ISUSER_AUTHENTICATED", false);
			edit.commit();
		}*/
		tv_username.setText("Welcome " + app.getUserInfo().fname);
		if (app.getTutorialInfo().isTutorialTraverse == 0) {
			btn_eta_shuttle.setClickable(false);
			btn_check_in.setClickable(false);
			btn_explore_hotel.setClickable(false);
			btn_feedback.setClickable(false);
			btn_housekeeping.setClickable(false);
			btn_connect.setClickable(false);
			iv_db_search.setClickable(false);
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			ll_slider.setClickable(false);
			
			app.getTutorialInfo().setTutorialInfo(1);
		} else if (app.getTutorialInfo().isTutorialTraverse == 1) {
			
			ll_coach_mark.setVisibility(View.GONE);
			app.getTutorialInfo().setTutorialInfo(2);
			Toast.makeText(DashBorad.this, "You are viewing limited version of waupi. please select your hotel to enjoy full version!!", Toast.LENGTH_SHORT).show();
		} else if (app.getTutorialInfo().isTutorialTraverse == 2) {
			ll_coach_mark.setVisibility(View.GONE);
		}

		if (app.getTutorialInfo().isAuthenticated()) {
			
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			tv_hotel_name.setText(app.getHotelInfo().hotelName);

			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			ll_hotel_search.startAnimation(animation);
			review_rating
					.setRating(Float.parseFloat(app.getHotelInfo().hotelrating));
			if (app.getHotelInfo().hoteleview_count.equalsIgnoreCase("0")) {
				tv_reviews.setText(app.getHotelInfo().hoteleview_count
						+ " Review");
			} else {
				tv_reviews.setText(app.getHotelInfo().hoteleview_count
						+ " Reviews");
			}
			if (app.getHotelInfo().hotelimage.equalsIgnoreCase("")) {
				iv_hotel.setImageResource(R.drawable.hotel_no_img);
			} else {
				imageloader.DisplayImage(Constant.URL
						+ app.getHotelInfo().hotelimage, iv_hotel);
			}
			tv_username.setText("Welcome " + app.getUserInfo().fname);
		}
	}

	private void callAllHotelList() {
		Thread t = new Thread() {
			public void run() {
				doShowLoading();
				fetchHotelList();
				doRemoveLoading();
			}
		};
		t.start();
	}

	private void fetchHotelList() {
		String response = HttpClient.getInstance(getApplicationContext())
				.SendHttpGet(Constant.DASHBOARD_HOTEL_LIST);
		try {
			hotelList.clear();
			JSONArray jarray = new JSONArray(response);
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject ob = jarray.getJSONObject(i);
				String id = Integer.toString(ob.getInt("id"));
				String name = ob.getString("name");

				System.out.println("!!name is:" + name);
				String address = ob.getString("address");
				String rating = ob.getString("rating");
				System.out.println("!!please print rating:" + rating);
				if (rating.equalsIgnoreCase("0")) {
					rating = "0.0f";
				}
				JSONObject obj = ob.getJSONObject("image");
				String hotelimageUrl = null;
				if (!obj.isNull("url")) {
					hotelimageUrl = obj.getString("url");
				} else {
					hotelimageUrl = "";
				}

				String city = ob.getString("city");
				String country = ob.getString("country");
				String reviews_count = ob.getString("reviews_count");
				String frontdesk_no = ob.getString("front_desk_no");
				String valet_no = ob.getString("valet_pick_no");
				String laundry_pickup_no = ob.getString("laundry_pick_no");

				hotelList.add(new DashBoardHotelListBean(id, name, address,
						hotelimageUrl, rating, city, country, reviews_count,
						frontdesk_no, laundry_pickup_no, valet_no, "123"));
			}
			UpdateHotelListView();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void UpdateHotelListView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter = new DashBoardHotelListAdapter(DashBorad.this,
						R.layout.dashboard_hotel_list_row, hotelList);
				ll_hotellist.setAdapter(adapter);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_slider:
			slidingMenu.toggle();
			break;

		case R.id.btn_eta_shuttle:
			mIntent = new Intent(DashBorad.this, EtaShuttle.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.btn_check_in:
			mIntent = new Intent(DashBorad.this, CheckIn.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.btn_housekeeping:
			mIntent = new Intent(DashBorad.this, Housekeeping.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.btn_explore_hotel:
			mIntent = new Intent(DashBorad.this, ExploreHotel.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.btn_connect:
			mIntent = new Intent(DashBorad.this, Connect.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.btn_feedback:
			mIntent = new Intent(DashBorad.this, Feedback.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_etashuttle:
			mIntent = new Intent(DashBorad.this, EtaShuttle.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_checkin:
			mIntent = new Intent(DashBorad.this, CheckIn.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_housekeeping:
			mIntent = new Intent(DashBorad.this, Housekeeping.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_expolreicon:
			mIntent = new Intent(DashBorad.this, ExploreHotel.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_connect:
			mIntent = new Intent(DashBorad.this, Connect.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_feedback:
			mIntent = new Intent(DashBorad.this, Feedback.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_logout:
			sendLogoutSatusToserver();
			break;

		case R.id.ll_mycoupon:
			mIntent = new Intent(DashBorad.this, MyCoupon.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			break;

		case R.id.ll_ok_button:
			ll_coach_mark.setVisibility(View.GONE);
			btn_eta_shuttle.setClickable(true);
			btn_check_in.setClickable(true);
			btn_explore_hotel.setClickable(true);
			btn_feedback.setClickable(true);
			btn_housekeeping.setClickable(true);
			btn_connect.setClickable(true);
			iv_db_search.setClickable(true);
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			ll_slider.setClickable(true);
			//showAcknowledgeToast();
			ll_hotel_search.setVisibility(View.VISIBLE);
			ll_hotel_search.startAnimation(animation1);
			callAllHotelList();
			
			break;

		case R.id.iv_db_search:
			app.getTutorialInfo().setTutorialInfo(2);
			ll_coach_mark.setVisibility(View.GONE);
			if (!app.getTutorialInfo().isAuthenticated()) {
				if (hotelList.size() == 0) {
					ll_hotel_search.setVisibility(View.VISIBLE);
					ll_hotel_search.startAnimation(animation1);
					callAllHotelList();
				} else {
					ll_hotel_search.setVisibility(View.VISIBLE);
					ll_hotel_search.startAnimation(animation1);
				}
			}
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
							if (Session.getActiveSession() != null) {
								Session.getActiveSession()
										.closeAndClearTokenInformation();
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

				HttpClient.getInstance(getApplicationContext())
						.ClearCookieManager();
				Intent i = new Intent(DashBorad.this, Login.class);
				startActivity(i);
				overridePendingTransition(R.anim.anim1, R.anim.anim2);
				finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
			long arg3) {

		if (hotelList.get(position).getHotel_name()
				.equalsIgnoreCase("Holiday Inn Express")) {
			final Dialog dialog = new Dialog(DashBorad.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.setContentView(R.layout.user_authentication_dialog);
			dialog.setCancelable(false);

			Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
			btn_no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ll_hotel_search.startAnimation(animation);
					dialog.cancel();
					showAcknowledgeToast();
				}
			});

			Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
			btn_yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					new GetHotelUserAccess().execute();

					String id = hotelList.get(position).getHotel_id();
					String name = hotelList.get(position).getHotel_name();
					String city = hotelList.get(position).getHotel_city();
					String country = hotelList.get(position).getHotel_country();
					String rating = hotelList.get(position).getHotel_rating();
					String reviews_count = hotelList.get(position)
							.getReviews_count();
					String image = hotelList.get(position).getHotel_image();

					app.getHotelInfo().setHotelCity(city);
					app.getHotelInfo().setHotelCountry(country);
					app.getHotelInfo().setHotelId(id);
					app.getHotelInfo().setHotelName(name);
					app.getHotelInfo().setHoterating(rating);
					app.getHotelInfo().setHoteleview_count(reviews_count);
					app.getHotelInfo().setHotelimage(image);
					app.getHotelInfo().setDonot_disturb(
							hotelList.get(position).donot_disturb_no);
					app.getHotelInfo().setValet_no(
							hotelList.get(position).valet_no);
					app.getHotelInfo().setLaundry_pick_up(
							hotelList.get(position).laundry_pickup_no);
					app.getHotelInfo().setWakeup_call_no(
							hotelList.get(position).frontdesk_no);

					ll_hotel_search.startAnimation(animation);

					InputMethodManager imm = (InputMethodManager) DashBorad.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(
								ll_hotel_search.getWindowToken(), 0);
					}
					dialog.cancel();
				}
			});

			dialog.show();

		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		String searchString = et_search_hotel.getText().toString();
		if (searchString.length() > 0) {
			int textLength = searchString.length();
			selectedList.clear();
			for (int i = 0; i < hotelList.size(); i++) {
				String retailerName = hotelList.get(i).getHotel_name();
				if (textLength <= retailerName.length()) {
					if (retailerName.contains(" ")) {
						String[] arr = retailerName.split("\\s+");
						for (int j = 0; j < arr.length; j++) {
							if (arr[j].toLowerCase().contains(
									searchString.toLowerCase())) {
								selectedList.add(new SelectedHotelListBean(
										hotelList.get(i).getHotel_id(),
										hotelList.get(i).getHotel_name(),
										hotelList.get(i).getHotel_address(),
										hotelList.get(i).getHotel_image(),
										hotelList.get(i).getHotel_rating(),
										hotelList.get(i).getHotel_city(),
										hotelList.get(i).getHotel_country()));
								break;
							}
						}
					} else if (retailerName.toLowerCase().contains(
							searchString.toLowerCase())) {
						selectedList.add(new SelectedHotelListBean(hotelList
								.get(i).getHotel_id(), hotelList.get(i)
								.getHotel_name(), hotelList.get(i)
								.getHotel_address(), hotelList.get(i)
								.getHotel_image(), hotelList.get(i)
								.getHotel_rating(), hotelList.get(i)
								.getHotel_city(), hotelList.get(i)
								.getHotel_country()));
					}
				}
			}
			selectedAdapter = new SelectedHotelListAdapter(DashBorad.this,
					R.layout.dashboard_hotel_list_row, selectedList);
			ll_hotellist.setAdapter(selectedAdapter);
		} else {
			ll_hotellist.setAdapter(adapter);
		}
	}

	@Override
	public void onAnimationEnd(Animation anm) {
		if (anm.equals(animation)) {
			ll_hotel_search.setVisibility(View.GONE);
		} else {
			ll_hotel_search.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {

	}

	@Override
	public void onAnimationStart(Animation arg0) {

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		setShuttleArrowPosition();
		sethotelListArrowPosition();
		setExploreHotelArrowPosition();
	}

	private void setExploreHotelArrowPosition() {
		int[] explore_hotel_list_position = new int[2];
		btn_explore_hotel.getLocationOnScreen(explore_hotel_list_position);

		BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(
				R.drawable.arrow_circle_icon);
		int circle_height = bd.getBitmap().getHeight();
		int circle_width = bd.getBitmap().getWidth();

		BitmapDrawable bd1 = (BitmapDrawable) getResources().getDrawable(
				R.drawable.arrow_shuttel);
		int image_width = bd1.getBitmap().getWidth();

		int leftMargin = (int) ((explore_hotel_list_position[0]
				+ (int) (btn_explore_hotel.getWidth() / 2) - (circle_width / 2)) - (image_width / 2));
		int topMargin = (int) (explore_hotel_list_position[1]
				+ (int) (btn_explore_hotel.getHeight() / 2) - (circle_height / 2));

		iv_explore.setX(leftMargin);
		iv_explore.setY(topMargin - getStatusBarHeight());

		int[] explore_image_location = new int[2];
		iv_explore.getLocationOnScreen(explore_image_location);

		int text_leftMargin = (int) (explore_image_location[0] - (tv_explore_text
				.getWidth() / 2));
		int text_topMargin = (int) (explore_image_location[1] + (int) (iv_explore
				.getHeight()));
		tv_explore_text.setX(text_leftMargin);
		tv_explore_text.setY(text_topMargin - getStatusBarHeight());
		tv_explore_text.setTypeface(custom_font);
	}

	private void sethotelListArrowPosition() {
		tv_search_list_text.measure(0, 0);

		int[] search_hotel_list_position = new int[2];
		iv_db_search.getLocationOnScreen(search_hotel_list_position);

		BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(
				R.drawable.arrow_circle_icon);
		int circle_height = bd.getBitmap().getHeight();
		int circle_width = bd.getBitmap().getWidth();

		BitmapDrawable bd1 = (BitmapDrawable) getResources().getDrawable(
				R.drawable.arrow_list);
		int image_width = bd1.getBitmap().getWidth();

		int leftMargin = (int) ((search_hotel_list_position[0]
				+ (int) (iv_db_search.getWidth() / 2) - (circle_width / 2)) - (image_width / 2));
		int topMargin = (int) (search_hotel_list_position[1]
				+ (int) (iv_db_search.getHeight() / 2) - (circle_height / 2));

		iv_search_list.setX(leftMargin);
		iv_search_list.setY(topMargin - getStatusBarHeight());

		int[] search_list_image_location = new int[2];
		iv_search_list.getLocationOnScreen(search_list_image_location);

		int text_leftMargin = (int) (search_list_image_location[0] - (tv_search_list_text
				.getMeasuredWidth()));
		int text_topMargin = (int) (search_list_image_location[1] + (int) (iv_search_list
				.getHeight()));
		tv_search_list_text.setX(text_leftMargin);
		tv_search_list_text.setY(text_topMargin - getStatusBarHeight());
		tv_search_list_text.setTypeface(custom_font);
	}

	private void setShuttleArrowPosition() {
		int[] shuttle_btn_location = new int[2];
		btn_eta_shuttle.getLocationOnScreen(shuttle_btn_location);

		BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(
				R.drawable.arrow_circle_icon);
		int circle_height = bd.getBitmap().getHeight();
		int circle_width = bd.getBitmap().getWidth();

		int leftMargin = (int) (shuttle_btn_location[0]
				+ (int) (btn_eta_shuttle.getWidth() / 2) - (circle_width / 2));
		int topMargin = (int) (shuttle_btn_location[1]
				+ (int) (btn_eta_shuttle.getHeight() / 2) - (circle_height / 2));
		iv_shuttel.setX(leftMargin);
		iv_shuttel.setY(topMargin - getStatusBarHeight());

		int[] shuttle_image_location = new int[2];
		iv_shuttel.getLocationOnScreen(shuttle_image_location);

		int text_leftMargin = (int) (shuttle_image_location[0] + (int) (iv_shuttel
				.getWidth()));
		int text_topMargin = (int) (shuttle_image_location[1] + (int) (iv_shuttel
				.getHeight()));
		tv_shuttle_text.setX(text_leftMargin);
		tv_shuttle_text.setY(text_topMargin - getStatusBarHeight());
		tv_shuttle_text.setTypeface(custom_font);
	}

	public class GetHotelUserAccess extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			try {
				JSONObject ob = new JSONObject();
				ob.put("hotel_id", "5");
				String response = HttpClient.getInstance(DashBorad.this)
						.SendHttpPost(Constant.REQUEST_USER_VALIDATION,
								ob.toString());
				if (response != null) {
					JSONObject obj = new JSONObject(response);
					if (obj.getBoolean("status")) {
						return true;
					} else {
						return false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if (result) {
				final Dialog dialog = new Dialog(DashBorad.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.user_acknowledged_dialog);
				dialog.setCancelable(false);

				Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
				btn_yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						dialog.cancel();
					}
				});
				dialog.show();
			}
		}
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public void showAccessAcknowledgeDialog(final String message) {
		if (!app.getTutorialInfo().isUserAuthenticated) {
			final Dialog dialog = new Dialog(DashBorad.this);
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
					Intent i = new Intent(DashBorad.this, DashBorad.class)
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
					dialog.cancel();
				}
			});
			dialog.show();
		}
	}
	
	public void showAcknowledgeToast(){
		final Dialog toastDialog = new Dialog(DashBorad.this);
		toastDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		toastDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		toastDialog.setContentView(R.layout.user_acknowledged_toast);
		
		Button btn_yes = (Button)toastDialog.findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toastDialog.cancel();
			}
		});
		toastDialog.show();
	}
	
	@Override
	protected void onDestroy() {
		this.mWakeLock.release();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (pref.getBoolean("DUMMY_ACCESS", false)) {
			Editor edit = pref.edit();
			edit.putBoolean("DUMMY_ACCESS", false);
			edit.commit();
			Intent i = new Intent(DashBorad.this, DashBorad.class);
			startActivity(i);
			DashBorad.this.finish();
		}
	}
}
