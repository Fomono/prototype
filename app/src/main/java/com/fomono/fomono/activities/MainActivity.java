package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fomono.fomono.R;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.network.client.EventBriteClient;
import com.fomono.fomono.network.client.YelpClient;
import com.fomono.fomono.utils.ConfigUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    YelpClient yelpClient = new YelpClient(MainActivity.this);
    EventBriteClient eventBriteClient = new EventBriteClient(MainActivity.this);
    String testPropValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(this, FomonoDetailActivity.class);
        FomonoEvent fEvent = new Business();
        //Sample Read test property file entries
        try {
            testPropValue = ConfigUtil.getProperty("115",getApplicationContext());
        } catch (IOException e) {
            testPropValue ="No Entry in prop file";
            e.printStackTrace();
        }
        i.putExtra("FOM_OBJ", fEvent);
        startActivity(i);
        //Setup APIs
        setupAPIs();
    }

    public void setupAPIs() {

        //   yelpClient.getYelpToken();
        //Get all the businesses in the area
      /* yelpClient.getYelpBusinesses(null, null, -1, -1, -1, null, null, false, null);
        //Get all the places which deliver
        yelpClient.getYelpFoodDeliveryBusinesses(null, -1, -1);
        //There is also an autocomplete endpoint. I will add it if we want to get auto complete in search

        //Get all the events in the area
       // eventBriteClient.getEventBriteEventList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        //Get all the event categories provided by event brite(For filters)
        eventBriteClient.getEventCategories();
        //Get the current user's credentials. Just in case
        eventBriteClient.getUserCredentials();*/

        String tokenString =null;
//        EventBriteClientRetrofit.getNewInstance().callEBRetrofitAPI(0, null);

       // YelpClientRetrofit.getNewInstance().callRetrofitGetYelpDeliveryBusinesses(MainActivity.this, tokenString);
        //MovieDBClientRetrofit.getNewInstance().callRetrofitGetMoviesNowPlaying(MainActivity.this,tokenString);

    }




}
