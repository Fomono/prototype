package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.fomono.fomono.models.FomonoEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/6/2017.
 */

public class Event implements Parcelable, FomonoEvent
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
            instance.start = ((com.fomono.fomono.models.events.events.Start) in.readValue((com.fomono.fomono.models.events.events.Start.class.getClassLoader())));
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
          //  instance.logo = ((Logo) in.readValue((Logo.class.getClassLoader())));
            instance.venue = ((Venue) in.readValue((Venue.class.getClassLoader())));
            return instance;
        }

        public Event[] newArray(int size) {
            return (new Event[size]);
        }

    }
            ;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public com.fomono.fomono.models.events.events.Start getStart() {
        return start;
    }

    public void setStart(com.fomono.fomono.models.events.events.Start start) {
        this.start = start;
    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
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
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
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

    public Logo getLogo() {return logo;}

    public void setLogo(Logo logo) {this.logo = logo;}

    public Venue getVenue() {return venue;}

    public void setVenue(Venue venue) {this.venue = venue;}

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
//        dest.writeValue(logo);
        dest.writeValue(venue);
    }

    public int describeContents() {
        return 0;
    }

}
