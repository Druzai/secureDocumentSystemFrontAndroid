package ru.mirea.documenteditor.ui.fragments.myDocuments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;

public class MyDocumentsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<DocumentInfo>> mDocumentInfo;
    private MyDocumentsRepository myDocumentsRepository;

    public MyDocumentsViewModel() {
        mDocumentInfo = new MutableLiveData<>();
        myDocumentsRepository = MyDocumentsRepository.getInstance();
        myDocumentsRepository.init();
    }

    public MutableLiveData<ArrayList<DocumentInfo>> getArrayDocumentInfo() {
        return mDocumentInfo;
    }

    public void queryDocuments() {
        myDocumentsRepository.getDocuments(mDocumentInfo);
    }
}