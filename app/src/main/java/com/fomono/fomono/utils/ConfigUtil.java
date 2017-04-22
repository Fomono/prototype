package com.fomono.fomono.utils;

/**
 * Created by Saranu on 4/8/17.
 */

import android.content.Context;
import android.content.res.AssetManager;

import com.fomono.fomono.FomonoApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigUtil {

    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("eventbritecategories.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }

    public static String getCategoryName(String categoryId, String apiName, Context context) {
        String result = "";
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            String fileName = getPropertiesFileName(apiName);
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
            result = properties.getProperty(categoryId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Set<Map.Entry<String, String>> getCategories(String apiName, Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        String fileName = getPropertiesFileName(apiName);
        InputStream inputStream = assetManager.open(fileName);
        properties.load(inputStream);
        return (Set<Map.Entry<String, String>>)(Set<?>)properties.entrySet();
    }

    public static Properties getCategoriesMap(String apiName, Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        String fileName = getPropertiesFileName(apiName);
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getPropertiesFileName(String apiName) {
        String fileName = "";
        switch (apiName) {
            case FomonoApplication.API_NAME_EVENTS:
                fileName = "eventbritecategories.properties";
                break;
            case FomonoApplication.API_NAME_EATS:
                fileName = "yelpcategories.properties";
                break;
            case FomonoApplication.API_NAME_MOVIE_GENRE:
                fileName = "moviedbgenres.properties";
                break;
            case FomonoApplication.API_NAME_MOVIE_LANGUAGE:
                fileName = "languagecodesmoviedb.properties";
                break;
            case FomonoApplication.API_NAME_MOVIES:
            default:
                break;
        }
        return fileName;
    }
}