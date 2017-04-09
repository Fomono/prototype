package com.fomono.fomono.network.client;

import android.content.Context;
import android.util.Log;

import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.eats.YelpResponse;
import com.fomono.fomono.models.eats.YelpTokenClass;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.*;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jsaluja on 4/5/2017.
 */

public class YelpClient {

    Context mContext;
    public YelpClient(Context context) {
        mContext = context;
    }

    public static final String USER_KEY = "d7LaIOfKYIeZw6r7MueI8A";
    public static final String USER_SECRET = "HPPdcnhNTswBnC4DiTLVbQNLsQ07m1y1dN767UC2pjSeSfBHObFlc5NWVjGZx9IT";
    public String YelpToken = "EHbNYMHOKBBlufnp61Eb2mO4gJ-Bmt4C8NWcGKyYDdVW5wTcEX5k_yUDyaTOTw7NvJhn-ws0OCcsEEXSQJixT4Wvf4JuYiF9qlpycTmsrBVk0URaftrXzKAKplvkWHYx";

    final static String TAG = "YelpClient";

    //Call this function before any other API call. To obtain the token
    public void getYelpToken() {
        String tokenUrl = "https://api.yelp.com/oauth2/token";
        RequestParams params = new RequestParams();
        params.put("grant_type", "client_credentials");
        params.put("client_id", USER_KEY);
        params.put("client_secret", USER_SECRET);
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(tokenUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                YelpTokenClass yelpTokenClass;
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                yelpTokenClass = gson.fromJson(response.toString(), YelpTokenClass.class);
                YelpToken = yelpTokenClass.getAccessToken();

                Log.d(TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });
    }

    public void getYelpBusinesses(String searchItem, String location, double lat, double lon, int radius,
                                  String sortBy/*best_match, rating, review_count, distance*/, String price,
                                  boolean openNow, String attributes) {
        String Url = "https://api.yelp.com/v3/businesses/search";
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer " + YelpToken);
        RequestParams params = new RequestParams();
        if (searchItem == null) {
            params.put("term", "restaurants");
        } else {
            params.put("term", searchItem);
        }
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
                ArrayList<Business> businesses = yelpResponse.getBusinesses();

                Log.d(TAG, "business is " + businesses.get(0).toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });
    }


    public void getYelpFoodDeliveryBusinesses(String location, double lat, double lon) {
        String Url = "https://api.yelp.com/v3/transactions/delivery/search";
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization","Bearer "+YelpToken);
        RequestParams params = new RequestParams();
        if(location == null) {
            if ((lat == -1) || (lon == -1)) {
                //FIXME - Use current location here
                params.put("location", "San Francisco");
            } else {
                params.put("latitude", lat);
                params.put("longitude", lon);
            }
        } else {
            params.put("location", location);
        }
        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                YelpResponse yelpResponse;
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                Log.d(TAG, "response is " + response);
                yelpResponse = gson.fromJson(response.toString(), YelpResponse.class);
                ArrayList<Business> businesses = yelpResponse.getBusinesses();

                Log.d(TAG, "business is " + businesses.get(0).toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });
    }


}

