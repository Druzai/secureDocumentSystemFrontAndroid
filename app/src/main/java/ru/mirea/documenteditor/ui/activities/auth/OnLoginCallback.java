package ru.mirea.documenteditor.ui.activities.auth;

import ru.mirea.documenteditor.data.model.auth.LoginModel;

public interface OnLoginCallback {
    void onLoginCallback(LoginModel currentLoginModel);
}
