package com.fomono.fomono.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.fragments.EatsFragment;
import com.fomono.fomono.fragments.EventFragment;
import com.fomono.fomono.fragments.FavoritesFragment;
import com.fomono.fomono.fragments.MovieFragment;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
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
            eventFragment.refreshEventList(null);
        }
        if (eatsFragment != null) {
            eatsFragment.refreshEatsList(null);
        }
        if (movieFragment != null) {
            movieFragment.refreshMovieList(null);
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

    public void refreshFomonoEvent(FomonoEvent fEvent, int fEventPosition) {
        if (fEvent instanceof Event) {
            eventFragment.updateFomonoEvent(fEvent, fEventPosition);
        } else if (fEvent instanceof Business) {
            eatsFragment.updateFomonoEvent(fEvent, fEventPosition);
        } else if (fEvent instanceof Movie) {
            movieFragment.updateFomonoEvent(fEvent, fEventPosition);
        }
    }

    public String getApiName(int position) {
        switch (position) {
            case 0:
                return FomonoApplication.API_NAME_EVENTS;
            case 1:
                return FomonoApplication.API_NAME_EATS;
            case 2:
                return FomonoApplication.API_NAME_MOVIES;
        }
        return "";
    }

}