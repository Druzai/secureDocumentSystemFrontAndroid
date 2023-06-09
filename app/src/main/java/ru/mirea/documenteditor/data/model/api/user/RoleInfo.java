package ru.mirea.documenteditor.data.model.api.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RoleInfo implements Parcelable {
    private Long id;
    private String name;
    private String userName;

    public RoleInfo(Long id, String name, String userName) {
        this.id = id;
        this.name = name;
        this.userName = userName;
    }

    protected RoleInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        userName = in.readString();
    }

    public static final Creator<RoleInfo> CREATOR = new Creator<RoleInfo>() {
        @Override
        public RoleInfo createFromParcel(Parcel in) {
            return new RoleInfo(in);
        }

        @Override
        public RoleInfo[] newArray(int size) {
            return new RoleInfo[size];
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        dest.writeString(userName);
    }
}
