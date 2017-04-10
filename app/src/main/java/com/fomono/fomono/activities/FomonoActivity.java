package com.fomono.fomono.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.fomono.fomono.R;
import com.fomono.fomono.adapters.FomonoMainPagerAdapter;

import retrofit2.http.HEAD;


public class FomonoActivity extends AppCompatActivity {
    private FomonoMainPagerAdapter fomonoMainPagerAdapter;
    private final static String TAG = "Fomono Activity";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fomono);

        Toolbar toolbar = (Toolbar) findViewById(R.id.fomonoToolbarId);
        setSupportActionBar(toolbar);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidthPixels = metrics.widthPixels;
        int screenWidth = (int) (screenWidthPixels / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        int screenHeightPixels = metrics.heightPixels;
        int screenHeight = (int) (screenHeightPixels / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));

        Log.d(TAG, "Screen width is "+screenWidth+" and screen height is "+screenHeight);
        fomonoMainPagerAdapter = new FomonoMainPagerAdapter(getSupportFragmentManager());
        fomonoMainPagerAdapter.setScreenWidth(screenWidth);
        fomonoMainPagerAdapter.setScreenHeight(screenHeight);

        ViewPager fomonoPager = (ViewPager)findViewById(R.id.fomonoViewpagerId);
        fomonoPager.setAdapter(fomonoMainPagerAdapter);
        PagerSlidingTabStrip fomonoTabStrip = (PagerSlidingTabStrip)findViewById(R.id.fomonoTabsId);
        fomonoTabStrip.setViewPager(fomonoPager);

    }

    @Override
    public void onBackPressed() {
        //FIXME - Disabling back button on Fomono Activity for now
    }
}
