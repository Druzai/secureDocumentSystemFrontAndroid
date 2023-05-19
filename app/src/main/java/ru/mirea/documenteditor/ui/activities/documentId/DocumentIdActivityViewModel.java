package ru.mirea.documenteditor.ui.activities.documentId;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.documenteditor.data.payload.DocumentIdEditor;
import ru.mirea.documenteditor.data.payload.DocumentRight;
import ru.mirea.documenteditor.data.payload.ParagraphInfo;
import ru.mirea.documenteditor.data.payload.UserIdInfo;

public class DocumentIdActivityViewModel extends ViewModel {

    private ArrayList<ParagraphInfo> documentParagraphListInMemory;
    private MutableLiveData<ArrayList<ParagraphInfo>> mDocumentParagraphListInMemory;
    private MutableLiveData<DocumentIdEditor> mDocumentIdEditor;
    private DocumentIdActivityRepository documentIdActivityRepository;


    public void init() {
        mDocumentParagraphListInMemory = new MutableLiveData<>();
        mDocumentIdEditor = new MutableLiveData<>();
        documentIdActivityRepository = DocumentIdActivityRepository.getInstance();
        documentIdActivityRepository.init();
    }

    public ArrayList<ParagraphInfo> getDocumentParagraphListInMemory() {
        return documentParagraphListInMemory;
    }

    public void setDocumentParagraphListInMemory(List<ParagraphInfo> documentParagraphListInMemory) {
        ArrayList<ParagraphInfo> newDocumentParagraphList = new ArrayList<>();
        for (int i = 0; i < documentParagraphListInMemory.size(); i++) {
            newDocumentParagraphList.add(new ParagraphInfo(documentParagraphListInMemory.get(i)));
        }
        this.documentParagraphListInMemory = newDocumentParagraphList;
    }

    public MutableLiveData<ArrayList<ParagraphInfo>> getLdDocumentParagraphListInMemory() {
        return mDocumentParagraphListInMemory;
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
