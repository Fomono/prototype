package com.fomono.fomono.models;

import android.os.Parcelable;

/**
 * Created by David on 4/8/2017.
 */

public interface ICategory extends Parcelable {
    String getName();
    String getId();
    String getApiName();
    String getParamName();
}
