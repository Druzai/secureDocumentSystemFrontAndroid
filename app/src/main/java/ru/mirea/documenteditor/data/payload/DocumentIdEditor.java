package ru.mirea.documenteditor.data.payload;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class DocumentIdEditor implements Parcelable {
    private boolean editor;
    private boolean owner;
    private DocumentInfo document;
    private List<ParagraphInfo> documentParagraphs;

    public DocumentIdEditor(boolean editor, boolean owner, DocumentInfo document, List<ParagraphInfo> documentParagraphs) {
        this.editor = editor;
        this.owner = owner;
        this.document = document;
        this.documentParagraphs = documentParagraphs;
    }

    protected DocumentIdEditor(Parcel in) {
        editor = in.readByte() != 0;
        owner = in.readByte() != 0;
        document = in.readParcelable(DocumentInfo.class.getClassLoader());
        documentParagraphs = in.createTypedArrayList(ParagraphInfo.CREATOR);
    }

    public static final Creator<DocumentIdEditor> CREATOR = new Creator<DocumentIdEditor>() {
        @Override
        public DocumentIdEditor createFromParcel(Parcel in) {
            return new DocumentIdEditor(in);
        }

        @Override
        public DocumentIdEditor[] newArray(int size) {
            return new DocumentIdEditor[size];
        }
    };

    public boolean isEditor() {
        return editor;
    }

    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public DocumentInfo getDocument() {
        return document;
    }

    public void setDocument(DocumentInfo document) {
        this.document = document;
    }

    public List<ParagraphInfo> getDocumentParagraphs() {
        return documentParagraphs;
    }

    public void setDocumentParagraphs(List<ParagraphInfo> documentParagraphs) {
        this.documentParagraphs = documentParagraphs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeByte((byte) (editor ? 1 : 0));
        dest.writeByte((byte) (owner ? 1 : 0));
        dest.writeParcelable(document, flags);
        dest.writeTypedList(documentParagraphs);
    }
}
