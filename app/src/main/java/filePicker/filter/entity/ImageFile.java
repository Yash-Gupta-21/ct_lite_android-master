package filePicker.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import filePicker.filter.entity.BaseFile;

/**
 * Created by Vincent Woo
 * Date: 2016/10/10
 * Time: 17:44
 */

public class ImageFile extends BaseFile implements Parcelable {
    private int orientation;   //0, 90, 180, 270

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
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
        dest.writeInt(orientation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<filePicker.filter.entity.ImageFile> CREATOR = new Creator<filePicker.filter.entity.ImageFile>() {
        @Override
        public filePicker.filter.entity.ImageFile[] newArray(int size) {
            return new filePicker.filter.entity.ImageFile[size];
        }

        @Override
        public filePicker.filter.entity.ImageFile createFromParcel(Parcel in) {
            filePicker.filter.entity.ImageFile file = new filePicker.filter.entity.ImageFile();
            file.setId(in.readLong());
            file.setName(in.readString());
            file.setPath(in.readString());
            file.setSize(in.readLong());
            file.setBucketId(in.readString());
            file.setBucketName(in.readString());
            file.setDate(in.readLong());
            file.setSelected(in.readByte() != 0);
            file.setOrientation(in.readInt());
            return file;
        }
    };
}
