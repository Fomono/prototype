package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jsaluja on 4/6/2017.
 */
@ParseClassName("EventStart")
public class Start extends ParseObject implements Parcelable
{

    @SerializedName("timezone")
    @Expose
    public String timezone;
    @SerializedName("local")
    @Expose
    public String local;
    @SerializedName("utc")
    @Expose
    public String utc;

    public Start() {
        //required empty default constructor
    }

    public final static Parcelable.Creator<Start> CREATOR = new Creator<Start>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Start createFromParcel(Parcel in) {
            Start instance = new Start();
            instance.timezone = ((String) in.readValue((String.class.getClassLoader())));
            instance.local = ((String) in.readValue((String.class.getClassLoader())));
            instance.utc = ((String) in.readValue((String.class.getClassLoader())));

            instance.initializeForParse();
            return instance;
        }

        public Start[] newArray(int size) {
            return (new Start[size]);
        }

    }
            ;

    public void updateWithExisting(Start instance) {
        this.put("timezone", instance.timezone);
        this.put("local", instance.local);
        this.put("utc", instance.utc);
    }

    public void initializeForParse() {
        this.put("timezone", timezone);
        this.put("local", local);
        this.put("utc", utc);
    }

    public String getTimezone() {
        if (timezone == null) {
            timezone = getString("timezone");
        }
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
        this.put("timezone", timezone);
    }

    public String getLocal() {
        if (local == null) {
            local = getString("local");
        }
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
        this.put("local", local);
    }

    public String getUtc() {
        if (utc == null) {
            utc = getString("utc");
        }
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
        this.put("utc", utc);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(timezone);
        dest.writeValue(local);
        dest.writeValue(utc);
    }

    public int describeContents() {
        return 0;
    }

}