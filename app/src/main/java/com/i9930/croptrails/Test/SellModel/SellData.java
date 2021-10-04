package com.i9930.croptrails.Test.SellModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.i9930.croptrails.Utility.AppConstants;

public class SellData {

    int index;
    @SerializedName("compare_type")
    String type= AppConstants.TIMELINE.SELL;

    @SerializedName("harvest_sale_id")
    @Expose
    private String harvestSaleId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;

    @SerializedName("season_id")
    @Expose
    private String seasonId;
    @SerializedName("sale_qty")
    @Expose
    private String saleQty;

    @SerializedName("rate")
    @Expose
    private String rate;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("sold_on")
    @Expose
    private String soldOn;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    @SerializedName("name")
    @Expose
    private String name;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHarvestSaleId() {
        return harvestSaleId;
    }

    public void setHarvestSaleId(String harvestSaleId) {
        this.harvestSaleId = harvestSaleId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(String saleQty) {
        this.saleQty = saleQty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSoldOn() {
        return soldOn;
    }

    public void setSoldOn(String soldOn) {
        this.soldOn = soldOn;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
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

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
