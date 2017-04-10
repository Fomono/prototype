package com.fomono.fomono.fragments;

import android.content.Context;
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
import com.fomono.fomono.network.client.YelpClientRetrofit;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;

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
        getYelpBusinesses(getActivity(), null);
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }
    public void getYelpBusinesses(Context context, String stringQuery){
        yelpClientRetrofit = YelpClientRetrofit.getNewInstance();

        Map<String, String> data = new HashMap<>();
        //FIXME - Should be current location
        if(stringQuery != null) {data.put("location", stringQuery);}
        else  {data.put("location", "San Francisco");}

        //FIXME:TODO: Incorrect string query. Write a method to generate a string
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
