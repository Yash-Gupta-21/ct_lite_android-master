package com.i9930.croptrails.Scan;

import java.util.List;

public class EncFarmResponse {
    int status;
    String message;
    List<EncFarmData>data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<com.i9930.croptrails.Scan.EncFarmData> getData() {
        return data;
    }

    public void setData(List<com.i9930.croptrails.Scan.EncFarmData> data) {
        this.data = data;
    }
}
