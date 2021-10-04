package com.i9930.croptrails.Query.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QueryParamResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    List<QueryParams>paramsList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<QueryParams> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<QueryParams> paramsList) {
        this.paramsList = paramsList;
    }
}
