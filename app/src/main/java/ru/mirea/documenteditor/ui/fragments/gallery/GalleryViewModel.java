package ru.mirea.documenteditor.ui.fragments.gallery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void addText(String string){
        mText.setValue(mText.getValue() + string);
    }
}