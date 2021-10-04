package com.i9930.croptrails.ResetPassword.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 05-12-2018.
 */

public class ResetPassSendData {
    @SerializedName("user_id")
    String userId;
    @SerializedName("token")
    String token;
    String old_password;
    String password;
    String person_id;

    /*public ResetPassSendData(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }
*/
    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
