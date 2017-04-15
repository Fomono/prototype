package com.fomono.fomono.models.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Saranu on 4/14/17.
 */

public class User implements Parcelable{

    public String firstName;
    public String lastName;
    public String email;
    public long phonenNumber;
    public String gender;
    public String aboutMe;
    public String location;

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

    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phonenNumber = in.readLong();
        gender = in.readString();
        aboutMe = in.readString();
        location = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeLong(phonenNumber);
        dest.writeString(gender);
        dest.writeString(aboutMe);
        dest.writeString(location);
    }
}
