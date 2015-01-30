package com.waupi.screens;

import com.waupi.util.AlertDialogManager;
import com.waupi.util.ConnectionDetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoginRegister extends BaseScreen{

	public LinearLayout ll_login,ll_register;
	public ImageView iv_facebook,iv_gplus;
	
	private AlertDialogManager alert = new AlertDialogManager();
	private ConnectionDetector cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginregister);
		
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(LoginRegister.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			return;
		}
		
		ll_login = (LinearLayout)findViewById(R.id.ll_login);
		ll_login.setOnClickListener(this);
		
		ll_register = (LinearLayout)findViewById(R.id.ll_register);
		ll_register.setOnClickListener(this);
		
		iv_facebook = (ImageView)findViewById(R.id.iv_facebook);
		iv_facebook.setOnClickListener(this);
		
		iv_gplus = (ImageView)findViewById(R.id.iv_gplus);
		iv_gplus.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_login:
			Intent i = new Intent(LoginRegister.this,Login.class);
			startActivity(i);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.ll_register:
			Intent i1 = new Intent(LoginRegister.this,Register.class);
			startActivity(i1);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.iv_facebook:
			Intent i2 = new Intent(LoginRegister.this,Login.class);
			startActivity(i2);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
			
		case R.id.iv_gplus:
			Intent i3 = new Intent(LoginRegister.this,Register.class);
			startActivity(i3);
			overridePendingTransition(R.anim.anim1, R.anim.anim2);
			finish();
			break;
		}
	}
}
