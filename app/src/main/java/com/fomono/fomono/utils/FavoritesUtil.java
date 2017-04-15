package com.fomono.fomono.utils;

import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.db.Favorite;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 4/14/2017.
 */

public class FavoritesUtil {
    private static FavoritesUtil instance;
    public static FavoritesUtil getInstance() {
        if (instance == null) {
            instance = new FavoritesUtil();
        }
        return instance;
    }

    private ParseUser user;
    private Map<String, Map<String, Favorite>> favoritesMap;

    public void initialize(ParseUser user) {
        this.user = user;
        this.favoritesMap = new HashMap<>();

        ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
        query.whereEqualTo("user", user)
                .include("fomono_event")
                .findInBackground(new FindCallback<Favorite>() {
            @Override
            public void done(List<Favorite> favs, ParseException e) {
                if (favs != null) {
                    Favorite.initializeFromList(favs);
                    for (Favorite f : favs) {
                        addToFavoritesMap(f);
                    }
                }
            }
        });
    }

    public void addToFavorites(FomonoEvent fEvent) {
        //first we need to get the event from our Parse db, so we don't duplicate events
        if (fEvent instanceof Event) {
            ((Event) fEvent).getFromParse(new GetCallback<Event>() {
                @Override
                public void done(Event object, ParseException e) {
                    addToFavoritesCallback(object);
                }
            });
        } else if (fEvent instanceof Business) {
            ((Business) fEvent).getFromParse(new GetCallback<Business>() {
                @Override
                public void done(Business object, ParseException e) {
                    addToFavoritesCallback(object);
                }
            });
        } else if (fEvent instanceof Movie) {
            ((Movie) fEvent).getFromParse(new GetCallback<Movie>() {
                @Override
                public void done(Movie object, ParseException e) {
                    addToFavoritesCallback(object);
                }
            });
        }
    }

    private void addToFavoritesCallback(FomonoEvent fEvent) {
        Favorite fav = new Favorite(fEvent);
        addToFavoritesMap(fav);
        fav.saveInBackground();
    }

    private void addToFavoritesMap(Favorite f) {
        String apiName = f.getApiName();
        if (!favoritesMap.containsKey(apiName)) {
            favoritesMap.put(apiName, new HashMap<>());
        }
        favoritesMap.get(apiName).put(f.getFomonoEventId(), f);
    }

    public void removeFromFavorites(FomonoEvent fEvent) {
        String id = fEvent.getStringId();
        String apiName = fEvent.getApiName();
        if (!favoritesMap.containsKey(apiName)) {
            return;
        }
        Favorite f = favoritesMap.get(apiName).get(id);
        if (f == null) {
            return;
        }
        favoritesMap.get(apiName).remove(id);
        f.deleteEventually();
    }

    public boolean isFavorited(FomonoEvent fEvent) {
        String id = fEvent.getStringId();
        String apiName = fEvent.getApiName();
        if (!favoritesMap.containsKey(apiName)) {
            return false;
        }
        Favorite f = favoritesMap.get(apiName).get(id);
        if (f == null) {
            return false;
        }
        return true;
    }
}
