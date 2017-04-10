package com.fomono.fomono.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.fomono.fomono.network.client.YelpClientRetrofit;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;
import com.fomono.fomono.utils.FilterUtil;
import com.fomono.fomono.utils.NumberUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jsaluja on 4/8/2017.
 */

public class EatsFragment extends MainListFragment {
    private final static String TAG = "Eats fragment";
    private YelpClientRetrofit yelpClientRetrofit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if(internetAlertDialogue.checkForInternet()) {
            populateEats();
        }
        return view;
    }

    public void populateEats() {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        try {
            //get user filters for yelp
            FilterUtil.getFilters(FomonoApplication.API_NAME_EATS, new FindCallback<Filter>() {
                @Override
                public void done(List<Filter> filters, ParseException e) {
                    Filter.initializeFromList(filters);
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String location = currentUser.getString("location");
                    int distance = currentUser.getInt("distance");
                    //gotta convert distance because yelp uses meters, and maxes out at 40,000 meters.
                    int distanceInMeters = Math.min(40000, NumberUtil.convertToMeters(distance));
                    String categoriesString = FilterUtil.buildCategoriesString(filters);
                    getYelpBusinesses(getActivity(), location, categoriesString, distanceInMeters);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }
    public void getYelpBusinesses(Context context, String location, String categories, int distance){
        yelpClientRetrofit = YelpClientRetrofit.getNewInstance();

        Map<String, String> data = new HashMap<>();
        if(location != null) {
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

        Call<YelpResponse> callVenue = yelpClientRetrofit.YelpRetrofitClientFactory().getYelpBusinesssesFromServer(data);

        callVenue.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {

                ArrayList<Business> businesses = response.body().getBusinesses();
                if (businesses == null || businesses.isEmpty()) {
                    Log.d(TAG, "No Yelp businesses fetched!!");
                } else {
                    fomonoEvents.addAll(businesses);
                    fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                }
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );

            }
        });
    }




/*
    public void getLocalYelpBusinesses(String searchItem, String location, double lat, double lon, int radius,
                                  String sortBy, String price,
                                  boolean openNow, String attributes) {
        String Url = "https://api.yelp.com/v3/businesses/search";
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + YelpToken);
        RequestParams params = new RequestParams();
        if (searchItem == null) {params.put("term", "restaurants");}
        else {params.put("term", searchItem);}

        if (location == null) {
            if ((lat == -1) || (lon == -1)) {
                //FIXME - Use current location here
                params.put("location", "San Francisco");
            } else {
                params.put("latitude", lat);
                params.put("longitude", lon);
            }
        } else {params.put("location", location);}

        if (radius != -1) {params.put("radius", radius);}
        if (sortBy != null) {params.put("sort_by", sortBy);}
        if (price != null) {params.put("price", price);}
        params.put("open_now", openNow);
        if (attributes != null) {params.put("attributes", attributes);}

        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                YelpResponse yelpResponse;
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                Log.d(TAG, "response is " + response);
                yelpResponse = gson.fromJson(response.toString(), YelpResponse.class);
                fomonoEvents.addAll(yelpResponse.getBusinesses());
                fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });
    }
*/

}
