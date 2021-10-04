package com.i9930.croptrails.Maps.WalkAround;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmLocationData {

    @SerializedName("device_id")
    String deviceName;
    @SerializedName("details")
    List<LocationData>locationDataList;

    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    @SerializedName("farm_id")
    String farmId;
    @SerializedName("comp_id")
    String compId;

    public FarmLocationData(String farmId, List<LocationData> locationDataList,String userId,String token,String compId) {
        this.farmId = farmId;
        this.userId=userId;
        this.token=token;
        this.compId=compId;
        this.locationDataList = locationDataList;
        this.deviceName= Build.MODEL + "(" + Build.MANUFACTURER + ")";
    }


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public List<LocationData> getLocationDataList() {
        return locationDataList;
    }

    public void setLocationDataList(List<LocationData> locationDataList) {
        this.locationDataList = locationDataList;
    }
}
