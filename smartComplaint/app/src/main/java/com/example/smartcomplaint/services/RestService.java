package com.example.smartcomplaint.services;


import com.example.smartcomplaint.LenientGsonConverter;
import com.example.smartcomplaint.interfaces.ApiService;
import com.google.gson.Gson;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Class to call rest service
 */
public class RestService {

    public static final String BASE_URL = "http://rakshithraj.byethost5.com/smart_complaint";


    private RestService(){}

    public static ApiService getService(){
        RestAdapter restAdapter = null;

            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .setConverter(new LenientGsonConverter(new Gson()))
                    .build();



        return restAdapter.create(ApiService.class);
    }


}
