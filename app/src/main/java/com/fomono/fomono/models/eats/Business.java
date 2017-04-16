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
        if (instance.rating >= 0) {
            instance.put("rating", instance.rating);
        }
        if (instance.price != null) {
            instance.put("price", instance.price);
        }
        instance.put("id", String.valueOf(instance.id));
        if (instance.categories != null) {
            for (Category c : instance.categories) {
                c.initializeForParse();
            }
            instance.put("categories", instance.categories);
        }
        if (instance.reviewCount != null) {
            instance.put("review_count", instance.reviewCount);
        }
        if (instance.name != null) {
            instance.put("name", instance.name);
        }
        if (instance.url != null) {
            instance.put("url", instance.url);
        }
        if (instance.coordinates != null) {
            instance.coordinates.initializeForParse();
            instance.put("coordinates", instance.coordinates);
        }
        if (instance.imageUrl != null) {
            instance.put("image_url", instance.imageUrl);
        }
        if (instance.location != null) {
            instance.location.initializeForParse();
            instance.put("location", instance.location);
        }
        if (instance.distance != null) {
            instance.put("distance", instance.distance);
        }
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
        if (instance.rating >= 0) {
            this.put("rating", instance.rating);
        }
        if (instance.price != null) {
            this.put("price", instance.price);
        }
        this.put("id", String.valueOf(instance.id));
        if (instance.categories != null) {
            this.mergeCategoriesForParse(instance.categories);
        }
        if (instance.reviewCount != null) {
            this.put("review_count", instance.reviewCount);
        }
        if (instance.name != null) {
            this.put("name", instance.name);
        }
        if (instance.url != null) {
            this.put("url", instance.url);
        }
        if (instance.coordinates != null) {
            ((Coordinates) this.get("coordinates")).updateWithExisting(instance.coordinates);
        }
        if (instance.imageUrl != null) {
            this.put("image_url", instance.imageUrl);
        }
        if (instance.location != null) {
            ((Location) this.get("location")).updateWithExisting(instance.location);
        }
        if (instance.distance != null) {
            this.put("distance", instance.distance);
        }
    }

    private void mergeCategoriesForParse(List<Category> categories) {
        //merging categories from api to our server
        Map<String, Category> apiCatsMap = new HashMap<>();
        for (Category c : categories) {
            apiCatsMap.put(c.getAlias(), c);
        }
        List<Category> finalCats = new ArrayList<>();
        //update existing categories in parse with new data from api, ignoring ones that have been removed
        List<Category> parseCats = (List<Category>)(List<?>)getList("categories");
        for (Category parseCat : parseCats) {
            String alias = parseCat.getAlias();
            if (apiCatsMap.containsKey(alias)) {
                parseCat.updateWithExisting(apiCatsMap.get(alias));
                finalCats.add(parseCat);
            }
        }
        //now add any new categories from api
        Map<String, Category> parseCatsMap = new HashMap<>();
        for (Category c : finalCats) {
            parseCatsMap.put(c.getAlias(), c);
        }
        for (Category c : categories) {
            String alias = c.getAlias();
            if (!parseCatsMap.containsKey(alias)) {
                finalCats.add(c);
            }
        }
        this.put("categories", finalCats);
    }

    @Override
    public void initializeFromParse() {
        if (has("rating")) {
            this.rating = getDouble("rating");
        }
        if (has("price")) {
            this.price = getString("price");
        }
        this.id = getString("id");
        if (has("categories")) {
            this.categories = (ArrayList<Category>) (List<?>) getList("categories");
            for (Category c : categories) {
                c.initializeFromParse();
            }
        }
        if (has("review_count")) {
            this.reviewCount = getInt("review_count");
        }
        if (has("name")) {
            this.name = getString("name");
        }
        if (has("url")) {
            this.url = getString("url");
        }
        if (has("coordinates")) {
            this.coordinates = (Coordinates) getParseObject("coordinates");
            this.coordinates.initializeFromParse();
        }
        if (has("image_url")) {
            this.imageUrl = getString("image_url");
        }
        if (has("location")) {
            this.location = (Location) getParseObject("location");
            this.location.initializeFromParse();
        }
        if (has("distance")) {
            this.distance = getDouble("distance");
        }
    }

    public double getRating() {
        if (rating == 0) {
            rating = getDouble("rating");
        }
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPrice() {
        if (price == null) {
            price = getString("price");
        }
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
        if (categories == null) {
            categories = (ArrayList<Category>)(List<?>)getList("categories");
        }
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Integer getReviewCount() {
        if (reviewCount == null) {
            reviewCount = getInt("review_count");
        }
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getName() {
        if (name == null) {
            name = getString("name");
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        if (url == null) {
            url = getString("url");
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Coordinates getCoordinates() {
        if (coordinates == null) {
            coordinates = (Coordinates) getParseObject("coordinates");
        }
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getImageUrl() {
        if (imageUrl == null) {
            imageUrl = getString("image_url");
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Location getLocation() {
        if (location == null) {
            location = (Location) getParseObject("location");
        }
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getDistance() {
        if (distance == null) {
            distance = getDouble("distance");
        }
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