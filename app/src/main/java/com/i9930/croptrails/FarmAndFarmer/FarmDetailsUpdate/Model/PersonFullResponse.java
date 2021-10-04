package com.i9930.croptrails.FarmAndFarmer.FarmDetailsUpdate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonFullResponse {
    @SerializedName("data")
    @Expose
    private PersonFullData PersonFullData;
    @SerializedName("status")
    @Expose
    private int status;

    public PersonFullData getPersonFullData() {
        return PersonFullData;
    }

    public void setPersonFullData(PersonFullData PersonFullData) {
        this.PersonFullData = PersonFullData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
