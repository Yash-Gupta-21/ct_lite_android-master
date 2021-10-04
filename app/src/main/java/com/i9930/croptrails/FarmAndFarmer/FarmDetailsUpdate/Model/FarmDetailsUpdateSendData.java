package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 07-07-2018.
 */

public class FarmDetailsUpdateSendData {
    String comp_id,farm_id,irrigation_source,irrigation_type,soil_type,previous_crop,sowing_date;
    String exp_harvest_date,exp_flowering_date;

    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getIrrigation_source() {
        return irrigation_source;
    }

    public void setIrrigation_source(String irrigation_source) {
        this.irrigation_source = irrigation_source;
    }

    public String getIrrigation_type() {
        return irrigation_type;
    }

    public void setIrrigation_type(String irrigation_type) {
        this.irrigation_type = irrigation_type;
    }

    public String getSoil_type() {
        return soil_type;
    }

    public void setSoil_type(String soil_type) {
        this.soil_type = soil_type;
    }

    public String getPrevious_crop() {
        return previous_crop;
    }

    public void setPrevious_crop(String previous_crop) {
        this.previous_crop = previous_crop;
    }

    public String getSowing_date() {
        return sowing_date;
    }

    public void setSowing_date(String sowing_date) {
        this.sowing_date = sowing_date;
    }

    public String getExp_harvest_date() {
        return exp_harvest_date;
    }

    public void setExp_harvest_date(String exp_harvest_date) {
        this.exp_harvest_date = exp_harvest_date;
    }

    public String getExp_flowering_date() {
        return exp_flowering_date;
    }

    public void setExp_flowering_date(String exp_flowering_date) {
        this.exp_flowering_date = exp_flowering_date;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
