package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.i9930.croptrails.AddFarm.Model.FarmCrop;

public class SpinnerResponse {

    @SerializedName("irrigation_type")
    @Expose
    private List<IrrigationType> irrigationType = null;
    @SerializedName("irrigation_source")
    @Expose
    private List<IrrigationSource> irrigationSource = null;
    @SerializedName("soil_type")
    @Expose
    private List<SoilType> soilType = null;

    @SerializedName("season")
    @Expose
    private List<Season> seasonList = null;

    @SerializedName("crop")
    @Expose
    private List<FarmCrop> cropList = null;

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;



    public List<IrrigationType> getIrrigationType() {
        return irrigationType;
    }

    public void setIrrigationType(List<IrrigationType> irrigationType) {
        this.irrigationType = irrigationType;
    }

    public List<IrrigationSource> getIrrigationSource() {
        return irrigationSource;
    }

    public void setIrrigationSource(List<IrrigationSource> irrigationSource) {
        this.irrigationSource = irrigationSource;
    }

    public List<SoilType> getSoilType() {
        return soilType;
    }

    public void setSoilType(List<SoilType> soilType) {
        this.soilType = soilType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public void setSeasonList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }

    public List<FarmCrop> getCropList() {
        return cropList;
    }

    public void setCropList(List<FarmCrop> cropList) {
        this.cropList = cropList;
    }
}