package com.i9930.croptrails.Maps.ShowArea.Model.near;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GpsDatum {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("near_by_farm_ids")
    @Expose
    private String nearByFarmIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNearByFarmIds() {
        return nearByFarmIds;
    }

    public void setNearByFarmIds(String nearByFarmIds) {
        this.nearByFarmIds = nearByFarmIds;
    }
}
