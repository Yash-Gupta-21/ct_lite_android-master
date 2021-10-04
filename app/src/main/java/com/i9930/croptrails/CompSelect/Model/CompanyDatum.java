package com.i9930.croptrails.CompSelect.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyDatum {

    @SerializedName("comp_id")
    String compId;
    @SerializedName("company_name")
    String compName;
    @SerializedName("cluster_id")
    String clusterId;
    @SerializedName("cluster_name")
    String clusterName;
    @SerializedName("role_id")
    String roleId;
    @SerializedName("role")
    String role;

    @SerializedName("date_format ")
    @Expose
    private String dateFormat;
    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public CompanyDatum(String compId, String compName) {
        this.compId = compId;
        this.compName = compName;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
