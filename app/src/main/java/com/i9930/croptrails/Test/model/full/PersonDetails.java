package com.i9930.croptrails.Test.model.full;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonDetails {

    @SerializedName("yob")
    @Expose
    private String yob;
    @SerializedName("literacy_status")
    @Expose
    private String literacyStatus;
    @SerializedName("financial_status")
    @Expose
    private String financialStatus;
    @SerializedName("lead_chak")
    @Expose
    private String leadChak;

    @SerializedName("id_proof_link")
    @Expose
    private String idProof;
    @SerializedName("address_proof_link")
    @Expose
    private String addressProof;

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getAddressProof() {
        return addressProof;
    }

    public void setAddressProof(String addressProof) {
        this.addressProof = addressProof;
    }

    @SerializedName("person_id")
    @Expose
    private int personId;
    @SerializedName("comp_id")
    @Expose
    private int compId;
    @SerializedName("cluster_id")
    @Expose
    private int clusterId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("role_id")
    @Expose
    private int roleId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
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
    @SerializedName("is_using_sphone")
    @Expose
    private String isUsingSphone;
    @SerializedName("is_shareholder")
    @Expose
    private String isShareholder;
    @SerializedName("share_json")
    @Expose
    private String shareJson;
    @SerializedName("cattle_json")
    @Expose
    private String cattleJson;
    @SerializedName("est_income")
    @Expose
    private String estIncome;
    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("address_id")
    @Expose
    private int addressId;
    @SerializedName("pan")
    @Expose
    private String pan;
    @SerializedName("aadhaar")
    @Expose
    private String aadhaar;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("added_by")
    @Expose
    private int addedBy;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    @SerializedName("deactivated_by")
    @Expose
    private String deactivatedBy;
    @SerializedName("dod")
    @Expose
    private String dod;
    @SerializedName("spouse_name")
    @Expose
    private String spouseName;

    @SerializedName("spouse_dob")
    @Expose
    private String spouseDob;
    @SerializedName("beneficiary_name")
    @Expose
    private String beneficiaryName;
    @SerializedName("civil_status")
    @Expose
    private String civilStatus;
    @SerializedName("caste_category")
    @Expose
    private String casteCategory;
    @SerializedName("caste")
    @Expose
    private String caste;
    @SerializedName("religion")
    @Expose
    private String religion;
    @SerializedName("family_mem_count")
    @Expose
    private int familyMemCount;
    @SerializedName("adults_count")
    @Expose
    private int adultsCount;
    @SerializedName("kids_count")
    @Expose
    private int kidsCount;
    @SerializedName("dependent_count")
    @Expose
    private int dependentCount;
    @SerializedName("mobile_operator")
    @Expose
    private String mobileOperator;
    @SerializedName("mia")
    @Expose
    private String mia;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getIsUsingSphone() {
        return isUsingSphone;
    }

    public void setIsUsingSphone(String isUsingSphone) {
        this.isUsingSphone = isUsingSphone;
    }

    public String getIsShareholder() {
        return isShareholder;
    }

    public void setIsShareholder(String isShareholder) {
        this.isShareholder = isShareholder;
    }

    public String getShareJson() {
        return shareJson;
    }

    public void setShareJson(String shareJson) {
        this.shareJson = shareJson;
    }

    public String getCattleJson() {
        return cattleJson;
    }

    public void setCattleJson(String cattleJson) {
        this.cattleJson = cattleJson;
    }

    public String getEstIncome() {
        return estIncome;
    }

    public void setEstIncome(String estIncome) {
        this.estIncome = estIncome;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
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

    public int getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(int addedBy) {
        this.addedBy = addedBy;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getDeactivatedBy() {
        return deactivatedBy;
    }

    public void setDeactivatedBy(String deactivatedBy) {
        this.deactivatedBy = deactivatedBy;
    }

    public String getDod() {
        return dod;
    }

    public void setDod(String dod) {
        this.dod = dod;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getCasteCategory() {
        return casteCategory;
    }

    public void setCasteCategory(String casteCategory) {
        this.casteCategory = casteCategory;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public int getFamilyMemCount() {
        return familyMemCount;
    }

    public void setFamilyMemCount(int familyMemCount) {
        this.familyMemCount = familyMemCount;
    }

    public int getAdultsCount() {
        return adultsCount;
    }

    public void setAdultsCount(int adultsCount) {
        this.adultsCount = adultsCount;
    }

    public int getKidsCount() {
        return kidsCount;
    }

    public void setKidsCount(int kidsCount) {
        this.kidsCount = kidsCount;
    }

    public int getDependentCount() {
        return dependentCount;
    }

    public void setDependentCount(int dependentCount) {
        this.dependentCount = dependentCount;
    }

    public String getMobileOperator() {
        return mobileOperator;
    }

    public void setMobileOperator(String mobileOperator) {
        this.mobileOperator = mobileOperator;
    }

    public String getMia() {
        return mia;
    }

    public void setMia(String mia) {
        this.mia = mia;
    }


    public String getLiteracyStatus() {
        return literacyStatus;
    }

    public void setLiteracyStatus(String literacyStatus) {
        this.literacyStatus = literacyStatus;
    }

    public String getFinancialStatus() {
        return financialStatus;
    }

    public void setFinancialStatus(String financialStatus) {
        this.financialStatus = financialStatus;
    }

    public String getLeadChak() {
        return leadChak;
    }

    public void setLeadChak(String leadChak) {
        this.leadChak = leadChak;
    }

    public String getSpouseDob() {
        return spouseDob;
    }

    public void setSpouseDob(String spouseDob) {
        this.spouseDob = spouseDob;
    }

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }
}