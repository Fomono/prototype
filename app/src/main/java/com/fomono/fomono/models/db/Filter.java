package com.fomono.fomono.models.db;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by David on 4/9/2017.
 */
@ParseClassName("Filter")
public class Filter extends ParseObject {

    public Filter() {
        //required empty default constructor
    }

    public Filter(String paramName, String value, String apiName) {
        this.put("param_name", paramName);
        this.put("value", value);
        this.put("api_name", apiName);
        this.put("user", ParseUser.getCurrentUser());
    }

    public Filter(String paramName, String value, String apiName, ParseUser user) {
        this.put("param_name", paramName);
        this.put("value", value);
        this.put("api_name", apiName);
        this.put("user", user);
    }

    public String getParamName() {
        return getString("param_name");
    }

    public void setParamName(String paramName) {
        put("param_name", paramName);
    }

    public String getValue() {
        return getString("value");
    }

    public void setValue(String value) {
        put("value", value);
    }

    public String getApiName() {
        return getString("api_name");
    }

    public void setApiName(String apiName) {
        put("api_name", apiName);
    }
}
