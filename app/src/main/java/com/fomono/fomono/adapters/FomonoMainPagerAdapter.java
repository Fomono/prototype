package com.fomono.fomono.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fomono.fomono.R;
import com.fomono.fomono.fragments.EatsFragment;
import com.fomono.fomono.fragments.EventFragment;
import com.fomono.fomono.fragments.MainListFragment;
import com.fomono.fomono.fragments.MovieFragment;
import com.fomono.fomono.models.events.events.Event;

import static android.R.attr.width;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class FomonoMainPagerAdapter extends FragmentPagerAdapter {
    private int screenWidth, screenHeight;

    private String tabTitles[] = {"Events", "Eats", "Movies"};
    public void setScreenWidth(int width) {
        screenWidth = width;
    }
    public void setScreenHeight(int height) {
        screenHeight = height;
    }

    public FomonoMainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        MainListFragment mainListFragment = new MainListFragment();
        mainListFragment.setScreenWidthFromFragment(screenWidth);
        mainListFragment.setScreenHeightFromFragment(screenHeight);
        if (position == 0) {
            EventFragment eventFragment = new EventFragment();
            return eventFragment;
        } else if (position == 1) {
            EatsFragment eatsFragment = new EatsFragment();
            return eatsFragment;
        } else if (position == 2) {
            MovieFragment movieFragment = new MovieFragment();
            return movieFragment;
        } else {
            EventFragment eventFragment = new EventFragment();
            return eventFragment;
        }
    }
    //Return the tab title
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
    //Number of fragment
    @Override
    public int getCount() {
        return tabTitles.length;
    }

}