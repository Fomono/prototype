package com.fomono.fomono.network.client;

/**
 * Created by Saranu on 4/8/17.
 */

import android.content.Context;
import android.util.Log;

import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.eats.YelpResponse;
import com.fomono.fomono.models.eats.YelpTokenClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Saranu on 3/16/17.
 */

public class YelpClientRetrofit {
    public static final String API_YELP_BASE_URL = "https://api.yelp.com/";
    public static final String USER_KEY = "d7LaIOfKYIeZw6r7MueI8A";
    public static final String USER_SECRET = "HPPdcnhNTswBnC4DiTLVbQNLsQ07m1y1dN767UC2pjSeSfBHObFlc5NWVjGZx9IT";
    public static final String YelpToken = "Bearer " + "EHbNYMHOKBBlufnp61Eb2mO4gJ-Bmt4C8NWcGKyYDdVW5wTcEX5k_yUDyaTOTw7NvJhn-ws0OCcsEEXSQJixT4Wvf4JuYiF9qlpycTmsrBVk0URaftrXzKAKplvkWHYx";


    YelpClientRetrofit.YelpService Yservice;
    EventBriteClientRetrofit eventBriteClientRetrofit;
    final static String TAG = "EventBriteClient";
    Context mcontext;
    ArrayList<Business> businesses;



    public YelpClientRetrofit.YelpService YelpRetrofitClientFactory(){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_YELP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Yservice = retrofit.create(YelpClientRetrofit.YelpService.class);

        return Yservice;

    }

    public static YelpClientRetrofit getNewInstance() {
        return new YelpClientRetrofit();
    }

    public interface YelpService
    {
        @GET("/oauth2/token")
        Call<YelpTokenClass> getYelpTokenFromServer(@Query("grant_type") String client_credentials,
                                                    @Query("client_id")String userKey,
                                                    @Query("client_secret") String stringQuery);
        //@Query("token") String api_key, @Query("q") String search_string

        @Headers({
                "Authorization: Bearer EHbNYMHOKBBlufnp61Eb2mO4gJ-Bmt4C8NWcGKyYDdVW5wTcEX5k_yUDyaTOTw7NvJhn-ws0OCcsEEXSQJixT4Wvf4JuYiF9qlpycTmsrBVk0URaftrXzKAKplvkWHYx",
        })
        @GET("/v3/businesses/search")
        Call<YelpResponse> getYelpBusinesssesFromServer(@Query("location") String resttautantString
                                                  );


        @Headers({
                "Authorization: Bearer EHbNYMHOKBBlufnp61Eb2mO4gJ-Bmt4C8NWcGKyYDdVW5wTcEX5k_yUDyaTOTw7NvJhn-ws0OCcsEEXSQJixT4Wvf4JuYiF9qlpycTmsrBVk0URaftrXzKAKplvkWHYx",
        })
        @GET("/v3/transactions/delivery/search")
        Call<YelpResponse> getYelpDeliveryFromServer(@Query("location") String resttautantString);


    }


    private String getApiUrl(String relativeUrl) {
        return API_YELP_BASE_URL + relativeUrl;
    }


    public void callRetrofitGetYelpToken(Context context,String stringQuery){

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer("client_credentials",
                USER_KEY,USER_SECRET);


        callVenue.enqueue(new Callback<YelpTokenClass>() {
            @Override
            public void onResponse(Call<YelpTokenClass> call, Response<YelpTokenClass> response) {
                YelpTokenClass yelpTokenClass = response.body();


                if (yelpTokenClass == null) {
                    Log.d(TAG, "NO MATCH " );

                }else{
                    String yelpToken = yelpTokenClass.getAccessToken();
                }
            }

            @Override
            public void onFailure(Call<YelpTokenClass> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );

            }
        });
    }


    public void callRetrofitGetYelpBusinesses(Context context,String stringQuery){

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<YelpResponse> callVenue = YelpRetrofitClientFactory().getYelpBusinesssesFromServer("San Francisco");


        callVenue.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                YelpResponse yelpResponse = response.body();

                if (yelpResponse == null) {
                    Log.d(TAG, "NO MATCH " );

                }else{
                     businesses = yelpResponse.getBusinesses();

                }
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );

            }
        });
    }


    public void callRetrofitGetYelpDeliveryBusinesses(Context context,String stringQuery){

        //eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();

        //TODO: Incorrect string query. Write a method to generate a string
        //Call<YelpTokenClass> callVenue = YelpRetrofitClientFactory().getYelpTokenFromServer(stringQuery);

        Call<YelpResponse> callVenue = YelpRetrofitClientFactory().getYelpDeliveryFromServer("San Francisco");


        callVenue.enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                YelpResponse yelpResponse = response.body();

                if (yelpResponse == null) {
                    Log.d(TAG, "NO MATCH " );

                }else{
                    businesses = yelpResponse.getBusinesses();

                }
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage() );

            }
        });
    }

}