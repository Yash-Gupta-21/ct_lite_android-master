package com.i9930.croptrails.Communication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CaseDatum {
    boolean isDownloaded;
    @SerializedName("file_link")
    @Expose
    private List<CaseFileDatum> fileLinks;

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("case_id")
    @Expose
    private long caseId;
    @SerializedName("comp_id")
    @Expose
    private long compId;
    @SerializedName("sender_id")
    @Expose
    private long senderId;
    @SerializedName("receiver_id")
    @Expose
    private long receiverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_sent")
    @Expose
    private String isSent;
    @SerializedName("is_read")
    @Expose
    private String isRead;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_deleted")
    @Expose
    private String isDeleted;

    public CaseDatum(long id, long caseId, long compId, long senderId, long receiverId, String message,
                     String doa, String isSent, String isRead, String status, String isDeleted) {
        this.id = id;
        this.caseId = caseId;
        this.compId = compId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.doa = doa;
        this.isSent = isSent;
        this.isRead = isRead;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCaseId() {
        return caseId;
    }

    public void setCaseId(long caseId) {
        this.caseId = caseId;
    }

    public long getCompId() {
        return compId;
    }

    public void setCompId(long compId) {
        this.compId = compId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getIsSent() {
        return isSent;
    }

    public void setIsSent(String isSent) {
        this.isSent = isSent;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<CaseFileDatum> getFileLinks() {
        return fileLinks;
    }

    public void setFileLinks(List<CaseFileDatum> fileLinks) {
        this.fileLinks = fileLinks;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }


}


