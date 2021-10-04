package com.i9930.croptrails.OfflineMode.Model;

import com.google.gson.annotations.SerializedName;

public class TextReply {
    @SerializedName("farmer_reply")
    String farmerReply;

    public String getFarmerReply() {
        return farmerReply;
    }

    public void setFarmerReply(String farmerReply) {
        this.farmerReply = farmerReply;
    }
}
