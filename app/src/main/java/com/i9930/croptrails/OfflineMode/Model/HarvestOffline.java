package com.i9930.croptrails.OfflineMode.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;

public class HarvestOffline {

    @SerializedName("farm_id")
    @Expose
    private String farmId;

    @SerializedName("harvest_master")
    @Expose
    private List<HarvestDetailMaster> harvestMaster = null;

    @SerializedName("harvest_details")
    @Expose
    private List<List<HarvestDetailInnerData>> harvestDetails = null;

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public List<HarvestDetailMaster> getHarvestMaster() {
        return harvestMaster;
    }

    public void setHarvestMaster(List<HarvestDetailMaster> harvestMaster) {
        this.harvestMaster = harvestMaster;
    }

    public List<List<HarvestDetailInnerData>> getHarvestDetails() {
        return harvestDetails;
    }

    public void setHarvestDetails(List<List<HarvestDetailInnerData>> harvestDetails) {
        this.harvestDetails = harvestDetails;
    }
}
