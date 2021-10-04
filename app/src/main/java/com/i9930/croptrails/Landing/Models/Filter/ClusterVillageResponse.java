package com.i9930.croptrails.Landing.Models.Filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClusterVillageResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<ClusterVillages> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ClusterVillages> getData() {
        return data;
    }

    public void setData(List<ClusterVillages> data) {
        this.data = data;
    }
}
