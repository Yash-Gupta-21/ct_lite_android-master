package com.i9930.croptrails.ServiceAndBroadcasts.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by hp on 23-07-2018.
 */

public class SendGpsArray {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String[] lat_cord;
    String[] long_cord;
    String[] enter_date;
    String[] sv_id;
    String[] time;


    public String[] getLat_cord() {
        return lat_cord;
    }

    public void setLat_cord(String[] lat_cord) {
        this.lat_cord = lat_cord;
    }

    public String[] getLong_cord() {
        return long_cord;
    }

    public void setLong_cord(String[] long_cord) {
        this.long_cord = long_cord;
    }

    public String[] getEnter_date() {
        return enter_date;
    }

    public void setEnter_date(String[] enter_date) {
        this.enter_date = enter_date;
    }

    public String[] getSv_id() {
        return sv_id;
    }

    public void setSv_id(String[] sv_id) {
        this.sv_id = sv_id;
    }

    public String[] getTime() {

        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
