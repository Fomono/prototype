package com.fomono.fomono.models.Events.UserCredentials;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class Email implements Parcelable
{

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("primary")
    @Expose
    private Boolean primary;
    public final static Parcelable.Creator<Email> CREATOR = new Creator<Email>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Email createFromParcel(Parcel in) {
            Email instance = new Email();
            instance.email = ((String) in.readValue((String.class.getClassLoader())));
            instance.verified = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.primary = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            return instance;
        }

        public Email[] newArray(int size) {
            return (new Email[size]);
        }

    }
            ;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(email);
        dest.writeValue(verified);
        dest.writeValue(primary);
    }

    public int describeContents() {
        return 0;
    }

}