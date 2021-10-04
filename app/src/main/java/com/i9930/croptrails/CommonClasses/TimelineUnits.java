package com.i9930.croptrails.CommonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimelineUnits {

    public TimelineUnits() {
    }

    public TimelineUnits(String unit, String unit_id) {
        this.unit = unit;
        this.unit_id = unit_id;
    }

    @SerializedName("unit")
    @Expose
    String unit;

    @SerializedName("unit_id")
    @Expose
    String unit_id;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }
}

