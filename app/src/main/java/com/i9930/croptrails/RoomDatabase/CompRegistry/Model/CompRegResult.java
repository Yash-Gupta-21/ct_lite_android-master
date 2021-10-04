package com.i9930.croptrails.RoomDatabase.CompRegistry.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompRegResult {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("feature_id")
    @Expose
    private String featureId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("is_active")
    @Expose
    private String is_active;
    @SerializedName("feature_name")
    @Expose
    private String featureName;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
