package com.i9930.croptrails.AgriInput.Model;

import java.util.List;

public class FarmAgriInputResponse {
    int status;
    String message;
    List<List<FarmAgriInput>>data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<List<FarmAgriInput>> getData() {
        return data;
    }

    public void setData(List<List<FarmAgriInput>> data) {
        this.data = data;
    }
}
