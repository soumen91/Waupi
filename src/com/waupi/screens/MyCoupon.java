package com.waupi.screens;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.Session;
import com.google.android.gms.internal.md;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.waupi.adapter.CouponAdapter;
import com.waupi.bean.CouponBean;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;

public class MyCoupon extends BaseScreen implements OnItemClickListener {

	private ListView lv_orderlist;
	private LinearLayout ll_slider;
	private SlidingMenu slidingMenu;

	private Intent mIntent = null;
	private LinearLayout ll_etashuttle, ll_checkin, ll_housekeeping,
			ll_exploreicon, ll_connect, ll_logout, ll_feedback, ll_mycoupon;

	private TextView tv_hotel_name, tv_username, tv_reviews;

	private RatingBar review_rating;
	private ImageView iv_hotel;
	private ImageLoader imageloader;

	private ArrayList<CouponBean> couponList = new ArrayList<CouponBean>();
	private CouponAdapter adapter;
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;
	private Bitmap myBitmap = null;
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycoupons);
		
		imageloader = new ImageLoader(MyCoupon.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(MyCoupon.this,
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

		lv_orderlist = (ListView) findViewById(R.id.lv_orderlist);
		lv_orderlist.setOnItemClickListener(this);

		tv_hotel_name = (TextView) findViewById(R.id.tv_hotel_name);
		tv_username = (TextView) findViewById(R.id.tv_username);
		
		iv_hotel = (ImageView) findViewById(R.id.iv_hotel);
		iv_hotel.setOnClickListener(this);
		
		tv_reviews = (TextView) findViewById(R.id.tv_reviews);
		review_rating = (RatingBar) findViewById(R.id.review_rating);

		ll_mycoupon = (LinearLayout) findViewById(R.id.ll_mycoupon);
		ll_mycoupon.setOnClickListener(this);

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

		ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
		ll_logout.setOnClickListener(this);

	}

	@Override
	public void init() {
		if (!app.getTutorialInfo().isAuthenticated()) {
			tv_username.setText("Welcome " + app.getUserInfo().fname);
			review_rating.setRating(Float.parseFloat("3"));
			tv_reviews.setText("10 Reviews");

		} else {
			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			tv_username.setText("Welcome " + app.getUserInfo().fname);

			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			review_rating
					.setRating(Float.parseFloat(app.getHotelInfo().hotelrating));
			if (app.getHotelInfo().hoteleview_count.equalsIgnoreCase("0")) {
				tv_reviews.setText(app.getHotelInfo().hoteleview_count
						+ " Review");
			} else {
				tv_reviews.setText(app.getHotelInfo().hoteleview_count
						+ " Reviews");
			}
			if (app.getHotelInfo().hotelimage.length() <= 0) {
				iv_hotel.setImageResource(R.drawable.hotel_no_img);
			} else {
				imageloader.DisplayImage(Constant.URL
						+ app.getHotelInfo().hotelimage, iv_hotel);
			}
			getAllCouponList();
		}
		super.init();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ll_slider:
			slidingMenu.toggle();
			break;

		case R.id.ll_mycoupon:
			slidingMenu.toggle();

		case R.id.ll_etashuttle:
			mIntent = new Intent(MyCoupon.this, EtaShuttle.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_checkin:
			mIntent = new Intent(MyCoupon.this, CheckIn.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_housekeeping:
			mIntent = new Intent(MyCoupon.this, Housekeeping.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_expolreicon:
			mIntent = new Intent(MyCoupon.this, ExploreHotel.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_connect:
			mIntent = new Intent(MyCoupon.this, Connect.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_feedback:
			mIntent = new Intent(MyCoupon.this, Feedback.class);
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

	private void getAllCouponList() {
		Thread t = new Thread() {
			public void run() {
				doShowLoading();
				fetchAllCouponlistFromServer();
				doRemoveLoading();
			}
		};
		t.start();
	}

	private void fetchAllCouponlistFromServer() {
		String response = HttpClient.getInstance(getApplicationContext())
				.SendHttpGet(Constant.URL + Constant.COUPON_LIST);
		if (response != null) {
			try {
				JSONObject jobj = new JSONObject(response);
				JSONArray jarray = jobj.getJSONArray("coupons");
				for (int i = 0; i < jarray.length(); i++) {
					JSONObject ob = jarray.getJSONObject(i);
					System.out.println("!!The response:" + ob.toString());
					String couon_id = ob.getString("coupon_id");
					String status = ob.getString("status");
					String validity = ob.getString("validity");
					JSONObject obj = ob.getJSONObject("coupon");
					String code = ob.getString("code");
					JSONObject object = obj.getJSONObject("image");
					String description = obj.getString("content");
					String image = object.getString("url");

					couponList.add(new CouponBean(couon_id, code, description,
							image, validity, status));
				}
				updateUi();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateUi() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter = new CouponAdapter(MyCoupon.this,
						R.layout.coupon_list_row, couponList);
				lv_orderlist.setAdapter(adapter);
			}
		});
	}

	public void ShowCouponInDialog(final int position) {
		final Dialog dialog = new Dialog(MyCoupon.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.coupon_code_dialog);
		int dialog_width = (int) (Constant.DEVICE_WIDTH * .90);
		int dialog_height = (int) (Constant.DEVICE_HEIGHT * .80);
		dialog.getWindow().setLayout(dialog_width, dialog_height);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(true);

		TextView tv_coupon_code = (TextView) dialog
				.findViewById(R.id.tv_coupon_code);
		tv_coupon_code
				.setText("Coupon No. " + couponList.get(position).getId());

		TextView tv_offer_name = (TextView) dialog
				.findViewById(R.id.tv_offer_name);
		tv_offer_name.setText(couponList.get(position).getDescripton());

		ImageView iv_coupon_image = (ImageView) dialog
				.findViewById(R.id.iv_coupon_image);
		imageloader.DisplayImage(Constant.URL
				+ couponList.get(position).getImage(), iv_coupon_image);

		try {

			Bitmap bitmap = encodeAsBitmap(couponList.get(position).getCode(),
					BarcodeFormat.CODE_128, 600, 300);
			ImageView iv_barcode = (ImageView) dialog
					.findViewById(R.id.iv_barcode);
			iv_barcode.setImageBitmap(bitmap);

			TextView tv_bar_code = (TextView) dialog
					.findViewById(R.id.tv_bar_code);
			tv_bar_code.setText(couponList.get(position).getCode());

		} catch (WriterException e) {
			e.printStackTrace();
		}

		LinearLayout ll_button_click = (LinearLayout) dialog
				.findViewById(R.id.ll_button_click);
		ll_button_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		dialog.show();
	}

	public Bitmap getBitmapFromURL(final String src) {

		Thread t = new Thread() {
			public void run() {
				try {
					URL url = new URL(src);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					myBitmap = BitmapFactory.decodeStream(input);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		return myBitmap;
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
				Intent i = new Intent(MyCoupon.this, DashBorad.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("status", true);
				startActivity(i);
				overridePendingTransition(R.anim.anim1, R.anim.anim2);
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {

		Intent i = new Intent(MyCoupon.this, DashBorad.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		ShowCouponInDialog(position);
	}

	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	public void showAccessAcknowledgeDialog(final String message) {
		final Dialog dialog = new Dialog(MyCoupon.this);
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

				Intent i = new Intent(MyCoupon.this, MyCoupon.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
			Intent i = new Intent(MyCoupon.this, MyCoupon.class);
			startActivity(i);
			MyCoupon.this.finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
