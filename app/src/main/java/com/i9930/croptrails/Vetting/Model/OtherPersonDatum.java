package com.i9930.croptrails.Vetting.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.Landing.Models.NewData.FetchFarmResultNew;

public class OtherPersonDatum {
    boolean isChecked =true;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("person_id")
    @Expose
    private String personId;
    @SerializedName("farms")
    @Expose
    private List<FetchFarmResultNew> farms = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public List<FetchFarmResultNew> getFarms() {
        return farms;
    }

    public void setFarms(List<FetchFarmResultNew> farms) {
        this.farms = farms;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
