package com.i9930.croptrails.OfflineMode.send;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.ShowInputCost.Model.ResourceData;

public class SendOfflineResource {

    @SerializedName("inputs_i")
    List<ResourceData> resourceDataList;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("token")
    @Expose
    private String token;


    public SendOfflineResource(List<ResourceData> resourceDataList,String userId) {
        this.resourceDataList = resourceDataList;
        this.userId=userId;
    }

    public List<ResourceData> getResourceDataList() {
        return resourceDataList;
    }

    public void setResourceDataList(List<ResourceData> resourceDataList) {
        this.resourceDataList = resourceDataList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
