package com.example.smartcomplaint;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.smartcomplaint.SmartComplaintInterface.ServerResponse;
import com.example.smartcomplaint.dao.LoginUser;
import com.example.smartcomplaint.utility.smartcomplaintConfig;
import com.example.smartcomplaint.utility.smartcomplaintUtility;
import com.example.smartcomplaint.webservice.AppController;
import com.example.smartcomplaint.webservice.ConnectWebService;
import com.example.smartcomplaint.webservice.MultipartRequestTest;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import tyrantgit.explosionfield.ExplosionField;
import tyrantgit.explosionfield.ExpolisionInterface;

public class DashboardActivity extends AppCompatActivity implements OnClickListener,NavigationView.OnNavigationItemSelectedListener,ExpolisionInterface {




	DrawerLayout mDrawerLayout;
	Toolbar mToolbar;
	Button btTraffic,btPolice,btMescom,btMNPO,btLogout;
	Intent intent;
	NetworkImageView profileImageView;
	TextView tvName;
	LinearLayout btStatus,btAboutUs;
	private ExplosionField mExplosionField;
	private static final int REQUEST_CHOOSER = 1234;
	final static int camdata = 0;
	ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_page);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);


		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		intialize();

	}


	private void intialize() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT<14) {

			btTraffic = (Button) findViewById(R.id.btTraffic);
			btTraffic.setOnClickListener(this);
			btPolice = (Button) findViewById(R.id.btPolice);
			btPolice.setOnClickListener(this);
			btMescom = (Button) findViewById(R.id.btMescom);
			btMescom.setOnClickListener(this);
			btMNPO = (Button) findViewById(R.id.btMNPO);
			btMNPO.setOnClickListener(this);

		}else{
			mExplosionField = ExplosionField.attach2Window(this);
			addListener(findViewById(R.id.lyLayout));
		}


		btStatus = (LinearLayout) findViewById(R.id.btStatus);
		btStatus.setOnClickListener(this);
		btAboutUs = (LinearLayout) findViewById(R.id.btAboutUs);
		btAboutUs.setOnClickListener(this);
		btLogout = (Button) findViewById(R.id.btLogout);
		btLogout.setOnClickListener(this);
		profileImageView = (NetworkImageView) this.findViewById(R.id.profileImageView);
		tvName = (TextView) this.findViewById(R.id.tvName);

		LoginUser loginUser=new LoginUser();
		loginUser=loginUser.DeSerialize(this);
		if(loginUser!=null) {
			if(imageLoader==null)
			 imageLoader = AppController.getInstance().getImageLoader();
			profileImageView.setDefaultImageResId(R.drawable.profile_icon);
			if(loginUser.getType().equals("FACEBOOK")) {
				profileImageView.setImageUrl(loginUser.getProfile_pic() + "?type=large", imageLoader);

			}
			else {
				profileImageView.setImageUrl(smartcomplaintConfig.BASIC_URL+loginUser.getProfile_pic(), imageLoader);

			}
			profileImageView.setOnClickListener(this);
			tvName.setText(loginUser.getName());
		}



	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btTraffic:
			 intent=new Intent(DashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "traffictable");
			 startActivity(intent);

			break;
		case R.id.btPolice:
			 intent=new Intent(DashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "policetable");
			 startActivity(intent);

			break;
		case R.id.btMescom:
			 intent=new Intent(DashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "mescomtable");
			 startActivity(intent);
			
			break;
		case R.id.btStatus:
			 intent=new Intent(DashboardActivity.this,AllStatusActivity.class);
			 intent.putExtra("key", "mnpotable");
			 startActivity(intent);
			
			break;
		case R.id.btMNPO:
			 intent=new Intent(DashboardActivity.this,complaintActivity.class);
			 intent.putExtra("key", "mnpotable");
			 startActivity(intent);
			
			break;
		case R.id.btAboutUs:
			 intent=new Intent(DashboardActivity.this,aboutus_Activity.class);
			 intent.putExtra("key", "mnpotable");
			 startActivity(intent);
			
			break;
			case R.id.btLogout:
				final SharedPreferences prefs = getSharedPreferences(LanuchActivity.class.getSimpleName(),this.getApplicationContext().MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putBoolean("login", false);
				editor.commit();

				FacebookSdk.sdkInitialize(this.getApplicationContext());
				boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
				Profile profile = Profile.getCurrentProfile();
				if (enableButtons && profile != null) {
					LoginManager.getInstance().logOut();
				}
				Intent intent=new Intent(this,HomeActivity.class);
				this.startActivity(intent);
				finish();
				break;
			case R.id.profileImageView:
				LoginUser loginUser=new LoginUser();
				loginUser=loginUser.DeSerialize(this);
				if(loginUser!=null) {
					if(loginUser.getType().equals("FACEBOOK")) {
						viewProfilePic(loginUser.getProfile_pic());
					}else
					viewProfilePic(smartcomplaintConfig.BASIC_URL+loginUser.getProfile_pic());
				}

				break;

		
		}
		
		
		
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem) {
		int id = menuItem.getItemId();
		return true;
	}
	
	/*
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		final SharedPreferences prefs = getSharedPreferences(LanuchActivity.class.getSimpleName(),DashboardActivity.this.getApplicationContext().MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.clear();
	    editor.commit();
	    Intent intent=new Intent(DashboardActivity.this,LanuchActivity.class);
		startActivity(intent);
		finish();
		return super.onMenuItemSelected(featureId, item);
		
		
	}*/


	private void addListener(View root) {
		if (root instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) root;
			for (int i = 0; i < parent.getChildCount(); i++) {
				addListener(parent.getChildAt(i));
			}
		} else {
			root.setClickable(true);
			root.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mExplosionField.explode(v, DashboardActivity.this);
					v.setOnClickListener(null);
				}
			});
		}
	}

	private void reset(View root) {
		if (root instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) root;
			for (int i = 0; i < parent.getChildCount(); i++) {
				reset(parent.getChildAt(i));
			}
		} else {
			root.setScaleX(1);
			root.setScaleY(1);
			root.setAlpha(1);
		}
	}

	@Override
	public void onExplodeEnded(View v) {

		switch(v.getId()) {
			case R.id.btTraffic:
				intent = new Intent(DashboardActivity.this, complaintActivity.class);
				intent.putExtra("key", "traffictable");
				startActivity(intent);

				break;
			case R.id.btPolice:
				intent = new Intent(DashboardActivity.this, complaintActivity.class);
				intent.putExtra("key", "policetable");
				startActivity(intent);

				break;
			case R.id.btMescom:
				intent = new Intent(DashboardActivity.this, complaintActivity.class);
				intent.putExtra("key", "mescomtable");
				startActivity(intent);

				break;
			case R.id.btStatus:
				intent = new Intent(DashboardActivity.this, AllStatusActivity.class);
				intent.putExtra("key", "mnpotable");
				startActivity(intent);

				break;
			case R.id.btMNPO:
				intent = new Intent(DashboardActivity.this, complaintActivity.class);
				intent.putExtra("key", "mnpotable");
				startActivity(intent);

				break;


		}
		reset(v);
		addListener(v);
		mExplosionField.clear();

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


	public void chooseExistingPhoto(){

		Intent getContentIntent =createGetContentIntent();
		intent = Intent.createChooser(getContentIntent, "Select a file");
		startActivityForResult(intent, REQUEST_CHOOSER);

	}

	 Dialog trace_dialog;
	public  void viewProfilePic(
			String profile_pic) {



		trace_dialog = new Dialog(this);
		trace_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		trace_dialog.getWindow().setBackgroundDrawableResource(
				R.drawable.trace_user_rounded_coner);
		trace_dialog.setCancelable(false);
		trace_dialog.setContentView(R.layout.user_profileview);
		ImageView imgClose=(ImageView)trace_dialog.findViewById(R.id.imgClose);
		Button btRemove=(Button)trace_dialog.findViewById(R.id.btRemove);
		Button btAdd=(Button)trace_dialog.findViewById(R.id.btAdd);
		imgClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trace_dialog.dismiss();
			}
		});

		btRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				RemoveProfilePic();
				trace_dialog.dismiss();
			}
		});

		btAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				addProfilrPic();

			}
		});


		NetworkImageView userProfilePic=(NetworkImageView)trace_dialog.findViewById(R.id.userProfilePic);
		userProfilePic.setDefaultImageResId(R.drawable.profile_icon);
		if(imageLoader==null)
			imageLoader = AppController.getInstance().getImageLoader();

		userProfilePic.setImageUrl(profile_pic + "?type=large", imageLoader);

		trace_dialog.show();
	}

	Dialog upload_dialog;
	public  void  showUploadOption() {
		// TODO Auto-generated method stub
		upload_dialog = new Dialog(this, R.style.DialogSlideAnim);
		upload_dialog = set_upload_attributes(upload_dialog);
		upload_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		upload_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		upload_dialog.setContentView(R.layout.upload_option);

		TextView tvCancel=(TextView)upload_dialog.findViewById(R.id.tvCancel);
		TextView tvTake_photo=(TextView)upload_dialog.findViewById(R.id.tvTake_photo);
		TextView tvChoose_existing=(TextView)upload_dialog.findViewById(R.id.tvChoose_existing);

		tvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				upload_dialog.dismiss();
			}
		});
		tvTake_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				upload_dialog.cancel();

				TakePhoto();
			}
		});
		tvChoose_existing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				upload_dialog.cancel();

				chooseExistingPhoto();
			}
		});



		upload_dialog.show();

	}

	private void TakePhoto() {

		intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent,camdata);





	}

	private void RemoveProfilePic() {

		ConnectWebService connectWebService=new ConnectWebService();
		connectWebService.setOnServerResponse(new ServerResponse() {
			@Override
			public void onServerResponse(String response) {
				if (response==null) {
					smartcomplaintUtility.showkDailog("some thing went wrong", DashboardActivity.this);
					return;
				}

				else if(response.matches("fail")){
					smartcomplaintUtility.showkDailog("retry", DashboardActivity.this);
					return;
				}else {
					LoginUser loginUser = new LoginUser();
					loginUser = loginUser.DeSerialize(DashboardActivity.this);
					loginUser.setProfile_pic("");
					loginUser.Serialize(DashboardActivity.this);

					if (trace_dialog != null)
						trace_dialog.dismiss();

					if (imageLoader == null)
						imageLoader = AppController.getInstance().getImageLoader();
					profileImageView.setDefaultImageResId(R.drawable.profile_icon);
					if (loginUser.getType().equals("FACEBOOK")) {
						profileImageView.setImageUrl(loginUser.getProfile_pic() + "?type=large", imageLoader);

					} else {
						profileImageView.setImageUrl(smartcomplaintConfig.BASIC_URL + loginUser.getProfile_pic(), imageLoader);

					}

					viewProfilePic(
							smartcomplaintConfig.BASIC_URL + loginUser.getProfile_pic());
				}

			}

			@Override
			public void onServerError(String result) {

			}

			@Override
			public void setLoading(boolean status) {

			}

			@Override
			public boolean getLoading() {
				return false;
			}
		});

		LoginUser loginUser=new LoginUser();
		loginUser=loginUser.DeSerialize(this);
		Map<String,String> hashMap=new HashMap<String,String>();
		hashMap.put("User",loginUser.getEmail());
		connectWebService.stringPostRequest(smartcomplaintConfig.REMOVE_PIC_URL,this,hashMap);


	}

	private void addProfilrPic() {
		showUploadOption();


	}

	public  Dialog set_upload_attributes(Dialog upload_dialog) {
		WindowManager.LayoutParams lp = upload_dialog.getWindow()
				.getAttributes();
		lp.gravity = Gravity.BOTTOM;
		lp.dimAmount = 0;
		upload_dialog.getWindow().setAttributes(lp);
		upload_dialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
		upload_dialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		upload_dialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		upload_dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		upload_dialog.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		upload_dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#00FFFFFF")));
		upload_dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
		return upload_dialog;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {
			case REQUEST_CHOOSER:
				if (resultCode == RESULT_OK) {

					if(	upload_dialog==null)
						showUploadOption();
					Uri result = intent == null || resultCode != RESULT_OK ? null
							: intent.getData();

					String path = complaintActivity.getPath(this.getApplicationContext(), result);
					File file = new File(path);
					String response = null;
					if(file!=null)
					 response=sendImageToServer(file);

					validateResponse(response);


				}
				break;
			case camdata:
				if(resultCode == RESULT_OK){


					Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
					if(cursor != null && cursor.moveToFirst())
					{ String picturePath;
						Uri selectedImage;
						do
						{
							picturePath =cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
							selectedImage = Uri.parse(picturePath);

						}
						while(cursor.moveToNext());
						cursor.close();
						File file = new File(picturePath);

						String response = null;
						if(file!=null)
							response=sendImageToServer(file);

						validateResponse(response);

					}

				}
				break;
		}
	}

	private void validateResponse(String response) {

		if (response==null) {
			smartcomplaintUtility.showkDailog("some thing went wrong", this);
			return;
		}

		else if(response.matches("fail")){
			smartcomplaintUtility.showkDailog("upload falied", this);
			return;
		}
		else {
			LoginUser loginUser=new LoginUser();
			loginUser=loginUser.DeSerialize(this);
			loginUser.setProfile_pic(response);
			loginUser.Serialize(this);

			if(trace_dialog!=null)
				trace_dialog.dismiss();

			if (imageLoader == null)
				imageLoader = AppController.getInstance().getImageLoader();
			profileImageView.setDefaultImageResId(R.drawable.profile_icon);
			if (loginUser.getType().equals("FACEBOOK")) {
				profileImageView.setImageUrl(loginUser.getProfile_pic() + "?type=large", imageLoader);

			} else {
				profileImageView.setImageUrl(smartcomplaintConfig.BASIC_URL + loginUser.getProfile_pic(), imageLoader);

			}

			viewProfilePic(
					smartcomplaintConfig.BASIC_URL+loginUser.getProfile_pic());
			return;
		}


	}


	public String sendImageToServer(File file) {

		LoginUser loginUser=new LoginUser();
		loginUser=loginUser.DeSerialize(this);

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
				URL url = new URL(smartcomplaintConfig.PROFILE_PIC_URL);
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
				dos.writeBytes("Content-Disposition: form-data; name=\"User\"" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(loginUser.getEmail());
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


			}
			if(response==null)
				return null;
			return response.toString();
		} // End else



	}






}
