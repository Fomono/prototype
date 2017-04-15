package com.fomono.fomono.models.db;

import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by David on 4/14/2017.
 */
@ParseClassName("Favorite")
public class Favorite extends ParseObject {
    String apiName;
    String fomonoEventId;
    FomonoEvent fomonoEvent;

    public Favorite() {
        //required empty default constructor
    }

    public Favorite(FomonoEvent favoriteEvent) {
        apiName = favoriteEvent.getApiName();
        fomonoEventId = favoriteEvent.getStringId();
        fomonoEvent = favoriteEvent;

        this.put("api_name", apiName);
        this.put("fomono_event_id", fomonoEventId);
        addFomonoEventForParse(favoriteEvent);
        this.put("user", ParseUser.getCurrentUser());
    }

    private void addFomonoEventForParse(FomonoEvent ev) {
        if (ev instanceof Event) {
            this.put("event", ev);
        } else if (ev instanceof Business) {
            this.put("business", ev);
        } else if (ev instanceof Movie) {
            this.put("movie", ev);
        }
    }

    private FomonoEvent getFomonoEventFromParse() {
        if (this.has("event")) {
            return (FomonoEvent) getParseObject("event");
        } else if (this.has("business")) {
            return (FomonoEvent) getParseObject("business");
        } else if (this.has("movie")) {
            return (FomonoEvent) getParseObject("movie");
        }
        return null;
    }

    /**
     * Needs to be called after retrieving objects from db.
     */
    public void initialize() {
        this.fomonoEvent = getFomonoEventFromParse();
        this.apiName = getString("api_name");
        this.fomonoEventId = getString("fomono_event_id");
    }

    public static void initializeFromList(List<Favorite> favs) {
        if (favs == null) {
            return;
        }
        for (Favorite f : favs) {
            f.initialize();
        }
    }

    public String getApiName() {
        if (apiName == null) {
            apiName = getString("api_name");
        }
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
        this.put("api_name", apiName);
    }

    public FomonoEvent getFomonoEvent() {
        return fomonoEvent;
    }

    public void setFomonoEvent(FomonoEvent fomonoEvent) {
        this.fomonoEvent = fomonoEvent;
        this.put("fomono_event", fomonoEvent);
    }

    public String getFomonoEventId() {
        return fomonoEventId;
    }

    public void setFomonoEventId(String fomonoEventId) {
        this.fomonoEventId = fomonoEventId;
    }
}
