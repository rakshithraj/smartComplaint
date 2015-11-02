package com.example.smartcomplaint.SmartComplaintInterface;

/**
 * Created by Rakshith on 10/13/2015.
 */
public interface ServerResponse {

    public void onServerResponse(String result);
    public void onServerError(String result);
    public void setLoading( boolean status);
    public boolean getLoading();
}
