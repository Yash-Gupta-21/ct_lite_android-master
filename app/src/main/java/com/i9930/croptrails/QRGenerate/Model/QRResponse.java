package com.i9930.croptrails.QRGenerate.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QRResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("qrCode")
    @Expose
    private String qrCode;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

}
