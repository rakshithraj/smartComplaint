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


import com.example.smartcomplaint.interfaces.onLoginCompleted;
import com.example.smartcomplaint.models.LoginDetails;
import com.example.smartcomplaint.utility.smartcomplaintConstant;
import com.example.smartcomplaint.utility.smartcomplaintUtility;
import com.example.smartcomplaint.webservice.ConnectSmartComplaintService;

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

public class loginActivity extends Activity implements OnClickListener, onLoginCompleted {


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    Button btSignIn;
    EditText etLoginEmail, etLoginPassword;
    String LoginEmail, LoginPassword;
    ProgressDialog mProgressDialog;
    boolean loginSucess = false;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        context = this.getApplicationContext();
        intialize();

    }


    private void intialize() {
        // TODO Auto-generated method stub
        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        btSignIn = (Button) findViewById(R.id.btSignIn);
        btSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v.getId() == R.id.btSignIn) {
            getdata();
            if (validateData()) {
                if (smartcomplaintUtility.isNetworkAvailable(this)) {
                    Task task = new Task(this.getApplicationContext());
                    task.execute();


                } else {
                    smartcomplaintUtility.showkDailog("connect to internet", this);
                }

            }

        }


    }


    private boolean validateData() {
        // TODO Auto-generated method stub
        if (LoginEmail.matches("")) {
            smartcomplaintUtility.showkDailog("Enter Email", this);
            return false;
        } else if (!checkEmailCorrect(LoginEmail)) {
            smartcomplaintUtility.showkDailog("Enter valid Email", this);
            return false;
        }

        if (LoginPassword.matches("")) {
            smartcomplaintUtility.showkDailog("Enter Password", this);
            return false;
        } else if (LoginPassword.length() < 6) {
            smartcomplaintUtility.showkDailog("Password to short", this);
            return false;
        }


        return true;
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

    private void getdata() {
        LoginEmail = etLoginEmail.getText().toString();
        LoginPassword = etLoginPassword.getText().toString();


    }

    @Override
    public void onLoginCompleted(Object result) {
        if (mProgressDialog != null)
            if (mProgressDialog.isShowing()) {
                mProgressDialog = smartcomplaintUtility.showProgressDialog(false, loginActivity.this, mProgressDialog);
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
            mProgressDialog = smartcomplaintUtility.showProgressDialog(true, loginActivity.this, mProgressDialog);

        }

        @Override
        protected String doInBackground(Context... arg0) {
            String upLoadServerUri = smartcomplaintConstant.LOGIN_URL;
            try {
                response = login();
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Log.d("tag", "onPostExecute=");
            Log.d("tag", "response=" + response);
            if (mProgressDialog != null)
                if (mProgressDialog.isShowing()) {
                    mProgressDialog = smartcomplaintUtility.showProgressDialog(false, loginActivity.this, mProgressDialog);
                }
            GetResponseResult(response);
        }


        private void GetResponseResult(String response2) {
            response = response.trim();
            if (response.matches("") || response == null) {
                smartcomplaintUtility.showkDailog("some thing went wrong", loginActivity.this);
            } else if (response.matches("valid user")) {
                loginSucess = true;
                final SharedPreferences prefs = getSharedPreferences(lanuchActivity.class.getSimpleName(), loginActivity.this.getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("login", true);
                editor.putString("userID", LoginEmail);
                editor.commit();
                new updateGcm().execute();
                Intent intent = new Intent(loginActivity.this, dashboardActivity.class);
                startActivity(intent);

                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);

                finish();

            } else {
                smartcomplaintUtility.showkDailog("username or password wrong", loginActivity.this);

            }


        }


    }


    class updateGcm extends AsyncTask<String, String, String> {
        String response = null;
        String mConnectionData;

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            Log.d("tag", "response=" + response);


        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub


            try {
                Log.d("tag", "update");
                ConnectSmartComplaintService httpReq = new ConnectSmartComplaintService();
                response = httpReq.executeget(smartcomplaintConstant.GCM_UPDATE_URL + "newGCM=" + smartcomplaintConstant.REGISTER_KEY + "&userId=" + LoginEmail, null, context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("tag", "error=" + e.toString());
            }


            return "";
        }


    }


    public String login() throws UnsupportedEncodingException {
        // TODO Auto-generated method stub

        String data = URLEncoder.encode("Email", "UTF-8")
                + "=" + URLEncoder.encode(LoginEmail, "UTF-8");

        data += "&" + URLEncoder.encode("Password", "UTF-8") + "="
                + URLEncoder.encode(LoginPassword, "UTF-8");


        String text = "";
        BufferedReader reader = null;

        // Send data 
        try {

            // Defined URL  where to send data
            URL url = new URL(smartcomplaintConstant.LOGIN_URL);

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
        } catch (Exception ex) {

        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

        return text;

    }


}
