package com.i9930.croptrails.SubmitInputCost.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseDataDD {

    @SerializedName("ict_id")
    @Expose
    private String ictId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("activity_type_id")
    @Expose
    private String activityTypeId;
    @SerializedName("other_activity_name")
    @Expose
    private String otherActivityName;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    public String getIctId() {
        return ictId;
    }

    public void setIctId(String ictId) {
        this.ictId = ictId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(String activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getOtherActivityName() {
        return otherActivityName;
    }

    public void setOtherActivityName(String otherActivityName) {
        this.otherActivityName = otherActivityName;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
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

}
