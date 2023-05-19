package ru.mirea.documenteditor.data.payload;

import java.util.List;

public class DocumentIdEditor {
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
}
