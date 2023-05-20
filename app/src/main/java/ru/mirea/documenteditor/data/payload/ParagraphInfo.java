package ru.mirea.documenteditor.data.payload;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ParagraphInfo implements Comparable<ParagraphInfo>, Parcelable {
    private Integer number;
    private String content;
    private String align;

    public ParagraphInfo(Integer number, String content, String align) {
        this.number = number;
        this.content = content;
        this.align = align;
    }

    public ParagraphInfo(WSContent wsContent){
        this.number = wsContent.getNumber();
        this.content = wsContent.getData();;
        this.align = wsContent.getAlign();
    }

    public ParagraphInfo(ParagraphInfo paragraphInfo) {
        this.number = paragraphInfo.getNumber();
        this.content = paragraphInfo.getContent();
        this.align = paragraphInfo.getAlign();
    }

    protected ParagraphInfo(Parcel in) {
        if (in.readByte() == 0) {
            number = null;
        } else {
            number = in.readInt();
        }
        content = in.readString();
        align = in.readString();
    }

    public static final Creator<ParagraphInfo> CREATOR = new Creator<ParagraphInfo>() {
        @Override
        public ParagraphInfo createFromParcel(Parcel in) {
            return new ParagraphInfo(in);
        }

        @Override
        public ParagraphInfo[] newArray(int size) {
            return new ParagraphInfo[size];
        }
    };

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    @Override
    public int compareTo(ParagraphInfo o) {
        return this.number.compareTo(o.number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(number);
        }
        dest.writeString(content);
        dest.writeString(align);
    }
}
