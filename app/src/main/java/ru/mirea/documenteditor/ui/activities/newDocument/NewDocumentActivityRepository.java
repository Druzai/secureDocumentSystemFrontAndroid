package ru.mirea.documenteditor.ui.activities.newDocument;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;

public class NewDocumentActivityRepository {

    private static NewDocumentActivityRepository newDocumentActivityRepository;
    private RetrofitManager retrofitManager;

    public static NewDocumentActivityRepository getInstance() {
        if (newDocumentActivityRepository == null)
            newDocumentActivityRepository = new NewDocumentActivityRepository();
        return newDocumentActivityRepository;
    }

    public void init() {
        retrofitManager = RetrofitManager.getInstance();
    }

    public void postNewDocument(MutableLiveData<Boolean> isDone, String newName) {
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()) {
            isDone.setValue(false);
            return;
        }
        Call<AnswerBase<DocumentInfo>> call = retrofitManager.getDocumentService().postNewDocument(
                token.get(), new DocumentInfo((long) -1, newName, null, null)
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
