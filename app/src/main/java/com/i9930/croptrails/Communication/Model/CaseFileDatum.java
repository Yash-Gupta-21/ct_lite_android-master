package com.i9930.croptrails.Communication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaseFileDatum {
    @SerializedName("fileLink")
    @Expose
    private String fileLink;
    @SerializedName("extension")
    @Expose
    private String extension;

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
