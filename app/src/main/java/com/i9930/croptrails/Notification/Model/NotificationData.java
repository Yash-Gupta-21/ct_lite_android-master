package com.i9930.croptrails.Notification.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationData {
    @SerializedName("noti_scheduler_hist_id")
    @Expose
    private String notiSchedulerHistId;

    @SerializedName("noti_sch_id")
    @Expose
    private String notiSchId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("extra_message")
    @Expose
    private String extraMessage;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    @SerializedName("farm_cal_activity_id")
    @Expose
    private String farmCalActivityId;
    @SerializedName("landing_intent_id")
    @Expose
    private String landingIntentId;
    @SerializedName("comp_id")
    @Expose
    private String compId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("farm_id")
    @Expose
    private String farmId;
    @SerializedName("scheduled_date")
    @Expose
    private String scheduledDate;
    @SerializedName("is_sent")
    @Expose
    private String isSent;
    @SerializedName("noti_type")
    @Expose
    private String notiType;
    @SerializedName("snooze_duration")
    @Expose
    private String snoozeDuration;
    @SerializedName("is_read")
    @Expose
    private String isRead;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("added_by")
    @Expose
    private String addedBy;
    @SerializedName("added_on")
    @Expose
    private String addedOn;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("landing_title")
    @Expose
    private String landingTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("img_link")
    @Expose
    private String imgLink;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("key_type")
    @Expose
    private String keyType;

    @SerializedName("sent_on")
    @Expose
    private String sentOn;

    public String getNotiSchId() {
        return notiSchId;
    }

    public void setNotiSchId(String notiSchId) {
        this.notiSchId = notiSchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getFarmCalActivityId() {
        return farmCalActivityId;
    }

    public void setFarmCalActivityId(String farmCalActivityId) {
        this.farmCalActivityId = farmCalActivityId;
    }

    public String getLandingIntentId() {
        return landingIntentId;
    }

    public void setLandingIntentId(String landingIntentId) {
        this.landingIntentId = landingIntentId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getIsSent() {
        return isSent;
    }

    public void setIsSent(String isSent) {
        this.isSent = isSent;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }

    public String getSnoozeDuration() {
        return snoozeDuration;
    }

    public void setSnoozeDuration(String snoozeDuration) {
        this.snoozeDuration = snoozeDuration;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLandingTitle() {
        return landingTitle;
    }

    public void setLandingTitle(String landingTitle) {
        this.landingTitle = landingTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getSentOn() {
        return sentOn;
    }

    public void setSentOn(String sentOn) {
        this.sentOn = sentOn;
    }

    public String getNotiSchedulerHistId() {
        return notiSchedulerHistId;
    }

    public void setNotiSchedulerHistId(String notiSchedulerHistId) {
        this.notiSchedulerHistId = notiSchedulerHistId;
    }
}
