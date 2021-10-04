package com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalExpectedArea {
    @SerializedName("sum")
    @Expose
    private String sum;

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}
