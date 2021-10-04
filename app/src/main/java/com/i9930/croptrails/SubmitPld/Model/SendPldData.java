package com.i9930.croptrails.SubmitPld.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.FarmDetails.Model.Visit.VisitImageTimeline;

/**
 * Created by hp on 13-08-2018.
 */

public class SendPldData {

    @SerializedName("pld_area")
    String pldArea;

    @SerializedName("pld_reason_id")
    String pldReasonId;
    @SerializedName("other_reason")
    String otherReason;
    @SerializedName("added_by")
    String addedBy;
    @SerializedName("pld_date")
    String pldDate;
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String cluster_id;
    String farm_id;
    String person_id;
    String pld_acres;
    String pld_reason;
    String standing_acres;
    String doa;
    @SerializedName("img_list")
    String[] imageList;

    List<VisitImageTimeline> imageListOffline = new ArrayList<>();

    String lat = "";
    String lon = "";

  /*  public SendPldData(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }*/

    public String getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(String cluster_id) {
        this.cluster_id = cluster_id;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getPld_acres() {
        return pld_acres;
    }

    public void setPld_acres(String pld_acres) {
        this.pld_acres = pld_acres;
    }

    public String getPld_reason() {
        return pld_reason;
    }

    public void setPld_reason(String pld_reason) {
        this.pld_reason = pld_reason;
    }

    public String getStanding_acres() {
        return standing_acres;
    }

    public void setStanding_acres(String standing_acres) {
        this.standing_acres = standing_acres;
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

    public String getPldArea() {
        return pldArea;
    }

    public void setPldArea(String pldArea) {
        this.pldArea = pldArea;
    }

    public String getPldReasonId() {
        return pldReasonId;
    }

    public void setPldReasonId(String pldReasonId) {
        this.pldReasonId = pldReasonId;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getPldDate() {
        return pldDate;
    }

    public void setPldDate(String pldDate) {
        this.pldDate = pldDate;
    }

    public String[] getImageList() {
        return imageList;
    }

    public void setImageList(String[] imageList) {
        this.imageList = imageList;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public List<VisitImageTimeline> getImageListOffline() {
        return imageListOffline;
    }

    public void setImageListOffline(List<VisitImageTimeline> imageListOffline) {
        this.imageListOffline = imageListOffline;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }
}
