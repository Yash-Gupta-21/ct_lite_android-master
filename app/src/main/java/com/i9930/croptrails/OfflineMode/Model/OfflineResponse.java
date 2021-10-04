package com.i9930.croptrails.OfflineMode.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.CommonClasses.TimelineUnits;
import com.i9930.croptrails.FarmDetails.Model.Visit.VisitResponse;
import com.i9930.croptrails.SubmitActivityForm.Model.TimelineChemicals;


public class OfflineResponse {

    @SerializedName("visit_report")
    @Expose
    List<VisitResponse>visitMasterList;

    @SerializedName("dataa")
    @Expose
    private List<HarvestOffline> harvestOfflineList = null;

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("resource_input_data")
    @Expose
    private List<InputCostAndResourceData>inputCostAndResourceDataList;
    @SerializedName("unit")
    @Expose
    List<TimelineUnits>unitsList;

    @SerializedName("comp_chemicals")
    @Expose
    List<TimelineChemicals>timelineChemicalsList;

    @SerializedName("comp_unit")
    @Expose
    List<TimelineUnits>timelineUnitsList;

    public List<VisitResponse> getVisitMasterList() {
        return visitMasterList;
    }

    public void setVisitMasterList(List<VisitResponse> visitMasterList) {
        this.visitMasterList = visitMasterList;
    }


    public List<HarvestOffline> getHarvestOfflineList() {
        return harvestOfflineList;
    }

    public void setHarvestOfflineList(List<HarvestOffline> harvestOfflineList) {
        this.harvestOfflineList = harvestOfflineList;
    }

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

    public List<InputCostAndResourceData> getInputCostAndResourceDataList() {
        return inputCostAndResourceDataList;
    }

    public void setInputCostAndResourceDataList(List<InputCostAndResourceData> inputCostAndResourceDataList) {
        this.inputCostAndResourceDataList = inputCostAndResourceDataList;
    }

    public List<TimelineUnits> getUnitsList() {
        return unitsList;
    }

    public void setUnitsList(List<TimelineUnits> unitsList) {
        this.unitsList = unitsList;
    }

    public List<TimelineChemicals> getTimelineChemicalsList() {
        return timelineChemicalsList;
    }

    public void setTimelineChemicalsList(List<TimelineChemicals> timelineChemicalsList) {
        this.timelineChemicalsList = timelineChemicalsList;
    }

    public List<TimelineUnits> getTimelineUnitsList() {
        return timelineUnitsList;
    }

    public void setTimelineUnitsList(List<TimelineUnits> timelineUnitsList) {
        this.timelineUnitsList = timelineUnitsList;
    }
}
