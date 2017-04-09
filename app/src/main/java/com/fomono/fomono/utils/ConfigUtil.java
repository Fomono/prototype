package com.fomono.fomono.utils;

/**
 * Created by Saranu on 4/8/17.
 */

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("categorycodes.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }
}