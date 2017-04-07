package com.fomono.fomono.models.Events.Categories;

/**
 * Created by jsaluja on 4/7/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class EventBriteCategories implements Parcelable
{

    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories = null;
    public final static Parcelable.Creator<EventBriteCategories> CREATOR = new Creator<EventBriteCategories>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EventBriteCategories createFromParcel(Parcel in) {
            EventBriteCategories instance = new EventBriteCategories();
            instance.locale = ((String) in.readValue((String.class.getClassLoader())));
            instance.pagination = ((Pagination) in.readValue((Pagination.class.getClassLoader())));
            in.readList(instance.categories, (com.fomono.fomono.models.Events.Categories.Category.class.getClassLoader()));
            return instance;
        }

        public EventBriteCategories[] newArray(int size) {
            return (new EventBriteCategories[size]);
        }

    }
            ;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(locale);
        dest.writeValue(pagination);
        dest.writeList(categories);
    }

    public int describeContents() {
        return 0;
    }

}

