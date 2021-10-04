package com.i9930.croptrails.CommonClasses.Address;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateDatum {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("country_id")
    @Expose
    private String countryId;

    @SerializedName("cities")
    @Expose
    private List<CityDatum> cities;

    public StateDatum(String id, String name) {
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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public List<CityDatum> getCities() {
        return cities;
    }

    public void setCities(List<CityDatum> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
