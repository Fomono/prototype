package com.fomono.fomono.models.movies;

import android.graphics.Movie;
import android.media.midi.MidiOutputPort;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsaluja on 4/8/2017.
 */

public class MovieDbResponse implements Parcelable
{

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private ArrayList<Result> results = null;
    public final static Parcelable.Creator<MovieDbResponse> CREATOR = new Creator<MovieDbResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieDbResponse createFromParcel(Parcel in) {
            MovieDbResponse instance = new MovieDbResponse();
            instance.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.results, (Movie.class.getClassLoader()));
            return instance;
        }

        public MovieDbResponse[] newArray(int size) {
            return (new MovieDbResponse[size]);
        }

    }
            ;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page);
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}