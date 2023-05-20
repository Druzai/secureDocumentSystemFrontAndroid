package ru.mirea.documenteditor.ui.activities.documentId;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.base.AnswerBaseObj;
import ru.mirea.documenteditor.data.model.api.document.DocumentIdEditor;
import ru.mirea.documenteditor.data.model.api.document.ParagraphInfo;
import ru.mirea.documenteditor.util.CipherManager;
import ru.mirea.documenteditor.util.Constants;
import ru.mirea.documenteditor.util.PreferenceManager;
import ru.mirea.documenteditor.util.RetrofitManager;
import ru.mirea.documenteditor.util.Utilities;
import ru.mirea.documenteditor.util.aes.Cipher;

public class DocumentIdActivityRepository {

    private static DocumentIdActivityRepository documentIdActivityRepository;
    private RetrofitManager retrofitManager;
    private PreferenceManager preferenceManager;
    private CipherManager cipherManager;

    public static DocumentIdActivityRepository getInstance() {
        if (documentIdActivityRepository == null)
            documentIdActivityRepository = new DocumentIdActivityRepository();
        return documentIdActivityRepository;
    }

    public void init() {
        retrofitManager = RetrofitManager.getInstance();
        preferenceManager = PreferenceManager.getInstance();
        cipherManager = CipherManager.getInstance();
    }

    public void getDocumentIdEditor(MutableLiveData<DocumentIdEditor> documentIdEditor, Integer documentId){
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            return;
        }
        Call<AnswerBaseObj<DocumentIdEditor>> call = retrofitManager.getDocumentService().getDocument(
                token.get(), documentId
        );
        call.enqueue(new Callback<AnswerBaseObj<DocumentIdEditor>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBaseObj<DocumentIdEditor>> call, @NonNull Response<AnswerBaseObj<DocumentIdEditor>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    Cipher cipher = cipherManager.getCipher(preferenceManager.getString(Constants.USER_KEY));
                    DocumentIdEditor documentIdEditorResponse = response.body().getResult();
                    documentIdEditorResponse.getDocument().setLastEditBy(
                            cipher.decrypt(documentIdEditorResponse.getDocument().getLastEditBy())
                    );
                    documentIdEditorResponse.setDocumentParagraphs(
                            documentIdEditorResponse.getDocumentParagraphs().stream()
                                    .map(p -> new ParagraphInfo(
                                            p.getNumber(),
                                            cipher.decrypt(p.getContent()),
                                            cipher.decrypt(p.getAlign())
                                    ))
                                    .collect(Collectors.toList())
                    );
                    documentIdEditor.setValue(documentIdEditorResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBaseObj<DocumentIdEditor>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
            }
        });
    }

    public void getDocumentKey(MutableLiveData<Boolean> isDone, Integer documentId) {
        Optional<String> token = Utilities.getAuthorizationBearer();
        if (!token.isPresent()){
            isDone.setValue(false);
            return;
        }
        Call<AnswerBase<String>> call = retrofitManager.getDocumentService().getDocumentKey(
                token.get(), documentId
        );
        call.enqueue(new Callback<AnswerBase<String>>() {
            @Override
            public void onResponse(@NonNull Call<AnswerBase<String>> call, @NonNull Response<AnswerBase<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null) {
                    cipherManager.deleteCipher(preferenceManager.getString(Constants.DOCUMENT_WS_KEY));

                    Cipher cipher = cipherManager.getCipher(preferenceManager.getString(Constants.USER_KEY));
                    String decodedKey = cipher.decrypt(Objects.requireNonNull(response.body().getResult().get("documentWsKey")));
                    preferenceManager.putString(Constants.DOCUMENT_WS_KEY, decodedKey);
                    isDone.setValue(true);
                } else {
                    isDone.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AnswerBase<String>> call, @NonNull Throwable t) {
                Log.e(Constants.LOG_TAG, t.getMessage());
                isDone.setValue(false);
            }
        });
    }
}
