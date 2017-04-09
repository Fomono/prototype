package com.fomono.fomono.models.events.categories;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class Pagination implements Parcelable
{

    @SerializedName("object_count")
    @Expose
    private Integer objectCount;
    @SerializedName("page_number")
    @Expose
    private Integer pageNumber;
    @SerializedName("page_size")
    @Expose
    private Integer pageSize;
    @SerializedName("page_count")
    @Expose
    private Integer pageCount;
    public final static Parcelable.Creator<Pagination> CREATOR = new Creator<Pagination>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Pagination createFromParcel(Parcel in) {
            Pagination instance = new Pagination();
            instance.objectCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.pageNumber = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.pageSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.pageCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public Pagination[] newArray(int size) {
            return (new Pagination[size]);
        }

    }
            ;

    public Integer getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(Integer objectCount) {
        this.objectCount = objectCount;
    }

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

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(objectCount);
        dest.writeValue(pageNumber);
        dest.writeValue(pageSize);
        dest.writeValue(pageCount);
    }

    public int describeContents() {
        return 0;
    }

}