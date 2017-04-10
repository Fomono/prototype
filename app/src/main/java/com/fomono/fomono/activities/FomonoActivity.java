package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.fomono.fomono.R;
import com.fomono.fomono.adapters.FomonoMainPagerAdapter;


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fomono, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_filters:
                Intent i = new Intent(this, FomonoFilterActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }
}
