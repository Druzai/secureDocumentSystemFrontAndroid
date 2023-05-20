package ru.mirea.documenteditor.ui.fragments.allUsers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.user.UserInfo;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.RetrofitManager;

public class AllUsersRepository {
    private static AllUsersRepository allUsersRepository;
    private RetrofitManager retrofitManager;

    public static AllUsersRepository getInstance() {
        if (allUsersRepository == null)
            allUsersRepository = new AllUsersRepository();
        return allUsersRepository;
    }

    public void init() {
        retrofitManager = RetrofitManager.getInstance();
    }

    public void getUsers(MutableLiveData<ArrayList<UserInfo>> arrayUserInfo) {
        Call<AnswerBase<List<UserInfo>>> call = retrofitManager.getUserService().getAll();
        call.enqueue(new Callback<AnswerBase<List<UserInfo>>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<List<UserInfo>>> call, @NonNull Response<AnswerBase<List<UserInfo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    arrayUserInfo.setValue((ArrayList<UserInfo>) response.body().getResult().get("users"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<List<UserInfo>>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
            }
        });
    }
}
