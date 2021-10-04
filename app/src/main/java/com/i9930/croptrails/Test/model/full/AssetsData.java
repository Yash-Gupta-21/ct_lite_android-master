package com.i9930.croptrails.Test.model.full;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssetsData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("farm_master_num")
    @Expose
    private Integer farmMasterNum;
    @SerializedName("asset_type")
    @Expose
    private String assetType;
    @SerializedName("json")
    @Expose
    private String json;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFarmMasterNum() {
        return farmMasterNum;
    }

    public void setFarmMasterNum(Integer farmMasterNum) {
        this.farmMasterNum = farmMasterNum;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
