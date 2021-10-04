package com.i9930.croptrails.CompSelect.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyResponse {
    @SerializedName("companies")
    @Expose
    private List<CompanyDatum >companyDatumList;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("msg")
    @Expose
    private String msg;

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


    public List<CompanyDatum> getCompanyDatumList() {
        return companyDatumList;
    }

    public void setCompanyDatumList(List<CompanyDatum> companyDatumList) {
        this.companyDatumList = companyDatumList;
    }
}
