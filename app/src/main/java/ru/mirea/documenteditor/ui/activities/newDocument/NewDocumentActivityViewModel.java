package ru.mirea.documenteditor.ui.activities.newDocument;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewDocumentActivityViewModel extends ViewModel {

    private MutableLiveData<String> mNewDocumentName;
    private NewDocumentActivityRepository newDocumentActivityRepository;


    public void init(){
        mNewDocumentName = new MutableLiveData<>();
        newDocumentActivityRepository = NewDocumentActivityRepository.getInstance();
        newDocumentActivityRepository.init();
    }

    public MutableLiveData<String> getNewDocumentName(){
        return mNewDocumentName;
    }

    public void postNewDocument(MutableLiveData<Boolean> isDone){
        newDocumentActivityRepository.postNewDocument(isDone, mNewDocumentName.getValue());
    }
}
