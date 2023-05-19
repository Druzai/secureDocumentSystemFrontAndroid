package ru.mirea.documenteditor.ui.activities.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.documenteditor.data.model.auth.LoginModel;
import ru.mirea.documenteditor.data.model.auth.RegisterModel;

public class AuthActivityViewModel extends ViewModel implements OnLoginCallback, OnRegistrationCallback{

    private MutableLiveData<Boolean> isRegisterLoading;
    private MutableLiveData<Boolean> isLoginLoading;
    private MutableLiveData<Boolean> gotUserKey;
    private MutableLiveData<LoginModel> loginModel;
    private MutableLiveData<RegisterModel> registerModel;
    private AuthActivityRepository authActivityRepository;

    public void init(){
        authActivityRepository = AuthActivityRepository.getInstance();
        authActivityRepository.init();
        isRegisterLoading = new MutableLiveData<>();
        isLoginLoading = new MutableLiveData<>();
        gotUserKey = new MutableLiveData<>();
        loginModel = new MutableLiveData<>();
        registerModel = new MutableLiveData<>();
    }

    public LiveData<Boolean> getIsRegisterLoading(){
        return isRegisterLoading;
    }

    public LiveData<Boolean> getIsLoginLoading(){
        return isLoginLoading;
    }

    public LiveData<LoginModel> getLoginModel(){
        return loginModel;
    }

    public MutableLiveData<RegisterModel> getRegisterModel(){
        return registerModel;
    }

    public LiveData<Boolean> getGotUserKey() {
        return gotUserKey;
    }

    public void registerUser(String username, String password, String passwordConfirm){
        isRegisterLoading.setValue(true);
        RegisterModel currentRegisterModel = new RegisterModel(username, password, passwordConfirm);
        authActivityRepository.registerUser(currentRegisterModel, this);
    }

    public void loginUser(String username, String password){
        isLoginLoading.setValue(true);
        LoginModel currentUserLoginModel = new LoginModel(username, password);
        authActivityRepository.loginUser(currentUserLoginModel, this);
    }

    public void fetchUserKey() {
        authActivityRepository.getUserKey(gotUserKey);
    }

    @Override
    public void onLoginCallback(LoginModel currentLoginModel) {
        loginModel.setValue(currentLoginModel);
        isLoginLoading.setValue(false);
    }

    @Override
    public void onRegisterCallback(RegisterModel currentRegisterModel) {
        registerModel.setValue(currentRegisterModel);
        isRegisterLoading.setValue(false);
    }
}
