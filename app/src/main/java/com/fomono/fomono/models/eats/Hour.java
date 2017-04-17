package com.fomono.fomono.models.eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saranu on 4/16/17.
 */

public class Hour implements Parcelable{

    @SerializedName("hours_type")
    @Expose
    private String hoursType;
    @SerializedName("open")
    @Expose
    private List<Open> open = null;
    @SerializedName("is_open_now")
    @Expose
    private boolean isOpenNow;

    protected Hour(Parcel in) {
        hoursType = in.readString();
        open = in.createTypedArrayList(Open.CREATOR);
        isOpenNow = in.readByte() != 0;
    }

    public static final Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel in) {
            return new Hour(in);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };

    public String getHoursType() {
        return hoursType;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public List<Open> getOpen() {
        return open;
    }

    public void setOpen(List<Open> open) {
        this.open = open;
    }

    public boolean isIsOpenNow() {
        return isOpenNow;
    }

    public void setIsOpenNow(boolean isOpenNow) {
        this.isOpenNow = isOpenNow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hoursType);
        dest.writeTypedList(open);
        dest.writeByte((byte) (isOpenNow ? 1 : 0));
    }
}
