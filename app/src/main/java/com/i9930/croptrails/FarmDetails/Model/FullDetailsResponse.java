package com.i9930.croptrails.FarmDetails.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import butterknife.BindView;
import com.i9930.croptrails.Landing.Models.FetchFarmResult;

public class FullDetailsResponse {
    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("expense")
    @Expose
    private String expense;

    @SerializedName("farm_details")
    @Expose
    private FetchFarmResult farmData;

    @SerializedName("grade")
    @Expose
    private List<LineChartInfo> grade = null;

    @SerializedName("chemical")
    @Expose
    private List<ChemiclasInfo> chemical = null;

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

    public FetchFarmResult getFarmData() {
        return farmData;
    }

    public void setFarmData(FetchFarmResult farmData) {
        this.farmData = farmData;
    }



    public List<LineChartInfo> getGrade() {
        return grade;
    }

    public void setGrade(List<LineChartInfo> grade) {
        this.grade = grade;
    }

    public List<ChemiclasInfo> getChemical() {
        return chemical;
    }

    public void setChemical(List<ChemiclasInfo> chemical) {
        this.chemical = chemical;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}
