package com.i9930.croptrails.Landing.Fragments.FarmerFragmentModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompName {

@SerializedName("company_name")
@Expose
private String companyName;
@SerializedName("name")
@Expose
private String name;
@SerializedName("comp_id")
@Expose
private String compId;
@SerializedName("company")
@Expose
private String company;

public String getCompanyName() {
return companyName;
}

public void setCompanyName(String companyName) {
this.companyName = companyName;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getCompId() {
return compId;
}

public void setCompId(String compId) {
this.compId = compId;
}

public String getCompany() {
return company;
}

public void setCompany(String company) {
this.company = company;
}

}