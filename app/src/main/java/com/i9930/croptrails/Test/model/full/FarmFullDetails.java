package com.i9930.croptrails.Test.model.full;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmFullDetails {

    @SerializedName("farmsDetails")
    @Expose
    private FarmsDetails farmsDetails;
    @SerializedName("farmCropDetails")
    @Expose
    private FarmCropDetails farmCropDetails;
    @SerializedName("personDetails")
    @Expose
    private PersonDetails personDetails;
    @SerializedName("bankDetails")
    @Expose
    private BankDetails bankDetails;
    @SerializedName("farmAddressDetails")
    @Expose
    private FarmAddressDetails farmAddressDetails;
    @SerializedName("personAddress")
    @Expose
    private PersonAddress personAddress;
    @SerializedName("dataCropDetails")
    @Expose
    private List<DataCropDetails> dataCropDetails;

    @SerializedName("assetDetails")
    @Expose
    private List<AssetsData> assetsDataList;

    @SerializedName("addInfo")
    @Expose
    private List<PrevCropDatum> prevCropDatumList;


    public FarmsDetails getFarmsDetails() {
        return farmsDetails;
    }

    public void setFarmsDetails(FarmsDetails farmsDetails) {
        this.farmsDetails = farmsDetails;
    }

    public FarmCropDetails getFarmCropDetails() {
        return farmCropDetails;
    }

    public void setFarmCropDetails(FarmCropDetails farmCropDetails) {
        this.farmCropDetails = farmCropDetails;
    }

    public PersonDetails getPersonDetails() {
        return personDetails;
    }

    public void setPersonDetails(PersonDetails personDetails) {
        this.personDetails = personDetails;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    public FarmAddressDetails getFarmAddressDetails() {
        return farmAddressDetails;
    }

    public void setFarmAddressDetails(FarmAddressDetails farmAddressDetails) {
        this.farmAddressDetails = farmAddressDetails;
    }

    public PersonAddress getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(PersonAddress personAddress) {
        this.personAddress = personAddress;
    }

    public List<DataCropDetails> getDataCropDetails() {
        return dataCropDetails;
    }

    public void setDataCropDetails(List<DataCropDetails> dataCropDetails) {
        this.dataCropDetails = dataCropDetails;
    }

    public List<AssetsData> getAssetsDataList() {
        return assetsDataList;
    }

    public void setAssetsDataList(List<AssetsData> assetsDataList) {
        this.assetsDataList = assetsDataList;
    }

    public List<PrevCropDatum> getPrevCropDatumList() {
        return prevCropDatumList;
    }

    public void setPrevCropDatumList(List<PrevCropDatum> prevCropDatumList) {
        this.prevCropDatumList = prevCropDatumList;
    }
}