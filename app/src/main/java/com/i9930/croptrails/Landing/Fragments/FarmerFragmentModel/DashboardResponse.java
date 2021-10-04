package com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DashboardResponse {

    @SerializedName("grade")
    @Expose
    private List<DashboardData> grade = null;
    @SerializedName("no. of farms in a cluster")
    @Expose
    private NoOfFarmsInACluster noOfFarmsInACluster;
    @SerializedName("maximum standing_area")
    @Expose
    private MaximumStandingArea maximumStandingArea;
    @SerializedName("total standing_area")
    @Expose
    private TotalStandingArea totalStandingArea;
    @SerializedName("last_visited_farm")
    @Expose
    private LastVisitedFarm lastVisitedFarm;
    @SerializedName("total_expected_area")
    @Expose
    private TotalExpectedArea totalExpectedArea;
    @SerializedName("maximum_population")
    @Expose
    private MaximumPopulation maximumPopulation;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private Integer  status;
    @SerializedName("comp_name")
    @Expose
    private CompName compName;

    public CompName getCompName() {
        return compName;
    }

    public void setCompName(CompName compName) {
        this.compName = compName;
    }

    public List<DashboardData> getGrade() {
        return grade;
    }

    public void setGrade(List<DashboardData> grade) {
        this.grade = grade;
    }

    public NoOfFarmsInACluster getNoOfFarmsInACluster() {
        return noOfFarmsInACluster;
    }

    public void setNoOfFarmsInACluster(NoOfFarmsInACluster noOfFarmsInACluster) {
        this.noOfFarmsInACluster = noOfFarmsInACluster;
    }

    public MaximumStandingArea getMaximumStandingArea() {
        return maximumStandingArea;
    }

    public void setMaximumStandingArea(MaximumStandingArea maximumStandingArea) {
        this.maximumStandingArea = maximumStandingArea;
    }

    public TotalStandingArea getTotalStandingArea() {
        return totalStandingArea;
    }

    public void setTotalStandingArea(TotalStandingArea totalStandingArea) {
        this.totalStandingArea = totalStandingArea;
    }

    public LastVisitedFarm getLastVisitedFarm() {
        return lastVisitedFarm;
    }

    public void setLastVisitedFarm(LastVisitedFarm lastVisitedFarm) {
        this.lastVisitedFarm = lastVisitedFarm;
    }
    public TotalExpectedArea getTotalExpectedArea() {
        return totalExpectedArea;
    }

    public void setTotalExpectedArea(TotalExpectedArea totalExpectedArea) {
        this.totalExpectedArea = totalExpectedArea;
    }

    public MaximumPopulation getMaximumPopulation() {
        return maximumPopulation;
    }

    public void setMaximumPopulation(MaximumPopulation maximumPopulation) {
        this.maximumPopulation = maximumPopulation;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
