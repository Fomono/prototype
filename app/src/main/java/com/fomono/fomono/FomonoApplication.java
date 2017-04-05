package com.fomono.fomono;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by David on 4/5/2017.
 */

public class FomonoApplication extends Application {
    final String CHANNEL_NAME = "pushChannelTest";

    @Override
    public void onCreate() {
        super.onCreate();

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

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
    }
}
