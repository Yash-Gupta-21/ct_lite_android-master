package com.i9930.croptrails.SubmitHealthCard.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendHealthCardData {

    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

    @SerializedName("health_card_num")
    String healthCardNo;
    @SerializedName("sample_collection_date")
    String collectedOnDate;
    @SerializedName("laboratory_name")
    String labName;
    @SerializedName("soil_sample_num")
    String sampleNumber;
    @SerializedName("from_date")
    String validDateFrom;
    @SerializedName("to_date")
    String validDateTo;
    @SerializedName("added_by")
    String addedBy;
    List<HealthCardData>healthCardParamsList;
    String farm_id;
    String []value;
    String []parameter_id;
    String []unit;

    public String getHealthCardNo() {
        return healthCardNo;
    }

    public void setHealthCardNo(String healthCardNo) {
        this.healthCardNo = healthCardNo;
    }

    public String getValidDateFrom() {
        return validDateFrom;
    }

    public void setValidDateFrom(String validDateFrom) {
        this.validDateFrom = validDateFrom;
    }

    public String getValidDateTo() {
        return validDateTo;
    }

    public void setValidDateTo(String validDateTo) {
        this.validDateTo = validDateTo;
    }

    public List<HealthCardData> getHealthCardParamsList() {
        return healthCardParamsList;
    }

    public void setHealthCardParamsList(List<HealthCardData> healthCardParamsList) {
        this.healthCardParamsList = healthCardParamsList;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public String[] getParameter_id() {
        return parameter_id;
    }

    public void setParameter_id(String[] parameter_id) {
        this.parameter_id = parameter_id;
    }

    public String[] getUnit() {
        return unit;
    }

    public void setUnit(String[] unit) {
        this.unit = unit;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getCollectedOnDate() {
        return collectedOnDate;
    }

    public void setCollectedOnDate(String collectedOnDate) {
        this.collectedOnDate = collectedOnDate;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
