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

public interface DocumentService {
    @GET("/api/document/allByUser")
    Call<AnswerBase<List<DocumentInfo>>> getDocuments(@Header("Authorization") String bearer);

    @GET("/api/document/{id}")
    Call<AnswerBaseObj<DocumentIdEditor>> getDocument(@Header("Authorization") String bearer, @Path("id") Integer documentId);

    @POST("/api/document/new")
    Call<AnswerBase<DocumentInfo>> postNewDocument(@Header("Authorization") String bearer, @Body DocumentInfo documentInfo);

    @GET("/api/document/{id}/wsKey")
    Call<AnswerBase<String>> getDocumentKey(@Header("Authorization") String bearer, @Path("id") Integer documentId);
}
