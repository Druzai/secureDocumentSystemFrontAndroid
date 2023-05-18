package ru.mirea.documenteditor.data.model.auth;


public class RegisterModel {

    private String username, password, passwordConfirm;
    private boolean isRegisterValid = false;

    public RegisterModel(String username, String password, String passwordConfirm) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public boolean getIsRegisterValid() {
        return isRegisterValid;
    }

    public void setIsRegisterValid(boolean registerValid) {
        isRegisterValid = registerValid;
    }
}
