package ru.mirea.documenteditor.ui.activities.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WelcomeActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> isSignedIn;
    private WelcomeActivityRepository welcomeActivityRepository;

    public LiveData<Boolean> getIsSignedIn(){
        return isSignedIn;
    }

    public void init(){
        isSignedIn = new MutableLiveData<>();
        welcomeActivityRepository = WelcomeActivityRepository.getInstance();
        welcomeActivityRepository.checkLogin(isSignedIn);
    }
}
