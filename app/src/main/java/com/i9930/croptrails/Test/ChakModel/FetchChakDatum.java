package com.i9930.croptrails.Test.ChakModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchChakDatum {

    @SerializedName("mob")
    @Expose
    private String mob;

    @SerializedName("lead_chak")
    @Expose
    private String leadChak;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("farm_master_num")
    @Expose
    private Integer farmMasterNum;

    public String getLeadChak() {
        return leadChak;
    }

    public void setLeadChak(String leadChak) {
        this.leadChak = leadChak;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(Integer farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }
}
