package com.waupi.screens;

import java.util.Arrays;
import java.util.EnumSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.VisibilityType;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.screens.LinkedinDialog.OnVerifyListener;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;
import com.waupi.util.LinkedInConfig;

public class Login extends BaseScreen implements OnTouchListener, ConnectionCallbacks, OnConnectionFailedListener{

	private LinearLayout ll_login;
	private EditText ed_email;
	private EditText ed_password;
	private boolean isSigninClicked = false;
	private String username;
	private String password;
	public LinearLayout ll_back;
	private Intent intent;
	
	private ImageView iv_gplus,iv_linkedin;
	private RelativeLayout rl_facebook;
	
	protected VisibilityType visibility = VisibilityType.CONNECTIONS_ONLY;
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";

	final LinkedInOAuthService oAuthService = LinkedInOAuthServiceFactory
            .getInstance().createLinkedInOAuthService(
            		LinkedInConfig.LINKEDIN_CONSUMER_KEY,LinkedInConfig.LINKEDIN_CONSUMER_SECRET);
	final LinkedInApiClientFactory factory = LinkedInApiClientFactory
			.newInstance(LinkedInConfig.LINKEDIN_CONSUMER_KEY,
					LinkedInConfig.LINKEDIN_CONSUMER_SECRET);
	
	LinkedInRequestToken liToken;
	LinkedInApiClient client;
	LinkedInAccessToken accessToken = null;
	
	private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";
 
    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;
 
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private SignInButton btnSignIn;
    
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    
    private UiLifecycleHelper uiHelper;
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private PendingAction pendingAction = PendingAction.NONE;
	private GraphUser user;
	private boolean isUiUpdateCall = false; 
	private LoginButton loginButton;
	private SharedPreferences pref;
	public String user_id, email, profile_image, profile_url, name = null;
	
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
	
    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Login.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		
		if( Build.VERSION.SDK_INT >= 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); 
		}
		
		ll_login = (LinearLayout)findViewById(R.id.ll_login);
		ll_login.setOnClickListener(this);
		
		ed_email = (EditText) findViewById(R.id.ed_email);
		ed_email.setOnTouchListener(this);
		
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_password.setOnTouchListener(this);

		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		ll_back.setOnClickListener(this);
		
		iv_gplus = (ImageView)findViewById(R.id.iv_gplus);
		iv_gplus.setOnClickListener(this);
		
		iv_linkedin = (ImageView)findViewById(R.id.iv_linkedin);
		iv_linkedin.setOnClickListener(this);
		
		btnSignIn = (SignInButton) findViewById(R.id.btn_gplus_signin);
        btnSignIn.setOnClickListener(this);
               
        mGoogleApiClient = new GoogleApiClient.Builder(this,this,this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();
        
        uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
	    if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
	    
	    loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
	        @Override
	        public void onUserInfoFetched(GraphUser user) {
	        	Login.this.user = user;	        	
	        	isUiUpdateCall = true;
	        	loginButton.setReadPermissions(Arrays.asList("email"));
	        	updateFacebookUI();
	            handlePendingAction();
	        }
	    });
	}
		
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_login:
			if (!isSigninClicked) {
				isSigninClicked = true;
				hideKeyBoard(ed_password);
				username = ed_email.getText().toString().trim();
				password = ed_password.getText().toString().trim();
				if (username.length() == 0) {
					ed_email.setError("Please enter your email id");
					isSigninClicked = false;
				} else if (password.length() == 0) {
					ed_password.setError("Please enter your password");
					isSigninClicked = false;
				} else {
					checkLogin();
				}
			}
			break;
			
		case R.id.iv_gplus:
			signInWithGplus();
			break;
			
		case R.id.btn_gplus_signin:
			break;
			
		case R.id.ll_back:
			Intent i = new Intent(Login.this,LoginRegister.class);
			startActivity(i);
			overridePendingTransition(R.anim.anim3, R.anim.anim4);
			finish();
			break;
			
		case R.id.iv_linkedin:
			linkedInLogin();
			break;
		}
	}
	
	private void updateFacebookUI()
	{
		if (isUiUpdateCall)
		{
			isUiUpdateCall = false;
			Session session = Session.getActiveSession();
			boolean enableButtons = (session != null && session.isOpened());

			if (enableButtons && user != null)
			{				
					user_id = user.getId();
					name = user.getName();										
					email = user.getProperty("email").toString();
					profile_image = "https://graph.facebook.com/"+user.getId()+"/picture";
					profile_url = "https://graph.facebook.com/"+user.getId();
					SendDetailsToserver();
			}
		}
	}
	
	public void SendDetailsToserver(){
		
		Thread t = new Thread()
		{
			public void run()
			{
				JSONObject obj = new JSONObject();
				try {
					obj.put("uid", user_id);
					obj.put("email", email);
					obj.put("profile_url", profile_url);
					obj.put("name", name);
					obj.put("profile_image", profile_image);
					obj.put("type", "facebook");
					obj.put("device_id", app.getDeviceInfo().device_id);
					obj.put("device_type", "android");
										
					String response = HttpClient.getInstance(getApplicationContext()).SendHttpPost(Constant.SOCIAL_LOGIN, obj.toString());
					if(response != null){
						
						JSONObject ob = new JSONObject(response);
						if(ob.getBoolean("status")){
							String first_name = ob.getString("first_name");
							String last_name = ob.getString("last_name");
							String user_id = ob.getString("user_id");
							String reservation_type = ob.getString("reservation_type");
							boolean checkin_status = ob.getBoolean("checkin_status");
							String rev_id = null,reservation_code = null;
							JSONArray object = ob.getJSONArray("reservation_detail");
							for(int i = 0;i<object.length();i++){
								rev_id = object.getJSONObject(i).getString("reservation_id");
								reservation_code = object.getJSONObject(i).getString("reservation_code");
							}
														
							app.getUserInfo().SetUserInfo(first_name,
									last_name,
									user_id,
									rev_id,
									reservation_code,
									reservation_type,
									checkin_status);
							
							app.getLogininfo().setLoginInfo(true);
						}
						UpdateUiResult(true);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}							
			}
		};
	t.start();
}
	
	private void UpdateUiResult(boolean b) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
				if(Session.getActiveSession() != null){
					Session.getActiveSession().closeAndClearTokenInformation();
					Session.setActiveSession(null);
				}
				
				if(mGoogleApiClient.isConnected()){
					signOutFromGplus();
				}
				
				intent = new Intent(Login.this,DashBorad.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception)
	{
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException))
		{
			new AlertDialog.Builder(Login.this)
					.setTitle(R.string.cancelled)
					.setMessage(R.string.permission_not_granted)
					.setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		}
		else if (state == SessionState.OPENED_TOKEN_UPDATED)
		{
			handlePendingAction();
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction()
	{
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction)
		{
			case POST_PHOTO:
				break;
			case POST_STATUS_UPDATE:
				break;
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}
	
	public void checkLogin() {
		Thread t = new Thread() {
			public void run() {
				doShowLoading();
				doLogIn(username, password);
				doRemoveLoading();
				isSigninClicked = false;
			}
		};
		t.start();
	}
	
	public void goNextScreen(final boolean flg) {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!flg) {
					startActivity(new Intent(Login.this,DashBorad.class));
					overridePendingTransition(R.anim.anim1, R.anim.anim2);
					finish();
				} else {
					Toast.makeText(getApplicationContext(),"Invalid User Id or Password", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.ed_email:
			ed_email.setFocusableInTouchMode(true);
			InputMethodManager keyboard = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE);
	                keyboard.showSoftInput(ed_email, 0); 
			break;

		case R.id.ed_password:
			ed_password.setFocusableInTouchMode(true);
			InputMethodManager keyboard1 = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE);
	                keyboard1.showSoftInput(ed_password, 0);
			break;
		}
		return false;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(Login.this,LoginRegister.class);
		startActivity(intent);
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		finish();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
	        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
	                0).show();
	        return;
	    }
	 
	    if (!mIntentInProgress) {
	        // Store the ConnectionResult for later usage
	        mConnectionResult = result;
	 
	        if (mSignInClicked) {
	            // The user has already clicked 'sign-in' so we attempt to
	            // resolve all
	            // errors until the user is signed in, or they cancel.
	            resolveSignInError();
	        }
	    }
	}
	
	protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
 
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
 
    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int responseCode,Intent intent) {
    	super.onActivityResult(requestCode, responseCode, intent);
    	if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }
            mIntentInProgress = false;
 
            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }else{
        	uiHelper.onActivityResult(requestCode, responseCode, intent, dialogCallback);
        }
    }
 
    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        // Get user's information
        getProfileInformation();
 
        // Update the UI after signin
        //updateUI(true);
    }
 
    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
    	//goNextScreen(false);
    }
 
    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = null;
                if(currentPerson.getImage().getUrl() != null){
                	personPhotoUrl = currentPerson.getImage().getUrl();
                }else{
                	personPhotoUrl = "";
                }
                String personGooglePlusProfile = currentPerson.getUrl();
                String gplusemail = Plus.AccountApi.getAccountName(mGoogleApiClient);
 
                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                
                user_id = currentPerson.getId();
                profile_image = personPhotoUrl;
                profile_url = personGooglePlusProfile;
                name = personName;
                this.email = gplusemail;
                
               /* txtName.setText(personName);
                txtEmail.setText(email);*/
 
                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;
 
               // new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
                
                Thread t = new Thread()
				{
					public void run()
					{
						JSONObject obj = new JSONObject();
						try {
							obj.put("uid", user_id);
							obj.put("email", email);
							obj.put("profile_url", profile_url);
							obj.put("name", name);
							obj.put("profile_image", profile_image);
							obj.put("type", "google");
							obj.put("device_id", app.getDeviceInfo().device_id);
							obj.put("device_type", "android");
							
							String response = HttpClient.getInstance(getApplicationContext()).SendHttpPost(Constant.SOCIAL_LOGIN, obj.toString());
							if(response != null){
								JSONObject ob = new JSONObject(response);
								if(ob.getBoolean("status")){
									String first_name = ob.getString("first_name");
									String last_name = ob.getString("last_name");
									String user_id = ob.getString("user_id");
									String reservation_type = ob.getString("reservation_type");
									boolean checkin_status = ob.getBoolean("checkin_status");
									
									String rev_id = null,reservation_code = null;
									JSONArray object = ob.getJSONArray("reservation_detail");
									for(int i = 0;i<object.length();i++){
										rev_id = object.getJSONObject(i).getString("reservation_id");
										reservation_code = object.getJSONObject(i).getString("reservation_code");
									}
									
									app.getUserInfo().SetUserInfo(first_name,
											last_name,
											user_id,
											rev_id,
											reservation_code,
											reservation_type,
											checkin_status);
									
									app.getLogininfo().setLoginInfo(true);
								}
								UpdateUiResult(true);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}							
					}
				};
			t.start();
                
            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }
    
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }
 
    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
        }
    }
 
    /**
     * Revoking access from google
     * */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }
             });
        }
    }
    
    private void linkedInLogin() {
		ProgressDialog progressDialog = new ProgressDialog(
				Login.this);

		LinkedinDialog d = new LinkedinDialog(Login.this,
				progressDialog);
		d.show();

		// set call back listener to get oauth_verifier value
		d.setVerifierListener(new OnVerifyListener() {
			@Override
			public void onVerify(String verifier) {
				try {
					Log.i("LinkedinSample", "verifier: " + verifier);

					accessToken = LinkedinDialog.oAuthService
							.getOAuthAccessToken(LinkedinDialog.liToken,
									verifier);
					LinkedinDialog.factory.createLinkedInApiClient(accessToken);
					client = factory.createLinkedInApiClient(accessToken);
					// client.postNetworkUpdate("Testing by Mukesh!!! LinkedIn wall post from Android app");
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getToken());
					Log.i("LinkedinSample",
							"ln_access_token: " + accessToken.getTokenSecret());
					
					com.google.code.linkedinapi.schema.Person profile = null;
					try {
						profile = client.getProfileForCurrentUser(EnumSet.of(
								ProfileField.ID, ProfileField.FIRST_NAME,
								ProfileField.EMAIL_ADDRESS, ProfileField.LAST_NAME,
								ProfileField.HEADLINE, ProfileField.INDUSTRY,
								ProfileField.PICTURE_URL, ProfileField.DATE_OF_BIRTH,
								ProfileField.LOCATION_NAME, ProfileField.MAIN_ADDRESS,
								ProfileField.LOCATION_COUNTRY,ProfileField.PUBLIC_PROFILE_URL));
						Log.e("create access token secret", profile.getEmailAddress());
					} catch (NullPointerException e) {
						
					}

					com.google.code.linkedinapi.schema.Person p = client.getProfileForCurrentUser();
					
					user_id = profile.getId();
					name = profile.getFirstName() + " "+ profile.getLastName();
					email = profile.getEmailAddress();
		
					if(profile.getPictureUrl() != null){
						profile_image = profile.getPictureUrl();
					}else{
						profile_image = "";
					}
					
					profile_url = profile.getPublicProfileUrl();
					
					System.out.println("!!reah here"+user_id + "Email"+email +
							"name" +name + "profile_image" + profile_image + "url" + profile_url);
			
					sendLinkedInProfileDetails();

				} catch (Exception e) {
					Log.i("LinkedinSample", "error to get verifier");
					e.printStackTrace();
				}
			}
		});

		// set progress dialog
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(true);
		progressDialog.show();
	}
    
    private void sendLinkedInProfileDetails() {
    	Thread t = new Thread()
		{
			public void run()
			{
				JSONObject obj = new JSONObject();
				try {
					obj.put("uid", user_id);
					obj.put("email", email);
					obj.put("profile_url", profile_url);
					
					obj.put("name", name);
					obj.put("profile_image", profile_image);
					obj.put("type", "linkedin");
					
					String response = HttpClient.getInstance(getApplicationContext()).SendHttpPost(Constant.SOCIAL_LOGIN, obj.toString());
					if(response != null){
						JSONObject ob = new JSONObject(response);
						if(ob.getBoolean("status")){
							String first_name = ob.getString("first_name");
							String last_name = ob.getString("last_name");
							String user_id = ob.getString("user_id");
							String reservation_type = ob.getString("reservation_type");
							boolean checkin_status = ob.getBoolean("checkin_status");
							
							String rev_id = null,reservation_code = null;
							JSONArray object = ob.getJSONArray("reservation_detail");
							for(int i = 0;i<object.length();i++){
								rev_id = object.getJSONObject(i).getString("reservation_id");
								reservation_code = object.getJSONObject(i).getString("reservation_code");
							}
							
							app.getUserInfo().SetUserInfo(first_name,
									last_name,
									user_id,
									rev_id,
									reservation_code,
									reservation_type,
									checkin_status);
							
							app.getLogininfo().setLoginInfo(true);
						}
						UpdateUiResult(true);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}							
			}
		};
	t.start();
	}
    
    @Override
	protected void onResume()
	{
		super.onResume();
		uiHelper.onResume();
		AppEventsLogger.activateApp(this);
	}
    
    @Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}	
}
