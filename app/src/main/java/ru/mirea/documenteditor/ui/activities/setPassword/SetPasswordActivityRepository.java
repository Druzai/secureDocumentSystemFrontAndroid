package ru.mirea.documenteditor.ui.activities.setPassword;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.base.AnswerBaseObj;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;
import ru.mirea.documenteditor.data.model.api.document.DocumentPassword;
import ru.mirea.documenteditor.data.model.api.document.DocumentRight;
import ru.mirea.documenteditor.data.model.api.user.RoleInfo;
import ru.mirea.documenteditor.data.model.api.user.UserIdInfo;
import ru.mirea.documenteditor.util.CipherManager;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.PreferenceManager;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;
import ru.mirea.documenteditor.util.aes.Cipher;

public class SetPasswordActivityRepository {

    private static SetPasswordActivityRepository setPasswordActivityRepository;
    private RetrofitManager retrofitManager;
    private PreferenceManager preferenceManager;
    private CipherManager cipherManager;

    public static SetPasswordActivityRepository getInstance() {
        if (setPasswordActivityRepository == null)
            setPasswordActivityRepository = new SetPasswordActivityRepository();
        return setPasswordActivityRepository;
    }

    public void init() {
        retrofitManager = RetrofitManager.getInstance();
        preferenceManager = PreferenceManager.getInstance();
        cipherManager = CipherManager.getInstance();
    }

    public void getListRoleInfo(MutableLiveData<List<RoleInfo>> listRoleInfo){
        Call<AnswerBase<List<RoleInfo>>> call = retrofitManager.getUserService().getRoleRights();
        call.enqueue(new Callback<AnswerBase<List<RoleInfo>>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<List<RoleInfo>>> call, @NonNull Response<AnswerBase<List<RoleInfo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    listRoleInfo.setValue(response.body().getResult().get("allRoles"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<List<RoleInfo>>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
            }
        });
    }

    public void postSetPassword(MutableLiveData<Boolean> isDone, DocumentPassword documentPassword) {
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            isDone.setValue(false);
            return;
        }
        Cipher cipher = cipherManager.getCipher(preferenceManager.getString(Constants.USER_KEY));
        documentPassword.setPassword(cipher.encrypt(documentPassword.getPassword()));
        Call<AnswerBase<DocumentInfo>> call = retrofitManager.getDocumentService().setPassword(
                token.get(), documentPassword
        );
        call.enqueue(new Callback<AnswerBase<DocumentInfo>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<DocumentInfo>> call, @NonNull Response<AnswerBase<DocumentInfo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    isDone.setValue(true);
                } else {
                    isDone.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<DocumentInfo>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                isDone.setValue(false);
            }
        });
    }
}
