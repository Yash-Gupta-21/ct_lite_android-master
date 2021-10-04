package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.SerializedName;

public class SupervisorDatum {
    @SerializedName("cluster_id")
    String clusterId;
    @SerializedName("name")
    String name;
    @SerializedName("pid")
    String personId;
    @SerializedName("user_id")
    String userId;

    @SerializedName("role")
    String role;
    @SerializedName("mobile")
    String mobile;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public SupervisorDatum(String name, String personId, String userId) {
        this.name = name;
        this.personId = personId;
        this.userId = userId;
    }

    public SupervisorDatum() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
