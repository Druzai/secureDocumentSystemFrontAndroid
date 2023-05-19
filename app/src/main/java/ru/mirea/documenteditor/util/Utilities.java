package ru.mirea.documenteditor.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.payload.AnswerBase;
import ru.mirea.documenteditor.data.payload.AnswerBaseObj;
import ru.mirea.documenteditor.data.payload.MyUserInfo;

public class Utilities {
    public static void checkIfLoggedIn(MutableLiveData<Boolean> isSignedIn) {
        String accessKey = PreferenceManager.getInstance().getString(Constants.ACCESS_KEY);
        if (accessKey != null) {
            Call<AnswerBaseObj<MyUserInfo>> call = RetrofitManager.getInstance().getUserService().getMe(
                    RetrofitManager.getInstance().makeHeaderBearer(accessKey)
            );
            call.enqueue(new Callback<AnswerBaseObj<MyUserInfo>>() {
                @Override
                public void onResponse(@NonNull Call<AnswerBaseObj<MyUserInfo>> call, @NonNull Response<AnswerBaseObj<MyUserInfo>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                        PreferenceManager.getInstance().putString(Constants.USER_NAME, response.body().getResult().getUsername());
                        isSignedIn.setValue(true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AnswerBaseObj<MyUserInfo>> call, @NonNull Throwable t) {
                    Log.e(Constants.LOG_TAG, t.getMessage());
                    isSignedIn.setValue(false);
                }
            });
        }
        refreshTokens(isSignedIn);
    }

    public static void refreshTokens(MutableLiveData<Boolean> isSignedIn) {
        String refreshKey = PreferenceManager.getInstance().getString(Constants.REFRESH_KEY);
        if (refreshKey == null) {
            isSignedIn.setValue(false);
            return;
        }
        Call<AnswerBase<String>> call = RetrofitManager.getInstance().getAuthService().getRefresh(
                RetrofitManager.getInstance().makeHeaderBearer(refreshKey)
        );
        call.enqueue(new Callback<AnswerBase<String>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<String>> call, @NonNull Response<AnswerBase<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    PreferenceManager.getInstance().putString(Constants.USER_NAME, (String) response.body().getResult().get("username"));
                    PreferenceManager.getInstance().putString(Constants.ACCESS_KEY, response.body().getResult().get("accessToken"));
                    PreferenceManager.getInstance().putString(Constants.REFRESH_KEY, response.body().getResult().get("refreshToken"));
                    isSignedIn.setValue(true);
                } else if (response.body() != null) {
                    Log.e(Constants.LOG_TAG, response.body().getError());
                    isSignedIn.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<String>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                isSignedIn.setValue(false);
            }
        });
    }

    public static Optional<String> getAuthorizationBearer() {
        String accessKey = PreferenceManager.getInstance().getString(Constants.ACCESS_KEY);
        if (accessKey == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(RetrofitManager.getInstance().makeHeaderBearer(accessKey));
        }
    }

    public static void fetchUserKey(MutableLiveData<Boolean> isSignedIn) {
        Optional<String> token = getAuthorizationBearer();
        if (!token.isPresent()) {
            isSignedIn.setValue(false);
            return;
        }
        Call<AnswerBase<String>> call = RetrofitManager.getInstance().getCipherService().getKey(
                token.get()
        );
        call.enqueue(new Callback<AnswerBase<String>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<String>> call, @NonNull Response<AnswerBase<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    PreferenceManager.getInstance().putString(Constants.USER_KEY, response.body().getResult().get("key"));
                    isSignedIn.setValue(true);
                    return;
                } else if (response.body() != null) {
                    Log.e(Constants.LOG_TAG, response.body().getError());
                }
                isSignedIn.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<String>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                isSignedIn.setValue(false);
            }
        });
    }
}
