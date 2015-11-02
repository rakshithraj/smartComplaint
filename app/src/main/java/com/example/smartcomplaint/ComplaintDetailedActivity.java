package com.example.smartcomplaint;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartcomplaint.dao.ComplaintInfo;
import com.example.smartcomplaint.utility.smartcomplaintConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rakshith on 9/15/2015.
 */
@SuppressWarnings("ALL")
public class ComplaintDetailedActivity extends AppCompatActivity {
    LinearLayout lySubject;
    public static final String EXTRA_NAME = "cheese_name";
    CollapsingToolbarLayout collapsingToolbar;
    ComplaintInfo complaintInfo=new ComplaintInfo();
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;

    TextView tvUsnNumber, tvGrade, tvToatal, tvSubName, tvCode, tvExtMarks, tvIntMarks, tvTotalMarks, tvResult;
    String usn_number;

    PublisherAdRequest adPublisherAdRequest;
    PublisherInterstitialAd interstitial;

    public final static int LOADED = 1, LOADING = 2, INTERNET_ERROR = 3, SERVER_ERROR = 4;
    static int CURRENT_STATE;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_screen);

        Bundle bundle=this.getIntent().getExtras();
        complaintInfo.setMessage(bundle.getString("message", null));
        complaintInfo.setMyLocation(bundle.getString("location", null));
        complaintInfo.setImage(bundle.getString("image", null));
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        intialize();

        this.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;



        //collapsingToolbar.setTitle(resultInfo.getStudentName());

        loadBackdrop();

    }





    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        if(!complaintInfo.getImage().trim().equals("")) {
            String imagePath=smartcomplaintConfig.IMAGE_STORAGE_PATH + complaintInfo.getImage();
            Glide.with(this).load(imagePath).centerCrop().into(imageView);
        }
        else
            Glide.with(this).load(R.drawable.no_image).centerCrop().into(imageView);

        final TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        final TextView tvLocation = (TextView) findViewById(R.id.tvLocation);

        tvMessage.setText(complaintInfo.getMessage());
        tvLocation.setText(complaintInfo.getMyLocation());
    }




    private void intialize() {



    }









}
