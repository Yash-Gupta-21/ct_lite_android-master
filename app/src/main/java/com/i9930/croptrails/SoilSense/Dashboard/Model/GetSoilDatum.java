package com.i9930.croptrails.SoilSense.Dashboard.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.i9930.croptrails.SoilSense.SoilDetails;

public class GetSoilDatum {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("details")
    @Expose
    private SoilDetails detail;

    @SerializedName("detail")
    @Expose
    private String detailStr;

    public GetSoilDatum(String id, String farmId, String compId, String doa, String latitude, String longitude, SoilDetails detail) {
        this.id = id;
        this.farmId = farmId;
        this.compId = compId;
        this.doa = doa;
        this.latitude = latitude;
        this.longitude = longitude;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public SoilDetails getDetail() {
        return detail;
    }

    public void setDetail(SoilDetails detail) {
        this.detail = detail;
    }

    public String getDetailStr() {
        return detailStr;
    }

    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }
}
