package com.i9930.croptrails.Task.Model.Farm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.i9930.croptrails.Utility.AppConstants;

public class SvFarm {
     int type=AppConstants.SV_TIMELINE_CARD_TYPES.FARM;

    @SerializedName("person_id")
    @Expose
    private long personId;
    @SerializedName("comp_farm_id")
    @Expose
    private String compFarmId;
    @SerializedName("farm_master_num")
    @Expose
    private long farmId;
    @SerializedName("name")
    @Expose
    private String farmerName;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("doa")
    @Expose
    private String doa;

    @SerializedName("isSelected")
    @Expose
    private String isSelected;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getCompFarmId() {
        return compFarmId;
    }

    public void setCompFarmId(String compFarmId) {
        this.compFarmId = compFarmId;
    }

    public long getFarmId() {
        return farmId;
    }

    public void setFarmId(long farmId) {
        this.farmId = farmId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }
}
