package ru.mirea.documenteditor.ui.activities.userInfoId;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.payload.AnswerBase;
import ru.mirea.documenteditor.data.payload.AnswerBaseObj;
import ru.mirea.documenteditor.data.payload.DocumentRight;
import ru.mirea.documenteditor.data.payload.UserIdInfo;
import ru.mirea.documenteditor.data.payload.UserInfo;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;

public class UserInfoIdActivityRepository {

    private static UserInfoIdActivityRepository userInfoIdActivityRepository;
    private RetrofitManager retrofitManager;

    public static UserInfoIdActivityRepository getInstance() {
        if (userInfoIdActivityRepository == null)
            userInfoIdActivityRepository = new UserInfoIdActivityRepository();
        return userInfoIdActivityRepository;
    }

    public void init() {
        retrofitManager = RetrofitManager.getInstance();
    }

    public void getUserInfoId(MutableLiveData<UserIdInfo> userIdInfo, Integer userId){
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            return;
        }
        Call<AnswerBaseObj<UserIdInfo>> call = retrofitManager.getUserService().getUser(
                token.get(), Long.valueOf(userId)
        );
        call.enqueue(new Callback<AnswerBaseObj<UserIdInfo>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBaseObj<UserIdInfo>> call, @NonNull Response<AnswerBaseObj<UserIdInfo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    userIdInfo.setValue(response.body().getResult());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBaseObj<UserIdInfo>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
            }
        });
    }

    public void postRights(MutableLiveData<Boolean> isDone, DocumentRight documentRight) {
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            return;
        }
        Call<AnswerBase<DocumentRight>> call = retrofitManager.getUserService().postChangeRights(
                token.get(), documentRight
        );
        call.enqueue(new Callback<AnswerBase<DocumentRight>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<DocumentRight>> call, @NonNull Response<AnswerBase<DocumentRight>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    isDone.setValue(true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<DocumentRight>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                isDone.setValue(false);
            }
        });
    }
}
