package ru.mirea.documenteditor.ui.activities.auth;

import ru.mirea.documenteditor.data.model.auth.RegisterModel;

public interface OnRegistrationCallback {
    void onRegisterCallback(RegisterModel currentRegisterModel);
}
