package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Saranu on 4/8/17.
 */
@ParseClassName("EventAddress")
public class Address extends ParseObject implements Parcelable{

    @SerializedName("address_1")
    @Expose
    public String address1;
    @SerializedName("address_2")
    @Expose
    public String address2;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("region")
    @Expose
    public String region;
    @SerializedName("postal_code")
    @Expose
    public String postalCode;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("localized_address_display")
    @Expose
    public String localizedAddressDisplay;
    @SerializedName("localized_area_display")
    @Expose
    public String localizedAreaDisplay;
    @SerializedName("localized_multi_line_address_display")
    @Expose
    public List<String> localizedMultiLineAddressDisplay = null;

    public Address() {
        //required empty default constructor
    }

    protected Address(Parcel in) {
        address1 = in.readString();
        address2 = in.readString();
        city = in.readString();
        region = in.readString();
        postalCode = in.readString();
        country = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        localizedAddressDisplay = in.readString();
        localizedAreaDisplay = in.readString();
        localizedMultiLineAddressDisplay = in.createStringArrayList();

        this.initializeForParse();
    }

    public void updateWithExisting(Address address) {
        if (address.address1 != null) {
            this.put("address_1", address.address1);
        }
        if (address.address2 != null) {
            this.put("address_2", address.address2);
        }
        if (address.city != null) {
            this.put("city", address.city);
        }
        if (address.region != null) {
            this.put("region", address.region);
        }
        if (address.postalCode != null) {
            this.put("postal_code", address.postalCode);
        }
        if (address.country != null) {
            this.put("country", address.country);
        }
        if (address.latitude != null) {
            this.put("latitude", address.latitude);
        }
        if (address.longitude != null) {
            this.put("longitude", address.longitude);
        }
    }

    public void initializeForParse() {
        if (address1 != null) {
            this.put("address_1", address1);
        }
        if (address2 != null) {
            this.put("address_2", address2);
        }
        if (city != null) {
            this.put("city", city);
        }
        if (region != null) {
            this.put("region", region);
        }
        if (postalCode != null) {
            this.put("postal_code", postalCode);
        }
        if (country != null) {
            this.put("country", country);
        }
        if (latitude != null) {
            this.put("latitude", latitude);
        }
        if (longitude != null) {
            this.put("longitude", longitude);
        }
    }

    public void initializeFromParse() {
        if (has("address_1")) {
            address1 = getString("address_1");
        }
        if (has("address_2")) {
            address2 = getString("address_2");
        }
        if (has("city")) {
            city = getString("city");
        }
        if (has("region")) {
            region = getString("region");
        }
        if (has("postal_code")) {
            postalCode = getString("postal_code");
        }
        if (has("country")) {
            country = getString("country");
        }
        if (has("latitude")) {
            latitude = getString("latitude");
        }
        if (has("longitutde")) {
            longitude = getString("longitude");
        }
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getAddress1() {
        if (address1 == null) {
            address1 = getString("address_1");
        }
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
        this.put("address_1", address1);
    }

    public String getAddress2() {
        if (address2 == null && has("address_2")) {
            address2 = getString("address_2");
        }
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
        this.put("address_2", address2);
    }

    public String getCity() {
        if (city == null) {
            city = getString("city");
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        this.put("city", city);
    }

    public String getRegion() {
        if (region == null) {
            region = getString("region");
        }
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
        this.put("region", region);
    }

    public String getPostalCode() {
        if (postalCode == null) {
            postalCode = getString("postal_code");
        }
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        this.put("postal_code", postalCode);
    }

    public String getCountry() {
        if (country == null) {
            country = getString("country");
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        this.put("country", country);
    }

    public String getLatitude() {
        if (latitude == null) {
            latitude = getString("latitude");
        }
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        this.put("latitude", latitude);
    }

    public String getLongitude() {
        if (longitude == null) {
            longitude = getString("longitude");
        }
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        this.put("longitude", longitude);
    }

    public String getLocalizedAddressDisplay() {
        return localizedAddressDisplay;
    }

    public void setLocalizedAddressDisplay(String localizedAddressDisplay) {
        this.localizedAddressDisplay = localizedAddressDisplay;
    }

    public String getLocalizedAreaDisplay() {
        return localizedAreaDisplay;
    }

    public void setLocalizedAreaDisplay(String localizedAreaDisplay) {
        this.localizedAreaDisplay = localizedAreaDisplay;
    }

    public List<String> getLocalizedMultiLineAddressDisplay() {
        return localizedMultiLineAddressDisplay;
    }

    public void setLocalizedMultiLineAddressDisplay(List<String> localizedMultiLineAddressDisplay) {
        this.localizedMultiLineAddressDisplay = localizedMultiLineAddressDisplay;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(city);
        dest.writeString(region);
        dest.writeString(postalCode);
        dest.writeString(country);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(localizedAddressDisplay);
        dest.writeString(localizedAreaDisplay);
        dest.writeStringList(localizedMultiLineAddressDisplay);
    }
}
