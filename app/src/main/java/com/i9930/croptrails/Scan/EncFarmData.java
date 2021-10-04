package com.i9930.croptrails.Scan;

import com.google.gson.annotations.SerializedName;

public class EncFarmData {
    @SerializedName("farm_id")
    String farmId;
    @SerializedName("sv_id")
    Long svId;
    @SerializedName("comp_id")
    Long compId;
    @SerializedName("owner_id")
    Long ownerId;

    @SerializedName("isSelected")
    String isSelected;
    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public Long getSvId() {
        return svId;
    }

    public void setSvId(Long svId) {
        this.svId = svId;
    }

    public Long getCompId() {
        return compId;
    }

    public void setCompId(Long compId) {
        this.compId = compId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
