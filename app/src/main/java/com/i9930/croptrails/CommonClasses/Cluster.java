package com.i9930.croptrails.CommonClasses;

import com.google.gson.annotations.SerializedName;

public class Cluster {
    @SerializedName("cluster_id")
    String clusterId;
    @SerializedName("cluster_name")
    String clusterName;

    public Cluster() {
    }

    public Cluster(String clusterId, String clusterName) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
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
}
