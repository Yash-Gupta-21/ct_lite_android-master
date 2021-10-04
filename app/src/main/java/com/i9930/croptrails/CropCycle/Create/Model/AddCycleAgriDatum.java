package com.i9930.croptrails.CropCycle.Create.Model;

import com.i9930.croptrails.SubmitActivityForm.Model.CompAgriDatum;

import java.util.List;

public class AddCycleAgriDatum {
    List<CompAgriDatum> agriDatumList;
    String qty,cost;
    CompAgriDatum selectedAgriDatum;
    public AddCycleAgriDatum(List<CompAgriDatum> agriDatumList) {
        this.agriDatumList = agriDatumList;
    }

    public List<CompAgriDatum> getAgriDatumList() {
        return agriDatumList;
    }

    public void setAgriDatumList(List<CompAgriDatum> agriDatumList) {
        this.agriDatumList = agriDatumList;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public CompAgriDatum getSelectedAgriDatum() {
        return selectedAgriDatum;
    }

    public void setSelectedAgriDatum(CompAgriDatum selectedAgriDatum) {
        this.selectedAgriDatum = selectedAgriDatum;
    }
}
