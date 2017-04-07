package com.fomono.fomono.models.Eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/5/2017.
 */

public class Coordinates implements Parcelable
{

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    public final static Parcelable.Creator<Coordinates> CREATOR = new Creator<Coordinates>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Coordinates createFromParcel(Parcel in) {
            Coordinates instance = new Coordinates();
            instance.latitude = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.longitude = ((Double) in.readValue((Double.class.getClassLoader())));
            return instance;
        }

        public Coordinates[] newArray(int size) {
            return (new Coordinates[size]);
        }

    }
            ;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    public int describeContents() {
        return 0;
    }

}