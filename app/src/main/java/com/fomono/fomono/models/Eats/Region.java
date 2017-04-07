package com.fomono.fomono.models.Eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/5/2017.
 */

public class Region implements Parcelable
{

    @SerializedName("center")
    @Expose
    private Center center;
    public final static Parcelable.Creator<Region> CREATOR = new Creator<Region>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Region createFromParcel(Parcel in) {
            Region instance = new Region();
            instance.center = ((Center) in.readValue((Center.class.getClassLoader())));
            return instance;
        }

        public Region[] newArray(int size) {
            return (new Region[size]);
        }

    }
            ;

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(center);
    }

    public int describeContents() {
        return 0;
    }

}
