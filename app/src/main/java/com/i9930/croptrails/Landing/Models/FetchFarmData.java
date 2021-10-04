package com.i9930.croptrails.Landing.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.AddFarm.Model.FarmCrop;
import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationSource;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.IrrigationType;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.Season;
import com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model.SoilType;
import com.i9930.croptrails.SubmitInputCost.Model.ExpenseDataDD;
import com.i9930.croptrails.SubmitInputCost.Model.ResourceDataDD;

public class FetchFarmData {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("no_of_farms")
    @Expose
    private String noOfFarms;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("result")
    @Expose
    private List<FetchFarmResult> fetchFarmResultList = null;

    @SerializedName("unit")
    @Expose
    private List<TimelineUnits> unit = null;
    @SerializedName("expense")
    @Expose
    List<ExpenseDataDD>expenseDataDDList;
    @SerializedName("resource")
    @Expose
    List<ResourceDataDD>resourceDataDDList;

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

    @SerializedName("crops")
    @Expose
    private List<FarmCrop> cropList = null;


    public List<TimelineUnits> getUnit() {
        return unit;
    }

    public void setUnit(List<TimelineUnits> unit) {
        this.unit = unit;
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

    public List<FetchFarmResult> getResult() {
        return fetchFarmResultList;
    }

    public void setResult(List<FetchFarmResult> fetchFarmResultList) {
        this.fetchFarmResultList = fetchFarmResultList;
    }

    public String getNoOfFarms() {
        return noOfFarms;
    }

    public void setNoOfFarms(String noOfFarms) {
        this.noOfFarms = noOfFarms;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<ExpenseDataDD> getExpenseDataDDList() {
        return expenseDataDDList;
    }

    public void setExpenseDataDDList(List<ExpenseDataDD> expenseDataDDList) {
        this.expenseDataDDList = expenseDataDDList;
    }

    public List<ResourceDataDD> getResourceDataDDList() {
        return resourceDataDDList;
    }

    public void setResourceDataDDList(List<ResourceDataDD> resourceDataDDList) {
        this.resourceDataDDList = resourceDataDDList;
    }

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

    public List<Season> getSeasonList() {
        return seasonList;
    }

    public List<FarmCrop> getCropList() {
        return cropList;
    }

    public void setCropList(List<FarmCrop> cropList) {
        this.cropList = cropList;
    }

    public void setSeasonList(List<Season> seasonList) {
        this.seasonList = seasonList;
    }
}