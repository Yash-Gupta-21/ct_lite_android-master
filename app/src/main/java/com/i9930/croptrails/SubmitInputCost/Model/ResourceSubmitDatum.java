package com.i9930.croptrails.SubmitInputCost.Model;

import com.google.gson.annotations.SerializedName;

public class ResourceSubmitDatum {

    @SerializedName("comp_id")
    String compId;
    @SerializedName("farm_id")
    String farmId;
    @SerializedName("resource_type_id")
    String resourceTypeId;
    @SerializedName("value")
    String qty;
    @SerializedName("other_resource_type")
    String otherResourceType;
    @SerializedName("activity_type_id")
    String activityTypeId;
    @SerializedName("used_on")
    String resourceUsedDate;
    @SerializedName("added_by")
    String addedBy;
    @SerializedName("other_unit")
    String otherUnit;
    @SerializedName("other_multiplier")
    String otherMultiplier="0";

    @SerializedName("doa")
    String doaOffline;

    String unit;
    String name;
    String q;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(String resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOtherResourceType() {
        return otherResourceType;
    }

    public void setOtherResourceType(String otherResourceType) {
        this.otherResourceType = otherResourceType;
    }

    public String getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(String activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getResourceUsedDate() {
        return resourceUsedDate;
    }

    public void setResourceUsedDate(String resourceUsedDate) {
        this.resourceUsedDate = resourceUsedDate;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getOtherUnit() {
        return otherUnit;
    }

    public void setOtherUnit(String otherUnit) {
        this.otherUnit = otherUnit;
    }

    public String getOtherMultiplier() {
        return otherMultiplier;
    }

    public void setOtherMultiplier(String otherMultiplier) {
        this.otherMultiplier = otherMultiplier;
    }

    public String getDoaOffline() {
        return doaOffline;
    }

    public void setDoaOffline(String doaOffline) {
        this.doaOffline = doaOffline;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public ResourceSubmitDatum(String compId, String farmId, String resourceTypeId, String qty, String otherResourceType, String activityTypeId, String resourceUsedDate, String addedBy, String otherUnit, String otherMultiplier, String doaOffline, String unit, String name, String q) {
        this.compId = compId;
        this.farmId = farmId;
        this.resourceTypeId = resourceTypeId;
        this.qty = qty;
        this.otherResourceType = otherResourceType;
        this.activityTypeId = activityTypeId;
        this.resourceUsedDate = resourceUsedDate;
        this.addedBy = addedBy;
        this.otherUnit = otherUnit;
        this.otherMultiplier = otherMultiplier;
        this.doaOffline = doaOffline;
        this.unit = unit;
        this.name = name;
        this.q = q;
    }
    public ResourceSubmitDatum() {

    }
}
