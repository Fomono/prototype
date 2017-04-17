package com.fomono.fomono.models.eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Saranu on 4/16/17.
 */
public class Open implements Parcelable {

    @SerializedName("is_overnight")
    @Expose
    private boolean isOvernight;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("day")
    @Expose
    private long day;
    @SerializedName("start")
    @Expose
    private String start;

    protected Open(Parcel in) {
        isOvernight = in.readByte() != 0;
        end = in.readString();
        day = in.readLong();
        start = in.readString();
    }

    public static final Creator<Open> CREATOR = new Creator<Open>() {
        @Override
        public Open createFromParcel(Parcel in) {
            return new Open(in);
        }

        @Override
        public Open[] newArray(int size) {
            return new Open[size];
        }
    };

    public boolean isIsOvernight() {
        return isOvernight;
    }

    public void setIsOvernight(boolean isOvernight) {
        this.isOvernight = isOvernight;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isOvernight ? 1 : 0));
        dest.writeString(end);
        dest.writeLong(day);
        dest.writeString(start);
    }
}