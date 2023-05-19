package ru.mirea.documenteditor.data.payload;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DocumentInfo implements Parcelable {
    private Long id;
    private String name;
    private String lastEditBy;
    private UserInfo owner;

    public DocumentInfo(Long id, String name, String lastEditBy, UserInfo owner) {
        this.id = id;
        this.name = name;
        this.lastEditBy = lastEditBy;
        this.owner = owner;
    }

    protected DocumentInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        lastEditBy = in.readString();
        owner = in.readParcelable(UserInfo.class.getClassLoader());
    }

    public static final Creator<DocumentInfo> CREATOR = new Creator<DocumentInfo>() {
        @Override
        public DocumentInfo createFromParcel(Parcel in) {
            return new DocumentInfo(in);
        }

        @Override
        public DocumentInfo[] newArray(int size) {
            return new DocumentInfo[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastEditBy() {
        return lastEditBy;
    }

    public void setLastEditBy(String lastEditBy) {
        this.lastEditBy = lastEditBy;
    }

    public UserInfo getOwner() {
        return owner;
    }

    public void setOwner(UserInfo owner) {
        this.owner = owner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(lastEditBy);
        dest.writeParcelable(owner, flags);
    }
}
