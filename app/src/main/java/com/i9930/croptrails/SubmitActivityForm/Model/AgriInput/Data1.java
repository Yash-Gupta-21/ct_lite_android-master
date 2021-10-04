package com.i9930.croptrails.SubmitActivityForm.Model.AgriInput;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data1 {

@SerializedName("farm_cal_activity_id")
@Expose
private String farmCalActivityId;
@SerializedName("farm_id")
@Expose
private String farmId;
@SerializedName("activity")
@Expose
private String activity;
@SerializedName("activity_type_id")
@Expose
private String activityTypeId;
@SerializedName("description")
@Expose
private String description;
@SerializedName("priority")
@Expose
private String priority;
@SerializedName("chemical_id")
@Expose
private String chemicalId;
@SerializedName("chemical_qty")
@Expose
private String chemicalQty;
@SerializedName("qty_unit")
@Expose
private String qtyUnit;
@SerializedName("is_done")
@Expose
private String isDone;
@SerializedName("farmer_reply")
@Expose
private Object farmerReply;
@SerializedName("farm_calendar_id")
@Expose
private String farmCalendarId;
@SerializedName("day_no")
@Expose
private String dayNo;
@SerializedName("date")
@Expose
private String date;
@SerializedName("activity_type")
@Expose
private String activityType;
@SerializedName("instruction")
@Expose
private List<Instruction> instruction = null;
@SerializedName("agri_Inputs")
@Expose
private List<AgriInput> agriInputs = null;

public String getFarmCalActivityId() {
return farmCalActivityId;
}

public void setFarmCalActivityId(String farmCalActivityId) {
this.farmCalActivityId = farmCalActivityId;
}

public String getFarmId() {
return farmId;
}

public void setFarmId(String farmId) {
this.farmId = farmId;
}

public String getActivity() {
return activity;
}

public void setActivity(String activity) {
this.activity = activity;
}

public String getActivityTypeId() {
return activityTypeId;
}

public void setActivityTypeId(String activityTypeId) {
this.activityTypeId = activityTypeId;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getPriority() {
return priority;
}

public void setPriority(String priority) {
this.priority = priority;
}

public String getChemicalId() {
return chemicalId;
}

public void setChemicalId(String chemicalId) {
this.chemicalId = chemicalId;
}

public String getChemicalQty() {
return chemicalQty;
}

public void setChemicalQty(String chemicalQty) {
this.chemicalQty = chemicalQty;
}

public String getQtyUnit() {
return qtyUnit;
}

public void setQtyUnit(String qtyUnit) {
this.qtyUnit = qtyUnit;
}

public String getIsDone() {
return isDone;
}

public void setIsDone(String isDone) {
this.isDone = isDone;
}

public Object getFarmerReply() {
return farmerReply;
}

public void setFarmerReply(Object farmerReply) {
this.farmerReply = farmerReply;
}

public String getFarmCalendarId() {
return farmCalendarId;
}

public void setFarmCalendarId(String farmCalendarId) {
this.farmCalendarId = farmCalendarId;
}

public String getDayNo() {
return dayNo;
}

public void setDayNo(String dayNo) {
this.dayNo = dayNo;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public String getActivityType() {
return activityType;
}

public void setActivityType(String activityType) {
this.activityType = activityType;
}

public List<Instruction> getInstruction() {
return instruction;
}

public void setInstruction(List<Instruction> instruction) {
this.instruction = instruction;
}

public List<AgriInput> getAgriInputs() {
return agriInputs;
}

public void setAgriInputs(List<AgriInput> agriInputs) {
this.agriInputs = agriInputs;
}

}