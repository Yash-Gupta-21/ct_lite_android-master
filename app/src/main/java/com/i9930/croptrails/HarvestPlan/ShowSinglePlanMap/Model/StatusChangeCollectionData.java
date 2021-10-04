package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StatusChangeCollectionData {

    List<String>id;
    @SerializedName("dataNEW")
    List<HarvestCollectionDetails>data;
    @SerializedName("user_id")
    String userId;
    String token;

    public StatusChangeCollectionData(List<String> id, List<HarvestCollectionDetails> data, String userId, String token) {
        this.id = id;
        this.data = data;
        this.userId = userId;
        this.token = token;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
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

    public List<HarvestCollectionDetails> getData() {
        return data;
    }

    public void setData(List<HarvestCollectionDetails> data) {
        this.data = data;
    }
}
