package com.fomono.fomono.models.db;

import com.fomono.fomono.models.FomonoEvent;
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
        this.put("fomono_event", fomonoEvent);
        this.put("user", ParseUser.getCurrentUser());
    }

    /**
     * Needs to be called after retrieving objects from db.
     */
    public void initialize() {
        this.fomonoEvent = (FomonoEvent) getParseObject("fomono_event");
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
        if (fomonoEvent == null) {
            fomonoEvent = (FomonoEvent) getParseObject("fomono_event");
        }
        return fomonoEvent;
    }

    public void setFomonoEvent(FomonoEvent fomonoEvent) {
        this.fomonoEvent = fomonoEvent;
        this.put("fomono_event", fomonoEvent);
    }

    public String getFomonoEventId() {
        if (fomonoEventId == null) {
            fomonoEventId = getString("fomono_event_id");
        }
        return fomonoEventId;
    }

    public void setFomonoEventId(String fomonoEventId) {
        this.fomonoEventId = fomonoEventId;
        this.put("fomono_event_id", fomonoEventId);
    }
}
