package com.i9930.croptrails.OfflineMode.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.Maps.ShowArea.Model.GetGpsCoordinates;
import com.i9930.croptrails.Maps.ShowArea.Model.LatLngFarm;

public class FarmsCoordinatesResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("cords")
    @Expose
    private List<GetGpsCoordinates> coordinatesList = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<GetGpsCoordinates> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<GetGpsCoordinates> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }
}
