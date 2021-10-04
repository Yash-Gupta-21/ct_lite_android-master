package com.i9930.croptrails.CommonClasses.DynamicForm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CropFormDatum {

    @SerializedName("super-type")
    @Expose
    private String superType;

    @SerializedName("info_type_id")
    @Expose
    private String infoTypeId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("col-1")
    @Expose
    private String col1="";
    @SerializedName("col-2")
    @Expose
    private String col2="";
    @SerializedName("col-3")
    @Expose
    private String col3="";
    @SerializedName("col-4")
    @Expose
    private String col4="";

    @SerializedName("col-5")
    @Expose
    private String col5="";
    @SerializedName("col-6")
    @Expose
    private String col6="";


    private String col1Value;
    private String col2Value;
    private String col3Value;
    private String col4Value;
    private String col5Value;
    private String col6Value;

    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("doa")
    @Expose
    private String doa;

    public CropFormDatum() {
    }

    public CropFormDatum(String superType, String infoTypeId, String type, String col1, String col2, String col3, String col4, String col5, String col6, String col1Value, String col2Value, String col3Value, String col4Value, String col5Value, String col6Value, String isActive, String doa) {
        this.superType = superType;
        this.infoTypeId = infoTypeId;
        this.type = type;
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
        this.col6 = col6;
        this.col1Value = col1Value;
        this.col2Value = col2Value;
        this.col3Value = col3Value;
        this.col4Value = col4Value;
        this.col5Value = col5Value;
        this.col6Value = col6Value;
        this.isActive = isActive;
        this.doa = doa;
    }

    public CropFormDatum(String superType, String infoTypeId, String type, String col1, String col2, String col3,
                         String col4, String col5, String col6, String isActive, String doa) {
        this.superType = superType;
        this.infoTypeId = infoTypeId;
        this.type = type;
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
        this.col6 = col6;
        this.isActive = isActive;
        this.doa = doa;
    }

    public static CropFormDatum copy(CropFormDatum datum){

        return new CropFormDatum(datum.getSuperType(),datum.getInfoTypeId(),datum.getType(),datum.getCol1(),datum.getCol2(),
                datum.getCol3(),datum.getCol4(),datum.getCol5(),datum.getCol6(),datum.getIsActive(),datum.getDoa());
    }



    public String getInfoTypeId() {
        return infoTypeId;
    }

    public void setInfoTypeId(String infoTypeId) {
        this.infoTypeId = infoTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getCol6() {
        return col6;
    }

    public void setCol6(String col6) {
        this.col6 = col6;
    }

    public String getCol1Value() {
        return col1Value;
    }

    public void setCol1Value(String col1Value) {
        this.col1Value = col1Value;
    }

    public String getCol2Value() {
        return col2Value;
    }

    public void setCol2Value(String col2Value) {
        this.col2Value = col2Value;
    }

    public String getCol3Value() {
        return col3Value;
    }

    public void setCol3Value(String col3Value) {
        this.col3Value = col3Value;
    }

    public String getCol4Value() {
        return col4Value;
    }

    public void setCol4Value(String col4Value) {
        this.col4Value = col4Value;
    }

    public String getCol5Value() {
        return col5Value;
    }

    public void setCol5Value(String col5Value) {
        this.col5Value = col5Value;
    }

    public String getCol6Value() {
        return col6Value;
    }

    public void setCol6Value(String col6Value) {
        this.col6Value = col6Value;
    }

    public String getSuperType() {
        return superType;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }
}
