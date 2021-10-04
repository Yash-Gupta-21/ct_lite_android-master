package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmerFarmInsertResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("person_id")
    @Expose
    private List<Integer> personId = null;
    @SerializedName("farm_id")
    @Expose
    private List<List<Integer>> farmId = null;

    @SerializedName("farms")
    @Expose
    private List<FarmerFarmInsertResponseFarms> farms = null;

    @SerializedName("updated")
    @Expose
    private List<FarmerFarmInsertResponseFarms> updated = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getPersonId() {
        return personId;
    }

    public void setPersonId(List<Integer> personId) {
        this.personId = personId;
    }

    public List<List<Integer>> getFarmId() {
        return farmId;
    }

    public void setFarmId(List<List<Integer>> farmId) {
        this.farmId = farmId;
    }

    public List<FarmerFarmInsertResponseFarms> getFarms() {
        return farms;
    }

    public void setFarms(List<FarmerFarmInsertResponseFarms> farms) {
        this.farms = farms;
    }

    public List<FarmerFarmInsertResponseFarms> getUpdated() {
        return updated;
    }

    public void setUpdated(List<FarmerFarmInsertResponseFarms> updated) {
        this.updated = updated;
    }
}
