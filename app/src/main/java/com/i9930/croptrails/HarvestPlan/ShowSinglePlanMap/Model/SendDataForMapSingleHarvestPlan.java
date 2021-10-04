package com.i9930.croptrails.HarvestPlan.ShowSinglePlanMap.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 08-09-2018.
 */

public class SendDataForMapSingleHarvestPlan {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String hcmid;

    public String getHcmid() {
        return hcmid;
    }

    public void setHcmid(String hcmid) {
        this.hcmid = hcmid;
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
