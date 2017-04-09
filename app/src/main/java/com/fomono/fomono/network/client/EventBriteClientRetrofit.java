package com.fomono.fomono.network.client;

/**
 * Created by Saranu on 4/8/17.
 */

import android.content.Context;
import android.util.Log;

import com.fomono.fomono.R;
import com.fomono.fomono.models.events.events.EventBriteResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

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
        Call<EventBriteResponse> getEventsFromServer(String stringQuery);
        //@Query("token") String api_key, @Query("q") String search_string

        @GET("/v3/venues/{venue_id}")
        Call<com.fomono.fomono.models.events.events.Venue> getVenueFromServer(String stringQuery);
        //@Path("venue_id") String venue_id, @Query("token") String api_key

        @GET("/v3/venues/{venue_id}")
        Call<com.fomono.fomono.models.Events.Events.Venue> getYelpEatsFromServer(String stringQuery);
        //@Path("venue_id") String venue_id, @Query("token") String api_key
    }


    private String getApiUrl(String relativeUrl) {
        return API_EB_BASE_URL + relativeUrl;
    }



    public void callEBRetrofitGetVenueAPI(Context context,String venueId, String stringQuery){

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();
        stringQuery = "@Path(\"venue_id\")" + venueId + "\"" +"," + "\"" +
                context.getResources().getString(R.string.eventbrite_api_key) + "\"";
        //TODO: Incorrect string query. Write a method to generate a string
        Call<com.fomono.fomono.models.Events.Events.Venue> callVenue = EBRetrofitClientFactory().
                getVenueFromServer(stringQuery);

        callVenue.enqueue(new Callback<com.fomono.fomono.models.Events.Events.Venue>() {
            @Override
            public void onResponse(Call<com.fomono.fomono.models.Events.Events.Venue> call, Response<com.fomono.fomono.models.Events.Events.Venue> response) {
                com.fomono.fomono.models.Events.Events.Venue venue = response.body();
                if (venue == null) {
                    Log.d(TAG, "MO MATCH " );

                }
            }

            @Override
            public void onFailure(Call<com.fomono.fomono.models.Events.Events.Venue> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );

            }
        });
    }




    public void callEBRetrofitAPI(Context context,int page, String strQuery) {


       // eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();
        Call<EventBriteResponse> call = EBRetrofitClientFactory().
                getEventsFromServer("4SBUTVBMQH4BH5ZQHJAE"+ "holi");

        call.enqueue(new Callback<EventBriteResponse>() {
            @Override
            public void onResponse(Call<EventBriteResponse> call, Response<EventBriteResponse> response) {
                ArrayList<Event> events = response.body().getEvents();
                if (events == null || events.isEmpty()) {
                    Log.d(TAG, "MO MATCH " );
                }else {
                }

            }

            @Override
            public void onFailure(Call<EventBriteResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );

            }
        });

    }






}