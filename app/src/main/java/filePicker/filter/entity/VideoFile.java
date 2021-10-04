package filePicker.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import filePicker.filter.entity.BaseFile;

/**
 * Created by Vincent Woo
 * Date: 2016/10/11
 * Time: 15:23
 */

public class VideoFile extends BaseFile implements Parcelable {
    private long duration;
    private String thumbnail;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getName());
        dest.writeString(getPath());
        dest.writeLong(getSize());
        dest.writeString(getBucketId());
        dest.writeString(getBucketName());
        dest.writeLong(getDate());
        dest.writeByte((byte) (isSelected() ? 1 : 0));
        dest.writeLong(getDuration());
        dest.writeString(getThumbnail());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<filePicker.filter.entity.VideoFile> CREATOR = new Creator<filePicker.filter.entity.VideoFile>() {
        @Override
        public filePicker.filter.entity.VideoFile[] newArray(int size) {
            return new filePicker.filter.entity.VideoFile[size];
        }

        @Override
        public filePicker.filter.entity.VideoFile createFromParcel(Parcel in) {
            filePicker.filter.entity.VideoFile file = new filePicker.filter.entity.VideoFile();
            file.setId(in.readLong());
            file.setName(in.readString());
            file.setPath(in.readString());
            file.setSize(in.readLong());
            file.setBucketId(in.readString());
            file.setBucketName(in.readString());
            file.setDate(in.readLong());
            file.setSelected(in.readByte() != 0);
            file.setDuration(in.readLong());
            file.setThumbnail(in.readString());
            return file;
        }
    };
}
