package com.example.smartcomplaint;

import java.io.File;
import java.util.ArrayList;

import com.example.smartcomplaint.loginActivity.Task;
import com.example.smartcomplaint.utility.smartcomplaintConstant;
import com.example.smartcomplaint.utility.smartcomplaintUtility;
import com.example.smartcomplaint.webservice.ConnectSmartComplaintService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComplaintStatus_Activity extends Activity implements OnClickListener{

	Button btSearch;
	AutoCompleteTextView etId;
	String id;
	ProgressDialog mProgressDialog;
	String[] myid;
	TextView tvReport;
	SQLiteDatabase mydb;
	private static String DBNAME = "smartcomplaint.db"; 
    private static String TABLE_COMPLAINT="complaint_table"; 
    ArrayList<String> allId=new  ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);			
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complaintsatus);
		get_allId();	   
		intialize();
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			    android.R.layout.simple_dropdown_item_1line, allId);
		etId.setAdapter(adapter);
		etId.setThreshold(1);
	}
	private void get_allId() {
		// TODO Auto-generated method stub
		 mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
		 mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE_COMPLAINT +" (ID INTEGER PRIMARY KEY, complaintId TEXT)");
		 String sqlQuery = "SELECT * FROM " + TABLE_COMPLAINT;
	     Cursor allrows = mydb.rawQuery(sqlQuery, null);	     
	   
	    
	     if(allrows.moveToFirst())
	     {do{
	    	 
	        String label = allrows.getString(1);
	        allId.add(label);
	     }
         while(allrows.moveToNext());
	     }
	     mydb.close();
	}
	private void intialize() {
		// TODO Auto-generated method stub
		tvReport=(TextView)findViewById(R.id.tvReport);
		btSearch=(Button)findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		etId=(AutoCompleteTextView)findViewById(R.id.etId);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		tvReport.setText("");
		if(v.getId()==R.id.btSearch){
			id=etId.getText().toString();	
			
			myid = id.split("_");
			

			
			if(myid[0].matches("traffic") ||myid[0].matches("police")  || myid[0].matches("mescom")||myid[0].matches("mnpo")){
				Log.d("tag", "acll task");
				if(myid.length==2){
					if(smartcomplaintUtility.isNetworkAvailable(this)){
						Task task=new Task(this.getApplicationContext());
						task.execute();
						}else{
							smartcomplaintUtility.showkDailog("connect to internet", this);
						}
			  }else{
				  smartcomplaintUtility.showkDailog("enter valid Id", ComplaintStatus_Activity.this);
			  }
				
			}
			
			
			
		}
		
		
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
			 mProgressDialog=smartcomplaintUtility.showProgressDialog(true,ComplaintStatus_Activity.this,mProgressDialog);
			
		}	
		
		@Override
		protected String doInBackground(Context... arg0) {		
			try{
			ConnectSmartComplaintService httpReq=new ConnectSmartComplaintService();
			response=httpReq.executeget(smartcomplaintConstant.STATUS_URL+"id="+ myid[1]+"&tablename="+myid[0]+"table", null, mContext);
			}catch(Exception e){
				
			}
			
			return null;
		}
	
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			if (mProgressDialog != null)
 				if (mProgressDialog.isShowing()) {
 					mProgressDialog=smartcomplaintUtility.showProgressDialog(false,ComplaintStatus_Activity.this,mProgressDialog);
 				}	
			if(response!=null){
			GetResponseResult(response);
			}else
				smartcomplaintUtility.showkDailog("enter valid Id", ComplaintStatus_Activity.this);
			Log.d("tag", "onPostExecute=");
			Log.d("tag", "response="+response);
			
			
		
		     }
	
		
	}



	public void GetResponseResult(String response) {
		
		Log.d("tag", "response="+response);
		int value=Integer.parseInt(response);
		switch(value)
		{
		case 0:
			tvReport.setText("pending");
			break;
			
		case 1:
			tvReport.setText("inprogress");
			break;
			
		case 2:
			tvReport.setText("Resolved");
			break;
		default:smartcomplaintUtility.showkDailog("enter valid Id", ComplaintStatus_Activity.this);
		
		}
		
		
	}

}
