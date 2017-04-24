package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.db.Favorite;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;
import com.fomono.fomono.utils.FavoritesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsaluja on 4/14/2017.
 */

public class FavoritesFragment extends MainListFragment implements FavoritesUtil.FavoritesListener {
    public final static String TAG = "Favorites fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if (internetAlertDialogue.checkForInternet()) {
            populateFavorites();
        }

        return view;
    }

    public void populateFavorites() {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);

        FavoritesUtil.getInstance().getFavoritesWhenLoaded(this);
    }

    @Override
    public void onFavoritesLoaded(List<Favorite> favorites) {
        smoothProgressBar.setVisibility(ProgressBar.GONE);

        clear();
        List<FomonoEvent> favoriteEvents = new ArrayList<>();
        for (Favorite f : favorites) {
            favoriteEvents.add(f.getFomonoEvent());
        }
        fomonoEvents.addAll(favoriteEvents);
        fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        FavoritesUtil.getInstance().removeListener(this);
    }

    @Override
    public void clearSearch() {
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }
}

