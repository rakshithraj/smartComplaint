package com.example.smartcomplaint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.smartcomplaint.ViewPageSlider.TestFragmentAdapter;
import com.example.smartcomplaint.dao.LoginUser;
import com.example.smartcomplaint.utility.SmartComplaintConstant;
import com.example.smartcomplaint.utility.smartcomplaintConfig;
import com.example.smartcomplaint.utility.smartcomplaintUtility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements OnClickListener {

    Button btLogin, btRegister,btLogin_facebook;
    LoginButton faceBook;
    Intent intent;
    String activity = "";
    final static int LOGIN = 1234;
    final static int REGISTER = 5678;
    private CallbackManager callbackManager;
    ProgressDialog mProgressDialog;
    private ProfileTracker profileTracker;
    private VideoView myVideoView;
    private int position = 0;
    private MediaController mediaControls;
    TestFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    ArrayList<Integer> CONTENT;
    int postion=0;
    boolean isRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        super.onCreate(savedInstanceState);
        intializeFacebook();
        setContentView(R.layout.activity_main);
        Log.d("tag", "called HomeActivity");
        activity = this.getIntent().getStringExtra("calledBy");
        intialize();
        setOnclickListner();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();

            }
        };

        setUpVideo();


        setUpViewPager();






    }


    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        mHandler.postDelayed(runnable, 3000);
        AppEventsLogger.activateApp(this);

        updateUI();
    }




    @Override
    public void onPause() {
        super.onPause();

        mHandler.removeCallbacks(runnable);




        Log.d("tag", "***pausing***********************************");
        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }


    Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {

            while(isRun){
                try {
                    Log.d("tag","runing.........");
                    Thread.sleep(3000);
                    HomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(position==CONTENT.size())
                                position=0;
                            mPager.setCurrentItem(position);
                            position++;
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("tag","Thread ended.........");

        }
    });


    Handler mHandler=new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if(position==CONTENT.size())
                position=0;
            mPager.setCurrentItem(position);
            position++;


            mHandler.postDelayed(runnable,3000);
        }
    };




    private void setUpViewPager() {

         CONTENT=new   ArrayList<Integer>();
        CONTENT.add(R.string.string1);
        CONTENT.add(R.string.string2);
        CONTENT.add(R.string.string3);
        CONTENT.add(R.string.string4);


        final String[] Tile = new String[] {
                "string1","string2","string3","string4"
        };


        mAdapter = new TestFragmentAdapter(getSupportFragmentManager(),CONTENT,Tile);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        final CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setBackgroundColor(Color.TRANSPARENT);
        indicator.setRadius(4 * density);
        indicator.setPageColor(Color.TRANSPARENT);
        indicator.setFillColor(Color.WHITE);
        indicator.setStrokeColor(Color.WHITE);
        indicator.setStrokeWidth(2 * density);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }








    private void setUpVideo() {

        if (mediaControls == null) {

            mediaControls = new MediaController(this);

        }

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);
            mediaControls.setVisibility(View.GONE);

            //set the uri of the video to be played
            String path = "android.resource://" + getPackageName() + "/" + R.raw.test;
            myVideoView.setVideoURI(Uri.parse(path));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();
        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video


                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });

        myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                myVideoView.start();
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    //set onclick listner
    private void setOnclickListner() {
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);
        btLogin_facebook.setOnClickListener(this);
    }


    private void intialize() {
        btLogin = (Button) findViewById(R.id.btLogin);
        btRegister = (Button) findViewById(R.id.btRegister);
        btLogin_facebook = (Button) findViewById(R.id.btLogin_facebook);

        faceBook=(LoginButton)findViewById(R.id.faceBook);
        myVideoView=(VideoView)findViewById(R.id.videoView);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btLogin:
                intent = new Intent(HomeActivity.this, LoginActivity.class);
                this.startActivityForResult(intent, LOGIN);

                break;
            case R.id.btRegister:
                intent = new Intent(HomeActivity.this, registrationActivity.class);
                this.startActivityForResult(intent, REGISTER);
                break;
            case R.id.btLogin_facebook:
                boolean enableButtons = AccessToken.getCurrentAccessToken() != null;



                Profile profile = Profile.getCurrentProfile();
                if (enableButtons && profile != null) {

                    new Task(this,profile).execute();
                } else {
                    faceBook.performClick();
                }

                break;


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case LOGIN:
                if (resultCode == this.RESULT_OK) {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case REGISTER:
                if (resultCode == this.RESULT_OK) {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

            default: callbackManager.onActivityResult(requestCode, resultCode, data);


        }

    }

    private void intializeFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        updateUI();
                    }

                    @Override
                    public void onCancel() {

                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        updateUI();
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });
    }


    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;



        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {

            new Task(this,profile).execute();
        } else {

        }
    }




    public class Task extends AsyncTask<Context, Integer, String> {

        String registrationType;
        Context mContext;
        String response = null;
        String mConnectionData;
        Profile profile;
        Task(Context mContext, Profile profile) {
            this.mContext = mContext;
            this.profile = profile;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("tag", "onPreExecute=");
            mProgressDialog= smartcomplaintUtility.showProgressDialog(true, HomeActivity.this, mProgressDialog);
        }


        @Override
        protected String doInBackground(Context... arg0) {

            try {
                response=register(profile);
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
                    mProgressDialog=smartcomplaintUtility.showProgressDialog(false,HomeActivity.this,mProgressDialog);
                }
            GetResponseResult(response,profile);


        }




    }






        public void GetResponseResult(String response, Profile profile) {


            if (response == null) {
                smartcomplaintUtility.showkDailog("some thing went wrong", HomeActivity.this);
                return;
            }

            if (response.matches("")) {
                smartcomplaintUtility.showkDailog("some thing went wrong", HomeActivity.this);
            }else {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);


                    if (jsonArray != null) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                        Gson gson=new Gson();
                        LoginUser loginUser=gson.fromJson(jsonObject.toString(),LoginUser.class);
                        loginUser.Serialize(HomeActivity.this);
                        final SharedPreferences prefs = getSharedPreferences(LanuchActivity.class.getSimpleName(), HomeActivity.this.getApplicationContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("login", true);
                        editor.putString("userID", profile.getId());
                        editor.commit();

                        Intent intent = new Intent(this, DashboardActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        smartcomplaintUtility.showkDailog("some thing went wrong", HomeActivity.this);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

    public  String  register(Profile profile)  throws  UnsupportedEncodingException
    {
        // Get user defined values


        // Create data variable for sent values to server

        String data = URLEncoder.encode("Email", "UTF-8")
                + "=" + URLEncoder.encode(profile.getId(), "UTF-8");

        data += "&" + URLEncoder.encode("Password", "UTF-8") + "="
                + URLEncoder.encode("", "UTF-8");

        data += "&" + URLEncoder.encode("Name", "UTF-8")
                + "=" + URLEncoder.encode(profile.getName(), "UTF-8");

        data += "&" + URLEncoder.encode("Age", "UTF-8")
                + "=" + URLEncoder.encode("", "UTF-8");

        data += "&" + URLEncoder.encode("Contact", "UTF-8")
                + "=" + URLEncoder.encode("", "UTF-8");

        data += "&" + URLEncoder.encode("City", "UTF-8")
                + "=" + URLEncoder.encode("", "UTF-8");

        data += "&" + URLEncoder.encode("State", "UTF-8")
                + "=" + URLEncoder.encode("", "UTF-8");

            data += "&" + URLEncoder.encode("sex", "UTF-8")
                    + "=" + URLEncoder.encode("", "UTF-8");


        data += "&" + URLEncoder.encode("GcmId", "UTF-8")
                + "=" + URLEncoder.encode(SmartComplaintConstant.REGISTER_KEY, "UTF-8");

        data += "&" + URLEncoder.encode("Type", "UTF-8")
                + "=" + URLEncoder.encode("FACEBOOK", "UTF-8");

        data += "&" + URLEncoder.encode("profile_pic", "UTF-8")
                + "=" + URLEncoder.encode(profile.getProfilePictureUri(500,500).toString(), "UTF-8");

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
