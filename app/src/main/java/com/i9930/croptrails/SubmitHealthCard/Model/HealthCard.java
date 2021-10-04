package com.i9930.croptrails.SubmitHealthCard.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthCard {

    @SerializedName("compare_type")
    String type;

    @SerializedName("soil_card_master_id")
    @Expose
    private String soilCardMasterId;
    @SerializedName("soil_card_num")
    @Expose
    private String soilCardNum;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("health_card_num")
    @Expose
    private String healthCardNum;
    @SerializedName("sample_collection_date")
    @Expose
    private String sampleCollectionDate;
    @SerializedName("laboratory_name")
    @Expose
    private String laboratoryName;
    @SerializedName("soil_sample_num")
    @Expose
    private String soilSampleNum;
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("parameter")
    @Expose
    private List<CardParameters> parameter = null;

    int index;

    public String getSoilCardMasterId() {
        return soilCardMasterId;
    }

    public void setSoilCardMasterId(String soilCardMasterId) {
        this.soilCardMasterId = soilCardMasterId;
    }

    public String getSoilCardNum() {
        return soilCardNum;
    }

    public void setSoilCardNum(String soilCardNum) {
        this.soilCardNum = soilCardNum;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getHealthCardNum() {
        return healthCardNum;
    }

    public void setHealthCardNum(String healthCardNum) {
        this.healthCardNum = healthCardNum;
    }

    public String getSampleCollectionDate() {
        return sampleCollectionDate;
    }

    public void setSampleCollectionDate(String sampleCollectionDate) {
        this.sampleCollectionDate = sampleCollectionDate;
    }

    public String getLaboratoryName() {
        return laboratoryName;
    }

    public void setLaboratoryName(String laboratoryName) {
        this.laboratoryName = laboratoryName;
    }

    public String getSoilSampleNum() {
        return soilSampleNum;
    }

    public void setSoilSampleNum(String soilSampleNum) {
        this.soilSampleNum = soilSampleNum;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    public List<CardParameters> getParameter() {
        return parameter;
    }

    public void setParameter(List<CardParameters> parameter) {
        this.parameter = parameter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
