package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/8/2017.
 */

public class TopLeft implements Parcelable{

    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;

    protected TopLeft(Parcel in) {
    }

    public static final Creator<TopLeft> CREATOR = new Creator<TopLeft>() {
        @Override
        public TopLeft createFromParcel(Parcel in) {
            return new TopLeft(in);
        }

        @Override
        public TopLeft[] newArray(int size) {
            return new TopLeft[size];
        }
    };

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}