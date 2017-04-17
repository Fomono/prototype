package com.fomono.fomono.models.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Saranu on 4/14/17.
 */

public class User implements Parcelable{

    public static final String LOC_PERM_SEEN = "seenLocPerm";

    String firstName;
    String lastName;
    String email;
    long phonenNumber;
    String gender;
    String aboutMe;
    String location;
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhonenNumber() {
        return phonenNumber;
    }

    public void setPhonenNumber(long phonenNumber) {
        this.phonenNumber = phonenNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeLong(this.phonenNumber);
        dest.writeString(this.gender);
        dest.writeString(this.aboutMe);
        dest.writeString(this.location);
        dest.writeString(this.imageUrl);
    }

    protected User(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.phonenNumber = in.readLong();
        this.gender = in.readString();
        this.aboutMe = in.readString();
        this.location = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
