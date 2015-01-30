package com.waupi.screens;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivity extends Activity{
	String message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		message = getIntent().getExtras().getString("message");
		
		final Dialog dialog = new Dialog(DialogActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.user_authaccess_dialog);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dialog.setCancelable(false);

		TextView tv_text = (TextView) dialog.findViewById(R.id.tv_text);
		tv_text.setText(message);

		Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
		btn_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if(message.equalsIgnoreCase("Access to holiday Inn Express is now available.")){
					dialog.dismiss();
					gotoNextScreen();
				}else{
					dialog.dismiss();
					finish();
				}
			}
		});
		dialog.show();
	}
	protected void gotoNextScreen() {
		Intent i = new Intent(DialogActivity.this,MyCoupon.class);
		startActivity(i);
		finish();
	}
}
