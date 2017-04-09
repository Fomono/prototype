package com.fomono.fomono.models.events.categories;

import android.os.Parcel;
import android.os.Parcelable;

import com.fomono.fomono.interfaces.ICategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class Category implements Parcelable, ICategory
{

    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("name_localized")
    @Expose
    private String nameLocalized;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("short_name_localized")
    @Expose
    private String shortNameLocalized;
    public final static Parcelable.Creator<Category> CREATOR = new Creator<Category>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Category createFromParcel(Parcel in) {
            Category instance = new Category();
            instance.resourceUri = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.nameLocalized = ((String) in.readValue((String.class.getClassLoader())));
            instance.shortName = ((String) in.readValue((String.class.getClassLoader())));
            instance.shortNameLocalized = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Category[] newArray(int size) {
            return (new Category[size]);
        }

    }
            ;

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLocalized() {
        return nameLocalized;
    }

    public void setNameLocalized(String nameLocalized) {
        this.nameLocalized = nameLocalized;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortNameLocalized() {
        return shortNameLocalized;
    }

    public void setShortNameLocalized(String shortNameLocalized) {
        this.shortNameLocalized = shortNameLocalized;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(resourceUri);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(nameLocalized);
        dest.writeValue(shortName);
        dest.writeValue(shortNameLocalized);
    }

    public int describeContents() {
        return 0;
    }

}