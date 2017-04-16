package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jsaluja on 4/8/2017.
 */
@ParseClassName("EventLogoOriginal")
public class Original extends ParseObject implements Parcelable {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;

    public Original() {
        //required empty default constructor
    }

    protected Original(Parcel in) {
        url = in.readString();
        this.initializeForParse();
    }

    public void updateWithExisting(Original instance) {
        if (instance.url != null) {
            this.put("url", instance.url);
        }
    }

    public void initializeForParse() {
        if (url != null){
            this.put("url", url);
        }
    }

    public void initializeFromParse() {
        if (has("url")) {
            url = getString("url");
        }
    }

    public static final Creator<Original> CREATOR = new Creator<Original>() {
        @Override
        public Original createFromParcel(Parcel in) {
            return new Original(in);
        }

        @Override
        public Original[] newArray(int size) {
            return new Original[size];
        }
    };

    public String getUrl() {
        if (url == null) {
            url = getString("url");
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.put("url", url);
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }
}
