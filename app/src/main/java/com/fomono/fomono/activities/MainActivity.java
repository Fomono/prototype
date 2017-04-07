package com.fomono.fomono.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fomono.fomono.R;
import com.fomono.fomono.network.client.EventBriteClient;
import com.fomono.fomono.network.client.YelpClient;

public class MainActivity extends AppCompatActivity {

    YelpClient yelpClient = new YelpClient(MainActivity.this);
    EventBriteClient eventBriteClient = new EventBriteClient(MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup APIs
        setupAPIs();
    }


    public void setupAPIs() {

        //   yelpClient.getYelpToken();
        //Get all the businesses in the area
        yelpClient.getYelpBusinesses(null, null, -1, -1, -1, null, null, false, null);
        //Get all the places which deliver
        yelpClient.getYelpFoodDeliveryBusinesses(null, -1, -1);
        //There is also an autocomplete endpoint. I will add it if we want to get auto complete in search

        //Get all the events in the area
        eventBriteClient.getEventBriteEventList(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        //Get all the event categories provided by event brite(For filters)
        eventBriteClient.getEventCategories();
        //Get the current user's credentials. Just in case
        eventBriteClient.getUserCredentials();
    }
}
