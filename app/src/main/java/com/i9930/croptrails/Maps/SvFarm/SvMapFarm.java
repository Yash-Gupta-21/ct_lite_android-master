package com.i9930.croptrails.Maps.SvFarm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SvMapFarm {
    @SerializedName("lat_cord")
    @Expose
    private String latCord;
    @SerializedName("long_cord")
    @Expose
    private String longCord;
    @SerializedName("farm_master_num")
    @Expose
    private long farmMasterNum;
    @SerializedName("sv_id")
    @Expose
    private long svId;

    public String getLatCord() {
        return latCord;
    }

    public void setLatCord(String latCord) {
        this.latCord = latCord;
    }

    public String getLongCord() {
        return longCord;
    }

    public void setLongCord(String longCord) {
        this.longCord = longCord;
    }

    public long getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(long farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public long getSvId() {
        return svId;
    }

    public void setSvId(long svId) {
        this.svId = svId;
    }
}

