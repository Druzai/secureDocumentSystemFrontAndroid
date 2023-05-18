package ru.mirea.documenteditor.ui.activities.welcome;

import androidx.lifecycle.MutableLiveData;

import ru.mirea.documenteditor.util.Utilities;

public class WelcomeActivityRepository {

    private static WelcomeActivityRepository welcomeActivityRepository;

    public static WelcomeActivityRepository getInstance() {
        if (welcomeActivityRepository == null)
            welcomeActivityRepository = new WelcomeActivityRepository();
        return welcomeActivityRepository;
    }

    public void checkLogin(MutableLiveData<Boolean> isSignedIn) {
        Utilities.checkIfLoggedIn(isSignedIn);
    }
}
