package com.i9930.croptrails.Login.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("country_code")
    @Expose
    String  countryCode;
    @SerializedName("comp_country")
    @Expose
    private String compCountry;
    @SerializedName("user_language")
    @Expose
    private String userLanguage;

    @SerializedName("mul_factor")
    @Expose
    private String mulFactor;

    @SerializedName("areaUnit")
    @Expose
    private String unitName;

    @SerializedName("person_id")
    @Expose
    private String personId;
    @SerializedName("comp_id")
    @Expose
    private String compId;

    @SerializedName("company_name")
    @Expose
    private String compName;
    @SerializedName("cluster_id")
    @Expose
    private String clusterId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("mob")
    @Expose
    private String mob;
    @SerializedName("mob2")
    @Expose
    private String mob2;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("addl1")
    @Expose
    private String addl1;
    @SerializedName("addl2")
    @Expose
    private String addl2;
    @SerializedName("village_or_city")
    @Expose
    private String villageOrCity;
    @SerializedName("mandal_or_tehsil")
    @Expose
    private String mandalOrTehsil;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("banner_img")
    @Expose
    private String compBanner;
    @SerializedName("icon_img")
    @Expose
    private String compLogo;

    public String getClusterName() {
        return clusterName;
    }
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @SerializedName("cluster_name")
    @Expose
    private String clusterName;



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getMob2() {
        return mob2;
    }

    public void setMob2(String mob2) {
        this.mob2 = mob2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddl1() {
        return addl1;
    }

    public void setAddl1(String addl1) {
        this.addl1 = addl1;
    }

    public String getAddl2() {
        return addl2;
    }

    public void setAddl2(String addl2) {
        this.addl2 = addl2;
    }

    public String getVillageOrCity() {
        return villageOrCity;
    }

    public void setVillageOrCity(String villageOrCity) {
        this.villageOrCity = villageOrCity;
    }

    public String getMandalOrTehsil() {
        return mandalOrTehsil;
    }

    public void setMandalOrTehsil(String mandalOrTehsil) {
        this.mandalOrTehsil = mandalOrTehsil;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
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

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompBanner() {
        return compBanner;
    }

    public void setCompBanner(String compBanner) {
        this.compBanner = compBanner;
    }

    public String getCompLogo() {
        return compLogo;
    }

    public void setCompLogo(String compLogo) {
        this.compLogo = compLogo;
    }

    public String getMulFactor() {
        return mulFactor;
    }

    public void setMulFactor(String mulFactor) {
        this.mulFactor = mulFactor;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCompCountry() {
        return compCountry;
    }

    public void setCompCountry(String compCountry) {
        this.compCountry = compCountry;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}