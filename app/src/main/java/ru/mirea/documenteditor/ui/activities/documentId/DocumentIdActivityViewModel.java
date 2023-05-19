package ru.mirea.documenteditor.ui.activities.documentId;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.mirea.documenteditor.data.payload.DocumentIdEditor;
import ru.mirea.documenteditor.data.payload.DocumentRight;
import ru.mirea.documenteditor.data.payload.ParagraphInfo;
import ru.mirea.documenteditor.data.payload.UserIdInfo;

public class DocumentIdActivityViewModel extends ViewModel {

    private ArrayList<ParagraphInfo> documentParagraphListInMemory;
    private MutableLiveData<DocumentIdEditor> mDocumentIdEditor;
    private DocumentIdActivityRepository documentIdActivityRepository;


    public void init() {
        mDocumentIdEditor = new MutableLiveData<>();
        documentIdActivityRepository = DocumentIdActivityRepository.getInstance();
        documentIdActivityRepository.init();
    }

    public ArrayList<ParagraphInfo> getDocumentParagraphListInMemory() {
        return documentParagraphListInMemory;
    }

    public void setDocumentParagraphListInMemory(ArrayList<ParagraphInfo> documentParagraphListInMemory) {
        this.documentParagraphListInMemory = documentParagraphListInMemory;
    }

    public MutableLiveData<DocumentIdEditor> getDocumentIdEditor() {
        return mDocumentIdEditor;
    }

    public void fetchDocumentKey(MutableLiveData<Boolean> isDone, Integer documentId) {
        documentIdActivityRepository.getDocumentKey(isDone, documentId);
    }

    public void getDocument(Integer documentId) {
        documentIdActivityRepository.getDocumentIdEditor(mDocumentIdEditor, documentId);
    }
}
