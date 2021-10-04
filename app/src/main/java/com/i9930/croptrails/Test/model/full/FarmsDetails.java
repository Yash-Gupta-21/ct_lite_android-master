package com.i9930.croptrails.Test.model.full;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmsDetails {
    @SerializedName("ownership_doc")
    @Expose
    private String ownershipDoc;

    public String getOwnershipDoc() {
        return ownershipDoc;
    }

    public void setOwnershipDoc(String ownershipDoc) {
        this.ownershipDoc = ownershipDoc;
    }

    public String getFarmPhoto() {
        return farmPhoto;
    }

    public void setFarmPhoto(String farmPhoto) {
        this.farmPhoto = farmPhoto;
    }

    @SerializedName("farm_photo")
    @Expose
    private String farmPhoto;

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("farm_master_num")
    @Expose
    private int farmMasterNum;
    @SerializedName("farm_name")
    @Expose
    private String farmName;
    @SerializedName("comp_farm_id")
    @Expose
    private String compFarmId;
    @SerializedName("owner_id")
    @Expose
    private int ownerId;
    @SerializedName("operator_id")
    @Expose
    private int operatorId;
    @SerializedName("cluster_id")
    @Expose
    private int clusterId;
    @SerializedName("comp_id")
    @Expose
    private int compId;
    @SerializedName("season_num")
    @Expose
    private int seasonNum;
    @SerializedName("address_id")
    @Expose
    private int addressId;
    @SerializedName("lat_cord")
    @Expose
    private String latCord;
    @SerializedName("long_cord")
    @Expose
    private String longCord;
    @SerializedName("is_free")
    @Expose
    private int isFree;
    @SerializedName("is_owned")
    @Expose
    private String isOwned;
    @SerializedName("zoom")
    @Expose
    private double zoom;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("added_by")
    @Expose
    private int addedBy;
    @SerializedName("dod")
    @Expose
    private String dod;
    @SerializedName("isSelected")
    @Expose
    private String isSelected;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    @SerializedName("delq_reason")
    @Expose
    private String delqReason;
    @SerializedName("sv_id")
    @Expose
    private int svId;
    @SerializedName("motor_hp")
    @Expose
    private String motorHp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(int farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getCompFarmId() {
        return compFarmId;
    }

    public void setCompFarmId(String compFarmId) {
        this.compFarmId = compFarmId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(int seasonNum) {
        this.seasonNum = seasonNum;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getIsOwned() {
        return isOwned;
    }

    public void setIsOwned(String isOwned) {
        this.isOwned = isOwned;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public int getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(int addedBy) {
        this.addedBy = addedBy;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getDelqReason() {
        return delqReason;
    }

    public void setDelqReason(String delqReason) {
        this.delqReason = delqReason;
    }

    public int getSvId() {
        return svId;
    }

    public void setSvId(int svId) {
        this.svId = svId;
    }

    public String getMotorHp() {
        return motorHp;
    }

    public void setMotorHp(String motorHp) {
        this.motorHp = motorHp;
    }

}