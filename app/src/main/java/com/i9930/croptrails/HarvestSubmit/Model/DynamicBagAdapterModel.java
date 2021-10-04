package com.i9930.croptrails.HarvestSubmit.Model;

public class DynamicBagAdapterModel {

    public int bagNo;
    public String bagValue;


    public DynamicBagAdapterModel(String bagValue) {
        this.bagValue = bagValue;
    }

    public DynamicBagAdapterModel(int bagNo, String bagValue) {
        this.bagNo = bagNo;
        this.bagValue = bagValue;
    }

    public int getBagNo() {
        return bagNo;
    }

    public void setBagNo(int bagNo) {
        this.bagNo = bagNo;
    }

    public String getBagValue() {
        return bagValue;
    }

    public void setBagValue(String bagValue) {
        this.bagValue = bagValue;
    }
}
