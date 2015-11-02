/*
package com.example.smartcomplaint.SmartCompAsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.smartcomplaint.SmartComplaintInterface.ServerResponse;
import com.example.smartcomplaint.utility.smartcomplaintUtility;
import com.example.smartcomplaint.webservice.ConnectSmartComplaintService;

*/
/**
 * Created by Rakshith on 10/13/2015.
 *//*

public class ConnectServerAsynTask extends AsyncTask<Context, Integer, String> {

    String registrationType;
    Context mContext;
    String response = null;
    String mConnectionData;
    Activity activity;

    ServerResponse mServerResponse;
    String url;
    public ConnectServerAsynTask(Activity activity,String url) {
        this.mContext = mContext;
        this.activity = activity;
        this.url = url;
    }


   public void setOnServerResponse( ServerResponse mServerResponse){
       this.mServerResponse = mServerResponse;
   }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("tag", "onPreExecute=");


    }

    @Override
    protected String doInBackground(Context... arg0) {
        try{
            ConnectSmartComplaintService httpReq=new ConnectSmartComplaintService();
           // response=httpReq.executeget(smartcomplaintConfig.STATUS_URL+"id="+ myid[1]+"&tablename="+myid[0]+"table", null, mContext);
            response=httpReq.executeget(url, null, activity);
        }catch(Exception e){

        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);



            mServerResponse.onServerResponse(response);
        Log.d("tag", "onPostExecute=");
        Log.d("tag", "response="+response);



    }


}*/
