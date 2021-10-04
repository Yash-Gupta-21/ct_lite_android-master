package com.i9930.croptrails.CommonClasses.Address;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("states")
    @Expose
    private List<StateDatum> stateDatumList;

    public CountryDatum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StateDatum> getStateDatumList() {
        return stateDatumList;
    }

    public void setStateDatumList(List<StateDatum> stateDatumList) {
        this.stateDatumList = stateDatumList;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
