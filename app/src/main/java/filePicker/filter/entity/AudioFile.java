package filePicker.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import filePicker.filter.entity.BaseFile;

/**
 * Created by Vincent Woo
 * Date: 2016/10/11
 * Time: 15:52
 */

public class AudioFile extends BaseFile implements Parcelable {
    private long duration;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<filePicker.filter.entity.AudioFile> CREATOR = new Creator<filePicker.filter.entity.AudioFile>() {
        @Override
        public filePicker.filter.entity.AudioFile[] newArray(int size) {
            return new filePicker.filter.entity.AudioFile[size];
        }

        @Override
        public filePicker.filter.entity.AudioFile createFromParcel(Parcel in) {
            filePicker.filter.entity.AudioFile file = new filePicker.filter.entity.AudioFile();
            file.setId(in.readLong());
            file.setName(in.readString());
            file.setPath(in.readString());
            file.setSize(in.readLong());
            file.setBucketId(in.readString());
            file.setBucketName(in.readString());
            file.setDate(in.readLong());
            file.setSelected(in.readByte() != 0);
            file.setDuration(in.readLong());
            return file;
        }
    };
}
