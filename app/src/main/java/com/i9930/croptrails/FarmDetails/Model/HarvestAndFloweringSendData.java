package com.i9930.croptrails.FarmDetails.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 18-07-2018.
 */

public class HarvestAndFloweringSendData {
    String actual_flowering_date;
    String farm_id;
    String actual_harvest_date;
    String sowing_date;
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;


    public String getActual_flowering_date() {
        return actual_flowering_date;
    }

    public void setActual_flowering_date(String actual_flowering_date) {
        this.actual_flowering_date = actual_flowering_date;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getActual_harvest_date() {
        return actual_harvest_date;
    }

    public void setActual_harvest_date(String actual_harvest_date) {
        this.actual_harvest_date = actual_harvest_date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSowing_date() {
        return sowing_date;
    }

    public void setSowing_date(String sowing_date) {
        this.sowing_date = sowing_date;
    }
}
