package com.i9930.croptrails.Maps.ShowArea.Model.near;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearBy {
    @SerializedName("farm_id")
    @Expose
    private Integer farmId;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("farmName")
    @Expose
    private String farmName;
    @SerializedName("addLine1")
    @Expose
    private String addLine1;
    @SerializedName("addLine2")
    @Expose
    private String addLine2;
    @SerializedName("farmerName")
    @Expose
    private String farmerName;

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }
}
