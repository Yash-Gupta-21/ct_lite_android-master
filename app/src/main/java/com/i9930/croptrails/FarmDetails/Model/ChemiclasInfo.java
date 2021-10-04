package com.i9930.croptrails.FarmDetails.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChemiclasInfo {
    @SerializedName("material_id")
    @Expose
    private String materialId;
    @SerializedName("sum")
    @Expose
    private String sum;
    @SerializedName("chemical")
    @Expose
    private String chemical;
    @SerializedName("unit")
    @Expose
    private String unit;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getChemical() {
        return chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
