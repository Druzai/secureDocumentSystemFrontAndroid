package ru.mirea.documenteditor.ui.fragments.addDocumentPassword;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfoShort;
import ru.mirea.documenteditor.data.model.api.document.DocumentPassword;
import ru.mirea.documenteditor.data.model.api.user.RoleInfo;

public class AddDocumentPasswordViewModel extends ViewModel {

    private MutableLiveData<List<DocumentInfoShort>> mListDocumentInfoShort;
    private AddDocumentPasswordRepository addDocumentPasswordRepository;

    public void init() {
        mListDocumentInfoShort = new MutableLiveData<>();
        addDocumentPasswordRepository = AddDocumentPasswordRepository.getInstance();
        addDocumentPasswordRepository.init();
    }

    public MutableLiveData<List<DocumentInfoShort>> getListDocumentInfoShort() {
        return mListDocumentInfoShort;
    }

    public void fetchListDocumentInfoShort() {
        addDocumentPasswordRepository.getListDocumentInfoShort(mListDocumentInfoShort);
    }

    public void postCheckPassword(MutableLiveData<RoleInfo> documentInfo, DocumentPassword documentPassword) {
        addDocumentPasswordRepository.postCheckPassword(documentInfo, documentPassword);
    }
}