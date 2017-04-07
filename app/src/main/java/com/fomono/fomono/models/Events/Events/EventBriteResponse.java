package com.fomono.fomono.models.Events.Events;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;

import java.util.ArrayList;

public class EventBriteResponse implements Parcelable
{

    @SerializedName("pagination")
    @Expose
    private com.fomono.fomono.models.Events.Categories.Pagination pagination;
    @SerializedName("events")
    @Expose
    private ArrayList<Event> events = null;
    public final static Parcelable.Creator<EventBriteResponse> CREATOR = new Creator<EventBriteResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EventBriteResponse createFromParcel(Parcel in) {
            EventBriteResponse instance = new EventBriteResponse();
            instance.pagination = ((com.fomono.fomono.models.Events.Categories.Pagination) in.readValue((com.fomono.fomono.models.Events.Categories.Pagination.class.getClassLoader())));
            in.readList(instance.events, (Event.class.getClassLoader()));
            return instance;
        }

        public EventBriteResponse[] newArray(int size) {
            return (new EventBriteResponse[size]);
        }

    }
            ;

    public com.fomono.fomono.models.Events.Categories.Pagination getPagination() {
        return pagination;
    }

    public void setPagination(com.fomono.fomono.models.Events.Categories.Pagination pagination) {
        this.pagination = pagination;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pagination);
        dest.writeList(events);
    }

    public int describeContents() {
        return 0;
    }

}
