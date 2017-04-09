package com.fomono.fomono.network.client;

/**
 * Created by Saranu on 4/8/17.
 */

import android.content.Context;
import android.util.Log;

import com.fomono.fomono.models.events.events.Address;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.EventBriteResponse;
import com.fomono.fomono.models.events.events.Venue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Saranu on 3/16/17.
 */

public class EventBriteClientRetrofit {
    public static final String API_EB_BASE_URL = "https://www.eventbriteapi.com/";


    EventBriteClientRetrofit.EventBriteService EBservice;
    EventBriteClientRetrofit eventBriteClientRetrofit;
    final static String TAG = "EventBriteClient";
    Context mcontext;


    public EventBriteClientRetrofit.EventBriteService EBRetrofitClientFactory(){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_EB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        EBservice = retrofit.create(EventBriteClientRetrofit.EventBriteService.class);

        return EBservice;

    }

    public static EventBriteClientRetrofit getNewInstance() {
        return new EventBriteClientRetrofit();
    }

    public interface EventBriteService
    {
        @GET("/v3/events/search")
        Call<EventBriteResponse> getEventsFromServer(@QueryMap Map<String, String> options);
        //@Query("token") String api_key, @Query("q") String search_string

        @GET("/v3/venues/{venue_id}")
        Call<Venue> getVenueFromServer(@Path("venue_id") String venue_id, @QueryMap Map<String, String> options);
        //@Path("venue_id") String venue_id, @Query("token") String api_key

    }


    private String getApiUrl(String relativeUrl) {
        return API_EB_BASE_URL + relativeUrl;
    }



    public void callEBRetrofitGetVenueAPI(Context context,String venueId, String stringQuery){

        eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();
        Map<String, String> data = new HashMap<>();
        data.put("token", "API_KEY goes here");
        Call<Venue> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getVenueFromServer(venueId,data);

        call.enqueue(new Callback<Venue>() {
            @Override
            public void onResponse(Call<Venue> call, Response<Venue> response) {
                Address address = response.body().getAddress();
                if (address == null) {
                    Log.d(TAG, "MO MATCH ");
                } else {
                    //e.setVenue(response.body());
                }

            }

            @Override
            public void onFailure(Call<Venue> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }




    public void callEBRetrofitAPI(int page, String strQuery) {

        eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();

        Map<String, String> data = new HashMap<>();
        data.put("token", "IMWD66EDBK2PQIUKRK4K");
        data.put("q", "holi");

        Call<EventBriteResponse> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getEventsFromServer(data);

        call.enqueue(new Callback<EventBriteResponse>() {
            @Override
            public void onResponse(Call<EventBriteResponse> call, Response<EventBriteResponse> response) {
                ArrayList<Event> events = response.body().getEvents();
                if (events == null || events.isEmpty()) {
                    Log.d(TAG, "NO MATCH ");
                } else {

                }

            }

            @Override
            public void onFailure(Call<EventBriteResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }







}