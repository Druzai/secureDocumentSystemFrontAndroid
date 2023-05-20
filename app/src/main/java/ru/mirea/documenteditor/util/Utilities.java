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
import ru.mirea.documenteditor.data.payload.InputMessage;
import ru.mirea.documenteditor.data.payload.MyUserInfo;
import ru.mirea.documenteditor.util.aes.Cipher;

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
                        return;
                    }
                    isSignedIn.setValue(false);
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

    public static Optional<String> getAuthorizationBearer() {
        String accessKey = PreferenceManager.getInstance().getString(Constants.ACCESS_KEY);
        if (accessKey == null) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(RetrofitManager.getInstance().makeHeaderBearer(accessKey));
        }
    }

    public static void checkIfUserKeyValid(MutableLiveData<Boolean> isValid) {
        Optional<String> token = getAuthorizationBearer();
        if (!token.isPresent()) {
            isValid.setValue(false);
            return;
        }
        String userKey = PreferenceManager.getInstance().getString(Constants.USER_KEY);
        if (userKey == null) {
            fetchUserKey(isValid);
            return;
        }
        if (!CipherManager.getInstance().isKeyValid(userKey)){
            CipherManager.getInstance().deleteCipher(userKey);
            fetchUserKey(isValid);
            return;
        }
        Call<AnswerBaseObj<InputMessage>> call = RetrofitManager.getInstance().getCipherService().processText(
                token.get(), new InputMessage(Constants.TEST_MESSAGE, true)
        );
        call.enqueue(new Callback<AnswerBaseObj<InputMessage>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBaseObj<InputMessage>> call, @NonNull Response<AnswerBaseObj<InputMessage>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    Cipher cipher = CipherManager.getInstance().getCipher(userKey);
                    String decodedMessage = cipher.decrypt(response.body().getResult().getText());
                    if (decodedMessage.equals(Constants.TEST_MESSAGE)) {
                        isValid.setValue(true);
                    } else {
                        fetchUserKey(isValid);
                    }
                    return;
                } else if (response.body() != null) {
                    Log.e(Constants.LOG_TAG, response.body().getError());
                }
                isValid.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBaseObj<InputMessage>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                isValid.setValue(false);
            }
        });
    }

    public static void fetchUserKey(MutableLiveData<Boolean> isValid) {
        Optional<String> token = getAuthorizationBearer();
        if (!token.isPresent()) {
            isValid.setValue(false);
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
                    isValid.setValue(true);
                    return;
                } else if (response.body() != null) {
                    Log.e(Constants.LOG_TAG, response.body().getError());
                }
                isValid.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<String>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                isValid.setValue(false);
            }
        });
    }
}
