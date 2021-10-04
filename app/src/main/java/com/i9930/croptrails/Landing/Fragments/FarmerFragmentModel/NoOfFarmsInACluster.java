package com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoOfFarmsInACluster {
    @SerializedName("count")
    @Expose
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
