package com.waupi.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import DirectionsJSONParser.DirectionsJSONParser;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;
import com.waupi.util.TrackLocation;

public class EtaShuttle extends BaseScreen {

	private LinearLayout ll_slider;
	private SlidingMenu slidingMenu;
	private Intent mIntent = null;
	private boolean isZoomed = false;
	private LinearLayout ll_etashuttle, ll_checkin, ll_housekeeping,
			ll_exploreicon, ll_connect, ll_feedback, ll_logout,ll_mycoupon;

	private TextView tv_hotel_ampm_text, tv_hotel_shuttle_time,
			tv_airport_shuttle_time,tv_airport_text,tv_hotel_text;

	public GoogleMap map;
	public Marker cabMarker;
	private Timer timer;
	private boolean marker = false;

	public ArrayList<LatLng> markerPoints;
	public Runnable runnable;
	public Handler handler = new Handler();

	private int hour;
	private int minute;
	private int second;
	private String am_pm;

	private double hotelcablat, hotelcablng, airportcablat,
			airportcablng = 0.0f;
	private List<List<HashMap<String, String>>> routes = null;
	private String hotle_time_format, airport_time_format;
	public boolean isTimeCalculated = false;

	public int nxtShuttleTimeMin = 0;
	public int nxtShuttleTimeHour = 0;

	private boolean isFirstTime = true;
	private TextView tv_hotel_name,tv_username,tv_reviews,tv_user_name;

	public double hotel_location_lat, hotel_location_lng, airport_location_lat,
			airport_location_lng;
	
	private RatingBar review_rating;
	private ImageView iv_hotel,iv_request_carimage;
	private ImageLoader imageloader;
	
	private boolean in_hotel_radious,in_airport_radious = false;
	private boolean isRequestable = false;
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eta_shuttle);
		
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(EtaShuttle.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		
		imageloader = new ImageLoader(EtaShuttle.this);
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
		
		review_rating = (RatingBar)findViewById(R.id.review_rating);
        tv_reviews = (TextView)findViewById(R.id.tv_reviews);
        
        iv_hotel = (ImageView)findViewById(R.id.iv_hotel);
        iv_hotel.setOnClickListener(this);

		ll_etashuttle = (LinearLayout) findViewById(R.id.ll_etashuttle);
		ll_etashuttle.setOnClickListener(this);
		
		ll_mycoupon = (LinearLayout)findViewById(R.id.ll_mycoupon);
		ll_mycoupon.setOnClickListener(this);

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
		
		tv_hotel_name = (TextView)findViewById(R.id.tv_hotel_name);
		tv_username = (TextView)findViewById(R.id.tv_username);

		tv_hotel_ampm_text = (TextView) findViewById(R.id.tv_hotel_ampm_text);
		tv_hotel_shuttle_time = (TextView) findViewById(R.id.tv_hotel_shuttle_time);

		tv_airport_shuttle_time = (TextView) findViewById(R.id.tv_airport_shuttle_time);
		
		tv_hotel_name = (TextView)findViewById(R.id.tv_hotel_name);
        tv_username = (TextView)findViewById(R.id.tv_username);
        tv_user_name = (TextView)findViewById(R.id.tv_user_name);
        
        tv_airport_text = (TextView)findViewById(R.id.tv_airport_text);
        
        tv_hotel_text = (TextView)findViewById(R.id.tv_hotel_text);
        
        iv_request_carimage = (ImageView)findViewById(R.id.iv_request_carimage);
        iv_request_carimage.setOnClickListener(this);

		//getCurrentLocation();
		
	}

	@Override
	public void init() {
		if(!app.getTutorialInfo().isAuthenticated()){
			tv_username.setText("Welcome "+app.getUserInfo().fname);
			review_rating.setRating(Float.parseFloat("3"));			
			tv_reviews.setText("10 Reviews");
			iv_request_carimage.setClickable(false);
			
			getCurrentShuttleTime("demo");
			
		}else{
			
			getCurrentShuttleTime("real");
			
			iv_request_carimage.setClickable(true);
			
			tv_hotel_name.setText(app.getHotelInfo().hotelName);
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
		super.init();
	}

	private void getCurrentShuttleTime(final String request_type) {

		runnable = new Runnable() {
			@Override
			public void run() {
				callServerForShuttleTime(request_type);
				handler.postDelayed(runnable, 30 * 1000);
			}
		};
		handler.postDelayed(runnable, 1000);
	}

	private void callServerForShuttleTime(final String request_type) {
		Thread t = new Thread() {
			public void run() {
				JSONObject ob = new JSONObject();
				try {
					ob.put("reservation_id", "20");
					String response = null;
					if(request_type.equalsIgnoreCase("demo")){
						response = HttpClient.getInstance(
								getApplicationContext()).SendHttpPost(
								Constant.DEMO_ETA_SHUTTLE, ob.toString());
					}else if(request_type.equalsIgnoreCase("real")){
						response = HttpClient.getInstance(
								getApplicationContext()).SendHttpPost(
								Constant.ETA_SHUTTLE, ob.toString());
					}
					
					if (response != null) {
						final JSONObject obj = new JSONObject(response);
						JSONObject objct = obj.getJSONObject("hotel_duration");
						JSONObject object = objct.getJSONObject("time");
						String hoteltime_format = object.getString("formatted");
						hotelcablat = objct.getDouble("lat");
						hotelcablng = objct.getDouble("lon");
						if(objct.getBoolean("in_radius")){
							in_hotel_radious = true;
						}else{
							in_hotel_radious = false;
						}
						String hotelvalue = object.getString("value");

						JSONObject objct1 = obj
								.getJSONObject("airport_duration");
						JSONObject object1 = objct1.getJSONObject("time");
						String airporttime_format = object1
								.getString("formatted");
						airportcablat = objct1.getDouble("lat");
						airportcablng = objct1.getDouble("lon");
						if(objct1.getBoolean("in_radius")){
							in_airport_radious = true;
						}else{
							in_airport_radious = false;
						}
						if(objct1.getBoolean("requestable")){
							isRequestable = true;
						}else{
							isRequestable = false;
						}
						
						String airportvalue = object1.getString("value");

						JSONObject hotelobj = obj.getJSONObject("hotel");
						hotel_location_lat = hotelobj.getDouble("latitude");
						hotel_location_lng = hotelobj.getDouble("longitude");

						JSONObject airportobj = obj.getJSONObject("airport");
						airport_location_lat = airportobj.getDouble("latitude");
						airport_location_lng = airportobj
								.getDouble("longitude");
						

						UpdateUi(hoteltime_format, airporttime_format,
								hotelvalue, airportvalue,in_hotel_radious,in_airport_radious,isRequestable);

						//updateui(hotelvalue,airportvalue);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	private void updateui(final String hotelvalue, final String airportvalue) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tv_hotel_shuttle_time.setText(hotelvalue);
				tv_airport_shuttle_time.setText(airportvalue);
			}
		});
	}

	private void UpdateUi(final String hoteltime_format,
			final String airporttime_format, final String hotelvalue,
			final String airportvalue, final boolean in_hotel_radious2, final boolean in_airport_radious2, final boolean isRequestable2) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				hotle_time_format = hoteltime_format;
				airport_time_format = airporttime_format;

				Calendar cal = Calendar.getInstance();
				hour = cal.get(Calendar.HOUR_OF_DAY);
				minute = cal.get(Calendar.MINUTE);
				second = cal.get(Calendar.SECOND);
				if (hour > 12) {
					hour = hour - 12;
					am_pm = "PM";

					tv_hotel_ampm_text.setText(am_pm);

				} else {
					am_pm = "AM";
					tv_hotel_ampm_text.setText(am_pm);
				}
				String[] separated = hotle_time_format.split(":");

				nxtShuttleTimeMin = minute + Integer.parseInt(separated[1]);
				nxtShuttleTimeHour = hour + Integer.parseInt(separated[0]);
				if (nxtShuttleTimeMin > 60) {
					nxtShuttleTimeMin = nxtShuttleTimeMin - 60;
					nxtShuttleTimeHour = nxtShuttleTimeHour + 1;
				}

				if(isRequestable2){
					iv_request_carimage.setVisibility(View.VISIBLE);
				}else{
					iv_request_carimage.setVisibility(View.GONE);
				}
				if(in_hotel_radious2){
					tv_hotel_text.setText("Shuttle is at Hotel");
				}
				tv_hotel_shuttle_time.setText(hotelvalue);
				/*tv_hotel_shuttle_time.setText(new StringBuilder()
						.append(pad(nxtShuttleTimeHour)).append(":")
						.append(pad(nxtShuttleTimeMin)));*/

				map.clear();
				if (routes != null) {
					synchronized (routes) {

						ArrayList<LatLng> points = null;
						PolylineOptions lineOptions = null;
						//MarkerOptions markerOptions = new MarkerOptions();

						// Traversing through all the routes
						for (int i = 0; i < routes.size(); i++) {
							points = new ArrayList<LatLng>();
							lineOptions = new PolylineOptions();

							// Fetching i-th route
							List<HashMap<String, String>> path = routes.get(i);

							// Fetching all the points in i-th route
							for (int j = 0; j < path.size(); j++) {
								HashMap<String, String> point = path.get(j);

								double lat = Double.parseDouble(point
										.get("lat"));
								double lng = Double.parseDouble(point
										.get("lng"));
								LatLng position = new LatLng(lat, lng);

								points.add(position);
							}

							// Adding all the points in the route to LineOptions
							lineOptions.addAll(points);
							lineOptions.width(4);
							lineOptions.color(Color.RED);

						}
						// Drawing polyline in the Google Map for the i-th route
						map.addPolyline(lineOptions);
					}
				} else {
					/*String url = getDirectionsUrl(new LatLng(airportcablat,
							airportcablng), new LatLng(hotel_location_lat,
							hotel_location_lng));*/
					String url = getDirectionsUrl(new LatLng(
							airport_location_lat, airport_location_lng),
							new LatLng(hotel_location_lat, hotel_location_lng));
					DownloadTask downloadTask = new DownloadTask();

					// Start downloading json data from Google Directions API
					downloadTask.execute(url);
				}

				map.addMarker(new MarkerOptions()
						.position(new LatLng(hotelcablat, hotelcablng))
						.title("Shuttle from Hotel")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.pointer)));

				String[] separated1 = airport_time_format.split(":");

				nxtShuttleTimeMin = minute + Integer.parseInt(separated1[1]);
				nxtShuttleTimeHour = hour + Integer.parseInt(separated1[0]);
				if (nxtShuttleTimeMin > 60) {
					nxtShuttleTimeMin = nxtShuttleTimeMin - 60;
					nxtShuttleTimeHour = nxtShuttleTimeHour + 1;
				}

				tv_airport_shuttle_time.setText(airportvalue);

				/*tv_airport_shuttle_time.setText(new StringBuilder()
						.append(pad(nxtShuttleTimeHour)).append(":")
						.append(pad(nxtShuttleTimeMin)));*/

				map.addMarker(new MarkerOptions()
						.position(new LatLng(airportcablat, airportcablng))
						.title("Shuttle from Airport To Hotel")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.car_icon)));

				map.addMarker(new MarkerOptions()
						.position(
								new LatLng(airport_location_lat,
										airport_location_lng))
						.title("Airport")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.airport_icon)));
				map.addMarker(new MarkerOptions()
						.position(
								new LatLng(hotel_location_lat,
										hotel_location_lng))
						.title("Hotel")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.hotel_icon)));

				if (!isZoomed) {
					isZoomed = true;
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(
							new LatLng(airportcablat, airportcablng), 14));
				}
			}
		});
	}

	private boolean blinkMarker() {
		if (marker == true) {
			cabMarker.setVisible(true);
			marker = false;
		} else if (marker == false) {
			cabMarker.setVisible(false);
			marker = true;
		}
		return marker;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initializeMap();
		if(pref.getBoolean("DUMMY_ACCESS", false)){
			Editor edit = pref.edit();
			edit.putBoolean("DUMMY_ACCESS", false);
			edit.commit();
			Intent i = new Intent(EtaShuttle.this, EtaShuttle.class);
			startActivity(i);
			EtaShuttle.this.finish();
		}
	}

	protected void getCurrentLocation() {
		new TrackLocation(EtaShuttle.this);
	}

	private void initializeMap() {
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		markerPoints = new ArrayList<LatLng>();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_slider:
			slidingMenu.toggle();
			break;

		case R.id.ll_etashuttle:
			slidingMenu.toggle();
			break;
			
		case R.id.ll_mycoupon:
			mIntent = new Intent(EtaShuttle.this, MyCoupon.class);
			startActivity(mIntent);
			handler.removeCallbacks(runnable);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_checkin:
			mIntent = new Intent(EtaShuttle.this, CheckIn.class);
			startActivity(mIntent);
			handler.removeCallbacks(runnable);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_housekeeping:
			mIntent = new Intent(EtaShuttle.this, Housekeeping.class);
			startActivity(mIntent);
			handler.removeCallbacks(runnable);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_expolreicon:
			mIntent = new Intent(EtaShuttle.this,ExploreHotel.class);				
			startActivity(mIntent);
			handler.removeCallbacks(runnable);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_connect:
			mIntent = new Intent(EtaShuttle.this, Connect.class);
			startActivity(mIntent);
			handler.removeCallbacks(runnable);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_feedback:
			mIntent = new Intent(EtaShuttle.this, Feedback.class);
			startActivity(mIntent);
			handler.removeCallbacks(runnable);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_logout:
			sendLogoutSatusToserver();
			break;
			
		case R.id.iv_request_carimage:
			showRequestDialog();
			break;
			
		case R.id.iv_hotel:
			slidingMenu.toggle();
			break;
		}
	}

	private void showRequestDialog() {
		final Dialog dialog = new Dialog(EtaShuttle.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.shuttle_request_dialog);
		dialog.setCancelable(false);
		
		Button btn_no = (Button)dialog.findViewById(R.id.btn_no);
		btn_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				dialog.cancel();			
				}
		});
		
		Button btn_yes = (Button)dialog.findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				sendRequestToserver();
				dialog.cancel();			
				}
		});
		
		dialog.show();
	}
	
	private void sendRequestToserver() {
		Thread t = new Thread(){
			public void run(){
				doShowLoading();
				callWebservice();
				doRemoveLoading();
			}
		};
		t.start();
	}
	
	private void callWebservice() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("hotel_id", "5");
			String response = HttpClient.getInstance(EtaShuttle.this).SendHttpPost(Constant.SHUTTLE_REQUEST, ob.toString());
			if(response != null){
				JSONObject obj = new JSONObject(response);
				if(obj.getBoolean("status")){
					UpdateUiWithStatusMessage(obj.getBoolean("status"));
				}
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void UpdateUiWithStatusMessage(final boolean b) {
		
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(b){
					Toast.makeText(getApplicationContext(), "Request has been send to hotel", 5000).show();
				}else{
					Toast.makeText(getApplicationContext(), "Some problem occured please try again", 5000).show();
				}
			}
		});
		
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
				handler.removeCallbacks(runnable);
				Intent i = new Intent(EtaShuttle.this, DashBorad.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("status", true);
				startActivity(i);	
				overridePendingTransition(R.anim.anim1, R.anim.anim2);
				finish();
			}
		});
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread       
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;

			try {
				jObject = new JSONObject(jsonData[0]);
				Log.d(getClass().getCanonicalName(), jObject.toString());
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			//MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(4);
				lineOptions.color(Color.RED);

			}

			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);
		}
	}
	
	public void showAccessAcknowledgeDialog(final String message) {
		final Dialog dialog = new Dialog(EtaShuttle.this);
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

				Intent i = new Intent(EtaShuttle.this,EtaShuttle.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				dialog.cancel();
			}
		});
		dialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.removeCallbacks(runnable);
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		finish();
	}
}
