package com.i9930.croptrails.SubmitActivityForm.Model;

import com.google.gson.annotations.SerializedName;

public class SendTimelineActivityData {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String farmer_reply;
    String is_done;
    String farm_cal_activity_id;
    String no_of_labors;
    String labor_total_cost;
    String no_of_machines;
    String machines_total_cost;
    String no_of_chemicals;
    String chemicals_total_cost;
    String[] chemical_id;

    String []quantity;
    String []unit_id;
    String farm_id;
    String[] imageList;
    String image_date;

    /*public SendTimelineActivityData(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }*/

    public String[] getImageList() {
        return imageList;
    }

    public void setImageList(String[] imageList) {
        this.imageList = imageList;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
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

    public String getNo_of_labors() {
        return no_of_labors;
    }

    public void setNo_of_labors(String no_of_labors) {
        this.no_of_labors = no_of_labors;
    }

    public String getLabor_total_cost() {
        return labor_total_cost;
    }

    public void setLabor_total_cost(String labor_total_cost) {
        this.labor_total_cost = labor_total_cost;
    }

    public String getNo_of_machines() {
        return no_of_machines;
    }

    public void setNo_of_machines(String no_of_machines) {
        this.no_of_machines = no_of_machines;
    }

    public String getMachines_total_cost() {
        return machines_total_cost;
    }

    public void setMachines_total_cost(String machines_total_cost) {
        this.machines_total_cost = machines_total_cost;
    }

    public String getNo_of_chemicals() {
        return no_of_chemicals;
    }

    public void setNo_of_chemicals(String no_of_chemicals) {
        this.no_of_chemicals = no_of_chemicals;
    }

    public String getChemicals_total_cost() {
        return chemicals_total_cost;
    }

    public void setChemicals_total_cost(String chemicals_total_cost) {
        this.chemicals_total_cost = chemicals_total_cost;
    }

    public String[] getChemical_id() {
        return chemical_id;
    }

    public void setChemical_id(String[] chemical_id) {
        this.chemical_id = chemical_id;
    }

    public String[] getQuantity() {
        return quantity;
    }

    public void setQuantity(String[] quantity) {
        this.quantity = quantity;
    }

    public String[] getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String[] unit_id) {
        this.unit_id = unit_id;
    }

    public String getImage_date() {
        return image_date;
    }

    public void setImage_date(String image_date) {
        this.image_date = image_date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
