package com.i9930.croptrails.AddFarm.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import com.i9930.croptrails.AddFarm.Share;

public class FarmerAndFarmData {

    String literacy;
    String financial_status;

    String idProof;
    String addressProof;

    @SerializedName("spouse_dob")
    String spouseDob;
    String family_mem_count;
    @SerializedName("sv_id")
    String svId;
    @SerializedName("sv_name")
    String svName;
    String adults_count;
    String kids_count;
    String dependent_count;

    public String isFarmAndFarmer="N";

    String upi_id;
    String civil_status;
    String spouse_name;
    String beneficiary_name;

    @SerializedName("img_link")
    String image;
    //Phone
    String isUsingPhone;
    @SerializedName("is_using_sphone")
    String isSmartPhone;

    //Animal
    String animalCount;
    String milkSalePrice;
    String milkInLiters;
    String isOtherAnimal;
    @SerializedName("cow_cattles")
    CowAndCatles cowAndCatles=new CowAndCatles("","","","");

    @SerializedName("is_share_holder")
    String isShareHolder;

    @SerializedName("share_json")
    Share shareJson=new Share("","","");

    @SerializedName("name")
    String name;
    @SerializedName("father_name")
    String father_name;
    @SerializedName("mob")
    String mob;
    @SerializedName("mob2")
    String mob2;
    @SerializedName("email")
    String email;
    @SerializedName("aadhar")
    String aadhaar;
    @SerializedName("pan")
    String pan;
    @SerializedName("pAddL1")
    String addL1;
    @SerializedName("pAddL2")
    String addL2;
    @SerializedName("pvillage_or_city")
    String village_or_city;
    @SerializedName("pmandal_or_tehsil")
    String mandal_or_tehsil;
    @SerializedName("pdistrict")
    String district;
    @SerializedName("pstate")
    String state;
    @SerializedName("pcountry")
    String country;
    @SerializedName("bank_name")
    String bankName;
    @SerializedName("account_no")
    String accountNumber;
    @SerializedName("account_name")
    String holderName;
    @SerializedName("branch")
    String branch;
    @SerializedName("ifsc")
    String ifscCode;
    @SerializedName("items")
    List<FarmData>farmDataList=new ArrayList<>();
    @SerializedName("cluster_id")
    String clusterId;
    @SerializedName("comp_id")
    String compId;
    @SerializedName("added_by")
    String added_by;

    @SerializedName("owner_id")
    String personId;
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

    @SerializedName("country_code")
    String countryCode;

    @SerializedName("country_id")
    String countryId;
    @SerializedName("state_id")
    String stateId;
    @SerializedName("city_id")
    String cityId;

    @SerializedName("dob")
    String dob;
    @SerializedName("yob")
    String yob;

    @SerializedName("caste")
    String cast;
    @SerializedName("est_income")
    String anualIncome;
    @SerializedName("gender")
    String geder;

    @SerializedName("isUploaded")
    String isUploaded;

    @SerializedName("swift_code")
    String swiftCode;
    @SerializedName("religion")
    String religion;

    @SerializedName("caste_category_id")
    String casteCategoryId;
    @SerializedName("caste_id")
    String casteId;
    @SerializedName("religion_id")
    String religionId;
    @SerializedName("mobile_network_id")
    String mobileNetworkId;
    @SerializedName("motor_hp_id")
    String motorhpId;
    @SerializedName("mia_id")
    String miaId;


    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddL1() {
        return addL1;
    }

    public void setAddL1(String addL1) {
        this.addL1 = addL1;
    }

    public String getAddL2() {
        return addL2;
    }

    public void setAddL2(String addL2) {
        this.addL2 = addL2;
    }

    public String getVillage_or_city() {
        return village_or_city;
    }

    public void setVillage_or_city(String village_or_city) {
        this.village_or_city = village_or_city;
    }

    public String getMandal_or_tehsil() {
        return mandal_or_tehsil;
    }

    public void setMandal_or_tehsil(String mandal_or_tehsil) {
        this.mandal_or_tehsil = mandal_or_tehsil;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public List<FarmData> getFarmDataList() {
        return farmDataList;
    }

    public void setFarmDataList(List<FarmData> farmDataList) {
        this.farmDataList = farmDataList;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getAnualIncome() {
        return anualIncome;
    }

    public void setAnualIncome(String anualIncome) {
        this.anualIncome = anualIncome;
    }

    public String getGeder() {
        return geder;
    }

    public void setGeder(String geder) {
        this.geder = geder;
    }

    public String getIsShareHolder() {
        return isShareHolder;
    }

    public void setIsShareHolder(String isShareHolder) {
        this.isShareHolder = isShareHolder;
    }

    public Share getShareJson() {
        return shareJson;
    }

    public void setShareJson(Share shareJson) {
        this.shareJson = shareJson;
    }

    public String getAnimalCount() {
        return animalCount;
    }

    public void setAnimalCount(String animalCount) {
        this.animalCount = animalCount;
    }

    public String getMilkSalePrice() {
        return milkSalePrice;
    }

    public void setMilkSalePrice(String milkSalePrice) {
        this.milkSalePrice = milkSalePrice;
    }

    public String getMilkInLitters() {
        return milkInLiters;
    }

    public void setMilkInLitters(String milkInLitters) {
        this.milkInLiters = milkInLitters;
    }

    public String getIsOtherAnimal() {
        return isOtherAnimal;
    }

    public void setIsOtherAnimal(String isOtherAnimal) {
        this.isOtherAnimal = isOtherAnimal;
    }

    public String getIsUsingPhone() {
        return isUsingPhone;
    }

    public void setIsUsingPhone(String isUsingPhone) {
        this.isUsingPhone = isUsingPhone;
    }

    public String getIsSmartPhone() {
        return isSmartPhone;
    }

    public void setIsSmartPhone(String isSmartPhone) {
        this.isSmartPhone = isSmartPhone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CowAndCatles getCowAndCatles() {
        return cowAndCatles;
    }

    public void setCowAndCatles(CowAndCatles cowAndCatles) {
        this.cowAndCatles = cowAndCatles;
    }

    public String getIsFarmAndFarmer() {
        return isFarmAndFarmer;
    }

    public void setIsFarmAndFarmer(String isFarmAndFarmer) {
        this.isFarmAndFarmer = isFarmAndFarmer;
    }

    public String getMilkInLiters() {
        return milkInLiters;
    }

    public void setMilkInLiters(String milkInLiters) {
        this.milkInLiters = milkInLiters;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id = upi_id;
    }

    public String getCivil_status() {
        return civil_status;
    }

    public void setCivil_status(String civil_status) {
        this.civil_status = civil_status;
    }

    public String getSpouse_name() {
        return spouse_name;
    }

    public void setSpouse_name(String spouse_name) {
        this.spouse_name = spouse_name;
    }

    public String getBeneficiary_name() {
        return beneficiary_name;
    }

    public void setBeneficiary_name(String beneficiary_name) {
        this.beneficiary_name = beneficiary_name;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
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

    public String getFamily_mem_count() {
        return family_mem_count;
    }

    public void setFamily_mem_count(String family_mem_count) {
        this.family_mem_count = family_mem_count;
    }

    public String getAdults_count() {
        return adults_count;
    }

    public void setAdults_count(String adults_count) {
        this.adults_count = adults_count;
    }

    public String getKids_count() {
        return kids_count;
    }

    public void setKids_count(String kids_count) {
        this.kids_count = kids_count;
    }

    public String getDependent_count() {
        return dependent_count;
    }

    public void setDependent_count(String dependent_count) {
        this.dependent_count = dependent_count;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getSvId() {
        return svId;
    }

    public void setSvId(String svId) {
        this.svId = svId;
    }

    public String getSvName() {
        return svName;
    }

    public void setSvName(String svName) {
        this.svName = svName;
    }

    public String getCasteCategoryId() {
        return casteCategoryId;
    }

    public void setCasteCategoryId(String casteCategoryId) {
        this.casteCategoryId = casteCategoryId;
    }

    public String getCasteId() {
        return casteId;
    }

    public void setCasteId(String casteId) {
        this.casteId = casteId;
    }

    public String getReligionId() {
        return religionId;
    }

    public void setReligionId(String religionId) {
        this.religionId = religionId;
    }

    public String getMobileNetworkId() {
        return mobileNetworkId;
    }

    public void setMobileNetworkId(String mobileNetworkId) {
        this.mobileNetworkId = mobileNetworkId;
    }

    public String getMotorhpId() {
        return motorhpId;
    }

    public void setMotorhpId(String motorhpId) {
        this.motorhpId = motorhpId;
    }

    public String getMiaId() {
        return miaId;
    }

    public void setMiaId(String miaId) {
        this.miaId = miaId;
    }

    public String getLiteracy() {
        return literacy;
    }

    public void setLiteracy(String literacy) {
        this.literacy = literacy;
    }

    public String getFinancial_status() {
        return financial_status;
    }

    public void setFinancial_status(String financial_status) {
        this.financial_status = financial_status;
    }

    public String getSpouseDob() {
        return spouseDob;
    }

    public void setSpouseDob(String spouseDob) {
        this.spouseDob = spouseDob;
    }

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

    public String getYob() {
        return yob;
    }

    public void setYob(String yob) {
        this.yob = yob;
    }
}
