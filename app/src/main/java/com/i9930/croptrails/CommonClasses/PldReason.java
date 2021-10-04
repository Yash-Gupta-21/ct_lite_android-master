package com.i9930.croptrails.CommonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PldReason {
    @SerializedName("pld_reason")
    @Expose
    private String name;
    @SerializedName("pld_reason_id")
    @Expose
    private String pldId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPldId() {
        return pldId;
    }

    public void setPldId(String pldId) {
        this.pldId = pldId;
    }
}
