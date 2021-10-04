package com.i9930.croptrails.Landing.Models.NewData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmResponseNew {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<FetchFarmResultNew> data = null;

    @SerializedName("farms")
    @Expose
    private List<FetchFarmResultNew> result = null;

    @SerializedName("pages")
    @Expose
    private Integer pages;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<FetchFarmResultNew> getData() {
        return data;
    }

    public void setData(List<FetchFarmResultNew> data) {
        this.data = data;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }


    public List<FetchFarmResultNew> getResult() {
        return result;
    }

    public void setResult(List<FetchFarmResultNew> result) {
        this.result = result;
    }
}
