
package com.i9930.croptrails.Landing.Fragments.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.i9930.croptrails.Utility.AppConstants;

public class Datum {
    int type= AppConstants.SV_TIMELINE_CARD_TYPES.EXPENSE;
    @SerializedName("sv_daily_exp_id")
    @Expose
    private String svDailyExpId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("person_id")
    @Expose
    private String personId;
    @SerializedName("s_no")
    @Expose
    private String sNo;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("exp_date")
    @Expose
    private String expDate;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("admin_comment")
    @Expose
    private String adminComment;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("verified_by")
    @Expose
    private String verifiedBy;
    @SerializedName("verified_on")
    @Expose
    private String verifiedOn;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    public String getSvDailyExpId() {
        return svDailyExpId;
    }

    public void setSvDailyExpId(String svDailyExpId) {
        this.svDailyExpId = svDailyExpId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getSNo() {
        return sNo;
    }

    public void setSNo(String sNo) {
        this.sNo = sNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getVerifiedOn() {
        return verifiedOn;
    }

    public void setVerifiedOn(String verifiedOn) {
        this.verifiedOn = verifiedOn;
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

}