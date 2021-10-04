package com.i9930.croptrails.Profile.Model;

import com.google.gson.annotations.SerializedName;

public class AddProfileSendData {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;

    String sv_id;
    String comp_id;
    String name;
    String father_name;
    String dob;
    String email;



    public void setSv_id(String sv_id) {
        this.sv_id = sv_id;
    }

    public void setComp_id(String comp_id) {
        this.comp_id = comp_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
