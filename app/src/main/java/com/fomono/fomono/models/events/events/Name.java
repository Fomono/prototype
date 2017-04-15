package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jsaluja on 4/6/2017.
 */
@ParseClassName("EventName")
public class Name extends ParseObject implements Parcelable
{

    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("html")
    @Expose
    public String html;

    public Name() {
        //required empty default constructor
    }

    public final static Parcelable.Creator<Name> CREATOR = new Creator<Name>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Name createFromParcel(Parcel in) {
            Name instance = new Name();
            instance.text = ((String) in.readValue((String.class.getClassLoader())));
            instance.html = ((String) in.readValue((String.class.getClassLoader())));

            instance.initializeForParse();
            return instance;
        }

        public Name[] newArray(int size) {
            return (new Name[size]);
        }

    }
            ;

    public void updateWithExisting(Name instance) {
        this.put("text", instance.text);
    }

    public void initializeForParse() {
        this.put("text", this.text);
    }

    public String getText() {
        if (text == null) {
            text = getString("text");
        }
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.put("text", text);
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(text);
        dest.writeValue(html);
    }

    public int describeContents() {
        return 0;
    }

}