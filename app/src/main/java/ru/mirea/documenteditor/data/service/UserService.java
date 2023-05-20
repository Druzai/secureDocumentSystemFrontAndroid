package ru.mirea.documenteditor.data.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.mirea.documenteditor.data.model.api.base.AnswerBase;
import ru.mirea.documenteditor.data.model.api.base.AnswerBaseObj;
import ru.mirea.documenteditor.data.model.api.document.DocumentRight;
import ru.mirea.documenteditor.data.model.api.user.MyUserInfo;
import ru.mirea.documenteditor.data.model.api.user.UserIdInfo;
import ru.mirea.documenteditor.data.model.api.user.UserInfo;

public interface UserService {
    @GET("/api/user/me")
    Call<AnswerBaseObj<MyUserInfo>> getMe(@Header("Authorization") String bearer);

    @GET("/api/user/{id}")
    Call<AnswerBaseObj<UserIdInfo>> getUser(@Header("Authorization") String bearer, @Path("id") Integer userId);

    @POST("/api/user/changeRights")
    Call<AnswerBase<DocumentRight>> postChangeRights(@Header("Authorization") String bearer, @Body DocumentRight documentRight);

    @GET("/api/user/all")
    Call<AnswerBase<List<UserInfo>>> getAll();
}
