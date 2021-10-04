package com.i9930.croptrails.Maps.VerifyArea.VerifyAreaModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 06-07-2018.
 */

public class VerifySendData {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String area;
    String comp_id;
    String farm_id;
    String deleted_by;
    @SerializedName("zoom")
    @Expose
    private String zoom;
    @SerializedName("lat")
    @Expose
    private String[] lat;
    @SerializedName("lng")
    @Expose
    private String[] lng;


    public String getDeleted_by() {
        return deleted_by;
    }

    public void setDeleted_by(String deleted_by) {
        this.deleted_by = deleted_by;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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

    public String[] getLat() {
        return lat;
    }

    public void setLat(String[] lat) {
        this.lat = lat;
    }

    public String[] getLng() {
        return lng;
    }

    public void setLng(String[] lng) {
        this.lng = lng;
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

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }
}
