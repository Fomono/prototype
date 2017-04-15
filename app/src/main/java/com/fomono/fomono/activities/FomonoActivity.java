package com.fomono.fomono.activities;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.fomono.fomono.R;
import com.fomono.fomono.adapters.FomonoMainPagerAdapter;
import com.fomono.fomono.databinding.ActivityFomonoBinding;
import com.fomono.fomono.supportclasses.NavigationDrawerClass;



public class FomonoActivity extends AppCompatActivity {
    private FomonoMainPagerAdapter fomonoMainPagerAdapter;
    private final static String TAG = "Fomono Activity";
    private NavigationView nvView;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private ViewPager fomonoPager;
    private PagerSlidingTabStrip fomonoTabStrip;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFomonoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fomono);
        toolbar = binding.fomonoToolbarId;
        nvView = binding.fomonoNavViewId;
        mDrawer = binding.fomonoDrawerLayoutId;


        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        NavigationDrawerClass navigationDrawerClass = new NavigationDrawerClass(FomonoActivity.this, mDrawer);
        navigationDrawerClass.setupDrawerContent(nvView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        //Calculate screen width/height
        fomonoMainPagerAdapter = new FomonoMainPagerAdapter(getSupportFragmentManager());

        fomonoPager = (ViewPager)findViewById(R.id.fomonoViewpagerId);
        fomonoPager.setAdapter(fomonoMainPagerAdapter);
        //Tells the view pager to not destroy the fragment more than one tab away
        fomonoPager.setOffscreenPageLimit(getResources().getInteger(R.integer.NUM_MAINLIST_FRAGMENTS) - 1);
        fomonoTabStrip = (PagerSlidingTabStrip)findViewById(R.id.fomonoTabsId);
        fomonoTabStrip.setViewPager(fomonoPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, (R.string.open_drawer), R.string.close_drawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

}
