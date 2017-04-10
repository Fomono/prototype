package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.eats.YelpResponse;
import com.fomono.fomono.network.client.YelpClient;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jsaluja on 4/8/2017.
 */

public class EatsFragment extends MainListFragment {
    public static final String USER_KEY = "IMWD66EDBK2PQIUKRK4K";
    private final static String TAG = "Eats fragment";
    public String YelpToken = "EHbNYMHOKBBlufnp61Eb2mO4gJ-Bmt4C8NWcGKyYDdVW5wTcEX5k_yUDyaTOTw7NvJhn-ws0OCcsEEXSQJixT4Wvf4JuYiF9qlpycTmsrBVk0URaftrXzKAKplvkWHYx";

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
        getLocalYelpBusinesses(null, null, -1, -1, -1, null, null, false, null);
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }

    public void getLocalYelpBusinesses(String searchItem, String location, double lat, double lon, int radius,
                                  String sortBy/*best_match, rating, review_count, distance*/, String price,
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


}
