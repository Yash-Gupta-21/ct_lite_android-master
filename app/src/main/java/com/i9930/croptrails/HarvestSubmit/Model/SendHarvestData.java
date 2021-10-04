package com.i9930.croptrails.HarvestSubmit.Model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendHarvestData {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

   /* public SendHarvestData(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }*/

    String comp_id, supervisor_id, farm_id;

    float total_net_weight, total_gross_weight, area_harvested, standing_acres;

    String harvest_date, weighment_date;

    @SerializedName("bag_no")
    List<String> bag_no;
    //int bag_no[];
    @SerializedName("net_wt")
    List<String> net_wt;
    @SerializedName("gross_wt")
    List<String> gross_wt;
    //float[] gross_wt;

    String deleted_by;


    public String getDeleted_by() {

        return deleted_by;

    }


    public void setDeleted_by(String deleted_by) {

        this.deleted_by = deleted_by;

    }


    public String getComp_id() {

        return comp_id;

    }


    public void setComp_id(String comp_id) {

        this.comp_id = comp_id;

    }


    public String getSupervisor_id() {

        return supervisor_id;

    }


    public void setSupervisor_id(String supervisor_id) {

        this.supervisor_id = supervisor_id;

    }


    public String getFarm_id() {

        return farm_id;

    }


    public void setFarm_id(String farm_id) {

        this.farm_id = farm_id;

    }


    public float getTotal_net_weight() {

        return total_net_weight;

    }


    public void setTotal_net_weight(float total_net_weight) {

        this.total_net_weight = total_net_weight;

    }


    public float getTotal_gross_weight() {

        return total_gross_weight;

    }


    public void setTotal_gross_weight(float total_gross_weight) {

        this.total_gross_weight = total_gross_weight;

    }


    public float getArea_harvested() {

        return area_harvested;

    }


    public void setArea_harvested(float area_harvested) {

        this.area_harvested = area_harvested;

    }


    public float getStanding_acres() {

        return standing_acres;

    }


    public void setStanding_acres(float standing_acres) {

        this.standing_acres = standing_acres;

    }


    public String getHarvest_date() {

        return harvest_date;

    }


    public void setHarvest_date(String harvest_date) {

        this.harvest_date = harvest_date;

    }


    public String getWeighment_date() {

        return weighment_date;

    }


    public void setWeighment_date(String weighment_date) {

        this.weighment_date = weighment_date;

    }


    public List<String> getBag_no() {

        return bag_no;

    }


    public void setBag_no(List<String> bag_no) {

        this.bag_no = bag_no;

    }


    public List<String> getNet_wt() {

        return net_wt;

    }


    public void setNet_wt(List<String> net_wt) {

        this.net_wt = net_wt;

    }


    public List<String> getGross_wt() {

        return gross_wt;

    }


    public void setGross_wt(List<String> gross_wt) {

        this.gross_wt = gross_wt;

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}