package com.fomono.fomono.models.eats;

/**
 * Created by jsaluja on 4/5/2017.
 */

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

@ParseClassName("Business")
public class Business extends ParseObject implements Parcelable, FomonoEvent
{

    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_closed")
    @Expose
    private Boolean isClosed;
    @SerializedName("categories")
    @Expose
    private ArrayList<Category> categories = null;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("transactions")
    @Expose
    private List<String> transactions = null;
    public final static Parcelable.Creator<Business> CREATOR = new Creator<Business>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Business createFromParcel(Parcel in) {
            Business instance = new Business();
            instance.rating = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.price = ((String) in.readValue((String.class.getClassLoader())));
            instance.phone = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.isClosed = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            instance.categories = in.readArrayList(Category.class.getClassLoader());
            instance.reviewCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.coordinates = ((Coordinates) in.readValue((Coordinates.class.getClassLoader())));
            instance.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.location = ((Location) in.readValue((Location.class.getClassLoader())));
            instance.distance = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.transactions = in.readArrayList(java.lang.String.class.getClassLoader());

            initializeForParse(instance);

            return instance;
        }

        public Business[] newArray(int size) {
            return (new Business[size]);
        }

    }
            ;

    /**
     * Initializes an as a ParseObject that can be saved to db.
     * Note: Only stores data we care about.
     * @param instance
     */
    public static void initializeForParse(Business instance) {
        instance.put("rating", instance.rating);
        instance.put("price", instance.price);
        instance.put("id", String.valueOf(instance.id));
        for (Category c : instance.categories) {
            c.initializeForParse();
        }
        instance.put("categories", instance.categories);
        instance.put("review_count", instance.reviewCount);
        instance.put("name", instance.name);
        instance.put("url", instance.url);
        instance.coordinates.initializeForParse();
        instance.put("coordinates", instance.coordinates);
        instance.put("image_url", instance.imageUrl);
        instance.location.initializeForParse();
        instance.put("location", instance.location);
        instance.put("distance", instance.distance);
    }

    public static void getListFromParse(List<Business> list, FindCallback<Business> callback) {
        ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
        List<String> ids = new ArrayList<>();
        for (Business e : list) {
            ids.add(e.getStringId());
        }
        query.whereContainedIn("id", ids)
                .include("categories")
                .include("coordinates")
                .include("location")
                .findInBackground(callback);
    }

    public static void saveOrUpdateFromList(List<Business> list) {
        getListFromParse(list, new FindCallback<Business>() {
            @Override
            public void done(List<Business> objects, ParseException e) {
                if (objects != null) {
                    Map<String, Business> map = new HashMap<>();
                    for (Business o : objects) {
                        map.put(o.getStringId(), o);
                    }
                    for (Business oFromAPI : list) {
                        if (map.containsKey(oFromAPI.getStringId())) {
                            Business oFromParse = map.get(oFromAPI.getStringId());
                            oFromParse.updateWithExisting(oFromAPI);
                            oFromParse.saveInBackground();
                        } else {
                            initializeForParse(oFromAPI);
                            oFromAPI.saveInBackground();
                        }
                    }
                } else {
                    for (Business oFromApi : list) {
                        initializeForParse(oFromApi);
                    }
                    ParseObject.saveAllInBackground(list);
                }
            }
        });
    }

    public void getFromParse(GetCallback<Business> callback) {
        ParseQuery<Business> query = ParseQuery.getQuery(Business.class);
        query.whereEqualTo("id", this.getStringId())
                .include("categories")
                .include("coordinates")
                .include("location")
                .getFirstInBackground(callback);
    }

    public void saveOrUpdate() {
        getFromParse(new GetCallback<Business>() {
            @Override
            public void done(Business object, ParseException e) {
                //if it's a new object, save it
                if (object == null) {
                    initializeForParse(Business.this);
                    Business.this.saveInBackground();
                } else {
                    //otherwise update any fields, and save the original
                    object.updateWithExisting(Business.this);
                    object.saveInBackground();
                }
            }
        });
    }

    public void updateWithExisting(Business instance) {
        this.put("rating", instance.rating);
        this.put("price", instance.price);
        this.put("id", String.valueOf(instance.id));
        this.put("categories", instance.categories);
        this.put("review_count", instance.reviewCount);
        this.put("name", instance.name);
        this.put("url", instance.url);
        ((Coordinates)this.get("coordinates")).updateWithExisting(instance.coordinates);
        this.put("image_url", instance.imageUrl);
        ((Location)this.get("location")).updateWithExisting(instance.location);
        this.put("distance", instance.distance);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        if (id == null) {
            id = getString("id");
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(rating);
        dest.writeValue(price);
        dest.writeValue(phone);
        dest.writeValue(id);
        dest.writeValue(isClosed);
        dest.writeList(categories);
        dest.writeValue(reviewCount);
        dest.writeValue(name);
        dest.writeValue(url);
        dest.writeValue(coordinates);
        dest.writeValue(imageUrl);
        dest.writeValue(location);
        dest.writeValue(distance);
        dest.writeList(transactions);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String getStringId() {
        return getId();
    }

    @Override
    public String getApiName() {
        return FomonoApplication.API_NAME_EATS;
    }
}