package ru.mirea.documenteditor.ui.fragments.myProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyProfileViewModel extends ViewModel {

    private MutableLiveData<String> usernameText;
    private MutableLiveData<String> rolesText;
    private MyProfileRepository myProfileRepository;

    public void init() {
        usernameText = new MutableLiveData<>();
        rolesText = new MutableLiveData<>();
        myProfileRepository = MyProfileRepository.getInstance();
        myProfileRepository.init();
    }

    public LiveData<String> getUsernameText(){
        return usernameText;
    }

    public LiveData<String> getRolesText(){
        return rolesText;
    }

    public void getUserInfo() {
        myProfileRepository.getUserInfo(usernameText, rolesText);
    }
}