package com.example.smartcomplaint.interfaces;



import com.example.smartcomplaint.models.ImgUploadResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * API service for retro fit
 */
public interface ApiService {






    @FormUrlEncoded
    @POST("/login.php")
    public String getFeed(
            @Field("Email") String Email,
            @Field("Password") String Password);


}
