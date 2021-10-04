package com.i9930.croptrails.ShowInputCost.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceData {
    @SerializedName("compare_type")
    String typeC;
    @SerializedName("farmId")
    @Expose
    private String farmId;

    @SerializedName("resource_id")
    @Expose
    private String resourceId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("farm_master_num")
    @Expose
    private String farmMasterNum;
    @SerializedName("resource_type_id")
    @Expose
    private String resourceTypeId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("other_resource_type")
    @Expose
    private String otherResourceType;
    @SerializedName("activity_type_id")
    @Expose
    private String activityTypeId;
    @SerializedName("used_on")
    @Expose
    private String usedOn;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("multiplier")
    @Expose
    private String multiplier;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("doa")
    @Expose
    private String doa;

    @SerializedName("other_unit")
    @Expose
    private String otherUnit;

    @SerializedName("other_multiplier")
    @Expose
    private String otherMultiplier;
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(String farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public String getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(String resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getUsedOn() {
        return usedOn;
    }

    public void setUsedOn(String usedOn) {
        this.usedOn = usedOn;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(String multiplier) {
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getTypeC() {
        return typeC;
    }

    public void setTypeC(String typeC) {
        this.typeC = typeC;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }
}
