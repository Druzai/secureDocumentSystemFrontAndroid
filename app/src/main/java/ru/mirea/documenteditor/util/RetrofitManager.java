package ru.mirea.documenteditor.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.documenteditor.data.service.AuthService;
import ru.mirea.documenteditor.data.service.CipherService;
import ru.mirea.documenteditor.data.service.DocumentService;
import ru.mirea.documenteditor.data.service.UserService;

public class RetrofitManager {
    private Retrofit retrofit;

    private AuthService authService;
    private CipherService cipherService;
    private UserService userService;
    private DocumentService documentService;

    private static RetrofitManager instance;

    public static RetrofitManager getInstance() {
        if (instance == null) {
            instance = new RetrofitManager();
        }
        return instance;
    }

    public void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authService = retrofit.create(AuthService.class);
        cipherService = retrofit.create(CipherService.class);
        userService = retrofit.create(UserService.class);
        documentService = retrofit.create(DocumentService.class);
    }

    public String makeHeaderBearer(String token) {
        return "Bearer " + token;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public CipherService getCipherService() {
        return cipherService;
    }

    public UserService getUserService() {
        return userService;
    }

    public DocumentService getDocumentService() {
        return documentService;
    }
}
