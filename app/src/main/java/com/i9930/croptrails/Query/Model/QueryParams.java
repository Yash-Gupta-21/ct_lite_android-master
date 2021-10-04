package com.i9930.croptrails.Query.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueryParams {

    @SerializedName("id")
    @Expose
    private String paramId;
    @SerializedName("parameters")
    @Expose
    private String name;

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
