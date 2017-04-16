package com.fomono.fomono.models.eats;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jsaluja on 4/5/2017.
 */
@ParseClassName("BusinessLocation")
public class Location extends ParseObject implements Parcelable
{

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("address3")
    @Expose
    private String address3;
    @SerializedName("state")
    @Expose
    private String locationState;
    @SerializedName("address1")
    @Expose
    private String address1;
    @SerializedName("zip_code")
    @Expose
    private String zipCode;
    public final static Parcelable.Creator<Location> CREATOR = new Creator<Location>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Location createFromParcel(Parcel in) {
            Location instance = new Location();
            instance.city = ((String) in.readValue((String.class.getClassLoader())));
            instance.country = ((String) in.readValue((String.class.getClassLoader())));
            instance.address2 = ((String) in.readValue((String.class.getClassLoader())));
            instance.address3 = ((String) in.readValue((String.class.getClassLoader())));
            instance.locationState = ((String) in.readValue((String.class.getClassLoader())));
            instance.address1 = ((String) in.readValue((String.class.getClassLoader())));
            instance.zipCode = ((String) in.readValue((String.class.getClassLoader())));
            instance.initializeForParse();
            return instance;
        }

        public Location[] newArray(int size) {
            return (new Location[size]);
        }

    }
            ;

    public void updateWithExisting(Location instance) {
        if (instance.city != null) {
            this.put("city", instance.city);
        }
        if (instance.country != null) {
            this.put("country", instance.country);
        }
        if (instance.address1 != null) {
            this.put("address1", instance.address1);
        }
        if (instance.address2 != null) {
            this.put("address2", instance.address2);
        }
        if (instance.address3 != null) {
            this.put("address3", instance.address3);
        }
        if (instance.locationState != null) {
            this.put("state", instance.locationState);
        }
        if (instance.zipCode != null) {
            this.put("zip_code", instance.zipCode);
        }
    }

    public void initializeForParse() {
        if (this.city != null) {
            this.put("city", this.city);
        }
        if (this.country != null) {
            this.put("country", this.country);
        }
        if (this.address1 != null) {
            this.put("address1", this.address1);
        }
        if (this.address2 != null) {
            this.put("address2", this.address2);
        }
        if (this.address3 != null) {
            this.put("address3", this.address3);
        }
        if (this.locationState != null) {
            this.put("state", this.locationState);
        }
        if (this.zipCode != null) {
            this.put("zip_code", this.zipCode);
        }
    }

    public void initializeFromParse() {
        if (has("city")) {
            city = getString("city");
        }
        if (has("country")) {
            country = getString("country");
        }
        if (has("address1")) {
            address1 = getString("address1");
        }
        if (has("address2")) {
            address2 = getString("address2");
        }
        if (has("address3")) {
            address3 = getString("address3");
        }
        if (has("state")) {
            locationState = getString("state");
        }
        if (has("zip_code")) {
            zipCode = getString("zip_code");
        }
    }

    public String getCity() {
        if (city == null) {
            city = getString("city");
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        if (country == null) {
            country = getString("country");
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress2() {
        if (address2 == null && has("address2")) {
            address2 = getString("address2");
        }
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        if (address3 == null && has("address3")) {
            address3 = getString("address3");
        }
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getLocationState() {
        if (locationState == null) {
            locationState = getString("state");
        }
        return locationState;
    }

    public void setLocationState(String state) {
        this.locationState = state;
    }

    public String getAddress1() {
        if (address1 == null) {
            address1 = getString("address1");
        }
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getZipCode() {
        if (zipCode == null) {
            zipCode = getString("zip_code");
        }
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(city);
        dest.writeValue(country);
        dest.writeValue(address2);
        dest.writeValue(address3);
        dest.writeValue(locationState);
        dest.writeValue(address1);
        dest.writeValue(zipCode);
    }

    public int describeContents() {
        return 0;
    }

}