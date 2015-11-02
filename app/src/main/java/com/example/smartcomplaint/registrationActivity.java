package com.example.smartcomplaint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;


import com.example.smartcomplaint.utility.SmartComplaintConstant;
import com.example.smartcomplaint.utility.smartcomplaintConfig;
import com.example.smartcomplaint.utility.smartcomplaintUtility;


public class registrationActivity extends Activity implements OnClickListener{
		
	



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();

		
	}



	EditText etEmail,etPassword,etRetypePassword,etName,etAge,etContact,etCity,etState;;
	Button btSingUp;
	RadioButton radio_male,radio_female;
	String Email,Password,RetypePassword,Name,Age,Contact,City,State;
	boolean male,female;
	boolean registrationSucess=false;
	ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_page);
		intialize();
		
	}


	
	private void intialize() {
		etEmail=(EditText)findViewById(R.id.etEmail);
		etPassword=(EditText)findViewById(R.id.etPassword);
		etRetypePassword=(EditText)findViewById(R.id.etRetypePassword);
		etName=(EditText)findViewById(R.id.etName);
		etAge=(EditText)findViewById(R.id.etAge);
		etContact=(EditText)findViewById(R.id.etContact);
		etCity=(EditText)findViewById(R.id.etCity);
		etState=(EditText)findViewById(R.id.etState);
		radio_male=(RadioButton)findViewById(R.id.radio_male);
		radio_female=(RadioButton)findViewById(R.id.radio_female);
		btSingUp=(Button)findViewById(R.id.btSingUp);
		btSingUp.setOnClickListener(this);
		
		
	}



	public class Task extends AsyncTask<Context, Integer, String> {

		String registrationType;
		Context mContext;
		String response = null;
		String mConnectionData;
		Task(Context mContext) {
			this.mContext = mContext;

		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("tag", "onPreExecute=");
			 mProgressDialog=smartcomplaintUtility.showProgressDialog(true,registrationActivity.this,mProgressDialog);
		}	
			
	
		@Override
		protected String doInBackground(Context... arg0) {			
		
			 try {
				response=register();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			Log.d("tag", "onPostExecute="+response);
			if (mProgressDialog != null)
 				if (mProgressDialog.isShowing()) {
 					mProgressDialog=smartcomplaintUtility.showProgressDialog(false,registrationActivity.this,mProgressDialog);
 				}	
			GetResponseResult(response);
			
			
		}
	
	
		

	}



	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.btSingUp){
		getUserdata();
		if(validateData()){
			if(smartcomplaintUtility.isNetworkAvailable(this)){
				Task task=new Task(this.getApplicationContext());
				task.execute();
				}else{
					smartcomplaintUtility.showkDailog("connect to internet", this);
				}
			
		}
		
		
		}
		
		
	}



	public void GetResponseResult(String response) {
		
		response=response.trim();
		if(response.matches("") || response==null){
			smartcomplaintUtility.showkDailog("some thing went wrong",registrationActivity.this);
		}else
		if(response.matches("user_exits")){
			
			smartcomplaintUtility.showkDailog("user with given email allredy exits",registrationActivity.this);
			
		}else{
			
			if(response.matches("inserted")){
				registrationSucess=true;
				 final SharedPreferences prefs = getSharedPreferences(LanuchActivity.class.getSimpleName(),registrationActivity.this.getApplicationContext().MODE_PRIVATE);
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putBoolean("login",true);
			    editor.putString("userID",Email);
			    editor.commit();
				//Intent intent=new Intent(registrationActivity.this,dashboardActivity.class);
				//startActivity(intent);

				Intent resultIntent = new Intent();
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				
			}else if(response.matches("insert_failed")){
				smartcomplaintUtility.showkDailog("some thing went wrong ",registrationActivity.this);
			}
			
		}
		
		
	}



	private boolean validateData() {
		// TODO Auto-generated method stub
       if(Email.matches("")){
    	   smartcomplaintUtility.showkDailog("Enter email", this);
			return false;
		}else if(!checkEmailCorrect(Email)){
			smartcomplaintUtility.showkDailog("Enter valid  email", this);
			return false;
		}
       if(Password.matches("")){
    	   smartcomplaintUtility.showkDailog("Enter Password", this);
			return false;
		}else if(Password.length()<6){
			smartcomplaintUtility.showkDailog("Password to short", this);
			return false;
		}
       if(RetypePassword.matches("")){
    	   smartcomplaintUtility.showkDailog("Renter Password", this);
			return false;
		}
       if(Name.matches("")){
    	   smartcomplaintUtility.showkDailog("Enter Name", this);
			return false;
		}
       if(Age.matches("")){
    	   smartcomplaintUtility.showkDailog("Enter Age", this);
			return false;
		}
       if(Contact.matches("")){
    	   smartcomplaintUtility.showkDailog("Enter Contact", this);
			return false;
		}
       if(City.matches("")){
    	   smartcomplaintUtility.showkDailog("Enter City", this);
			return false;
		} 
       if(male==false && female==false){
    	   smartcomplaintUtility.showkDailog("Select sex", this);
    	   return false;
       }
       if(State.matches("")){
    	   smartcomplaintUtility.showkDailog("Enter State", this);
    	   return false;
       }
       if(!Password.equals(RetypePassword)){
    	   smartcomplaintUtility.showkDailog("Enter Password not match retyped", this);
    	   return false;
       }
            
		return true;
	}



	private void getUserdata() {
		Email=etEmail.getText().toString();		  
		Password=etPassword.getText().toString();
		RetypePassword=etRetypePassword.getText().toString();
		Name=etName.getText().toString();
		Age=etAge.getText().toString();
		Contact=etContact.getText().toString();
		City=etCity.getText().toString();
		State=etState.getText().toString();
		male=radio_male.isChecked();
		female=radio_female.isChecked();	
	}
	
	
	
	public static boolean checkEmailCorrect(String Email) {
		String pttn = "^\\D.+@.+\\.[a-z]+";
		Pattern p = Pattern.compile(pttn);
		Matcher m = p.matcher(Email);

		if (m.matches()) {
			return true;
		}

		return false;
	}
	
	
	
	public  String  register()  throws  UnsupportedEncodingException
    {
        // Get user defined values
      
         
         // Create data variable for sent values to server  
         
          String data = URLEncoder.encode("Email", "UTF-8") 
                       + "=" + URLEncoder.encode(Email, "UTF-8"); 

          data += "&" + URLEncoder.encode("Password", "UTF-8") + "="
                      + URLEncoder.encode(Password, "UTF-8"); 

          data += "&" + URLEncoder.encode("Name", "UTF-8") 
                      + "=" + URLEncoder.encode(Name, "UTF-8");

          data += "&" + URLEncoder.encode("Age", "UTF-8") 
                      + "=" + URLEncoder.encode(Age, "UTF-8");
          
          data += "&" + URLEncoder.encode("Contact", "UTF-8") 
                  + "=" + URLEncoder.encode(Contact, "UTF-8");
          
          data += "&" + URLEncoder.encode("City", "UTF-8") 
                  + "=" + URLEncoder.encode(City, "UTF-8");
          
          data += "&" + URLEncoder.encode("State", "UTF-8") 
                  + "=" + URLEncoder.encode(City, "UTF-8");
          if(male){
          data += "&" + URLEncoder.encode("sex", "UTF-8") 
                  + "=" + URLEncoder.encode("male", "UTF-8");
          }else{
        	  data += "&" + URLEncoder.encode("sex", "UTF-8") 
                      + "=" + URLEncoder.encode("female", "UTF-8");
          }
          
          data += "&" + URLEncoder.encode("GcmId", "UTF-8") 
                  + "=" + URLEncoder.encode(SmartComplaintConstant.REGISTER_KEY, "UTF-8");
          
          
          String text = "";
          BufferedReader reader=null;

          // Send data 
        try
        { 
          
            // Defined URL  where to send data
            URL url = new URL(smartcomplaintConfig.REGISTER_URL);
             
         // Send POST data request

          URLConnection conn = url.openConnection(); 
          conn.setDoOutput(true); 
          OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
          wr.write( data ); 
          wr.flush(); 
      
          // Get the server response 
           
        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        // Read Server Response
        while((line = reader.readLine()) != null)
            {
                   // Append server response in string
                   sb.append(line + "\n");
            }
            
            
            text = sb.toString();
        }
        catch(Exception ex)
        {
             
        }
        finally
        {
            try
            {
 
                reader.close();
            }

            catch(Exception ex) {}
        }
              
     return text;
        
    }
	
	
	

    
	
	
	
}
