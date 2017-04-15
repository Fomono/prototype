package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.models.FomonoEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by jsaluja on 4/6/2017.
 */
@ParseClassName("Event")
public class Event extends ParseObject implements Parcelable, FomonoEvent
{
    @SerializedName("name")
    @Expose
    public Name name;
    @SerializedName("description")
    @Expose
    public Description description;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("start")
    @Expose
    public Start start;
    @SerializedName("end")
    @Expose
    public End end;
    @SerializedName("created")
    @Expose
    public String created;
    @SerializedName("changed")
    @Expose
    public String changed;
    @SerializedName("capacity")
    @Expose
    public Integer capacity;
    @SerializedName("capacity_is_custom")
    @Expose
    public Boolean capacityIsCustom;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("listed")
    @Expose
    public Boolean listed;
    @SerializedName("shareable")
    @Expose
    public Boolean shareable;
    @SerializedName("online_event")
    @Expose
    public Boolean onlineEvent;
    @SerializedName("tx_time_limit")
    @Expose
    public Integer txTimeLimit;
    @SerializedName("hide_start_date")
    @Expose
    public Boolean hideStartDate;
    @SerializedName("hide_end_date")
    @Expose
    public Boolean hideEndDate;
    @SerializedName("locale")
    @Expose
    public String locale;
    @SerializedName("is_locked")
    @Expose
    public Boolean isLocked;
    @SerializedName("privacy_setting")
    @Expose
    public String privacySetting;
    @SerializedName("is_series")
    @Expose
    public Boolean isSeries;
    @SerializedName("is_series_parent")
    @Expose
    public Boolean isSeriesParent;
    @SerializedName("is_reserved_seating")
    @Expose
    public Boolean isReservedSeating;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("is_free")
    @Expose
    public Boolean isFree;
    @SerializedName("logo_id")
    @Expose
    public Object logoId;
    @SerializedName("organizer_id")
    @Expose
    public String organizerId;
    @SerializedName("venue_id")
    @Expose
    public String venueId;
    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("subcategory_id")
    @Expose
    public String subcategoryId;
    @SerializedName("format_id")
    @Expose
    public String formatId;
    @SerializedName("resource_uri")
    @Expose
    public String resourceUri;
    @SerializedName("logo")
    @Expose
    public Logo logo;
    @SerializedName("venue")
    @Expose
    public Venue venue;

    public final static Parcelable.Creator<Event> CREATOR = new Creator<Event>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Event createFromParcel(Parcel in) {
            Event instance = new Event();
            instance.name = ((Name) in.readValue((Name.class.getClassLoader())));
            instance.description = ((Description) in.readValue((Description.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.start = ((Start) in.readValue((Start.class.getClassLoader())));
            instance.end = ((End) in.readValue((End.class.getClassLoader())));
            instance.created = ((String) in.readValue((String.class.getClassLoader())));
            instance.changed = ((String) in.readValue((String.class.getClassLoader())));
            instance.capacity = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.capacityIsCustom = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            instance.currency = ((String) in.readValue((String.class.getClassLoader())));
            instance.listed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.shareable = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.onlineEvent = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.txTimeLimit = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.hideStartDate = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.hideEndDate = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.locale = ((String) in.readValue((String.class.getClassLoader())));
            instance.isLocked = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.privacySetting = ((String) in.readValue((String.class.getClassLoader())));
            instance.isSeries = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.isSeriesParent = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.isReservedSeating = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.source = ((String) in.readValue((String.class.getClassLoader())));
            instance.isFree = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.logoId = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.organizerId = ((String) in.readValue((String.class.getClassLoader())));
            instance.venueId = ((String) in.readValue((String.class.getClassLoader())));
            instance.categoryId = ((String) in.readValue((String.class.getClassLoader())));
            instance.subcategoryId = ((String) in.readValue((String.class.getClassLoader())));
            instance.formatId = ((String) in.readValue((String.class.getClassLoader())));
            instance.resourceUri = ((String) in.readValue((String.class.getClassLoader())));
            instance.logo = ((Logo) in.readValue((Logo.class.getClassLoader())));
            instance.venue = ((Venue) in.readValue((Venue.class.getClassLoader())));
            instance.setObjectId(in.readString());

            initializeForParse(instance);

            return instance;
        }

        public Event[] newArray(int size) {
            return (new Event[size]);
        }

    };

    public Event() {
        //required empty default constructor
    }

    /**
     * Initializes an Event object as a ParseObject that can be saved to db.
     * Note: Only stores data we care about.
     * @param instance
     */
    private static void initializeForParse(Event instance) {
        instance.name.initializeForParse();
        instance.put("name", instance.name);
        instance.description.initializeForParse();
        instance.put("description", instance.description);
        instance.put("id", instance.id);
        instance.put("url", instance.url);
        instance.start.initializeForParse();
        instance.put("start", instance.start);
        instance.end.initializeForParse();
        instance.put("end", instance.end);
        instance.put("created", instance.created);
        instance.put("is_free", instance.isFree);
        instance.put("venue_id", instance.venueId);
        instance.logo.initializeForParse();
        instance.put("logo", instance.logo);
        if (instance.venue != null) {
            instance.venue.initializeForParse();
            instance.put("venue", instance.venue);
        }
    }

    public void getFromParse(GetCallback<Event> callback) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.whereEqualTo("id", this.getId())
                .include("name")
                .include("description")
                .include("start")
                .include("end")
                .include("logo")
                .include("venue")
                .include("logo.original")
                .include("venue.address")
                .getFirstInBackground(callback);
    }

    public void saveOrUpdate() {
        getFromParse(new GetCallback<Event>() {
                    @Override
                    public void done(Event object, ParseException e) {
                        //if it's a new object, save it
                        if (object == null) {
                            Event.this.saveInBackground();
                        } else {
                            //otherwise update any fields, and save the original
                            object.updateWithExisting(Event.this);
                            object.saveInBackground();
                        }
                    }
                });
    }

    public void updateWithExisting(Event event) {
        ((Name)this.get("name")).updateWithExisting(event.name);
        ((Description)this.get("description")).updateWithExisting(event.description);
        this.put("id", event.id);
        this.put("url", event.url);
        ((Start)this.get("start")).updateWithExisting(event.start);
        ((End)this.get("end")).updateWithExisting(event.end);
        this.put("created", event.created);
        this.put("is_free", event.isFree);
        this.put("venue_id", event.venueId);
        ((Logo)this.get("logo")).updateWithExisting(event.logo);
        if (event.venue != null) {
            if (this.has("venue")) {
                ((Venue)this.get("venue")).updateWithExisting(event.venue);
            } else {
                this.put("venue", event.venue);
            }
        }
    }

    public Name getName() {
        if (name == null) {
            name = (Name) getParseObject("name");
        }
        return name;
    }

    public void setName(Name name) {
        this.name = name;
        this.put("name", name);
    }

    public Description getDescription() {
        if (description == null) {
            description = (Description) getParseObject("description");
        }
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
        this.put("description", description);
    }

    public String getId() {
        if (id == null) {
            id = getString("id");
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.put("id", id);
    }

    public String getUrl() {
        if (url == null) {
            url = getString("url");
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.put("url", url);
    }

    public Start getStart() {
        if (start == null) {
            start = (Start) getParseObject("start");
        }
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
        this.put("start", start);
    }

    public End getEnd() {
        if (end == null) {
            end = (End) getParseObject("end");
        }
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
        this.put("end", end);
    }

    public String getCreated() {
        if (created == null) {
            created = getString("created");
        }
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
        this.put("created", created);
    }

    public String getChanged() {
        return changed;
    }

    public void setChanged(String changed) {
        this.changed = changed;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getCapacityIsCustom() {
        return capacityIsCustom;
    }

    public void setCapacityIsCustom(Boolean capacityIsCustom) {
        this.capacityIsCustom = capacityIsCustom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getListed() {
        return listed;
    }

    public void setListed(Boolean listed) {
        this.listed = listed;
    }

    public Boolean getShareable() {
        return shareable;
    }

    public void setShareable(Boolean shareable) {
        this.shareable = shareable;
    }

    public Boolean getOnlineEvent() {
        return onlineEvent;
    }

    public void setOnlineEvent(Boolean onlineEvent) {
        this.onlineEvent = onlineEvent;
    }

    public Integer getTxTimeLimit() {
        return txTimeLimit;
    }

    public void setTxTimeLimit(Integer txTimeLimit) {
        this.txTimeLimit = txTimeLimit;
    }

    public Boolean getHideStartDate() {
        return hideStartDate;
    }

    public void setHideStartDate(Boolean hideStartDate) {
        this.hideStartDate = hideStartDate;
    }

    public Boolean getHideEndDate() {
        return hideEndDate;
    }

    public void setHideEndDate(Boolean hideEndDate) {
        this.hideEndDate = hideEndDate;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(String privacySetting) {
        this.privacySetting = privacySetting;
    }

    public Boolean getIsSeries() {
        return isSeries;
    }

    public void setIsSeries(Boolean isSeries) {
        this.isSeries = isSeries;
    }

    public Boolean getIsSeriesParent() {
        return isSeriesParent;
    }

    public void setIsSeriesParent(Boolean isSeriesParent) {
        this.isSeriesParent = isSeriesParent;
    }

    public Boolean getIsReservedSeating() {
        return isReservedSeating;
    }

    public void setIsReservedSeating(Boolean isReservedSeating) {
        this.isReservedSeating = isReservedSeating;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getIsFree() {
        if (has("is_free")) {
            isFree = getBoolean("is_free");
        }
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
        this.put("is_free", isFree);
    }

    public Object getLogoId() {
        return logoId;
    }

    public void setLogoId(Object logoId) {
        this.logoId = logoId;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getVenueId() {
        if (venueId == null) {
            venueId = getString("venue_id");
        }
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
        this.put("venue_id", venueId);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Logo getLogo() {
        if (logo == null) {
            logo = (Logo) getParseObject("logo");
        }
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
        logo.initializeForParse();
        this.put("logo", logo);
    }

    public Venue getVenue() {
        if (venue == null) {
            venue = (Venue) getParseObject("venue");
        }
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
        venue.initializeForParse();
        this.put("venue", venue);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(description);
        dest.writeValue(id);
        dest.writeValue(url);
        dest.writeValue(start);
        dest.writeValue(end);
        dest.writeValue(created);
        dest.writeValue(changed);
        dest.writeValue(capacity);
        dest.writeValue(capacityIsCustom);
        dest.writeValue(status);
        dest.writeValue(currency);
        dest.writeValue(listed);
        dest.writeValue(shareable);
        dest.writeValue(onlineEvent);
        dest.writeValue(txTimeLimit);
        dest.writeValue(hideStartDate);
        dest.writeValue(hideEndDate);
        dest.writeValue(locale);
        dest.writeValue(isLocked);
        dest.writeValue(privacySetting);
        dest.writeValue(isSeries);
        dest.writeValue(isSeriesParent);
        dest.writeValue(isReservedSeating);
        dest.writeValue(source);
        dest.writeValue(isFree);
        dest.writeValue(logoId);
        dest.writeValue(organizerId);
        dest.writeValue(venueId);
        dest.writeValue(categoryId);
        dest.writeValue(subcategoryId);
        dest.writeValue(formatId);
        dest.writeValue(resourceUri);
        dest.writeValue(logo);
        dest.writeValue(venue);
        dest.writeString(getObjectId());
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String getStringId() {
        return getId();
    }

    @Override
    public String getApiName() {
        return FomonoApplication.API_NAME_EVENTS;
    }
}
