package com.i9930.croptrails.AddFarm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnimalHusbandory {

    @SerializedName("col-1")
    @Expose
    private String col1;
    @SerializedName("col-2")
    @Expose
    private String col2;
    @SerializedName("col-3")
    @Expose
    private String col3;
    @SerializedName("col-4")
    @Expose
    private String col4;

    @SerializedName("col-5")
    @Expose
    private String col5;
    @SerializedName("col-6")
    @Expose
    private String col6;

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
}
