package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fomono.fomono.network.client.YelpClientRetrofit;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;

import static android.R.attr.offset;

/**
 * Created by jsaluja on 4/14/2017.
 */

public class FavoritesFragment extends MainListFragment {
    private final static String TAG = "Favorites fragment";
    private YelpClientRetrofit yelpClientRetrofit;
    private boolean initialFavoritesLoaded = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if (internetAlertDialogue.checkForInternet() && !initialFavoritesLoaded) {
            populateFavorites();
        }

        return view;
    }

    public void populateFavorites() {

    }
}

