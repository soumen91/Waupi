package com.waupi.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.waupi.screens.R;

public class AlertDialogManager {
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		if (status != null)
			alertDialog.setIcon((status) ? R.drawable.ic_launcher : R.drawable.ic_launcher);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}
}
