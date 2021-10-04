package com.i9930.croptrails.Test.SatusreModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import com.i9930.croptrails.Utility.AppConstants;

public class SatsureData {
/*
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("clusterId")
    @Expose
    private Integer clusterId;
    @SerializedName("cropId")
    @Expose
    private Integer cropId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("farmId")
    @Expose
    private Integer farmId;
    @SerializedName("productData")
    @Expose
    private List<ProductDatum> productData = null;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public Integer getCropId() {
        return cropId;
    }

    public void setCropId(Integer cropId) {
        this.cropId = cropId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }


    public List<ProductDatum> getProductData() {
        return productData;
    }

    public void setProductData(List<ProductDatum> productData) {
        this.productData = productData;
    }*/


    @SerializedName("compare_type")
    String type= AppConstants.TIMELINE.SAT;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    @SerializedName("sat_image")
    @Expose
    private String satImage;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    private int sliderIndex;
    int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSatImage() {
        return satImage;
    }

    public void setSatImage(String satImage) {
        this.satImage = satImage;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSliderIndex() {
        return sliderIndex;
    }

    public void setSliderIndex(int sliderIndex) {
        this.sliderIndex = sliderIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
