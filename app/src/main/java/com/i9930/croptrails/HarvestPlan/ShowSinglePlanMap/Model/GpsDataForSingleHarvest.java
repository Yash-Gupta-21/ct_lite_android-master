package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hp on 08-09-2018.
 */

public class GpsDataForSingleHarvest {
    private boolean isAllSelected;
    @SerializedName("no_of_bags")
    @Expose
    private String noOfBags;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("lat_cord")
    @Expose
    private String latCord;
    @SerializedName("long_cord")
    @Expose
    private String longCord;

    @SerializedName("farm_name")
    @Expose
    private String farmName;
    @SerializedName("lot_no")
    @Expose
    private String lotNo;
    @SerializedName("pickedup")
    @Expose
    private String pickedup;
    @SerializedName("hcmid")
    @Expose
    private String harvestCollectionMasterId;

    @SerializedName("farm")
    @Expose
    private List<HarvestCollectionDetails> harvestCollectionDetailsList;

    public String getNoOfBags() {
        return noOfBags;
    }

    public void setNoOfBags(String noOfBags) {
        this.noOfBags = noOfBags;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getLatCord() {
        return latCord;
    }

    public void setLatCord(String latCord) {
        this.latCord = latCord;
    }

    public String getLongCord() {
        return longCord;
    }

    public void setLongCord(String longCord) {
        this.longCord = longCord;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getPickedup() {
        return pickedup;
    }

    public void setPickedup(String pickedup) {
        this.pickedup = pickedup;
    }

    public String getHarvestCollectionMasterId() {
        return harvestCollectionMasterId;
    }

    public void setHarvestCollectionMasterId(String harvestCollectionMasterId) {
        this.harvestCollectionMasterId = harvestCollectionMasterId;
    }

    public List<HarvestCollectionDetails> getHarvestCollectionDetailsList() {
        return harvestCollectionDetailsList;
    }

    public void setHarvestCollectionDetailsList(List<HarvestCollectionDetails> harvestCollectionDetailsList) {
        this.harvestCollectionDetailsList = harvestCollectionDetailsList;
    }

    public boolean isAllSelected() {
        return isAllSelected;
    }

    public void setAllSelected(boolean allSelected) {
        isAllSelected = allSelected;
    }
}
