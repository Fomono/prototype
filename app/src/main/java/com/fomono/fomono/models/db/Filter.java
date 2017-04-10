package com.fomono.fomono.models.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by David on 4/9/2017.
 */
@ParseClassName("Filter")
public class Filter extends ParseObject implements Parcelable {

    String paramName;
    String value;
    String apiName;

    public Filter() {
        //required empty default constructor
    }

    public Filter(String paramName, String value, String apiName) {
        this.put("param_name", paramName);
        this.put("value", value);
        this.put("api_name", apiName);
        this.put("user", ParseUser.getCurrentUser());

        this.paramName = paramName;
        this.value = value;
        this.apiName = apiName;
    }

    /**
     * Needs to be called after retrieving objects from db.
     */
    public void initialize() {
        this.paramName = getString("param_name");
        this.value = getString("value");
        this.apiName = getString("apiName");
    }

    public static void initializeFromList(List<Filter> filters) {
        if (filters == null) {
            return;
        }
        for (Filter f : filters) {
            f.initialize();
        }
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
        put("param_name", paramName);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        put("value", value);
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
        put("api_name", apiName);
    }

    private Filter(Parcel in) {
        this.paramName = in.readString();
        this.value = in.readString();
        this.apiName = in.readString();

        this.put("param_name", paramName);
        this.put("value", value);
        this.put("api_name", apiName);
        this.put("user", ParseUser.getCurrentUser());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.paramName);
        parcel.writeString(this.value);
        parcel.writeString(this.apiName);
    }

    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel parcel) {
            return new Filter(parcel);
        }

        @Override
        public Filter[] newArray(int i) {
            return new Filter[i];
        }
    };
}
