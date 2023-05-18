package ru.mirea.documenteditor.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.payload.AnswerBase;

public class Utilities {
    public static void checkIfLoggedIn(MutableLiveData<Boolean> isSignedIn) {
        String accessKey = PreferenceManager.getInstance().getString(Constants.ACCESS_KEY);
        if (accessKey != null) {
            Call<AnswerBase<Object>> call = RetrofitManager.getInstance().getUserService().getMe(
                    RetrofitManager.getInstance().makeHeaderBearer(accessKey)
            );
            call.enqueue(new Callback<AnswerBase<Object>>() {
                @Override
                public void onResponse(@NonNull Call<AnswerBase<Object>> call, @NonNull Response<AnswerBase<Object>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                        PreferenceManager.getInstance().putString(Constants.USER_NAME, (String) response.body().getResult().get("username"));
                        isSignedIn.setValue(true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AnswerBase<Object>> call, @NonNull Throwable t) {
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

    /*
    public static Optional<String> checkIfLoggedIn() {
        String accessKey = PreferenceManager.getInstance().getString(Constants.ACCESS_KEY);
        if (accessKey != null) {
            Call<AnswerBase<Object>> call = RetrofitManager.getInstance().getUserService().getMe(
                    RetrofitManager.getInstance().makeHeaderBearer(accessKey)
            );
            try {
                Response<AnswerBase<Object>> response = call.execute();
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    return Optional.of((String) Objects.requireNonNull(response.body().getResult().get("username")));
                }
            } catch (IOException | RuntimeException e) {
                Log.e(Constants.LOG_TAG, e.getMessage());
            }
        }
        return refreshTokens();
    }

    public static Optional<String> refreshTokens() {
        String refreshKey = PreferenceManager.getInstance().getString(Constants.REFRESH_KEY);
        if (refreshKey == null) {
            return Optional.empty();
        }
        Call<AnswerBase<String>> call = RetrofitManager.getInstance().getAuthService().getRefresh(
                RetrofitManager.getInstance().makeHeaderBearer(refreshKey)
        );
        try {
            Response<AnswerBase<String>> response = call.execute();
            if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                PreferenceManager.getInstance().putString(Constants.ACCESS_KEY, response.body().getResult().get("accessToken"));
                PreferenceManager.getInstance().putString(Constants.REFRESH_KEY, response.body().getResult().get("refreshToken"));
                return Optional.ofNullable(response.body().getResult().get("username"));
            } else if (response.body() != null) {
                Log.e(Constants.LOG_TAG, response.body().getError());
                return Optional.empty();
            }
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
        }
        return Optional.empty();
    }
    * */

    public static String getAuthorizationBearer(){
        String accessKey = PreferenceManager.getInstance().getString(Constants.ACCESS_KEY);
        return RetrofitManager.getInstance().makeHeaderBearer(accessKey);
    }

    public static boolean fetchUserKey() {
        Call<AnswerBase<String>> call = RetrofitManager.getInstance().getCipherService().getKey(
                getAuthorizationBearer()
        );
        try {
            Response<AnswerBase<String>> response = call.execute();
            if (response.code() == 401){
                return false;
            }
            if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                PreferenceManager.getInstance().putString(Constants.USER_KEY, response.body().getResult().get("key"));
                return true;
            } else if (response.body() != null) {
                Log.e(Constants.LOG_TAG, response.body().getError());
                return false;
            }
        } catch (IOException e) {
            Log.e(Constants.LOG_TAG, e.getMessage());
        }
        return false;
    }
}
