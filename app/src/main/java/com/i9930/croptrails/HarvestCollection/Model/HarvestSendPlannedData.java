package com.i9930.croptrails.HarvestCollection.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 06-09-2018.
 */

public class HarvestSendPlannedData {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String net_wt_of_stock,comp_id,no_of_bags_blk,total_bags,avg_moisture,remark,svid,trans_admin_id;
    String vehicle_no,vehicle_net_wt,driver_name;
    String plan_pickup_date;

    public String getPlan_pickup_date() {
        return plan_pickup_date;
    }

    public void setPlan_pickup_date(String plan_pickup_date) {
        this.plan_pickup_date = plan_pickup_date;
    }

    String[] harvest_detail_id;
    String[] farm_id;

    public String[] getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String[] farm_id) {
        this.farm_id = farm_id;
    }

    public String getNet_wt_of_stock() {
        return net_wt_of_stock;
    }

    public void setNet_wt_of_stock(String net_wt_of_stock) {
        this.net_wt_of_stock = net_wt_of_stock;
    }

    public String getComp_id() {
        return comp_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public String getNo_of_bags_blk() {
        return no_of_bags_blk;
    }

    public void setNo_of_bags_blk(String no_of_bags_blk) {
        this.no_of_bags_blk = no_of_bags_blk;
    }

    public String getTotal_bags() {
        return total_bags;
    }

    public void setTotal_bags(String total_bags) {
        this.total_bags = total_bags;
    }

    public String getAvg_moisture() {
        return avg_moisture;
    }

    public void setAvg_moisture(String avg_moisture) {
        this.avg_moisture = avg_moisture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSvid() {
        return svid;
    }

    public void setSvid(String svid) {
        this.svid = svid;
    }

    public String getTrans_admin_id() {
        return trans_admin_id;
    }

    public void setTrans_admin_id(String trans_admin_id) {
        this.trans_admin_id = trans_admin_id;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_net_wt() {
        return vehicle_net_wt;
    }

    public void setVehicle_net_wt(String vehicle_net_wt) {
        this.vehicle_net_wt = vehicle_net_wt;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String[] getHarvest_detail_id() {
        return harvest_detail_id;
    }

    public void setHarvest_detail_id(String[] harvest_detail_id) {
        this.harvest_detail_id = harvest_detail_id;
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
