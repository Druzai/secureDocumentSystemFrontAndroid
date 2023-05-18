package ru.mirea.documenteditor.data.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.mirea.documenteditor.data.payload.AnswerBase;
import ru.mirea.documenteditor.data.payload.DocumentRight;
import ru.mirea.documenteditor.data.payload.UserInfo;

public interface UserService {
    @GET("/api/user/me")
    Call<AnswerBase<Object>> getMe(@Header("Authorization") String bearer);

    @GET("/api/user/{id}")
    Call<AnswerBase<Object>> getUser(@Header("Authorization") String bearer, @Path("id") Long userId);

    @POST("/api/user/changeRights")
    Call<AnswerBase<Object>> postChangeRights(@Header("Authorization") String bearer, @Body DocumentRight documentRight);

    @GET("/api/user/all")
    Call<AnswerBase<List<UserInfo>>> getAll();
}
