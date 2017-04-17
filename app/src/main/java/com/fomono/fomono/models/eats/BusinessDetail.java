package com.fomono.fomono.models.eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saranu on 4/16/17.
 */

public class BusinessDetail implements Parcelable {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("photos")
        @Expose
        private List<String> photos = null;
        @SerializedName("hours")
        @Expose
        private List<Hour> hours = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Hour> getHours() {
        return hours;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }

    protected BusinessDetail(Parcel in) {
        id = in.readString();
        photos = in.createStringArrayList();
        hours = in.createTypedArrayList(Hour.CREATOR);
    }

    public static final Creator<BusinessDetail> CREATOR = new Creator<BusinessDetail>() {
        @Override
        public BusinessDetail createFromParcel(Parcel in) {
            return new BusinessDetail(in);
        }

        @Override
        public BusinessDetail[] newArray(int size) {
            return new BusinessDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringList(photos);
        dest.writeTypedList(hours);
    }
}