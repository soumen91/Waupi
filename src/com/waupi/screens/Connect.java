package com.waupi.screens;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.ImageLoader;

public class Connect extends BaseScreen {

	private LinearLayout ll_slider,ll_twitter,ll_gplus;
	private SlidingMenu slidingMenu;
	private Intent mIntent = null;
	private TextView tv_hotel_name, tv_username, tv_user_name, tv_reviews,tv_roomno;
	private LinearLayout ll_etashuttle, ll_checkin, ll_housekeeping,
			ll_exploreicon, ll_connect,ll_mycoupon, ll_logout, ll_feedback;
	private RatingBar review_rating;
	private ImageView iv_hotel, iv_image_hotel;
	private LinearLayout ll_facebook;
	private ImageLoader imageloader;
	Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static List<String> permissions;
	
	String filepath;
	
	/* Shared preference keys */
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";
	
	/* Any number for uniquely distinguish your request */
	public static final int WEBVIEW_REQUEST_CODE = 100;

	private ProgressDialog pDialog;

	private static Twitter twitter;
	private static RequestToken requestToken;
	
	private static SharedPreferences mSharedPreferences;

	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;
	
	private String isType = "fb";
	
	private SharedPreferences pref;
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);
		
		imageloader = new ImageLoader(Connect.this);
		pref = getSharedPreferences("TUTORIALTRAVERSE",Context.MODE_PRIVATE);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Connect.this,
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

		ll_slider = (LinearLayout) findViewById(R.id.ll_slider);
		ll_slider.setOnClickListener(this);

		iv_image_hotel = (ImageView) findViewById(R.id.iv_image_hotel);

		review_rating = (RatingBar) findViewById(R.id.review_rating);
		tv_reviews = (TextView) findViewById(R.id.tv_reviews);
		
		iv_hotel = (ImageView) findViewById(R.id.iv_hotel);
		iv_hotel.setOnClickListener(this);
		
		tv_roomno = (TextView)findViewById(R.id.tv_roomno);

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

		tv_hotel_name = (TextView) findViewById(R.id.tv_hotel_name);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);

		ll_facebook = (LinearLayout) findViewById(R.id.ll_facebook);
		ll_facebook.setOnClickListener(this);
		
		ll_gplus = (LinearLayout)findViewById(R.id.ll_gplus);
		ll_gplus.setOnClickListener(this);
		
		permissions = new ArrayList<String>();
		permissions.add("email"); 
		
		
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			session.addCallback(statusCallback);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this).setCallback(
						statusCallback).setPermissions(permissions));
			}
		}
		
		ll_twitter = (LinearLayout)findViewById(R.id.ll_twitter);
		ll_twitter.setOnClickListener(this);
		
		/* initializing twitter parameters from string.xml */
		initTwitterConfigs();

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		/* Check if required twitter keys are set */
		if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
			Toast.makeText(this, "Twitter key and secret not configured",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		/* Initialize application preferences */
		mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
	}

	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			//Check if Session is Opened or not
			processSessionStatus(session, state, exception);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(!app.getTutorialInfo().isAuthenticated()){
			tv_username.setText("Welcome "+app.getUserInfo().fname);
			tv_user_name.setText("Welcome "+app.getUserInfo().fname);
			review_rating.setRating(Float.parseFloat("3"));			
			tv_reviews.setText("10 Reviews");
			
			ll_facebook.setClickable(false);
			ll_twitter.setClickable(false);
			ll_gplus.setClickable(false);
			
		}else{
			
			ll_facebook.setClickable(true);
			ll_twitter.setClickable(true);
			ll_gplus.setClickable(true);
			
			tv_roomno.setText("");
			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			tv_username.setText("Welcome " + app.getUserInfo().fname);
			tv_user_name.setText("Welcome " + app.getUserInfo().fname);
	
			tv_hotel_name.setText(app.getHotelInfo().hotelName);
			review_rating.setRating(Float.parseFloat(app.getHotelInfo().hotelrating));
			if (app.getHotelInfo().hoteleview_count.equalsIgnoreCase("0")) {
				tv_reviews.setText(app.getHotelInfo().hoteleview_count + " Review");
			} else {
				tv_reviews
						.setText(app.getHotelInfo().hoteleview_count + " Reviews");
			}
			if (app.getHotelInfo().hotelimage.equalsIgnoreCase("")) {
				iv_hotel.setImageResource(R.drawable.hotel_no_img);
				iv_image_hotel.setImageResource(R.drawable.hotel_no_img);
			} else {
				imageloader.DisplayImage(Constant.URL
						+ app.getHotelInfo().hotelimage, iv_hotel);
				imageloader.DisplayImage(Constant.URL
						+ app.getHotelInfo().hotelimage, iv_image_hotel);
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
			mIntent = new Intent(Connect.this,MyCoupon.class);				
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_etashuttle:
			mIntent = new Intent(Connect.this, EtaShuttle.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_checkin:
			mIntent = new Intent(Connect.this, CheckIn.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_housekeeping:
			mIntent = new Intent(Connect.this, Housekeeping.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_expolreicon:
			mIntent = new Intent(Connect.this, ExploreHotel.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_connect:
			slidingMenu.toggle();
			break;

		case R.id.ll_feedback:
			mIntent = new Intent(Connect.this, Feedback.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;

		case R.id.ll_logout:
			sendLogoutSatusToserver();
			break;

		case R.id.ll_facebook:
			isType = "fb";
			Session session = Session.getActiveSession();
			if(session==null){                      
			    // try to restore from cache 
			    session = Session.openActiveSessionFromCache(this);
			    if (session.isOpened()) {
					publishFeedDialog();
				} else {
					session.openForRead(new Session.OpenRequest(Connect.this)
							.setCallback(statusCallback)
							.setPermissions(permissions));
				}
			}else{
				if (session.isOpened()) {
					publishFeedDialog();
				} else {
					session.openForRead(new Session.OpenRequest(Connect.this)
							.setCallback(statusCallback)
							.setPermissions(permissions));
				}
			}
			
			break;
			
		case R.id.ll_twitter:
			boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
			isType = "twit";
			/*  if already logged in, then hide login layout and show share layout */
			if (isLoggedIn) {
				
				showTwitterShareDialog();
				
			} else {			
				loginToTwitter();
			}
			break;
			
		case R.id.ll_gplus:
				
			if(isGooglePlusInstalled())
		    {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
				
				if(storeImage(bitmap, "app_logo")){
					File pictureFile = new File(filepath);
					String photoUri = null;
					try {
						photoUri = MediaStore.Images.Media.insertImage(
							     getContentResolver(), pictureFile.getAbsolutePath(), null, null);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					File externalFile = new File(Environment.getExternalStorageDirectory(),"/waupi/myImages/demo.jpg");
					Uri external = Uri.fromFile(externalFile);
					
					Intent shareIntent = ShareCompat.IntentBuilder.from(this)
			               .setText("Hello there! This is a pic of the lazy cat")
			               .setType("text/*")
			               .setStream(external)
			               .getIntent()
			               .setPackage("com.google.android.apps.plus");
			        startActivityForResult(shareIntent, 0);
				}	
		   }
		   else{
		     Toast.makeText(Connect.this, "Please download GooglePlus App for share..", 5000).show();
		   }
			
		break;
		
		case R.id.iv_hotel:
			slidingMenu.toggle();
			break;
		}
	}
	
	public boolean isGooglePlusInstalled()
	{
	    try
	    {
	        getPackageManager().getApplicationInfo("com.google.android.apps.plus", 0 );
	        return true;
	    } 
	    catch(PackageManager.NameNotFoundException e)
	    {
	        return false;
	    }
	}
	
	private boolean storeImage(Bitmap imageData, String filename) {
		//get path to external storage (SD card)
		String iconsStoragePath = Environment.getExternalStorageDirectory() + "/waupi/myImages/";
		File sdIconStorageDir = new File(iconsStoragePath);

		//create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();
		String fname = "demo.jpg";
		File file = new File (sdIconStorageDir, fname);
		filepath = iconsStoragePath + filename;
		if (file.exists ()) file.delete (); 
		try {
		       FileOutputStream out = new FileOutputStream(file);
		       imageData.compress(Bitmap.CompressFormat.JPEG, 90, out);
		       out.flush();
		       out.close();
		       return true;

		} catch (Exception e) {
		       e.printStackTrace();
		       return false;
		}
	}
	
	private void loginToTwitter() {
		boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		
		if (!isLoggedIn) {
			final ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(callbackUrl);

				/**
				 *  Loading twitter login page on webview for authorization 
				 *  Once authorized, results are received at onActivityResult
				 *  */
				final Intent intent = new Intent(this, TwitterWebViewActivity.class);
				intent.putExtra(TwitterWebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
				startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
				
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} 
	}
	
	private void showTwitterShareDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.twitter_share_dialog);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dialog.setCancelable(false);
		
		TextView user_name = (TextView)dialog.findViewById(R.id.user_name);
		String username = mSharedPreferences.getString(PREF_USER_NAME, "");
		user_name.setText("Hello, "+username);
		final EditText share_text = (EditText)dialog.findViewById(R.id.share_text);
		
		Button btn_share = (Button)dialog.findViewById(R.id.btn_share);
		btn_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final String status = share_text.getText().toString();
				
				if (status.trim().length() > 0) {
					new updateTwitterStatus().execute(status);
					dialog.dismiss();
				} else {
					Toast.makeText(Connect.this, "Message is empty!!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(Connect.this, "Posted to Twitter dismissed!", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	

	class updateTwitterStatus extends AsyncTask<String, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			pDialog = new ProgressDialog(Connect.this);
			pDialog.setMessage("Posting to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(String... args) {

			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(consumerKey);
				builder.setOAuthConsumerSecret(consumerSecret);
				
				// Access Token
				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);
				InputStream is = getResources().openRawResource(R.drawable.app_logo);
				statusUpdate.setMedia("test.jpg", is);
				statusUpdate.hashCode();
				
				twitter4j.Status response = twitter.updateStatus(statusUpdate);
				
				Log.d("Status", response.getText());
				
			} catch (TwitterException e) {
				Log.d("Failed to post!", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			/* Dismiss the progress dialog after sharing */
			pDialog.dismiss();
			
			Toast.makeText(Connect.this, "Posted to Twitter!", Toast.LENGTH_SHORT).show();
			
			// Clearing EditText field
			//mShareEditText.setText("");
		}
	}

	/**
	 * Saving user information, after user is authenticated for the first time.
	 * You don't need to show user to login, until user has a valid access toen
	 */
	private void saveTwitterInfo(AccessToken accessToken) {
		
		long userID = accessToken.getUserId();
		
		User user;
		try {
			user = twitter.showUser(userID);
		
			String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
			Editor e = mSharedPreferences.edit();
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();

		} catch (TwitterException e1) {
			e1.printStackTrace();
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

	private void publishFeedDialog() {
		// Log.e(Constant.TAG, "Constant==>>>"+Constant.fb_status);
		/*if(Constant.fb_status){*/
		
		Bundle params = new Bundle();
		params.putString("name", "Waupi");
		params.putString("caption", "Social App");
		params.putString("description", "Great Experiance");
		params.putString("link", "https://developers.facebook.com/android");
		params.putString("picture", "http://waupi.com/app_logo.png");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(getApplicationContext(),
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} else {
							// Generic, ex: network error
							Toast.makeText(getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
		/*}else{
		 
		 Log.e(Constant.TAG, "Constant==1111111111>>>"+Constant.fb_status);
		 Constant.fb_status = true; 
		}*/
	}

	@SuppressWarnings("deprecation")
	public void processSessionStatus(Session session, SessionState state,
			Exception exception) {
		if (session != null && session.isOpened()) {
			if (session.getPermissions().contains("email")) {
				//Show Progress Dialog
				//dialog = new ProgressDialog(Connect.this);
				//dialog.setMessage("Loggin in..");
				//dialog.show();
				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								
								if(user != null){
									
									System.out.println("!!name"+user.getFirstName());
									
									publishFeedDialog();
								}
							}
						});
			} else {
				session.requestNewReadPermissions(new Session.NewPermissionsRequest(
						Connect.this, permissions));
			}
		}
	}

	/********** Activity Methods **********/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*if(Session.getActiveSession() != null && resultCode == RESULT_OK)
					if(isType.equalsIgnoreCase("fb")){
				Session.getActiveSession().onActivityResult(Connect.this, requestCode,resultCode, data);
			}*/
		if (resultCode == Activity.RESULT_OK) {
			if(isType.equalsIgnoreCase("fb")){
				Session.getActiveSession().onActivityResult(Connect.this, requestCode,resultCode, data);
			}else if(isType.equalsIgnoreCase("twit")){
				String verifier = data.getExtras().getString(oAuthVerifier);
				try {
					AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

					long userID = accessToken.getUserId();
					final User user = twitter.showUser(userID);
					String username = user.getName();
					
					saveTwitterInfo(accessToken);

					showTwitterShareDialog();
					isType = "fb";
				} catch (Exception e) {
					Log.e("Twitter Login Failed", e.getMessage());
				}
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Save current session
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	/* Reading twitter essential configuration parameters from strings.xml */
	private void initTwitterConfigs() {
		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);
	}

	@Override
	protected void onStop() {
		// TODO Remove callback
		super.onStop();
		/*if(Session.getActiveSession() != null){
			Session.getActiveSession().removeCallback(statusCallback);
			Session.getActiveSession().closeAndClearTokenInformation();
			Session.setActiveSession(null);
		}*/
		/*if(isType.equalsIgnoreCase("fb")){
			if(Session.getActiveSession() != null){
				Session.getActiveSession().closeAndClearTokenInformation();
				Session.setActiveSession(null);
			}
		}	*/	//Session.getActiveSession().removeCallback(statusCallback);
	}

	private void gotoLoginScreen() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent i = new Intent(Connect.this, DashBorad.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				i.putExtra("status", true);
				startActivity(i);
				overridePendingTransition(R.anim.anim1, R.anim.anim2);
				finish();
			}
		});
	}
	
	public void showAccessAcknowledgeDialog(final String message) {
		final Dialog dialog = new Dialog(Connect.this);
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

				Intent i = new Intent(Connect.this,Connect.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				dialog.cancel();
			}
		});
		dialog.show();
	}
	
	@Override
	protected void onDestroy() {
		if(Session.getActiveSession() != null){
		Session.getActiveSession().removeCallback(statusCallback);
		Session.getActiveSession().closeAndClearTokenInformation();
		Session.setActiveSession(null);
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(pref.getBoolean("DUMMY_ACCESS", false)){
			Editor edit = pref.edit();
			edit.putBoolean("DUMMY_ACCESS", false);
			edit.commit();
			Intent i = new Intent(Connect.this, Connect.class);
			startActivity(i);
			Connect.this.finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		finish();
	}
}
