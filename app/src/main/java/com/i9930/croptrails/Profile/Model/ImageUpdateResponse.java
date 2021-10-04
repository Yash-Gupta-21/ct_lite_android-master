package com.i9930.croptrails.Profile.Model;

import com.google.gson.annotations.SerializedName;

public class ImageUpdateResponse {
    @SerializedName("status")
    int status;
    @SerializedName("msg")
    String msg;
    @SerializedName("img_link")
    String imageLink;

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

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
