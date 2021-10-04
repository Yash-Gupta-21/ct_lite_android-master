package com.i9930.croptrails.CommonClasses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClusterResponse {

    @SerializedName("status")
    Integer status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    List<Cluster>clusterList;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Cluster> getClusterList() {
        return clusterList;
    }

    public void setClusterList(List<Cluster> clusterList) {
        this.clusterList = clusterList;
    }
}
