package com.i9930.croptrails.Communication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageObj {
    @SerializedName("d")
    String message;
    @SerializedName("s")
    String subject;

    @SerializedName("f")
    List<String> filesLink;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getFilesLink() {
        return filesLink;
    }

    public void setFilesLink(List<String> filesLink) {
        this.filesLink = filesLink;
    }
}
