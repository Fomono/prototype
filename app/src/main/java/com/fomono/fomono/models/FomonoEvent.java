package com.fomono.fomono.models;

import android.os.Parcelable;

/**
 * Created by Saranu on 4/8/17.
 */

public interface FomonoEvent extends Parcelable {
    String getStringId();
    String getApiName();
    void initializeFromParse();
}
