package com.i9930.croptrails.HarvestCollection.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HarvestCollectionData {
    @SerializedName("harvest_detail_id")
    @Expose
    private String harvestDetailId;
    @SerializedName("harvest_master_id")
    @Expose
    private String harvestMasterId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("bag_no")
    @Expose
    private String bagNo;
    @SerializedName("net_wt")
    @Expose
    private String netWt;
    @SerializedName("gross_wt")
    @Expose
    private String grossWt;
    @SerializedName("is_planned")
    @Expose
    private String isPlanned;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getHarvestDetailId() {
        return harvestDetailId;
    }

    public void setHarvestDetailId(String harvestDetailId) {
        this.harvestDetailId = harvestDetailId;
    }

    public String getHarvestMasterId() {
        return harvestMasterId;
    }

    public void setHarvestMasterId(String harvestMasterId) {
        this.harvestMasterId = harvestMasterId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getBagNo() {
        return bagNo;
    }

    public void setBagNo(String bagNo) {
        this.bagNo = bagNo;
    }

    public String getNetWt() {
        return netWt;
    }

    public void setNetWt(String netWt) {
        this.netWt = netWt;
    }

    public String getGrossWt() {
        return grossWt;
    }

    public void setGrossWt(String grossWt) {
        this.grossWt = grossWt;
    }

    public String getIsPlanned() {
        return isPlanned;
    }

    public void setIsPlanned(String isPlanned) {
        this.isPlanned = isPlanned;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
