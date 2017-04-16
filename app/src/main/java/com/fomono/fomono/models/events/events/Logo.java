package com.fomono.fomono.models.events.events;

/**
 * Created by jsaluja on 4/8/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("EventLogo")
public class Logo extends ParseObject implements Parcelable {

    @SerializedName("crop_mask")
    @Expose
    public CropMask cropMask;
    @SerializedName("original")
    @Expose
    public Original original;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("aspect_ratio")
    @Expose
    public String aspectRatio;
    @SerializedName("edge_color")
    @Expose
    public String edgeColor;
    @SerializedName("edge_color_set")
    @Expose
    public Boolean edgeColorSet;

    public Logo() {
        //required empty default constructor
    }

    protected Logo(Parcel in) {
        cropMask = in.readParcelable(CropMask.class.getClassLoader());
        original = in.readParcelable(Original.class.getClassLoader());
        id = in.readString();
        url = in.readString();
        aspectRatio = in.readString();
        edgeColor = in.readString();

        this.initializeForParse();
    }

    public void updateWithExisting(Logo instance) {
        ((Original) this.get("original")).updateWithExisting(instance.original);
        this.put("id", instance.id);
        this.put("url", instance.url);
    }

    public void initializeForParse() {
        original.initializeForParse();
        this.put("original", original);
        this.put("id", id);
        this.put("url", url);
    }

    public void initializeFromParse() {
        original = (Original) getParseObject("original");
        original.initializeFromParse();
        id = getString("id");
        url = getString("url");
    }

    public static final Creator<Logo> CREATOR = new Creator<Logo>() {
        @Override
        public Logo createFromParcel(Parcel in) {
            return new Logo(in);
        }

        @Override
        public Logo[] newArray(int size) {
            return new Logo[size];
        }
    };

    public CropMask getCropMask() {
        return cropMask;
    }

    public void setCropMask(CropMask cropMask) {
        this.cropMask = cropMask;
    }

    public Original getOriginal() {
        if (original == null) {
            original = (Original) getParseObject("original");
        }
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
        this.put("original", original);
    }

    public String getId() {
        if (id == null) {
            id = getString("id");
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.put("id", id);
    }

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

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getEdgeColor() {
        return edgeColor;
    }

    public void setEdgeColor(String edgeColor) {
        this.edgeColor = edgeColor;
    }

    public Boolean getEdgeColorSet() {
        return edgeColorSet;
    }

    public void setEdgeColorSet(Boolean edgeColorSet) {
        this.edgeColorSet = edgeColorSet;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(cropMask, flags);
        dest.writeParcelable(original, flags);
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(aspectRatio);
        dest.writeString(edgeColor);
    }
}


