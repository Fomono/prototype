package com.fomono.fomono.models.events.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/6/2017.
 */

public class Description implements Parcelable
{

    @SerializedName("text")
    @Expose
    public Object text;
    @SerializedName("html")
    @Expose
    public Object html;
    public final static Parcelable.Creator<Description> CREATOR = new Creator<Description>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Description createFromParcel(Parcel in) {
            Description instance = new Description();
            instance.text = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.html = ((Object) in.readValue((Object.class.getClassLoader())));
            return instance;
        }

        public Description[] newArray(int size) {
            return (new Description[size]);
        }

    }
            ;

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public Object getHtml() {
        return html;
    }

    public void setHtml(Object html) {
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