package com.fomono.fomono.models.Eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jsaluja on 4/5/2017.
 */

public class YelpResponse implements Parcelable
{

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("businesses")
    @Expose
    private ArrayList<Business> businesses = null;
    @SerializedName("region")
    @Expose
    private Region region;
    public final static Parcelable.Creator<YelpResponse> CREATOR = new Creator<YelpResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public YelpResponse createFromParcel(Parcel in) {
            YelpResponse instance = new YelpResponse();
            instance.total = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.businesses, (Business.class.getClassLoader()));
            instance.region = ((Region) in.readValue((Region.class.getClassLoader())));
            return instance;
        }

        public YelpResponse[] newArray(int size) {
            return (new YelpResponse[size]);
        }

    }
            ;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ArrayList<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(ArrayList<Business> businesses) {
        this.businesses = businesses;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(total);
        dest.writeList(businesses);
        dest.writeValue(region);
    }

    public int describeContents() {
        return 0;
    }

}