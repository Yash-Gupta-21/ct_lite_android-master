package com.i9930.croptrails.AgriInput.Model;

public class FarmAgriInput {
    long amount;
    long quantity;
    long id;
    long agri_id;
    Long exp_amount;
    Long exp_quanity;
    String added_by;
    String doa;
    String other_agri_input;
    String name,parameters;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAgri_id() {
        return agri_id;
    }

    public void setAgri_id(long agri_id) {
        this.agri_id = agri_id;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getOther_agri_input() {
        return other_agri_input;
    }

    public void setOther_agri_input(String other_agri_input) {
        this.other_agri_input = other_agri_input;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExp_amount(Long exp_amount) {
        this.exp_amount = exp_amount;
    }

    public void setExp_quanity(Long exp_quanity) {
        this.exp_quanity = exp_quanity;
    }

    public Long getExp_amount() {
        return exp_amount;
    }

    public Long getExp_quanity() {
        return exp_quanity;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
