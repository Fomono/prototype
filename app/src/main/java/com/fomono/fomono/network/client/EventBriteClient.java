package com.fomono.fomono.network.client;

import android.content.Context;
import android.util.Log;

import com.fomono.fomono.models.Events.Categories.Category;
import com.fomono.fomono.models.Events.Categories.EventBriteCategories;
import com.fomono.fomono.models.Events.Events.Event;
import com.fomono.fomono.models.Events.Events.EventBriteResponse;
import com.fomono.fomono.models.Events.UserCredentials.EventBriteUser;
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
 * Created by jsaluja on 4/6/2017.
 */

public class EventBriteClient {


    Context mContext;
    public EventBriteClient(Context context) {
        mContext = context;
    }

    public static final String USER_KEY = "IMWD66EDBK2PQIUKRK4K";
    final static String TAG = "EventBriteClient";

    public void getEventBriteEventList(String query, String sortBy, String locationAddress, String locationRadius, String locationLat,
                                       String locationLon, String categories,String subCategories, String price, String startDateRangeStart,
                                       String startDateRangeEnd, String startDateKeyword, String dateModifiedRangeStart, String dateModifiedRangeEnd,
                                       String dateModifiedDateKeyword) {
        String Url = "https://www.eventbriteapi.com/v3/events";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", USER_KEY);

        if(query != null) {params.put("q", query);}
        if(sortBy != null) {params.put("sort_by", sortBy);}
        if(locationAddress != null) {params.put("location.address", sortBy);}
        if(locationRadius != null) {params.put("location.within", locationRadius);}
        if(locationLat != null) {params.put("location.latitude", locationLat);}
        if(locationLon != null) {params.put("location.longitude", locationLon);}
        if(categories != null) {params.put("categories", categories);}
        if(subCategories != null) {params.put("subcategories", subCategories);}
        if(price != null) {params.put("price", price);}
        if(startDateRangeStart != null) {params.put("start_date.range_start", startDateRangeStart);}
        if(startDateRangeEnd != null) {params.put("start_date.range_end", startDateRangeEnd);}
        if(startDateKeyword != null) {params.put("start_date.keyword", startDateKeyword);}
        if(dateModifiedRangeStart != null) {params.put("date_modified.range_start", startDateRangeStart);}
        if(dateModifiedRangeEnd != null) {params.put("date_modified.range_end", startDateRangeEnd);}
        if(dateModifiedDateKeyword != null) {params.put("date_modified.keyword", startDateKeyword);}

        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                EventBriteResponse eventBriteResponse;
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                Log.d(TAG, "response is " + response);
                eventBriteResponse = gson.fromJson(response.toString(), EventBriteResponse.class);
                ArrayList<Event> events = eventBriteResponse.getEvents();

                Log.d(TAG, "event name is " + events.get(0).getName().getText().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });

    }

    public void getEventCategories() {
        String Url = "https://www.eventbriteapi.com/v3/categories";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", USER_KEY);

        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                EventBriteCategories eventBriteCategories;
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                Log.d(TAG, "response is " + response);
                eventBriteCategories = gson.fromJson(response.toString(), EventBriteCategories.class);
                ArrayList<Category> categories = eventBriteCategories.getCategories();

                Log.d(TAG, "categories name is " + categories.get(0).getName().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });
    }

    public void getUserCredentials() {

        String Url = "https://www.eventbriteapi.com/v3/users/me";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", USER_KEY);

        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                EventBriteUser eventBriteUser;
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                Log.d(TAG, "response is " + response);
                eventBriteUser = gson.fromJson(response.toString(), EventBriteUser.class);

                Log.d(TAG, "user name is " + eventBriteUser.getName());
                Log.d(TAG, "user name is " + eventBriteUser.getName());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });
    }
}
