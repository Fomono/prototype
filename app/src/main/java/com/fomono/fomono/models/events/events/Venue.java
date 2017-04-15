package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Saranu on 4/8/17.
 */
@ParseClassName("EventVenue")
public class Venue extends ParseObject implements Parcelable {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("address")
    @Expose
    public Address address;

    public Venue() {
        //required empty default constructor
    }

    protected Venue(Parcel in) {
        name = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());

        initializeForParse();
    }

    public void updateWithExisting(Venue instance) {
        this.put("name", instance.name);
        ((Address)this.get("address")).updateWithExisting(instance.address);
    }

    public void initializeForParse() {
        this.put("name", name);
        address.initializeForParse();
        this.put("address", address);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(address, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    public String getName() {
        if (name == null) {
            name = getString("name");
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.put("name", name);
    }

    public Address getAddress() {
        if (address == null) {
            address = (Address) getParseObject("address");
        }
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        this.put("address", address);
    }

}