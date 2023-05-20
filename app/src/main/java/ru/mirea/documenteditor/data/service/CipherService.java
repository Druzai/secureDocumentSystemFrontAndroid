package ru.mirea.documenteditor.data.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.base.AnswerBaseObj;
import ru.mirea.documenteditor.data.model.api.cipher.InputMessage;

public interface CipherService {
    @POST("/api/aes/text")
    Call<AnswerBaseObj<InputMessage>> processText(@Header("Authorization") String bearer, @Body InputMessage inputMessage);
    @GET("/api/aes/key")
    Call<AnswerBase<String>> getKey(@Header("Authorization") String bearer);
}
