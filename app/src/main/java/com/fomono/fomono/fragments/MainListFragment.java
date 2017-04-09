package com.fomono.fomono.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;

import com.fomono.fomono.R;
import com.fomono.fomono.models.movies.Result;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;
import com.fomono.fomono.supportclasses.RecyclerItemDecorator;
import com.fomono.fomono.adapters.FomonoAdapter;
import com.fomono.fomono.databinding.FomonoMainListFragmentBinding;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class MainListFragment extends Fragment {
    final static int NUM_GRID_COLUMNS = 1;
    private final static String TAG = "MAIN_LIST_FRAGMENT_LOG";
    public ArrayList<Business> yelpBusinesses;
    public ArrayList<Event> eventBriteEvents;
    public ArrayList<Result> movieResults;
    public FomonoAdapter fomonoAdapter;
    public RecyclerView rvList;
    public StaggeredGridLayoutManager gridLayoutManager;
    public EndlessRecyclerViewScrollListener scrollListener;
    Context mContext;
    public ProgressBar progressBar;
    public SmoothProgressBar smoothProgressBar;
    public static int screenWidth, screenHeight;

    public void setScreenWidthFromFragment(int width) {screenWidth = width;}
    public void setScreenHeightFromFragment(int height) {screenHeight = height;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        yelpBusinesses = new ArrayList<>();
        eventBriteEvents = new ArrayList<>();
        movieResults = new ArrayList<>();
        fomonoAdapter = new FomonoAdapter(getActivity(), eventBriteEvents, yelpBusinesses, movieResults, screenWidth, screenHeight);

        FomonoMainListFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.fomono_main_list_fragment, container, false);
        View view = binding.getRoot();
        dataBindFragmentValues(binding);

        mContext = view.getContext();
        //Implementing the smooth progress bar. The loading is so fast that the bar never shows up
        progressBar.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(getActivity()).interpolator(new AccelerateInterpolator()).build());
        smoothProgressBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) smoothProgressBar.getIndeterminateDrawable()).getStrokeWidth()));

        progressBar.setVisibility(ProgressBar.INVISIBLE);
        setupRecycleAdapter();
        return view;
    }

    public void setupRecycleAdapter() {
        rvList.setAdapter(fomonoAdapter);
        gridLayoutManager = new StaggeredGridLayoutManager(NUM_GRID_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(gridLayoutManager);
        rvList.addItemDecoration(new RecyclerItemDecorator.SimpleDividerItemDecoration(getActivity()));
    }

    public void dataBindFragmentValues(FomonoMainListFragmentBinding binding) {
        rvList = binding.fomonoMainFragRvId;
        progressBar = binding.fomonoMainFragProgressId;
        smoothProgressBar = binding.fomonoMainFragSmoothBarId;
    }

    public void alert_user(String title, String message) {
        AlertDialog.Builder dialog;
        dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setNegativeButton("Ok",
                (dialog1, which) -> dialog1.cancel());
        AlertDialog alertD = dialog.create();
        alertD.show();
    }
}
