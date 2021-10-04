package com.i9930.croptrails.Test.PldModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.Visit.VisitImageTimeline;
import com.i9930.croptrails.Utility.AppConstants;

public class PldData {
    @SerializedName("compare_type")
    String type= AppConstants.TIMELINE.PLD;
    @SerializedName("pld_id")
    @Expose
    private String pldId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("pld_area")
    @Expose
    private String pldArea;
    @SerializedName("pld_reason_id")
    @Expose
    private String pldReasonId;

    @SerializedName("pld_reason")
    @Expose
    private String pldReason;

    @SerializedName("pld_date")
    @Expose
    private String pldDate;

    @SerializedName("other_reason")
    @Expose
    private String otherReason;
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
    @SerializedName("dod")
    @Expose
    private String dod;

    @SerializedName("isOfflineAdded")
    @Expose
    private String isOfflineAdded;

    @SerializedName("name")
    @Expose
    private String name;

    int index;

    @SerializedName("images")
    @Expose
    private List<VisitImageTimeline> pldImages = null;


    public String getPldId() {
        return pldId;
    }

    public void setPldId(String pldId) {
        this.pldId = pldId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getPldArea() {
        return pldArea;
    }

    public void setPldArea(String pldArea) {
        this.pldArea = pldArea;
    }

    public String getPldReasonId() {
        return pldReasonId;
    }

    public void setPldReasonId(String pldReasonId) {
        this.pldReasonId = pldReasonId;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
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

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getPldReason() {
        return pldReason;
    }

    public void setPldReason(String pldReason) {
        this.pldReason = pldReason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPldDate() {
        return pldDate;
    }

    public void setPldDate(String pldDate) {
        this.pldDate = pldDate;
    }

    public PldData(String type, String pldId, String farmId, String pldArea, String pldReasonId,
                   String pldReason, String pldDate, String otherReason, String addedBy, String doa,
                   String isActive, String deletedBy, String dod,List<VisitImageTimeline>pldImages,String isOfflineAdded,String name) {
        this.type = type;
        this.pldId = pldId;
        this.name=name;
        this.farmId = farmId;
        this.isOfflineAdded=isOfflineAdded;
        this.pldImages=pldImages;
        this.pldArea = pldArea;
        this.pldReasonId = pldReasonId;
        this.pldReason = pldReason;
        this.pldDate = pldDate;
        this.otherReason = otherReason;
        this.addedBy = addedBy;
        this.doa = doa;
        this.isActive = isActive;
        this.deletedBy = deletedBy;
        this.dod = dod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VisitImageTimeline> getPldImages() {
        return pldImages;
    }

    public void setPldImages(List<VisitImageTimeline> pldImages) {
        this.pldImages = pldImages;
    }

    public String getIsOfflineAdded() {
        return isOfflineAdded;
    }

    public void setIsOfflineAdded(String isOfflineAdded) {
        this.isOfflineAdded = isOfflineAdded;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
