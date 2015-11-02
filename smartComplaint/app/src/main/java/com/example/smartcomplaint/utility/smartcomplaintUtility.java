package com.example.smartcomplaint.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class smartcomplaintUtility {
	 public static void showkDailog(String str,Activity activity) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(str)
					.setTitle("")
					.setCancelable(false)
					.setPositiveButton("OK ",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {

								}
							});
			builder.show();
		
		}
	 
	 
	 public static ProgressDialog showProgressDialog(boolean isNew,Activity activity,ProgressDialog mProgressDialog) {
		 Log.d("tag"," showProgressDialog");
		
		if (isNew) {
			mProgressDialog = new ProgressDialog(activity);
			mProgressDialog.setMessage("Please wait Loading");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		} else {
			if (mProgressDialog != null) {
				if (mProgressDialog.isShowing())
					mProgressDialog.dismiss();
			}
		}
		return mProgressDialog;
	}
	 
	 
	 public static boolean isNetworkAvailable(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			// if no network is available networkInfo will be null, otherwise check if we are connected
			if (networkInfo != null && networkInfo.isConnected()) {
				return true;
			}
			return false;
		}
}
