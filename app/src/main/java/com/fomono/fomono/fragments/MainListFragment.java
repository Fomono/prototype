package com.fomono.fomono.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fomono.fomono.R;
import com.fomono.fomono.activities.FomonoActivity;
import com.fomono.fomono.activities.FomonoDetailActivity;
import com.fomono.fomono.adapters.FomonoAdapter;
import com.fomono.fomono.databinding.FomonoMainListFragmentBinding;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.network.client.EventBriteClientRetrofit;
import com.fomono.fomono.network.client.MovieDBClientRetrofit;
import com.fomono.fomono.network.client.YelpClientRetrofit;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;
import com.fomono.fomono.supportclasses.ItemClickSupport;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

/**
 * Created by jsaluja on 4/7/2017.
 */

public abstract class MainListFragment extends Fragment {
    final static int NUM_GRID_COLUMNS = 1;
    private final static String TAG = "MAIN_LIST_FRAGMENT_LOG";
    public ArrayList<FomonoEvent> fomonoEvents;
    public FomonoAdapter fomonoAdapter;
    public RecyclerView rvList;
    public StaggeredGridLayoutManager gridLayoutManager;
    public EndlessRecyclerViewScrollListener scrollListener;
    Context mContext;
    public ProgressBar progressBar;
    public SmoothProgressBar smoothProgressBar;
    public EventBriteClientRetrofit eventBriteClientRetrofit;
    public YelpClientRetrofit yelpClientRetrofit;
    MovieDBClientRetrofit movieDBClientRetrofit;
    public TextView searchParamDispText;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fomonoEvents = new ArrayList<>();
        fomonoAdapter = new FomonoAdapter(getActivity(), fomonoEvents, getFragmentName());

        eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();
        yelpClientRetrofit = YelpClientRetrofit.getInstance();
        movieDBClientRetrofit = MovieDBClientRetrofit.getInstance();

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


        ItemClickSupport.addTo(rvList).setOnItemClickListener(
                (recyclerView, position, v) -> {
                    Intent showDetails = new Intent(mContext, FomonoDetailActivity.class);
                    showDetails.putExtra("position", position);
                    showDetails.putExtra("FOM_OBJ", fomonoEvents.get(position));
                    ((AppCompatActivity) mContext).startActivityForResult(showDetails, FomonoActivity.REQUEST_CODE_DETAILS);
                }
        );

        fomonoAdapter.setCustomObjectListener(i -> startActivity(i));

        return view;
    }

    public void setupRecycleAdapter() {
        rvList.setAdapter(fomonoAdapter);
        gridLayoutManager = new StaggeredGridLayoutManager(NUM_GRID_COLUMNS, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(gridLayoutManager);
    //    rvList.addItemDecoration(new RecyclerItemDecorator.SimpleDividerItemDecoration(getActivity()));
    }

    public void dataBindFragmentValues(FomonoMainListFragmentBinding binding) {
        rvList = binding.fomonoMainFragRvId;
        progressBar = binding.fomonoMainFragProgressId;
        smoothProgressBar = binding.fomonoMainFragSmoothBarId;
        searchParamDispText = binding.SearchParamDisplayTextId;
    }

    public void clear() {
        int size = fomonoEvents.size();
        fomonoEvents.clear();
        fomonoAdapter.notifyItemRangeRemoved(0, size);
    }

    public abstract String getFragmentName();

    public void updateFomonoEvent(FomonoEvent fEvent, int position) {
        if (position < 0) {
            for (int i = 0; i < fomonoEvents.size(); i++) {
                FomonoEvent curEvent = fomonoEvents.get(i);
                if (curEvent.getStringId().equals(fEvent.getStringId()) && curEvent.getApiName().equals(fEvent.getApiName())) {
                    position = i;
                    break;
                }
            }
        }
        if (position >= 0) {
            fomonoAdapter.notifyItemChanged(position);
        }
    }
}
