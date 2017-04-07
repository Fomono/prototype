package com.fomono.fomono.models.Events.Events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/6/2017.
 */

public class Event implements Parcelable
{

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("description")
    @Expose
    private Description description;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("start")
    @Expose
    private com.fomono.fomono.models.Events.Events.Start start;
    @SerializedName("end")
    @Expose
    private End end;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("changed")
    @Expose
    private String changed;
    @SerializedName("capacity")
    @Expose
    private Integer capacity;
    @SerializedName("capacity_is_custom")
    @Expose
    private Boolean capacityIsCustom;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("listed")
    @Expose
    private Boolean listed;
    @SerializedName("shareable")
    @Expose
    private Boolean shareable;
    @SerializedName("online_event")
    @Expose
    private Boolean onlineEvent;
    @SerializedName("tx_time_limit")
    @Expose
    private Integer txTimeLimit;
    @SerializedName("hide_start_date")
    @Expose
    private Boolean hideStartDate;
    @SerializedName("hide_end_date")
    @Expose
    private Boolean hideEndDate;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("is_locked")
    @Expose
    private Boolean isLocked;
    @SerializedName("privacy_setting")
    @Expose
    private String privacySetting;
    @SerializedName("is_series")
    @Expose
    private Boolean isSeries;
    @SerializedName("is_series_parent")
    @Expose
    private Boolean isSeriesParent;
    @SerializedName("is_reserved_seating")
    @Expose
    private Boolean isReservedSeating;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("is_free")
    @Expose
    private Boolean isFree;
    @SerializedName("logo_id")
    @Expose
    private Object logoId;
    @SerializedName("organizer_id")
    @Expose
    private String organizerId;
    @SerializedName("venue_id")
    @Expose
    private String venueId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("subcategory_id")
    @Expose
    private String subcategoryId;
    @SerializedName("format_id")
    @Expose
    private String formatId;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("logo")
    @Expose
    private Object logo;
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
            instance.start = ((com.fomono.fomono.models.Events.Events.Start) in.readValue((com.fomono.fomono.models.Events.Events.Start.class.getClassLoader())));
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
            instance.logo = ((Object) in.readValue((Object.class.getClassLoader())));
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

    public com.fomono.fomono.models.Events.Events.Start getStart() {
        return start;
    }

    public void setStart(com.fomono.fomono.models.Events.Events.Start start) {
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

    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) {
        this.logo = logo;
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
    }

    public int describeContents() {
        return 0;
    }

}
