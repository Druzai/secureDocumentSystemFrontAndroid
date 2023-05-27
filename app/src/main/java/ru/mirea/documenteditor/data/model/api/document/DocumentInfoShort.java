package ru.mirea.documenteditor.data.model.api.document;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import ru.mirea.documenteditor.data.model.api.user.UserInfo;

public class DocumentInfoShort implements Parcelable {
    private Long id;
    private String name;

    public DocumentInfoShort(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected DocumentInfoShort(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DocumentInfoShort> CREATOR = new Creator<DocumentInfoShort>() {
        @Override
        public DocumentInfoShort createFromParcel(Parcel in) {
            return new DocumentInfoShort(in);
        }

        @Override
        public DocumentInfoShort[] newArray(int size) {
            return new DocumentInfoShort[size];
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
}
