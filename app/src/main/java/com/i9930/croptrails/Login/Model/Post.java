package com.i9930.croptrails.Login.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.CommonClasses.Cluster;
import com.i9930.croptrails.CompSelect.Model.CompanyDatum;

public class Post {
    @SerializedName("date_format")
    @Expose
    String  dateFormat;

    @SerializedName("accuracy")
    @Expose
    String  accuracy;

    @SerializedName("jwtoken")
    @Expose
    String  jwToken;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("data")
    @Expose
    private LoginData data;

    @SerializedName("data1")
    @Expose
    private List<Cluster>clusterIdList;


    @SerializedName("companies")
    @Expose
    private List<CompanyDatum>companyDatumList;



    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Cluster> getClusterIdList() {
        return clusterIdList;
    }

    public void setClusterIdList(List<Cluster> clusterIdList) {
        this.clusterIdList = clusterIdList;
    }

    public String getJwToken() {
        return jwToken;
    }

    public void setJwToken(String jwToken) {
        this.jwToken = jwToken;
    }

    public List<CompanyDatum> getCompanyDatumList() {
        return companyDatumList;
    }

    public void setCompanyDatumList(List<CompanyDatum> companyDatumList) {
        this.companyDatumList = companyDatumList;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}