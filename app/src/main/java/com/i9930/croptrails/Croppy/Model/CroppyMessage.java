package com.i9930.croptrails.Croppy.Model;

public class CroppyMessage {

    String message;
    boolean isSender;

    public CroppyMessage(String message, boolean isSender) {
        this.message = message;
        this.isSender = isSender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(boolean sender) {
        isSender = sender;
    }

    public boolean isSender() {
        return isSender;
    }
}
