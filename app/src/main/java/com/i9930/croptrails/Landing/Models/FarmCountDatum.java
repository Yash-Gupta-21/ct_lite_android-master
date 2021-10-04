package com.i9930.croptrails.Landing.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmCountDatum {
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("isSelected")
    @Expose
    private String isSelected;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
