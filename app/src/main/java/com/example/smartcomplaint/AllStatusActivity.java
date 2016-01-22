package com.example.smartcomplaint;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.smartcomplaint.dao.ComplaintInfo;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rakshith on 10/13/2015.
 */
public class AllStatusActivity extends AppCompatActivity {
    public final static int MNPO=0,MESCOM=1,TRAFFIC=2,POLICE=3;
    private static final String[] CONTENT = new String[] { "MNPO", "Mescom", "Traffic", "Police"};
    ArrayList<ComplaintInfo> myDataset=new ArrayList<ComplaintInfo>();

    HashMap<String, ArrayList<ComplaintInfo>> myHashMapDataset =new  HashMap<String, ArrayList<ComplaintInfo>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabLayout indicator = (TabLayout)findViewById(R.id.indicator);
        indicator.setupWithViewPager(pager);

        indicator.setTabTextColors(Color.WHITE,Color.WHITE);

        Drawable drawable;
        if(Build.VERSION.SDK_INT<21)
           drawable= this.getResources().getDrawable(R.drawable.editbg);
        else
            drawable= this.getDrawable(R.drawable.editbg);

       /* if(Build.VERSION.SDK_INT>=16)
            indicator.setBackground(drawable);

        else
        indicator.setBackgroundDrawable(drawable);*/

    }



    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            return AllStatusFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
}
