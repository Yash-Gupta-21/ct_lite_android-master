package com.i9930.croptrails.Task.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.i9930.croptrails.Utility.AppConstants;

public class TaskDatum {
    int type= AppConstants.SV_TIMELINE_CARD_TYPES.TASK;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comp_id")
    @Expose
    private long compId;
    @SerializedName("farm_id")
    @Expose
    private long farmId;
    @SerializedName("supervisor_id")
    @Expose
    private long supervisorId;
    @SerializedName("assign_date")
    @Expose
    private String assignDate;
    @SerializedName("assign_by")
    @Expose
    private long assignBy;
    @SerializedName("task")
    @Expose
    private String task;
    @SerializedName("is_complete")
    @Expose
    private String isComplete;
    @SerializedName("complete_date")
    @Expose
    private String completeDate;
    @SerializedName("instructions")
    @Expose
    private String instructions;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("comp_farm_id")
    @Expose
    private String compFarmId;

    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCompId() {
        return compId;
    }

    public void setCompId(long compId) {
        this.compId = compId;
    }

    public long getFarmId() {
        return farmId;
    }

    public void setFarmId(long farmId) {
        this.farmId = farmId;
    }

    public long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(long supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public long getAssignBy() {
        return assignBy;
    }

    public void setAssignBy(long assignBy) {
        this.assignBy = assignBy;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getCompFarmId() {
        return compFarmId;
    }

    public void setCompFarmId(String compFarmId) {
        this.compFarmId = compFarmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
