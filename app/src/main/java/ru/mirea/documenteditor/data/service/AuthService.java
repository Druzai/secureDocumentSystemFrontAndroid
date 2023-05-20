package ru.mirea.documenteditor.data.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.auth.AuthenticationPayload;

public interface AuthService {
    @Headers({"Accept: application/json"})
    @POST("/api/auth/register")
    Call<AnswerBase<String>> postRegister(@Body AuthenticationPayload authenticationPayload);

    @Headers({"Accept: application/json"})
    @POST("/api/auth/signin")
    Call<AnswerBase<String>> postSignIn(@Body AuthenticationPayload authenticationPayload);

    @Headers({"Accept: application/json"})
    @GET("/api/auth/refresh")
    Call<AnswerBase<String>> getRefresh(@Header("Authorization") String bearer);
}
