package com.example.smartcomplaint;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;


import com.example.smartcomplaint.utility.smartcomplaintConfig;
import com.example.smartcomplaint.utility.smartcomplaintUtility;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class complaintActivity extends Activity implements OnClickListener{

	String category;
	Button btUpload,btTakePic,btSubmit,btPriority,bt_LOCATION;
	ImageView imgUploaded;
	EditText etMessage;
	String path,value,myLocation;
	File file;
	Intent intent ;
	final static int camdata = 0;
	private static final int REQUEST_CHOOSER = 1234; 
	int Priority=0;
	boolean picture=false,location=false;
	String [] mPriorityList={"low","high"};
	String [] mLocationList={"manually","atomaically"};
	ProgressDialog mProgressDialog;
	String userId;
	LocationManager locationManager;
	Location mLocation;
	double longitude,latitude;
	SQLiteDatabase mydb;
	private static String DBNAME = "smartcomplaint.db"; 
    private static String TABLE_COMPLAINT="complaint_table"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);	
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.complaint_page);
		category=this.getIntent().getStringExtra("key");
		 final SharedPreferences prefs = getSharedPreferences(LanuchActivity.class.getSimpleName(),this.getApplicationContext().MODE_PRIVATE);
		 userId=prefs.getString("userID","");
	    intialize();
	}

	private void intialize() {
		// TODO Auto-generated method stub
		etMessage=(EditText)findViewById(R.id.etMessage);
		btUpload=(Button)findViewById(R.id.btUpload);
		btUpload.setOnClickListener(this);
		btTakePic=(Button)findViewById(R.id.btTakePic);
		btTakePic.setOnClickListener(this);
		btSubmit=(Button)findViewById(R.id.btSubmit);
		btSubmit.setOnClickListener(this);
		btPriority=(Button)findViewById(R.id.btPriority);
		btPriority.setOnClickListener(this);
		bt_LOCATION=(Button)findViewById(R.id.bt_LOCATION);
		bt_LOCATION.setOnClickListener(this);		
		imgUploaded=(ImageView)findViewById(R.id.imgUploaded);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
		case R.id.btUpload:
			Intent getContentIntent =createGetContentIntent();
		        intent = Intent.createChooser(getContentIntent, "Select a file");
		        startActivityForResult(intent, REQUEST_CHOOSER);
			break;
			
		case R.id.btTakePic:
			intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			 startActivityForResult(intent,camdata);
			
			break;
		case R.id.btPriority:
			PriorityPicker();
			
			break;
		case R.id.bt_LOCATION:
			LocationPicker();
			
			break;
		case R.id.btSubmit:
			if(validateData()){
				if(smartcomplaintUtility.isNetworkAvailable(this)){
					Task task=new Task(this.getApplicationContext());
					task.execute();
					}else{
						smartcomplaintUtility.showkDailog("connect to internet", this);
					}
		
				 
			}								
			break;		
		}
				
		
	}
	


	public boolean validateData() {
		// TODO Auto-generated method stub		
		if(etMessage.getText().toString().matches(""))
		{
			smartcomplaintUtility.showkDailog("Enter message", this);
			return false;
		}
		if(!picture)
    	{
    		smartcomplaintUtility.showkDailog("upload images from gallery and take new pic", this);
    		return false;
    	}
		if(!location)
    	{
    		smartcomplaintUtility.showkDailog("Location not set", this);
    		return false;
    	}
		return true;
	}

	private void LocationPicker() {
		// TODO Auto-generated method stub
		 final AlertDialog.Builder ad = new AlertDialog.Builder(this);
		  
		  ad.setTitle("Set Location");
		  ad.setSingleChoiceItems(mLocationList, -1,  new DialogInterface.OnClickListener() {
			  
		   @Override
		   public void onClick(DialogInterface arg0, int arg1) {

		   
			   
			   value=mLocationList[arg1].toString();

		   }
		  });
		  ad.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    // TODO Auto-generated method stub
			
		   }
		  });
		  ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				  if(value=="manually"){
					  setLocationManually();
				  }else if(value=="atomaically"){
					  
					  AtomaicallyLocationPicker() ;
					  
				  }
				  
			   
			   }

			
			  });
		  
		  
		  ad.show();
		 
	}

	
	 private final Handler handler=new Handler(){
		 public void handleMessage(Message msg) {
			 String aResponse = msg.getData().getString("message");
			 if(aResponse.matches("getLocatonUsingNetwork"))
				 getLocatonUsingNetwork();
			 if(aResponse.matches("getLocatonUsingGps"))
				 getLocatonUsingGps();
			 
			 
		 }
	};
	
	protected void AtomaicallyLocationPicker() {
		
		boolean value=checkGpsEnabled();
		Log.d("tag", "value="+value);
		if(!value){
		final AlertDialog.Builder ad = new AlertDialog.Builder(this);		  
		  ad.setTitle("Set Location");
		  ad.setMessage("Enable gps for accurate location");  
		  ad.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    // TODO Auto-generated method stub			
			   new Thread(){
				    public void run(){
				    	Message msgObj = handler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", "getLocatonUsingNetwork");
                        msgObj.setData(b);
				    	handler.sendMessage(msgObj);
				 	 
				    }
				  }

				 .start();				
		   }
		
		  });
		  ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				 
				   startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
			   
			   }

			
			  });
		  
		  
		  ad.show();
		}else{
			  new Thread(){
				    public void run(){
				     	Message msgObj = handler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", "getLocatonUsingGps");
                        msgObj.setData(b);
				    	handler.sendMessage(msgObj);
				    
				    }
				  }.start();
			
		}
		
		
	}

	public void getLocatonUsingGps() {
		// TODO Auto-generated method stub
		Log.d("tag", "gps");
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
		Log.d("tag", "isGPSEnabled="+isGPSEnabled);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		  mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.d("tag", "mLocation="+mLocation);
		  if (mLocation!=null)       
	        {
	        	 latitude = mLocation.getLatitude();
	        	 longitude = mLocation.getLongitude();
	        	 Log.d("tag", "latitude="+latitude);
	        	 Log.d("tag", "longitude="+longitude);
	        	 getAddress();
	        }else{
	        	getLocatonUsingNetwork();
	        }
		
	}



	public void getLocatonUsingNetwork() {
		// TODO Auto-generated method stub
		Log.d("tag", "Network");
		  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		   locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		    boolean isNetworkEnabled = locationManager
	                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		    Log.d("tag", "isNetworkEnabled="+isNetworkEnabled);
		   mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		   if (mLocation == null){

	        	showDailogForLocationServiceEnable();
	        	return;
	        }
	        else
	        {
	        	 latitude = mLocation.getLatitude();
	        	 longitude = mLocation.getLongitude();
	        	 Log.d("tag", "latitude="+latitude);
	        	 Log.d("tag", "longitude="+longitude);
	        	 getAddress();
	        }
	}
	
	public void getAddress() {
		// TODO Auto-generated method stub
		
		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(this, Locale.getDefault());
			addresses = geocoder.getFromLocation(latitude, longitude, 1);

			myLocation = addresses.get(0).getAddressLine(0)+","+addresses.get(0).getAddressLine(1)+","+ addresses.get(0).getAddressLine(2);
			Log.d("tag", "myLocation="+myLocation);
			if(!myLocation.matches(""))
				location=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      // Called when a new location is found by the network location provider.
	    	mLocation=location;
	    	Log.d("tag", "listner mLocation="+mLocation);
	    	 if (mLocation!=null)       
		        {
		        	 latitude = mLocation.getLatitude();
		        	 longitude = mLocation.getLongitude();
		        	 Log.d("tag", "latitude="+latitude);
		        	 Log.d("tag", "longitude="+longitude);
		        	 getAddress();
		        }
	    
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };
	
	
	
	private boolean checkGpsEnabled() {  
		// TODO Auto-generated method stub
		boolean gps_enabled=false;;
		 if(locationManager==null)
             locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		 try{
	            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	            }catch(Exception ex){}
		
		return gps_enabled;
	}

	private void setLocationManually() {
		// TODO Auto-generated method stub
		final EditText input = new EditText(complaintActivity.this);
		input.setMaxLines(3);
		input.setHeight(50);
		new AlertDialog.Builder(complaintActivity.this)
		    .setTitle("Enter Locations")
		    .setView(input)
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int whichButton) {
		        	 myLocation = input.getText().toString(); 
		        	 if(!myLocation.matches(""))
		        	    location=true;
		         }
		    })
		    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int whichButton) {
		                // Do nothing.
		         }
		    }).show();
		
		
		
	}
	public void PriorityPicker(){
		  
		 final AlertDialog.Builder ad = new AlertDialog.Builder(this);
		  
		  ad.setTitle("Set Priority");
		  ad.setSingleChoiceItems(mPriorityList, -1,  new DialogInterface.OnClickListener() {
			  
		   @Override
		   public void onClick(DialogInterface arg0, int arg1) {

		   
			   
			   value=mPriorityList[arg1].toString();

		   }
		  });
		  ad.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    // TODO Auto-generated method stub
			
		   }
		  });
		  ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				  
				   if(value=="low"){
					   
					   Priority=0;
				   }else if(value=="high"){
					   Priority=1;
					   
				   }
			   
			   }
			  });
		  
		  
		  ad.show();
		 

	}
	
	public static Intent createGetContentIntent() {
		// Implicitly allow the user to select a particular kind of data
		final Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		// The MIME data type filter
		intent.setType("image/*"); 
		// Only return URIs that can be opened with ContentResolver
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		return intent;
	}
	
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CHOOSER:   
                if (resultCode == RESULT_OK) {  
                	 Uri result = intent == null || resultCode != RESULT_OK ? null  
                             : intent.getData(); 
                	 imgUploaded.setImageURI(result);
                	 path=getPath(this.getApplicationContext(),result);                	 
                    file=new File(path);
                   picture=true;
                }
                
                break;   
            case camdata:
            	Log.d("tag","camera");
            	Log.d("tag","resultCode="+resultCode);
            	if(resultCode == RESULT_OK){
            		
            		Log.d("tag","resultCode="+resultCode);
            		 Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{Media.DATA, Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, Media.DATE_ADDED, null, "date_added ASC");
            	        if(cursor != null && cursor.moveToFirst())
            	        { String picturePath;
            	          Uri selectedImage;
            	            do 
            	            {
            	                 picturePath =cursor.getString(cursor.getColumnIndex(Media.DATA));
            	                  selectedImage = Uri.parse(picturePath);

            	            }
            	            while(cursor.moveToNext());
            	                cursor.close();
            	            file = new File(picturePath);
            	           


            	            imgUploaded.setImageURI(selectedImage);
            	        }
            	        picture=true;
        		}
            	break;
            	
            	
            case 100:
            	boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
            	if(isGPSEnabled)
            	  getLocatonUsingGps() ;
            	else
            		getLocatonUsingNetwork();
            	break;
            	
            case 101:
            	if(resultCode == RESULT_OK)
            		  getLocatonUsingNetwork();
            	break;
            	
         
                
            
          }
        }
	
	
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
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
			 mProgressDialog=smartcomplaintUtility.showProgressDialog(true,complaintActivity.this,mProgressDialog);
		}	
			
	
		@Override
		protected String doInBackground(Context... arg0) {			
		
			
			response=submitToServer();
			
			
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			
			super.onPostExecute(result);
			Log.d("tag", "onPostExecute=");
			if (mProgressDialog != null)
 				if (mProgressDialog.isShowing()) {
 					mProgressDialog=smartcomplaintUtility.showProgressDialog(false,complaintActivity.this,mProgressDialog);
 				}
			if(response!=null){								
				int size=response.split(",").length;
				String array[]=new String[size];
				array=response.split(",");
				Log.d("tag","Id="+array[0]);
				mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
				 mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE_COMPLAINT +" (ID INTEGER PRIMARY KEY, complaintId TEXT)");
				 mydb.execSQL("INSERT INTO " + TABLE_COMPLAINT + "(complaintId) VALUES('"+array[0]+"')");
			     mydb.close();
				
			}
			
			finish();
	
			
			
		}
	
	
		

	}

	public String submitToServer() {

		 StringBuffer response = null;
		 int serverResponseCode = 0;
		 String fileName =file.getPath();
		 HttpURLConnection conn = null;
		 DataOutputStream dos = null;
		 String lineEnd = "\r\n";
		 String twoHyphens = "--";
		 String boundary = "*****";
		 int bytesRead, bytesAvailable, bufferSize;
		 byte[] buffer;
		 int maxBufferSize = 1 * 1024 * 1024;
		 File sourceFile =file;
		 {
			 if(sourceFile.exists()){
				 Log.d("tag", "file size="+sourceFile.length());
			 }
		 try {
		 FileInputStream fileInputStream = new FileInputStream(
		 sourceFile);
		 URL url = new URL(smartcomplaintConfig.COMPLAINT_URL);
		 // Open a HTTP connection to the URL
		 conn = (HttpURLConnection) url.openConnection();
		 conn.setDoInput(true); // Allow Inputs
		 conn.setDoOutput(true); // Allow Outputs
		 conn.setUseCaches(false); // Don't use a Cached Copy
		 conn.setConnectTimeout(30000);
		 conn.setReadTimeout(30000);
		 conn.setChunkedStreamingMode(1024);
		 conn.setRequestMethod("POST");
		 conn.setRequestProperty("Connection", "Keep-Alive");
		 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		 conn.setRequestProperty("Content-Type",
		 "multipart/form-data;boundary=" + boundary);
		 conn.setRequestProperty("uploaded_file", fileName);
		 
		 dos = new DataOutputStream(conn.getOutputStream());	
		 dos.writeBytes(twoHyphens + boundary + lineEnd);
		 dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
		 + fileName + "\"" + lineEnd);
		 dos.writeBytes(lineEnd);
		 // create a buffer of maximum size
		 bytesAvailable = fileInputStream.available();
		 bufferSize = Math.min(bytesAvailable, maxBufferSize);
		 buffer = new byte[bufferSize];
		 // read file and write it into form...
		 bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		 while (bytesRead > 0)
		 {
		 dos.write(buffer, 0, bufferSize);
		 bytesAvailable = fileInputStream.available();
		 bufferSize = Math.min(bytesAvailable, maxBufferSize);
		 bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		 }
		 // send multipart form data necesssary after file data...
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		 
		 
	     dos.writeBytes(twoHyphens + boundary + lineEnd); 
		 dos.writeBytes("Content-Disposition: form-data; name=\"myLocation\"" + lineEnd);
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(myLocation);
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		 
		 dos.writeBytes(twoHyphens + boundary + lineEnd); 
		 dos.writeBytes("Content-Disposition: form-data; name=\"Priority\"" + lineEnd);
		 dos.writeBytes(lineEnd);Log.d("tag", "Priority="+Priority);	
		 dos.writeBytes(Priority+"");
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		 
		 dos.writeBytes(twoHyphens + boundary + lineEnd); 
		 dos.writeBytes("Content-Disposition: form-data; name=\"User\"" + lineEnd);
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(userId);
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		 
		 
		 
		 dos.writeBytes(twoHyphens + boundary + lineEnd); 
		 dos.writeBytes("Content-Disposition: form-data; name=\"Category\"" + lineEnd);
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(category);
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		 
		 
		 dos.writeBytes(twoHyphens + boundary + lineEnd); 
		 dos.writeBytes("Content-Disposition: form-data; name=\"message\"" + lineEnd);
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(etMessage.getText().toString());
		 dos.writeBytes(lineEnd);
		 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	
		 // Responses from the server (code and message)
		 serverResponseCode = conn.getResponseCode();
		 String serverResponseMessage = conn.getResponseMessage();
		 Log.d("tag", "HTTP Response is : "
		 + serverResponseMessage + ": " + serverResponseCode);
		 if (serverResponseCode == 200)
		  {
		 System.out.println("Done Send to Server !!!");
		 InputStream is = conn.getInputStream();
		 BufferedReader rd = new BufferedReader(
		 new InputStreamReader(is));
		 String line;
		 response = new StringBuffer();
		 while ((line = rd.readLine()) != null) {
		 response.append(line);
		 response.append('\r');
		 }
		 rd.close();
		 is.close();
		 }
		 // close the streams //
		 fileInputStream.close();
		 dos.flush();
		 dos.close();
		
		 Log.d("tag", "response="+response);
		 } catch (MalformedURLException ex) {
		 Log.d("tag", "error: " + ex.getMessage(), ex);
		 } catch (Exception e) {
			 submitToServer();
		
		 }
		 return response.toString();
		 } // End else 
		
		
		
	}
	
	
	public  void showDailogForLocationServiceEnable(){
        AlertDialog.Builder builder = new AlertDialog.Builder(complaintActivity.this);
        builder.setMessage("Please enable Use wireless network  in your Locations Services")
        .setTitle("DenaBank")
        .setCancelable(false)
        .setPositiveButton("OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);

            }
        });
        builder.show();
    }
	
	
}
