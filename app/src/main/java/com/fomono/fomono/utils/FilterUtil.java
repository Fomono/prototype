package com.fomono.fomono.utils;

import com.fomono.fomono.models.db.Filter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by David on 4/9/2017.
 */

public class FilterUtil {

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
    public static void getFilters(String apiName, FindCallback<Filter> callback) throws Exception {
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
    public static void getFilter(String paramName, String value, String apiName, GetCallback<Filter> callback) throws Exception {
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

    public static void getAvailableCategories(String apiName) {
        
    }
}
