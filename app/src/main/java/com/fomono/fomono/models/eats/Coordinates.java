package com.fomono.fomono.models.eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jsaluja on 4/5/2017.
 */
@ParseClassName("BusinessCoordinates")
public class Coordinates extends ParseObject implements Parcelable
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
            instance.initializeForParse();
            return instance;
        }

        public Coordinates[] newArray(int size) {
            return (new Coordinates[size]);
        }

    }
            ;

    public void updateWithExisting(Coordinates instance) {
        if (instance.latitude != null) {
            this.put("latitude", instance.latitude);
        }
        if (instance.longitude != null) {
            this.put("longitude", instance.longitude);
        }
    }

    public void initializeForParse() {
        if (this.latitude != null) {
            this.put("latitude", this.latitude);
        }
        if (this.longitude != null) {
            this.put("longitude", this.longitude);
        }
    }

    public void initializeFromParse() {
        if (has("latitude")) {
            latitude = getDouble("latitude");
        }
        if (has("longitude")) {
            longitude = getDouble("longitude");
        }
    }

    public Double getLatitude() {
        if (latitude == null) {
            latitude = getDouble("latitude");
        }
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        if (longitude == null) {
            longitude = getDouble("longitude");
        }
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