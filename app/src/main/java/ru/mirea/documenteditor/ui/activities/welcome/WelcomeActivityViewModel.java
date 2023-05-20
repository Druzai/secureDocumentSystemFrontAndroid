package ru.mirea.documenteditor.ui.activities.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WelcomeActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> isSignedIn;
    private MutableLiveData<Boolean> gotUserKey;
    private WelcomeActivityRepository welcomeActivityRepository;

    public LiveData<Boolean> getIsSignedIn() {
        return isSignedIn;
    }

    public LiveData<Boolean> getGotUserKey() {
        return gotUserKey;
    }

    public void init() {
        isSignedIn = new MutableLiveData<>();
        gotUserKey = new MutableLiveData<>();
        welcomeActivityRepository = WelcomeActivityRepository.getInstance();
        welcomeActivityRepository.checkLogin(isSignedIn);
    }

    public void checkUserKey() {
        welcomeActivityRepository.checkUserKey(gotUserKey);
    }
}
