package com.fomono.fomono;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.fomono.fomono.models.db.Favorite;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.eats.Category;
import com.fomono.fomono.models.eats.Coordinates;
import com.fomono.fomono.models.eats.Location;
import com.fomono.fomono.models.events.events.Address;
import com.fomono.fomono.models.events.events.Description;
import com.fomono.fomono.models.events.events.End;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.Logo;
import com.fomono.fomono.models.events.events.Name;
import com.fomono.fomono.models.events.events.Original;
import com.fomono.fomono.models.events.events.Start;
import com.fomono.fomono.models.events.events.Venue;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.utils.FavoritesUtil;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
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
    public static final String API_NAME_MOVIES = "movies";

    //permission request codes
    public static final int PERM_LOC_EVENT_REQ_CODE = 1;
    public static final int PERM_LOC_BUS_REQ_CODE = 2;
    public static final int PERM_LOC_MOVIE_REQ_CODE = 3;

    final String CHANNEL_NAME = "pushChannelTest";

    @Override
    public void onCreate() {
        super.onCreate();

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Register our Parse objects
        ParseObject.registerSubclass(Filter.class);
        ParseObject.registerSubclass(Favorite.class);
        //Parse objects related to Events
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Address.class);
        ParseObject.registerSubclass(Description.class);
        ParseObject.registerSubclass(End.class);
        ParseObject.registerSubclass(Logo.class);
        ParseObject.registerSubclass(Name.class);
        ParseObject.registerSubclass(Original.class);
        ParseObject.registerSubclass(Start.class);
        ParseObject.registerSubclass(Venue.class);
        //Parse objects related to Businesses
        ParseObject.registerSubclass(Business.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Coordinates.class);
        ParseObject.registerSubclass(Location.class);
        //Parse objects related to Movies
        ParseObject.registerSubclass(Movie.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fomono") // should correspond to APP_ID env variable
                .clientKey(null)  // set explicitly unless clientKey is explicitly configured on Parse server
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://fomono.herokuapp.com/parse/")
                .enableLocalDataStore()
                .build());


        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
        //subscribe to channel for push notifications
//        ParsePush.subscribeInBackground(CHANNEL_NAME);
        // Need to register GCM token
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.d("DEBUG", "Anonymous login failed.");
                } else {
                    Log.d("DEBUG", "Anonymous user logged in.");
                    try {
                        //update user from server
                        user.fetch();
                        FavoritesUtil.getInstance().initialize(ParseUser.getCurrentUser());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }
}
