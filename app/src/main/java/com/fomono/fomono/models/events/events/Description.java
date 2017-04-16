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
@ParseClassName("EventDescription")
public class Description extends ParseObject implements Parcelable
{

    @SerializedName("text")
    @Expose
    public Object text;
    @SerializedName("html")
    @Expose
    public Object html;

    public Description() {
        //required empty default constructor
    }

    public final static Parcelable.Creator<Description> CREATOR = new Creator<Description>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Description createFromParcel(Parcel in) {
            Description instance = new Description();
            instance.text = ((Object) in.readValue((Object.class.getClassLoader())));
            instance.html = ((Object) in.readValue((Object.class.getClassLoader())));

            instance.initializeForParse();
            return instance;
        }

        public Description[] newArray(int size) {
            return (new Description[size]);
        }

    }
            ;

    public void updateWithExisting(Description desc) {
        this.put("text", desc.text);
    }

    public void initializeForParse() {
        this.put("text", this.text);
    }

    public void initializeFromParse() {
        text = getString("text");
    }

    public Object getText() {
        if (text == null) {
            text = get("text");
        }
        return text;
    }

    public void setText(Object text) {
        this.text = text;
        this.put("text", text);
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