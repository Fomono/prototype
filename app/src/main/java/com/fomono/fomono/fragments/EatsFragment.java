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
import com.fomono.fomono.R;
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
    private String sortParameter = null;
    private String searchParameter = null;
    private int buttonSelected = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        searchParamDispText.setVisibility(View.GONE);

        if(internetAlertDialogue.checkForInternet()) {
            int offset = fomonoEvents.size();
            populateEats(offset, null, searchParameter);
        }

        rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                int offset = fomonoEvents.size();
                populateEats(offset, sortParameter, searchParameter);

            }
        });

        searchParamDispText.setOnClickListener(v -> {
            clear();
            searchParameter = null;
            populateEats(0, null, searchParameter);
            searchParamDispText.setVisibility(View.GONE);
        });
        return view;
    }

    public void searchEatsList(String query) {
        clear();
        searchParameter = query;
        populateEats(0, sortParameter, searchParameter);
    }

    public void refreshEatsList(String sortParam) {
        sortParameter = sortParam;
        clear();
        populateEats(0, sortParameter, searchParameter);
    }

    public void populateEats(int offset, String sortParameter, String searchQuery) {

        if(internetAlertDialogue.checkForInternet()) {
            if (searchQuery != null) {
                searchParamDispText.setVisibility(View.VISIBLE);
                searchParamDispText.setText("" + searchQuery + " X");
            } else {
                searchParamDispText.setVisibility(View.GONE);
            }

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
                    getYelpBusinesses(getActivity(), location, categoriesString, distanceInMeters, offset, sortParameter, searchQuery);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getYelpBusinesses(Context context, String location, String categories,
                                  int distance, int offset, String sortParam, String strQuery){

        Map<String, String> data = new HashMap<>();
        Log.d(TAG, "Location is " +location);
        if((location != null) && !(location.equals(""))) {
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

        if((sortParam != null) && !(sortParam.equals(""))) {
            if(sortParam.equals("rating_count")) {
                sortParam = "review_count";
            }
            data.put("sort_by", sortParam);
        }

        if(strQuery != null) {
            data.put("term", strQuery);
        }

        Call<YelpResponse> callEats = yelpClientRetrofit.YelpRetrofitClientFactory().getYelpBusinesssesFromServer(data);

        Log.d(TAG, "Eats URL String is " + callEats.request().url());

        callEats.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                try {
                    ArrayList<Business> businesses = response.body().getBusinesses();
                    if (businesses == null || businesses.isEmpty()) {
                        Log.d(TAG, "No Yelp businesses fetched!!");
                    } else {
                        Business.saveOrUpdateFromList(businesses);
                        fomonoEvents.addAll(businesses);
                        fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                smoothProgressBar.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );
                smoothProgressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }
}
