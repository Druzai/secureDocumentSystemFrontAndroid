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
import ru.mirea.documenteditor.data.model.api.document.DocumentIdEditor;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfo;
import ru.mirea.documenteditor.data.model.api.document.DocumentInfoShort;
import ru.mirea.documenteditor.data.model.api.document.DocumentPassword;
import ru.mirea.documenteditor.data.model.api.user.RoleInfo;

public interface DocumentService {
    @GET("/api/document/allByUser")
    Call<AnswerBase<List<DocumentInfo>>> getDocumentsByUser(@Header("Authorization") String bearer);

    @GET("/api/document/all")
    Call<AnswerBase<List<DocumentInfoShort>>> getAllDocuments(@Header("Authorization") String bearer);

    @GET("/api/document/{id}")
    Call<AnswerBaseObj<DocumentIdEditor>> getDocument(@Header("Authorization") String bearer, @Path("id") Integer documentId);

    @POST("/api/document/new")
    Call<AnswerBase<DocumentInfo>> postNewDocument(@Header("Authorization") String bearer, @Body DocumentInfo documentInfo);

    @GET("/api/document/{id}/wsKey")
    Call<AnswerBase<String>> getDocumentKey(@Header("Authorization") String bearer, @Path("id") Integer documentId);

    @POST("/api/document/setPassword")
    Call<AnswerBase<DocumentInfo>> setPassword(@Header("Authorization") String bearer, @Body DocumentPassword documentPassword);

    @POST("/api/document/checkPassword")
    Call<AnswerBase<RoleInfo>> checkPassword(@Header("Authorization") String bearer, @Body DocumentPassword documentPassword);
}
