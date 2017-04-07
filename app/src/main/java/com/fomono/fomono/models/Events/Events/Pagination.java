package com.fomono.fomono.models.Events.Events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/6/2017.
 */

public class Pagination implements Parcelable
{

    @SerializedName("page_number")
    @Expose
    private Integer pageNumber;
    @SerializedName("page_size")
    @Expose
    private Integer pageSize;
    @SerializedName("continuation")
    @Expose
    private String continuation;
    public final static Parcelable.Creator<com.fomono.fomono.models.Events.Events.Pagination> CREATOR = new Creator<com.fomono.fomono.models.Events.Events.Pagination>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Pagination createFromParcel(Parcel in) {
            Pagination instance = new Pagination();
            instance.pageNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.pageSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.continuation = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public com.fomono.fomono.models.Events.Events.Pagination[] newArray(int size) {
            return (new com.fomono.fomono.models.Events.Events.Pagination[size]);
        }

    }
            ;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getContinuation() {
        return continuation;
    }

    public void setContinuation(String continuation) {
        this.continuation = continuation;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pageNumber);
        dest.writeValue(pageSize);
        dest.writeValue(continuation);
    }

    public int describeContents() {
        return 0;
    }

}