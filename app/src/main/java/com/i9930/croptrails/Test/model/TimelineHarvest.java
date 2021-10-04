package com.i9930.croptrails.Test.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.HarvestReport.Model.HarvestDetailInnerData;
import com.i9930.croptrails.HarvestReport.Model.HarvestDetailMaster;

public class TimelineHarvest {
    String doa;
    @SerializedName("compare_type")
    String type;
    int index;
    HarvestDetailMaster harvestDetailMaster;

    List<HarvestDetailInnerData> harvestDetailInnerDataList;

    public HarvestDetailMaster getHarvestDetailMaster() {
        return harvestDetailMaster;
    }

    public void setHarvestDetailMaster(HarvestDetailMaster harvestDetailMaster) {
        this.harvestDetailMaster = harvestDetailMaster;
    }

    public List<HarvestDetailInnerData> getHarvestDetailInnerDataList() {
        return harvestDetailInnerDataList;
    }

    public void setHarvestDetailInnerDataList(List<HarvestDetailInnerData> harvestDetailInnerDataList) {
        this.harvestDetailInnerDataList = harvestDetailInnerDataList;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
