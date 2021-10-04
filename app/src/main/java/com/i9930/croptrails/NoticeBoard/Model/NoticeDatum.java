package com.i9930.croptrails.NoticeBoard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeDatum implements Parcelable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("impression")
    @Expose
    private long impression;
    @SerializedName("comp_id")
    @Expose
    private long compId;
    @SerializedName("group_id")
    @Expose
    private long groupId;
    @SerializedName("person_id")
    @Expose
    private long personId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("ext")
    @Expose
    private String ext;
    @SerializedName("doa")
    @Expose
    private String doa;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("name")
    @Expose
    private String name;


    protected NoticeDatum(Parcel in) {
        id = in.readLong();
        impression = in.readLong();
        compId = in.readLong();
        groupId = in.readLong();
        personId = in.readLong();
        title = in.readString();
        content = in.readString();
        link = in.readString();
        profile = in.readString();
        ext = in.readString();
        doa = in.readString();
        isActive = in.readString();
        name = in.readString();
    }

    public static final Creator<NoticeDatum> CREATOR = new Creator<NoticeDatum>() {
        @Override
        public NoticeDatum createFromParcel(Parcel in) {
            return new NoticeDatum(in);
        }

        @Override
        public NoticeDatum[] newArray(int size) {
            return new NoticeDatum[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompId() {
        return compId;
    }

    public void setCompId(long compId) {
        this.compId = compId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public long getImpression() {
        return impression;
    }

    public void setImpression(long impression) {
        this.impression = impression;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(impression);
        dest.writeLong(compId);
        dest.writeLong(groupId);
        dest.writeLong(personId);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(link);
        dest.writeString(profile);
        dest.writeString(ext);
        dest.writeString(doa);
        dest.writeString(isActive);
        dest.writeString(name);
    }
}
