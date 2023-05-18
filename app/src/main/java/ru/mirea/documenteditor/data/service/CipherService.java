package ru.mirea.documenteditor.data.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import ru.mirea.documenteditor.data.payload.AnswerBase;

public interface CipherService {
    @GET("/api/aes/key")
    Call<AnswerBase<String>> getKey(@Header("Authorization") String bearer);
}
