package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HarvestCollectionDetails {
    //For changing status and inserting in produce sell
    @SerializedName("sale_qty")
    @Expose
    private String saleQty;

    @SerializedName("id")
    @Expose
    private String harvestDetailId;
    @SerializedName("hcmid")
    @Expose
    private String noOfBags;
    @SerializedName("no_of_bags")
    @Expose
    private String harvestCollectionMasterId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("harvest_detail_id")
    @Expose
    private String harvestId;
    @SerializedName("pickedup")
    @Expose
    private String pickedup;
    @SerializedName("arrived")
    @Expose
    private String arrived;
    @SerializedName("doa")
    @Expose
    private String doa;

    @SerializedName("bag_no")
    @Expose
    private String bagNo;

    @SerializedName("net_wt")
    @Expose
    private String netWeight;

    @SerializedName("gross_wt")
    @Expose
    private String grossWeight;

    @SerializedName("is_active")
    @Expose
    private String is_active;

    private boolean isPickedUp;
    private boolean isAlreadyPickedUp;
    private boolean isSelfClicked;


    public String getHarvestDetailId() {
        return harvestDetailId;
    }

    public void setHarvestDetailId(String harvestDetailId) {
        this.harvestDetailId = harvestDetailId;
    }

    public String getNoOfBags() {
        return noOfBags;
    }

    public void setNoOfBags(String noOfBags) {
        this.noOfBags = noOfBags;
    }

    public String getHarvestCollectionMasterId() {
        return harvestCollectionMasterId;
    }

    public void setHarvestCollectionMasterId(String harvestCollectionMasterId) {
        this.harvestCollectionMasterId = harvestCollectionMasterId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(String harvestId) {
        this.harvestId = harvestId;
    }

    public String getPickedup() {
        return pickedup;
    }

    public void setPickedup(String pickedup) {
        this.pickedup = pickedup;
    }

    public String getArrived() {
        return arrived;
    }

    public void setArrived(String arrived) {
        this.arrived = arrived;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    public String getBagNo() {
        return bagNo;
    }

    public void setBagNo(String bagNo) {
        this.bagNo = bagNo;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public boolean isAlreadyPickedUp() {
        return isAlreadyPickedUp;
    }

    public void setAlreadyPickedUp(boolean alreadyPickedUp) {
        isAlreadyPickedUp = alreadyPickedUp;
    }

    public boolean isSelfClicked() {
        return isSelfClicked;
    }

    public void setSelfClicked(boolean selfClicked) {
        isSelfClicked = selfClicked;
    }

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }
}
