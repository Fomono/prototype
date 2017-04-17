package com.fomono.fomono.models.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.models.FomonoEvent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Saranu on 4/8/17.
 */
@ParseClassName("Movie")
public class Movie extends ParseObject implements Parcelable, FomonoEvent{

        @SerializedName("poster_path")
        @Expose
        public String posterPath;
        @SerializedName("adult")
        @Expose
        public boolean adult;
        @SerializedName("overview")
        @Expose
        public String overview;
        @SerializedName("release_date")
        @Expose
        public String releaseDate;
        @SerializedName("genre_ids")
        @Expose
        public List<Long> genreIds = null;
        @SerializedName("id")
        @Expose
        public long id = -1;
        @SerializedName("original_title")
        @Expose
        public String originalTitle;
        @SerializedName("original_language")
        @Expose
        public String originalLanguage;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("backdrop_path")
        @Expose
        public String backdropPath;
        @SerializedName("popularity")
        @Expose
        public double popularity;
        @SerializedName("vote_count")
        @Expose
        public long voteCount;
        @SerializedName("video")
        @Expose
        public boolean video;
        @SerializedName("vote_average")
        @Expose
        public double voteAverage;

    public  Movie(){
        //required empty default constructor
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readLong();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readLong();
        video = in.readByte() != 0;
        voteAverage = in.readDouble();

        initializeForParse(this);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Initializes an as a ParseObject that can be saved to db.
     * Note: Only stores data we care about.
     * @param instance
     */
    public static void initializeForParse(Movie instance) {
        if (instance.posterPath != null) {
            instance.put("poster_path", instance.posterPath);
        }
        if (instance.overview != null) {
            instance.put("overview", instance.overview);
        }
        if (instance.releaseDate != null) {
            instance.put("release_date", instance.releaseDate);
        }
        if (instance.id >= 0) {
            instance.put("id", String.valueOf(instance.id));
        }
        if (instance.originalTitle != null) {
            instance.put("original_title", instance.originalTitle);
        }
        if (instance.title != null) {
            instance.put("title", instance.title);
        }
        if (instance.backdropPath != null) {
            instance.put("backdrop_path", instance.backdropPath);
        }
        if (instance.voteAverage >= 0) {
            instance.put("vote_average", instance.voteAverage);
        }
    }

    public static void getListFromParse(List<Movie> list, FindCallback<Movie> callback) {
        ParseQuery<Movie> query = ParseQuery.getQuery(Movie.class);
        List<String> ids = new ArrayList<>();
        for (Movie e : list) {
            ids.add(e.getStringId());
        }
        query.whereContainedIn("id", ids)
                .findInBackground(callback);
    }

    public static void saveOrUpdateFromList(List<Movie> list) {
        getListFromParse(list, new FindCallback<Movie>() {
            @Override
            public void done(List<Movie> objects, ParseException e) {
                if (objects != null) {
                    Map<String, Movie> map = new HashMap<>();
                    for (Movie o : objects) {
                        map.put(o.getStringId(), o);
                    }
                    for (Movie oFromAPI : list) {
                        if (map.containsKey(oFromAPI.getStringId())) {
                            Movie oFromParse = map.get(oFromAPI.getStringId());
                            oFromParse.updateWithExisting(oFromAPI);
                            oFromParse.saveInBackground();
                        } else {
                            initializeForParse(oFromAPI);
                            oFromAPI.saveInBackground();
                        }
                    }
                } else {
                    for (Movie oFromApi : list) {
                        initializeForParse(oFromApi);
                    }
                    ParseObject.saveAllInBackground(list);
                }
            }
        });
    }

    @Override
    public void initializeFromParse() {
        if (has("poster_path")) {
            this.posterPath = getString("poster_path");
        }
        if (has("overview")) {
            this.overview = getString("overview");
        }
        if (has("releaseDate")) {
            this.releaseDate = getString("release_date");
        }
        if (has("id")) {
            this.id = Long.parseLong(getString("id"));
        }
        if (has("original_title")) {
            this.originalTitle = getString("original_title");
        }
        if (has("title")) {
            this.title = getString("title");
        }
        if (has("backdrop_path")) {
            this.backdropPath = getString("backdrop_path");
        }
        if (has("vote_average")) {
            this.voteAverage = getDouble("vote_average");
        }
    }

    public void getFromParse(GetCallback<Movie> callback) {
        ParseQuery<Movie> query = ParseQuery.getQuery(Movie.class);
        query.whereEqualTo("id", this.getStringId())
                .getFirstInBackground(callback);
    }

    public void saveOrUpdate() {
        getFromParse(new GetCallback<Movie>() {
            @Override
            public void done(Movie object, ParseException e) {
                //if it's a new object, save it
                if (object == null) {
                    initializeForParse(Movie.this);
                    Movie.this.saveInBackground();
                } else {
                    //otherwise update any fields, and save the original
                    object.updateWithExisting(Movie.this);
                    object.saveInBackground();
                }
            }
        });
    }

    public void updateWithExisting(Movie instance) {
        if (instance.posterPath != null) {
            this.put("poster_path", instance.posterPath);
        }
        if (instance.overview != null) {
            this.put("overview", instance.overview);
        }
        if (instance.releaseDate != null) {
            this.put("release_date", instance.releaseDate);
        }
        if (instance.id >= 0) {
            this.put("id", String.valueOf(instance.id));
        }
        if (instance.originalTitle != null) {
            this.put("original_title", instance.originalTitle);
        }
        if (instance.title != null) {
            this.put("title", instance.title);
        }
        if (instance.backdropPath != null) {
            this.put("backdrop_path", instance.backdropPath);
        }
        if (instance.voteAverage >= 0) {
            this.put("vote_average", instance.voteAverage);
        }
    }

    public String getPosterPath() {
        if (posterPath == null) {
            posterPath = getString("poster_path");
        }
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        if (overview == null) {
            overview = getString("overview");
        }
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        if (releaseDate == null) {
            releaseDate = getString("release_date");
        }
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public long getId() {
        if (id <= 0) {
            id = Long.parseLong(getString("id"));
        }
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        if (originalTitle == null && has("original_title")) {
            originalTitle = getString("original_title");
        }
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        if (title == null) {
            title = getString("title");
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        if (backdropPath == null) {
            backdropPath = getString("backdrop_path");
        }
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        if (voteAverage == 0) {
            voteAverage = getDouble("vote_average");
        }
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeLong(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeLong(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(voteAverage);
    }

    @Override
    public String getStringId() {
        return String.valueOf(getId());
    }

    @Override
    public String getApiName() {
        return FomonoApplication.API_NAME_MOVIES;
    }
}
