package com.fomono.fomono.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fomono.fomono.fragments.EatsFragment;
import com.fomono.fomono.fragments.EventFragment;
import com.fomono.fomono.fragments.FavoritesFragment;
import com.fomono.fomono.fragments.MovieFragment;
import com.fomono.fomono.utils.FilterUtil;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class FomonoMainPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = {"Events", "Eats", "Movies", "Favorites"};

    private EventFragment eventFragment;
    private EatsFragment eatsFragment;
    private MovieFragment movieFragment;
    private FavoritesFragment favoritesFragment;

    public FomonoMainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (eventFragment == null) {
                eventFragment = new EventFragment();
            }
            return eventFragment;
        } else if (position == 1) {
            if (eatsFragment == null) {
                eatsFragment = new EatsFragment();
            }
            return eatsFragment;
        } else if (position == 2) {
            if (movieFragment == null) {
                movieFragment = new MovieFragment();
            }
            return movieFragment;
        } else {
            if (favoritesFragment == null) {
                favoritesFragment = new FavoritesFragment();
            }
            return favoritesFragment;
        }
    }

    public void refreshFragments() {
        if (eventFragment != null) {
            eventFragment.refresh();
        }
        if (eatsFragment != null) {
            eatsFragment.refresh();
        }
        if (movieFragment != null) {
            movieFragment.refresh();
        }
        FilterUtil.getInstance().clearDirty();
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