package com.fomono.fomono.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.eats.YelpResponse;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;
import com.fomono.fomono.utils.FilterUtil;
import com.fomono.fomono.utils.NumberUtil;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jsaluja on 4/8/2017.
 */

public class EatsFragment extends MainListFragment {
    private final static String TAG = "Eats fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if(internetAlertDialogue.checkForInternet()) {
            int offset = fomonoEvents.size();
            populateEats(offset, null);
        }

        rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(internetAlertDialogue.checkForInternet()) {
                    int offset = fomonoEvents.size();
                    populateEats(offset, null);
                }
            }
        });

        return view;
    }

    public void refreshEatsList(String sortParam) {
        String sortParameter = sortParam;
        clear();
        populateEats(0, sortParameter);
    }

    public void populateEats(int offset, String sortParameter) {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        try {
            //get user filters for yelp
            FilterUtil.getInstance().getFilters(FomonoApplication.API_NAME_EATS, (filters, e) -> {
                String categoriesString = "";
                if (filters != null) {
                    Filter.initializeFromList(filters);
                    categoriesString = FilterUtil.getInstance().buildCategoriesString(filters);
                }
                //default to our own set of filter categories
                if (TextUtils.isEmpty(categoriesString)) {
                    categoriesString = FilterUtil.getInstance().getDefaultFilterString(FomonoApplication.API_NAME_EATS, mContext);
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                String location = currentUser.getString("location");
                int distance = currentUser.getInt("distance");
                //gotta convert distance because yelp uses meters, and maxes out at 40,000 meters.
                int distanceInMeters = Math.min(40000, NumberUtil.convertToMeters(distance));
                getYelpBusinesses(getActivity(), location, categoriesString, distanceInMeters, offset, sortParameter);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getYelpBusinesses(Context context, String location, String categories, int distance, int offset, String sortParam){
        Map<String, String> data = new HashMap<>();
        if((location != null) && (location != "")) {
            data.put("location", location);
        } else {
            data.put("location", "San Francisco");
        }
        if (!TextUtils.isEmpty(categories)) {
            data.put("categories", categories);
        }
        if (distance > 0) {
            data.put("radius", String.valueOf(distance));
        }
        if(offset > 0) {
            data.put("offset", String.valueOf(offset));
        }

        if((sortParam != null) && (sortParam != "")) {
            data.put("sort_by", sortParam);
        }

        Call<YelpResponse> callEats = yelpClientRetrofit.YelpRetrofitClientFactory().getYelpBusinesssesFromServer(data);

        Log.d(TAG, "Eats URL String is " + callEats.request().url());

        callEats.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {

                ArrayList<Business> businesses = response.body().getBusinesses();
                if (businesses == null || businesses.isEmpty()) {
                    Log.d(TAG, "No Yelp businesses fetched!!");
                } else {
                    Business.saveOrUpdateFromList(businesses);
                    fomonoEvents.addAll(businesses);
                    fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                }
                smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );
                smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }
}
