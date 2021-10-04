package com.i9930.croptrails.CommonClasses.DynamicForm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CropFormResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<CropFormDatum> data = null;

    @SerializedName("data1")
    @Expose
    private List<CropFormDatum> superData = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CropFormDatum> getData() {
        return data;
    }

    public void setData(List<CropFormDatum> data) {
        this.data = data;
    }

    public List<CropFormDatum> getSuperData() {
        return superData;
    }

    public void setSuperData(List<CropFormDatum> superData) {
        this.superData = superData;
    }
}
