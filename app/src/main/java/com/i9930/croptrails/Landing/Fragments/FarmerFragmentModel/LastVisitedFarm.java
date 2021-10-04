package com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastVisitedFarm {

    @SerializedName("visit_report_id")
    @Expose
    private String visitReportId;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("cluster_id")
    @Expose
    private String clusterId;
    @SerializedName("lot_no")
    @Expose
    private String lotNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("village_or_city")
    @Expose
    private String villageOrCity;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("addL1")
    @Expose
    private Object addL1;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("addL2")
    @Expose
    private Object addL2;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("mandal_or_tehsil")
    @Expose
    private String mandalOrTehsil;

    public String getVisitReportId() {
        return visitReportId;
    }

    public void setVisitReportId(String visitReportId) {
        this.visitReportId = visitReportId;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getVillageOrCity() {
        return villageOrCity;
    }

    public void setVillageOrCity(String villageOrCity) {
        this.villageOrCity = villageOrCity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getAddL1() {
        return addL1;
    }

    public void setAddL1(Object addL1) {
        this.addL1 = addL1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Object getAddL2() {
        return addL2;
    }

    public void setAddL2(Object addL2) {
        this.addL2 = addL2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMandalOrTehsil() {
        return mandalOrTehsil;
    }

    public void setMandalOrTehsil(String mandalOrTehsil) {
        this.mandalOrTehsil = mandalOrTehsil;
    }
}
