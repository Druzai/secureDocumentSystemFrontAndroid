package ru.mirea.documenteditor.ui.fragments.myProfile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.payload.AnswerBaseObj;
import ru.mirea.documenteditor.data.payload.MyUserInfo;
import ru.mirea.documenteditor.data.payload.RoleInfo;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.PreferenceManager;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;

public class MyProfileRepository {

    private static MyProfileRepository myProfileRepository;
    private PreferenceManager preferenceManager;
    private RetrofitManager retrofitManager;

    public static MyProfileRepository getInstance() {
        if (myProfileRepository == null)
            myProfileRepository = new MyProfileRepository();
        return myProfileRepository;
    }

    public void init() {
        preferenceManager = PreferenceManager.getInstance();
        retrofitManager = RetrofitManager.getInstance();
    }

    public void getUserInfo(MutableLiveData<String> username, MutableLiveData<String> roles) {
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            return;
        }
        Call<AnswerBaseObj<MyUserInfo>> call = retrofitManager.getUserService().getMe(token.get());
        call.enqueue(new Callback<AnswerBaseObj<MyUserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBaseObj<MyUserInfo>> call, @NonNull Response<AnswerBaseObj<MyUserInfo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    username.setValue((String) response.body().getResult().getUsername());
                    roles.setValue(response.body().getResult().getMyRoles().stream()
                            .map(RoleInfo::getName)
                            .collect(Collectors.joining(", "))
                    );
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBaseObj<MyUserInfo>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
            }
        });
    }
}
