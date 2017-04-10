package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/6/2017.
 */

public class Start implements Parcelable
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
    public final static Parcelable.Creator<Start> CREATOR = new Creator<Start>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Start createFromParcel(Parcel in) {
            Start instance = new Start();
            instance.timezone = ((String) in.readValue((String.class.getClassLoader())));
            instance.local = ((String) in.readValue((String.class.getClassLoader())));
            instance.utc = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Start[] newArray(int size) {
            return (new Start[size]);
        }

    }
            ;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
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