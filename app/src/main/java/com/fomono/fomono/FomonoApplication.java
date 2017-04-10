package com.fomono.fomono;

import android.app.Application;
import android.util.Log;

import com.fomono.fomono.models.db.Filter;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by David on 4/5/2017.
 */

public class FomonoApplication extends Application {
    public static final String API_NAME_EVENTS = "eventbrite";
    public static final String API_NAME_EATS = "yelp";
    public static final String API_NAME_MOVIES = "fandango";

    final String CHANNEL_NAME = "pushChannelTest";

    @Override
    public void onCreate() {
        super.onCreate();

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Register our Parse objects
        ParseObject.registerSubclass(Filter.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fomono") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://fomono.herokuapp.com/parse/")
                .build());

        //subscribe to channel for push notifications
//        ParsePush.subscribeInBackground(CHANNEL_NAME);
        // Need to register GCM token
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d("DEBUG", "Anonymous login failed.");
                } else {
                    Log.d("DEBUG", "Anonymous user logged in.");
                }
            }
        });
    }
}
