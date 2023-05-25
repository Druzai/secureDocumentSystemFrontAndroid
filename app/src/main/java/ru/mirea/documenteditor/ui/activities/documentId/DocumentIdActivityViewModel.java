package ru.mirea.documenteditor.ui.activities.documentId;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.documenteditor.data.model.api.document.DocumentIdEditor;
import ru.mirea.documenteditor.data.model.api.document.ParagraphInfo;
import ru.mirea.documenteditor.util.ThrottledLiveData;

public class DocumentIdActivityViewModel extends ViewModel {

    private ArrayList<ParagraphInfo> documentParagraphListInMemory;
    private ThrottledLiveData<DocumentIdEditor> tmDocumentIdEditor;
    private MutableLiveData<DocumentIdEditor> mDocumentIdEditor;
    private DocumentIdActivityRepository documentIdActivityRepository;


    public void init() {
        mDocumentIdEditor = new MutableLiveData<>();
        tmDocumentIdEditor = new ThrottledLiveData<>(mDocumentIdEditor, 100L);
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

    public MutableLiveData<DocumentIdEditor> getDocumentIdEditor() {
        return mDocumentIdEditor;
    }

    public ThrottledLiveData<DocumentIdEditor> getTmDocumentIdEditor(){
        return tmDocumentIdEditor;
    }

    public void fetchDocumentKey(MutableLiveData<Boolean> isDone, Integer documentId) {
        documentIdActivityRepository.getDocumentKey(isDone, documentId);
    }

    public void getDocument(Integer documentId) {
        documentIdActivityRepository.getDocumentIdEditor(mDocumentIdEditor, documentId);
    }
}
