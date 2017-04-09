package com.fomono.fomono.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fomono.fomono.fragments.EatsFragment;
import com.fomono.fomono.fragments.EventFragment;
import com.fomono.fomono.fragments.MainListFragment;
import com.fomono.fomono.fragments.MovieFragment;

import static android.R.attr.width;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class FomonoMainPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = {"Events", "Eats", "Movies"};
    private int screenWidth, screenHeight;

    public void setScreenWidth(int width) {
        screenWidth = width;
    }
    public void setScreenHeight(int height) {
        screenHeight = height;
    }
    //Adapter gets the manger insert or remove fragment from activity
    public FomonoMainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // The order and creation of fragments within the pager
    @Override
    public Fragment getItem(int position) {
        MainListFragment mainListFragment = new MainListFragment();
        mainListFragment.setScreenWidthFromFragment(screenWidth);
        mainListFragment.setScreenHeightFromFragment(screenHeight);
        if (position == 0) {
   //         EventFragment eventFragment = new EventFragment();
   //         return eventFragment;
   //         EatsFragment eatsFragment = new EatsFragment();
   //         return eatsFragment;
            MovieFragment movieFragment = new MovieFragment();
            return movieFragment;
        } else if (position == 1) {
   //         EatsFragment eatsFragment = new EatsFragment();
   //         return eatsFragment;
            MovieFragment movieFragment = new MovieFragment();
            return movieFragment;
        } else if (position == 2) {
            //         EatsFragment eatsFragment = new EatsFragment();
            //         return eatsFragment;
            MovieFragment movieFragment = new MovieFragment();
            return movieFragment;
        } else {
      //      EatsFragment eatsFragment = new EatsFragment();
      //      return eatsFragment;
      //      EventFragment eventFragment = new EventFragment();
      //      return eventFragment;
            MovieFragment movieFragment = new MovieFragment();
            return movieFragment;
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