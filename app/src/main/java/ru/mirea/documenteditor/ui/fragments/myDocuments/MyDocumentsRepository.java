package ru.mirea.documenteditor.ui.fragments.myDocuments;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;

public class MyDocumentsRepository {
    private static MyDocumentsRepository myDocumentsRepository;
    private RetrofitManager retrofitManager;

    public static MyDocumentsRepository getInstance() {
        if (myDocumentsRepository == null)
            myDocumentsRepository = new MyDocumentsRepository();
        return myDocumentsRepository;
    }

    public void init() {
        retrofitManager = RetrofitManager.getInstance();
    }

    public void getDocuments(MutableLiveData<ArrayList<DocumentInfo>> arrayDocumentInfo) {
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            return;
        }
        Call<AnswerBase<List<DocumentInfo>>> call = retrofitManager.getDocumentService().getDocuments(
                token.get()
        );
        call.enqueue(new Callback<AnswerBase<List<DocumentInfo>>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<List<DocumentInfo>>> call, @NonNull Response<AnswerBase<List<DocumentInfo>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    arrayDocumentInfo.setValue((ArrayList<DocumentInfo>) response.body().getResult().get("documents"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<List<DocumentInfo>>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
            }
        });
    }
}
