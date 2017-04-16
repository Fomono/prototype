package com.fomono.fomono.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fomono.fomono.fragments.EatsFragment;
import com.fomono.fomono.fragments.EventFragment;
import com.fomono.fomono.fragments.FavoritesFragment;
import com.fomono.fomono.fragments.MainListFragment;
import com.fomono.fomono.fragments.MovieFragment;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class FomonoMainPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = {"Events", "Eats", "Movies", "Favorites"};

    public FomonoMainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
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
            FavoritesFragment frag = new FavoritesFragment();
            return frag;
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