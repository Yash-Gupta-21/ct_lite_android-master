package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfflineFarmDatum {

    @SerializedName("farm_id_offline")
    @Expose
    private String farmIdOffline;
    @SerializedName("farm_id")
    @Expose
    private Integer farmId;

    public String getFarmIdOffline() {
        return farmIdOffline;
    }

    public void setFarmIdOffline(String farmIdOffline) {
        this.farmIdOffline = farmIdOffline;
    }

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

}
