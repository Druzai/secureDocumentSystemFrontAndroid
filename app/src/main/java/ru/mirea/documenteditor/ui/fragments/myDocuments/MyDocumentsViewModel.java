package ru.mirea.documenteditor.ui.fragments.myDocuments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.documenteditor.data.payload.DocumentInfo;
import ru.mirea.documenteditor.data.payload.UserIdInfo;
import ru.mirea.documenteditor.ui.activities.userInfoId.UserInfoIdActivityRepository;

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