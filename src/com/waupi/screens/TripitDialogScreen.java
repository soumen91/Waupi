package com.waupi.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TripitDialogScreen extends BaseScreen{

	private Button btn_tripit,btn_skip;
	private Intent mIntent;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.tripit_dialog);
		
		btn_tripit = (Button)findViewById(R.id.btn_tripit);
		btn_tripit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mIntent = new Intent(TripitDialogScreen.this,TripitActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
		
		btn_skip = (Button)findViewById(R.id.btn_skip);
		btn_skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mIntent = new Intent(TripitDialogScreen.this,Login.class);
				startActivity(mIntent);
				finish();
			}
		});	
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mIntent = new Intent(TripitDialogScreen.this,Login.class);
		startActivity(mIntent);
		finish();
	}
}
