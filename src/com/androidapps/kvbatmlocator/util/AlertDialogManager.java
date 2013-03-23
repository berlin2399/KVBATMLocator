package com.androidapps.kvbatmlocator.util;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {
	
	public void showAlertDialog(Context context, String title, String message,
			boolean status){
		
		AlertDialog alert = new AlertDialog.Builder(context).create();
		
		alert.setTitle(title);
		alert.setMessage(message);
		
		alert.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		
		alert.show();
		
	}

}
