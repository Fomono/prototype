package com.fomono.fomono.utils;

import android.content.Context;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.models.ICategory;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.models.events.categories.Category;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by David on 4/9/2017.
 */

public class FilterUtil {

    private static FilterUtil instance;
    public static FilterUtil getInstance() {
        if (instance == null) {
            instance = new FilterUtil();
        }
        return instance;
    }

    private boolean isDirty;

    private FilterUtil() {
        isDirty = false;
    }

    /**
     * Gets all filters for a given api, for current user.
     * Callback format:
     * new FindCallback<Filter>() {
            @Override
            public void done(List<Filter> results, ParseException e) {
                for (Filter a : results) {
                    // ...
                }
            }
        }
     * @param apiName
     * @param callback
     * @throws Exception
     */
    public void getFilters(String apiName, FindCallback<Filter> callback) throws Exception {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            throw new Exception("Error getting user filters: Current user is null.");
        }
        ParseQuery<Filter> query = ParseQuery.getQuery(Filter.class);
        query.whereEqualTo("api_name", apiName);
        query.whereEqualTo("user", user);
        query.findInBackground(callback);
    }

    /**
     * Gets a specific filter for current user.
     * Callback format:
     * new GetCallback<Filter>() {
            @Override
             public void done(Filter object, ParseException e) {
                 if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                 } else {
                    Log.d("score", "Retrieved the object.");
                 }
             }
        }
     * @param paramName
     * @param value
     * @param apiName
     * @param callback
     * @throws Exception
     */
    public void getFilter(String paramName, String value, String apiName, GetCallback<Filter> callback) throws Exception {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            throw new Exception("Error getting specific filter: Current user is null.");
        }
        ParseQuery<Filter> query = ParseQuery.getQuery(Filter.class);
        query.whereEqualTo("api_name", apiName);
        query.whereEqualTo("param_name", paramName);
        query.whereEqualTo("value", value);
        query.whereEqualTo("user", user);
        query.getFirstInBackground(callback);
    }

    /**
     * Gets list of ICategory objects that can be used to populate views.
     * @param apiName
     * @param context
     * @return
     */
    public List<ICategory> getCategories(String apiName, Context context) {
        List<ICategory> categories = new ArrayList<>();
        //get list of event categories
        try {
            Set<Map.Entry<String, String>> catSet = ConfigUtil.getCategories(apiName, context);
            switch (apiName) {
                case FomonoApplication.API_NAME_EVENTS:
                    for (Map.Entry<String, String> pair : catSet) {
                        Category category = new Category();
                        category.setId(pair.getKey());
                        category.setName(pair.getValue());
                        categories.add(category);
                    }
                    break;
                case FomonoApplication.API_NAME_EATS:
                    for (Map.Entry<String, String> pair : catSet) {
                        com.fomono.fomono.models.eats.Category category = new com.fomono.fomono.models.eats.Category();
                        category.setAlias(pair.getKey());
                        category.setTitle(pair.getValue());
                        categories.add(category);
                    }
                    break;
                case FomonoApplication.API_NAME_MOVIES:
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public String buildCategoriesString(List<Filter> filters) {
        StringBuilder sb = new StringBuilder();
        if (filters != null) {
            for (int i = 0; i < filters.size(); i++) {
                Filter f = filters.get(i);
                sb.append(f.getValue());
                if (i < filters.size() - 1) {
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Gets all filters regardless of what API they apply to.
     * @param fromLocal
     * @param callback
     * @throws Exception
     */
    public void getAllFilters(boolean fromLocal, FindCallback<Filter> callback) throws Exception {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            throw new Exception("Error getting user filters: Current user is null.");
        }
        ParseQuery<Filter> query = ParseQuery.getQuery(Filter.class);
        query.whereEqualTo("user", user);
        if (fromLocal) {
            query.fromLocalDatastore();
        }
        query.findInBackground(callback);
    }

    public void addFilter(String paramName, String value, String apiName, GetCallback<Filter> callback) {
        try {
            FilterUtil.getInstance().getFilter(paramName, value, apiName, new GetCallback<Filter>() {
                @Override
                public void done(Filter object, ParseException e) {
                    if (object == null) {
                        Filter f = new Filter(paramName, value, apiName);
                        f.pinInBackground();    //save locally on device for easy access
                        f.saveInBackground();
                        callback.done(f, null);
                        setDirty();
                    } else {
                        callback.done(object, null);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFilter(String paramName, String value, String apiName) {
        try {
            FilterUtil.getInstance().getFilter(paramName, value, apiName, new GetCallback<Filter>() {
                @Override
                public void done(Filter object, ParseException e) {
                    if (object != null) {
                        object.unpinInBackground();
                        object.deleteInBackground();
                        setDirty();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty() {
        isDirty = true;
    }

    public void clearDirty() {
        isDirty = false;
    }
}
