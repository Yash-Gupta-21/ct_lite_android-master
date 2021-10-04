package com.i9930.croptrails.CropCycle.Create.Model;

import com.i9930.croptrails.CropCycle.Create.Model.FetchType.ActivityType;

import java.util.ArrayList;
import java.util.List;

public class AddCycleDatum {
    String dayNo,activity,description,inst1,inst2,inst3,inst4,inst5;
    String selectedPriority;
    ActivityType selectedActivity;
    
    List<Integer>priorityList=new ArrayList<>();
    List<ActivityType>activityTypeList;
    List<AddCycleAgriDatum>agriInputs;

    public AddCycleDatum(List<AddCycleAgriDatum> agriInputs) {
        this.agriInputs = agriInputs;
        priorityList.clear();
        priorityList.add(1);
        priorityList.add(2);
        priorityList.add(3);
        priorityList.add(4);
        priorityList.add(5);

    }

    public List<AddCycleAgriDatum> getAgriInputs() {
        return agriInputs;
    }

    public void setAgriInputs(List<AddCycleAgriDatum> agriInputs) {
        this.agriInputs = agriInputs;
    }

    public List<Integer> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List<Integer> priorityList) {
        this.priorityList = priorityList;
    }

    public String getDayNo() {
        return dayNo;
    }

    public void setDayNo(String dayNo) {
        this.dayNo = dayNo;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInst1() {
        return inst1;
    }

    public void setInst1(String inst1) {
        this.inst1 = inst1;
    }

    public String getInst2() {
        return inst2;
    }

    public void setInst2(String inst2) {
        this.inst2 = inst2;
    }

    public String getInst3() {
        return inst3;
    }

    public void setInst3(String inst3) {
        this.inst3 = inst3;
    }

    public String getInst4() {
        return inst4;
    }

    public void setInst4(String inst4) {
        this.inst4 = inst4;
    }

    public String getInst5() {
        return inst5;
    }

    public void setInst5(String inst5) {
        this.inst5 = inst5;
    }

    public String getSelectedPriority() {
        return selectedPriority;
    }

    public void setSelectedPriority(String selectedPriority) {
        this.selectedPriority = selectedPriority;
    }

    public ActivityType getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(ActivityType selectedActivity) {
        this.selectedActivity = selectedActivity;
    }



    public List<ActivityType> getActivityTypeList() {
        return activityTypeList;
    }

    public void setActivityTypeList(List<ActivityType> activityTypeList) {
        this.activityTypeList = activityTypeList;
    }
}
