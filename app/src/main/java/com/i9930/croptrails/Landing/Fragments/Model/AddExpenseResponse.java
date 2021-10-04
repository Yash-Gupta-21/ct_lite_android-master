package com.i9930.croptrails.Landing.Fragments.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddExpenseResponse {
    @SerializedName("status")
    @Expose
    int status;

    @SerializedName("msg")
    @Expose
    String msg;

    @SerializedName("img_link")
    @Expose
    String img_link;

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

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }
}
