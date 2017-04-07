package com.fomono.fomono.models.Events.Events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/6/2017.
 */


public class End implements Parcelable
{

    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("local")
    @Expose
    private String local;
    @SerializedName("utc")
    @Expose
    private String utc;
    public final static Parcelable.Creator<End> CREATOR = new Creator<End>() {


        @SuppressWarnings({
                "unchecked"
        })
        public End createFromParcel(Parcel in) {
            End instance = new End();
            instance.timezone = ((String) in.readValue((String.class.getClassLoader())));
            instance.local = ((String) in.readValue((String.class.getClassLoader())));
            instance.utc = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public End[] newArray(int size) {
            return (new End[size]);
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