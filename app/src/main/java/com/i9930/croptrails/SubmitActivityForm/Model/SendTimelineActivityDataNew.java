package com.i9930.croptrails.SubmitActivityForm.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;

public class SendTimelineActivityDataNew {
    List<AgriInput> agri_input_data;
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String farmer_reply;
    String is_done;
    String farm_cal_activity_id;
    String farm_id;
    String comp_id;
    String[] imageList;
    public SendTimelineActivityDataNew() {
    }
    public SendTimelineActivityDataNew(String userId, String token, String farmer_reply, String is_done, String farm_cal_activity_id, String farm_id, String[] imageList) {
        this.userId = userId;
        this.token = token;
        this.farmer_reply = farmer_reply;
        this.is_done = is_done;
        this.farm_cal_activity_id = farm_cal_activity_id;
        this.farm_id = farm_id;
        this.imageList = imageList;
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

    public String getFarmer_reply() {
        return farmer_reply;
    }

    public void setFarmer_reply(String farmer_reply) {
        this.farmer_reply = farmer_reply;
    }

    public String getIs_done() {
        return is_done;
    }

    public void setIs_done(String is_done) {
        this.is_done = is_done;
    }

    public String getFarm_cal_activity_id() {
        return farm_cal_activity_id;
    }

    public void setFarm_cal_activity_id(String farm_cal_activity_id) {
        this.farm_cal_activity_id = farm_cal_activity_id;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String[] getImageList() {
        return imageList;
    }

    public void setImageList(String[] imageList) {
        this.imageList = imageList;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public List<AgriInput> getAgri_input_data() {
        return agri_input_data;
    }

    public void setAgri_input_data(List<AgriInput> agri_input_data) {
        this.agri_input_data = agri_input_data;
    }
}
