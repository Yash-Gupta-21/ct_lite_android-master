package com.i9930.croptrails.Landing.Fragments.DashboardMapModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapData {
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("lot_no")
    @Expose
    private String lotNo;
    @SerializedName("lat_cord")
    @Expose
    private Object latCord;
    @SerializedName("long_cord")
    @Expose
    private Object longCord;
    @SerializedName("village_or_city")
    @Expose
    private String villageOrCity;
    @SerializedName("name")
    @Expose
    private String name;

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Object getLatCord() {
        return latCord;
    }

    public void setLatCord(Object latCord) {
        this.latCord = latCord;
    }

    public Object getLongCord() {
        return longCord;
    }

    public void setLongCord(Object longCord) {
        this.longCord = longCord;
    }

    public String getVillageOrCity() {
        return villageOrCity;
    }

    public void setVillageOrCity(String villageOrCity) {
        this.villageOrCity = villageOrCity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
