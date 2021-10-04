package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfflineAddFarmResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("person_id")
    @Expose
    private Integer personId;
    @SerializedName("updated")
    @Expose
    private List<OfflineFarmDatum> offlineFarmDatumList = null;

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

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public List<OfflineFarmDatum> getOfflineFarmDatumList() {
        return offlineFarmDatumList;
    }

    public void setOfflineFarmDatumList(List<OfflineFarmDatum> offlineFarmDatumList) {
        this.offlineFarmDatumList = offlineFarmDatumList;
    }
}
