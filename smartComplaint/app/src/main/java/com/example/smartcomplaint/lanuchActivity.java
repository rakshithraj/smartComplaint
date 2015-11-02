package com.example.smartcomplaint;



import java.io.IOException;


import com.example.smartcomplaint.utility.smartcomplaintConstant;
import com.example.smartcomplaint.webservice.ConnectSmartComplaintService;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class lanuchActivity extends Activity  {

   private static final String PROPERTY_APP_VERSION = "appVersion";
   public static final String PROPERTY_REG_ID = "registration_id";
   Context context;
   GoogleCloudMessaging gcm;
   String user;
   static final String GOOGLE_SENDER_ID = "420315138612"; 
   boolean SERVICE_NOT_AVAILABLE=false;
    int retryCount=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lanuch_page);
		context=this.getApplicationContext();
		gcm = GoogleCloudMessaging.getInstance(this);
		smartcomplaintConstant.REGISTER_KEY = getRegistrationId(context);
		
			final SharedPreferences prefs = getGCMPreferences(context);
			 int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
			    int currentVersion = getAppVersion(context);
			    if (registeredVersion != currentVersion) {
			    	new RegisterBackground().execute();
			       
			    }
		
		
		 else
		if(smartcomplaintConstant.REGISTER_KEY==""){
			
			new RegisterBackground().execute();
		}
		else{
			Thread timer = new Thread(){
				   public void run(){
						 try{
						 sleep(300);
						 } catch (InterruptedException e){
							 e.printStackTrace();
							 
						 }finally{
							 final SharedPreferences prefs = getSharedPreferences(lanuchActivity.class.getSimpleName(),lanuchActivity.this.getApplicationContext().MODE_PRIVATE);
							 boolean login=prefs.getBoolean("login", false);
							 if(login){
								 Intent mainPage= new Intent(lanuchActivity.this,dashboardActivity.class); 
								    startActivity(mainPage);
							 }else{
								 Log.d("tag", "call homeActivity");
							Intent mainPage= new Intent(lanuchActivity.this,homeActivity.class); 
							mainPage.putExtra("calledBy", "lanuchActivity");
						    startActivity(mainPage);
							 }
						 }
					}
		       };
		       timer.start();
		  }
		
		
		
		
   }

	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 Log.d("tag", "call finish");
		finish();
	}

	
	   private String getRegistrationId(Context context) {
		    final SharedPreferences prefs = getGCMPreferences(context);
		    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		    if (registrationId=="") {
		        Log.i("tag", "Registration not found.");
		        return "";
		    }
		    
		    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		    int currentVersion = getAppVersion(context);
		    if (registeredVersion != currentVersion) {
		        Log.i("tag", "App version changed.");
		        return "";
		    }
		    return registrationId;
		}
	   
	   private SharedPreferences getGCMPreferences(Context context) {
		    
		    return getSharedPreferences(lanuchActivity.class.getSimpleName(),
		            Context.MODE_PRIVATE);
		}
	   
	   private static int getAppVersion(Context context) {
		    try {
		        PackageInfo packageInfo = context.getPackageManager()
		                .getPackageInfo(context.getPackageName(), 0);
		        return packageInfo.versionCode;
		    } catch (NameNotFoundException e) {
		        // should never happen
		        throw new RuntimeException("Could not get package name: " + e);
		    }
		}
	   
	   
	   class RegisterBackground extends AsyncTask<String,String,String>{

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(!SERVICE_NOT_AVAILABLE){
				 final SharedPreferences prefs = getSharedPreferences(lanuchActivity.class.getSimpleName(),lanuchActivity.this.getApplicationContext().MODE_PRIVATE);
				 boolean login=prefs.getBoolean("login", false);
				 user=prefs.getString("userID", "");
				 if(login){
					 
					 new Task().execute();
					
				 }else{
				Intent mainPage= new Intent(lanuchActivity.this,homeActivity.class); 
			    startActivity(mainPage);
				 }
				
				}
			}

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				Log.d("tag", "doInBackground");
				String msg = "";
				try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                 
	                }
	            	Log.d("tag"," before gcm.register");
	            	
	            	smartcomplaintConstant.REGISTER_KEY= gcm.register(GOOGLE_SENDER_ID);
	                
	                msg = "Dvice registered, registration ID=" + smartcomplaintConstant.REGISTER_KEY;
	                Log.d("tag", msg);	                	                
	                // Persist the regID - no need to register again.
	               storeRegistrationId(context, smartcomplaintConstant.REGISTER_KEY);
	               SERVICE_NOT_AVAILABLE=false;
	            } catch (IOException ex) {
	            	Log.d("tag","IOException "+ ex.toString());
	                msg = "Error :" + ex.getMessage();
	                SERVICE_NOT_AVAILABLE=true;
	                if(retryCount<=5){
	                	retryCount++;
				  	new RegisterBackground().execute();				  	
	                }else{
	                	SERVICE_NOT_AVAILABLE=false;
	                }
	                
	            }catch (NullPointerException ex) {
					Log.d("tag","NullPointerException "+ ex.toString());
					msg = "Error :" + ex.getMessage();
					SERVICE_NOT_AVAILABLE=true;
					if(retryCount<=5){
						retryCount++;
						new RegisterBackground().execute();
					}else{
						SERVICE_NOT_AVAILABLE=false;
					}
				}

	            return msg;
	        }
			
			
		}
	   
	   private void storeRegistrationId(Context context, String regId) {
		    final SharedPreferences prefs = getGCMPreferences(context);
		    int appVersion = getAppVersion(context);
		    Log.i("tag", "Saving regId on app version " + appVersion);
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putString(PROPERTY_REG_ID, regId);
		    editor.putInt(PROPERTY_APP_VERSION, appVersion);
		    editor.commit();
		}
	

	   
	   
	   
	   class Task extends AsyncTask<String,String,String>{
		   String response = null;
			String mConnectionData;
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				Log.d("tag", "response="+response);
				 Intent mainPage= new Intent(lanuchActivity.this,dashboardActivity.class); 
				    startActivity(mainPage);
				   
			}

			@Override
			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
			
				
				try {
					Log.d("tag", "update");
					ConnectSmartComplaintService httpReq=new ConnectSmartComplaintService();
					response=httpReq.executeget(smartcomplaintConstant.GCM_UPDATE_URL+"newGCM="+ smartcomplaintConstant.REGISTER_KEY+"&userId="+user, null, context);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("tag", "error="+e.toString());
				}
				
				
	            return "";
	        }
			
			
		}
	   
	   
}
