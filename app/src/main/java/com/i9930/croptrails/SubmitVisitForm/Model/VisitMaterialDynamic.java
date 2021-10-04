package com.i9930.croptrails.SubmitVisitForm.Model;

import com.i9930.croptrails.SubmitActivityForm.Model.AgriInput.AgriInput;

public class VisitMaterialDynamic {

    String material,quantity,unit,doneDate,comment,materialId,unitId,type;
    int unitPosition,materialPosition,otherPosition;
    String area;
    AgriInput agriInput;

    public VisitMaterialDynamic() {
    }

    public VisitMaterialDynamic(String type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUnitPosition() {
        return unitPosition;
    }

    public void setUnitPosition(int unitPosition) {
        this.unitPosition = unitPosition;
    }

    public int getMaterialPosition() {
        return materialPosition;
    }

    public void setMaterialPosition(int materialPosition) {
        this.materialPosition = materialPosition;
    }

    public int getOtherPosition() {
        return otherPosition;
    }

    public void setOtherPosition(int otherPosition) {
        this.otherPosition = otherPosition;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public AgriInput getAgriInput() {
        return agriInput;
    }

    public void setAgriInput(AgriInput agriInput) {
        this.agriInput = agriInput;
    }
}
