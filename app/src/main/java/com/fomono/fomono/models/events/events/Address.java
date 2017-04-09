package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Saranu on 4/8/17.
 */

public class Address implements Parcelable{

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
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
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
