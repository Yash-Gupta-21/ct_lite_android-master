package com.i9930.croptrails.Test.model.full;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankDetails {

    @SerializedName("bank_detail_id")
    @Expose
    private int bankDetailId;
    @SerializedName("person_id")
    @Expose
    private int personId;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("upi_id")
    @Expose
    private String upiId;
    @SerializedName("swift_code")
    @Expose
    private String swiftCode;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("doa")
    @Expose
    private String doa;

    public int getBankDetailId() {
        return bankDetailId;
    }

    public void setBankDetailId(int bankDetailId) {
        this.bankDetailId = bankDetailId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
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

}