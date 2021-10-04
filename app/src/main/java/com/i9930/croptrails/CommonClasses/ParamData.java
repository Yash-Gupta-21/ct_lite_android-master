package com.i9930.croptrails.CommonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParamData {
    @SerializedName("area_unit_id")
    @Expose
    private String areaUnitId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("type")
    @Expose
    private String type;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreaUnitId() {
        return areaUnitId;
    }

    public void setAreaUnitId(String areaUnitId) {
        this.areaUnitId = areaUnitId;
    }
}
