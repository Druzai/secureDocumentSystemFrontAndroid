package ru.mirea.documenteditor.ui.activities.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.model.auth.LoginModel;
import ru.mirea.documenteditor.data.model.auth.RegisterModel;
import ru.mirea.documenteditor.data.payload.AnswerBase;
import ru.mirea.documenteditor.data.payload.AuthenticationPayload;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.PreferenceManager;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;

public class AuthActivityRepository {

    private static AuthActivityRepository instance;
    private PreferenceManager preferenceManager;
    private RetrofitManager retrofitManager;

    public static AuthActivityRepository getInstance() {
        if (instance == null)
            instance = new AuthActivityRepository();
        return instance;
    }

    public void init() {
        preferenceManager = PreferenceManager.getInstance();
        retrofitManager = RetrofitManager.getInstance();
    }

    public void registerUser(RegisterModel registerModel, OnRegistrationCallback onRegistrationCallback) {
        Call<AnswerBase<String>> call = retrofitManager.getAuthService().postRegister(
                new AuthenticationPayload(registerModel.getUsername(), registerModel.getPassword())
        );
        call.enqueue(new Callback<AnswerBase<String>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<String>> call, @NonNull Response<AnswerBase<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    registerModel.setIsRegisterValid(true);
                    onRegistrationCallback.onRegisterCallback(registerModel);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<String>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                onRegistrationCallback.onRegisterCallback(registerModel);
            }
        });
    }

    public void loginUser(LoginModel loginModel, OnLoginCallback onLoginCallback) {
        Call<AnswerBase<String>> call = retrofitManager.getAuthService().postSignIn(
                new AuthenticationPayload(loginModel.getUsername(), loginModel.getPassword())
        );
        call.enqueue(new Callback<AnswerBase<String>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<String>> call, @NonNull Response<AnswerBase<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    handleLogin(loginModel, response.body().getResult(), onLoginCallback);
                } else {
                    onLoginCallback.onLoginCallback(loginModel);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<String>> call, @NonNull Throwable t) {
                Log.d(Constants.LOG_TAG, t.getMessage());
                onLoginCallback.onLoginCallback(loginModel);
            }
        });
    }

    private void handleLogin(LoginModel loginModel, Map<String, String> result, OnLoginCallback onLoginCallback) {
        preferenceManager.putString(Constants.USER_NAME, result.get("username"));
        preferenceManager.putString(Constants.ACCESS_KEY, result.get("accessToken"));
        preferenceManager.putString(Constants.REFRESH_KEY, result.get("refreshToken"));
        loginModel.setIsLoginValid(true);
        onLoginCallback.onLoginCallback(loginModel);
    }

    public void getUserKey(MutableLiveData<Boolean> isSignedIn){
        Utilities.fetchUserKey(isSignedIn);
    }
}
