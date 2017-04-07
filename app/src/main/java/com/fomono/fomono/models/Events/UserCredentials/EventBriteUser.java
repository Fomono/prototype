package com.fomono.fomono.models.Events.UserCredentials;

import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import android.os.Parcel;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by jsaluja on 4/7/2017.
 */


public class EventBriteUser implements Parcelable
{

    @SerializedName("emails")
    @Expose
    private List<Email> emails = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("image_id")
    @Expose
    private Object imageId;
    public final static Parcelable.Creator<EventBriteUser> CREATOR = new Creator<EventBriteUser>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EventBriteUser createFromParcel(Parcel in) {
            EventBriteUser instance = new EventBriteUser();
            in.readList(instance.emails, (com.fomono.fomono.models.Events.UserCredentials.Email.class.getClassLoader()));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.firstName = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastName = ((String) in.readValue((String.class.getClassLoader())));
            instance.imageId = ((Object) in.readValue((Object.class.getClassLoader())));
            return instance;
        }

        public EventBriteUser[] newArray(int size) {
            return (new EventBriteUser[size]);
        }

    }
            ;

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Object getImageId() {
        return imageId;
    }

    public void setImageId(Object imageId) {
        this.imageId = imageId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(emails);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(imageId);
    }

    public int describeContents() {
        return 0;
    }

}