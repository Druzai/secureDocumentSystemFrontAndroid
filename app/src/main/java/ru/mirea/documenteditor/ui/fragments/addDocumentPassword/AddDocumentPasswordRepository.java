package ru.mirea.documenteditor.ui.fragments.addDocumentPassword;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.base.AnswerBaseObj;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfoShort;
import ru.mirea.documenteditor.data.model.api.document.DocumentPassword;
import ru.mirea.documenteditor.data.model.api.user.MyUserInfo;
import ru.mirea.documenteditor.data.model.api.user.RoleInfo;
import ru.mirea.documenteditor.util.CipherManager;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.PreferenceManager;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;
import ru.mirea.documenteditor.util.aes.Cipher;

public class AddDocumentPasswordRepository {

    private static AddDocumentPasswordRepository addDocumentPasswordRepository;
    private RetrofitManager retrofitManager;
    private PreferenceManager preferenceManager;
    private CipherManager cipherManager;

    public static AddDocumentPasswordRepository getInstance() {
        if (addDocumentPasswordRepository == null)
            addDocumentPasswordRepository = new AddDocumentPasswordRepository();
        return addDocumentPasswordRepository;
    }

    public void init() {
        retrofitManager = RetrofitManager.getInstance();
        preferenceManager = PreferenceManager.getInstance();
        cipherManager = CipherManager.getInstance();
    }

    public void getListDocumentInfoShort(MutableLiveData<List<DocumentInfoShort>> listDocumentInfoShort){
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            return;
        }
        Call<AnswerBase<List<DocumentInfoShort>>> call = retrofitManager.getDocumentService().getAllDocuments(
                token.get()
        );
        call.enqueue(new Callback<AnswerBase<List<DocumentInfoShort>>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<List<DocumentInfoShort>>> call, @NonNull Response<AnswerBase<List<DocumentInfoShort>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    listDocumentInfoShort.setValue(response.body().getResult().get("documents"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<List<DocumentInfoShort>>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
            }
        });
    }

    public void postCheckPassword(MutableLiveData<RoleInfo> roleInfo, DocumentPassword documentPassword) {
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            roleInfo.setValue(null);
            return;
        }
        Cipher cipher = cipherManager.getCipher(preferenceManager.getString(Constants.USER_KEY));
        documentPassword.setPassword(cipher.encrypt(documentPassword.getPassword()));
        Call<AnswerBase<RoleInfo>> call = retrofitManager.getDocumentService().checkPassword(
                token.get(), documentPassword
        );
        call.enqueue(new Callback<AnswerBase<RoleInfo>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<RoleInfo>> call, @NonNull Response<AnswerBase<RoleInfo>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    roleInfo.setValue(response.body().getResult().get("role"));
                } else {
                    roleInfo.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<RoleInfo>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                roleInfo.setValue(null);
            }
        });
    }
}
