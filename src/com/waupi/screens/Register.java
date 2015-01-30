package com.waupi.screens;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;

public class Register extends BaseScreen implements TextWatcher, OnTouchListener{
	
	private EditText et_fname;
	private EditText et_lname;
	private EditText et_email;
	private EditText et_password;
	
	public LinearLayout ll_submit,ll_back;
	
	public String fname,lname,email_id,password;
	public boolean isSignUpClicked = false;
	
	private static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";	
	public Intent mIntent;
	
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		this.overridePendingTransition(R.anim.anim1, R.anim.anim2);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(Register.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		
		et_fname = (EditText)findViewById(R.id.et_fname);
		et_fname.clearFocus();
		et_lname = (EditText)findViewById(R.id.et_lname);
		
		et_email = (EditText)findViewById(R.id.et_email);
		et_email.addTextChangedListener(this);
		
		et_password = (EditText)findViewById(R.id.et_password);
		
		ll_submit = (LinearLayout)findViewById(R.id.ll_submit);
		ll_submit.setOnClickListener(this);
		
		ll_back = (LinearLayout)findViewById(R.id.ll_back);
		ll_back.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_submit:
			if(!isSignUpClicked){
				isSignUpClicked = true;
				fname = et_fname.getText().toString().trim();
				lname = et_lname.getText().toString().trim();
				email_id = et_email.getText().toString().trim();
				password = et_password.getText().toString().trim();
				
				if(fname.length() == 0){
					et_fname.setError("Please enter your first name");
					isSignUpClicked = false;
				}else if(lname.length() == 0){
					et_lname.setError("Please enter your last name");
					isSignUpClicked = false;
				}else if(email_id.length() == 0){
					et_email.setError("Please enter your email id");
					isSignUpClicked = false;
				}else if(password.length() == 0){
					et_password.setError("Please enter your password");
					isSignUpClicked = false;
				}else if(password.length()<8){
					et_password.setError("Please enter your password atleast 8 digit");
					isSignUpClicked = false;
				}else{
					checkSignUp();
				}
			}
			break;
			
		case R.id.ll_back:
			Intent i = new Intent(Register.this,LoginRegister.class);
			startActivity(i);
			overridePendingTransition(R.anim.anim3, R.anim.anim4);
			finish();
			break;
		}
	}
	
	public  boolean isvalidMailid(String mail) {		
		return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
	}
	
	private void checkSignUp() {
		Thread t = new Thread(){
			public void run(){
				doShowLoading();
				doSignUp(fname, lname, email_id, password);
				doRemoveLoading();
			}
		};
		t.start();
	}
	
	public void doSignUp(String fname, String lname, String email_id, String password) {
		try {
			JSONObject json = new JSONObject();
			json.put("first_name", fname);
			json.put("last_name", lname);
			json.put("email", email_id);
			json.put("password", password);
			json.put("password_confirmation", password);
			JSONObject user = new JSONObject();
			user.put("user", json);
			String response = HttpClient.getInstance(getApplicationContext()).SendHttpPost("http://ec2-54-200-96-40.us-west-2.compute.amazonaws.com/api/v1/create", user.toString());
			Log.e("print", ""+response);
			if(response!= null){
				JSONObject obj = new JSONObject(response);
				if(obj.getBoolean("status")){
					goNextScreen(false);
				}else{
					goNextScreen(true);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void goNextScreen(final boolean flg){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(!flg){
					Toast.makeText(Register.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
					mIntent = new Intent(Register.this,TripitDialogScreen.class);
					startActivity(mIntent);
					app.getLogininfo().setTraversHotelList(false);
					finish();
					isSignUpClicked = false;
				}else{
					Toast.makeText(Register.this, "Email id has already been taken", Toast.LENGTH_SHORT).show();
					isSignUpClicked = false;
				}
			}
		});
	} 
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(Register.this,LoginRegister.class);
		startActivity(intent);
		overridePendingTransition(R.anim.anim3, R.anim.anim4);
		finish();
	}
	
	@Override
	public void afterTextChanged(Editable arg0) {
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int arg1, int arg2,int arg3) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
		if(!isvalidMailid(s.toString().trim())){
			et_email.setTextColor(Color.parseColor("#ff7d00"));
		}else{
			et_email.setTextColor(Color.parseColor("#00c283"));
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.et_fname:
			et_fname.setHint("");
			et_fname.setFocusableInTouchMode(true);
			InputMethodManager keyboard = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE);
	                keyboard.showSoftInput(et_fname, 0);
			break;

		case R.id.et_lname:
			et_lname.setHint("");
			et_lname.setFocusableInTouchMode(true);
			InputMethodManager keyboard1 = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE);
	                keyboard1.showSoftInput(et_lname, 0);
			break;
		case R.id.et_email:
			et_email.setHint("");
			et_email.setFocusableInTouchMode(true);
			InputMethodManager keyboard2 = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE);
	                keyboard2.showSoftInput(et_email, 0);
			break;

		case R.id.et_password:
			et_password.setHint("");
			et_password.setFocusableInTouchMode(true);
			InputMethodManager keyboard3 = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE);
	                keyboard3.showSoftInput(et_password, 0);
			break;
		}
		return false;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(Constant.isLoginWithTripit){
			mIntent = new Intent(Register.this,Login.class);
			startActivity(mIntent);
			finish();
		}
	}
}
