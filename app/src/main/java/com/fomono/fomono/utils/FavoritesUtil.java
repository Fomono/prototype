package com.fomono.fomono.utils;

import android.support.annotation.NonNull;

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

import java.util.ArrayList;
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
    private boolean loaded;
    private List<FavoritesListener> listeners;

    private FavoritesUtil() {
        this.favoritesMap = new HashMap<>();
        loaded = false;
        listeners = new ArrayList<>();
    }

    public interface FavoritesListener {
        void onFavoritesLoaded(List<Favorite> favorites);
    }

    public void initialize(ParseUser user) {
        this.user = user;

        ParseQuery<Favorite> query = ParseQuery.getQuery(Favorite.class);
        query.whereEqualTo("user", user)
                .include("event")
                .include("event.name")
                .include("event.description")
                .include("event.start")
                .include("event.end")
                .include("event.logo")
                .include("event.venue")
                .include("event.logo.original")
                .include("event.venue.address")
                .include("business")
                .include("business.categories")
                .include("business.coordinates")
                .include("business.location")
                .include("movie")
                .findInBackground(new FindCallback<Favorite>() {
            @Override
            public void done(List<Favorite> favs, ParseException e) {
                if (favs != null) {
                    Favorite.initializeFromList(favs);
                    for (Favorite f : favs) {
                        addToFavoritesMap(f);
                    }
                }
                loaded = true;
                notifyListeners();
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
        notifyListeners();
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
        f.deleteInBackground();
        notifyListeners();
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

    public void getFavoritesWhenLoaded(@NonNull FavoritesListener listener) {
        if (loaded) {
            listener.onFavoritesLoaded(getFavorites());
        } else {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    private void notifyListeners() {
        List<Favorite> favs = getFavorites();
        for (FavoritesListener l : listeners) {
            if (l != null) {
                l.onFavoritesLoaded(favs);
            }
        }
    }

    private List<Favorite> getFavorites() {
        List<Favorite> favs = new ArrayList<>();
        for (Map<String, Favorite> m : favoritesMap.values()) {
            for (Favorite f : m.values()) {
                favs.add(f);
            }
        }
        return favs;
    }
}
