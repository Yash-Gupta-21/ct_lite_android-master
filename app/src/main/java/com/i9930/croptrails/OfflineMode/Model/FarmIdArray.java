package com.i9930.croptrails.OfflineMode.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmIdArray {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    @SerializedName("comp_id")
    String compId;

    @SerializedName("farm_id")
    List<String>farmIdList;

    public List<String> getFarmIdList() {
        return farmIdList;
    }

    public void setFarmIdList(List<String> farmIdList) {
        this.farmIdList = farmIdList;
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

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }
}
