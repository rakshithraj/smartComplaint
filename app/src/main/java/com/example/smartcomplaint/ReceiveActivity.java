package com.example.smartcomplaint;
/* http://techlovejump.in/2013/11/android-push-notification-using-google-cloud-messaging-gcm-php-google-play-service-library/
 * techlovejump.in
 * tutorial link
 * 
 *  */

import org.json.JSONException;
import org.json.JSONObject;

import com.example.smartcomplaint.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveActivity extends Activity implements OnClickListener{
	Button btOK;
    TextView tvNOTIFICATION;
    public static final int NOTIFICATION_ID = 1;
  String message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		message=this.getIntent().getStringExtra("message");
		
		setContentView(R.layout.recive_notification);
		intialize();
		tvNOTIFICATION.setText(message);
		String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager nMgr = (NotificationManager) this.getApplicationContext().getSystemService(ns);
	    nMgr.cancel(NOTIFICATION_ID);
	}
		
	private void intialize() {
		// TODO Auto-generated method stub
		tvNOTIFICATION=(TextView)findViewById(R.id.tvNOTIFICATION);
		btOK=(Button)findViewById(R.id.btOK);
		btOK.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(R.id.btOK==v.getId()){
			
			finish();
		}
		
	}
	
	

}