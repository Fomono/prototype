package com.fomono.fomono.models.events.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jsaluja on 4/8/2017.
 */

public class TopLeft {

    @SerializedName("x")
    @Expose
    private Integer x;
    @SerializedName("y")
    @Expose
    private Integer y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}