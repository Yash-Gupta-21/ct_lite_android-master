package com.i9930.croptrails.Test.SoilSensResModel;

import org.json.JSONObject;

import java.util.List;

import com.i9930.croptrails.SoilSense.Dashboard.Model.GetSoilDatum;
import com.i9930.croptrails.SoilSense.SendSoilData;

public class SoilSenseResponse {

    String msg;
    int status;
    List<GetSoilDatum>data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GetSoilDatum> getData() {
        return data;
    }

    public void setData(List<GetSoilDatum> data) {
        this.data = data;
    }
}
