package com.i9930.croptrails.SubmitHealthCard.Model;

import com.google.gson.annotations.SerializedName;

public class HealthCardParams {
    int spinnerPosition=0;

    @SerializedName("parameter")
    String parameter;

    @SerializedName("id")
    String parameterId;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameterId() {
        return parameterId;
    }

    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }
}
