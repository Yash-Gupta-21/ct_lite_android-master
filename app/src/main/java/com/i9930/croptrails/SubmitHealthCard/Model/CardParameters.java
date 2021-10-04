package com.i9930.croptrails.SubmitHealthCard.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardParameters {

   @SerializedName("parameter_id")
    @Expose
    private String parameterId;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("rating")
    @Expose
    private Object rating;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("parameter")
    @Expose
    private String parameter;

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

}
