package com.i9930.croptrails.SubmitActivityForm.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimelineChemicals {

    public TimelineChemicals() {
    }

    public TimelineChemicals(String chemical, String chemicalId) {
        this.chemical = chemical;
        this.chemicalId = chemicalId;
    }

    @SerializedName("chemical")
    @Expose
    private String chemical;
    @SerializedName("chemical_id")
    @Expose
    private String chemicalId;

    public String getChemical() {
        return chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }

    public String getChemicalId() {
        return chemicalId;
    }

    public void setChemicalId(String chemicalId) {
        this.chemicalId = chemicalId;
    }

}
