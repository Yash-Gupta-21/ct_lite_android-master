package com.i9930.croptrails.FarmDetails.Model.Visit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisitResponse {
    @SerializedName("farm_id")
    @Expose
    private String farmId;

    @SerializedName("visit_report")
    @Expose
    private List<VisitResponseDatum> visitResponseDatumList = null;

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public List<VisitResponseDatum> getVisitResponseDatumList() {
        return visitResponseDatumList;
    }

    public void setVisitResponseDatumList(List<VisitResponseDatum> visitResponseDatumList) {
        this.visitResponseDatumList = visitResponseDatumList;
    }
}
