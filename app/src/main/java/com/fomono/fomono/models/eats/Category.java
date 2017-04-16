package com.fomono.fomono.models.eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.models.ICategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jsaluja on 4/5/2017.
 */

@ParseClassName("BusinessCategory")
public class Category extends ParseObject implements Parcelable, ICategory
{

    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("title")
    @Expose
    private String title;
    public final static Parcelable.Creator<Category> CREATOR = new Creator<Category>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Category createFromParcel(Parcel in) {
            Category instance = new Category();
            instance.alias = ((String) in.readValue((String.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.initializeForParse();
            return instance;
        }

        public Category[] newArray(int size) {
            return (new Category[size]);
        }

    }
            ;

    public void updateWithExisting(Category instance) {
        this.put("alias", instance.alias);
        this.put("title", instance.title);
    }

    public void initializeForParse() {
        this.put("alias", this.alias);
        this.put("title", this.title);
    }

    public void initializeFromParse() {
        this.alias = getString("alias");
        this.title = getString("title");
    }

    public String getAlias() {
        if (alias == null) {
            alias = getString("alias");
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        if (title == null) {
            title = getString("title");
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(alias);
        dest.writeValue(title);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String getName() {
        return getTitle();
    }

    @Override
    public String getId() {
        return getAlias();
    }

    @Override
    public String getApiName() {
        return FomonoApplication.API_NAME_EATS;
    }

    @Override
    public String getParamName() {
        return "categories";
    }
}