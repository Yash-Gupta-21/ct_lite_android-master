package com.i9930.croptrails.FarmerInnerDashBoard.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Timeline {

    @SerializedName("dataType")
    @Expose
    private Object dataType;
    @SerializedName("farm_cal_activity_id")
    @Expose
    private Object farmCalActivityId;
    @SerializedName("visit_report_id")
    @Expose
    private String visitReportId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("img_link")
    @Expose
    private Object imgLink;
    @SerializedName("activity")
    @Expose
    private Object activity;
    @SerializedName("activity_type_id")
    @Expose
    private Object activityTypeId;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("priority")
    @Expose
    private Object priority;
    @SerializedName("chemical_id")
    @Expose
    private Object chemicalId;
    @SerializedName("chemical_qty")
    @Expose
    private Object chemicalQty;
    @SerializedName("qty_unit")
    @Expose
    private Object qtyUnit;
    @SerializedName("is_done")
    @Expose
    private Object isDone;
    @SerializedName("farmer_reply")
    @Expose
    private Object farmerReply;
    @SerializedName("activity_type")
    @Expose
    private Object activityType;
    @SerializedName("activity_img")
    @Expose
    private Object activityImg;
    @SerializedName("unit")
    @Expose
    private Object unit;
    @SerializedName("germination")
    @Expose
    private Object germination;
    @SerializedName("population")
    @Expose
    private Object population;
    @SerializedName("spacing_rtr")
    @Expose
    private Object spacingRtr;
    @SerializedName("spacing_ptp")
    @Expose
    private Object spacingPtp;
    @SerializedName("resultType")
    @Expose
    private String resultType;

    public Object getDataType() {
        return dataType;
    }

    public void setDataType(Object dataType) {
        this.dataType = dataType;
    }

    public Object getFarmCalActivityId() {
        return farmCalActivityId;
    }

    public void setFarmCalActivityId(Object farmCalActivityId) {
        this.farmCalActivityId = farmCalActivityId;
    }

    public String getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(String visitReportId) {
        this.visitReportId = visitReportId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public Object getImgLink() {
        return imgLink;
    }

    public void setImgLink(Object imgLink) {
        this.imgLink = imgLink;
    }

    public Object getActivity() {
        return activity;
    }

    public void setActivity(Object activity) {
        this.activity = activity;
    }

    public Object getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Object activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getPriority() {
        return priority;
    }

    public void setPriority(Object priority) {
        this.priority = priority;
    }

    public Object getChemicalId() {
        return chemicalId;
    }

    public void setChemicalId(Object chemicalId) {
        this.chemicalId = chemicalId;
    }

    public Object getChemicalQty() {
        return chemicalQty;
    }

    public void setChemicalQty(Object chemicalQty) {
        this.chemicalQty = chemicalQty;
    }

    public Object getQtyUnit() {
        return qtyUnit;
    }

    public void setQtyUnit(Object qtyUnit) {
        this.qtyUnit = qtyUnit;
    }

    public Object getIsDone() {
        return isDone;
    }

    public void setIsDone(Object isDone) {
        this.isDone = isDone;
    }

    public Object getFarmerReply() {
        return farmerReply;
    }

    public void setFarmerReply(Object farmerReply) {
        this.farmerReply = farmerReply;
    }

    public Object getActivityType() {
        return activityType;
    }

    public void setActivityType(Object activityType) {
        this.activityType = activityType;
    }

    public Object getActivityImg() {
        return activityImg;
    }

    public void setActivityImg(Object activityImg) {
        this.activityImg = activityImg;
    }

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

    public Object getGermination() {
        return germination;
    }

    public void setGermination(Object germination) {
        this.germination = germination;
    }

    public Object getPopulation() {
        return population;
    }

    public void setPopulation(Object population) {
        this.population = population;
    }

    public Object getSpacingRtr() {
        return spacingRtr;
    }

    public void setSpacingRtr(Object spacingRtr) {
        this.spacingRtr = spacingRtr;
    }

    public Object getSpacingPtp() {
        return spacingPtp;
    }

    public void setSpacingPtp(Object spacingPtp) {
        this.spacingPtp = spacingPtp;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
