package com.i9930.croptrails.CropCycle.Create.Model.FetchType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityType {
    @SerializedName("activity_type_id")
    @Expose
    private Integer activityTypeId;
    @SerializedName("activity_type")
    @Expose
    private String activityType;

    public ActivityType(Integer activityTypeId, String activityType) {
        this.activityTypeId = activityTypeId;
        this.activityType = activityType;
    }

    public ActivityType() {
    }

    public Integer getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public static ActivityType copy(ActivityType type)
    {   if (type!=null)
        return new ActivityType(type.getActivityTypeId(),type.getActivityType());
    else
        return new ActivityType();
    }

}
