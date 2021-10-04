package com.i9930.croptrails.SubmitHealthCard.Model;

import com.google.gson.annotations.SerializedName;

public class HealthCardData {

    int spinnerPosition=0;

    @SerializedName("parameter")
    String name;

    @SerializedName("value")
    String value;

    @SerializedName("unit")
    String unit;

    @SerializedName("id")
    String parameterId;

    public HealthCardData() {
    }

    public HealthCardData(String name, String value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getSpinnerPosition() {
        return spinnerPosition;
    }

    public void setSpinnerPosition(int spinnerPosition) {
        this.spinnerPosition = spinnerPosition;
    }

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }
}
